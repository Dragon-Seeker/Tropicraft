package net.tropicraft.core.common.dimension.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SeaPickleBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

public class UndergroundSeaPickleFeature extends Feature<NoneFeatureConfiguration> {
    public UndergroundSeaPickleFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        Random rand = context.random();
        BlockPos pos = context.origin();

        BlockState surface = world.getBlockState(pos.below());
        if (!surface.is(Blocks.STONE) && !surface.is(Blocks.DIRT)) {
            return false;
        }

        if (world.getBlockState(pos).is(Blocks.WATER) && world.getBlockState(pos.above()).is(Blocks.WATER)) {
            int count = rand.nextInt(rand.nextInt(4) + 1) + 1;
            if (surface.is(Blocks.DIRT)) {
                count = Math.min(count + rand.nextInt(2), 4);
            }

            BlockState pickle = Blocks.SEA_PICKLE.defaultBlockState().setValue(SeaPickleBlock.PICKLES, count);
            world.setBlock(pos, pickle, Constants.BlockFlags.BLOCK_UPDATE);
            return true;
        }

        return false;
    }
}
