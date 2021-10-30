package net.tropicraft.core.mixin.worldgen;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Lifecycle;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.registry.*;
import net.minecraft.world.level.dimension.LevelStem;
import net.tropicraft.Constants;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.OptionalInt;

import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.RegistryReadOps;

@Mixin(RegistryReadOps.class)
public class WorldSettingsImportMixin {
    @Shadow
    @Final
    private RegistryAccess.RegistryHolder dynamicRegistries;

    /**
     * Add the tropicraft dimension to both new worlds and existing worlds when they get loaded.
     */
    @Inject(
            method = "decode(Lnet/minecraft/util/registry/SimpleRegistry;Lnet/minecraft/util/RegistryKey;Lcom/mojang/serialization/Codec;)Lcom/mojang/serialization/DataResult;",
            at = @At("HEAD")
    )
    @SuppressWarnings("unchecked")
    private void decode(MappedRegistry<?> registry, ResourceKey<?> registryKey, Codec<?> codec, CallbackInfoReturnable<DataResult<MappedRegistry<?>>> ci) {
        if (registryKey == Registry.LEVEL_STEM_REGISTRY && registry.get(TropicraftDimension.ID) == null) {
            this.addDimensions((MappedRegistry<LevelStem>) registry);
        }
    }

    private void addDimensions(MappedRegistry<LevelStem> registry) {
        LevelStem overworld = registry.get(LevelStem.OVERWORLD);
        if (overworld == null) {
            return;
        }

        // steal the seed from the overworld chunk generator.
        // not necessarily the world seed if a datapack changes it, but it's probably a safe bet.
        long seed = overworld.generator().strongholdSeed;

        LevelStem dimension = TropicraftDimension.createDimension(
                this.dynamicRegistries.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY),
                this.dynamicRegistries.registryOrThrow(Registry.BIOME_REGISTRY),
                this.dynamicRegistries.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY),
                seed
        );
        registry.registerOrOverride(OptionalInt.empty(), TropicraftDimension.DIMENSION, dimension, Lifecycle.stable());
    }

    /**
     * Remove the experimental world settings warning screen for tropicraft content.
     */
    @ModifyVariable(
            method = "createRegistry",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/util/registry/WorldSettingsImport$IResourceAccess;decode(Lcom/mojang/serialization/DynamicOps;Lnet/minecraft/util/RegistryKey;Lnet/minecraft/util/RegistryKey;Lcom/mojang/serialization/Decoder;)Lcom/mojang/serialization/DataResult;"
            ),
            ordinal = 1
    )
    private <E> DataResult<Pair<E, OptionalInt>> modifyDataResult(
            DataResult<Pair<E, OptionalInt>> result,
            ResourceKey<? extends Registry<E>> registryKey, WritableRegistry<E> registry, Codec<E> mapCodec, ResourceLocation id
    ) {
        if (id.getNamespace().equals(Constants.MODID)) {
            return result.setLifecycle(Lifecycle.stable());
        }
        return result;
    }
}
