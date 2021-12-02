package net.tropicraft.core.client;

import com.google.common.collect.ImmutableMap;
import net.bermuda.registery.TropicraftClientEventRegister;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.tropicraft.core.common.item.IColoredItem;
import net.tropicraft.core.common.item.TropicraftItems;

public class TropicraftClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        TropicraftClientEventRegister.registerClientEvents();

        ClientSetup.setupBlockRenderLayers();
        ClientSetup.registerLayerDefinitions();
        ClientSetup.registerRenderers();
        ClientSetup.setupDimensionRenderInfo();
        itemColorinit();
    }

    @Environment(EnvType.CLIENT)
    public static void itemColorinit(){
        for(Item item : TropicraftItems.COCKTAILS.values().asList()) {
            ColorProviderRegistry.ITEM.register((stack, tintIndex) -> ((IColoredItem) stack.getItem()).getColor(stack, tintIndex), item); //tintIndex > 0 ? CocktailItem.getCocktailColor(stack) :
            //ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? CocktailItem.getCocktailColor(stack) : ((DyeableItem) stack.getItem()).getColor(stack), item);
        }

        for(Item item : TropicraftItems.CHAIRS.values()) {
            ColorProviderRegistry.ITEM.register((stack, tintIndex) -> ((IColoredItem) stack.getItem()).getColor(stack, tintIndex), item); }

        for(Item item : TropicraftItems.BEACH_FLOATS.values()) {
            ColorProviderRegistry.ITEM.register((stack, tintIndex) -> ((IColoredItem) stack.getItem()).getColor(stack, tintIndex), item); }

        for(Item item : TropicraftItems.UMBRELLAS.values()) {
            ColorProviderRegistry.ITEM.register((stack, tintIndex) -> ((IColoredItem) stack.getItem()).getColor(stack, tintIndex), item); }
    }

    @Environment(EnvType.CLIENT)
    public static void bambooItemFrameInit(){
        StateDefinition<Block, BlockState> frameState = new StateDefinition.Builder<Block, BlockState>(Blocks.AIR).add(BooleanProperty.create("map")).create(Block::defaultBlockState, BlockState::new);

        ModelBakery.STATIC_DEFINITIONS = ImmutableMap.<ResourceLocation, StateDefinition<Block, BlockState>>builder()
                .putAll(ModelBakery.STATIC_DEFINITIONS)
                .put(Registry.ITEM.getKey(TropicraftItems.BAMBOO_ITEM_FRAME), frameState)
                .build();
    }
}
