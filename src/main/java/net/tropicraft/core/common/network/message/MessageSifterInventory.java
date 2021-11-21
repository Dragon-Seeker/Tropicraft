package net.tropicraft.core.common.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.tropicraft.core.common.block.tileentity.SifterTileEntity;

public class MessageSifterInventory extends MessageTileEntity<SifterTileEntity> {

	private ItemStack siftItem;

	public MessageSifterInventory(FriendlyByteBuf buf) {
		super();
		this.decode(buf);
	}

	public MessageSifterInventory(SifterTileEntity sifter) {
		super(sifter);
		siftItem = sifter.getSiftItem();
	}

	public void encode(final FriendlyByteBuf buf) {
		super.encode(buf);
		buf.writeItem(siftItem);
	}

	public void decode(final FriendlyByteBuf buf) {
		super.decode(buf);
		this.siftItem = buf.readItem();
	}

	@Override
	public void onMessage(Player playerEntity) {
		Handler.onMessage(this);
	}

	public class Handler{

		public static boolean onMessage(MessageSifterInventory message) {
			Minecraft.getInstance().execute(() -> {
				SifterTileEntity sifter = message.getClientTileEntity();
				if (sifter != null) {
					sifter.setSiftItem(message.siftItem);
				}
			});
			return true;
		}
	}
}
