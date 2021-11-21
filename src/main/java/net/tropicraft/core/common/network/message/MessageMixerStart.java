package net.tropicraft.core.common.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.tropicraft.core.common.block.tileentity.DrinkMixerTileEntity;

public class MessageMixerStart extends MessageTileEntity<DrinkMixerTileEntity> {

	public MessageMixerStart(FriendlyByteBuf buf) {
		super();
		this.decode(buf);
	}

	public MessageMixerStart(DrinkMixerTileEntity sifter) {
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
		public static boolean onMessage(MessageMixerStart message) {
			Minecraft.getInstance().execute(() -> {
				final DrinkMixerTileEntity te = message.getClientTileEntity();
				if (te != null) {
					te.startMixing();
				}
			});
			return true;
		}
	}

}
