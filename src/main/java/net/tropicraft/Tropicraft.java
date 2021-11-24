package net.tropicraft;

import com.google.common.base.Supplier;
import com.google.common.reflect.Reflection;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.tropicraft.core.client.data.TropicraftBlockstateProvider;
import net.tropicraft.core.client.data.TropicraftItemModelProvider;
import net.tropicraft.core.client.data.TropicraftLangProvider;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.TropicraftFlower;
import net.tropicraft.core.common.block.tileentity.TropicraftTileEntityTypes;
import net.tropicraft.core.common.command.CommandTropicsTeleport;
import net.tropicraft.core.common.command.MapBiomesCommand;
import net.tropicraft.core.common.data.*;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomeProvider;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomes;
import net.tropicraft.core.common.dimension.carver.TropicraftCarvers;
import net.tropicraft.core.common.dimension.carver.TropicraftConfiguredCarvers;
import net.tropicraft.core.common.dimension.chunk.TropicraftChunkGenerator;
import net.tropicraft.core.common.dimension.feature.TropicraftConfiguredFeatures;
import net.tropicraft.core.common.dimension.feature.TropicraftConfiguredStructures;
import net.tropicraft.core.common.dimension.feature.TropicraftFeatures;
import net.tropicraft.core.common.dimension.feature.block_placer.TropicraftBlockPlacerTypes;
import net.tropicraft.core.common.dimension.feature.block_state_provider.TropicraftBlockStateProviders;
import net.tropicraft.core.common.dimension.feature.jigsaw.*;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.HomeTreeBranchPiece;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.NoRotateSingleJigsawPiece;
import net.tropicraft.core.common.dimension.feature.jigsaw.piece.SingleNoAirJigsawPiece;
import net.tropicraft.core.common.dimension.feature.pools.TropicraftTemplatePools;
import net.tropicraft.core.common.dimension.feature.tree.TropicraftFoliagePlacers;
import net.tropicraft.core.common.dimension.feature.tree.TropicraftTreeDecorators;
import net.tropicraft.core.common.dimension.feature.tree.TropicraftTrunkPlacers;
import net.tropicraft.core.common.dimension.surfacebuilders.TropicraftConfiguredSurfaceBuilders;
import net.tropicraft.core.common.dimension.surfacebuilders.TropicraftSurfaceBuilders;
import net.tropicraft.core.common.drinks.MixerRecipes;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.item.TropicraftItems;
import net.tropicraft.core.common.item.scuba.ScubaGogglesItem;
import net.tropicraft.core.common.network.TropicraftPackets;
import net.tropicraft.core.common.sound.Sounds;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

//@Mod(Constants.MODID)
public class Tropicraft implements ModInitializer {
    public static final CreativeModeTab TROPICRAFT_ITEM_GROUP =
            FabricItemGroupBuilder.build(new ResourceLocation(Constants.MODID, "tropicraft"),
                    () -> new ItemStack(TropicraftFlower.RED_ANTHURIUM.get()));

    public static boolean DatagenSwitch = true;

    @Override
    public void onInitialize() {
        Tropicraft();
    }

