package net.tropicraft.core.common.dimension.feature;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.DoublePlantBlockPlacer;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.placement.*;
import net.minecraftforge.fml.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.data.WorldgenDataConsumer;
import net.tropicraft.core.common.dimension.feature.block_placer.HugePlantBlockPlacer;
import net.tropicraft.core.common.dimension.feature.block_state_provider.NoiseFromTagBlockStateProvider;
import net.tropicraft.core.common.dimension.feature.config.HomeTreeBranchConfig;
import net.tropicraft.core.common.dimension.feature.config.RainforestVinesConfig;
import net.tropicraft.core.common.dimension.feature.tree.CitrusFoliagePlacer;
import net.tropicraft.core.common.dimension.feature.tree.CitrusTrunkPlacer;
import net.tropicraft.core.common.dimension.feature.tree.PleodendronFoliagePlacer;
import net.tropicraft.core.common.dimension.feature.tree.PleodendronTrunkPlacer;
import net.tropicraft.core.common.dimension.feature.tree.mangrove.*;

import java.util.Arrays;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public final class TropicraftConfiguredFeatures {
    public final ConfiguredFeature<?, ?> grapefruitTree;
    public final ConfiguredFeature<?, ?> orangeTree;
    public final ConfiguredFeature<?, ?> lemonTree;
    public final ConfiguredFeature<?, ?> limeTree;
    public final ConfiguredFeature<?, ?> normalPalmTree;
    public final ConfiguredFeature<?, ?> curvedPalmTree;
    public final ConfiguredFeature<?, ?> largePalmTree;
    public final ConfiguredFeature<?, ?> rainforestUpTree;
    public final ConfiguredFeature<?, ?> rainforestSmallTualung;
    public final ConfiguredFeature<?, ?> rainforestLargeTualung;
    public final ConfiguredFeature<?, ?> rainforestTallTree;
    public final ConfiguredFeature<?, ?> rainforestVines;
    public final ConfiguredFeature<?, ?> eih;
    public final ConfiguredFeature<?, ?> tropicsGrass;
    public final ConfiguredFeature<?, ?> bamboo;

    public final ConfiguredFeature<?, ?> redMangroveShort;
    public final ConfiguredFeature<?, ?> redMangroveSmall;
    public final ConfiguredFeature<?, ?> redMangrove;

    public final ConfiguredFeature<?, ?> blackMangrove;
    public final ConfiguredFeature<?, ?> tallMangrove;
    public final ConfiguredFeature<?, ?> teaMangrove;
    public final ConfiguredFeature<?, ?> lightMangroves;

    public final ConfiguredFeature<?, ?> mangroves;

    public final ConfiguredFeature<?, ?> mangroveVegetation;

    public final ConfiguredFeature<?, ?> mudDisk;

    public final ConfiguredFeature<?, ?> smallGoldenLeatherFern;
    public final ConfiguredFeature<?, ?> tallGoldenLeatherFern;
    public final ConfiguredFeature<?, ?> hugeGoldenLeatherFern;

    public final ConfiguredFeature<?, ?> pleodendron;

    public final ConfiguredFeature<?, ?> pineapplePatch;
    public final ConfiguredFeature<?, ?> tropicsFlowers;
    public final ConfiguredFeature<?, ?> rainforestFlowers;
    public final ConfiguredFeature<?, ?> irisFlowers;

    public final ConfiguredFeature<?, ?> coffeeBush;
    public final ConfiguredFeature<?, ?> undergrowth;

    public final ConfiguredFeature<?, ?> seagrass;
    public final ConfiguredFeature<?, ?> undergroundSeagrassOnStone;
    public final ConfiguredFeature<?, ?> undergroundSeagrassOnDirt;
    public final ConfiguredFeature<?, ?> undergroundSeaPickles;

    public final ConfiguredFeature<?, ?> mangroveReeds;

    public final ConfiguredFeature<?, ?> azurite;
    public final ConfiguredFeature<?, ?> eudialyte;
    public final ConfiguredFeature<?, ?> zircon;
    public final ConfiguredFeature<?, ?> manganese;
    public final ConfiguredFeature<?, ?> shaka;

    public final ConfiguredFeature<?, ?> homeTreeBranchSouth;
    public final ConfiguredFeature<?, ?> homeTreeBranchSouthExact;
    public final ConfiguredFeature<?, ?> homeTreeBranchSouthEast;
    public final ConfiguredFeature<?, ?> homeTreeBranchSouthEastExact;
    public final ConfiguredFeature<?, ?> homeTreeBranchEast;
    public final ConfiguredFeature<?, ?> homeTreeBranchEastExact;
    public final ConfiguredFeature<?, ?> homeTreeBranchNorthEast;
    public final ConfiguredFeature<?, ?> homeTreeBranchNorthEastExact;
    public final ConfiguredFeature<?, ?> homeTreeBranchNorth;
    public final ConfiguredFeature<?, ?> homeTreeBranchNorthExact;
    public final ConfiguredFeature<?, ?> homeTreeBranchNorthWest;
    public final ConfiguredFeature<?, ?> homeTreeBranchNorthWestExact;
    public final ConfiguredFeature<?, ?> homeTreeBranchWest;
    public final ConfiguredFeature<?, ?> homeTreeBranchWestExact;
    public final ConfiguredFeature<?, ?> homeTreeBranchSouthWest;
    public final ConfiguredFeature<?, ?> homeTreeBranchSouthWestExact;

    public TropicraftConfiguredFeatures(WorldgenDataConsumer<? extends ConfiguredFeature<?, ?>> worldgen) {
        Register features = new Register(worldgen);

        this.grapefruitTree = features.fruitTree("grapefruit_tree", TropicraftBlocks.GRAPEFRUIT_SAPLING, TropicraftBlocks.GRAPEFRUIT_LEAVES);
        this.orangeTree = features.fruitTree("orange_tree", TropicraftBlocks.ORANGE_SAPLING, TropicraftBlocks.ORANGE_LEAVES);
        this.lemonTree = features.fruitTree("lemon_tree", TropicraftBlocks.LEMON_SAPLING, TropicraftBlocks.LEMON_LEAVES);
        this.limeTree = features.fruitTree("lime_tree", TropicraftBlocks.LIME_SAPLING, TropicraftBlocks.LIME_LEAVES);

        this.normalPalmTree = features.sparseTree("normal_palm_tree", TropicraftFeatures.NORMAL_PALM_TREE, IFeatureConfig.NONE, 0.2F);
        this.curvedPalmTree = features.sparseTree("curved_palm_tree", TropicraftFeatures.CURVED_PALM_TREE, IFeatureConfig.NONE, 0.2F);
        this.largePalmTree = features.sparseTree("large_palm_tree", TropicraftFeatures.LARGE_PALM_TREE, IFeatureConfig.NONE, 0.2F);

        this.rainforestUpTree = features.sparseTree("rainforest_up_tree", TropicraftFeatures.UP_TREE, IFeatureConfig.NONE, 0.2F);
        this.rainforestSmallTualung = features.sparseTree("rainforest_small_tualung", TropicraftFeatures.SMALL_TUALUNG, IFeatureConfig.NONE, 0.3F);
        this.rainforestLargeTualung = features.sparseTree("rainforest_large_tualung", TropicraftFeatures.LARGE_TUALUNG, IFeatureConfig.NONE, 0.4F);
        this.rainforestTallTree = features.sparseTree("rainforest_tall_tree", TropicraftFeatures.TALL_TREE, IFeatureConfig.NONE, 0.5F);
        this.rainforestVines = features.register("rainforest_vines", TropicraftFeatures.VINES,
                f -> f.configured(new RainforestVinesConfig()).squared().count(50)
        );

        this.smallGoldenLeatherFern = features.register("small_golden_leather_fern", Feature.RANDOM_PATCH, feature -> {
            SimpleBlockStateProvider state = new SimpleBlockStateProvider(TropicraftBlocks.GOLDEN_LEATHER_FERN.get().defaultBlockState());
            return feature.configured(new BlockClusterFeatureConfig.Builder(state, SimpleBlockPlacer.INSTANCE)
                    .tries(12)
                    .build()
            ).decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE);
        });

        this.tallGoldenLeatherFern = features.register("tall_golden_leather_fern", Feature.RANDOM_PATCH, feature -> {
            SimpleBlockStateProvider state = new SimpleBlockStateProvider(TropicraftBlocks.TALL_GOLDEN_LEATHER_FERN.get().defaultBlockState());
            return feature.configured(new BlockClusterFeatureConfig.Builder(state, DoublePlantBlockPlacer.INSTANCE)
                    .tries(6)
                    .build()
            ).decorated(Features.Placements.ADD_32).decorated(Features.Placements.HEIGHTMAP_SQUARE);
        });

        this.hugeGoldenLeatherFern = features.register("huge_golden_leather_fern", Feature.RANDOM_PATCH, feature -> {
            SimpleBlockStateProvider state = new SimpleBlockStateProvider(TropicraftBlocks.LARGE_GOLDEN_LEATHER_FERN.get().defaultBlockState());
            return feature.configured(new BlockClusterFeatureConfig.Builder(state, HugePlantBlockPlacer.INSTANCE)
                    .tries(3)
                    .build()
            ).decorated(Features.Placements.ADD_32).decorated(Features.Placements.HEIGHTMAP_SQUARE);
        });

        this.pleodendron = features.tree("pleodendron",
                new BaseTreeFeatureConfig.Builder(
                        new SimpleBlockStateProvider(Blocks.JUNGLE_LOG.defaultBlockState()),
                        new SimpleBlockStateProvider(Blocks.JUNGLE_LEAVES.defaultBlockState()),
                        new PleodendronFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(0), 1),
                        new PleodendronTrunkPlacer(10, 8, 0),
                        new TwoLayerFeature(0, 0, 0, OptionalInt.of(4))
                ).build(),
                0, 0.05f, 1);

        FoliagePlacer mangroveFoliage = new MangroveFoliagePlacer(FeatureSpread.fixed(0), FeatureSpread.fixed(0));
        BlockStateProvider redMangroveLog = new SimpleBlockStateProvider(TropicraftBlocks.RED_MANGROVE_LOG.get().defaultBlockState());
        BlockStateProvider lightMangroveLog = new SimpleBlockStateProvider(TropicraftBlocks.LIGHT_MANGROVE_LOG.get().defaultBlockState());
        BlockStateProvider blackMangroveLog = new SimpleBlockStateProvider(TropicraftBlocks.BLACK_MANGROVE_LOG.get().defaultBlockState());
        Block redMangroveRoots = TropicraftBlocks.RED_MANGROVE_ROOTS.get();
        Block lightMangroveRoots = TropicraftBlocks.LIGHT_MANGROVE_ROOTS.get();
        Block blackMangroveRoots = TropicraftBlocks.BLACK_MANGROVE_ROOTS.get();

        BlockStateProvider redMangroveLeaves = new SimpleBlockStateProvider(TropicraftBlocks.RED_MANGROVE_LEAVES.get().defaultBlockState());
        BlockStateProvider tallMangroveLeaves = new SimpleBlockStateProvider(TropicraftBlocks.TALL_MANGROVE_LEAVES.get().defaultBlockState());
        BlockStateProvider teaMangroveLeaves = new SimpleBlockStateProvider(TropicraftBlocks.TEA_MANGROVE_LEAVES.get().defaultBlockState());
        BlockStateProvider blackMangroveLeaves = new SimpleBlockStateProvider(TropicraftBlocks.BLACK_MANGROVE_LEAVES.get().defaultBlockState());

        TwoLayerFeature mangroveMinimumSize = new TwoLayerFeature(0, 0, 0, OptionalInt.of(4));

        MangroveTrunkPlacer redMangroveTrunk = new MangroveTrunkPlacer(3, 3, 0, redMangroveRoots, true, false);
        this.redMangroveShort = features.mangrove("red_mangrove_short",
                new BaseTreeFeatureConfig.Builder(redMangroveLog, redMangroveLeaves, mangroveFoliage, redMangroveTrunk, mangroveMinimumSize)
                        .decorators(ImmutableList.of(Features.Placements.BEEHIVE_002, PianguasTreeDecorator.REGULAR))
                        .maxWaterDepth(1).build()
        );
        this.redMangroveSmall = features.mangrove("red_mangrove_small",
                new BaseTreeFeatureConfig.Builder(
                        redMangroveLog, redMangroveLeaves,
                        new SmallMangroveFoliagePlacer(FeatureSpread.fixed(0), FeatureSpread.fixed(0)),
                        new SmallMangroveTrunkPlacer(2, 1, 0, redMangroveRoots),
                        mangroveMinimumSize
                ).decorators(ImmutableList.of(Features.Placements.BEEHIVE_002, PianguasTreeDecorator.SMALL)).build()
        );
        this.redMangrove = features.random("red_mangrove", this.redMangroveShort, this.redMangroveSmall);

        this.tallMangrove = features.mangrove("tall_mangrove",
                new BaseTreeFeatureConfig.Builder(
                        lightMangroveLog, tallMangroveLeaves,
                        mangroveFoliage,
                        new MangroveTrunkPlacer(7, 4, 2, lightMangroveRoots, false, false),
                        mangroveMinimumSize
                ).decorators(ImmutableList.of(Features.Placements.BEEHIVE_002, PianguasTreeDecorator.REGULAR)).maxWaterDepth(2).build()
        );

        PneumatophoresTreeDecorator teaMangrovePneumatophores = new PneumatophoresTreeDecorator(lightMangroveRoots, 2, 6, 4);
        this.teaMangrove = features.mangrove("tea_mangrove",
                new BaseTreeFeatureConfig.Builder(
                        lightMangroveLog, teaMangroveLeaves,
                        mangroveFoliage,
                        new MangroveTrunkPlacer(5, 3, 0, lightMangroveRoots, false, true),
                        mangroveMinimumSize
                ).decorators(ImmutableList.of(Features.Placements.BEEHIVE_002, PianguasTreeDecorator.REGULAR, teaMangrovePneumatophores)).maxWaterDepth(1).build()
        );

        PneumatophoresTreeDecorator blackMangrovePneumatophores = new PneumatophoresTreeDecorator(blackMangroveRoots, 8, 16, 6);
        this.blackMangrove = features.mangrove("black_mangrove",
                new BaseTreeFeatureConfig.Builder(
                        blackMangroveLog, blackMangroveLeaves,
                        mangroveFoliage,
                        new MangroveTrunkPlacer(4, 3, 0, blackMangroveRoots, true, false),
                        mangroveMinimumSize
                ).decorators(ImmutableList.of(Features.Placements.BEEHIVE_002, PianguasTreeDecorator.REGULAR, blackMangrovePneumatophores)).maxWaterDepth(1).build()
        );
        this.lightMangroves = features.random("light_mangroves", this.tallMangrove, this.teaMangrove, this.blackMangrove);

        this.mangroves = features.random("mangroves", this.redMangrove, this.lightMangroves);

        this.mangroveVegetation = features.register("mangrove_vegetation", this.mangroves
                    .decorated(Features.Placements.HEIGHTMAP_SQUARE)
                    .decorated(Placement.COUNT_NOISE_BIASED.configured(new TopSolidWithNoiseConfig(7, 200.0, 1.5)))
        );

        this.mudDisk = features.register("mud_disk", Feature.DISK, feature -> feature
                .configured(new SphereReplaceConfig(
                        TropicraftBlocks.MUD.get().defaultBlockState(),
                        FeatureSpread.of(2, 4),
                        2,
                        ImmutableList.of(Blocks.DIRT.defaultBlockState(), Blocks.GRASS_BLOCK.defaultBlockState())
                ))
                .decorated(Features.Placements.TOP_SOLID_HEIGHTMAP_SQUARE).count(3)
        );

        this.eih = features.noConfig("eih", TropicraftFeatures.EIH,
                f -> f.decorated(Features.Placements.HEIGHTMAP_SQUARE)
                        .decorated(Placement.COUNT_EXTRA.configured(new AtSurfaceWithExtraConfig(0, 0.01F, 1)))
        );

        this.tropicsGrass = features.register("tropics_grass", Feature.RANDOM_PATCH,
                f -> f.configured(Features.Configs.JUNGLE_GRASS_CONFIG).decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).count(10));

        this.bamboo = features.register("bamboo", Feature.BAMBOO,
                f -> f.configured(new ProbabilityConfig(0.15F))
                        .decorated(Features.Placements.HEIGHTMAP_WORLD_SURFACE)
                        .squared()
                        .decorated(Placement.COUNT_NOISE_BIASED.configured(new TopSolidWithNoiseConfig(70, 140.0D, 0.3D)))
        );

        this.pineapplePatch = features.register("pineapple_patch", Feature.RANDOM_PATCH, feature -> {
            SimpleBlockStateProvider state = new SimpleBlockStateProvider(TropicraftBlocks.PINEAPPLE.get().defaultBlockState());
            return feature.configured(new BlockClusterFeatureConfig.Builder(state, new DoublePlantBlockPlacer())
                    .tries(64)
                    .noProjection()
                    .build()
            ).decorated(Features.Placements.ADD_32).decorated(Features.Placements.HEIGHTMAP_SQUARE);
        });
        this.tropicsFlowers = features.register("tropics_flowers", Feature.FLOWER, feature -> {
            BlockStateProvider stateProvider = new NoiseFromTagBlockStateProvider(TropicraftTags.Blocks.TROPICS_FLOWERS);
            BlockClusterFeatureConfig config = new BlockClusterFeatureConfig.Builder(stateProvider, SimpleBlockPlacer.INSTANCE).tries(64).build();
            return feature.configured(config).decorated(Features.Placements.ADD_32.decorated(Features.Placements.HEIGHTMAP_SQUARE).count(12));
        });
        this.rainforestFlowers = features.register("rainforest_flowers", Feature.FLOWER, feature -> {
            BlockStateProvider stateProvider = new NoiseFromTagBlockStateProvider(TropicraftTags.Blocks.RAINFOREST_FLOWERS);
            BlockClusterFeatureConfig config = new BlockClusterFeatureConfig.Builder(stateProvider, SimpleBlockPlacer.INSTANCE).tries(64).noProjection().build();
            return feature.configured(config).decorated(Features.Placements.ADD_32.decorated(Features.Placements.HEIGHTMAP_SQUARE).count(4));
        });
        this.irisFlowers = features.register("iris_flowers", Feature.RANDOM_PATCH, feature -> {
            BlockStateProvider stateProvider = new SimpleBlockStateProvider(TropicraftBlocks.IRIS.get().defaultBlockState());
            BlockClusterFeatureConfig config = new BlockClusterFeatureConfig.Builder(stateProvider, new DoublePlantBlockPlacer()).tries(64).noProjection().build();
            return feature.configured(config).decorated(Features.Placements.ADD_32.decorated(Features.Placements.HEIGHTMAP_SQUARE).count(10));
        });

        this.coffeeBush = features.noConfig("coffee_bush", TropicraftFeatures.COFFEE_BUSH, feature -> {
            return feature.decorated(Features.Placements.ADD_32.decorated(Features.Placements.HEIGHTMAP_SQUARE).count(5));
        });
        this.undergrowth = features.noConfig("undergrowth", TropicraftFeatures.UNDERGROWTH, feature -> {
            return feature.decorated(Features.Placements.ADD_32.decorated(Features.Placements.HEIGHTMAP_SQUARE).count(100));
        });

        this.seagrass = features.register("seagrass", Feature.SEAGRASS, feature -> {
            return feature.configured(new ProbabilityConfig(0.3f)).count(48).decorated(Features.Placements.TOP_SOLID_HEIGHTMAP_SQUARE).count(3);
        });

        this.undergroundSeagrassOnStone = features.register("underground_seagrass_on_stone", Feature.SIMPLE_BLOCK, feature -> {
            BlockWithContextConfig config = new BlockWithContextConfig(
                    Blocks.SEAGRASS.defaultBlockState(),
                    ImmutableList.of(Blocks.STONE.defaultBlockState()),
                    ImmutableList.of(Blocks.WATER.defaultBlockState()),
                    ImmutableList.of(Blocks.WATER.defaultBlockState())
            );
            return feature.configured(config).decorated(Placement.CARVING_MASK.configured(new CaveEdgeConfig(GenerationStage.Carving.LIQUID, 0.1F)));
        });
        this.undergroundSeagrassOnDirt = features.register("underground_seagrass_on_dirt", Feature.SIMPLE_BLOCK, feature -> {
            BlockWithContextConfig config = new BlockWithContextConfig(
                    Blocks.SEAGRASS.defaultBlockState(),
                    ImmutableList.of(Blocks.DIRT.defaultBlockState()),
                    ImmutableList.of(Blocks.WATER.defaultBlockState()),
                    ImmutableList.of(Blocks.WATER.defaultBlockState())
            );
            return feature.configured(config).decorated(Placement.CARVING_MASK.configured(new CaveEdgeConfig(GenerationStage.Carving.LIQUID, 0.5F)));
        });
        this.undergroundSeaPickles = features.noConfig("underground_sea_pickles", TropicraftFeatures.UNDERGROUND_SEA_PICKLE, feature -> {
            return feature.decorated(Placement.CARVING_MASK.configured(new CaveEdgeConfig(GenerationStage.Carving.LIQUID, 0.05F)));
        });

        this.mangroveReeds = features.noConfig("mangrove_reeds", TropicraftFeatures.REEDS, feature -> {
            return feature.decorated(Features.Placements.TOP_SOLID_HEIGHTMAP_SQUARE).count(2);
        });

        this.azurite = features.register("azurite", Feature.ORE, f -> {
            return f.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, TropicraftBlocks.AZURITE_ORE.get().defaultBlockState(), 8))
                    .decorated(Placement.RANGE.configured(new TopSolidRangeConfig(100, 0, 128)))
                    .squared().count(3);
        });
        this.eudialyte = features.register("eudialyte", Feature.ORE, f -> {
            return f.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, TropicraftBlocks.EUDIALYTE_ORE.get().defaultBlockState(), 12))
                    .decorated(Placement.RANGE.configured(new TopSolidRangeConfig(100, 0, 128)))
                    .squared().count(10);
        });
        this.zircon = features.register("zircon", Feature.ORE, f -> {
            return f.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, TropicraftBlocks.ZIRCON_ORE.get().defaultBlockState(), 14))
                    .decorated(Placement.RANGE.configured(new TopSolidRangeConfig(100, 0, 128)))
                    .squared().count(15);
        });
        this.manganese = features.register("manganese", Feature.ORE, f -> {
            return f.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, TropicraftBlocks.MANGANESE_ORE.get().defaultBlockState(), 10))
                    .decorated(Placement.RANGE.configured(new TopSolidRangeConfig(32, 0, 32)))
                    .squared().count(8);
        });
        this.shaka = features.register("shaka", Feature.ORE, f -> {
            return f.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, TropicraftBlocks.SHAKA_ORE.get().defaultBlockState(), 8))
                    .decorated(Placement.RANGE.configured(new TopSolidRangeConfig(0, 0, 32)))
                    .squared().count(6);
        });

        // 0 = south
        // 90 = east
        // 180 = north
        // 270 = west
        this.homeTreeBranchSouth = features.homeTreeBranch("home_tree/branch/south", -30, 30);
        this.homeTreeBranchSouthExact = features.homeTreeBranch("home_tree/branch/south_exact", 0, 0);
        this.homeTreeBranchSouthEast = features.homeTreeBranch("home_tree/branch/southeast", 30, 60);
        this.homeTreeBranchSouthEastExact = features.homeTreeBranch("home_tree/branch/southeast_exact", 45, 45);
        this.homeTreeBranchEast = features.homeTreeBranch("home_tree/branch/east", 60, 120);
        this.homeTreeBranchEastExact = features.homeTreeBranch("home_tree/branch/east_exact", 90, 90);
        this.homeTreeBranchNorthEast = features.homeTreeBranch("home_tree/branch/northeast", 120, 150);
        this.homeTreeBranchNorthEastExact = features.homeTreeBranch("home_tree/branch/northeast_exact", 135, 135);
        this.homeTreeBranchNorth = features.homeTreeBranch("home_tree/branch/north", 150, 210);
        this.homeTreeBranchNorthExact = features.homeTreeBranch("home_tree/branch/north_exact", 180, 180);
        this.homeTreeBranchNorthWest = features.homeTreeBranch("home_tree/branch/northwest", 210, 240);
        this.homeTreeBranchNorthWestExact = features.homeTreeBranch("home_tree/branch/northwest_exact", 225, 225);
        this.homeTreeBranchWest = features.homeTreeBranch("home_tree/branch/west", 240, 300);
        this.homeTreeBranchWestExact = features.homeTreeBranch("home_tree/branch/west_exact", 270, 270);
        this.homeTreeBranchSouthWest = features.homeTreeBranch("home_tree/branch/southwest", 300, 330);
        this.homeTreeBranchSouthWestExact = features.homeTreeBranch("home_tree/branch/southwest_exact", 315, 315);
    }

    public void addFruitTrees(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.grapefruitTree);
        generation.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.orangeTree);
        generation.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.lemonTree);
        generation.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.limeTree);
    }

    public void addPalmTrees(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.normalPalmTree);
        generation.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.curvedPalmTree);
        generation.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.largePalmTree);
    }

    public void addRainforestTrees(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.rainforestUpTree);
        generation.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.rainforestSmallTualung);
        generation.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.rainforestLargeTualung);
        generation.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.rainforestTallTree);
    }

    public void addRainforestPlants(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.PATCH_MELON);
        generation.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.rainforestVines);
    }

    public void addMangroveVegetation(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.mangroveVegetation);
    }

    public void addGoldenLeatherFern(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.smallGoldenLeatherFern);
        generation.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.tallGoldenLeatherFern);
        generation.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.hugeGoldenLeatherFern);
    }

    public void addPleodendron(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.pleodendron);
    }

    public void addMudDisks(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, this.mudDisk);
    }

    public void addMangroveReeds(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.mangroveReeds);
    }

    public void addTropicsGrass(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.tropicsGrass);
    }

    public void addBamboo(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.bamboo);
    }

    public void addPineapples(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.pineapplePatch);
    }

    public void addEih(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.eih);
    }

    public void addTropicsFlowers(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.tropicsFlowers);
        generation.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.irisFlowers);
    }

    public void addTropicsGems(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, this.azurite);
        generation.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, this.eudialyte);
        generation.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, this.zircon);
    }

    public void addTropicsMetals(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, this.manganese);
        generation.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, this.shaka);
    }

    public void addUndergroundSeagrass(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.undergroundSeagrassOnStone);
        generation.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.undergroundSeagrassOnDirt);
    }

    public void addRegularSeagrass(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.seagrass);
    }

    public void addUndergroundPickles(BiomeGenerationSettings.Builder generation) {
        generation.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.undergroundSeaPickles);
    }

    static final class Register {
        private final WorldgenDataConsumer<ConfiguredFeature<?, ?>> worldgen;

        @SuppressWarnings("unchecked")
		Register(WorldgenDataConsumer<? extends ConfiguredFeature<?, ?>> worldgen) {
            this.worldgen = (WorldgenDataConsumer<ConfiguredFeature<?, ?>>) worldgen;
        }

        public <F extends Feature<?>> ConfiguredFeature<?, ?> register(String id, ConfiguredFeature<?, ?> feature) {
            return this.worldgen.register(new ResourceLocation(Constants.MODID, id), feature);
        }

        public <F extends Feature<?>> ConfiguredFeature<?, ?> register(String id, F feature, Function<F, ConfiguredFeature<?, ?>> configure) {
            return this.register(id, configure.apply(feature));
        }

        public <F extends Feature<?>> ConfiguredFeature<?, ?> register(String id, RegistryObject<F> feature, Function<F, ConfiguredFeature<?, ?>> configure) {
            return this.register(id, feature.get(), configure);
        }

        public <F extends Feature<NoFeatureConfig>> ConfiguredFeature<?, ?> noConfig(String id, RegistryObject<F> feature, UnaryOperator<ConfiguredFeature<?, ?>> configure) {
            return this.register(id, feature, f -> configure.apply(f.configured(NoFeatureConfig.INSTANCE)));
        }

        public ConfiguredFeature<?, ?> fruitTree(String id, Supplier<? extends Block> sapling, Supplier<? extends Block> fruitLeaves) {
            BaseTreeFeatureConfig config = new BaseTreeFeatureConfig.Builder(
                    new SimpleBlockStateProvider(Blocks.OAK_LOG.defaultBlockState()),
                    new WeightedBlockStateProvider().add(TropicraftBlocks.FRUIT_LEAVES.get().defaultBlockState(), 1).add(fruitLeaves.get().defaultBlockState(), 1),
                    new CitrusFoliagePlacer(FeatureSpread.fixed(0), FeatureSpread.fixed(0)),
                    new CitrusTrunkPlacer(6, 3, 0),
                    new TwoLayerFeature(1, 0, 2)
            ).build();

            return this.tree(id, config, 0, 0.1F, 1);
        }

        public <C extends IFeatureConfig, F extends Feature<C>> ConfiguredFeature<?, ?> sparseTree(String id, RegistryObject<F> feature, C config, float chance) {
            return this.register(id, feature, f -> {
                return f.configured(config).decorated(Features.Placements.HEIGHTMAP_SQUARE)
                        .decorated(Placement.COUNT_EXTRA.configured(new AtSurfaceWithExtraConfig(0, chance, 1)));
            });
        }

        public <F extends Feature<?>> ConfiguredFeature<?, ?> tree(String id, BaseTreeFeatureConfig config, int count, float extraChance, int extraCount) {
            return this.register(id, Feature.TREE, feature -> {
                return feature.configured(config)
                        .decorated(Features.Placements.HEIGHTMAP_SQUARE)
                        .decorated(Placement.COUNT_EXTRA.configured(new AtSurfaceWithExtraConfig(count, extraChance, extraCount)));
            });
        }

        public ConfiguredFeature<?, ?> mangrove(String id, BaseTreeFeatureConfig config) {
            return this.register(id, TropicraftFeatures.MANGROVE_TREE.get(), feature -> feature.configured(config));
        }

        public ConfiguredFeature<?, ?> random(String id, ConfiguredFeature<?, ?>... choices) {
            if (choices.length == 2) {
                ConfiguredFeature<?, ?> left = choices[0];
                ConfiguredFeature<?, ?> right = choices[1];
                return this.register(id, Feature.RANDOM_BOOLEAN_SELECTOR, feature -> {
                    return feature.configured(new TwoFeatureChoiceConfig(() -> left, () -> right));
                });
            }

            return this.register(id, Feature.SIMPLE_RANDOM_SELECTOR, feature -> {
                SingleRandomFeature config = new SingleRandomFeature(Arrays.stream(choices)
                        .map(c -> (Supplier<ConfiguredFeature<?, ?>>) () -> c)
                        .collect(Collectors.toList()));
                return feature.configured(config);
            });
        }

        public <C extends IFeatureConfig, F extends Feature<C>> ConfiguredFeature<?, ?> homeTreeBranch(String id, float minAngle, float maxAngle) {
            return this.register(id, TropicraftFeatures.HOME_TREE_BRANCH,
                    f -> f.configured(new HomeTreeBranchConfig(minAngle, maxAngle))
            );
        }
    }
}
