package net.tropicraft.core.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.api.forge.entity.ExtEntity;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.tropicraft.core.events.PlayerRenderEvents;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Debug(export = true)
@Mixin(PlayerRenderer.class)
public class PlayerRenderMixin {
    @Inject(method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/player/PlayerRenderer;setModelProperties(Lnet/minecraft/client/player/AbstractClientPlayer;)V", shift = At.Shift.AFTER), cancellable = true)
    public void renderPlayerEventPre(AbstractClientPlayer abstractClientPlayer, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci){
        if(!PlayerRenderEvents.PRE.invoker().pre(abstractClientPlayer, ((PlayerRenderer)(Object)this), g, poseStack, multiBufferSource, i)){
            ci.cancel();
        }
    }

    @Inject(method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "TAIL"))
    public void renderPlayerEventPost(AbstractClientPlayer abstractClientPlayer, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci){
        PlayerRenderEvents.POST.invoker().post(abstractClientPlayer, ((PlayerRenderer)(Object)this), g, poseStack, multiBufferSource, i);
    }
}
