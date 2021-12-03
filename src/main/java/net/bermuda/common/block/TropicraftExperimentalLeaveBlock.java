package net.bermuda.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Random;

public class TropicraftExperimentalLeaveBlock extends LeavesBlock {
    public static IntegerProperty EXTRADISTANCE;
    public static BooleanProperty PASTFIRSTCYCLE; //Property to check if there has even been a block update to prevent unwanted decay instantly

    public TropicraftExperimentalLeaveBlock(Properties settings) {
        super(settings);

        this.registerDefaultState(this.defaultBlockState().setValue(DISTANCE, 7).setValue(EXTRADISTANCE, 20).setValue(PASTFIRSTCYCLE, false));
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        world.setBlock(pos, updateDistanceFromLeave(state, world, pos), 3);
    }

    //Used to update diagonal or Indirect blocks to propagate the extra distance value for our leaves
    @Override
    public void updateIndirectNeighbourShapes(BlockState state, LevelAccessor world, BlockPos pos, int flags, int maxUpdateDepth) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        DiagonalDirections[] directions = DiagonalDirections.values();
        int LENGTH = directions.length;

        for (int index = 0; index < LENGTH; ++index) {
            DiagonalDirections direction = directions[index];
            mutable.setWithOffset(pos, direction.getX(), direction.getY(), direction.getZ());
            BlockState blockState = world.getBlockState(mutable);
            if (blockState != Blocks.AIR.defaultBlockState() && blockState.getBlock() == this) {
                BlockState blockState2 = updateShapeBlackList(blockState, Direction.UP, state, world, mutable, pos, logBlackListTest(direction));
                Block.updateOrDestroy(blockState, blockState2, world, mutable, flags, maxUpdateDepth);
            }
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) { //*
        //Checks if it is set Persistent but also checks for the custom leaf distance
        return !(Boolean)state.getValue(PERSISTENT) && (Integer)state.getValue(EXTRADISTANCE) == 20;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        //Checks for leaf's extra distance and also checks the first cycle value to prevent random decay to happen prematurely
        if (state.getValue(EXTRADISTANCE) == 20 && state.getValue(PASTFIRSTCYCLE)) {
            super.randomTick(state, world, pos, random);
        }
    }

//    @Override
//    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
//        int i = getDistanceFromLeave(neighborState, false) + 1;
//        if (i != 1 || state.getValue(EXTRADISTANCE) >= i) {
//            world.getBlockTicks().scheduleTick(pos, this, 1);
//        }
//
//        return state;
//    }

    public BlockState updateShapeBlackList(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos, boolean blackListActive) {
        int i = getDistanceFromLeave(neighborState, blackListActive) + 1;
        if (i != 1 || state.getValue(EXTRADISTANCE) >= i) {
            world.getBlockTicks().scheduleTick(pos, this, 1);
        }

        return state;
    }

    // Similar to base leaf block but with the diagonal directions
    private static BlockState updateDistanceFromLeave(BlockState state, LevelAccessor world, BlockPos pos) { //*
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        DiagonalDirections[] directionDiagonals = DiagonalDirections.values();

        int currentExtraDistance = 20; //*
        int length = directionDiagonals.length;

        for(int i = 0; i < length; ++i) {
            DiagonalDirections direction = directionDiagonals[i];
            mutable.setWithOffset(pos, direction.getX(), direction.getY(), direction.getZ());
            currentExtraDistance = Math.min(currentExtraDistance, getDistanceFromLeave(world.getBlockState(mutable), logBlackListTest(direction)) + direction.additionAmount);
            if (currentExtraDistance == 1) {
                break;
            }
        }

        return state.setValue(EXTRADISTANCE, currentExtraDistance).setValue(PASTFIRSTCYCLE, true);
    }

    private static int getDistanceFromLeave(BlockState state, boolean blackListBlock) {
        if (BlockTags.LOGS.contains(state.getBlock()) && !blackListBlock) {
            return 0;
        }

        else {
            return (state.getBlock() instanceof TropicraftExperimentalLeaveBlock) ? state.getValue(EXTRADISTANCE) : 20;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { //*
        builder.add(new Property[]{DISTANCE, PERSISTENT, EXTRADISTANCE, PASTFIRSTCYCLE});
    }

    static {
        EXTRADISTANCE = IntegerProperty.create("extradistance", 1, 20);
        PASTFIRSTCYCLE = BooleanProperty.create("pastfirstcycle");
    }

    private static boolean logBlackListTest(DiagonalDirections direction){
        return direction.getY() == -1 && !(direction.getX() == 0 && direction.getZ() == 0);
    }
}
