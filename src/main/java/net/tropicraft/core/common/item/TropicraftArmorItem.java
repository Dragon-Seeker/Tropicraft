package net.tropicraft.core.common.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.tropicraft.Constants;

import net.minecraft.world.item.Item.Properties;

public class TropicraftArmorItem extends ArmorItem {
    public TropicraftArmorItem(ArmorMaterial armorMaterial, EquipmentSlot slotType, Properties properties) {
        super(armorMaterial, slotType, properties);
    }

    protected String getTexturePath(String name) {
        return Constants.ARMOR_LOCATION + name;
    }

}
