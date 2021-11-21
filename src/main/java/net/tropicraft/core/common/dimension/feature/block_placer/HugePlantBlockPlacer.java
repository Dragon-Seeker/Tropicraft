package net.tropicraft.core.common.dimension.feature.block_placer;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.blockplacers.BlockPlacer;
import net.minecraft.world.level.levelgen.feature.blockplacers.BlockPlacerType;
import net.tropicraft.core.common.block.huge_plant.HugePlantBlock;

import java.util.Random;

public final class HugePlantBlockPlacer extends BlockPlacer {
    public static final HugePlantBlockPlacer INSTANCE = new HugePlantBlockPlacer();
    public static final Codec<HugePlantBlockPlacer> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public void place(LevelAccessor world, BlockPos pos, BlockState state, Random random) {
        HugePlantBlock block = (HugePlantBlock) state.getBlock();
        block.placeAt(world, pos, Constants.BlockFlags.BLOCK_UPDATE);
    }

    @Override
    protected BlockPlacerType<?> type() {
        return TropicraftBlockPlacerTypes.HUGE_PLANT.get();
    }
}
