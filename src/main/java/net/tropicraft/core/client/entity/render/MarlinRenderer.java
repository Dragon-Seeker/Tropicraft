package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.MarlinModel;
import net.tropicraft.core.common.entity.underdasea.MarlinEntity;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class MarlinRenderer extends MobRenderer<MarlinEntity, MarlinModel> {
    public MarlinRenderer(EntityRenderDispatcher renderManager) {
        super(renderManager, new MarlinModel(), 0.5F);
        shadowStrength = 0.5f;
    }

    @Override
    public void render(MarlinEntity marlin, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        getModel().inWater = marlin.isInWater();
        super.render(marlin, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(MarlinEntity marlin) {
        return TropicraftRenderUtils.getTextureEntity(marlin.getTexture());
    }
}
