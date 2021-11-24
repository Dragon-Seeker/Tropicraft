package net.tropicraft.core.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.block.RedstoneWallTorchBlock;
import net.minecraft.world.phys.Vec3;
import net.shadow.fabric.api.client.rendering.v1.ArmorRenderingRegistry;
import net.tropicraft.Constants;
import net.tropicraft.core.client.armor.MaskArmorProvider;
import net.tropicraft.core.client.armor.ScubaArmorProvider;
import net.tropicraft.core.client.armor.StacheArmorProvider;
import net.tropicraft.core.client.entity.model.*;
import net.tropicraft.core.client.entity.render.*;
import net.tropicraft.core.client.scuba.ModelScubaGear;
import net.tropicraft.core.client.tileentity.*;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.tileentity.AirCompressorTileEntity;
import net.tropicraft.core.common.block.tileentity.BambooChestTileEntity;
import net.tropicraft.core.common.block.tileentity.DrinkMixerTileEntity;
import net.tropicraft.core.common.block.tileentity.TropicraftTileEntityTypes;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.item.AshenMaskItem;
import net.tropicraft.core.common.item.TropicraftItems;
import net.tropicraft.core.common.item.scuba.ScubaArmorItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Environment(EnvType.CLIENT)
public class ClientSetup {

    public static ModelLayerLocation KOA_HUNTER_LAYER;
    public static ModelLayerLocation TROPI_CREEPER_LAYER;
    public static ModelLayerLocation IGUANA_LAYER;
    public static ModelLayerLocation UMBRELLA_LAYER;
    public static ModelLayerLocation BEACH_FLOAT_LAYER;
    public static ModelLayerLocation CHAIR_LAYER;
    public static ModelLayerLocation TROPI_SKELLY_LAYER;
    public static ModelLayerLocation EIH_LAYER;
    public static ModelLayerLocation SEA_TURTLE_LAYER;
    public static ModelLayerLocation MARLIN_LAYER;
    public static ModelLayerLocation FAILGULL_LAYER;
    public static ModelLayerLocation DOLPHIN_LAYER;
    public static ModelLayerLocation SEAHORSE_LAYER;
    public static ModelLayerLocation TREE_FROG_LAYER;
    public static ModelLayerLocation SEA_URCHIN_LAYER;
    public static ModelLayerLocation SEA_URCHIN_EGG_ENTITY_LAYER;
    public static ModelLayerLocation STARFISH_EGG_LAYER;
    public static ModelLayerLocation V_MONKEY_LAYER;
    public static ModelLayerLocation PIRANHA_LAYER;
    public static ModelLayerLocation RIVER_SARDINE_LAYER;
    public static ModelLayerLocation TROPICAL_FISH_LAYER;
    public static ModelLayerLocation EAGLE_RAY_LAYER;
    public static ModelLayerLocation TROPI_SPIDER_EGG_LAYER;
    public static ModelLayerLocation ASHEN_LAYER;
    public static ModelLayerLocation HAMMERHEAD_LAYER;
    public static ModelLayerLocation SEA_TURTLE_EGG_LAYER;
    public static ModelLayerLocation TROPI_BEE_LAYER;
    public static ModelLayerLocation COWKTAIL_LAYER;
    public static ModelLayerLocation MAN_O_WAR_LAYER;
    public static ModelLayerLocation TAPIR_LAYER;
    public static ModelLayerLocation JAGUAR_LAYER;
    public static ModelLayerLocation BROWN_BASILISK_LIZARD_LAYER;
    public static ModelLayerLocation GREEN_BASILISK_LIZARD_LAYER;
    public static ModelLayerLocation HUMMINGBIRD_LAYER;
    public static ModelLayerLocation FIDDLER_CRAB_LAYER;
    public static ModelLayerLocation SPIDER_MONKEY_LAYER;
    public static ModelLayerLocation WHITE_LIPPED_PECCARY_LAYER;
    public static ModelLayerLocation CUBERA_LAYER;
    public static ModelLayerLocation BAMBOO_MUG;
    public static ModelLayerLocation EIHMACHINE_LAYER;
    public static ModelLayerLocation AIRCOMPRESSOR_LAYER;
    public static ArrayList<ModelLayerLocation> ASHEN_MASK_LAYERS = new ArrayList<>();//= registerMain("mask", PlayerHeadpieceModel::getTexturedModelData);
    public static ModelLayerLocation STACHE_LAYER;
    public static ModelLayerLocation CHEST_SCUBA_LAYER;
    public static ModelLayerLocation FEET_SCUBA_LAYER;
    public static ModelLayerLocation HEAD_SCUBA_LAYER;
    public static ModelLayerLocation TANK_SCUBA_LAYER;

