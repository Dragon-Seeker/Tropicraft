package net.tropicraft.core.common.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import net.api.frogeExpansion.TropicBambooPot;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.block.PapayaBlock;
import net.tropicraft.core.common.block.TikiTorchBlock;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.item.TropicraftItems;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TropicraftLootTableProvider extends LootTableProvider {

    public TropicraftLootTableProvider(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    //@Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return ImmutableList.of(
                Pair.of(Blocks::new, LootContextParamSets.BLOCK)
        );
    }
    
    //@Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationresults) {
        // Nothing for now, but chest loot tables eventually
    }

    @Override
    public void run(HashCache pCache) {
        Path path = this.generator.getOutputFolder();
        Map<ResourceLocation, LootTable> map = Maps.newHashMap();
        this.getTables().forEach((p_124458_) -> {
            p_124458_.getFirst().get().accept((p_176077_, p_176078_) -> {
                if (map.put(p_176077_, p_176078_.setParamSet(p_124458_.getSecond()).build()) != null) {
                    throw new IllegalStateException("Duplicate loot table " + p_176077_);
                }
            });
        });
        ValidationContext validationcontext = new ValidationContext(LootContextParamSets.ALL_PARAMS, (p_124465_) -> null, map::get);

        validate(map, validationcontext);

        Multimap<String, String> multimap = validationcontext.getProblems();
        if (!multimap.isEmpty()) {
            multimap.forEach((p_124446_, p_124447_) -> {
                LOGGER.warn("Found validation problem in {}: {}", p_124446_, p_124447_);
            });
            throw new IllegalStateException("Failed to validate loot tables, see logs");
        } else {
            map.forEach((p_124451_, p_124452_) -> {
                Path path1 = createPath(path, p_124451_);

                try {
                    DataProvider.save(GSON, pCache, LootTables.serialize(p_124452_), path1);
                } catch (IOException ioexception) {
                    LOGGER.error("Couldn't save loot table {}", path1, ioexception);
                }

            });
        }
    }

    private static class Blocks extends BlockLoot {

        private final Set<Block> knownBlocks = new HashSet<>();

        private static final float[] FRUIT_SAPLING_RATES = new float[]{1/10F, 1/8F, 1/6F, 1/5F};
        private static final float[] SAPLING_RATES = new float[]{0.05F, 0.0625F, 0.083333336F, 0.1F};
        private static final float[] RARE_SAPLING_RATES = new float[]{0.025F, 0.027777778F, 0.03125F, 0.041666668F, 0.1F};

        @Override
        public void add(Block block, LootTable.Builder builder) {
            super.add(block, builder);
            knownBlocks.add(block);
        }

        @Override
        public void accept(BiConsumer<ResourceLocation, LootTable.Builder> p_124179_) {
            this.addTables();
            Set<ResourceLocation> set = Sets.newHashSet();

            for(Block block : getKnownBlocks()) {
                ResourceLocation resourcelocation = block.getLootTable();
                if (resourcelocation != BuiltInLootTables.EMPTY && set.add(resourcelocation)) {
                    LootTable.Builder loottable$builder = this.map.remove(resourcelocation);
                    if (loottable$builder == null) {
                        throw new IllegalStateException(String.format("Missing loottable '%s' for '%s'", resourcelocation, Registry.BLOCK.getKey(block)));
                    }

                    p_124179_.accept(resourcelocation, loottable$builder);
                }
            }

            if (!this.map.isEmpty()) {
                throw new IllegalStateException("Created block loot tables for non-blocks: " + this.map.keySet());
            }
        }

        //@Override
        protected void addTables() {
            dropsSelf(() -> TropicraftBlocks.CHUNK);
            
            // Ores and storage blocks
            dropsOreItem(() -> TropicraftBlocks.AZURITE_ORE, () -> TropicraftItems.AZURITE);
            dropsOreItem(() -> TropicraftBlocks.EUDIALYTE_ORE, () -> TropicraftItems.EUDIALYTE);
            dropsOreItem(() -> TropicraftBlocks.ZIRCON_ORE, () -> TropicraftItems.ZIRCON);
            dropsSelf(() -> TropicraftBlocks.SHAKA_ORE);
            dropsSelf(() -> TropicraftBlocks.MANGANESE_ORE);
            
            dropsSelf(() -> TropicraftBlocks.AZURITE_BLOCK);
            dropsSelf(() -> TropicraftBlocks.EUDIALYTE_BLOCK);
            dropsSelf(() -> TropicraftBlocks.ZIRCON_BLOCK);
            dropsSelf(() -> TropicraftBlocks.ZIRCONIUM_BLOCK);
            dropsSelf(() -> TropicraftBlocks.SHAKA_BLOCK);
            dropsSelf(() -> TropicraftBlocks.MANGANESE_BLOCK);
            
            // All flowers
            TropicraftBlocks.FLOWERS.values().forEach((b) -> this.dropsSelf(() -> b));
            
            // Sands
            dropsSelf(() -> TropicraftBlocks.PURIFIED_SAND);
            dropsSelf(() -> TropicraftBlocks.PACKED_PURIFIED_SAND);
            dropsSelf(() -> TropicraftBlocks.CORAL_SAND);
            dropsSelf(() -> TropicraftBlocks.FOAMY_SAND);
            dropsSelf(() -> TropicraftBlocks.VOLCANIC_SAND);
            dropsSelf(() -> TropicraftBlocks.MINERAL_SAND);

            // Mud
            dropsSelf(() -> TropicraftBlocks.MUD);

            add(TropicraftBlocks.MUD_WITH_PIANGUAS, applyExplosionDecay(TropicraftBlocks.MUD_WITH_PIANGUAS,
                    LootTable.lootTable()
                            .withPool(LootPool.lootPool()
                                    .add(LootItem.lootTableItem(TropicraftBlocks.MUD_WITH_PIANGUAS)
                                            .when(HAS_SILK_TOUCH)
                                            .otherwise(LootItem.lootTableItem(TropicraftBlocks.MUD))
                                    )
                            )
                            .withPool(LootPool.lootPool()
                                    .when(HAS_NO_SILK_TOUCH)
                                    .add(LootItem.lootTableItem(TropicraftItems.PIANGUAS)
                                            .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                                    )
                            )
            ));

            // Bundles
            dropsSelf(() -> TropicraftBlocks.BAMBOO_BUNDLE);
            dropsSelf(() -> TropicraftBlocks.THATCH_BUNDLE);
            
            // Planks & Logs
            dropsSelf(() -> TropicraftBlocks.MAHOGANY_PLANKS);
            dropsSelf(() -> TropicraftBlocks.PALM_PLANKS);
            
            dropsSelf(() -> TropicraftBlocks.MAHOGANY_LOG);
            dropsSelf(() -> TropicraftBlocks.PALM_LOG);
            
            dropsSelf(() -> TropicraftBlocks.MAHOGANY_WOOD);
            dropsSelf(() -> TropicraftBlocks.PALM_WOOD);

            dropsSelf(() -> TropicraftBlocks.PAPAYA_LOG);
            dropsSelf(() -> TropicraftBlocks.PAPAYA_WOOD);

            dropsSelf(() -> TropicraftBlocks.RED_MANGROVE_LOG);
            dropsSelf(() -> TropicraftBlocks.RED_MANGROVE_WOOD);
            dropsSelf(() -> TropicraftBlocks.RED_MANGROVE_ROOTS);

            dropsSelf(() -> TropicraftBlocks.LIGHT_MANGROVE_LOG);
            dropsSelf(() -> TropicraftBlocks.LIGHT_MANGROVE_WOOD);
            dropsSelf(() -> TropicraftBlocks.LIGHT_MANGROVE_ROOTS);

            dropsSelf(() -> TropicraftBlocks.BLACK_MANGROVE_LOG);
            dropsSelf(() -> TropicraftBlocks.BLACK_MANGROVE_WOOD);
            dropsSelf(() -> TropicraftBlocks.BLACK_MANGROVE_ROOTS);

            dropsSelf(() -> TropicraftBlocks.STRIPPED_MANGROVE_LOG);
            dropsSelf(() -> TropicraftBlocks.STRIPPED_MANGROVE_WOOD);
            dropsSelf(() -> TropicraftBlocks.MANGROVE_PLANKS);

            // Stairs & Slabs
            dropsSelf(() -> TropicraftBlocks.BAMBOO_STAIRS);
            dropsSelf(() -> TropicraftBlocks.THATCH_STAIRS);
            dropsSelf(() -> TropicraftBlocks.CHUNK_STAIRS);
            dropsSelf(() -> TropicraftBlocks.PALM_STAIRS);
            dropsSelf(() -> TropicraftBlocks.MAHOGANY_STAIRS);
            dropsSelf(() -> TropicraftBlocks.THATCH_STAIRS_FUZZY);
            dropsSelf(() -> TropicraftBlocks.MANGROVE_STAIRS);

            slab(() -> TropicraftBlocks.BAMBOO_SLAB);
            slab(() -> TropicraftBlocks.THATCH_SLAB);
            slab(() -> TropicraftBlocks.CHUNK_SLAB);
            slab(() -> TropicraftBlocks.PALM_SLAB);
            slab(() -> TropicraftBlocks.MAHOGANY_SLAB);
            slab(() -> TropicraftBlocks.MANGROVE_SLAB);

            // Leaves
            leaves(() -> TropicraftBlocks.MAHOGANY_LEAVES, () -> TropicraftBlocks.MAHOGANY_SAPLING, RARE_SAPLING_RATES);
            leaves(() -> TropicraftBlocks.PALM_LEAVES, () -> TropicraftBlocks.PALM_SAPLING, SAPLING_RATES);
            leavesNoSapling(() -> TropicraftBlocks.KAPOK_LEAVES); // TODO Should we have kapok as its own actual tree/wood?
            leavesNoSapling(() -> TropicraftBlocks.FRUIT_LEAVES); // TODO Should there be general purpose fruit leaves? This makes saplings pretty rare
            fruitLeaves(() -> TropicraftBlocks.GRAPEFRUIT_LEAVES, () -> TropicraftBlocks.GRAPEFRUIT_SAPLING, () -> TropicraftItems.GRAPEFRUIT);
            fruitLeaves(() -> TropicraftBlocks.LEMON_LEAVES, () -> TropicraftBlocks.LEMON_SAPLING, () -> TropicraftItems.LEMON);
            fruitLeaves(() -> TropicraftBlocks.LIME_LEAVES, () -> TropicraftBlocks.LIME_SAPLING, () -> TropicraftItems.LIME);
            fruitLeaves(() -> TropicraftBlocks.ORANGE_LEAVES, () -> TropicraftBlocks.ORANGE_SAPLING, () -> TropicraftItems.ORANGE);
            leavesNoSapling(() -> TropicraftBlocks.RED_MANGROVE_LEAVES);
            leavesNoSapling(() -> TropicraftBlocks.TALL_MANGROVE_LEAVES);
            leavesNoSapling(() -> TropicraftBlocks.TEA_MANGROVE_LEAVES);
            leavesNoSapling(() -> TropicraftBlocks.BLACK_MANGROVE_LEAVES);
            leaves(() -> TropicraftBlocks.PAPAYA_LEAVES, () -> TropicraftBlocks.PAPAYA_SAPLING, SAPLING_RATES);

            // Saplings
            dropsSelf(() -> TropicraftBlocks.MAHOGANY_SAPLING);
            dropsSelf(() -> TropicraftBlocks.PALM_SAPLING);
            dropsSelf(() -> TropicraftBlocks.GRAPEFRUIT_SAPLING);
            dropsSelf(() -> TropicraftBlocks.LEMON_SAPLING);
            dropsSelf(() -> TropicraftBlocks.LIME_SAPLING);
            dropsSelf(() -> TropicraftBlocks.ORANGE_SAPLING);
            dropsSelf(() -> TropicraftBlocks.PAPAYA_SAPLING);
            dropsSelf(() -> TropicraftBlocks.RED_MANGROVE_PROPAGULE);
            dropsSelf(() -> TropicraftBlocks.TALL_MANGROVE_PROPAGULE);
            dropsSelf(() -> TropicraftBlocks.TEA_MANGROVE_PROPAGULE);
            dropsSelf(() -> TropicraftBlocks.BLACK_MANGROVE_PROPAGULE);

            // Fences, Gates, and Walls
            dropsSelf(() -> TropicraftBlocks.BAMBOO_FENCE);
            dropsSelf(() -> TropicraftBlocks.THATCH_FENCE);
            dropsSelf(() -> TropicraftBlocks.CHUNK_FENCE);
            dropsSelf(() -> TropicraftBlocks.PALM_FENCE);
            dropsSelf(() -> TropicraftBlocks.MAHOGANY_FENCE);
            dropsSelf(() -> TropicraftBlocks.MANGROVE_FENCE);

            dropsSelf(() -> TropicraftBlocks.BAMBOO_FENCE_GATE);
            dropsSelf(() -> TropicraftBlocks.THATCH_FENCE_GATE);
            dropsSelf(() -> TropicraftBlocks.CHUNK_FENCE_GATE);
            dropsSelf(() -> TropicraftBlocks.PALM_FENCE_GATE);
            dropsSelf(() -> TropicraftBlocks.MAHOGANY_FENCE_GATE);
            dropsSelf(() -> TropicraftBlocks.MANGROVE_FENCE_GATE);

            dropsSelf(() -> TropicraftBlocks.CHUNK_WALL);

            // Doors and Trapdoors
            doubleBlock(() -> TropicraftBlocks.BAMBOO_DOOR);
            doubleBlock(() -> TropicraftBlocks.THATCH_DOOR);
            doubleBlock(() -> TropicraftBlocks.PALM_DOOR);
            doubleBlock(() -> TropicraftBlocks.MAHOGANY_DOOR);
            doubleBlock(() -> TropicraftBlocks.MANGROVE_DOOR);

            dropsSelf(() -> TropicraftBlocks.BAMBOO_TRAPDOOR);
            dropsSelf(() -> TropicraftBlocks.THATCH_TRAPDOOR);
            dropsSelf(() -> TropicraftBlocks.PALM_TRAPDOOR);
            dropsSelf(() -> TropicraftBlocks.MAHOGANY_TRAPDOOR);
            dropsSelf(() -> TropicraftBlocks.MANGROVE_TRAPDOOR);

            // Misc remaining blocks
            doubleBlock(() -> TropicraftBlocks.IRIS);
            add(TropicraftBlocks.PINEAPPLE, droppingChunks(TropicraftBlocks.PINEAPPLE, () -> TropicraftItems.PINEAPPLE_CUBES,
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(TropicraftBlocks.PINEAPPLE).setProperties(
                        StatePropertiesPredicate.Builder.properties().hasProperty(
                                DoublePlantBlock.HALF, DoubleBlockHalf.UPPER))));

            dropsSelf(() -> TropicraftBlocks.REEDS);


            add(TropicraftBlocks.MUD_WITH_PIANGUAS, applyExplosionDecay(TropicraftBlocks.MUD_WITH_PIANGUAS,
                    LootTable.lootTable()
                            .withPool(LootPool.lootPool()
                                    .add(LootItem.lootTableItem(TropicraftBlocks.MUD_WITH_PIANGUAS)
                                            .when(HAS_SILK_TOUCH)
                                            .otherwise(LootItem.lootTableItem(TropicraftBlocks.MUD))
                                    )
                            )
                            .withPool(LootPool.lootPool()
                                    .when(HAS_NO_SILK_TOUCH)
                                    .add(LootItem.lootTableItem(TropicraftItems.PIANGUAS)
                                            .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                                    )
                            )
            ));

            add(TropicraftBlocks.PAPAYA, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                    .add(applyExplosionCondition(TropicraftBlocks.PAPAYA, LootItem.lootTableItem(TropicraftBlocks.PAPAYA.asItem())
                                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2)))
                                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TropicraftBlocks.PAPAYA).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(PapayaBlock.AGE, 1))))
                                    )));

            dropsSelf(() -> TropicraftBlocks.SMALL_BONGO_DRUM);
            dropsSelf(() -> TropicraftBlocks.MEDIUM_BONGO_DRUM);
            dropsSelf(() -> TropicraftBlocks.LARGE_BONGO_DRUM);
            
            dropsSelf(() -> TropicraftBlocks.BAMBOO_LADDER);

            dropsSelf(() -> TropicraftBlocks.BAMBOO_BOARDWALK);
            dropsSelf(() -> TropicraftBlocks.PALM_BOARDWALK);
            dropsSelf(() -> TropicraftBlocks.MAHOGANY_BOARDWALK);
            dropsSelf(() -> TropicraftBlocks.MANGROVE_BOARDWALK);

            dropsSelf(() -> TropicraftBlocks.BAMBOO_CHEST);
            dropsSelf(() -> TropicraftBlocks.SIFTER);
            dropsSelf(() -> TropicraftBlocks.DRINK_MIXER);
            dropsSelf(() -> TropicraftBlocks.AIR_COMPRESSOR);

            add(TropicraftBlocks.TIKI_TORCH, createSinglePropConditionTable(TropicraftBlocks.TIKI_TORCH, TikiTorchBlock.SECTION, TikiTorchBlock.TorchSection.UPPER));
            
            add(TropicraftBlocks.COCONUT, droppingChunks(TropicraftBlocks.COCONUT, () -> TropicraftItems.COCONUT_CHUNK));
            
            dropsSelf(() -> TropicraftBlocks.BAMBOO_FLOWER_POT);
            TropicraftBlocks.ALL_POTTED_PLANTS.forEach(ro -> add(ro, droppingFlowerPotAndFlower(ro)));

            add(TropicraftBlocks.COFFEE_BUSH, dropNumberOfItems(TropicraftBlocks.COFFEE_BUSH, () -> TropicraftItems.RAW_COFFEE_BEAN, 1, 3));

            dropsSelf(() -> TropicraftBlocks.GOLDEN_LEATHER_FERN);
            dropsOther(() ->TropicraftBlocks.TALL_GOLDEN_LEATHER_FERN, () ->TropicraftBlocks.GOLDEN_LEATHER_FERN);
            dropsOther(() ->TropicraftBlocks.LARGE_GOLDEN_LEATHER_FERN, () ->TropicraftBlocks.GOLDEN_LEATHER_FERN);
        }
        
        private void dropsSelf(Supplier<? extends Block> block) {
            dropSelf(block.get());
        }

        private void dropsOther(Supplier<? extends Block> block, Supplier<? extends ItemLike> drops) {
            dropOther(block.get(), drops.get());
        }
        
        private void dropsOreItem(Supplier<? extends Block> block, Supplier<? extends ItemLike> item) {
            add(block.get(), createOreDrop(block.get(), item.get().asItem()));
        }
        
        private void slab(Supplier<? extends SlabBlock> block) {
            add(block.get(), createSlabItemTable(block.get()));
        }

        private void leaves(Supplier<? extends LeavesBlock> block, Supplier<? extends SaplingBlock> sapling, float[] rates) {
            add(block.get(), createLeavesDrops(block.get(), sapling.get(), rates));
        }
        
        private void leavesNoSapling(Supplier<? extends LeavesBlock> block) {
            add(block.get(), droppingWithSticks(block.get()));
        }
        
        private void fruitLeaves(Supplier<? extends LeavesBlock> block, Supplier<? extends SaplingBlock> sapling, Supplier<? extends ItemLike> fruit) {
            add(block.get(), droppingWithChancesSticksAndFruit(block.get(), sapling.get(), fruit.get(), FRUIT_SAPLING_RATES));
        }
        
        protected static LootTable.Builder onlyWithSilkTouchOrShears(Block block) {
            return LootTable.lootTable().withPool(LootPool.lootPool().when(HAS_SHEARS_OR_SILK_TOUCH).setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(block)));
        }
        
        protected static LootTable.Builder droppingWithSticks(Block block) {
            return onlyWithSilkTouchOrShears(block).withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .when(HAS_NO_SHEARS_OR_SILK_TOUCH)
                        .add(applyExplosionDecay(block, LootItem.lootTableItem(Items.STICK)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                                .when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F))));
        }
        
        protected static LootTable.Builder droppingWithChancesSticksAndFruit(Block block, Block sapling, ItemLike fruit, float[] chances) {
            return createLeavesDrops(block, sapling, chances)
                    .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                            .when(HAS_NO_SHEARS_OR_SILK_TOUCH)
                            .add(applyExplosionDecay(block, LootItem.lootTableItem(fruit))));
        }
        
        private void doubleBlock(Supplier<? extends Block> block) {
            add(block.get(), createSinglePropConditionTable(block.get(), DoorBlock.HALF, DoubleBlockHalf.LOWER));
        }

        // Same as droppingAndFlowerPot but with variable flower pot item
        protected static LootTable.Builder droppingFlowerPotAndFlower(FlowerPotBlock fullPot) {
            Supplier<? extends FlowerPotBlock> fullModPot = fullPot instanceof TropicBambooPot fullBambooPot ? () -> fullBambooPot.getEmptyPot() : () -> (FlowerPotBlock) net.minecraft.world.level.block.Blocks.FLOWER_POT;
            return LootTable.lootTable().withPool(applyExplosionCondition(fullModPot.get(), LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(fullModPot.get()))))
                    .withPool(applyExplosionCondition(fullPot.getContent(), LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(fullPot.getContent()))));
        }
        
        private static LootPool.Builder droppingChunksPool(Block block, Supplier<? extends ItemLike> chunk) {
            return LootPool.lootPool().add(LootItem.lootTableItem(chunk.get())
                    .when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(TropicraftTags.Items.SWORDS)))
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F)))
                    .otherwise(applyExplosionCondition(block, LootItem.lootTableItem(block))));
        }
        
        protected static LootTable.Builder droppingChunks(Block block, Supplier<? extends ItemLike> chunk) {
            return LootTable.lootTable().withPool(droppingChunksPool(block, chunk));
        }
        
        protected static LootTable.Builder droppingChunks(Block block, Supplier<? extends ItemLike> chunk, LootItemCondition.Builder condition) {
            return LootTable.lootTable().withPool(droppingChunksPool(block, chunk).when(condition));
        }

        private static LootTable.Builder dropNumberOfItems(Block block, Supplier<? extends ItemLike> drop, final int minDrops, final int maxDrops) {
            return LootTable.lootTable().withPool(applyExplosionCondition(block, LootPool.lootPool()
                    .add(LootItem.lootTableItem(drop.get()))
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(minDrops, maxDrops)))));
        }
        
        //@Override
        protected Iterable<Block> getKnownBlocks() {
            return knownBlocks;
        }
    }
}