    public void Tropicraft() {
        // Compatible with all versions that match the semver (excluding the qualifier e.g. "-beta+42")
//        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(Tropicraft::getCompatVersion, (s, v) -> Tropicraft.isCompatibleVersion(s)));
//        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        TropicraftTags.Blocks.init();
        TropicraftTags.BlocksExtended.init();
        TropicraftTags.Items.init();
        TropicraftTags.ItemsExtended.init();

        // General mod setup
        setup();//modBus.addListener(this::setup);
//        modBus.addListener(this::gatherData);

//        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
//            // Client setup
//            modBus.addListener(this::setupClient);
//            modBus.addListener(this::registerItemColors);
//        });
        Sounds.init();
        onServerStarting();

        TropicraftDimension.addDefaultDimensionKey();

        // Registry objects
        TropicraftBlocks.init();
        //TropicraftBlocks.BLOCKITEMS.register(modBus);
        TropicraftItems.init();
        ScubaGogglesItem.init();

        //

//        new TropicraftBlockTagsProvider();
//        new TropicraftItemTagsProvider();

        MixerRecipes.addMixerRecipes();
        TropicraftTileEntityTypes.init();
        TropicraftEntities.init();
        TropicraftEntities.onCreateEntityAttributes();
        TropicraftCarvers.init();
        TropicraftFeatures.inti();
        TropicraftFoliagePlacers.init();
        TropicraftTreeDecorators.init();
        TropicraftFeatures.inti();
        TropicraftConfiguredFeatures.init();
        TropicraftConfiguredStructures.init();
        TropicraftSurfaceBuilders.init();
        TropicraftBiomes.onBiomeLoad();
        TropicraftBlockStateProviders.init();
        TropicraftBlockPlacerTypes.init();
        runIfEnabled();
//        temporaryWorldGenRegistry();

        // Hack in our item frame models the way vanilla does
//        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
//            StateDefinition<Block, BlockState> frameState = new StateDefinition.Builder<Block, BlockState>(Blocks.AIR).add(BooleanProperty.create("map")).create(Block::defaultBlockState, BlockState::new);
//
//            ModelBakery.STATIC_DEFINITIONS = ImmutableMap.<ResourceLocation, StateDefinition<Block, BlockState>>builder()
//                    .putAll(ModelBakery.STATIC_DEFINITIONS)
//                    .put(TropicraftItems.BAMBOO_ITEM_FRAME.getId(), frameState)
//                    .build();
//        });
    }

    private static final Pattern QUALIFIER = Pattern.compile("-\\w+\\+\\d+");

//    public static String getCompatVersion() {
//        return getCompatVersion(ModList.get().getModContainerById(Constants.MODID).orElseThrow(IllegalStateException::new).getModInfo().getVersion().toString());
//    }

    private static String getCompatVersion(String fullVersion) {
        return QUALIFIER.matcher(fullVersion).replaceAll("");
    }

//    public static boolean isCompatibleVersion(String version) {
//        return getCompatVersion().equals(getCompatVersion(version));
//    }

//    @OnlyIn(Dist.CLIENT)
//    private void setupClient(final FMLClientSetupEvent event) {
//        ClientSetup.setupBlockRenderLayers();
//
//        //ClientSetup.setupEntityRenderers(event);
//
//        //ClientSetup.setupTileEntityRenderers();
//
//        ClientSetup.setupDimensionRenderInfo();
//    }

//    @OnlyIn(Dist.CLIENT)
//    private void registerItemColors(ColorHandlerEvent.Item evt) {
//        BasicColorHandler basic = new BasicColorHandler();
//        for (RegistryObject<Item> ro : TropicraftItems.ITEMS.getEntries()) {
//            Item item = ro.get();
//            if (item instanceof IColoredItem) {
//                evt.getItemColors().register(basic, item);
//            }
//        }
//    }

    private void setup() {
        TropicraftPackets.init();
        //ScubaData.registerCapability();
        TropicraftEntities.registerSpawns();

        TropicraftChunkGenerator.register();
        TropicraftBiomeProvider.register();

        Reflection.initialize(
                SingleNoAirJigsawPiece.class, NoRotateSingleJigsawPiece.class, HomeTreeBranchPiece.class,
                AdjustBuildingHeightProcessor.class, AirToCaveAirProcessor.class, SinkInGroundProcessor.class,
                SmoothingGravityProcessor.class, SteepPathProcessor.class, StructureSupportsProcessor.class,
                StructureVoidProcessor.class,
                TropicraftTrunkPlacers.class
        );
    }

    private void onServerStarting() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            CommandTropicsTeleport.register(dispatcher);

