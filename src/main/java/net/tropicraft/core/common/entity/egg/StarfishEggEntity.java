package net.tropicraft.core.common.entity.egg;

import net.api.network.ExtraSpawnDataEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.underdasea.StarfishEntity;
import net.tropicraft.core.common.entity.underdasea.StarfishType;
import net.tropicraft.core.common.item.TropicraftItems;
import org.jetbrains.annotations.Nullable;

public class StarfishEggEntity extends EchinodermEggEntity implements ExtraSpawnDataEntity {
	private StarfishType starfishType;

	public StarfishEggEntity(final EntityType<? extends StarfishEggEntity> type, Level world) {
		super(type, world);
		starfishType = StarfishType.values()[random.nextInt(StarfishType.values().length)];
	}

	public StarfishType getStarfishType() {
		return starfishType;
	}

	public void setStarfishType(StarfishType starfishType) {
		this.starfishType = starfishType;
	}

	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		buffer.writeByte(starfishType.ordinal());
	}

	@Override
	public void readSpawnData(FriendlyByteBuf additionalData) {
		starfishType = StarfishType.values()[additionalData.readByte()];
	}

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		nbt.putByte("StarfishType", (byte) getStarfishType().ordinal());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		setStarfishType(StarfishType.values()[nbt.getByte("StarfishType")]);
	}

	@Override
	public String getEggTexture() {
		return "starfishegg";
	}

	@Override
	public Entity onHatch() {
		StarfishEntity baby = new StarfishEntity(TropicraftEntities.STARFISH, level);
		baby.setBaby();
		baby.setStarfishType(starfishType);
		return baby;
	}

	@Nullable
	@Override
	public ItemStack getPickResult() {
		return new ItemStack(TropicraftItems.STARFISH);
	}
}