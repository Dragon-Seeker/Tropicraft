//package net.tropicraft.core.common.network.message;
//
//import net.api.network.ISimplePacket;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.multiplayer.ClientLevel;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.world.entity.player.Player;
//import net.minecraftforge.common.util.LazyOptional;
//import net.minecraftforge.fmllegacy.network.NetworkEvent;
//import net.tropicraft.core.common.item.scuba.ScubaData;
//import net.tropicraft.core.common.network.TropicraftMessage;
//
//import java.util.function.Supplier;
//
//
//public class MessageUpdateScubaData extends ISimplePacket implements TropicraftMessage {
//
//    private final ScubaData data;
//
//    public MessageUpdateScubaData(ScubaData data) {
//        this.data = data;
//    }
//
//    public MessageUpdateScubaData(FriendlyByteBuf buf) {
//        ScubaData data = new ScubaData();
//        data.deserializeBuffer(buf);
//        this.data = data;
//    }
//
////    public static void encode(final MessageUpdateScubaData message, final FriendlyByteBuf buf) {
////        message.data.serializeBuffer(buf);
////    }
////
////    public static MessageUpdateScubaData decode(final FriendlyByteBuf buf) {
////        ScubaData data = new ScubaData();
////        data.deserializeBuffer(buf);
////        return new MessageUpdateScubaData(data);
////    }
//
//    @Override
//    public void encode(FriendlyByteBuf buf) {
//        data.serializeBuffer(buf);
//    }
//
//    @Override
//    public void onMessage(Player playerEntity) {
//        Handler.onMessage(this);
//    }
//
//    public static void handle(final MessageUpdateScubaData message, Supplier<NetworkEvent.Context> ctx) {
//        ctx.get().enqueueWork(() -> {
//            LazyOptional<ScubaData> data = Minecraft.getInstance().player.getCapability(ScubaData.CAPABILITY);
//            data.ifPresent(d -> d.copyFrom(message.data));
//        });
//        ctx.get().setPacketHandled(true);
//    }
//
//
//}
