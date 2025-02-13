package net.tropicraft.core.common.block.jigarbov;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneWallTorchBlock;
import net.minecraft.state.Property;
import net.minecraftforge.common.util.Constants.BlockFlags;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.tropicraft.Constants;
import net.tropicraft.core.common.block.TropicraftBlocks;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public final class JigarbovTorchPlacement {
    @SubscribeEvent
    public static void onPlaceBlock(BlockEvent.EntityPlaceEvent event) {
        BlockState placedState = event.getPlacedBlock();
        Block placedBlock = placedState.getBlock();
        if (placedBlock == Blocks.REDSTONE_WALL_TORCH) {
            RegistryObject<RedstoneWallTorchBlock> jigarbovTorchBlock = getJigarbovTorchFor(event.getPlacedAgainst().getBlock());
            if (jigarbovTorchBlock != null) {
                BlockState jigarbovTorch = jigarbovTorchBlock.get().getDefaultState();
                jigarbovTorch = copyPropertiesTo(jigarbovTorch, placedState);

                event.getWorld().setBlockState(event.getPos(), jigarbovTorch, BlockFlags.DEFAULT);
            }
        }
    }

    @Nullable
    private static RegistryObject<RedstoneWallTorchBlock> getJigarbovTorchFor(Block placedAgainst) {
        JigarbovTorchType type = JigarbovTorchType.byBlock(placedAgainst);
        return type != null ? TropicraftBlocks.JIGARBOV_WALL_TORCHES.get(type) : null;
    }

    private static BlockState copyPropertiesTo(BlockState to, BlockState from) {
        for (Property<?> property : from.getProperties()) {
            to = copyPropertyTo(to, from, property);
        }
        return to;
    }

    private static <T extends Comparable<T>> BlockState copyPropertyTo(BlockState to, BlockState from, Property<T> property) {
        if (to.hasProperty(property)) {
            return to.with(property, from.get(property));
        } else {
            return to;
        }
    }
}
