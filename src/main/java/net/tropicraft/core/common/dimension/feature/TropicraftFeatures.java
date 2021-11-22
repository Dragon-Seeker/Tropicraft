package net.tropicraft.core.common.dimension.feature;

import com.google.common.collect.ImmutableList;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool.Projection;
import net.tropicraft.Constants;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.dimension.feature.config.FruitTreeConfig;
import net.tropicraft.core.common.dimension.feature.config.RainforestVinesConfig;
import net.tropicraft.core.common.dimension.feature.jigsaw.SinkInGroundProcessor;
import net.tropicraft.core.common.dimension.feature.jigsaw.SmoothingGravityProcessor;
import net.tropicraft.core.common.dimension.feature.jigsaw.SteepPathProcessor;
import net.tropicraft.core.common.dimension.feature.jigsaw.StructureSupportsProcessor;
import net.tropicraft.core.common.dimension.feature.tree.*;
import net.tropicraft.core.common.dimension.feature.tree.mangrove.MangroveTreeFeature;
import net.tropicraft.core.mixin.ProjectionFactory;

import java.util.function.Supplier;

public class TropicraftFeatures {

    //public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Constants.MODID);
    //public static final DeferredRegister<StructureFeature<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, Constants.MODID);

    public static final FruitTreeFeature FRUIT_TREE = register("fruit_tree", () -> new FruitTreeFeature(FruitTreeConfig.CODEC));
    public static final PalmTreeFeature NORMAL_PALM_TREE = register("normal_palm_tree", () -> new NormalPalmTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final PalmTreeFeature CURVED_PALM_TREE = register("curved_palm_tree", () -> new CurvedPalmTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final PalmTreeFeature LARGE_PALM_TREE = register("large_palm_tree", () -> new LargePalmTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final RainforestTreeFeature UP_TREE = register("up_tree", () -> new UpTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final RainforestTreeFeature SMALL_TUALUNG = register("small_tualung", () -> new TualungFeature(NoneFeatureConfiguration.CODEC, 16, 9));
    public static final RainforestTreeFeature LARGE_TUALUNG = register("large_tualung", () -> new TualungFeature(NoneFeatureConfiguration.CODEC, 25, 11));
    public static final RainforestTreeFeature TALL_TREE = register("tall_tree", () -> new TallRainforestTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final EIHFeature EIH = register("eih", () -> new EIHFeature(NoneFeatureConfiguration.CODEC));
    public static final UndergrowthFeature UNDERGROWTH = register("undergrowth", () -> new UndergrowthFeature(NoneFeatureConfiguration.CODEC));
    public static final SingleUndergrowthFeature SINGLE_UNDERGROWTH = register("single_undergrowth", () -> new SingleUndergrowthFeature(NoneFeatureConfiguration.CODEC));
    public static final RainforestVinesFeature VINES = register("rainforest_vines", () -> new RainforestVinesFeature(RainforestVinesConfig.CODEC));
    public static final UndergroundSeaPickleFeature UNDERGROUND_SEA_PICKLE = register("underground_sea_pickle", () -> new UndergroundSeaPickleFeature(NoneFeatureConfiguration.CODEC));

    public static final StructureFeature<JigsawConfiguration> KOA_VILLAGE = registerStructure("koa_village", new KoaVillageStructure(JigsawConfiguration.CODEC), GenerationStep.Decoration.SURFACE_STRUCTURES);
    public static final StructureFeature<JigsawConfiguration> HOME_TREE = registerStructure("home_tree", new HomeTreeStructure(JigsawConfiguration.CODEC), GenerationStep.Decoration.SURFACE_STRUCTURES);
    public static final CoffeePlantFeature COFFEE_BUSH = register("coffee_bush", () -> new CoffeePlantFeature(NoneFeatureConfiguration.CODEC));

    public static final ReedsFeature REEDS = register("reeds", () -> new ReedsFeature(NoneFeatureConfiguration.CODEC));
    public static final MangroveTreeFeature MANGROVE_TREE = register("mangrove_tree", () -> new MangroveTreeFeature((TreeFeature) Feature.TREE, TreeConfiguration.CODEC));

    public static final Projection KOA_PATH = ProjectionFactory.newProjection("KOA_PATH", 2, Constants.MODID + ":koa_path",
            ImmutableList.of(new SmoothingGravityProcessor(Heightmap.Types.WORLD_SURFACE_WG, -1), new SinkInGroundProcessor(), new SteepPathProcessor(), new StructureSupportsProcessor(false, ImmutableList.of(Registry.BLOCK.getKey(TropicraftBlocks.BAMBOO_FENCE)))));

    public static void setStructurePoolsProjectionMap(){
        Projection.BY_NAME.put(KOA_PATH.getName(), KOA_PATH);
    }

    private static <T extends Feature<?>> T register(final String name, final Supplier<T> sup) {
        ResourceLocation tropicID = new ResourceLocation(Constants.MODID, name);
        if (Registry.FEATURE.keySet().contains(tropicID))
            throw new IllegalStateException("Feature ID: \"" + tropicID.toString() + "\" already exists in the Features registry!");

        return Registry.register(Registry.FEATURE, tropicID, sup.get());
    }

    private static <FC extends FeatureConfiguration, T extends StructureFeature<FC>> T registerStructure(final String name, T structure, GenerationStep.Decoration step) {
        ResourceLocation tropicID = new ResourceLocation(Constants.MODID, name);
        if (Registry.STRUCTURE_FEATURE.keySet().contains(tropicID))
            throw new IllegalStateException("Structure Feature ID: \"" + tropicID.toString() + "\" already exists in the Structure Features registry!");

        //TODO [FABRIC]: DOUBLE CHECK THAT A DEFAULT CONFIG MUST BE USED FOR STRUCTURE REGISTRY
        return FabricStructureBuilder.create(tropicID, structure)
                .step(step)
                .defaultConfig(32, 8, 12345)
                .adjustsSurface()
                .register();
    }

    public static void inti(){
        setStructurePoolsProjectionMap();
    }
}
