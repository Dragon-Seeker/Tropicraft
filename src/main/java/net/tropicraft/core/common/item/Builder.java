package net.tropicraft.core.common.item;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraftforge.fml.RegistryObject;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.drinks.MixerRecipes;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.placeable.BeachFloatEntity;
import net.tropicraft.core.common.entity.placeable.ChairEntity;
import net.tropicraft.core.common.entity.placeable.FurnitureEntity;
import net.tropicraft.core.common.entity.placeable.UmbrellaEntity;
import net.tropicraft.core.common.item.scuba.ScubaFlippersItem;
import net.tropicraft.core.common.item.scuba.ScubaGogglesItem;
import net.tropicraft.core.common.item.scuba.ScubaHarnessItem;
import net.tropicraft.core.common.item.scuba.ScubaType;

import java.util.function.Function;
import java.util.function.Supplier;

public class Builder {
    
    public static Supplier<Item> item() {
        return item(getDefaultProperties());
    }
    
    public static Supplier<Item> item(Item.Properties properties) {
        return item(Item::new, properties);
    }
    
    public static <T> Supplier<T> item(Function<Item.Properties, T> ctor) {
        return item(ctor, getDefaultProperties());
    }
    
    public static <T> Supplier<T> item(Function<Item.Properties, T> ctor, Item.Properties properties) {
        return item(ctor, () -> properties);
    }
    
    public static <T> Supplier<T> item(Function<Item.Properties, T> ctor, Supplier<Item.Properties> properties) {
        return () -> ctor.apply(properties.get());
    }

    public static Supplier<BlockNamedItem> blockNamedItem(Supplier<? extends Block> block) {
        return item(p -> new BlockNamedItem(block.get(), p));
    }

    private static <T extends FurnitureEntity> Supplier<FurnitureItem<T>> furniture(Supplier<EntityType<T>> type, DyeColor color) {
        return item(p -> new FurnitureItem<>(p, type, color));
    }

    public static Supplier<FurnitureItem<UmbrellaEntity>> umbrella(final DyeColor color) {
        return furniture(TropicraftEntities.UMBRELLA, color);
    }
    
    public static Supplier<FurnitureItem<ChairEntity>> chair(final DyeColor color) {
        return furniture(TropicraftEntities.CHAIR, color);
    }
    
    public static Supplier<FurnitureItem<BeachFloatEntity>> beachFloat(final DyeColor color) {
        return furniture(TropicraftEntities.BEACH_FLOAT, color);
    }

    public static <T extends AbstractFishEntity> Supplier<Item> fishBucket(final Supplier<EntityType<T>> type) {
        return item(p -> new TropicraftFishBucketItem<>(type, Fluids.WATER, getDefaultProperties().stacksTo(1)));
    }

    public static Supplier<Item> shell() {
        return item(ShellItem::new);
    }

    public static Supplier<Item> food(final Food food) {
        return item(getDefaultProperties().food(food));
    }

    public static Supplier<CocktailItem> cocktail(final Drink drink) {
        return item(p -> {
            CocktailItem ret = new CocktailItem(drink, p);
            MixerRecipes.setDrinkItem(drink, ret);
            return ret;
        }, () -> getDefaultProperties().durability(0).stacksTo(1).craftRemainder(TropicraftItems.BAMBOO_MUG.get()));
    }

    public static Supplier<AshenMaskItem> mask(final AshenMasks mask) {
        return item(p -> new AshenMaskItem(ArmorMaterials.ASHEN_MASK, mask, p));
    }
    
    public static Supplier<TropicalMusicDiscItem> musicDisc(RecordMusic type) {
        return item(p -> new TropicalMusicDiscItem(type, p) {}, () -> getDefaultProperties().rarity(Rarity.RARE));
    }

    public static <T extends Entity> Supplier<Item> spawnEgg(final RegistryObject<EntityType<T>> type) {
        return item(p -> new TropicraftSpawnEgg<>(type, p), Builder::getDefaultProperties);
    }

    public static Supplier<Item> hoe(final IItemTier tier) {
        return item(p -> new HoeItem(tier, 0, -2.0f, getDefaultProperties()));
    }

    public static Supplier<Item> shovel(final IItemTier tier) {
        return item(p -> new ShovelItem(tier, 2.0f, -3.0f, getDefaultProperties()));
    }

    public static Supplier<Item> pickaxe(final IItemTier tier) {
        return item(p -> new PickaxeItem(tier, 2, -2.0f, getDefaultProperties()));
    }

    public static Supplier<Item> axe(final IItemTier tier) {
        return item(p -> new AxeItem(tier, 5.0f, -2.0f, getDefaultProperties()));
    }

    public static Supplier<Item> sword(final IItemTier tier) {
        return item(p -> new SwordItem(tier, 3, -3.0f, getDefaultProperties()));
    }

    public static Item.Properties getDefaultProperties() {
        return new Item.Properties().tab(Tropicraft.TROPICRAFT_ITEM_GROUP);
    }

    public static Supplier<Item> fireArmor(EquipmentSlotType slotType) {
        return item(p -> new FireArmorItem(slotType, getDefaultProperties().stacksTo(1).durability(300)));
    }

    public static Supplier<Item> scaleArmor(EquipmentSlotType slotType) {
        return item(p -> new ScaleArmorItem(slotType, getDefaultProperties().stacksTo(1)));
    }

    public static Supplier<ScubaGogglesItem> scubaGoggles(ScubaType type) {
        return item(p -> new ScubaGogglesItem(type, p));
    }
    
    public static Supplier<ScubaHarnessItem> scubaHarness(ScubaType type) {
        return item(p -> new ScubaHarnessItem(type, p));
    }
    
    public static Supplier<ScubaFlippersItem> scubaFlippers(ScubaType type) {
        return item(p -> new ScubaFlippersItem(type, p));
    }
}
