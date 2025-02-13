package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.Constants;
import net.tropicraft.core.client.entity.model.BasiliskLizardModel;
import net.tropicraft.core.common.entity.passive.basilisk.BasiliskLizardEntity;

public class BasiliskLizardRenderer extends MobRenderer<BasiliskLizardEntity, BasiliskLizardModel<BasiliskLizardEntity>> {
    private static final ResourceLocation BROWN_TEXTURE = new ResourceLocation(Constants.MODID, "textures/entity/basilisk_lizard_brown.png");
    private static final ResourceLocation GREEN_TEXTURE = new ResourceLocation(Constants.MODID, "textures/entity/basilisk_lizard_green.png");

    private final ResourceLocation texture;

    public BasiliskLizardRenderer(EntityRendererManager manager, ResourceLocation texture) {
        super(manager, new BasiliskLizardModel<>(), 0.3F);
        this.texture = texture;
    }

    public static BasiliskLizardRenderer brown(EntityRendererManager manager) {
        return new BasiliskLizardRenderer(manager, BROWN_TEXTURE);
    }

    public static BasiliskLizardRenderer green(EntityRendererManager manager) {
        return new BasiliskLizardRenderer(manager, GREEN_TEXTURE);
    }

    @Override
    public ResourceLocation getEntityTexture(BasiliskLizardEntity entity) {
        return texture;
    }
}
