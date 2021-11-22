package net.tropicraft.core.client.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.shadow.fabric.api.client.rendering.v1.ArmorRenderingRegistry;
import net.tropicraft.core.client.ClientSetup;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.PlayerHeadpieceModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StacheArmorProvider implements ArmorRenderingRegistry.ModelProvider, ArmorRenderingRegistry.TextureProvider {

    public StacheArmorProvider(){
    }

    @Override
    public @NotNull HumanoidModel<LivingEntity> getArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<LivingEntity> defaultModel) {
        return slot == EquipmentSlot.HEAD ? PlayerHeadpieceModel.createModel(ClientSetup.STACHE_LAYER, null, 0, 0, 0) : null;
    }

    @Override
    public @NotNull ResourceLocation getArmorTexture(LivingEntity entity, ItemStack stack, EquipmentSlot slot, boolean secondLayer, @Nullable String suffix, ResourceLocation defaultTexture) {
        return TropicraftRenderUtils.getTextureArmor("nigel_layer_1");
    }
}


