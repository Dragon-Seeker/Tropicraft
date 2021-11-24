package net.tropicraft.core.common;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag.Named;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Constants;

import java.util.function.Function;

public class TropicraftTags {

    public static class Blocks extends TropicraftTags {

        public static final Named<Block> MINEABLE_WITH_AXE = modTag("mineable/axe");
        public static final Named<Block> MINEABLE_WITH_HOE = modTag("mineable/hoe");
        public static final Named<Block> MINEABLE_WITH_PICKAXE = modTag("mineable/pickaxe");
        public static final Named<Block> MINEABLE_WITH_SHOVEL = modTag("mineable/shovel");

        //public static final Named<Block> NEEDS_DIAMOND_TOOL = modTag("needs_diamond_tool");
        public static final Named<Block> NEEDS_IRON_TOOL = modTag("needs_iron_tool");
        //public static final Named<Block> NEEDS_STONE_TOOL = modTag("needs_stone_tool");

        public static final Named<Block> SAND = modTag("sand");
        public static final Named<Block> MUD = modTag("mud");

        public static final Named<Block> SAPLINGS = modTag("saplings");
        public static final Named<Block> LEAVES = modTag("leaves");

        public static final Named<Block> SMALL_FLOWERS = modTag("small_flowers");
        public static final Named<Block> TROPICS_FLOWERS = modTag("tropics_flowers");
        public static final Named<Block> RAINFOREST_FLOWERS = modTag("rainforest_flowers");
        public static final Named<Block> OVERWORLD_FLOWERS = modTag("overworld_flowers");

        public static final Named<Block> LOGS = modTag("logs");
        public static final Named<Block> PLANKS = modTag("planks");

        public static final Named<Block> ROOTS = modTag("roots");

        public static final Named<Block> WOODEN_SLABS = modTag("wooden_slabs");
        public static final Named<Block> WOODEN_STAIRS = modTag("wooden_stairs");
        public static final Named<Block> WOODEN_DOORS = modTag("wooden_doors");
        public static final Named<Block> WOODEN_TRAPDOORS = modTag("wooden_trapdoors");
        public static final Named<Block> WOODEN_FENCES = modTag("wooden_fences");

        public static final Named<Block> SLABS = modTag("slabs");
        public static final Named<Block> STAIRS = modTag("stairs");
        public static final Named<Block> DOORS = modTag("doors");
        public static final Named<Block> TRAPDOORS = modTag("trapdoors");
        public static final Named<Block> FENCES = modTag("fences");
        public static final Named<Block> WALLS = modTag("walls");

        public static final Named<Block> FLOWER_POTS = modTag("flower_pots");
        public static final Named<Block> BONGOS = modTag("bongos");

        public static final Named<Block> CLIMBABLE = modTag("climbable");

        static Named<Block> tag(String modid, String name) {
            return tagBlock(modid, name);
        }

        static Named<Block> modTag(String name) {
            return tagBlock(Constants.MODID, name);
        }

        static Named<Block> compatTag(String name) {
            return tagBlock("c", name);
        }

        public static void init(){}
    }
    
    public static class Items extends TropicraftTags {

        public static final Named<Item> AZURITE_ORE = compatTag("azurite_ores");
        public static final Named<Item> EUDIALYTE_ORE = compatTag("eudialyte_ores");
        public static final Named<Item> MANGANESE_ORE = compatTag("manganese_ores");
        public static final Named<Item> SHAKA_ORE = compatTag("shaka_ores");
        public static final Named<Item> ZIRCON_ORE = compatTag("zircon_ores");
        
        public static final Named<Item> AZURITE_GEM = compatTag("azurite_gems");
        public static final Named<Item> EUDIALYTE_GEM = compatTag("eudialyte_gems");
        public static final Named<Item> MANGANESE_INGOT = compatTag("manganese_ingots");
        public static final Named<Item> SHAKA_INGOT = compatTag("shaka_ingots");
        public static final Named<Item> ZIRCON_GEM = compatTag("zircon_gems");
        public static final Named<Item> ZIRCONIUM_GEM = compatTag("zirconium_gems");
        
