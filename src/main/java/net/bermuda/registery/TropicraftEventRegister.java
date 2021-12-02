package net.bermuda.registery;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.tropicraft.core.common.block.jigarbov.JigarbovTorchPlacement;

public class TropicraftEventRegister {
    public static void registerEvents(){
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            return new JigarbovTorchPlacement().interact(player,world, hand, hitResult);
        });
    }
}
