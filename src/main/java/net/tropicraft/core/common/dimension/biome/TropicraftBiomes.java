package net.tropicraft.core.common.dimension.biome;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.Features;
import net.minecraft.data.worldgen.SurfaceBuilders;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.tropicraft.Constants;
import net.tropicraft.core.common.data.WorldgenDataConsumer;
import net.tropicraft.core.common.dimension.carver.TropicraftConfiguredCarvers;
import net.tropicraft.core.common.dimension.feature.TropicraftConfiguredFeatures;
import net.tropicraft.core.common.dimension.feature.TropicraftConfiguredStructures;
import net.tropicraft.core.common.dimension.surfacebuilders.TropicraftConfiguredSurfaceBuilders;
import net.tropicraft.core.common.entity.TropicraftEntities;

import java.util.function.Predicate;

public final class TropicraftBiomes {
    public static final int TROPICS_WATER_COLOR = 0x4eecdf;
    public static final int TROPICS_WATER_FOG_COLOR = 0x041f33;
    public static final int TROPICS_FOG_COLOR = 0xC0D8FF;
    public static final int RAINFOREST_FOG_COLOR = 0xbae6c3;
    public static final int TROPICS_SKY_COLOR = getSkyColor(0.8F);

    public static final ResourceKey<Biome> TROPICS_OCEAN = key("tropics_ocean");
    public static final ResourceKey<Biome> TROPICS = key("tropics");
    public static final ResourceKey<Biome> KELP_FOREST = key("kelp_forest");
    public static final ResourceKey<Biome> RAINFOREST_PLAINS = key("rainforest_plains");
    public static final ResourceKey<Biome> RAINFOREST_HILLS = key("rainforest_hills");
    public static final ResourceKey<Biome> RAINFOREST_MOUNTAINS = key("rainforest_mountains");
    public static final ResourceKey<Biome> BAMBOO_RAINFOREST = key("bamboo_rainforest");
    public static final ResourceKey<Biome> RAINFOREST_ISLAND_MOUNTAINS = key("rainforest_island_mountains");
    public static final ResourceKey<Biome> TROPICS_RIVER = key("tropics_river");
    public static final ResourceKey<Biome> TROPICS_BEACH = key("tropics_beach");
    public static final ResourceKey<Biome> MANGROVES = key("mangroves");
    public static final ResourceKey<Biome> OVERGROWN_MANGROVES = key("overgrown_mangroves");
    public static final ResourceKey<Biome> OSA_RAINFOREST = key("osa_rainforest");

