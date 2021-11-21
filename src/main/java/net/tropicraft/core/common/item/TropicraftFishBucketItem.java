package net.tropicraft.core.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class TropicraftFishBucketItem<T extends AbstractFish> extends MobBucketItem {
    private final Supplier<? extends EntityType<T>> fishType;

    public TropicraftFishBucketItem(final Supplier<? extends EntityType<T>> type, Fluid fluid, SoundEvent soundEvent, Properties props) {
        super(type.get(), fluid, soundEvent, props);
        this.fishType = type;
    }

    @Override
    public void checkExtraContent(@Nullable Player pPlayer, Level world, ItemStack stack, BlockPos pos) {
        if (!world.isClientSide) {
            this.placeFish((ServerLevel) world, stack, pos);
        }
    }

    private void placeFish(ServerLevel world, ItemStack stack, BlockPos pos) {
        Entity fishy = fishType.get().spawn(world, stack, null, pos, MobSpawnType.BUCKET, true, false);
        if (fishy != null) {
            ((AbstractFish) fishy).setFromBucket(true);
        }

    }
}
