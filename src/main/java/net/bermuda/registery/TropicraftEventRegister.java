package net.bermuda.registery;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.Registry;
import net.tropicraft.core.common.block.jigarbov.JigarbovTorchPlacement;
import net.tropicraft.core.common.dimension.chunk.TropicraftChunkGenerator;
import net.tropicraft.core.common.dimension.chunk.VolcanoGenerator;

public class TropicraftEventRegister {
    public static void registerEvents(){
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            return new JigarbovTorchPlacement().interact(player,world, hand, hitResult);
        });

        ServerLifecycleEvents.SERVER_STARTING.register( (server) -> {
            VolcanoGenerator.onServerStarting(server);
        });
    }
}
