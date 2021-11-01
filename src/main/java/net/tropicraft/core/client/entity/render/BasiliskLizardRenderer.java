package net.tropicraft.core.client.entity.render;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Constants;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.BasiliskLizardModel;
import net.tropicraft.core.common.entity.passive.basilisk.BasiliskLizardEntity;

public class BasiliskLizardRenderer extends MobRenderer<BasiliskLizardEntity, BasiliskLizardModel<BasiliskLizardEntity>> {
    private static final ResourceLocation BROWN_TEXTURE = new ResourceLocation(Constants.MODID, "textures/entity/basilisk_lizard_brown.png");
    private static final ResourceLocation GREEN_TEXTURE = new ResourceLocation(Constants.MODID, "textures/entity/basilisk_lizard_green.png");

    private final ResourceLocation texture;

    public BasiliskLizardRenderer(EntityRendererProvider.Context context, ResourceLocation texture, ModelLayerLocation layer) {
        super(context, new BasiliskLizardModel<>(context.bakeLayer(layer)), 0.3F);
        this.texture = texture;
    }

    public static BasiliskLizardRenderer brown(EntityRendererProvider.Context context) {
        return new BasiliskLizardRenderer(context, BROWN_TEXTURE, TropicraftRenderLayers.BROWN_BASILISK_LIZARD_LAYER);
    }

    public static BasiliskLizardRenderer green(EntityRendererProvider.Context context) {
        return new BasiliskLizardRenderer(context, GREEN_TEXTURE, TropicraftRenderLayers.GREEN_BASILISK_LIZARD_LAYER);
    }

    @Override
    public ResourceLocation getTextureLocation(BasiliskLizardEntity entity) {
        return texture;
    }
}