    public static ModelLayerLocation BAMBOO_CHEST;
    public static ModelLayerLocation BAMBOO_DOUBLE_CHEST_LEFT;
    public static ModelLayerLocation BAMBOO_DOUBLE_CHEST_RIGHT;
    public static ModelLayerLocation ARMOR_CHEST_SCUBA_LAYER;
    public static ModelLayerLocation ARMOR_FEET_SCUBA_LAYER;
    public static ModelLayerLocation ARMOR_HEAD_SCUBA_LAYER;

    public static void setupBlockRenderLayers() {
        RenderType cutout = RenderType.cutout();
        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.AIR_COMPRESSOR, cutout);
        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.COCONUT, cutout);
        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.DRINK_MIXER, cutout);
        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.SIFTER, cutout);
        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.TIKI_TORCH, cutout);
        TropicraftBlocks.FLOWERS.forEach((key, value) -> BlockRenderLayerMap.INSTANCE.putBlock(value, RenderType.cutout()));
        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.PINEAPPLE, cutout);
        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.IRIS, cutout);
        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.COFFEE_BUSH, cutout);
        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.GOLDEN_LEATHER_FERN, cutout);
        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.TALL_GOLDEN_LEATHER_FERN, cutout);
        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.LARGE_GOLDEN_LEATHER_FERN, cutout);
        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.GRAPEFRUIT_SAPLING, cutout);
        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.LEMON_SAPLING, cutout);
        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.LIME_SAPLING, cutout);
        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.ORANGE_SAPLING, cutout);
        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.PAPAYA_SAPLING, cutout);
        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.MAHOGANY_SAPLING, cutout);
        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.PALM_SAPLING, cutout);
        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.PALM_TRAPDOOR, cutout);
        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.PALM_DOOR, cutout);

        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.MANGROVE_TRAPDOOR, cutout);
        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.MANGROVE_DOOR, cutout);
        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.BAMBOO_TRAPDOOR, cutout);
        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.BAMBOO_DOOR, cutout);
        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.BAMBOO_LADDER, cutout);
        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.BAMBOO_FLOWER_POT, cutout);
        TropicraftBlocks.BAMBOO_POTTED_TROPICS_PLANTS.forEach(value -> BlockRenderLayerMap.INSTANCE.putBlock(value, RenderType.cutout()));
        TropicraftBlocks.BAMBOO_POTTED_VANILLA_PLANTS.forEach(value -> BlockRenderLayerMap.INSTANCE.putBlock(value, RenderType.cutout()));
        TropicraftBlocks.VANILLA_POTTED_TROPICS_PLANTS.forEach(value -> BlockRenderLayerMap.INSTANCE.putBlock(value, RenderType.cutout()));
        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.REEDS, cutout);
        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.PAPAYA, cutout);

        RenderType cutoutMipped = RenderType.cutoutMipped();
        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.THATCH_STAIRS_FUZZY, cutoutMipped);

        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.RED_MANGROVE_PROPAGULE, cutout);
        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.TALL_MANGROVE_PROPAGULE, cutout);
        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.TEA_MANGROVE_PROPAGULE, cutout);
        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.BLACK_MANGROVE_PROPAGULE, cutout);
        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.RED_MANGROVE_ROOTS, cutoutMipped);
        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.LIGHT_MANGROVE_ROOTS, cutoutMipped);
        BlockRenderLayerMap.INSTANCE.putBlock(TropicraftBlocks.BLACK_MANGROVE_ROOTS, cutoutMipped);

        for (RedstoneWallTorchBlock block : TropicraftBlocks.JIGARBOV_WALL_TORCHES.values()) {
            BlockRenderLayerMap.INSTANCE.putBlock(block, cutoutMipped);
        }
    }


    public static void registerLayerDefinitions() {
        KOA_HUNTER_LAYER = registerLayer("koa_hunter", () -> KoaModel.create());
        TROPI_CREEPER_LAYER = registerLayer("tropi_creeper", () -> TropiCreeperModel.create());
        IGUANA_LAYER = registerLayer("iguana", () -> IguanaModel.create());
        UMBRELLA_LAYER = registerLayer("umbrella", () -> UmbrellaModel.create());
        BEACH_FLOAT_LAYER = registerLayer("beach_float", () -> BeachFloatModel.create());
        CHAIR_LAYER = registerLayer("chair", () -> ChairModel.create());
        TROPI_SKELLY_LAYER = registerLayer("tropi_skelly", () -> TropiSkellyModel.create());
        EIH_LAYER = registerLayer("eih", () -> EIHModel.create());
        SEA_TURTLE_LAYER = registerLayer("sea_turtle", () -> SeaTurtleModel.create());
        MARLIN_LAYER = registerLayer("marlin", () -> MarlinModel.create());
        FAILGULL_LAYER = registerLayer("failgull", () -> FailgullModel.create());
        DOLPHIN_LAYER = registerLayer("dolphin", () -> TropicraftDolphinModel.create());
        SEAHORSE_LAYER = registerLayer("seahorse", () -> SeahorseModel.create());
        TREE_FROG_LAYER = registerLayer("tree_frog", () -> TreeFrogModel.create());
        SEA_URCHIN_LAYER = registerLayer("sea_urchin", () -> SeaUrchinModel.create());
        SEA_URCHIN_EGG_ENTITY_LAYER = registerLayer("sea_urchin_egg", () -> EggModel.create());
        STARFISH_EGG_LAYER = registerLayer("starfish_egg", () -> EggModel.create());
        V_MONKEY_LAYER = registerLayer("v_monkey", () -> VMonkeyModel.create());
        PIRANHA_LAYER = registerLayer("piranha", () -> PiranhaModel.create());
        RIVER_SARDINE_LAYER = registerLayer("river_sardine", () -> SardineModel.create());
        TROPICAL_FISH_LAYER = registerLayer("tropical_fish", () -> TropicraftTropicalFishModel.create());
        EAGLE_RAY_LAYER = registerLayer("eagle_ray", () ->  EagleRayModel.create());
        TROPI_SPIDER_EGG_LAYER = registerLayer("tropi_spider_egg", () -> EggModel.create());
        ASHEN_LAYER = registerLayer("ashen", () -> AshenModel.create());
        HAMMERHEAD_LAYER = registerLayer("hammerhead", () -> SharkModel.create());
        SEA_TURTLE_EGG_LAYER  = registerLayer("turtle_egg", () -> EggModel.create());
        TROPI_BEE_LAYER = registerLayer("tropi_bee", () -> TropiBeeModel.createBodyLayer());
        COWKTAIL_LAYER = registerLayer("cowktail", () -> CowModel.createBodyLayer());
        MAN_O_WAR_LAYER = registerLayer("man_o_war", () -> ManOWarModel.create());
        TAPIR_LAYER = registerLayer("tapir", () -> TapirModel.create());
        JAGUAR_LAYER = registerLayer("jaguar", () -> JaguarModel.create());
        BROWN_BASILISK_LIZARD_LAYER = registerLayer("brown_basilisk_lizard", () -> BasiliskLizardModel.create());
        GREEN_BASILISK_LIZARD_LAYER = registerLayer("green_basilisk_lizard", () -> BasiliskLizardModel.create());
        HUMMINGBIRD_LAYER = registerLayer("hummingbird", () -> HummingbirdModel.create());
        FIDDLER_CRAB_LAYER = registerLayer("fiddler_crab", () -> FiddlerCrabModel.create());
        SPIDER_MONKEY_LAYER = registerLayer("spider_monkey", () -> SpiderMonkeyModel.create());
        WHITE_LIPPED_PECCARY_LAYER = registerLayer("white_lipped_peccary", () -> WhiteLippedPeccaryModel.create());
        CUBERA_LAYER = registerLayer("cubera", () -> CuberaModel.create());

        //Misc Layers
        BAMBOO_MUG = registerLayer("bamboo_mug", () -> BambooMugModel.create());

        //Block Entity's Layers
//        BAMBOO_CHEST = registerLayer("bamboo_chest", () -> BambooChestRenderer.getSingleTexturedModelData());
//        BAMBOO_DOUBLE_CHEST_LEFT = registerLayer("bamboo_double_chest_left", () -> BambooChestRenderer.getLeftDoubleTexturedModelData());
//        BAMBOO_DOUBLE_CHEST_RIGHT = registerLayer("bamboo_double_chest_right", () -> BambooChestRenderer.getRightDoubleTexturedModelData());

        BAMBOO_CHEST = registerLayer("bamboo_chest", () -> ChestRenderer.createSingleBodyLayer());
        BAMBOO_DOUBLE_CHEST_LEFT = registerLayer("bamboo_double_chest_left", () -> ChestRenderer.createDoubleBodyLeftLayer());
        BAMBOO_DOUBLE_CHEST_RIGHT = registerLayer("bamboo_double_chest_right", () -> ChestRenderer.createDoubleBodyRightLayer());

        EIHMACHINE_LAYER = registerLayer("drink_mixer", () -> EIHMachineModel.create());
        AIRCOMPRESSOR_LAYER = registerLayer("air_compressor", () -> EIHMachineModel.create());

        //Armor Layers
        armorRenderingRegistryInitialization();

        HEAD_SCUBA_LAYER = registerLayer("scuba_goggles", () -> ModelScubaGear.create());
        CHEST_SCUBA_LAYER = registerLayer("scuba_harness", () -> ModelScubaGear.create());
        FEET_SCUBA_LAYER = registerLayer("scuba_flippers", () -> ModelScubaGear.create());

        TANK_SCUBA_LAYER = registerLayer("pony_bottle", () -> ModelScubaGear.create());

        setupScubaGearModels();
    }

    public static void registerRenderers() {
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.KOA_HUNTER, KoaRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.TROPI_CREEPER, TropiCreeperRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.IGUANA, IguanaRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.UMBRELLA, UmbrellaRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.CHAIR, ChairRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.BEACH_FLOAT, BeachFloatRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.TROPI_SKELLY, TropiSkellyRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.EIH, EIHRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.WALL_ITEM, WallItemRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.BAMBOO_ITEM_FRAME, BambooItemFrameRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.SEA_TURTLE, SeaTurtleRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.MARLIN, MarlinRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.FAILGULL, FailgullRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.DOLPHIN, TropicraftDolphinRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.SEAHORSE, SeahorseRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.TREE_FROG, TreeFrogRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.POISON_BLOT, PoisonBlotRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.SEA_URCHIN, SeaUrchinRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.SEA_URCHIN_EGG_ENTITY, (context) -> new EggRenderer(context, SEA_URCHIN_EGG_ENTITY_LAYER));
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.STARFISH, StarfishRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.STARFISH_EGG, (context) -> new EggRenderer(context, STARFISH_EGG_LAYER));
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.V_MONKEY, VMonkeyRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.PIRANHA, PiranhaRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.RIVER_SARDINE, SardineRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.TROPICAL_FISH, TropicraftTropicalFishRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.EAGLE_RAY, EagleRayRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.TROPI_SPIDER, TropiSpiderRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.TROPI_SPIDER_EGG, (context) -> new EggRenderer(context, TROPI_SPIDER_EGG_LAYER));
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.ASHEN, AshenRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.ASHEN_MASK, AshenMaskRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.EXPLODING_COCONUT, (context) -> new ThrownItemRenderer<>(context));
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.HAMMERHEAD, SharkRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.SEA_TURTLE_EGG, (context) -> new EggRenderer(context, SEA_TURTLE_EGG_LAYER));
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.TROPI_BEE, TropiBeeRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.COWKTAIL, CowktailRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.MAN_O_WAR, ManOWarRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.TAPIR, TapirRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.JAGUAR, JaguarRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.BROWN_BASILISK_LIZARD, BasiliskLizardRenderer::brown);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.GREEN_BASILISK_LIZARD, BasiliskLizardRenderer::green);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.HUMMINGBIRD, HummingbirdRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.FIDDLER_CRAB, FiddlerCrabRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.SPIDER_MONKEY, SpiderMonkeyRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.WHITE_LIPPED_PECCARY, WhiteLippedPeccaryRenderer::new);
        EntityRendererRegistry.INSTANCE.register(TropicraftEntities.CUBERA, CuberaRenderer::new);

        setupTileEntityRenderers();

    }

    public static void setupTileEntityRenderers() {
        BlockEntityRendererRegistry.INSTANCE.register(TropicraftTileEntityTypes.BAMBOO_CHEST, BambooChestRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(TropicraftTileEntityTypes.DRINK_MIXER, DrinkMixerRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(TropicraftTileEntityTypes.SIFTER, SifterRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(TropicraftTileEntityTypes.AIR_COMPRESSOR, AirCompressorRenderer::new);

        BuiltinItemRendererRegistry.INSTANCE.register(TropicraftBlocks.BAMBOO_CHEST,
                (itemStack, transform, stack, source, light, overlay) ->
                        Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(new BambooChestTileEntity(BlockPos.ZERO, TropicraftBlocks.BAMBOO_CHEST.defaultBlockState()), stack, source, light, overlay));

        BuiltinItemRendererRegistry.INSTANCE.register(TropicraftBlocks.DRINK_MIXER,
                (itemStack, transform, stack, source, light, overlay) ->
                        Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(new DrinkMixerTileEntity(BlockPos.ZERO, TropicraftBlocks.DRINK_MIXER.defaultBlockState()), stack, source, light, overlay));

        BuiltinItemRendererRegistry.INSTANCE.register(TropicraftBlocks.AIR_COMPRESSOR,
                (itemStack, transform, stack, source, light, overlay) ->
                        Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(new AirCompressorTileEntity(BlockPos.ZERO, TropicraftBlocks.AIR_COMPRESSOR.defaultBlockState()), stack, source, light, overlay));

    }

    public static void setupDimensionRenderInfo() {
        DimensionSpecialEffects.EFFECTS.put(TropicraftDimension.WORLD.location(), new DimensionSpecialEffects(192.0F, true, DimensionSpecialEffects.SkyType.NORMAL, false, false) {
            @Override
            public Vec3 getBrightnessDependentFogColor(Vec3 color, float brightness) {
                return color.multiply(brightness * 0.94F + 0.06F, brightness * 0.94F + 0.06F, brightness * 0.91F + 0.09F);
            }

            @Override
            public boolean isFoggyAt(int x, int z) {
                return false;
            }
        });
    }



    public static void setupScubaGearModels(){
        ModelScubaGear.HEAD = ModelScubaGear.createModel(HEAD_SCUBA_LAYER, null, EquipmentSlot.CHEST);
        ModelScubaGear.CHEST = ModelScubaGear.createModel(CHEST_SCUBA_LAYER, null, EquipmentSlot.CHEST);
        ModelScubaGear.FEET = ModelScubaGear.createModel(FEET_SCUBA_LAYER, null, EquipmentSlot.CHEST);

        ModelScubaGear.tankModel = ModelScubaGear.createModel(TANK_SCUBA_LAYER, null, EquipmentSlot.CHEST);
    }

    @Environment(EnvType.CLIENT)
    private static ModelLayerLocation registerLayer(String id, EntityModelLayerRegistry.TexturedModelDataProvider textureModelData) {
        ModelLayerLocation modelLayer = new ModelLayerLocation(new ResourceLocation(Constants.MODID, id), "main");
        EntityModelLayerRegistry.registerModelLayer(modelLayer, textureModelData);
        return modelLayer;
    }

    @Environment(EnvType.CLIENT)
    public static void armorRenderingRegistryInitialization() {
        ArrayList<MaskArmorProvider> MASK_PROVIDER = new ArrayList<>();
        final List<AshenMaskItem> values = TropicraftItems.ASHEN_MASKS.values().asList();
        final int size = values.size();

        for (int i = 0; i < size; i++) {
            AshenMaskItem maskItem = values.get(i);
            ModelLayerLocation ashen_mask_layer = registerLayer("ashen_mask_" + maskItem.getMaskType().name().toLowerCase(Locale.ROOT), PlayerHeadpieceModel::create);
            ASHEN_MASK_LAYERS.add(ashen_mask_layer);

            MASK_PROVIDER.add(new MaskArmorProvider(maskItem.getMaskType()));
            //ArmorRendererRegistryImpl.register();
            ArmorRenderingRegistry.registerModel(MASK_PROVIDER.get(i), maskItem);
            ArmorRenderingRegistry.registerTexture(MASK_PROVIDER.get(i), maskItem);
        }

        scubaArmorRegister(TropicraftItems.PINK_SCUBA_GOGGLES);
        scubaArmorRegister(TropicraftItems.PINK_SCUBA_HARNESS);
        scubaArmorRegister(TropicraftItems.PINK_SCUBA_FLIPPERS);

        scubaArmorRegister(TropicraftItems.YELLOW_SCUBA_GOGGLES);
        scubaArmorRegister(TropicraftItems.YELLOW_SCUBA_HARNESS);
        scubaArmorRegister(TropicraftItems.YELLOW_SCUBA_FLIPPERS);

        STACHE_LAYER = registerLayer("nigel_stache", PlayerHeadpieceModel::create);

        final StacheArmorProvider STACHPROVIDER = new StacheArmorProvider();

        ArmorRenderingRegistry.registerModel(STACHPROVIDER, TropicraftItems.NIGEL_STACHE);
        ArmorRenderingRegistry.registerTexture(STACHPROVIDER, TropicraftItems.NIGEL_STACHE);
    }

    private static void scubaArmorRegister(ScubaArmorItem scubaItem){
        final ScubaArmorProvider SCUBAPROVIDER = new ScubaArmorProvider(scubaItem);
        ArmorRenderingRegistry.registerModel(SCUBAPROVIDER, scubaItem);
        ArmorRenderingRegistry.registerTexture(SCUBAPROVIDER, scubaItem);
    }
}
