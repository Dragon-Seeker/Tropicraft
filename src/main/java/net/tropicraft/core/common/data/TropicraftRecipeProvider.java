package net.tropicraft.core.common.data;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag.Named;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.tropicraft.Constants;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.block.TropicraftFlower;
import net.tropicraft.core.common.drinks.Drink;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.tropicraft.core.common.block.TropicraftBlocks.*;
import static net.tropicraft.core.common.block.TropicraftFlower.*;
import static net.tropicraft.core.common.item.TropicraftItems.*;

public class TropicraftRecipeProvider extends RecipeProvider {

    public TropicraftRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    public void run(HashCache cache) {
        super.run(cache);
        Path path = this.generator.getOutputFolder();
        Set<ResourceLocation> set = Sets.newHashSet();
        buildCraftingRecipesTF((recipe) -> {
            if (!set.add(recipe.getId())) {
                throw new IllegalStateException("Duplicate recipe " + recipe.getId());
            } else {
                saveRecipe(cache, recipe.serializeRecipe(), path.resolve("data/" + recipe.getId().getNamespace() + "/recipes/" + recipe.getId().getPath() + ".json"));
                JsonObject jsonobject = recipe.serializeAdvancement();
                if (jsonobject != null) {
                    saveAdvancement(cache, jsonobject, path.resolve("data/" + recipe.getId().getNamespace() + "/advancements/" + recipe.getAdvancementId().getPath() + ".json"));
                }
            }
        });
    }


