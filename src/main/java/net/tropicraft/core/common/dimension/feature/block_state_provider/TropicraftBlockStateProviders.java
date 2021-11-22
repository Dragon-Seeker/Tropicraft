package net.tropicraft.core.common.dimension.feature.block_state_provider;

import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;

import java.util.function.Supplier;

public final class TropicraftBlockStateProviders {
    //public static final DeferredRegister<BlockStateProviderType<?>> BLOCK_STATE_PROVIDERS = DeferredRegister.create(ForgeRegistries.BLOCK_STATE_PROVIDER_TYPES, Constants.MODID);

    public static final BlockStateProviderType<?> NOISE_FROM_TAG = register("noise_from_tag", () -> new BlockStateProviderType(NoiseFromTagBlockStateProvider.CODEC));

    private static BlockStateProviderType<?> register(String id, Supplier<BlockStateProviderType<?>> sup) {
        return Registry.register(Registry.BLOCKSTATE_PROVIDER_TYPES, id, sup.get());
    }

    public static void init(){
    }
}
