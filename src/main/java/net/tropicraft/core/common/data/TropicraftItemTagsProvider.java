package net.tropicraft.core.common.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag.Named;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.item.AshenMaskItem;
import net.tropicraft.core.common.item.TropicraftItems;

import java.util.Arrays;
import java.util.function.Supplier;

import static net.tropicraft.core.common.TropicraftTags.Items.*;

public class TropicraftItemTagsProvider extends ItemTagsProvider {

    public TropicraftItemTagsProvider(DataGenerator generatorIn, BlockTagsProvider blockTags, ExistingFileHelper existingFileHelper) {
        super(generatorIn, blockTags);
    }

    public TropicraftItemTagsProvider() {
        super(null, null);
        addTags();
    }

    @Override
    protected void addTags() {
        // Add forge tags for our ores
        addItemsToTag(AZURITE_ORE, TropicraftBlocks.AZURITE_ORE);
        addItemsToTag(EUDIALYTE_ORE, TropicraftBlocks.EUDIALYTE_ORE);
        addItemsToTag(ZIRCON_ORE, TropicraftBlocks.ZIRCON_ORE);
        addItemsToTag(MANGANESE_ORE, TropicraftBlocks.MANGANESE_ORE);
        addItemsToTag(SHAKA_ORE, TropicraftBlocks.SHAKA_ORE);
        appendToTag(TropicraftTags.ItemsExtended.ORES, AZURITE_ORE, EUDIALYTE_ORE, ZIRCON_ORE, MANGANESE_ORE, SHAKA_ORE);
        
        // Add forge tags for our gems/ingots
        addItemsToTag(AZURITE_GEM, TropicraftItems.AZURITE);
        addItemsToTag(EUDIALYTE_GEM, TropicraftItems.EUDIALYTE);
        addItemsToTag(ZIRCON_GEM, TropicraftItems.ZIRCON);
        addItemsToTag(MANGANESE_INGOT, TropicraftItems.MANGANESE);
        addItemsToTag(SHAKA_INGOT, TropicraftItems.SHAKA);
        addItemsToTag(ZIRCONIUM_GEM, TropicraftItems.ZIRCONIUM);
        appendToTag(TropicraftTags.ItemsExtended.GEMS, AZURITE_GEM, EUDIALYTE_GEM, ZIRCON_GEM, ZIRCONIUM_GEM);
        appendToTag(TropicraftTags.ItemsExtended.INGOTS, MANGANESE_INGOT, SHAKA_INGOT);

        addItemsToTag(LEATHER, TropicraftItems.IGUANA_LEATHER);
        appendToTag(TropicraftTags.ItemsExtended.LEATHER, LEATHER);

        // Add bamboo sticks to forge ore tag
        addItemsToTag(TropicraftTags.ItemsExtended.RODS_WOODEN, TropicraftItems.BAMBOO_STICK);
        
        // Add our fish items to stats and dolphin food
        addItemsToTag(ItemTags.FISHES, TropicraftItems.RAW_FISH, TropicraftItems.COOKED_FISH);
        
        // Shells for sifter drops
        addItemsToTag(SHELLS, TropicraftItems.SOLONOX_SHELL, TropicraftItems.FROX_CONCH, TropicraftItems.PAB_SHELL,
                TropicraftItems.RUBE_NAUTILUS, TropicraftItems.STARFISH, TropicraftItems.TURTLE_SHELL);
        
        // Swords for chunk drops
        addItemsToTag(SWORDS, Items.WOODEN_SWORD, Items.STONE_SWORD, Items.IRON_SWORD, Items.GOLDEN_SWORD, Items.DIAMOND_SWORD, Items.NETHERITE_SWORD,
                TropicraftItems.EUDIALYTE_SWORD, TropicraftItems.ZIRCON_SWORD, TropicraftItems.ZIRCONIUM_SWORD);

        for (AshenMaskItem item : TropicraftItems.ASHEN_MASKS.values()) {
            addItemsToTag(ASHEN_MASKS, item);
        }

        for (FlowerBlock flower : TropicraftBlocks.FLOWERS.values()) {
            addItemsToTag(ItemTags.FLOWERS, flower);
        }

        addItemsToTag(FRUITS, Items.APPLE);
        addItemsToTag(FRUITS, TropicraftItems.GRAPEFRUIT, TropicraftItems.LEMON, TropicraftItems.LIME, TropicraftItems.ORANGE);

        addItemsToTag(MEATS, Items.BEEF, Items.PORKCHOP, Items.CHICKEN, Items.RABBIT, Items.MUTTON);

        // Copy block tags
        copy(TropicraftTags.Blocks.SAND, SAND);
        copy(TropicraftTags.Blocks.MUD, MUD);

        copy(TropicraftTags.Blocks.SAPLINGS, SAPLINGS);
        copy(TropicraftTags.Blocks.LEAVES, LEAVES);
        
        copy(TropicraftTags.Blocks.SMALL_FLOWERS, SMALL_FLOWERS);

        copy(TropicraftTags.Blocks.LOGS, LOGS);
        copy(TropicraftTags.Blocks.PLANKS, PLANKS);
        
        copy(TropicraftTags.Blocks.WOODEN_SLABS, WOODEN_SLABS);
        copy(TropicraftTags.Blocks.WOODEN_STAIRS, WOODEN_STAIRS);
        copy(TropicraftTags.Blocks.WOODEN_DOORS, WOODEN_DOORS);
        copy(TropicraftTags.Blocks.WOODEN_TRAPDOORS, WOODEN_TRAPDOORS);
        copy(TropicraftTags.Blocks.WOODEN_FENCES, WOODEN_FENCES);
        
        copy(TropicraftTags.Blocks.SLABS, SLABS);
        copy(TropicraftTags.Blocks.STAIRS, STAIRS);
        copy(TropicraftTags.Blocks.DOORS, DOORS);
        copy(TropicraftTags.Blocks.TRAPDOORS, TRAPDOORS);
        copy(TropicraftTags.Blocks.FENCES, FENCES);
        copy(TropicraftTags.Blocks.WALLS, WALLS);
    }

    @SafeVarargs
    private final void addItemsToTag(Named<Item> tag, Supplier<? extends ItemLike>... items) {
        tag(tag).add(Arrays.stream(items).map(Supplier::get).map(ItemLike::asItem).toArray(Item[]::new));
    }

    private void addItemsToTag(Named<Item> tag, ItemLike... items) {
        tag(tag).add(Arrays.stream(items).map(ItemLike::asItem).toArray(Item[]::new));
    }
    
    @SafeVarargs
    private final void appendToTag(Named<Item> tag, Named<Item>... toAppend) {
        TagsProvider.TagAppender<Item> builder = tag(tag);
        for (Named<Item> value : toAppend) {
            builder.addTag(value);
        }
    }

    @Override
    public String getName() {
        return "Tropicraft Item Tags";
    }

    public static void init(){

    }
}
