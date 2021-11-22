package net.tropicraft.core.client.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.shadow.fabric.api.client.rendering.v1.ArmorRenderingRegistry;
import net.tropicraft.Constants;
import net.tropicraft.core.client.ClientSetup;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.PlayerHeadpieceModel;
import net.tropicraft.core.client.scuba.ModelScubaGear;
import net.tropicraft.core.common.item.AshenMasks;
import net.tropicraft.core.common.item.scuba.ScubaArmorItem;
import net.tropicraft.core.common.item.scuba.ScubaType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ScubaArmorProvider implements ArmorRenderingRegistry.ModelProvider, ArmorRenderingRegistry.TextureProvider {

    ScubaArmorItem scubaItem;

    public ScubaArmorProvider(ScubaArmorItem scubaItem){
        this.scubaItem = scubaItem;
    }

    @Override
    public @NotNull HumanoidModel<LivingEntity> getArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot armorSlot, HumanoidModel<LivingEntity> defaultModel) {
        if (stack.isEmpty()) {
            return null;
        }

        HumanoidModel<?> armorModel;
        switch (armorSlot) {
            case HEAD:
                armorModel = ModelScubaGear.HEAD;
                break;
            case CHEST:
                armorModel = ModelScubaGear.CHEST;
                break;
            case FEET:
                armorModel = ModelScubaGear.FEET;
                break;
            default:
                return null;
        }

        ((HumanoidModel) armorModel).prepareMobModel(entity, 0.0F, 0.0F, 1.0F);

        armorModel.crouching = entity.isShiftKeyDown();
        armorModel.young = entity.isBaby();
        armorModel.rightArmPose = entity.getMainHandItem() != null ? HumanoidModel.ArmPose.BLOCK : HumanoidModel.ArmPose.EMPTY;
        return (HumanoidModel<LivingEntity>) armorModel;
    }

    @Override
    public @NotNull ResourceLocation getArmorTexture(LivingEntity entity, ItemStack stack, EquipmentSlot slot, boolean secondLayer, @Nullable String suffix, ResourceLocation defaultTexture) {
        return getArmorTexture(this.scubaItem.getType());
    }

    public static ResourceLocation getArmorTexture(ScubaType material) {
        return new ResourceLocation(Constants.ARMOR_LOCATION + "scuba_gear_" + material.getTextureName() + ".png");
    }
}


