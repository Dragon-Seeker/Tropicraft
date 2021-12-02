package net.bermuda.common.forge;

import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

public interface ItemEx {
    default Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot pSlot, ItemStack stack){
        return null;
    }

}
