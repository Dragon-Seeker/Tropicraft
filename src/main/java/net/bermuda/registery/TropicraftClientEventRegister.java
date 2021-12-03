package net.bermuda.registery;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.tropicraft.core.client.entity.render.PlayerRotationHandler;
import net.tropicraft.core.client.scuba.ScubaHUD;
import net.tropicraft.core.common.block.huge_plant.HugePlantBlockHighlight;
import net.bermuda.common.events.PlayerRenderEvents;

public class TropicraftClientEventRegister {
    public static void registerClientEvents(){
        WorldRenderEvents.BLOCK_OUTLINE.register((worldRenderContext, blockOutlineContext) -> {
            return new HugePlantBlockHighlight().onBlockOutline(worldRenderContext, blockOutlineContext);
        });

        PlayerRenderEvents.PRE.register((player, renderer, partialRenderTick, stack, buffers, light) -> {
            return new PlayerRotationHandler().pre(player, renderer, partialRenderTick, stack, buffers, light);
        });

        PlayerRenderEvents.POST.register((player, renderer, partialRenderTick, stack, buffers, light) -> {
            new PlayerRotationHandler().post(player, renderer, partialRenderTick, stack, buffers, light);
        });

        HudRenderCallback.EVENT.register((matrixStack, delta) -> {
            new ScubaHUD().onHudRender(matrixStack, delta);
        });
    }
}
