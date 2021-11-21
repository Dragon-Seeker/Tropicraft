package net.api.network;

import net.minecraft.network.FriendlyByteBuf;

public interface ExtraSpawnDataEntity {
    void readSpawnData(FriendlyByteBuf buf);

    void writeSpawnData(FriendlyByteBuf buf);
}
