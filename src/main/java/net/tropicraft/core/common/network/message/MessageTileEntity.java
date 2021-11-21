package net.tropicraft.core.common.network.message;

import com.google.common.reflect.TypeToken;
import net.api.network.ISimplePacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.tropicraft.core.common.network.TropicraftMessage;

/**
 * Based on <a href="https://github.com/SleepyTrousers/EnderCore">EnderCore</a>, with permission.
 *
 * Licensed under CC0.
 */
public abstract class MessageTileEntity<T extends BlockEntity> extends ISimplePacket implements TropicraftMessage {
	protected long pos;
	@Deprecated
	protected int x;
	@Deprecated
	protected int y;
	@Deprecated
	protected int z;

	protected MessageTileEntity() {}

	protected MessageTileEntity(T tile) {
		pos = tile.getBlockPos().asLong();
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeLong(this.pos);
	}

	public void decode(FriendlyByteBuf buf) {
		this.pos = buf.readLong();
		BlockPos bp = this.getPos();
		this.x = bp.getX();
		this.y = bp.getY();
		this.z = bp.getZ();
	}

	public BlockPos getPos() {
		return BlockPos.of(pos);
	}

	protected T getClientTileEntity() {
		if(FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT){
			return getTileEntity(Minecraft.getInstance().level);
		}
		return null;
		//return getTileEntity(DistExecutor.callWhenOn(Dist.CLIENT, () -> () -> Minecraft.getInstance().level));
	}

	@SuppressWarnings("unchecked")
	protected T getTileEntity(Level worldObj) {
		// Sanity check, and prevent malicious packets from loading chunks
		if (worldObj == null || !worldObj.hasChunkAt(getPos())) {
			return null;
		}
		BlockEntity te = worldObj.getBlockEntity(getPos());
		if (te == null) {
			return null;
		}
		TypeToken<?> teType = TypeToken.of(getClass()).resolveType(MessageTileEntity.class.getTypeParameters()[0]);
		if (teType.isSubtypeOf(te.getClass())) {
			return (T) te;
		}
		return null;
	}

	@Override
	public void onMessage(Player playerEntity) {
		Handler.onMessage(this);
	}

	public static class Handler {

		public static boolean onMessage(MessageTileEntity message) {
			Minecraft.getInstance().execute(() -> {
			});
			return true;
		}
	}
}
