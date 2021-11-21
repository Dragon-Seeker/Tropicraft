package net.api.network;

import net.minecraft.network.FriendlyByteBuf;

public interface ClientboundAddEntityPacketExtensions {
    FriendlyByteBuf create$getExtraDataBuf();
}
