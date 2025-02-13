package net.tropicraft.core.common.block;

import it.unimi.dsi.fastutil.objects.Reference2ByteMap;
import it.unimi.dsi.fastutil.objects.Reference2ByteOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public final class MangroveRootsBlock extends Block implements IWaterLoggable {
    private static final Reference2ByteMap<BlockState> STATE_TO_KEY = new Reference2ByteOpenHashMap<>();
    private static final VoxelShape[] SHAPE_TABLE = buildShapeTable();

    private static final int PIANGUA_GROW_CHANCE = 80;
    private static final int PIANGUA_RADIUS = 1;

    public static final BooleanProperty TALL = BooleanProperty.create("tall");
    public static final BooleanProperty GROUNDED = BooleanProperty.create("grounded");
    public static final EnumProperty<Connection> NORTH = EnumProperty.create("north", Connection.class);
    public static final EnumProperty<Connection> EAST = EnumProperty.create("east", Connection.class);
    public static final EnumProperty<Connection> SOUTH = EnumProperty.create("south", Connection.class);
    public static final EnumProperty<Connection> WEST = EnumProperty.create("west", Connection.class);

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final Direction[] DIRECTIONS = new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };
    public static final EnumProperty<Connection>[] CONNECTIONS = new EnumProperty[] { NORTH, EAST, SOUTH, WEST };

    public MangroveRootsBlock(Block.Properties properties) {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState()
                .with(TALL, true)
                .with(GROUNDED, false)
                .with(NORTH, Connection.NONE)
                .with(EAST, Connection.NONE)
                .with(SOUTH, Connection.NONE)
                .with(WEST, Connection.NONE)
                .with(WATERLOGGED, false)
        );
    }

    private static VoxelShape[] buildShapeTable() {
        VoxelShape[] table = new VoxelShape[1 << 5];

        for (int key = 0; key < table.length; key++) {
            boolean tall = (key & 1) != 0;
            boolean north = (key >> 1 & 1) != 0;
            boolean east = (key >> 2 & 1) != 0;
            boolean south = (key >> 3 & 1) != 0;
            boolean west = (key >> 4 & 1) != 0;
            table[key] = computeShapeFor(tall, north, east, south, west);
        }

        return table;
    }

    private static VoxelShape computeShapeFor(boolean tall, boolean north, boolean east, boolean south, boolean west) {
        double height = tall ? 16.0 : 10.0;

        VoxelShape shape = Block.makeCuboidShape(6.0, 0.0, 6.0, 10.0, height, 10.0);
        if (north) shape = VoxelShapes.or(shape, Block.makeCuboidShape(6.0, 0.0, 0.0, 10.0, height, 6.0));
        if (east) shape = VoxelShapes.or(shape, Block.makeCuboidShape(10.0, 0.0, 6.0, 16.0, height, 10.0));
        if (south) shape = VoxelShapes.or(shape, Block.makeCuboidShape(6.0, 0.0, 10.0, 10.0, height, 16.0));
        if (west) shape = VoxelShapes.or(shape, Block.makeCuboidShape(0.0, 0.0, 6.0, 6.0, height, 10.0));

        return shape;
    }

    private static int shapeKey(BlockState state) {
        return (state.get(TALL) ? 1 : 0)
                | (state.get(NORTH).exists() ? 1 << 1 : 0)
                | (state.get(EAST).exists() ? 1 << 2 : 0)
                | (state.get(SOUTH).exists() ? 1 << 3 : 0)
                | (state.get(WEST).exists() ? 1 << 4 : 0);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        byte key = STATE_TO_KEY.computeByteIfAbsent(state, MangroveRootsBlock::shapeKey);
        return SHAPE_TABLE[key];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader reader, BlockPos pos) {
        return super.getCollisionShape(state, reader, pos);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getConnectedState(context.getWorld(), context.getPos());
    }

    @Override
    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
        if (state.get(WATERLOGGED)) {
            world.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return this.getConnectedState(world, currentPos);
    }

    private BlockState getConnectedState(IWorldReader world, BlockPos pos) {
        BlockState state = this.getDefaultState()
                .with(WATERLOGGED, world.getFluidState(pos).getFluid() == Fluids.WATER)
                .with(GROUNDED, this.isGrounded(world, pos));

        state = state
                .with(NORTH, this.getConnectionFor(world, pos, Direction.NORTH))
                .with(EAST, this.getConnectionFor(world, pos, Direction.EAST))
                .with(SOUTH, this.getConnectionFor(world, pos, Direction.SOUTH))
                .with(WEST, this.getConnectionFor(world, pos, Direction.WEST));

        if (!this.isTall(state) && !this.canConnectUp(world, pos.up())) {
            state = state.with(TALL, false)
                    .with(NORTH, state.get(NORTH).shorten())
                    .with(EAST, state.get(EAST).shorten())
                    .with(SOUTH, state.get(SOUTH).shorten())
                    .with(WEST, state.get(WEST).shorten());
        }

        return state;
    }

    private Connection getConnectionFor(IWorldReader world, BlockPos pos, Direction direction) {
        BlockPos adjacentPos = pos.offset(direction);
        BlockState adjacentState = world.getBlockState(adjacentPos);

        if (this.canConnectTo(adjacentState, world, adjacentPos, direction)) {
            // don't add a connection if the block above us has that connection too
            if (world.getBlockState(pos.up()).matchesBlock(this) && this.canConnectTo(world, adjacentPos.up(), direction)) {
                return Connection.NONE;
            }

            if (adjacentState.matchesBlock(this)) {
                boolean tall = this.isAdjacentTall(world, adjacentPos, direction.getOpposite());
                return tall ? Connection.HIGH : Connection.LOW;
            } else {
                return Connection.HIGH;
            }
        }

        return Connection.NONE;
    }

    private boolean isAdjacentTall(IWorldReader world, BlockPos pos, Direction sourceDirection) {
        if (this.canConnectUp(world, pos.up())) {
            return true;
        }

        for (Direction direction : DIRECTIONS) {
            if (direction != sourceDirection && this.canConnectTo(world, pos.offset(direction), direction)) {
                return true;
            }
        }

        return false;
    }

    private boolean isTall(BlockState state) {
        int count = 0;
        if (state.get(NORTH).exists()) count++;
        if (state.get(EAST).exists()) count++;
        if (state.get(SOUTH).exists()) count++;
        if (state.get(WEST).exists()) count++;
        return count > 1;
    }

    private boolean canConnectTo(IBlockReader world, BlockPos pos, Direction direction) {
        return this.canConnectTo(world.getBlockState(pos), world, pos, direction);
    }

    private boolean canConnectTo(BlockState state, IBlockReader world, BlockPos pos, Direction direction) {
        return (state.matchesBlock(this) || state.isSolidSide(world, pos, direction)) && !FenceBlock.cannotAttach(state.getBlock());
    }

    private boolean canConnectUp(IWorldReader world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.matchesBlock(this) || Block.hasEnoughSolidSide(world, pos, Direction.DOWN);
    }

    private boolean isGrounded(IBlockReader world, BlockPos pos) {
        BlockPos groundPos = pos.down();
        return world.getBlockState(groundPos).isSolidSide(world, groundPos, Direction.UP);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return !state.get(WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        switch (rotation) {
            case CLOCKWISE_180: return state
                    .with(NORTH, state.get(SOUTH))
                    .with(EAST, state.get(WEST))
                    .with(SOUTH, state.get(NORTH))
                    .with(WEST, state.get(EAST));
            case COUNTERCLOCKWISE_90: return state
                    .with(NORTH, state.get(EAST))
                    .with(EAST, state.get(SOUTH))
                    .with(SOUTH, state.get(WEST))
                    .with(WEST, state.get(NORTH));
            case CLOCKWISE_90: return state
                    .with(NORTH, state.get(WEST))
                    .with(EAST, state.get(NORTH))
                    .with(SOUTH, state.get(EAST))
                    .with(WEST, state.get(SOUTH));
            default: return state;
        }
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        switch (mirror) {
            case LEFT_RIGHT: return state.with(NORTH, state.get(SOUTH)).with(SOUTH, state.get(NORTH));
            case FRONT_BACK: return state.with(EAST, state.get(WEST)).with(WEST, state.get(EAST));
            default: return super.mirror(state, mirror);
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(TALL, GROUNDED, NORTH, EAST, SOUTH, WEST, WATERLOGGED);
    }

    @Override
    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }

    @Override
    public boolean ticksRandomly(BlockState state) {
        return state.get(GROUNDED) && state.get(TALL);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (random.nextInt(PIANGUA_GROW_CHANCE) == 0) {
            this.tryGrowPianguas(world, pos, random);
        }
    }

    private void tryGrowPianguas(ServerWorld world, BlockPos pos, Random random) {
        BlockPos soilPos = pos.down();
        if (!world.getBlockState(soilPos).matchesBlock(TropicraftBlocks.MUD.get())) {
            return;
        }

        BlockPos growPos = soilPos.add(
                random.nextInt(PIANGUA_RADIUS * 2 + 1) - PIANGUA_RADIUS,
                -random.nextInt(PIANGUA_RADIUS + 1),
                random.nextInt(PIANGUA_RADIUS * 2 + 1) - PIANGUA_RADIUS
        );

        BlockState growIn = world.getBlockState(growPos);
        if (growIn.matchesBlock(TropicraftBlocks.MUD.get()) && !world.getBlockState(growPos.up()).isSolid()) {
            if (!this.hasNearPianguas(world, growPos)) {
                world.setBlockState(growPos, TropicraftBlocks.MUD_WITH_PIANGUAS.get().getDefaultState());
            }
        }
    }

    private boolean hasNearPianguas(ServerWorld world, BlockPos source) {
        Block mudWithPianguas = TropicraftBlocks.MUD_WITH_PIANGUAS.get();
        BlockPos minSpacingPos = source.add(-PIANGUA_RADIUS, -PIANGUA_RADIUS, -PIANGUA_RADIUS);
        BlockPos maxSpacingPos = source.add(PIANGUA_RADIUS, 0, PIANGUA_RADIUS);

        for (BlockPos pos : BlockPos.getAllInBoxMutable(minSpacingPos, maxSpacingPos)) {
            if (world.getBlockState(pos).matchesBlock(mudWithPianguas)) {
                return true;
            }
        }

        return false;
    }

    public enum Connection implements IStringSerializable {
        NONE("none"),
        HIGH("high"),
        LOW("low");

        private final String key;

        Connection(String key) {
            this.key = key;
        }

        public boolean exists() {
            return this != NONE;
        }

        @Override
        public String getString() {
            return this.key;
        }

        public Connection shorten() {
            return this == HIGH ? LOW : this;
        }
    }
}
