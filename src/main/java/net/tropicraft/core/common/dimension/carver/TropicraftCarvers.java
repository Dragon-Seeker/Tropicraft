package net.tropicraft.core.common.dimension.carver;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.carver.CanyonCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.tropicraft.Constants;

import java.util.function.Supplier;

public class TropicraftCarvers {

    //public static final DeferredRegister<WorldCarver<?>> CARVERS = DeferredRegister.create(ForgeRegistries.WORLD_CARVERS, Constants.MODID);

    public static final TropicsCaveCarver CAVE = register("cave", () -> new TropicsCaveCarver(CaveCarverConfiguration.CODEC));
    public static final TropicsCanyonCarver CANYON = register("canyon", () -> new TropicsCanyonCarver(CanyonCarverConfiguration.CODEC));

    public static final TropicsUnderwaterCaveCarver UNDERWATER_CAVE = register("underwater_cave", () -> new TropicsUnderwaterCaveCarver(CaveCarverConfiguration.CODEC));
    public static final TropicsUnderwaterCanyonCarver UNDERWATER_CANYON = register("underwater_canyon", () -> new TropicsUnderwaterCanyonCarver(CanyonCarverConfiguration.CODEC));

    private static <T extends WorldCarver> T register(String id, Supplier<T> carver){
        return (T) Registry.register(Registry.CARVER, new ResourceLocation(Constants.MODID, id), carver.get());
    }

    public static void init(){

    }
}
