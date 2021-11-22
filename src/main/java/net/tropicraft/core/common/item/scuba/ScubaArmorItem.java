package net.tropicraft.core.common.item.scuba;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.tropicraft.Constants;
import net.tropicraft.core.common.item.ArmorMaterials;
import net.tropicraft.core.common.item.TropicraftArmorItem;

public class ScubaArmorItem extends TropicraftArmorItem {
    
    private final ScubaType type;

    public ScubaArmorItem(ScubaType type, EquipmentSlot slotType, Item.Properties properties) {
        super(ArmorMaterials.SCUBA, slotType, properties);
        this.type = type;
    }
    
    public ScubaType getType() {
        return type;
    }
    
    public boolean providesAir() {
        return false;
    }
    
    public void tickAir(Player player, EquipmentSlot slot, ItemStack stack) {
    }

    public int addAir(int air, ItemStack stack) {
        return 0;
    }
    
    public int getRemainingAir(ItemStack stack) {
        return 0;
    }
    
    public int getMaxAir(ItemStack stack) {
        return 0;
    }
    
    public static ResourceLocation getArmorTexture(ScubaType material) {
        return new ResourceLocation(Constants.ARMOR_LOCATION + "scuba_gear_" + material.getTextureName() + ".png");   
    }



}
