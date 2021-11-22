package net.tropicraft.core.common.dimension.feature.block_placer;

import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.blockplacers.BlockPlacerType;

import java.util.function.Supplier;

public final class TropicraftBlockPlacerTypes {
    //public static final DeferredRegister<BlockPlacerType<?>> BLOCK_PLACER_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_PLACER_TYPES, Constants.MODID);

    public static final BlockPlacerType<?> HUGE_PLANT = register("huge_plant", () -> new BlockPlacerType<>(HugePlantBlockPlacer.CODEC));

    private static BlockPlacerType<?> register(String id, Supplier<BlockPlacerType<?>> sup) {
        return Registry.register(Registry.BLOCK_PLACER_TYPES, id, sup.get());
    }

    public static void init(){
    }
}
