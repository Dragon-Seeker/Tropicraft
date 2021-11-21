package net.tropicraft.core.common.network;

import net.api.network.ISimplePacket;
import net.api.network.NetworkWrapper;
import net.fabricmc.api.EnvType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.tropicraft.Constants;
import net.tropicraft.core.common.network.message.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TropicraftPackets {
    public static final NetworkWrapper CHANNEL = new NetworkWrapper(new ResourceLocation(Constants.MODID, "main"));

//    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
//            new ResourceLocation(Constants.MODID, "main"),
//            () -> Tropicraft.getCompatVersion(),
//            Tropicraft::isCompatibleVersion,
//            Tropicraft::isCompatibleVersion
//    );

    private static final Logger LOGGER = LogManager.getLogger(TropicraftPackets.class);

    private static int messageID = 0;

    private static int getUniqueId() {
        return messageID++;
    }

    public static void init() {
        CHANNEL.registerPacket(MessageSifterInventory.class, MessageSifterInventory::new, EnvType.CLIENT);
        CHANNEL.registerPacket(MessageSifterStart.class, MessageSifterStart::new, EnvType.CLIENT);
        CHANNEL.registerPacket(MessageMixerInventory.class, MessageMixerInventory::new, EnvType.CLIENT);
        CHANNEL.registerPacket(MessageMixerStart.class, MessageMixerStart::new, EnvType.CLIENT);
        CHANNEL.registerPacket(MessageAirCompressorInventory.class, MessageAirCompressorInventory::new, EnvType.CLIENT);
        CHANNEL.registerPacket(MessageSifterInventory.class, MessageSifterInventory::new, EnvType.CLIENT);

        //INSTANCE.registerMessage(getUniqueId(), MessageSifterInventory.class, MessageSifterInventory::encode, MessageSifterInventory::decode, MessageSifterInventory::handle);
        //INSTANCE.registerMessage(getUniqueId(), MessageSifterStart.class, MessageSifterStart::encode, MessageSifterStart::decode, MessageSifterStart::handle);
        //INSTANCE.registerMessage(getUniqueId(), MessageMixerInventory.class, MessageMixerInventory::encode, MessageMixerInventory::decode, MessageMixerInventory::handle);
        //INSTANCE.registerMessage(getUniqueId(), MessageMixerStart.class, MessageMixerStart::encode, MessageMixerStart::decode, MessageMixerStart::handle);
        //INSTANCE.registerMessage(getUniqueId(), MessageAirCompressorInventory.class, MessageAirCompressorInventory::encode, MessageAirCompressorInventory::decode, MessageAirCompressorInventory::handle);
        //INSTANCE.registerMessage(getUniqueId(), MessageUpdateScubaData.class, MessageUpdateScubaData::encode, MessageUpdateScubaData::decode, MessageUpdateScubaData::handle);
    }

    public static void sendToDimension(final ISimplePacket msg, final Level world) {
        ResourceKey<Level> dimension = world.dimension();
        if (world.isClientSide()) {
            LOGGER.warn("Attempted to send packet to dimension on client world", new RuntimeException());
            return;
        }
        CHANNEL.sendToServer(msg);

        //INSTANCE.send(PacketDistributor.DIMENSION.with(() -> dimension), msg);
    }
}
