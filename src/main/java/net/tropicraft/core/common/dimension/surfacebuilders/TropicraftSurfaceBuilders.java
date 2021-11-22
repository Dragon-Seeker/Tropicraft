package net.tropicraft.core.common.dimension.surfacebuilders;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;
import net.tropicraft.Constants;

import java.util.function.Supplier;

public class TropicraftSurfaceBuilders {

    //public static final DeferredRegister<SurfaceBuilder<?>> SURFACE_BUILDERS = DeferredRegister.create(ForgeRegistries.SURFACE_BUILDERS, Constants.MODID);

    public static final TropicsSurfaceBuilder TROPICS = register("tropics", () -> new TropicsSurfaceBuilder(TropicsSurfaceBuilder.Config.CODEC));
    public static final UnderwaterSurfaceBuilder UNDERWATER = register("underwater", () -> new UnderwaterSurfaceBuilder(UnderwaterSurfaceBuilder.Config.CODEC));
    public static final MangroveSurfaceBuilder MANGROVE = register("mangrove", () -> new MangroveSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));
    public static final OsaRainforestSurfaceBuilder OSA_RAINFOREST = register("osa_rainforest", () -> new OsaRainforestSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));

    private static <T extends SurfaceBuilder<?>> T register(final String name, final Supplier<T> sup) {
        return Registry.register(Registry.SURFACE_BUILDER, new ResourceLocation(Constants.MODID, name), sup.get());
    }

    public static void init(){

    }
}
