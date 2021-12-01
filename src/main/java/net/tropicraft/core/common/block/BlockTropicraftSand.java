package net.tropicraft.core.common.block;

import net.api.forge.block.BlockExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.api.forge.ForgeConstants;

public class BlockTropicraftSand extends FallingBlock implements BlockExtension {
    public static final BooleanProperty UNDERWATER = BooleanProperty.create("underwater");

    private final int dustColor;

    public BlockTropicraftSand(final Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(UNDERWATER, false));
        this.dustColor = this.defaultMaterialColor().col | 0xFF000000;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(UNDERWATER);
    }

    public boolean canSustainPlant(BlockState state) {
        return state.is(Blocks.CACTUS) || state.is(Blocks.SUGAR_CANE) || state.is(TropicraftBlocks.PALM_SAPLING);
    }

    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext context) {
        final FluidState upState = context.getLevel().getFluidState(context.getClickedPos().above());
        boolean waterAbove = false;
        if (!upState.isEmpty()) {
            waterAbove = true;
        }
        return this.defaultBlockState().setValue(UNDERWATER, waterAbove);
    }

    @Override
    @Deprecated
    public void neighborChanged(final BlockState state, final Level world, final BlockPos pos, final Block block, final BlockPos pos2, boolean isMoving) {
        final FluidState upState = world.getFluidState(pos.above());
        boolean underwater = upState.getType().isSame(Fluids.WATER);
        if (underwater != state.getValue(UNDERWATER)) {
            world.setBlock(pos, state.setValue(UNDERWATER, underwater), ForgeConstants.BlockFlags.BLOCK_UPDATE);
        }
        super.neighborChanged(state, world, pos, block, pos2, isMoving);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public int getDustColor(BlockState state, BlockGetter reader, BlockPos pos) {
        return this.dustColor;
    }
}
