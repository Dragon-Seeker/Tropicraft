package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FoliagePlacerType;

import java.util.Random;
import java.util.Set;

public final class CitrusFoliagePlacer extends FoliagePlacer {
    public static final Codec<CitrusFoliagePlacer> CODEC = RecordCodecBuilder.create((instance) -> {
        return func_242830_b(instance).apply(instance, CitrusFoliagePlacer::new);
    });

    public CitrusFoliagePlacer(FeatureSpread radius, FeatureSpread offset) {
        super(radius, offset);
    }

    @Override
    protected FoliagePlacerType<?> getPlacerType() {
        return TropicraftFoliagePlacers.CITRUS.get();
    }

    @Override
    protected void func_230372_a_(IWorldGenerationReader world, Random random, BaseTreeFeatureConfig config, int p_230372_4_, Foliage node, int p_230372_6_, int radius, Set<BlockPos> leaves, int p_230372_9_, MutableBoundingBox p_230372_10_) {
        this.func_236753_a_(world, random, config, node.func_236763_a_(), 1, leaves, 1, node.func_236765_c_(), p_230372_10_);
        this.func_236753_a_(world, random, config, node.func_236763_a_(), 2, leaves, 0, node.func_236765_c_(), p_230372_10_);

        if (node.func_236764_b_() == 1) {
            // Center leaf cluster, add another layer at the bottom
            this.func_236753_a_(world, random, config, node.func_236763_a_(), 3, leaves, -1, node.func_236765_c_(), p_230372_10_);
        }
    }

    @Override
    public int func_230374_a_(Random p_230374_1_, int p_230374_2_, BaseTreeFeatureConfig p_230374_3_) {
        return 0;
    }

    @Override
    protected boolean func_230373_a_(Random random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        return radius != 0 && dx == radius && dz == radius && random.nextInt(2) == 0;
    }
}
