package net.bermuda.common.network;

import net.minecraft.network.FriendlyByteBuf;

public interface ClientboundAddEntityPacketExtensions {
    FriendlyByteBuf create$getExtraDataBuf();
}
