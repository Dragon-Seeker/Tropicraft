package net.tropicraft.core.client.entity.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Constants;
import net.tropicraft.core.client.ClientSetup;
import net.tropicraft.core.client.entity.model.HummingbirdModel;
import net.tropicraft.core.common.entity.passive.HummingbirdEntity;

@Environment(EnvType.CLIENT)
public class HummingbirdRenderer extends MobRenderer<HummingbirdEntity, HummingbirdModel<HummingbirdEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MODID, "textures/entity/hummingbird.png");

    public HummingbirdRenderer(EntityRendererProvider.Context context) {
        super(context, new HummingbirdModel<>(context.bakeLayer(ClientSetup.HUMMINGBIRD_LAYER)), 0.2F);
    }

    @Override
    public ResourceLocation getTextureLocation(HummingbirdEntity entity) {
        return TEXTURE;
    }
}
