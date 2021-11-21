package net.tropicraft.core.common.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.tropicraft.core.common.block.tileentity.SifterTileEntity;

public class MessageSifterStart extends MessageTileEntity<SifterTileEntity> {

	public MessageSifterStart(FriendlyByteBuf buf) {
		super();
		this.decode(buf);
	}

	public MessageSifterStart(SifterTileEntity sifter) {
		super(sifter);
	}

	public void encode(final FriendlyByteBuf buf) {
		super.encode(buf);
	}

	public void decode(final FriendlyByteBuf buf) {
		super.decode(buf);
	}

	@Override
	public void onMessage(Player playerEntity) {
		Handler.onMessage(this);
	}

	public static class Handler {

		public static boolean onMessage(MessageSifterStart message) {
			Minecraft.getInstance().execute(() -> {
				final SifterTileEntity sifter = message.getClientTileEntity();
				if (sifter != null) {
					sifter.startSifting();
				}
			});
			return true;
		}
	}

}
