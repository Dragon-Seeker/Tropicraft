package net.api;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.tropicraft.core.common.block.huge_plant.HugePlantBlockHighlight;

public class TropicraftClientEventRegister {
    public static void registerClientEvents(){
        WorldRenderEvents.BLOCK_OUTLINE.register((worldRenderContext, blockOutlineContext) -> {
            return new HugePlantBlockHighlight().onBlockOutline(worldRenderContext, blockOutlineContext);
        });
    }
}
