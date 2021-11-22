package net.tropicraft.core.common.dimension.carver;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.carver.CanyonCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.UnderwaterCanyonWorldCarver;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.tropicraft.core.common.block.TropicraftBlocks;

public class TropicsUnderwaterCanyonCarver extends UnderwaterCanyonWorldCarver {

    public TropicsUnderwaterCanyonCarver(Codec<CanyonCarverConfiguration> codec) {
        super(codec);
        this.replaceableBlocks = ImmutableSet.<Block>builder().addAll(this.replaceableBlocks)
                .add(TropicraftBlocks.CORAL_SAND)
                .add(TropicraftBlocks.FOAMY_SAND)
                .add(TropicraftBlocks.MINERAL_SAND)
                .add(TropicraftBlocks.PACKED_PURIFIED_SAND)
                .add(TropicraftBlocks.PURIFIED_SAND)
                .add(TropicraftBlocks.VOLCANIC_SAND)
                .add(TropicraftBlocks.MUD, TropicraftBlocks.MUD_WITH_PIANGUAS)
                .build();
    }
}
