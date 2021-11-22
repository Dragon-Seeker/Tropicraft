package net.tropicraft.core.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.tropicraft.core.events.ItemUseTickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Shadow
    protected int useItemRemaining;

    @Shadow
    protected ItemStack useItem;

//    @ModifyVariable(method = "updatingUsingItem()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isSameIgnoreDurability(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
//    public boolean canContinueUse(boolean canContinue){
//        if(ItemUseTickEvent.CAN_CONTINUE_USING_EVENT.invoker().canContinueUsing(useItem, getMainHandItem())){
//            return canContinue;
//        }
//        return false;
//    }

//    @Inject(method = "updatingUsingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isSameIgnoreDurability(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
//    public void test(CallbackInfo ci){
//
//    }

    @Inject(method = "updatingUsingItem", at = @At("HEAD"))
    public void preItemUseTick(CallbackInfo ci){
        ItemStack itemStack = getMainHandItem();

        ItemUseTickEvent.BEFORE_ITEM_USE_TICK.invoker().beforeItemUsingTick((LivingEntity) (Object) this, itemStack, this.useItemRemaining);
    }

    @Inject(method = "updatingUsingItem", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/entity/LivingEntity;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;", ordinal = 1))
    public void duringItemUseTick(CallbackInfo ci){
        ItemStack itemStack = getMainHandItem();

        ItemUseTickEvent.DURING_ITEM_USE_TICK.invoker().onUsingTick((LivingEntity) (Object) this, itemStack, this.useItemRemaining);
    }

    @Inject(method = "updatingUsingItem", at = @At("TAIL"))
    public void postItemUseTick(CallbackInfo ci){
        ItemStack itemStack = getMainHandItem();

        ItemUseTickEvent.AFTER_ITEM_USE_TICK.invoker().postItemUsingTick((LivingEntity) (Object) this, itemStack, this.useItemRemaining);
    }

    @Unique
    public ItemStack getMainHandItem(){
        return ((LivingEntity) (Object) this).getItemInHand(((LivingEntity) (Object) this).getUsedItemHand());
    }
}
