package net.tropicraft.core.mixin;

import com.google.common.collect.Multimap;
import net.api.forge.ItemEx;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract Item getItem();

    @Unique private EquipmentSlot pSlot;

    @Inject(method = "getAttributeModifiers", at = @At("HEAD"))
    private void grabEquipSlot(EquipmentSlot equipmentSlot, CallbackInfoReturnable<Multimap<Attribute, AttributeModifier>> cir){
        pSlot = equipmentSlot;
    }

    @ModifyVariable(method = "getAttributeModifiers", at = @At(value="INVOKE_ASSIGN", target="Lnet/minecraft/world/item/Item;getDefaultAttributeModifiers(Lnet/minecraft/world/entity/EquipmentSlot;)Lcom/google/common/collect/Multimap;"))
    private Multimap<Attribute, AttributeModifier> getAttributeModifiers(Multimap<Attribute, AttributeModifier> attributeModifierMap){
        if(((ItemStack) (Object) this).getItem() instanceof ItemEx){
            Multimap<Attribute, AttributeModifier> attributes = ((ItemEx)getItem()).getAttributeModifiers(pSlot, (ItemStack) (Object) this);
            return attributes != null ? attributes : attributeModifierMap;
        }
        return attributeModifierMap;
    }
}
