package net.tropicraft.core.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.api.forge.entity.ExtEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Debug(export = true)
@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {

    @Shadow protected M model;

    @Unique private Entity vehicalCache;

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isPassenger()Z", shift = At.Shift.AFTER, ordinal = 0))
    private void modelChange(T livingEntity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci){
        model.riding = livingEntity.isPassenger() && (livingEntity.getVehicle() instanceof ExtEntity extEntity && extEntity.shouldRiderSit());
    }




    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isPassenger()Z", shift = At.Shift.BEFORE, ordinal = 1))
    private void passagerCheckChange1(T livingEntity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci){
        if(livingEntity.getVehicle() instanceof ExtEntity extEntity && !extEntity.shouldRiderSit()){
            vehicalCache = livingEntity.vehicle;
            livingEntity.vehicle = null;
        }
    }

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;prepareMobModel(Lnet/minecraft/world/entity/Entity;FFF)V", shift = At.Shift.BEFORE))
    private void passagerCheckChange2Test(T livingEntity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci){
        if(livingEntity.getVehicle() instanceof ExtEntity extEntity && !extEntity.shouldRiderSit()){
            livingEntity.vehicle = vehicalCache;
        }
    }

//    @ModifyVariable(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isPassenger()Z", shift = At.Shift.AFTER, ordinal = 1), index = 1)
//    private boolean passagerCheckChange1(boolean var, T livingEntity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i){
//        return var && (livingEntity.getVehicle() instanceof ExtEntity extEntity && extEntity.shouldRiderSit());
//    }
//
//    @ModifyVariable(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isPassenger()Z", shift = At.Shift.AFTER, ordinal = 2), index = 2)
//    private boolean passagerCheckChange2Test(boolean var, T livingEntity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i){
//        return var && (livingEntity.getVehicle() instanceof ExtEntity extEntity && extEntity.shouldRiderSit());
//    }
}