    protected void buildCraftingRecipesTF(Consumer<FinishedRecipe> consumer) {
        ore(TropicraftTags.Items.AZURITE_ORE, () -> AZURITE, 0.3F, consumer);
        ore(TropicraftTags.Items.EUDIALYTE_ORE, () -> EUDIALYTE, 0.5F, consumer);
        ore(TropicraftTags.Items.ZIRCON_ORE, () -> ZIRCON, 0.5F, consumer);
        ore(TropicraftTags.Items.MANGANESE_ORE, () -> MANGANESE, 0.5F, consumer);
        ore(TropicraftTags.Items.SHAKA_ORE, () ->  SHAKA, 0.7F, consumer);
        
        storage(() -> AZURITE, () -> AZURITE_BLOCK, consumer);
        storage(() -> EUDIALYTE, () -> EUDIALYTE_BLOCK, consumer);
        storage(() -> ZIRCON, () -> ZIRCON_BLOCK, consumer);
        storage(() -> ZIRCONIUM, () -> ZIRCONIUM_BLOCK, consumer);
        storage(() -> MANGANESE, () -> MANGANESE_BLOCK, consumer);
        storage(() -> SHAKA, () -> SHAKA_BLOCK, consumer);

        pickaxe(() -> ZIRCON, () -> ZIRCON_PICKAXE, consumer);
        pickaxe(() -> ZIRCONIUM, () -> ZIRCONIUM_PICKAXE, consumer);
        pickaxe(() -> EUDIALYTE, () -> EUDIALYTE_PICKAXE, consumer);

        axe(() -> ZIRCON, () -> ZIRCON_AXE, consumer);
        axe(() -> ZIRCONIUM, () -> ZIRCONIUM_AXE, consumer);
        axe(() -> EUDIALYTE, () -> EUDIALYTE_AXE, consumer);

        shovel(() -> ZIRCON, () -> ZIRCON_SHOVEL, consumer);
        shovel(() -> ZIRCONIUM, () -> ZIRCONIUM_SHOVEL, consumer);
        shovel(() -> EUDIALYTE, () -> EUDIALYTE_SHOVEL, consumer);

        hoe(() -> ZIRCON, () -> ZIRCON_HOE, consumer);
        hoe(() -> ZIRCONIUM, () -> ZIRCONIUM_HOE, consumer);
        hoe(() -> EUDIALYTE, () -> EUDIALYTE_HOE, consumer);

        sword(() -> ZIRCON, () -> ZIRCON_SWORD, consumer);
        sword(() -> ZIRCONIUM, () -> ZIRCONIUM_SWORD, consumer);
        sword(() -> EUDIALYTE, () -> EUDIALYTE_SWORD, consumer);

        helmet(() -> SCALE, () -> SCALE_HELMET, consumer);
        chestplate(() -> SCALE, () -> SCALE_CHESTPLATE, consumer);
        leggings(() -> SCALE, () -> SCALE_LEGGINGS, consumer);
        boots(() -> SCALE, () -> SCALE_BOOTS, consumer);

        for (DyeColor color : DyeColor.values()) {
            ItemLike wool = Sheep.ITEM_BY_DYE.get(color);
            ShapedRecipeBuilder.shaped(UMBRELLAS.get(color))
                .pattern("WWW").pattern(" B ").pattern(" B ")
                .group(Constants.MODID + ":umbrellas")
                .define('W', wool)
                .define('B', BAMBOO_STICK)
                .unlockedBy("has_" + color.getSerializedName() + "_wool", this.has(wool))
                .save(consumer);
            
            ShapedRecipeBuilder.shaped(CHAIRS.get(color))
                .pattern("BWB").pattern("BWB").pattern("BWB")
                .group(Constants.MODID + ":chairs")
                .define('W', wool)
                .define('B', BAMBOO_STICK)
                .unlockedBy("has_" + color.getSerializedName() + "_wool", this.has(wool))
                .save(consumer);
            
            ShapedRecipeBuilder.shaped(BEACH_FLOATS.get(color))
                .pattern("WWW").pattern("BBB")
                .group(Constants.MODID + ":beach_floats")
                .define('W', wool)
                .define('B', Blocks.BAMBOO)
                .unlockedBy("has_" + color.getSerializedName() + "_wool", this.has(wool))
                .save(consumer);
        
            // TODO other colored items
        }
        
        // Override the vanilla recipe to output ours, it's tagged so it will behave the same
        ShapedRecipeBuilder.shaped(BAMBOO_STICK)
            .pattern("X").pattern("X")
            .define('X', Items.BAMBOO)
            .unlockedBy("has_bamboo", this.has(Items.BAMBOO))
            .save(consumer, new ResourceLocation("stick_from_bamboo_item"));
        
        ShapedRecipeBuilder.shaped(BAMBOO_SPEAR)
            .pattern("X ").pattern(" X")
            .define('X', BAMBOO_STICK)
            .unlockedBy("has_bamboo_stick", this.has(BAMBOO_STICK))
            .save(consumer);
        
        ShapelessRecipeBuilder.shapeless(RAW_COFFEE_BEAN)
            .requires(COFFEE_BERRY)
            .unlockedBy("has_coffee_bean", this.has(COFFEE_BERRY))
            .save(consumer);

        ShapelessRecipeBuilder.shapeless(ZIRCONIUM)
            .requires(AZURITE, 2)
            .requires(ZIRCON, 2)
            .unlockedBy("has_zircon", has(ZIRCON))
            .unlockedBy("has_azurite", has(AZURITE))
            .save(consumer);
        
        food(() -> RAW_COFFEE_BEAN, () -> ROASTED_COFFEE_BEAN, 0.1F, consumer);
        
        ShapedRecipeBuilder.shaped(BAMBOO_MUG)
            .pattern("X X").pattern("X X").pattern("XXX")
            .define('X', Items.BAMBOO)
            .unlockedBy("has_bamboo", this.has(Items.BAMBOO))
            .save(consumer);

        food(() -> Items.SEAGRASS, () -> TOASTED_NORI, 0.1F, consumer);
        food(() -> FRESH_MARLIN, () -> SEARED_MARLIN, 0.15F, consumer);
        food(() -> RAW_RAY, () -> COOKED_RAY, 0.15F, consumer);
        food(() -> FROG_LEG, () -> COOKED_FROG_LEG, 0.1F, consumer);
        food(() -> RAW_FISH, () -> COOKED_FISH, 0.1F, consumer);
        
        // Flowers to dye
        dye(COMMELINA_DIFFUSA, () -> Items.LIGHT_BLUE_DYE, 1, 2, consumer);
        dye(CANNA, () -> Items.YELLOW_DYE, 1, 2, consumer);
        dye(ORANGE_ANTHURIUM, () -> Items.ORANGE_DYE, 1, 2, consumer);
        dye(RED_ANTHURIUM, () -> Items.RED_DYE, 1, 2, consumer);
        dye(DRACAENA, () -> Items.GREEN_DYE, 1, 2, consumer);
        dye(() -> IRIS, () -> Items.PURPLE_DYE, 1, 4, consumer);

        // Bundles
        singleItem(() -> Blocks.BAMBOO, () -> BAMBOO_BUNDLE, 9, 1, consumer);
        singleItem(() -> Blocks.SUGAR_CANE, () -> THATCH_BUNDLE, 9, 1, consumer);
        
        // Planks
        planks(() -> MAHOGANY_LOG, () -> MAHOGANY_PLANKS, consumer);
        planks(() -> PALM_LOG, () -> PALM_PLANKS, consumer);
        
        bark(() -> MAHOGANY_LOG,() ->  MAHOGANY_WOOD, consumer);
        bark(() -> PALM_LOG, () -> PALM_WOOD, consumer);

        ShapelessRecipeBuilder.shapeless(Blocks.JUNGLE_LOG)
                .requires(PAPAYA_LOG)
                .unlockedBy("has_papaya_log", has(PAPAYA_LOG))
                .save(consumer);

        bark(() -> PAPAYA_LOG, () -> PAPAYA_WOOD, consumer);

        bark(() -> RED_MANGROVE_LOG, () -> RED_MANGROVE_WOOD, consumer);
        bark(() -> LIGHT_MANGROVE_LOG, () -> LIGHT_MANGROVE_WOOD, consumer);
        bark(() -> BLACK_MANGROVE_LOG, () -> BLACK_MANGROVE_WOOD, consumer);
        bark(() -> STRIPPED_MANGROVE_LOG, () -> STRIPPED_MANGROVE_WOOD, consumer);

        // Stairs
        stairs(() -> PALM_PLANKS, () -> PALM_STAIRS, "wooden_stairs", false, consumer);
        stairs(() -> MAHOGANY_PLANKS, () -> MAHOGANY_STAIRS, "wooden_stairs", false, consumer);
        stairs(() -> MANGROVE_PLANKS, () -> MANGROVE_STAIRS, "wooden_stairs", false, consumer);

        stairs(() -> THATCH_BUNDLE, () -> THATCH_STAIRS, null, false, consumer);
        stairs(() -> BAMBOO_BUNDLE, () -> BAMBOO_STAIRS, null, false, consumer);
        stairs(() -> CHUNK, () -> CHUNK_STAIRS, null, true, consumer);
        
        ShapedRecipeBuilder.shaped(THATCH_STAIRS_FUZZY, 4)
            .pattern("C  ").pattern("XC ").pattern("XXC")
            .define('X', THATCH_BUNDLE)
            .define('C', Items.SUGAR_CANE)
            .unlockedBy("has_thatch_bundle", this.has(THATCH_BUNDLE))
            .save(consumer);
        
        // Slabs
        slab(() -> PALM_PLANKS, () -> PALM_SLAB, "wooden_slab", false, consumer);
        slab(() -> MAHOGANY_PLANKS, () -> MAHOGANY_SLAB, "wooden_slab", false, consumer);
        slab(() -> MANGROVE_PLANKS, () -> MANGROVE_SLAB, "wooden_slab", false, consumer);

        slab(() -> THATCH_BUNDLE, () -> THATCH_SLAB, null, false, consumer);
        slab(() -> BAMBOO_BUNDLE, () -> BAMBOO_SLAB, null, false, consumer);
        slab(() -> CHUNK, () -> CHUNK_SLAB, null, true, consumer);
        
        // Fences
        fence(() -> PALM_PLANKS, () -> PALM_FENCE, "wooden_fence", consumer);
        fence(() -> MAHOGANY_PLANKS, () -> MAHOGANY_FENCE, "wooden_fence", consumer);
        fence(() -> MANGROVE_PLANKS, () -> MANGROVE_FENCE, "wooden_fence", consumer);

        fence(() -> THATCH_BUNDLE, () -> THATCH_FENCE, null, consumer);
        fence(() -> BAMBOO_BUNDLE, () -> BAMBOO_FENCE, null, consumer);
        fence(() -> CHUNK, () -> CHUNK_FENCE, null, consumer);
        
        // Fence Gates
        fenceGate(() -> PALM_PLANKS, () -> PALM_FENCE_GATE, "wooden_fence_gate", consumer);
        fenceGate(() -> MAHOGANY_PLANKS, () -> MAHOGANY_FENCE_GATE, "wooden_fence_gate", consumer);
        fenceGate(() -> MANGROVE_PLANKS, () -> MANGROVE_FENCE_GATE, "wooden_fence_gate", consumer);

        fenceGate(() -> THATCH_BUNDLE, () -> THATCH_FENCE_GATE, null, consumer);
        fenceGate(() -> BAMBOO_BUNDLE, () -> BAMBOO_FENCE_GATE, null, consumer);
        fenceGate(() -> CHUNK, () -> CHUNK_FENCE_GATE, null, consumer);
        
        // Walls
        wall(() -> CHUNK, () -> CHUNK_WALL, consumer);
        
        // Doors
        door(() -> PALM_PLANKS, () -> PALM_DOOR, "wooden_door", consumer);
        door(() -> MAHOGANY_PLANKS, () -> MAHOGANY_DOOR, "wooden_door", consumer);
        door(() -> MANGROVE_PLANKS, () -> MANGROVE_DOOR, "wooden_door", consumer);

        door(() -> THATCH_BUNDLE, () -> THATCH_DOOR, null, consumer);
        door(() -> BAMBOO_BUNDLE, () -> BAMBOO_DOOR, null, consumer);

        // Trap doors
        trapDoor(() -> PALM_PLANKS, () -> PALM_TRAPDOOR, "wooden_trapdoor", consumer);
        trapDoor(() -> MAHOGANY_PLANKS, () -> MAHOGANY_TRAPDOOR, "wooden_trapdoor", consumer);
        trapDoor(() -> MANGROVE_PLANKS, () -> MANGROVE_TRAPDOOR, "wooden_trapdoor", consumer);

        trapDoor(() -> THATCH_BUNDLE, () -> THATCH_TRAPDOOR, null, consumer);
        trapDoor(() -> BAMBOO_BUNDLE, () -> BAMBOO_TRAPDOOR, null, consumer);
        
        // Bongos
        bongo(() -> IGUANA_LEATHER, () -> MAHOGANY_PLANKS, 1, () -> SMALL_BONGO_DRUM, consumer);
        bongo(() -> IGUANA_LEATHER, () -> MAHOGANY_PLANKS, 2, () -> MEDIUM_BONGO_DRUM, consumer);
        bongo(() -> IGUANA_LEATHER, () -> MAHOGANY_PLANKS, 3, () -> LARGE_BONGO_DRUM, consumer);

        // Scuba gear
        goggles(() -> PINK_SCUBA_GOGGLES, () -> Items.PINK_DYE, consumer);
        goggles(() -> YELLOW_SCUBA_GOGGLES, () -> Items.YELLOW_DYE, consumer);

        flippers(() -> PINK_SCUBA_FLIPPERS, () -> Items.PINK_DYE, consumer);
        flippers(() -> YELLOW_SCUBA_FLIPPERS, () -> Items.YELLOW_DYE, consumer);

        harness(() -> PINK_SCUBA_HARNESS, () -> Items.PINK_DYE, consumer);
        harness(() -> YELLOW_SCUBA_HARNESS, () -> Items.YELLOW_DYE, consumer);

        ponyBottle(() -> PINK_PONY_BOTTLE, () -> Items.PINK_DYE, consumer);
        ponyBottle(() -> YELLOW_PONY_BOTTLE, () -> Items.YELLOW_DYE, consumer);

        ShapedRecipeBuilder.shaped(WATER_WAND, 1)
            .pattern("  X")
            .pattern(" Y ")
            .pattern("Y  ")
            .define('X', AZURITE)
            .define('Y', Items.GOLD_INGOT)
            .unlockedBy("has_" + safeName(() -> AZURITE), has(AZURITE))
            .unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT))
            .save(consumer);

        ShapedRecipeBuilder.shaped(BAMBOO_ITEM_FRAME, 1)
            .pattern("XXX")
            .pattern("XYX")
            .pattern("XXX")
            .define('X', Items.BAMBOO)
            .define('Y', Items.LEATHER)
            .unlockedBy("has_bamboo", has(Items.BAMBOO))
            .unlockedBy("has_leather", has(Items.LEATHER))
            .save(consumer);

        ShapedRecipeBuilder.shaped(AIR_COMPRESSOR, 1)
            .pattern("XXX")
            .pattern("XYX")
            .pattern("XXX")
            .define('X', CHUNK)
            .define('Y', AZURITE)
            .unlockedBy("has_" + safeName(() -> CHUNK), has(CHUNK))
            .unlockedBy("has_" + safeName(() -> AZURITE), has(AZURITE))
            .save(consumer);
        
        ShapedRecipeBuilder.shaped(BAMBOO_LADDER, 4)
            .pattern("S S").pattern("BSB").pattern("S S")
            .define('S', BAMBOO_STICK)
            .define('B', Items.BAMBOO)
            .unlockedBy("has_bamboo", this.has(Items.BAMBOO))
            .save(consumer);

        boardwalk(() -> BAMBOO_SLAB, () -> BAMBOO_BOARDWALK, consumer);
        boardwalk(() -> PALM_SLAB, () -> PALM_BOARDWALK, consumer);
        boardwalk(() -> MAHOGANY_SLAB, () -> MAHOGANY_BOARDWALK, consumer);
        boardwalk(() -> MANGROVE_SLAB, () -> MANGROVE_BOARDWALK, consumer);

        ShapedRecipeBuilder.shaped(BAMBOO_CHEST)
            .pattern("BBB").pattern("B B").pattern("BBB")
            .define('B', Items.BAMBOO)
            .unlockedBy("has_bamboo", this.has(Items.BAMBOO))
            .save(consumer);
        
        ShapedRecipeBuilder.shaped(SIFTER)
            .pattern("XXX").pattern("XIX").pattern("XXX")
            .define('X', ItemTags.PLANKS)
            .define('I',  TropicraftTags.ItemsExtended.GLASS)
            .group("tropicraft:sifter")
            .unlockedBy("has_glass", this.has(TropicraftTags.ItemsExtended.GLASS))
            .save(consumer);
        
        ShapedRecipeBuilder.shaped(SIFTER)
            .pattern("XXX").pattern("XIX").pattern("XXX")
            .define('X', ItemTags.PLANKS)
            .define('I', TropicraftTags.ItemsExtended.GLASS_PANES)
            .group("tropicraft:sifter")
            .unlockedBy("has_glass_pane", this.has(TropicraftTags.ItemsExtended.GLASS_PANES))
            .save(consumer, new ResourceLocation(Constants.MODID, "sifter_with_glass_pane"));
        
        ShapedRecipeBuilder.shaped(DRINK_MIXER)
            .pattern("XXX").pattern("XYX").pattern("XXX")
            .define('X', CHUNK)
            .define('Y', BAMBOO_MUG)
            .unlockedBy("has_bamboo_mug", this.has(BAMBOO_MUG))
            .save(consumer);
        
        ShapedRecipeBuilder.shaped(TIKI_TORCH)
            .pattern("Y").pattern("X").pattern("X")
            .define('X', BAMBOO_STICK)
            .define('Y', ItemTags.COALS)
            .unlockedBy("has_bamboo_stick", this.has(BAMBOO_STICK))
            .save(consumer);
        
        ShapedRecipeBuilder.shaped(BAMBOO_FLOWER_POT)
            .pattern("# #").pattern(" # ")
            .define('#', Items.BAMBOO)
            .unlockedBy("has_bamboo", this.has(Items.BAMBOO))
            .save(consumer);
        
        ShapelessRecipeBuilder.shapeless(COCKTAILS.get(Drink.PINA_COLADA))
            .requires(BAMBOO_MUG)
            .requires(COCONUT_CHUNK)
            .requires(PINEAPPLE_CUBES)
            .unlockedBy("has_bamboo_mug", this.has(BAMBOO_MUG))
            .save(consumer);
        
        ShapelessRecipeBuilder.shapeless(TROPICAL_FERTILIZER)
            .requires(TropicraftFlower.MAGIC_MUSHROOM.get())
            .requires(TropicraftFlower.CROTON.get())
            .unlockedBy("has_magic_mushroom", this.has(TropicraftFlower.MAGIC_MUSHROOM.get()))
            .save(consumer);

        ShapedRecipeBuilder.shaped(DAGGER)
            .pattern("X")
            .pattern("I")
            .define('X', CHUNK)
            .define('I', BAMBOO_STICK)
            .unlockedBy("has_" + safeName(() -> CHUNK), has(CHUNK))
            .unlockedBy("has_bamboo", this.has(Items.BAMBOO))
            .save(consumer);

        ShapedRecipeBuilder.shaped(BLOW_GUN)
            .pattern("X  ")
            .pattern(" I ")
            .pattern("  X")
            .define('X', BAMBOO_STICK)
            .define('I', ZIRCON)
            .unlockedBy("has_" + safeName(() -> ZIRCON), has(ZIRCON))
            .unlockedBy("has_" + safeName(() -> BAMBOO_STICK), has(BAMBOO_STICK))
            .save(consumer);
    }
    
    private ResourceLocation safeId(ResourceLocation id) {
        return new ResourceLocation(id.getNamespace(), safeName(id));
    }

    private ResourceLocation safeId(Supplier<? extends ItemLike> registryEntry) {
        return safeId(Registry.ITEM.getKey(registryEntry.get().asItem()));
    }
    
    private String safeName(ResourceLocation nameSource) {
        return nameSource.getPath().replace('/', '_');
    }
    
    private String safeName(Supplier<? extends ItemLike> registryEntry) {
        return safeName(Registry.ITEM.getKey(registryEntry.get().asItem()));
    }
    
    private <T extends ItemLike> void ore(Named<Item> source, Supplier<T> result, float xp, Consumer<FinishedRecipe> consumer) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(source), result.get(), xp, 100)
            .unlockedBy("has_" + safeName(source.getName()), this.has(source))
            .save(consumer);
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(source), result.get(), xp, 100)
            .unlockedBy("has_" + safeName(source.getName()), this.has(source))
            .save(consumer, safeId(result) + "_from_blasting");
    }

    private <T extends ItemLike> void food(Supplier<? extends T> source, Supplier<? extends T> result, float xp, Consumer<FinishedRecipe> consumer) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(source.get()), result.get(), xp, 100)
            .unlockedBy("has_" + safeName(source), this.has(source.get()))
            .save(consumer);
        SimpleCookingRecipeBuilder.cooking(Ingredient.of(source.get()), result.get(), xp, 100, RecipeSerializer.SMOKING_RECIPE)
            .unlockedBy("has_" + safeName(source), this.has(source.get()))
            .save(consumer, safeId(result) + "_from_smoking");
        SimpleCookingRecipeBuilder.cooking(Ingredient.of(source.get()), result.get(), xp, 100, RecipeSerializer.CAMPFIRE_COOKING_RECIPE)
            .unlockedBy("has_" + safeName(source), this.has(source.get()))
            .save(consumer, safeId(result) + "_from_campfire");
    }
    
    private <T extends ItemLike> void storage(Supplier<? extends T> input, Supplier<? extends T> output, Consumer<FinishedRecipe> consumer) {
        // TODO probably not ported correctly
        ShapedRecipeBuilder.shaped(output.get())
            .pattern("XXX").pattern("XXX").pattern("XXX")
            .define('X', input.get())
            .unlockedBy("has_at_least_9_" + safeName(input), this.has(input.get()))
            .save(consumer);
        
        ShapelessRecipeBuilder.shapeless(input.get(), 9)
            .requires(output.get())
            .unlockedBy("has_" + safeName(output), this.has(output.get()))
            .save(consumer, safeId(input) + "_from_" + safeName(output));
    }

    private <T extends ItemLike> void pickaxe(Supplier<? extends T> input, Supplier<? extends T> output, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(output.get())
                .pattern("XXX")
                .pattern(" B ")
                .pattern(" B ")
                .define('X', input.get())
                .define('B', BAMBOO_STICK)
                .unlockedBy("has_" + safeName(input), has(input.get()))
                .unlockedBy("has_" + safeName(() -> Items.BAMBOO), has(Items.BAMBOO))
                .save(consumer);
    }

    private <T extends ItemLike> void shovel(Supplier<? extends T> input, Supplier<? extends T> output, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(output.get())
                .pattern(" X ")
                .pattern(" B ")
                .pattern(" B ")
                .define('X', input.get())
                .define('B', BAMBOO_STICK)
                .unlockedBy("has_" + safeName(input), has(input.get()))
                .unlockedBy("has_" + safeName(() -> Items.BAMBOO), has(Items.BAMBOO))
                .save(consumer);
    }

    private <T extends ItemLike> void axe(Supplier<? extends T> input, Supplier<? extends T> output, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(output.get())
                .pattern("XX ")
                .pattern("XB ")
                .pattern(" B ")
                .define('X', input.get())
                .define('B', BAMBOO_STICK)
                .unlockedBy("has_" + safeName(input), has(input.get()))
                .unlockedBy("has_" + safeName(() -> Items.BAMBOO), has(Items.BAMBOO))
                .save(consumer);
    }

    private <T extends ItemLike> void hoe(Supplier<? extends T> input, Supplier<? extends T> output, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(output.get())
                .pattern("XX ")
                .pattern(" B ")
                .pattern(" B ")
                .define('X', input.get())
                .define('B', BAMBOO_STICK)
                .unlockedBy("has_" + safeName(input), has(input.get()))
                .unlockedBy("has_" + safeName(() -> Items.BAMBOO), has(Items.BAMBOO))
                .save(consumer);
    }

    private <T extends ItemLike> void sword(Supplier<? extends T> input, Supplier<? extends T> output, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(output.get())
                .pattern(" X ")
                .pattern(" X ")
                .pattern(" B ")
                .define('X', input.get())
                .define('B', BAMBOO_STICK)
                .unlockedBy("has_" + safeName(input), has(input.get()))
                .unlockedBy("has_" + safeName(() -> Items.BAMBOO), has(Items.BAMBOO))
                .save(consumer);
    }

    private <T extends ItemLike> void helmet(Supplier<? extends T> input, Supplier<? extends T> output, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(output.get())
                .pattern("XXX")
                .pattern("X X")
                .define('X', input.get())
                .unlockedBy("has_" + safeName(input), has(input.get()))
                .save(consumer);
    }

    private <T extends ItemLike> void chestplate(Supplier<? extends T> input, Supplier<? extends T> output, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(output.get())
                .pattern("X X")
                .pattern("XXX")
                .pattern("XXX")
                .define('X', input.get())
                .unlockedBy("has_" + safeName(input), has(input.get()))
                .save(consumer);
    }

    private <T extends ItemLike> void leggings(Supplier<? extends T> input, Supplier<? extends T> output, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(output.get())
                .pattern("XXX")
                .pattern("X X")
                .pattern("X X")
                .define('X', input.get())
                .unlockedBy("has_" + safeName(input), has(input.get()))
                .save(consumer);
    }

    private <T extends ItemLike> void boots(Supplier<? extends T> input, Supplier<? extends T> output, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(output.get())
                .pattern("X X")
                .pattern("X X")
                .define('X', input.get())
                .unlockedBy("has_" + safeName(input), has(input.get()))
                .save(consumer);
    }

    //@CheckReturnValue
    private <T extends ItemLike> ShapelessRecipeBuilder singleItemUnfinished(Supplier<? extends T> source, Supplier<? extends T> result, int required, int amount) {
        return ShapelessRecipeBuilder.shapeless(result.get(), amount)
            .requires(source.get(), required)
            .unlockedBy("has_" + safeName(source), this.has(source.get()));
    }

    private <T extends ItemLike> void dye(Supplier<? extends T> source, Supplier<? extends T> result, int required, int amount, Consumer<FinishedRecipe> consumer) {
        singleItemUnfinished(source, result, required, amount).save(consumer, new ResourceLocation(Constants.MODID, Registry.ITEM.getKey(result.get().asItem()).getPath()));
    }
    
    private <T extends ItemLike> void singleItem(Supplier<? extends T> source, Supplier<? extends T> result, int required, int amount, Consumer<FinishedRecipe> consumer) {
        singleItemUnfinished(source, result, required, amount).save(consumer);
    }
    
    private <T extends ItemLike> void planks(Supplier<? extends T> source, Supplier<? extends T> result, Consumer<FinishedRecipe> consumer) {
        singleItemUnfinished(source, result, 1, 4)
            .group("planks")
            .save(consumer);
    }
    
    private <T extends ItemLike> void bark(Supplier<? extends T> source, Supplier<? extends T> result, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(result.get(), 3)
            .pattern("##").pattern("##")
            .define('#', source.get())
            .group("bark")
            .unlockedBy("has_log", this.has(Blocks.ACACIA_LOG))
            .save(consumer);
    }
    
    private <T extends ItemLike> void stairs(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, boolean stone, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(result.get(), 4)
            .pattern("X  ").pattern("XX ").pattern("XXX")
            .define('X', source.get())
            .group(group)
            .unlockedBy("has_" + safeName(source), this.has(source.get()))
            .save(consumer);
        if (stone) {
            SingleItemRecipeBuilder.stonecutting(Ingredient.of(source.get()), result.get())
                .unlockedBy("has_" + safeName(source), this.has(source.get()))
                .save(consumer, safeId(result) + "_from_" + safeName(source) + "_stonecutting");
        }
    }
    
    private <T extends ItemLike> void slab(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, boolean stone, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(result.get(), 6)
            .pattern("XXX")
            .define('X', source.get())
            .group(group)
            .unlockedBy("has_" + safeName(source), this.has(source.get()))
            .save(consumer);
        if (stone) {
            SingleItemRecipeBuilder.stonecutting(Ingredient.of(source.get()), result.get(), 2)
                .unlockedBy("has_" + safeName(source), this.has(source.get()))
                .save(consumer, safeId(result) + "_from_" + safeName(source) + "_stonecutting");
        }
    }

    private <T extends ItemLike> void boardwalk(Supplier<? extends T> slab, Supplier<? extends T> result, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(result.get(), 3)
                .pattern("XXX")
                .pattern("S S")
                .define('X', slab.get())
                .define('S', TropicraftTags.ItemsExtended.RODS_WOODEN)
                .group("tropicraft:boardwalk")
                .unlockedBy("has_" + safeName(slab), has(slab.get()))
                .save(consumer);
    }
    
    private <T extends ItemLike> void fence(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(result.get(), 3)
            .pattern("W#W").pattern("W#W")
            .define('W', source.get())
            .define('#', TropicraftTags.ItemsExtended.RODS_WOODEN)
            .group(group)
            .unlockedBy("has_" + safeName(source), this.has(source.get()))
            .save(consumer);
    }
    
    private <T extends ItemLike> void fenceGate(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(result.get())
            .pattern("#W#").pattern("#W#")
            .define('W', source.get())
            .define('#', TropicraftTags.ItemsExtended.RODS_WOODEN)
            .group(group)
            .unlockedBy("has_" + safeName(source), this.has(source.get()))
            .save(consumer);
    }
    
    private <T extends ItemLike> void wall(Supplier<? extends T> source, Supplier<? extends T> result, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(result.get(), 6)
            .pattern("XXX").pattern("XXX")
            .define('X', source.get())
            .unlockedBy("has_" + safeName(source), this.has(source.get()))
            .save(consumer);
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(source.get()), result.get())
            .unlockedBy("has_" + safeName(source), this.has(source.get()))
            .save(consumer, safeId(result) + "_from_" + safeName(source) + "_stonecutting");
    }
    
    private <T extends ItemLike> void door(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(result.get(), 3)
            .pattern("XX").pattern("XX").pattern("XX")
            .define('X', source.get())
            .group(group)
            .unlockedBy("has_" + safeName(source), this.has(source.get()))
            .save(consumer);
    }

    private <T extends ItemLike> void trapDoor(Supplier<? extends T> source, Supplier<? extends T> result, @Nullable String group, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(result.get(), 2)
            .pattern("XXX").pattern("XXX")
            .define('X', source.get())
            .group(group)
            .unlockedBy("has_" + safeName(source), this.has(source.get()))
            .save(consumer);
    }
    
    private <T extends ItemLike> void bongo(Supplier<? extends T> top, Supplier<? extends T> bottom, int size, Supplier<? extends T> result, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(result.get())
            .pattern(StringUtils.repeat('T', size))
            .pattern(StringUtils.repeat('B', size))
            .pattern(StringUtils.repeat('B', size))
            .define('T', top.get())
            .define('B', bottom.get())
            .group("tropicraft:bongos")
            .unlockedBy("has_" + safeName(top), this.has(top.get()))
            .save(consumer);
    }

    private <T extends ItemLike> void goggles(Supplier<? extends T> result, Supplier<? extends T> source, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(result.get(), 1)
            .pattern("YYY")
            .pattern("X X")
            .pattern(" Z ")
            .define('X', Blocks.GLASS_PANE)
            .define('Y', ZIRCON)
            .define('Z', source.get())
            .unlockedBy("has_" + safeName(source), has(source.get()))
            .unlockedBy("has_" + safeName(() -> ZIRCON), has(ZIRCON))
            .save(consumer);
    }

    private <T extends ItemLike> void flippers(Supplier<? extends T> result, Supplier<? extends T> source, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(result.get(), 1)
            .pattern("XX")
            .pattern("YY")
            .pattern("XX")
            .define('X', source.get())
            .define('Y', ZIRCON)
            .unlockedBy("has_" + safeName(source), has(source.get()))
            .unlockedBy("has_" + safeName(() -> ZIRCON), has(ZIRCON))
            .save(consumer);
    }

    private <T extends ItemLike> void ponyBottle(Supplier<? extends T> result, Supplier<? extends T> source, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(result.get(), 1)
            .pattern("Y")
            .pattern("X")
            .define('X', Blocks.PINK_STAINED_GLASS_PANE)
            .define('Y', Blocks.LEVER)
            .unlockedBy("has_" + safeName(() -> Blocks.PINK_STAINED_GLASS_PANE), has(Blocks.PINK_STAINED_GLASS_PANE))
            .save(consumer);
    }

    private <T extends ItemLike> void harness(Supplier<? extends T> result, Supplier<? extends T> source, Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(result.get(), 1)
            .pattern("Y Y")
            .pattern("YXY")
            .pattern("YZY")
            .define('X', source.get())
            .define('Y', TropicraftTags.Items.LEATHER)
            .define('Z', AZURITE)
            .unlockedBy("has_" + safeName(() -> AZURITE), has(AZURITE))
            .save(consumer);
    }
}
