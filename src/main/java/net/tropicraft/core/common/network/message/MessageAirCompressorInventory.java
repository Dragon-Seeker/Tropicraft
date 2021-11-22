//package net.tropicraft.core.common.network.message;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.tropicraft.core.common.block.tileentity.AirCompressorTileEntity;
//
//public class MessageAirCompressorInventory extends MessageTileEntity<AirCompressorTileEntity> {
//
//    private ItemStack tank = ItemStack.EMPTY;
//    public MessageAirCompressorInventory(FriendlyByteBuf buf) {
//        super();
//        this.decode(buf);
//    }
//
//    public MessageAirCompressorInventory(AirCompressorTileEntity airCompressor) {
//        super(airCompressor);
//        this.tank = airCompressor.getTankStack();
//    }
//
//    public void encode(final FriendlyByteBuf buf) {
//        super.encode(buf);
//        buf.writeItem(this.tank);
//    }
//
//    public void decode(final FriendlyByteBuf buf) {
//        super.decode(buf);
//        this.tank = buf.readItem();
//    }
//
//    @Override
//    public void onMessage(Player playerEntity) {
//        Handler.onMessage(this);
//    }
//
//    public static class Handler {
//
//        public static boolean onMessage(MessageAirCompressorInventory message) {
//            Minecraft.getInstance().execute(() -> {
//
//                AirCompressorTileEntity compressor = message.getClientTileEntity();
//                if (compressor != null) {
//                    if (!message.tank.isEmpty()) {
//                        compressor.addTank(message.tank);
//                    } else {
//                        compressor.ejectTank();
//                    }
//                }
//            });
//            return true;
//        }
//    }
//}