        public static final Named<Item> SWORDS = compatTag("swords");

        public static final Named<Item> SAND = modTag("sand");
        public static final Named<Item> MUD = modTag("mud");

        public static final Named<Item> SAPLINGS = modTag("saplings");
        public static final Named<Item> LEAVES = modTag("leaves");
        
        public static final Named<Item> SMALL_FLOWERS = modTag("small_flowers");
        
        public static final Named<Item> LOGS = modTag("logs");
        public static final Named<Item> PLANKS = modTag("planks");
        
        public static final Named<Item> WOODEN_SLABS = modTag("wooden_slabs");
        public static final Named<Item> WOODEN_STAIRS = modTag("wooden_stairs");
        public static final Named<Item> WOODEN_DOORS = modTag("wooden_doors");
        public static final Named<Item> WOODEN_TRAPDOORS = modTag("wooden_trapdoors");
        public static final Named<Item> WOODEN_FENCES = modTag("wooden_fences");
        
        public static final Named<Item> SLABS = modTag("slabs");
        public static final Named<Item> STAIRS = modTag("stairs");
        public static final Named<Item> DOORS = modTag("doors");
        public static final Named<Item> TRAPDOORS = modTag("trapdoors");
        public static final Named<Item> FENCES = modTag("fences");
        public static final Named<Item> WALLS = modTag("walls");

        public static final Named<Item> LEATHER = modTag("leather");
        
        public static final Named<Item> SHELLS = modTag("shells");

        public static final Named<Item> ASHEN_MASKS = modTag("ashen_masks");

        public static final Named<Item> FRUITS = modTag("fruits");
        public static final Named<Item> MEATS = modTag("meats");

        static Named<Item> tag(String modid, String name) {
            return tagItem(modid, name);
        }

        static Named<Item> modTag(String name) {
            return tagItem(Constants.MODID, name);
        }

        static Named<Item> compatTag(String name) {
            return tagItem("c", name);
        }

        public static void init(){}
    }

    public static class BlocksExtended extends TropicraftTags{


        static Named<Block> tag(String modid, String name) {
            return tagBlock(modid, name);
        }

        static Named<Block> modTag(String name) {
            return tagBlock(Constants.MODID, name);
        }

        static Named<Block> compatTag(String name) {
            return tagBlock("c", name);
        }

        public static void init(){}
    }

    public static class ItemsExtended extends TropicraftTags{
        public static final Named<Item> ORES = compatTag("ores");
        public static final Named<Item> GEMS = compatTag("gems");
        public static final Named<Item> INGOTS = compatTag("ingots");
        public static final Named<Item> LEATHER = compatTag("leather");
        public static final Named<Item> RODS_WOODEN = compatTag("wooden_rods");
        public static final Named<Item> GLASS = compatTag("glass");
        public static final Named<Item> GLASS_PANES = compatTag("glass_panes");

        static Named<Item> compatTag(String name) {
            return tagItem("c", name);
        }

        public static void init(){}
    }
    
    static <T extends Named<?>> T tag(Function<String, T> creator, String modid, String name) {
        return creator.apply(new ResourceLocation(modid, name).toString());
    }

    static <T extends Named<?>> T modTag(Function<String, T> creator, String name) {
        return tag(creator, Constants.MODID, name);
    }

    static <T extends Named<?>> T compatTag(Function<String, T> creator, String name) {
        return tag(creator, "c", name);
    }

    //-------------------------------- Fabric Start --------------------------------//
    static Named<Item> tagItem(String modid, String name) {
        return TagFactory.ITEM.create(new ResourceLocation(modid, name));
    }

    static Named<Block> tagBlock(String modid, String name) {
        return TagFactory.BLOCK.create(new ResourceLocation(modid, name));
    }
    //--------------------------------- Fabric End ---------------------------------//
}
