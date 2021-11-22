package net.tropicraft.core.common.block;

import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.world.level.block.RotatedPillarBlock;

import java.util.function.Supplier;

public final class TropicraftLogBlock extends RotatedPillarBlock{
    private final Supplier<RotatedPillarBlock> strippedBlock;

    public TropicraftLogBlock(Properties properties, Supplier<RotatedPillarBlock> strippedBlock) {
        super(properties);
        this.strippedBlock = strippedBlock;
    }
}
