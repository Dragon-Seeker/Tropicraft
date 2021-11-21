package net.tropicraft.core.common.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.tropicraft.core.common.block.tileentity.DrinkMixerTileEntity;

public class MessageMixerInventory extends MessageTileEntity<DrinkMixerTileEntity> {
	private NonNullList<ItemStack> inventory;
	private ItemStack result = ItemStack.EMPTY;

	public MessageMixerInventory(FriendlyByteBuf buf) {
		super();
		this.decode(buf);
	}

	public MessageMixerInventory(final DrinkMixerTileEntity mixer) {
		super(mixer);
		inventory = mixer.ingredients;
		result = mixer.result;
	}

	public void encode(final FriendlyByteBuf buf) {
		super.encode(buf);

		buf.writeByte(this.inventory.size());
		for (final ItemStack i : this.inventory) {
			buf.writeItem(i);
		}

		buf.writeItem(this.result);
	}

	public void decode(final FriendlyByteBuf buf) {
		super.decode(buf);

		this.inventory = NonNullList.withSize(buf.readByte(), ItemStack.EMPTY);
		for (int i = 0; i < this.inventory.size(); i++) {
			this.inventory.set(i, buf.readItem());
		}

		this.result = buf.readItem();
	}

	@Override
	public void onMessage(Player playerEntity) {
		Handler.onMessage(this);
	}

	public static class Handler {
		public static boolean onMessage(MessageMixerInventory message) {
			Minecraft.getInstance().execute(() -> {
				final DrinkMixerTileEntity mixer = message.getClientTileEntity();
				if (mixer != null) {
					mixer.ingredients = message.inventory;
					mixer.result = message.result;
				}
			});
			return true;
		}
	}
}
