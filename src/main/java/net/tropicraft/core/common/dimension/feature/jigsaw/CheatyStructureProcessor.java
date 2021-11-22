package net.tropicraft.core.common.dimension.feature.jigsaw;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraftforge.common.util.Constants;

public abstract class CheatyStructureProcessor extends StructurePassProcessor {
    protected boolean isAirOrWater(LevelReader worldReaderIn, BlockPos pos) {
        return worldReaderIn.isEmptyBlock(pos) || worldReaderIn.getBlockState(pos).getBlock() == Blocks.WATER;
    }

    protected boolean setBlockState(LevelReader world, BlockPos pos, BlockState state) {
        if (world instanceof LevelAccessor) {
            return ((LevelAccessor) world).setBlock(pos, state, Constants.BlockFlags.NO_RERENDER | Constants.BlockFlags.UPDATE_NEIGHBORS);
        }
        return false;
    }
}
