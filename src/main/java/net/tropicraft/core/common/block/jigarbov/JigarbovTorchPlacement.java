package net.tropicraft.core.common.block.jigarbov;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedstoneWallTorchBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.util.Constants;
import net.tropicraft.core.common.block.TropicraftBlocks;
import org.jetbrains.annotations.Nullable;

//@Mod.EventBusSubscriber(modid = Constants.MODID)
public final class JigarbovTorchPlacement implements UseBlockCallback {

    @Override
    public InteractionResult interact(Player player, Level world, InteractionHand hand, BlockHitResult hitResult) {
        if(player.getMainHandItem().getItem() == Items.REDSTONE_TORCH) {

            BlockPos blockPos = hitResult.getBlockPos().relative(hitResult.getDirection());

            RedstoneWallTorchBlock jigarbovTorchBlock = getJigarbovTorchFor(world.getBlockState(hitResult.getBlockPos()).getBlock());
            if (jigarbovTorchBlock != null) {
                BlockState jigarbovTorch = jigarbovTorchBlock.defaultBlockState();
                jigarbovTorch = copyPropertiesTo(jigarbovTorch, Blocks.REDSTONE_WALL_TORCH.getStateForPlacement(new BlockPlaceContext(player, hand, player.getMainHandItem(), hitResult)));

                player.getInventory().removeItem(Items.REDSTONE_TORCH.getDefaultInstance());

                SoundType soundType = jigarbovTorch.getSoundType();
                world.playSound(player, blockPos, soundType.getPlaceSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
                world.setBlock(blockPos, jigarbovTorch, Constants.BlockFlags.DEFAULT);

                return InteractionResult.SUCCESS;
            }

        }
        return InteractionResult.PASS;
    }

    @Nullable
    private static RedstoneWallTorchBlock getJigarbovTorchFor(Block placedAgainst) {
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
            return to.setValue(property, from.getValue(property));
        } else {
            return to;
        }
    }
}
