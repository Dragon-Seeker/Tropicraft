package net.bermuda.common.events;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.player.Player;

@Environment(EnvType.CLIENT)
public class PlayerRenderEvents {
    public static final Event<Pre> PRE = EventFactory.createArrayBacked(Pre.class, (player, renderer, partialRenderTick, stack, buffers, light) -> true, callbacks -> (player, renderer, partialRenderTick, stack, buffers, light) -> {
        boolean shouldRender = true;

        for (final PlayerRenderEvents.Pre callback : callbacks) {
            if(!callback.pre(player, renderer, partialRenderTick, stack, buffers, light)){
                shouldRender = false;
            }
        }

        return shouldRender;
    });

    public static final Event<Post> POST = EventFactory.createArrayBacked(Post.class, (player, renderer, partialRenderTick, stack, buffers, light) -> {}, callbacks -> (player, renderer, partialRenderTick, stack, buffers, light) -> {
        for (final PlayerRenderEvents.Post callback : callbacks) {
            callback.post(player, renderer, partialRenderTick, stack, buffers, light);
        }
    });

    public interface Pre {
        /**
         * Called before the player tick.
         *
         * @param player
         * @param renderer
         * @param partialRenderTick
         * @param stack
         * @param buffers
         * @param light
         */
        boolean pre(Player player, PlayerRenderer renderer, float partialRenderTick, PoseStack stack, MultiBufferSource buffers, int light);
    }

    public interface Post {
        /**
         * Called before the player tick.
         *
         * @param player
         * @param renderer
         * @param partialRenderTick
         * @param stack
         * @param buffers
         * @param light
         */
        void post(Player player, PlayerRenderer renderer, float partialRenderTick, PoseStack stack, MultiBufferSource buffers, int light);
    }
}