            // Dev only debug!
            if (!FabricLoader.getInstance().isDevelopmentEnvironment()) {
                MapBiomesCommand.register(dispatcher);
            }
        });
    }



    private void gatherData(DataGenerator gen, ExistingFileHelper existingFileHelper) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            TropicraftBlockstateProvider blockstates = new TropicraftBlockstateProvider(gen, existingFileHelper);
            gen.addProvider(blockstates);
            gen.addProvider(new TropicraftItemModelProvider(gen, blockstates.getExistingHelper()));
            gen.addProvider(new TropicraftLangProvider(gen));
        }
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT || FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER ) {
            TropicraftBlockTagsProvider blockTags = new TropicraftBlockTagsProvider(gen, existingFileHelper);
            gen.addProvider(blockTags);
            gen.addProvider(new TropicraftItemTagsProvider(gen, blockTags, existingFileHelper));
            gen.addProvider(new TropicraftRecipeProvider(gen));
            gen.addProvider(new TropicraftLootTableProvider(gen));
            gen.addProvider(new TropicraftEntityTypeTagsProvider(gen, existingFileHelper));

            gatherWorldgenData(gen);
        }
    }

    private void gatherWorldgenData(DataGenerator gen) {
        gen.addProvider(new TropicraftWorldgenProvider(gen, generator -> {
            TropicraftConfiguredFeatures features = generator.addConfiguredFeatures(TropicraftConfiguredFeatures::new);
            TropicraftConfiguredSurfaceBuilders surfaceBuilders = generator.addConfiguredSurfaceBuilders(TropicraftConfiguredSurfaceBuilders::new);
            TropicraftConfiguredCarvers carvers = generator.addConfiguredCarvers(TropicraftConfiguredCarvers::new);
            TropicraftProcessorLists processors = generator.addProcessorLists(TropicraftProcessorLists::new);
            TropicraftTemplatePools templates = generator.addTemplatePools(consumer -> new TropicraftTemplatePools(consumer, features, processors));
            TropicraftConfiguredStructures structures = generator.addConfiguredStructures(consumer -> new TropicraftConfiguredStructures(consumer, templates));

            generator.addBiomes(consumer -> {
                return new TropicraftBiomes(consumer, features, structures, carvers, surfaceBuilders);
            });
        }));
    }

    private static final Logger LOGGER = LogManager.getLogger();

    public void dump(Path outputPath, List<Path> existingDataPaths) throws Exception {
        LOGGER.info("Writing generated resources to {}", outputPath.toAbsolutePath());

        DataGenerator generator = new DataGenerator(outputPath, Collections.emptyList());
        var existingFileHelper = new ExistingFileHelper(existingDataPaths, Collections.emptySet(),
                true, null, null);
        gatherData(generator, existingFileHelper);
        generator.run();
    }

    public void runIfEnabled() {
        if (DatagenSwitch){//!"true".equals(System.getProperty("tropicraft.generateData"))) {
            return;
        }

        var outputPath = Paths.get("../src/generated/resources");
        //var existingData = System.getProperty("tropicraft.generateData.existingData").split(";");
        var existingDataPaths = Arrays.asList(Paths.get("../src/main/resources"));//Arrays.stream("src/main/resources".split(";")).map(Paths::get).toList();

        try {
            dump(outputPath, existingDataPaths);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
//        System.exit(0);
    }


    private void temporaryWorldGenRegistry() {
        new TempRegistryWorldGen(generator -> {
            TropicraftConfiguredFeatures features = generator.addConfiguredFeatures(TropicraftConfiguredFeatures::new);
            TropicraftConfiguredSurfaceBuilders surfaceBuilders = generator.addConfiguredSurfaceBuilders(TropicraftConfiguredSurfaceBuilders::new);
            TropicraftConfiguredCarvers carvers = generator.addConfiguredCarvers(TropicraftConfiguredCarvers::new);
            TropicraftProcessorLists processors = generator.addProcessorLists(TropicraftProcessorLists::new);
            TropicraftTemplatePools templates = generator.addTemplatePools(consumer -> new TropicraftTemplatePools(consumer, features, processors));
            TropicraftConfiguredStructures structures = generator.addConfiguredStructures(consumer -> new TropicraftConfiguredStructures(consumer, templates));

            generator.addBiomes(consumer -> {
                return new TropicraftBiomes(consumer, features, structures, carvers, surfaceBuilders);
            });
        }).run();
    }
}