    private static ResourceKey<Biome> key(String id) {
        return ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(Constants.MODID, id));
    }

    public final Biome tropics;
    public final Biome tropicsBeach;
    public final Biome rainforestPlains;
    public final Biome rainforestHills;
    public final Biome rainforestMountains;
    public final Biome bambooRainforest;
    public final Biome rainforestIslandMountains;

    public final Biome tropicsOcean;
    public final Biome kelpForest;

    public final Biome tropicsRiver;

    public final Biome mangroves;
    public final Biome overgrownMangroves;
    public final Biome osaRainforest;

    private final TropicraftConfiguredFeatures features;
    private final TropicraftConfiguredStructures structures;
    private final TropicraftConfiguredCarvers carvers;
    private final TropicraftConfiguredSurfaceBuilders surfaces;

    public TropicraftBiomes(WorldgenDataConsumer<Biome> worldgen, TropicraftConfiguredFeatures features, TropicraftConfiguredStructures structures, TropicraftConfiguredCarvers carvers, TropicraftConfiguredSurfaceBuilders surfaces) {
        this.features = features;
        this.structures = structures;
        this.carvers = carvers;
        this.surfaces = surfaces;

        this.tropics = worldgen.register(TROPICS, createTropics());
        this.tropicsBeach = worldgen.register(TROPICS_BEACH, createTropicsBeach());
        this.rainforestPlains = worldgen.register(RAINFOREST_PLAINS, createRainforest(0.25F, 0.1F));
        this.rainforestHills = worldgen.register(RAINFOREST_HILLS, createRainforest(0.45F, 0.425F));
        this.rainforestMountains = worldgen.register(RAINFOREST_MOUNTAINS, createRainforest(0.8F, 0.8F));
        this.bambooRainforest = worldgen.register(BAMBOO_RAINFOREST, createRainforest(0.25F, 0.25F, true));
        this.rainforestIslandMountains = worldgen.register(RAINFOREST_ISLAND_MOUNTAINS, createRainforest(0.1F, 1.2F));

        this.tropicsOcean = worldgen.register(TROPICS_OCEAN, createTropicsOcean());
        this.kelpForest = worldgen.register(KELP_FOREST, createKelpForest());

        this.tropicsRiver = worldgen.register(TROPICS_RIVER, createTropicsRiver());

        this.mangroves = worldgen.register(MANGROVES, createMangroves(false));
        this.overgrownMangroves = worldgen.register(OVERGROWN_MANGROVES, createMangroves(true));
        this.osaRainforest = worldgen.register(OSA_RAINFOREST, createOsaRainforest(0.25F, 0.1F));
    }


    public static void onBiomeLoad() {
        Predicate<BiomeSelectionContext> biomeSelectorBeach = BiomeSelectors.foundInOverworld().and(BiomeSelectors.categories(Biome.BiomeCategory.BEACH));
        Predicate<BiomeSelectionContext> biomeSelectorJungle = BiomeSelectors.foundInOverworld().and(BiomeSelectors.categories(Biome.BiomeCategory.JUNGLE));

        BiomeModifications.addFeature(biomeSelectorBeach,
                GenerationStep.Decoration.VEGETAL_DECORATION,
                TropicraftConfiguredFeatures.NORMAL_PALM_TREE_CONFIGURED_KEY);

        BiomeModifications.addFeature(biomeSelectorBeach,
                GenerationStep.Decoration.VEGETAL_DECORATION,
                TropicraftConfiguredFeatures.CURVED_PALM_TREE_CONFIGURED_KEY);

        BiomeModifications.addFeature(biomeSelectorBeach,
                GenerationStep.Decoration.VEGETAL_DECORATION,
                TropicraftConfiguredFeatures.LARGE_PALM_TREE_CONFIGURED_KEY);

        BiomeModifications.addFeature(biomeSelectorJungle,
                GenerationStep.Decoration.VEGETAL_DECORATION,
                TropicraftConfiguredFeatures.PINEAPPLE_PATCH_OVERWORLD_KEY);

    }

    private Biome createTropics() {
        BiomeGenerationSettings.Builder generation = defaultGeneration()
                .surfaceBuilder(surfaces.tropics);

        carvers.addLand(generation);

        features.addFruitTrees(generation);
        features.addPalmTrees(generation);
        features.addEih(generation);
        features.addPapaya(generation);
        features.addTropicsFlowers(generation);
        features.addPineapples(generation);
        features.addRegularSeagrass(generation);

        features.addTropicsGrass(generation);
        BiomeDefaultFeatures.addFerns(generation);
        BiomeDefaultFeatures.addSavannaGrass(generation);

        MobSpawnSettings.Builder spawns = defaultSpawns();
        spawns.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(TropicraftEntities.FAILGULL, 10, 5, 15));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.TROPI_BEE, 10, 4, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.COWKTAIL, 10, 4, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.TREE_FROG, 4, 4, 4));

        return new Biome.BiomeBuilder()
                .precipitation(Biome.Precipitation.RAIN)
                .depth(0.1F).scale(0.1F)
                .temperature(2.0F).downfall(1.5F)
                .biomeCategory(Biome.BiomeCategory.PLAINS)
                .generationSettings(generation.build())
                .mobSpawnSettings(spawns.build())
                .specialEffects(defaultAmbience(true).build())
                .build();
    }

    private Biome createTropicsBeach() {
        BiomeGenerationSettings.Builder generation = defaultGeneration()
                .surfaceBuilder(surfaces.sandy);

        features.addRegularSeagrass(generation);
        carvers.addUnderwater(generation);

        features.addPalmTrees(generation);
        features.addTropicsFlowers(generation);

        generation.addStructureStart(structures.koaVillage);

        MobSpawnSettings.Builder spawns = defaultSpawns();
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.FIDDLER_CRAB, 10, 1, 2));

        return new Biome.BiomeBuilder()
                .precipitation(Biome.Precipitation.RAIN)
                .depth(-0.1F).scale(0.1F)
                .temperature(1.5F).downfall(1.25F)
                .biomeCategory(Biome.BiomeCategory.BEACH)
                .generationSettings(generation.build())
                .mobSpawnSettings(spawns.build())
                .specialEffects(defaultAmbience(false).build())
                .build();
    }

    private Biome createOsaRainforest(float depth, float scale) {
        BiomeGenerationSettings.Builder generation = defaultGeneration()
                .surfaceBuilder(surfaces.osaRainforest);

        carvers.addLand(generation);

        features.addGoldenLeatherFern(generation);

        features.addTropicsGems(generation);
        features.addPleodendron(generation);
        features.addRainforestTrees(generation);
        features.addRegularSeagrass(generation);
        features.addPapaya(generation);

        features.addTropicsFlowers(generation);
        features.addPineapples(generation);

        features.addTropicsGrass(generation);
        BiomeDefaultFeatures.addFerns(generation);
        BiomeDefaultFeatures.addSavannaGrass(generation);

        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.coffeeBush);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.singleUndergrowth);

        features.addRainforestPlants(generation);

        MobSpawnSettings.Builder spawns = defaultSpawns();
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.OCELOT, 10, 1, 1));
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.PARROT, 10, 1, 2));
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TropicraftEntities.TREE_FROG, 25, 2, 5));
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TropicraftEntities.TROPI_SPIDER, 30, 1, 1));

        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.TAPIR, 15, 2, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.WHITE_LIPPED_PECCARY, 15, 6, 12));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.JAGUAR, 5, 1, 2));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.HUMMINGBIRD, 10, 3, 5));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.SPIDER_MONKEY, 15, 6, 8));

        return new Biome.BiomeBuilder()
                .precipitation(Biome.Precipitation.RAIN)
                .depth(depth).scale(scale)
                .temperature(1.5F).downfall(2.0F)
                .biomeCategory(Biome.BiomeCategory.JUNGLE)
                .generationSettings(generation.build())
                .mobSpawnSettings(spawns.build())
                .specialEffects(defaultAmbience(true).build())
                .build();
    }

    private Biome createRainforest(float depth, float scale) {
        return createRainforest(depth, scale, false);
    }

    private Biome createRainforest(float depth, float scale, boolean bamboo) {
        BiomeGenerationSettings.Builder generation = defaultGeneration()
                .surfaceBuilder(SurfaceBuilders.GRASS);

        carvers.addLand(generation);

        features.addTropicsGems(generation);
        features.addPleodendron(generation);
        features.addRainforestTrees(generation);
        features.addRegularSeagrass(generation);
        features.addPapaya(generation);

        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.rainforestFlowers);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.coffeeBush);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, features.undergrowth);

        generation.addStructureStart(structures.homeTree);

        BiomeDefaultFeatures.addJungleGrass(generation);
        BiomeDefaultFeatures.addLightBambooVegetation(generation);

        features.addRainforestPlants(generation);

        if (bamboo) {
            features.addBamboo(generation);
        }

        MobSpawnSettings.Builder spawns = defaultSpawns();
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.OCELOT, 10, 1, 1));
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.PARROT, 10, 1, 2));
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TropicraftEntities.TREE_FROG, 25, 2, 5));
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(TropicraftEntities.TROPI_SPIDER, 30, 1, 1));

        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.TAPIR, 15, 2, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.WHITE_LIPPED_PECCARY, 15, 2, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.JAGUAR, 5, 1, 2));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.HUMMINGBIRD, 10, 3, 5));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.SPIDER_MONKEY, 15, 5, 8));

        return new Biome.BiomeBuilder()
                .precipitation(Biome.Precipitation.RAIN)
                .depth(depth).scale(scale)
                .temperature(1.5F).downfall(2.0F)
                .biomeCategory(Biome.BiomeCategory.JUNGLE)
                .generationSettings(generation.build())
                .mobSpawnSettings(spawns.build())
                .specialEffects(defaultAmbience(true).build())
                .build();
    }

    private Biome createTropicsOcean() {
        BiomeGenerationSettings.Builder generation = defaultGeneration()
                .surfaceBuilder(surfaces.sandy);

        carvers.addUnderwater(generation);

        features.addTropicsMetals(generation);

        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.WARM_OCEAN_VEGETATION);

        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.SEAGRASS_WARM);
        features.addUndergroundSeagrass(generation);

        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.SEA_PICKLE);
        features.addUndergroundPickles(generation);

        MobSpawnSettings.Builder spawns = defaultSpawns();
        this.addOceanWaterCreatures(spawns);
        spawns.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(TropicraftEntities.FAILGULL, 15, 5, 10));

        return new Biome.BiomeBuilder()
                .precipitation(Biome.Precipitation.RAIN)
                .depth(-1.6F).scale(0.4F)
                .temperature(1.5F).downfall(1.25F)
                .biomeCategory(Biome.BiomeCategory.OCEAN)
                .generationSettings(generation.build())
                .mobSpawnSettings(spawns.build())
                .specialEffects(defaultAmbience(false).build())
                .build();
    }

    private Biome createKelpForest() {
        BiomeGenerationSettings.Builder generation = defaultGeneration()
                .surfaceBuilder(surfaces.sandy);

        carvers.addUnderwater(generation);

        // KELP!
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.KELP_COLD);

        features.addUndergroundSeagrass(generation);
        features.addUndergroundPickles(generation);

        MobSpawnSettings.Builder spawns = defaultSpawns();
        this.addOceanWaterCreatures(spawns);

        return new Biome.BiomeBuilder()
                .precipitation(Biome.Precipitation.RAIN)
                .depth(-1.5F).scale(0.3F)
                .temperature(1.5F).downfall(1.25F)
                .biomeCategory(Biome.BiomeCategory.OCEAN)
                .generationSettings(generation.build())
                .mobSpawnSettings(spawns.build())
                .specialEffects(defaultAmbience(false).build())
                .build();
    }

    private Biome createTropicsRiver() {
        BiomeGenerationSettings.Builder generation = defaultGeneration()
                .surfaceBuilder(surfaces.sandy);

        carvers.addLand(generation);

        features.addTropicsFlowers(generation);

        MobSpawnSettings.Builder spawns = defaultSpawns();
        addRiverWaterCreatures(spawns);

        return new Biome.BiomeBuilder()
                .precipitation(Biome.Precipitation.RAIN)
                .depth(-0.7F).scale(0.05F)
                .temperature(1.5F).downfall(1.25F)
                .biomeCategory(Biome.BiomeCategory.RIVER)
                .generationSettings(generation.build())
                .mobSpawnSettings(spawns.build())
                .specialEffects(defaultAmbience(false).build())
                .build();
    }

    // TODO: rebalance all spawns
    private Biome createMangroves(boolean overgrown) {
        BiomeGenerationSettings.Builder generation = defaultGeneration()
                .surfaceBuilder(surfaces.mangrove);

        carvers.addLand(generation);

        features.addMudDisks(generation);
        if(overgrown){
            features.addOvergrownGoldenLeatherFern(generation);
        }
        features.addGoldenLeatherFern(generation);
        features.addMangroveVegetation(generation, overgrown);
        features.addTropicsFlowers(generation);

        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.SEAGRASS_DEEP_WARM);
        features.addMangroveReeds(generation);

        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_GRASS_PLAIN);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, Features.PATCH_WATERLILLY);

        BiomeDefaultFeatures.addSavannaGrass(generation);

        MobSpawnSettings.Builder spawns = defaultSpawns();
        spawns.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(TropicraftEntities.FAILGULL, 5, 5, 15));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.TREE_FROG, 4, 4, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.TAPIR, 15, 2, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.WHITE_LIPPED_PECCARY, 15, 2, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.JAGUAR, 8, 1, 3));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.HUMMINGBIRD, 12, 3, 5));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.SPIDER_MONKEY, 15, 5, 8));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.BROWN_BASILISK_LIZARD, 10, 1, 3));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.GREEN_BASILISK_LIZARD, 10, 1, 3));

        addMangroveWaterCreatures(spawns);

        BiomeSpecialEffects.Builder ambience = defaultAmbience(true);
        ambience.waterColor(0x66C197).waterFogColor(0x0C3522);
        ambience.grassColorOverride(0x6FB21C);

        return new Biome.BiomeBuilder()
                .precipitation(Biome.Precipitation.RAIN)
                .depth(-0.25F).scale(-0.045F)
                .temperature(2.0F).downfall(1.5F)
                .biomeCategory(Biome.BiomeCategory.SWAMP)
                .generationSettings(generation.build())
                .mobSpawnSettings(spawns.build())
                .specialEffects(ambience.build())
                .build();
    }

    private void addOceanWaterCreatures(MobSpawnSettings.Builder spawns) {
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(TropicraftEntities.TROPICAL_FISH, 20, 4, 8));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(TropicraftEntities.STARFISH, 4, 1, 4));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(TropicraftEntities.SEAHORSE, 6, 6, 12));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(TropicraftEntities.SEA_URCHIN, 4, 1, 4));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(TropicraftEntities.MAN_O_WAR, 2, 1, 1));
        spawns.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(TropicraftEntities.MARLIN, 10, 1, 4));
        spawns.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(TropicraftEntities.CUBERA, 10, 2, 4));
        spawns.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(TropicraftEntities.EAGLE_RAY, 6, 1, 1));
        spawns.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(TropicraftEntities.SEA_TURTLE, 6, 3, 8));
        spawns.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(TropicraftEntities.DOLPHIN, 3, 4, 7));
        spawns.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(TropicraftEntities.HAMMERHEAD, 2, 1, 1));
    }

    private void addRiverWaterCreatures(MobSpawnSettings.Builder spawns) {
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.FIDDLER_CRAB, 20, 2, 6));
        spawns.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(TropicraftEntities.PIRANHA, 15, 1, 12));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(TropicraftEntities.RIVER_SARDINE, 20, 1, 8));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.SQUID, 8, 1, 4));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.COD, 4, 1, 5));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.SALMON, 4, 1, 5));
    }

    private void addMangroveWaterCreatures(MobSpawnSettings.Builder spawns) {
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.FIDDLER_CRAB, 20, 2, 6));
        spawns.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(TropicraftEntities.PIRANHA, 15, 1, 12));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(TropicraftEntities.RIVER_SARDINE, 20, 1, 8));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.COD, 4, 1, 5));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.SALMON, 4, 1, 5));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.TROPICAL_FISH, 12, 1, 5));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(TropicraftEntities.STARFISH, 4, 1, 4));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(TropicraftEntities.SEA_URCHIN, 4, 1, 4));
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(TropicraftEntities.SEAHORSE, 6, 6, 12));
        spawns.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(TropicraftEntities.HAMMERHEAD, 2, 1, 1));
        spawns.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SQUID, 8, 1, 4));
    }

    private BiomeGenerationSettings.Builder defaultGeneration() {
        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder();

        BiomeDefaultFeatures.addDefaultOverworldLandStructures(generation);
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(generation);

        return generation;
    }

    private MobSpawnSettings.Builder defaultSpawns() {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();

        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.PARROT, 15, 1, 2));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.V_MONKEY, 15, 1, 3));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.IGUANA, 15, 4, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.TROPI_CREEPER, 4, 1, 2));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.EIH, 5, 1, 1));

        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.TROPI_SKELLY, 8, 2, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(TropicraftEntities.TROPI_SPIDER, 8, 2, 2));

        return spawns;
    }

    private BiomeSpecialEffects.Builder defaultAmbience(boolean greenFog) {
        return new BiomeSpecialEffects.Builder()
                .fogColor(greenFog ? RAINFOREST_FOG_COLOR : TROPICS_FOG_COLOR)
                .skyColor(TROPICS_SKY_COLOR)
                .waterColor(TROPICS_WATER_COLOR)
                .waterFogColor(TROPICS_WATER_FOG_COLOR);
    }

    private static int getSkyColor(float temperature) {
        float shift = Mth.clamp(temperature / 3.0F, -1.0F, 1.0F);
        return Mth.hsvToRgb((224.0F / 360.0F) - shift * 0.05F, 0.5F + shift * 0.1F, 1.0F);
    }
}
