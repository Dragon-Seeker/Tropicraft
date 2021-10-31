package net.tropicraft.core.common.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import net.tropicraft.core.common.item.scuba.ScubaData;
import net.tropicraft.core.common.network.TropicraftMessage;

import java.util.function.Supplier;


public class MessageUpdateScubaData implements TropicraftMessage {
    
    private final ScubaData data;

    public MessageUpdateScubaData(ScubaData data) {
        this.data = data;
    }

    public static void encode(final MessageUpdateScubaData message, final FriendlyByteBuf buf) {
        message.data.serializeBuffer(buf);
    }

    public static MessageUpdateScubaData decode(final FriendlyByteBuf buf) {
        ScubaData data = new ScubaData();
        data.deserializeBuffer(buf);
        return new MessageUpdateScubaData(data);
    }

    public static void handle(final MessageUpdateScubaData message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            LazyOptional<ScubaData> data = Minecraft.getInstance().player.getCapability(ScubaData.CAPABILITY);
            data.ifPresent(d -> d.copyFrom(message.data));
        });
        ctx.get().setPacketHandled(true);
    }
}
