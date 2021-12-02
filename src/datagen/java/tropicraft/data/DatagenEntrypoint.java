package tropicraft.data;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;
import net.tropicraft.core.client.data.TropicraftBlockstateProvider;
import net.tropicraft.core.client.data.TropicraftItemModelProvider;
import net.tropicraft.core.client.data.TropicraftLangProvider;
import net.tropicraft.core.common.data.*;
import net.tropicraft.core.common.dimension.biome.TropicraftBiomes;
import net.tropicraft.core.common.dimension.carver.TropicraftConfiguredCarvers;
import net.tropicraft.core.common.dimension.feature.TropicraftConfiguredFeatures;
import net.tropicraft.core.common.dimension.feature.TropicraftConfiguredStructures;
import net.tropicraft.core.common.dimension.feature.jigsaw.TropicraftProcessorLists;
import net.tropicraft.core.common.dimension.feature.pools.TropicraftTemplatePools;
import net.tropicraft.core.common.dimension.surfacebuilders.TropicraftConfiguredSurfaceBuilders;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class DatagenEntrypoint implements ClientModInitializer {
    private void gatherData(DataGenerator gen, ExistingFileHelper existingFileHelper) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            TropicraftBlockstateProvider blockstates = new TropicraftBlockstateProvider(gen, existingFileHelper);
            gen.addProvider(blockstates);
            gen.addProvider(new TropicraftItemModelProvider(gen, blockstates.getExistingHelper()));
            gen.addProvider(new TropicraftLangProvider(gen));
        }
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT || FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER ) {
            TropicraftBlockTagsProvider blockTags = new TropicraftBlockTagsProvider(gen);
            gen.addProvider(blockTags);
            gen.addProvider(new TropicraftItemTagsProvider(gen, blockTags));
            gen.addProvider(new TropicraftRecipeProvider(gen));
            gen.addProvider(new TropicraftLootTableProvider(gen));
            gen.addProvider(new TropicraftEntityTypeTagsProvider(gen));

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
        if (!"true".equals(System.getProperty("tropicraft.generateData"))) {
            System.out.println("Tropicraft: Skipping data generation. System property not set.");
            return;
        }

//        var outputPath = Paths.get("../src/generated/resources");
//        //var existingData = System.getProperty("tropicraft.generateData.existingData").split(";");
//        var existingDataPaths = Arrays.asList(Paths.get("../src/main/resources"));//Arrays.stream("src/main/resources".split(";")).map(Paths::get).toList();

        var outputPath = Paths.get(System.getProperty("tropicraft.generateData.outputPath"));
        var existingData = System.getProperty("tropicraft.generateData.existingData").split(";");
        var existingDataPaths = Arrays.stream(existingData).map(Paths::get).toList();

        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
//        new Tropicraft().onInitialize();
//        new TropicraftClient().onInitializeClient();

        try {
            dump(outputPath, existingDataPaths);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        System.exit(0);
    }

    @Override
    public void onInitializeClient() {
        runIfEnabled();
    }


}
