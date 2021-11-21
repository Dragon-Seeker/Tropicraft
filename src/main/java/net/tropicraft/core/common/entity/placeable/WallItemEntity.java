package net.tropicraft.core.common.entity.placeable;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;
import net.tropicraft.core.common.entity.BambooItemFrame;
import net.tropicraft.core.common.entity.TropicraftEntities;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nullable;

public class WallItemEntity extends BambooItemFrame {

    public WallItemEntity(EntityType<? extends WallItemEntity> entityType, Level world) {
        super(entityType, world);
    }

    public WallItemEntity(Level worldIn, BlockPos pos, Direction on) {
        super(TropicraftEntities.WALL_ITEM, worldIn, pos, on);
    }

    @Override
    public int getWidth() {
        return 16;
    }

    @Override
    public int getHeight() {
        return 16;
    }

    @Override
    protected void dropItem(@Nullable Entity entityIn, boolean p_146065_2_) {
        super.dropItem(entityIn, false);
        this.remove(RemovalReason.DISCARDED);
    }

    @Override
    public void playPlacementSound() {
    }

    @Override
    public ItemStack getPickResult() {
        return getItem();
    }
}
