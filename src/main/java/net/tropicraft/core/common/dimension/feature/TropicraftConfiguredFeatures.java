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
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;
import net.minecraftforge.fml.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.data.WorldgenDataConsumer;
import net.tropicraft.core.common.dimension.feature.block_placer.HugePlantBlockPlacer;
import net.tropicraft.core.common.dimension.feature.block_state_provider.NoiseFromTagBlockStateProvider;
import net.tropicraft.core.common.dimension.feature.config.RainforestVinesConfig;
import net.tropicraft.core.common.dimension.feature.tree.*;
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
    public final ConfiguredFeature<?, ?> papaya;

    public final ConfiguredFeature<?, ?> mangroveVegetation;
    public final ConfiguredFeature<?, ?> sparseMangroveVegetation;

    public final ConfiguredFeature<?, ?> mudDisk;

    public final ConfiguredFeature<?, ?> smallGoldenLeatherFern;
    public final ConfiguredFeature<?, ?> tallGoldenLeatherFern;
    public final ConfiguredFeature<?, ?> hugeGoldenLeatherFern;
    public final ConfiguredFeature<?, ?> overgrownSmallGoldenLeatherFern;
    public final ConfiguredFeature<?, ?> overgrownTallGoldenLeatherFern;
    public final ConfiguredFeature<?, ?> overgrownHugeGoldenLeatherFern;

    public final ConfiguredFeature<?, ?> pleodendron;

    public final ConfiguredFeature<?, ?> pineapplePatch;
    public final ConfiguredFeature<?, ?> tropicsFlowers;
    public final ConfiguredFeature<?, ?> rainforestFlowers;
    public final ConfiguredFeature<?, ?> irisFlowers;

    public final ConfiguredFeature<?, ?> coffeeBush;
    public final ConfiguredFeature<?, ?> undergrowth;
    public final ConfiguredFeature<?, ?> singleUndergrowth;

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

    public TropicraftConfiguredFeatures(WorldgenDataConsumer<? extends ConfiguredFeature<?, ?>> worldgen) {
        Register features = new Register(worldgen);

        this.grapefruitTree = features.fruitTree("grapefruit_tree", TropicraftBlocks.GRAPEFRUIT_SAPLING, TropicraftBlocks.GRAPEFRUIT_LEAVES);
        this.orangeTree = features.fruitTree("orange_tree", TropicraftBlocks.ORANGE_SAPLING, TropicraftBlocks.ORANGE_LEAVES);
        this.lemonTree = features.fruitTree("lemon_tree", TropicraftBlocks.LEMON_SAPLING, TropicraftBlocks.LEMON_LEAVES);
        this.limeTree = features.fruitTree("lime_tree", TropicraftBlocks.LIME_SAPLING, TropicraftBlocks.LIME_LEAVES);

        this.normalPalmTree = features.sparseTree("normal_palm_tree", TropicraftFeatures.NORMAL_PALM_TREE, IFeatureConfig.NO_FEATURE_CONFIG, 0.2F);
        this.curvedPalmTree = features.sparseTree("curved_palm_tree", TropicraftFeatures.CURVED_PALM_TREE, IFeatureConfig.NO_FEATURE_CONFIG, 0.2F);
        this.largePalmTree = features.sparseTree("large_palm_tree", TropicraftFeatures.LARGE_PALM_TREE, IFeatureConfig.NO_FEATURE_CONFIG, 0.2F);

        this.rainforestUpTree = features.sparseTree("rainforest_up_tree", TropicraftFeatures.UP_TREE, IFeatureConfig.NO_FEATURE_CONFIG, 0.2F);
        this.rainforestSmallTualung = features.sparseTree("rainforest_small_tualung", TropicraftFeatures.SMALL_TUALUNG, IFeatureConfig.NO_FEATURE_CONFIG, 0.3F);
        this.rainforestLargeTualung = features.sparseTree("rainforest_large_tualung", TropicraftFeatures.LARGE_TUALUNG, IFeatureConfig.NO_FEATURE_CONFIG, 0.4F);
        this.rainforestTallTree = features.sparseTree("rainforest_tall_tree", TropicraftFeatures.TALL_TREE, IFeatureConfig.NO_FEATURE_CONFIG, 0.5F);
        this.rainforestVines = features.register("rainforest_vines", TropicraftFeatures.VINES,
                f -> f.withConfiguration(new RainforestVinesConfig()).square().count(50)
        );

        this.smallGoldenLeatherFern = features.register("small_golden_leather_fern", Feature.RANDOM_PATCH, feature -> {
            SimpleBlockStateProvider state = new SimpleBlockStateProvider(TropicraftBlocks.GOLDEN_LEATHER_FERN.get().getDefaultState());
            return feature.withConfiguration(new BlockClusterFeatureConfig.Builder(state, SimpleBlockPlacer.PLACER)
                    .tries(12)
                    .build()
            ).withPlacement(Features.Placements.PATCH_PLACEMENT);
        });

        this.tallGoldenLeatherFern = features.register("tall_golden_leather_fern", Feature.RANDOM_PATCH, feature -> {
            SimpleBlockStateProvider state = new SimpleBlockStateProvider(TropicraftBlocks.TALL_GOLDEN_LEATHER_FERN.get().getDefaultState());
            return feature.withConfiguration(new BlockClusterFeatureConfig.Builder(state, DoublePlantBlockPlacer.PLACER)
                    .tries(6)
                    .build()
            ).withPlacement(Features.Placements.VEGETATION_PLACEMENT).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT);
        });

        this.hugeGoldenLeatherFern = features.register("huge_golden_leather_fern", Feature.RANDOM_PATCH, feature -> {
            SimpleBlockStateProvider state = new SimpleBlockStateProvider(TropicraftBlocks.LARGE_GOLDEN_LEATHER_FERN.get().getDefaultState());
            return feature.withConfiguration(new BlockClusterFeatureConfig.Builder(state, HugePlantBlockPlacer.INSTANCE)
                    .tries(3)
                    .build()
            ).withPlacement(Features.Placements.VEGETATION_PLACEMENT).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT);
        });

        this.overgrownSmallGoldenLeatherFern = features.register("overgrown_small_golden_leather_fern", Feature.RANDOM_PATCH, feature -> {
            SimpleBlockStateProvider state = new SimpleBlockStateProvider(TropicraftBlocks.GOLDEN_LEATHER_FERN.get().getDefaultState());
            return feature.withConfiguration(new BlockClusterFeatureConfig.Builder(state, SimpleBlockPlacer.PLACER)
                    .tries(28)
                    .build()
            ).withPlacement(Features.Placements.PATCH_PLACEMENT).count(10);
        });

        this.overgrownTallGoldenLeatherFern = features.register("overgrown_tall_golden_leather_fern", Feature.RANDOM_PATCH, feature -> {
            SimpleBlockStateProvider state = new SimpleBlockStateProvider(TropicraftBlocks.TALL_GOLDEN_LEATHER_FERN.get().getDefaultState());
            return feature.withConfiguration(new BlockClusterFeatureConfig.Builder(state, DoublePlantBlockPlacer.PLACER)
                    .tries(16)
                    .build()
            ).withPlacement(Features.Placements.VEGETATION_PLACEMENT).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).count(8);
        });

        this.overgrownHugeGoldenLeatherFern = features.register("overgrown_huge_golden_leather_fern", Feature.RANDOM_PATCH, feature -> {
            SimpleBlockStateProvider state = new SimpleBlockStateProvider(TropicraftBlocks.LARGE_GOLDEN_LEATHER_FERN.get().getDefaultState());
            return feature.withConfiguration(new BlockClusterFeatureConfig.Builder(state, HugePlantBlockPlacer.INSTANCE)
                    .tries(8)
                    .build()
            ).withPlacement(Features.Placements.VEGETATION_PLACEMENT).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).count(6);
        });

        this.pleodendron = features.tree("pleodendron",
                new BaseTreeFeatureConfig.Builder(
                        new SimpleBlockStateProvider(Blocks.JUNGLE_LOG.getDefaultState()),
                        new SimpleBlockStateProvider(Blocks.JUNGLE_LEAVES.getDefaultState()),
                        new PleodendronFoliagePlacer(FeatureSpread.create(2), FeatureSpread.create(0), 1),
                        new PleodendronTrunkPlacer(10, 8, 0),
                        new TwoLayerFeature(0, 0, 0, OptionalInt.of(4))
                ).build(),
                0, 0.08f, 1);

        this.papaya = features.tree("papaya",
                new BaseTreeFeatureConfig.Builder(
                        new SimpleBlockStateProvider(TropicraftBlocks.PAPAYA_LOG.get().getDefaultState()),
                        new SimpleBlockStateProvider(TropicraftBlocks.PAPAYA_LEAVES.get().getDefaultState()),
                        new PapayaFoliagePlacer(FeatureSpread.create(0), FeatureSpread.create(0)),
                        new StraightTrunkPlacer(5, 2, 3),
                        new TwoLayerFeature(0, 0, 0, OptionalInt.of(4))
                ).setDecorators(ImmutableList.of(Features.Placements.BEES_005_PLACEMENT, new PapayaTreeDecorator())).build(),
                0, 0.2f, 1
        );

        FoliagePlacer mangroveFoliage = new MangroveFoliagePlacer(FeatureSpread.create(0), FeatureSpread.create(0));
        BlockStateProvider redMangroveLog = new SimpleBlockStateProvider(TropicraftBlocks.RED_MANGROVE_LOG.get().getDefaultState());
        BlockStateProvider lightMangroveLog = new SimpleBlockStateProvider(TropicraftBlocks.LIGHT_MANGROVE_LOG.get().getDefaultState());
        BlockStateProvider blackMangroveLog = new SimpleBlockStateProvider(TropicraftBlocks.BLACK_MANGROVE_LOG.get().getDefaultState());
        Block redMangroveRoots = TropicraftBlocks.RED_MANGROVE_ROOTS.get();
        Block lightMangroveRoots = TropicraftBlocks.LIGHT_MANGROVE_ROOTS.get();
        Block blackMangroveRoots = TropicraftBlocks.BLACK_MANGROVE_ROOTS.get();

        BlockStateProvider redMangroveLeaves = new SimpleBlockStateProvider(TropicraftBlocks.RED_MANGROVE_LEAVES.get().getDefaultState());
        BlockStateProvider tallMangroveLeaves = new SimpleBlockStateProvider(TropicraftBlocks.TALL_MANGROVE_LEAVES.get().getDefaultState());
        BlockStateProvider teaMangroveLeaves = new SimpleBlockStateProvider(TropicraftBlocks.TEA_MANGROVE_LEAVES.get().getDefaultState());
        BlockStateProvider blackMangroveLeaves = new SimpleBlockStateProvider(TropicraftBlocks.BLACK_MANGROVE_LEAVES.get().getDefaultState());

        TwoLayerFeature mangroveMinimumSize = new TwoLayerFeature(0, 0, 0, OptionalInt.of(4));

        MangroveTrunkPlacer redMangroveTrunk = new MangroveTrunkPlacer(3, 3, 0, redMangroveRoots, true, false);
        this.redMangroveShort = features.mangrove("red_mangrove_short",
                new BaseTreeFeatureConfig.Builder(redMangroveLog, redMangroveLeaves, mangroveFoliage, redMangroveTrunk, mangroveMinimumSize)
                        .setDecorators(ImmutableList.of(Features.Placements.BEES_002_PLACEMENT, PianguasTreeDecorator.REGULAR))
                        .setMaxWaterDepth(1).build()
        );
        this.redMangroveSmall = features.mangrove("red_mangrove_small",
                new BaseTreeFeatureConfig.Builder(
                        redMangroveLog, redMangroveLeaves,
                        new SmallMangroveFoliagePlacer(FeatureSpread.create(0), FeatureSpread.create(0)),
                        new SmallMangroveTrunkPlacer(2, 1, 0, redMangroveRoots),
                        mangroveMinimumSize
                ).setDecorators(ImmutableList.of(Features.Placements.BEES_002_PLACEMENT, PianguasTreeDecorator.SMALL)).build()
        );
        this.redMangrove = features.random("red_mangrove", this.redMangroveShort, this.redMangroveSmall);

        this.tallMangrove = features.mangrove("tall_mangrove",
                new BaseTreeFeatureConfig.Builder(
                        lightMangroveLog, tallMangroveLeaves,
                        mangroveFoliage,
                        new MangroveTrunkPlacer(7, 4, 2, lightMangroveRoots, false, false),
                        mangroveMinimumSize
                ).setDecorators(ImmutableList.of(Features.Placements.BEES_002_PLACEMENT, PianguasTreeDecorator.REGULAR)).setMaxWaterDepth(2).build()
        );

        PneumatophoresTreeDecorator teaMangrovePneumatophores = new PneumatophoresTreeDecorator(lightMangroveRoots, 2, 6, 4);
        this.teaMangrove = features.mangrove("tea_mangrove",
                new BaseTreeFeatureConfig.Builder(
                        lightMangroveLog, teaMangroveLeaves,
                        mangroveFoliage,
                        new MangroveTrunkPlacer(5, 3, 0, lightMangroveRoots, false, true),
                        mangroveMinimumSize
                ).setDecorators(ImmutableList.of(Features.Placements.BEES_002_PLACEMENT, PianguasTreeDecorator.REGULAR, teaMangrovePneumatophores)).setMaxWaterDepth(1).build()
        );

        PneumatophoresTreeDecorator blackMangrovePneumatophores = new PneumatophoresTreeDecorator(blackMangroveRoots, 8, 16, 6);
        this.blackMangrove = features.mangrove("black_mangrove",
                new BaseTreeFeatureConfig.Builder(
                        blackMangroveLog, blackMangroveLeaves,
                        mangroveFoliage,
                        new MangroveTrunkPlacer(4, 3, 0, blackMangroveRoots, true, false),
                        mangroveMinimumSize
                ).setDecorators(ImmutableList.of(Features.Placements.BEES_002_PLACEMENT, PianguasTreeDecorator.REGULAR, blackMangrovePneumatophores)).setMaxWaterDepth(1).build()
        );
        this.lightMangroves = features.random("light_mangroves", this.tallMangrove, this.teaMangrove, this.blackMangrove);

        this.mangroves = features.random("mangroves", this.redMangrove, this.lightMangroves);

        this.mangroveVegetation = features.register("mangrove_vegetation", this.mangroves
                    .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT)
                    .withPlacement(Placement.COUNT_NOISE_BIASED.configure(new TopSolidWithNoiseConfig(7, 200.0, 1.5)))
        );

        this.sparseMangroveVegetation = features.register("sparse_mangrove_vegetation", this.mangroves
                .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT)
                .withPlacement(Placement.COUNT_NOISE_BIASED.configure(new TopSolidWithNoiseConfig(3, 200.0, 1.5)))
        );

        this.mudDisk = features.register("mud_disk", Feature.DISK, feature -> feature
                .withConfiguration(new SphereReplaceConfig(
                        TropicraftBlocks.MUD.get().getDefaultState(),
                        FeatureSpread.create(2, 4),
                        2,
                        ImmutableList.of(Blocks.DIRT.getDefaultState(), Blocks.GRASS_BLOCK.getDefaultState())
                ))
                .withPlacement(Features.Placements.SEAGRASS_DISK_PLACEMENT).count(3)
        );

        this.eih = features.noConfig("eih", TropicraftFeatures.EIH,
                f -> f.withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT)
                        .withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(0, 0.01F, 1)))
        );

        this.tropicsGrass = features.register("tropics_grass", Feature.RANDOM_PATCH,
                f -> f.withConfiguration(Features.Configs.JUNGLE_VEGETATION_CONFIG).withPlacement(Features.Placements.PATCH_PLACEMENT).count(10));

        this.bamboo = features.register("bamboo", Feature.BAMBOO,
                f -> f.withConfiguration(new ProbabilityConfig(0.15F))
                        .withPlacement(Features.Placements.BAMBOO_PLACEMENT)
                        .square()
                        .withPlacement(Placement.COUNT_NOISE_BIASED.configure(new TopSolidWithNoiseConfig(70, 140.0D, 0.3D)))
        );

        this.pineapplePatch = features.register("pineapple_patch", Feature.RANDOM_PATCH, feature -> {
            SimpleBlockStateProvider state = new SimpleBlockStateProvider(TropicraftBlocks.PINEAPPLE.get().getDefaultState());
            return feature.withConfiguration(new BlockClusterFeatureConfig.Builder(state, new DoublePlantBlockPlacer())
                    .tries(64)
                    .preventProjection()
                    .build()
            ).withPlacement(Features.Placements.VEGETATION_PLACEMENT).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT);
        });
        this.tropicsFlowers = features.register("tropics_flowers", Feature.FLOWER, feature -> {
            BlockStateProvider stateProvider = new NoiseFromTagBlockStateProvider(TropicraftTags.Blocks.TROPICS_FLOWERS);
            BlockClusterFeatureConfig config = new BlockClusterFeatureConfig.Builder(stateProvider, SimpleBlockPlacer.PLACER).tries(64).build();
            return feature.withConfiguration(config).withPlacement(Features.Placements.VEGETATION_PLACEMENT.withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).count(12));
        });
        this.rainforestFlowers = features.register("rainforest_flowers", Feature.FLOWER, feature -> {
            BlockStateProvider stateProvider = new NoiseFromTagBlockStateProvider(TropicraftTags.Blocks.RAINFOREST_FLOWERS);
            BlockClusterFeatureConfig config = new BlockClusterFeatureConfig.Builder(stateProvider, SimpleBlockPlacer.PLACER).tries(64).preventProjection().build();
            return feature.withConfiguration(config).withPlacement(Features.Placements.VEGETATION_PLACEMENT.withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).count(4));
        });
        this.irisFlowers = features.register("iris_flowers", Feature.RANDOM_PATCH, feature -> {
            BlockStateProvider stateProvider = new SimpleBlockStateProvider(TropicraftBlocks.IRIS.get().getDefaultState());
            BlockClusterFeatureConfig config = new BlockClusterFeatureConfig.Builder(stateProvider, new DoublePlantBlockPlacer()).tries(64).preventProjection().build();
            return feature.withConfiguration(config).withPlacement(Features.Placements.VEGETATION_PLACEMENT.withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).count(10));
        });

        this.coffeeBush = features.noConfig("coffee_bush", TropicraftFeatures.COFFEE_BUSH, feature -> {
            return feature.withPlacement(Features.Placements.VEGETATION_PLACEMENT.withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).count(5));
        });
        this.undergrowth = features.noConfig("undergrowth", TropicraftFeatures.UNDERGROWTH, feature -> {
            return feature.withPlacement(Features.Placements.VEGETATION_PLACEMENT.withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).count(100));
        });
        this.singleUndergrowth = features.noConfig("single_undergrowth", TropicraftFeatures.SINGLE_UNDERGROWTH, feature -> {
            return feature.withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT.count(2));
        });

        this.seagrass = features.register("seagrass", Feature.SEAGRASS, feature -> {
            return feature.withConfiguration(new ProbabilityConfig(0.3f)).count(48).withPlacement(Features.Placements.SEAGRASS_DISK_PLACEMENT).count(3);
        });

        this.undergroundSeagrassOnStone = features.register("underground_seagrass_on_stone", Feature.SIMPLE_BLOCK, feature -> {
            BlockWithContextConfig config = new BlockWithContextConfig(
                    Blocks.SEAGRASS.getDefaultState(),
                    ImmutableList.of(Blocks.STONE.getDefaultState()),
                    ImmutableList.of(Blocks.WATER.getDefaultState()),
                    ImmutableList.of(Blocks.WATER.getDefaultState())
            );
            return feature.withConfiguration(config).withPlacement(Placement.CARVING_MASK.configure(new CaveEdgeConfig(GenerationStage.Carving.LIQUID, 0.1F)));
        });
        this.undergroundSeagrassOnDirt = features.register("underground_seagrass_on_dirt", Feature.SIMPLE_BLOCK, feature -> {
            BlockWithContextConfig config = new BlockWithContextConfig(
                    Blocks.SEAGRASS.getDefaultState(),
                    ImmutableList.of(Blocks.DIRT.getDefaultState()),
                    ImmutableList.of(Blocks.WATER.getDefaultState()),
                    ImmutableList.of(Blocks.WATER.getDefaultState())
            );
            return feature.withConfiguration(config).withPlacement(Placement.CARVING_MASK.configure(new CaveEdgeConfig(GenerationStage.Carving.LIQUID, 0.5F)));
        });
        this.undergroundSeaPickles = features.noConfig("underground_sea_pickles", TropicraftFeatures.UNDERGROUND_SEA_PICKLE, feature -> {
            return feature.withPlacement(Placement.CARVING_MASK.configure(new CaveEdgeConfig(GenerationStage.Carving.LIQUID, 0.05F)));
        });

        this.mangroveReeds = features.noConfig("mangrove_reeds", TropicraftFeatures.REEDS, feature -> {
            return feature.withPlacement(Features.Placements.SEAGRASS_DISK_PLACEMENT).count(2);
        });

        this.azurite = features.register("azurite", Feature.ORE, f -> {
            return f.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, TropicraftBlocks.AZURITE_ORE.get().getDefaultState(), 8))
                    .withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(100, 0, 128)))
                    .square().count(3);
        });
        this.eudialyte = features.register("eudialyte", Feature.ORE, f -> {
            return f.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, TropicraftBlocks.EUDIALYTE_ORE.get().getDefaultState(), 12))
                    .withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(100, 0, 128)))
                    .square().count(10);
        });
        this.zircon = features.register("zircon", Feature.ORE, f -> {
            return f.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, TropicraftBlocks.ZIRCON_ORE.get().getDefaultState(), 14))
                    .withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(100, 0, 128)))
                    .square().count(15);
        });
        this.manganese = features.register("manganese", Feature.ORE, f -> {
            return f.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, TropicraftBlocks.MANGANESE_ORE.get().getDefaultState(), 10))
                    .withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(32, 0, 32)))
                    .square().count(8);
        });
        this.shaka = features.register("shaka", Feature.ORE, f -> {
            return f.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, TropicraftBlocks.SHAKA_ORE.get().getDefaultState(), 8))
                    .withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(0, 0, 32)))
                    .square().count(6);
        });
    }

    public void addFruitTrees(BiomeGenerationSettings.Builder generation) {
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.grapefruitTree);
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.orangeTree);
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.lemonTree);
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.limeTree);
    }

    public void addPalmTrees(BiomeGenerationSettings.Builder generation) {
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.normalPalmTree);
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.curvedPalmTree);
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.largePalmTree);
    }

    public void addRainforestTrees(BiomeGenerationSettings.Builder generation) {
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.rainforestUpTree);
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.rainforestSmallTualung);
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.rainforestLargeTualung);
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.rainforestTallTree);
    }

    public void addRainforestPlants(BiomeGenerationSettings.Builder generation) {
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.PATCH_MELON);
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.rainforestVines);
    }

    public void addMangroveVegetation(BiomeGenerationSettings.Builder generation, boolean sparse) {
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, sparse ? this.sparseMangroveVegetation : this.mangroveVegetation);
    }

    public void addOvergrownGoldenLeatherFern(BiomeGenerationSettings.Builder generation) {
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.overgrownSmallGoldenLeatherFern);
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.overgrownTallGoldenLeatherFern);
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.overgrownHugeGoldenLeatherFern);
    }

    public void addGoldenLeatherFern(BiomeGenerationSettings.Builder generation) {
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.smallGoldenLeatherFern);
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.tallGoldenLeatherFern);
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.hugeGoldenLeatherFern);
    }

    public void addPleodendron(BiomeGenerationSettings.Builder generation) {
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.pleodendron);
    }

    public void addPapaya(BiomeGenerationSettings.Builder generation) {
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.papaya);
    }

    public void addMudDisks(BiomeGenerationSettings.Builder generation) {
        generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, this.mudDisk);
    }

    public void addMangroveReeds(BiomeGenerationSettings.Builder generation) {
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.mangroveReeds);
    }

    public void addTropicsGrass(BiomeGenerationSettings.Builder generation) {
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.tropicsGrass);
    }

    public void addBamboo(BiomeGenerationSettings.Builder generation) {
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.bamboo);
    }

    public void addPineapples(BiomeGenerationSettings.Builder generation) {
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.pineapplePatch);
    }

    public void addEih(BiomeGenerationSettings.Builder generation) {
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.eih);
    }

    public void addTropicsFlowers(BiomeGenerationSettings.Builder generation) {
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.tropicsFlowers);
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.irisFlowers);
    }

    public void addTropicsGems(BiomeGenerationSettings.Builder generation) {
        generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, this.azurite);
        generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, this.eudialyte);
        generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, this.zircon);
    }

    public void addTropicsMetals(BiomeGenerationSettings.Builder generation) {
        generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, this.manganese);
        generation.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, this.shaka);
    }

    public void addUndergroundSeagrass(BiomeGenerationSettings.Builder generation) {
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.undergroundSeagrassOnStone);
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.undergroundSeagrassOnDirt);
    }

    public void addRegularSeagrass(BiomeGenerationSettings.Builder generation) {
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.seagrass);
    }

    public void addUndergroundPickles(BiomeGenerationSettings.Builder generation) {
        generation.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, this.undergroundSeaPickles);
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
            return this.register(id, feature, f -> configure.apply(f.withConfiguration(NoFeatureConfig.INSTANCE)));
        }

        public ConfiguredFeature<?, ?> fruitTree(String id, Supplier<? extends Block> sapling, Supplier<? extends Block> fruitLeaves) {
            BaseTreeFeatureConfig config = new BaseTreeFeatureConfig.Builder(
                    new SimpleBlockStateProvider(Blocks.OAK_LOG.getDefaultState()),
                    new WeightedBlockStateProvider().addWeightedBlockstate(TropicraftBlocks.FRUIT_LEAVES.get().getDefaultState(), 1).addWeightedBlockstate(fruitLeaves.get().getDefaultState(), 1),
                    new CitrusFoliagePlacer(FeatureSpread.create(0), FeatureSpread.create(0)),
                    new CitrusTrunkPlacer(6, 3, 0),
                    new TwoLayerFeature(1, 0, 2)
            ).build();

            return this.tree(id, config, 0, 0.1F, 1);
        }

        public <C extends IFeatureConfig, F extends Feature<C>> ConfiguredFeature<?, ?> sparseTree(String id, RegistryObject<F> feature, C config, float chance) {
            return this.register(id, feature, f -> {
                return f.withConfiguration(config).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT)
                        .withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(0, chance, 1)));
            });
        }

        public <F extends Feature<?>> ConfiguredFeature<?, ?> tree(String id, BaseTreeFeatureConfig config, int count, float extraChance, int extraCount) {
            return this.register(id, Feature.TREE, feature -> {
                return feature.withConfiguration(config)
                        .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT)
                        .withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(count, extraChance, extraCount)));
            });
        }

        public ConfiguredFeature<?, ?> mangrove(String id, BaseTreeFeatureConfig config) {
            return this.register(id, TropicraftFeatures.MANGROVE_TREE.get(), feature -> feature.withConfiguration(config));
        }

        public ConfiguredFeature<?, ?> random(String id, ConfiguredFeature<?, ?>... choices) {
            if (choices.length == 2) {
                ConfiguredFeature<?, ?> left = choices[0];
                ConfiguredFeature<?, ?> right = choices[1];
                return this.register(id, Feature.RANDOM_BOOLEAN_SELECTOR, feature -> {
                    return feature.withConfiguration(new TwoFeatureChoiceConfig(() -> left, () -> right));
                });
            }

            return this.register(id, Feature.SIMPLE_RANDOM_SELECTOR, feature -> {
                SingleRandomFeature config = new SingleRandomFeature(Arrays.stream(choices)
                        .map(c -> (Supplier<ConfiguredFeature<?, ?>>) () -> c)
                        .collect(Collectors.toList()));
                return feature.withConfiguration(config);
            });
        }
    }
}
