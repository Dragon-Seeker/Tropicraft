package net.tropicraft.core.common.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.mixin.object.builder.SpawnRestrictionAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Material;
import net.tropicraft.Constants;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.dimension.TropicraftDimension;
import net.tropicraft.core.common.entity.egg.*;
import net.tropicraft.core.common.entity.hostile.AshenEntity;
import net.tropicraft.core.common.entity.hostile.TropiSkellyEntity;
import net.tropicraft.core.common.entity.hostile.TropiSpiderEntity;
import net.tropicraft.core.common.entity.neutral.*;
import net.tropicraft.core.common.entity.passive.*;
import net.tropicraft.core.common.entity.passive.basilisk.BasiliskLizardEntity;
import net.tropicraft.core.common.entity.passive.monkey.SpiderMonkeyEntity;
import net.tropicraft.core.common.entity.placeable.*;
import net.tropicraft.core.common.entity.projectile.ExplodingCoconutEntity;
import net.tropicraft.core.common.entity.projectile.LavaBallEntity;
import net.tropicraft.core.common.entity.projectile.PoisonBlotEntity;
import net.tropicraft.core.common.entity.underdasea.*;

import java.util.Random;
import java.util.function.Supplier;

//@Mod.EventBusSubscriber(modid = Constants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TropicraftEntities {

    private static final float EGG_WIDTH = 0.4F;
    private static final float EGG_HEIGHT = 0.5F;

    //public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Constants.MODID);

    public static final EntityType<EntityKoaHunter> KOA_HUNTER = register("koa", TropicraftEntities::koaHunter);
    public static final EntityType<TropiCreeperEntity> TROPI_CREEPER = register("tropicreeper", TropicraftEntities::tropicreeper);
    public static final EntityType<IguanaEntity> IGUANA = register("iguana", TropicraftEntities::iguana);
    public static final EntityType<UmbrellaEntity> UMBRELLA = register("umbrella", TropicraftEntities::umbrella);
    public static final EntityType<ChairEntity> CHAIR = register("chair", TropicraftEntities::chair);
    public static final EntityType<BeachFloatEntity> BEACH_FLOAT = register("beach_float", TropicraftEntities::beachFloat);
    public static final EntityType<TropiSkellyEntity> TROPI_SKELLY = register("tropiskelly", TropicraftEntities::tropiskelly);
    public static final EntityType<EIHEntity> EIH = register("eih", TropicraftEntities::eih);
    public static final EntityType<WallItemEntity> WALL_ITEM = register("wall_item", TropicraftEntities::wallItem);
    public static final EntityType<BambooItemFrame> BAMBOO_ITEM_FRAME = register("bamboo_item_frame", TropicraftEntities::bambooItemFrame);
    // TODO: Register again when volcano eruption is finished
    public static final EntityType<LavaBallEntity> LAVA_BALL = null;//register("lava_ball", TropicraftEntities::lavaBall);
    public static final EntityType<SeaTurtleEntity> SEA_TURTLE = register("turtle", TropicraftEntities::turtle);
    public static final EntityType<MarlinEntity> MARLIN = register("marlin", TropicraftEntities::marlin);
    public static final EntityType<FailgullEntity> FAILGULL = register("failgull", TropicraftEntities::failgull);
    public static final EntityType<TropicraftDolphinEntity> DOLPHIN = register("dolphin", TropicraftEntities::dolphin);
    public static final EntityType<SeahorseEntity> SEAHORSE = register("seahorse", TropicraftEntities::seahorse);
    public static final EntityType<PoisonBlotEntity> POISON_BLOT = register("poison_blot", TropicraftEntities::poisonBlot);
    public static final EntityType<TreeFrogEntity> TREE_FROG = register("tree_frog", TropicraftEntities::treeFrog);
    public static final EntityType<SeaUrchinEntity> SEA_URCHIN = register("sea_urchin", TropicraftEntities::seaUrchin);
    public static final EntityType<SeaUrchinEggEntity> SEA_URCHIN_EGG_ENTITY = register("sea_urchin_egg", TropicraftEntities::seaUrchinEgg);
    public static final EntityType<StarfishEntity> STARFISH = register("starfish", TropicraftEntities::starfish);
    public static final EntityType<StarfishEggEntity> STARFISH_EGG = register("starfish_egg", TropicraftEntities::starfishEgg);
    public static final EntityType<VMonkeyEntity> V_MONKEY = register("v_monkey", TropicraftEntities::vervetMonkey);
    public static final EntityType<SardineEntity> RIVER_SARDINE = register("sardine", TropicraftEntities::riverSardine);
    public static final EntityType<PiranhaEntity> PIRANHA = register("piranha", TropicraftEntities::piranha);
    public static final EntityType<TropicraftTropicalFishEntity> TROPICAL_FISH = register("tropical_fish", TropicraftEntities::tropicalFish);
    public static final EntityType<EagleRayEntity> EAGLE_RAY = register("eagle_ray", TropicraftEntities::eagleRay);
    public static final EntityType<TropiSpiderEntity> TROPI_SPIDER = register("tropi_spider", TropicraftEntities::tropiSpider);
    public static final EntityType<TropiSpiderEggEntity> TROPI_SPIDER_EGG = register("tropi_spider_egg", TropicraftEntities::tropiSpiderEgg);
    public static final EntityType<AshenMaskEntity> ASHEN_MASK = register("ashen_mask", TropicraftEntities::ashenMask);
    public static final EntityType<AshenEntity> ASHEN = register("ashen", TropicraftEntities::ashen);
    public static final EntityType<ExplodingCoconutEntity> EXPLODING_COCONUT = register("exploding_coconut", TropicraftEntities::explodingCoconut);
    public static final EntityType<SharkEntity> HAMMERHEAD = register("hammerhead", TropicraftEntities::hammerhead);
    public static final EntityType<SeaTurtleEggEntity> SEA_TURTLE_EGG = register("turtle_egg", TropicraftEntities::turtleEgg);
    public static final EntityType<TropiBeeEntity> TROPI_BEE = register("tropibee", TropicraftEntities::tropiBee);
    public static final EntityType<CowktailEntity> COWKTAIL = register("cowktail", TropicraftEntities::cowktail);
    public static final EntityType<ManOWarEntity> MAN_O_WAR = register("man_o_war", TropicraftEntities::manOWar);
    public static final EntityType<TapirEntity> TAPIR = register("tapir", TropicraftEntities::tapir);
    public static final EntityType<JaguarEntity> JAGUAR = register("jaguar", TropicraftEntities::jaguar);
    public static final EntityType<BasiliskLizardEntity> BROWN_BASILISK_LIZARD = register("brown_basilisk_lizard", TropicraftEntities::basiliskLizard);
    public static final EntityType<BasiliskLizardEntity> GREEN_BASILISK_LIZARD = register("green_basilisk_lizard", TropicraftEntities::basiliskLizard);
    public static final EntityType<HummingbirdEntity> HUMMINGBIRD = register("hummingbird", TropicraftEntities::hummingbird);
    public static final EntityType<FiddlerCrabEntity> FIDDLER_CRAB = register("fiddler_crab", TropicraftEntities::fiddlerCrab);
    public static final EntityType<SpiderMonkeyEntity> SPIDER_MONKEY = register("spider_monkey", TropicraftEntities::spiderMonkey);
    public static final EntityType<WhiteLippedPeccaryEntity> WHITE_LIPPED_PECCARY = register("white_lipped_peccary", TropicraftEntities::whiteLippedPeccary);
    public static final EntityType<CuberaEntity> CUBERA = register("cubera", TropicraftEntities::cubera);

    public static <E extends Entity, T extends EntityType<E>> EntityType<E> register(String id, Supplier<FabricEntityTypeBuilder<E>> builder) { //FabricEntityTypeBuilder<T> builder,
        return (EntityType) Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(Constants.MODID, id), builder.get().build());
    }

    // TODO review -- tracking range is in chunks...these values seem way too high

    private static FabricEntityTypeBuilder<CowktailEntity> cowktail() {
        return FabricEntityTypeBuilder.create(MobCategory.MONSTER, CowktailEntity::new)
                .dimensions(EntityDimensions
                        .fixed(0.9F, 1.4F))
                .trackRangeBlocks(10)
                .trackedUpdateRate(3)
                .forceTrackedVelocityUpdates(true);
    }

    private static FabricEntityTypeBuilder<ManOWarEntity> manOWar() {
        return FabricEntityTypeBuilder.create(MobCategory.WATER_AMBIENT, ManOWarEntity::new)
                .dimensions(EntityDimensions
                        .fixed(0.6F, 0.8F)
                )
                .trackRangeBlocks(10)
                .trackedUpdateRate(3)
                .forceTrackedVelocityUpdates(true);
    }

    private static FabricEntityTypeBuilder<TropiBeeEntity> tropiBee() {
        return FabricEntityTypeBuilder.create(MobCategory.MONSTER, TropiBeeEntity::new)
                .dimensions(EntityDimensions
                        .fixed(0.4F, 0.6F)
                )
                .trackRangeBlocks(8)
                .trackedUpdateRate(3)
                .forceTrackedVelocityUpdates(true);
    }

    private static FabricEntityTypeBuilder<SeaTurtleEggEntity> turtleEgg() {
        return FabricEntityTypeBuilder.create(MobCategory.MONSTER, SeaTurtleEggEntity::new)
                .dimensions(EntityDimensions
                        .fixed(EGG_WIDTH, EGG_HEIGHT)
                )
                .trackRangeBlocks(6)
                .trackedUpdateRate(3)
                .forceTrackedVelocityUpdates(false);
    }

    private static FabricEntityTypeBuilder<SharkEntity> hammerhead() {
        return FabricEntityTypeBuilder.create(MobCategory.WATER_CREATURE, SharkEntity::new)
                .dimensions(EntityDimensions
                        .fixed(2.4F, 1.4F))
                .trackRangeBlocks(5)
                .trackedUpdateRate(2)
                .forceTrackedVelocityUpdates(true);
    }

    private static FabricEntityTypeBuilder<ExplodingCoconutEntity> explodingCoconut() {
        return FabricEntityTypeBuilder
                .create(MobCategory.MISC, (EntityType.EntityFactory<ExplodingCoconutEntity>) ExplodingCoconutEntity::new)
                .dimensions(EntityDimensions
                        .fixed(0.25F, 0.25F))
                .trackRangeBlocks(4)
                .trackedUpdateRate(10)
                .forceTrackedVelocityUpdates(true);
    }

    private static FabricEntityTypeBuilder<AshenMaskEntity> ashenMask() {
        return FabricEntityTypeBuilder.<AshenMaskEntity>create( MobCategory.MISC, AshenMaskEntity::new)
                .dimensions(EntityDimensions
                        .fixed(0.8F, 0.2F))
                .trackRangeBlocks(6)
                .trackedUpdateRate(100)
                .forceTrackedVelocityUpdates(true);
    }

    private static FabricEntityTypeBuilder<AshenEntity> ashen() {
        return FabricEntityTypeBuilder.create(MobCategory.MONSTER, AshenEntity::new )
                .dimensions(EntityDimensions
                        .fixed(0.5F, 1.3F))
                .trackRangeBlocks(8)
                .trackedUpdateRate(3)
                .forceTrackedVelocityUpdates(true);
    }

    private static FabricEntityTypeBuilder<TropiSpiderEntity> tropiSpider() {
        return FabricEntityTypeBuilder.create(MobCategory.MONSTER, TropiSpiderEntity::new)
                .dimensions(EntityDimensions
                        .fixed(1.4F, 0.9F))
                .trackRangeBlocks(8)
                .trackedUpdateRate(3)
                .forceTrackedVelocityUpdates(true);
    }

    private static FabricEntityTypeBuilder<TropiSpiderEggEntity> tropiSpiderEgg() {
        return FabricEntityTypeBuilder.create(MobCategory.MONSTER, TropiSpiderEggEntity::new)
                .dimensions(EntityDimensions
                        .fixed(EGG_WIDTH, EGG_HEIGHT))
                .trackRangeBlocks(6)
                .trackedUpdateRate(10)
                .forceTrackedVelocityUpdates(false);
    }

    private static FabricEntityTypeBuilder<EagleRayEntity> eagleRay() {
        return FabricEntityTypeBuilder.create(MobCategory.WATER_CREATURE, EagleRayEntity::new )
                .dimensions(EntityDimensions
                        .fixed(2F, 0.4F))
                .trackRangeBlocks(8)
                .trackedUpdateRate(3)
                .forceTrackedVelocityUpdates(true);
    }

    private static FabricEntityTypeBuilder<TropicraftTropicalFishEntity> tropicalFish() {
        return FabricEntityTypeBuilder.create(MobCategory.WATER_AMBIENT, TropicraftTropicalFishEntity::new)
                .dimensions(EntityDimensions
                        .fixed(0.3F, 0.4F)
                )
                .trackRangeBlocks(4)
                .trackedUpdateRate(3)
                .forceTrackedVelocityUpdates(true);
    }

    private static FabricEntityTypeBuilder<SardineEntity> riverSardine() {
        return FabricEntityTypeBuilder.create(MobCategory.WATER_AMBIENT, SardineEntity::new)
                .dimensions(EntityDimensions
                        .fixed(0.3F, 0.4F)
                )
                .trackRangeBlocks(4)
                .trackedUpdateRate(3)
                .forceTrackedVelocityUpdates(true);
    }

    private static FabricEntityTypeBuilder<PiranhaEntity> piranha() {
        return FabricEntityTypeBuilder.create(MobCategory.WATER_CREATURE, PiranhaEntity::new)
                .dimensions(EntityDimensions
                        .fixed(0.3F, 0.4F)
                )
                .trackRangeBlocks(4)
                .trackedUpdateRate(3)
                .forceTrackedVelocityUpdates(true);
    }

    private static FabricEntityTypeBuilder<VMonkeyEntity> vervetMonkey() {
        return FabricEntityTypeBuilder.create(MobCategory.MONSTER, VMonkeyEntity::new)
                .dimensions(EntityDimensions
                        .fixed(0.8F, 0.8F)
                )
                .trackRangeBlocks(8)
                .trackedUpdateRate(3)
                .forceTrackedVelocityUpdates(true);
    }

    private static FabricEntityTypeBuilder<StarfishEggEntity> starfishEgg() {
        return FabricEntityTypeBuilder.create(MobCategory.WATER_AMBIENT, StarfishEggEntity::new)
                .dimensions(EntityDimensions
                        .fixed(0.4F, 0.5F)
                )
                .trackRangeBlocks(8)
                .trackedUpdateRate(3)
                .forceTrackedVelocityUpdates(false);
    }

    private static FabricEntityTypeBuilder<StarfishEntity> starfish() {
        return FabricEntityTypeBuilder.create(MobCategory.WATER_AMBIENT, StarfishEntity::new)
                .dimensions(EntityDimensions
                        .fixed(0.5F, 0.5F)
                )
                .trackRangeBlocks(4)
                .trackedUpdateRate(15)
                .forceTrackedVelocityUpdates(true);
    }

    private static FabricEntityTypeBuilder<SeaUrchinEggEntity> seaUrchinEgg() {
        return FabricEntityTypeBuilder.create(MobCategory.WATER_AMBIENT, SeaUrchinEggEntity::new)
                .dimensions(EntityDimensions
                        .fixed(0.4F, 0.5F)
                )
                .trackRangeBlocks(6)
                .trackedUpdateRate(15)
                .forceTrackedVelocityUpdates(false);
    }

    private static FabricEntityTypeBuilder<SeaUrchinEntity> seaUrchin() {
        return FabricEntityTypeBuilder.create(MobCategory.WATER_AMBIENT, SeaUrchinEntity::new)
                .dimensions(EntityDimensions
                        .fixed(0.5F, 0.5F)
                )
                .trackRangeBlocks(8)
                .trackedUpdateRate(3)
                .forceTrackedVelocityUpdates(true);
    }

    private static FabricEntityTypeBuilder<TreeFrogEntity> treeFrog() {
        return FabricEntityTypeBuilder.create(MobCategory.MONSTER, TreeFrogEntity::new)
                .dimensions(EntityDimensions
                        .fixed(0.6F, 0.4F)
                )
                .trackRangeBlocks(8)
                .trackedUpdateRate(3)
                .forceTrackedVelocityUpdates(true);
    }

    private static FabricEntityTypeBuilder<PoisonBlotEntity> poisonBlot() {
        return FabricEntityTypeBuilder.<PoisonBlotEntity>create(MobCategory.MISC, PoisonBlotEntity::new)
                .dimensions(EntityDimensions
                        .fixed(0.25F, 0.25F)
                )
                .trackRangeBlocks(4)
                .trackedUpdateRate(20)
                .forceTrackedVelocityUpdates(true);
    }

    private static FabricEntityTypeBuilder<SeahorseEntity> seahorse() {
        return FabricEntityTypeBuilder.create(MobCategory.WATER_AMBIENT, SeahorseEntity::new)
                .dimensions(EntityDimensions
                        .fixed(0.5F, 0.6F)
                )
                .trackRangeBlocks(8)
                .trackedUpdateRate(3)
                .forceTrackedVelocityUpdates(true);
    }

    private static FabricEntityTypeBuilder<TropicraftDolphinEntity> dolphin() {
        return FabricEntityTypeBuilder.create(MobCategory.WATER_CREATURE, TropicraftDolphinEntity::new)
                .dimensions(EntityDimensions
                        .fixed(1.4F, 0.5F)
                )
                .trackRangeBlocks(8)
                .trackedUpdateRate(3)
                .forceTrackedVelocityUpdates(true);
    }

    private static FabricEntityTypeBuilder<FailgullEntity> failgull() {
        return FabricEntityTypeBuilder.create(MobCategory.AMBIENT, FailgullEntity::new)
                .dimensions(EntityDimensions
                        .fixed(0.4F, 0.6F)
                )
                .trackRangeBlocks(8)
                .trackedUpdateRate(3)
                .forceTrackedVelocityUpdates(true);
    }

    private static FabricEntityTypeBuilder<MarlinEntity> marlin() {
        return FabricEntityTypeBuilder.create(MobCategory.WATER_CREATURE, MarlinEntity::new)
                .dimensions(EntityDimensions
                        .fixed(1.4F, 0.95F)
                )
                .trackRangeBlocks(8)
                .trackedUpdateRate(3)
                .forceTrackedVelocityUpdates(true);
    }

    private static FabricEntityTypeBuilder<SeaTurtleEntity> turtle() {
        return FabricEntityTypeBuilder.create(MobCategory.MONSTER, SeaTurtleEntity::new)
                .dimensions(EntityDimensions
                        .fixed(0.8F, 0.35F)
                )
                .trackRangeBlocks(8)
                .trackedUpdateRate(3)
                .forceTrackedVelocityUpdates(true);
    }

    private static FabricEntityTypeBuilder<BambooItemFrame> bambooItemFrame() {
        return FabricEntityTypeBuilder
                .create(MobCategory.MISC, (EntityType.EntityFactory<BambooItemFrame>) BambooItemFrame::new)
                .dimensions(EntityDimensions
                        .fixed(0.5F, 0.5F))
                .trackRangeBlocks(8)
                .trackedUpdateRate(3)
                .forceTrackedVelocityUpdates(false);
    }

    private static FabricEntityTypeBuilder<LavaBallEntity> lavaBall() {
        return FabricEntityTypeBuilder.<LavaBallEntity>create(MobCategory.MISC, LavaBallEntity::new)
                .dimensions(EntityDimensions
                        .fixed(1.0F, 1.0F)
                )
                .trackRangeBlocks(8)
                .trackedUpdateRate(3)
                .forceTrackedVelocityUpdates(true);
    }

    private static FabricEntityTypeBuilder<WallItemEntity> wallItem() {
        return FabricEntityTypeBuilder.<WallItemEntity>create(MobCategory.MISC, WallItemEntity::new)
                .dimensions(EntityDimensions
                        .fixed(0.5F, 0.5F)
                )
                .trackRangeBlocks(8)
                .trackedUpdateRate(Integer.MAX_VALUE)
                .forceTrackedVelocityUpdates(false);
    }

    private static FabricEntityTypeBuilder<EIHEntity> eih() {
        return FabricEntityTypeBuilder.create(MobCategory.MONSTER, EIHEntity::new)
                .dimensions(EntityDimensions
                        .fixed(1.2F, 3.25F)
                )
                .trackRangeBlocks(8)
                .trackedUpdateRate(3)
                .forceTrackedVelocityUpdates(true);
    }

    private static FabricEntityTypeBuilder<TropiSkellyEntity> tropiskelly() {
        return FabricEntityTypeBuilder.create(MobCategory.MONSTER, TropiSkellyEntity::new)
                .dimensions(EntityDimensions
                        .fixed(0.7F, 1.95F)
                )
                .trackRangeBlocks(8)
                .trackedUpdateRate(3)
                .forceTrackedVelocityUpdates(true);
    }

    private static FabricEntityTypeBuilder<UmbrellaEntity> umbrella() {
        return FabricEntityTypeBuilder.<UmbrellaEntity>create(MobCategory.MISC, UmbrellaEntity::new)
                .dimensions(EntityDimensions
                        .fixed(1.0F, 4.0F)
                )
                .trackRangeBlocks(10)
                .trackedUpdateRate(3)
                .forceTrackedVelocityUpdates(false);
    }

    private static FabricEntityTypeBuilder<ChairEntity> chair() {
        return FabricEntityTypeBuilder.<ChairEntity>create(MobCategory.MISC, ChairEntity::new)
                .dimensions(EntityDimensions
                        .fixed(1.5F, 0.5F)
                )
                .trackRangeBlocks(10)
                .trackedUpdateRate(3)
                .forceTrackedVelocityUpdates(false);
    }

    private static FabricEntityTypeBuilder<BeachFloatEntity> beachFloat() {
        return FabricEntityTypeBuilder.<BeachFloatEntity>create(MobCategory.MISC, BeachFloatEntity::new)
                .dimensions(EntityDimensions
                        .fixed(2F, 0.175F)
                )
                .trackRangeBlocks(8)
                .trackedUpdateRate(3)
                .forceTrackedVelocityUpdates(false);
    }

    private static FabricEntityTypeBuilder<IguanaEntity> iguana() {
        return FabricEntityTypeBuilder.create(MobCategory.MONSTER, IguanaEntity::new)
                .dimensions(EntityDimensions
                        .fixed(1.0F, 0.4F)
                )
                .trackRangeBlocks(8)
                .trackedUpdateRate(3)
                .fireImmune()
                .forceTrackedVelocityUpdates(true);
    }

    private static FabricEntityTypeBuilder<EntityKoaHunter> koaHunter() {
        return FabricEntityTypeBuilder.create(MobCategory.MISC, EntityKoaHunter::new)
                .dimensions(EntityDimensions
                        .fixed(0.6F, 1.95F)
                )
                .trackRangeBlocks(8)
                .trackedUpdateRate(3)
                .fireImmune()
                .forceTrackedVelocityUpdates(true);
    }

    private static FabricEntityTypeBuilder<TropiCreeperEntity> tropicreeper() {
        return FabricEntityTypeBuilder.create(MobCategory.MONSTER, TropiCreeperEntity::new)
                .dimensions(EntityDimensions
                        .fixed(0.6F, 1.7F)
                )
                .trackRangeBlocks(8)
                .trackedUpdateRate(3)
                .forceTrackedVelocityUpdates(true);
    }

    private static FabricEntityTypeBuilder<TapirEntity> tapir() {
        return FabricEntityTypeBuilder.create(MobCategory.MONSTER, TapirEntity::new)
                .dimensions(EntityDimensions
                        .fixed(0.8F, 1.0F)
                )
                .trackRangeBlocks(10)
                .trackedUpdateRate(3)
                .forceTrackedVelocityUpdates(true);
    }

    private static FabricEntityTypeBuilder<JaguarEntity> jaguar() {
        return FabricEntityTypeBuilder.create(MobCategory.MONSTER, JaguarEntity::new)
                .dimensions(EntityDimensions
                        .fixed(0.9F, 1.0F)
                )
                .trackRangeBlocks(10)
                .trackedUpdateRate(3)
                .forceTrackedVelocityUpdates(true);
    }

    private static FabricEntityTypeBuilder<BasiliskLizardEntity> basiliskLizard() {
        return FabricEntityTypeBuilder.create(MobCategory.MONSTER, BasiliskLizardEntity::new)
                .dimensions(EntityDimensions
                        .fixed(0.7F, 0.4F)
                )
                .trackRangeBlocks(8)
                .trackedUpdateRate(3)
                .forceTrackedVelocityUpdates(true);
    }

    private static FabricEntityTypeBuilder<HummingbirdEntity> hummingbird() {
        return FabricEntityTypeBuilder.create(MobCategory.MONSTER, HummingbirdEntity::new)
                .dimensions(EntityDimensions
                        .fixed(0.5F, 0.5F)
                )
                .trackRangeBlocks(8)
                .trackedUpdateRate(3)
                .forceTrackedVelocityUpdates(true);
    }

    private static FabricEntityTypeBuilder<FiddlerCrabEntity> fiddlerCrab() {
        return FabricEntityTypeBuilder.create(MobCategory.MONSTER, FiddlerCrabEntity::new)
                .dimensions(EntityDimensions
                        .fixed(0.5F, 0.2F)
                )
                .trackRangeBlocks(10)
                .trackedUpdateRate(3)
                .forceTrackedVelocityUpdates(true);
    }

    private static FabricEntityTypeBuilder<SpiderMonkeyEntity> spiderMonkey() {
        return FabricEntityTypeBuilder.create(MobCategory.MONSTER, SpiderMonkeyEntity::new)
                .dimensions(EntityDimensions
                        .fixed(0.5F, 0.6F)
                )
                .trackRangeBlocks(10)
                .trackedUpdateRate(3)
                .forceTrackedVelocityUpdates(true);
    }

    private static FabricEntityTypeBuilder<WhiteLippedPeccaryEntity> whiteLippedPeccary() {
        return FabricEntityTypeBuilder.create(MobCategory.MONSTER, WhiteLippedPeccaryEntity::new)
                .dimensions(EntityDimensions
                        .fixed(0.7F, 0.8F)
                )
                .trackRangeBlocks(8)
                .trackedUpdateRate(3)
                .forceTrackedVelocityUpdates(true);
    }

    private static FabricEntityTypeBuilder<CuberaEntity> cubera() {
        return FabricEntityTypeBuilder.create(MobCategory.WATER_CREATURE, CuberaEntity::new)
                .dimensions(EntityDimensions
                        .fixed(1.2F, 0.8F)
                )
                .trackRangeBlocks(8)
                .trackedUpdateRate(3)
                .forceTrackedVelocityUpdates(true);
    }

    public static void registerSpawns() {
        registerWaterSpawn(TROPICAL_FISH, AbstractFish::checkFishSpawnRules);
        registerWaterSpawn(RIVER_SARDINE, AbstractFish::checkFishSpawnRules);
        registerWaterSpawn(PIRANHA, AbstractFish::checkFishSpawnRules);
        registerWaterSpawn(DOLPHIN, TropicraftEntities::canSpawnOceanWaterMob);
        registerWaterSpawn(EAGLE_RAY, TropicraftEntities::canSpawnOceanWaterMob);
        registerWaterSpawn(MARLIN, TropicraftEntities::canSpawnOceanWaterMob);
        registerWaterSpawn(SEAHORSE, TropicraftEntities::canSpawnOceanWaterMob);
        registerWaterSpawn(SEA_URCHIN, TropicraftEntities::canSpawnOceanWaterMob);
        registerWaterSpawn(STARFISH, TropicraftEntities::canSpawnOceanWaterMob);
        registerWaterSpawn(HAMMERHEAD, TropicraftEntities::canSpawnOceanWaterMob);
        registerWaterSpawn(MAN_O_WAR, TropicraftEntities::canSpawnSurfaceOceanWaterMob);

        registerLandSpawn(KOA_HUNTER, TropicraftEntities::canAnimalSpawn);
        registerLandSpawn(TROPI_CREEPER, TropicraftEntities::canAnimalSpawn);
        registerLandSpawn(IGUANA, TropicraftEntities::canAnimalSpawn);
        registerLandSpawn(TROPI_SKELLY, Monster::checkMonsterSpawnRules);
        registerLandSpawn(TROPI_SPIDER, Monster::checkMonsterSpawnRules);
        registerLandSpawn(EIH, TropicraftEntities::canAnimalSpawn);
        registerLandSpawn(SEA_TURTLE, SeaTurtleEntity::canSpawnOnLand);
        registerLandSpawn(TREE_FROG, TropicraftEntities::canAnimalSpawn);
        registerLandSpawn(V_MONKEY, TropicraftEntities::canAnimalSpawn);
        registerLandSpawn(COWKTAIL, TropicraftEntities::canAnimalSpawn);
        registerLandSpawn(TAPIR, TropicraftEntities::canAnimalSpawn);
        registerLandSpawn(JAGUAR, TropicraftEntities::canAnimalSpawn);
        registerLandSpawn(BROWN_BASILISK_LIZARD, TropicraftEntities::canAnimalSpawn);
        registerLandSpawn(GREEN_BASILISK_LIZARD, TropicraftEntities::canAnimalSpawn);
        registerLandSpawn(HUMMINGBIRD, HummingbirdEntity::canHummingbirdSpawnOn);
        registerNoRestrictionSpawn(FIDDLER_CRAB, FiddlerCrabEntity::canCrabSpawn);
        registerLandSpawn(SPIDER_MONKEY, TropicraftEntities::canAnimalSpawn);
        registerLandSpawn(WHITE_LIPPED_PECCARY, TropicraftEntities::canAnimalSpawn);
        registerWaterSpawn(CUBERA, TropicraftEntities::canSpawnOceanWaterMob);

        registerLandSpawn(ASHEN, Mob::checkMobSpawnRules);
        registerLandSpawn(FAILGULL, Mob::checkMobSpawnRules);
        registerLandSpawn(TROPI_BEE, Mob::checkMobSpawnRules);
        // TODO tropibee, or from nests?
    }

    public static boolean canAnimalSpawn(EntityType<? extends Mob> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, Random random) {
        BlockState groundState = worldIn.getBlockState(pos.below());
        return groundState.getBlock() == Blocks.GRASS_BLOCK
                || groundState.getMaterial() == Material.SAND
                || groundState.is(TropicraftTags.Blocks.MUD);
    }

    private static <T extends Mob> void registerLandSpawn(final EntityType<T> type, SpawnPlacements.SpawnPredicate<T> predicate) {
        SpawnRestrictionAccessor.callRegister(type, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, predicate);
    }

    private static <T extends Mob> void registerWaterSpawn(final EntityType<T> type, SpawnPlacements.SpawnPredicate<T> predicate) {
        SpawnRestrictionAccessor.callRegister(type, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, predicate);
    }

    private static <T extends Mob> void registerNoRestrictionSpawn(final EntityType<T> type,  SpawnPlacements.SpawnPredicate<T> predicate) {
        SpawnRestrictionAccessor.callRegister(type, SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, predicate);
    }

    public static <T extends Mob> boolean canSpawnOceanWaterMob(EntityType<T> waterMob, LevelAccessor world, MobSpawnType reason, BlockPos pos, Random rand) {
        int seaLevel = TropicraftDimension.getSeaLevel(world);
        return pos.getY() > 90 && pos.getY() < seaLevel && world.getFluidState(pos).is(FluidTags.WATER);
    }

    public static <T extends Mob> boolean canSpawnSurfaceOceanWaterMob(EntityType<T> waterMob, LevelAccessor world, MobSpawnType reason, BlockPos pos, Random rand) {
        int seaLevel = TropicraftDimension.getSeaLevel(world);
        return pos.getY() > seaLevel - 3 && pos.getY() < seaLevel && world.getFluidState(pos).is(FluidTags.WATER);
    }


    public static void onCreateEntityAttributes() {
        FabricDefaultAttributeRegistry.register(KOA_HUNTER, EntityKoaBase.createAttributes());
        FabricDefaultAttributeRegistry.register(TROPI_CREEPER, TropiCreeperEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(IGUANA, IguanaEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(TROPI_SKELLY, TropiSkellyEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(EIH, EIHEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(SEA_TURTLE, SeaTurtleEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(MARLIN, MarlinEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(FAILGULL, FailgullEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(DOLPHIN, TropicraftDolphinEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(SEAHORSE, SeahorseEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(TREE_FROG, TreeFrogEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(SEA_URCHIN, SeaUrchinEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(SEA_URCHIN_EGG_ENTITY, EggEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(STARFISH, StarfishEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(STARFISH_EGG, EggEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(V_MONKEY, VMonkeyEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(RIVER_SARDINE, SardineEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(PIRANHA, PiranhaEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(TROPICAL_FISH, TropicraftTropicalFishEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(EAGLE_RAY, EagleRayEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(TROPI_SPIDER, TropiSpiderEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(TROPI_SPIDER_EGG, EggEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(ASHEN, AshenEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(HAMMERHEAD, SharkEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(SEA_TURTLE_EGG, EggEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(TROPI_BEE, TropiBeeEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(COWKTAIL, CowktailEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(MAN_O_WAR, ManOWarEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(TAPIR, TapirEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(JAGUAR, JaguarEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(BROWN_BASILISK_LIZARD, BasiliskLizardEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(GREEN_BASILISK_LIZARD, BasiliskLizardEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(HUMMINGBIRD, HummingbirdEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(FIDDLER_CRAB, FiddlerCrabEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(SPIDER_MONKEY, SpiderMonkeyEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(WHITE_LIPPED_PECCARY, WhiteLippedPeccaryEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(CUBERA, CuberaEntity.createAttributes());
    }

    public static void init(){}
}
