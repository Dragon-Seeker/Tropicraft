package net.tropicraft.core.client.entity.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.core.client.ClientSetup;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.TropicraftDolphinModel;
import net.tropicraft.core.common.entity.underdasea.TropicraftDolphinEntity;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class TropicraftDolphinRenderer extends MobRenderer<TropicraftDolphinEntity, TropicraftDolphinModel> {

    public TropicraftDolphinRenderer(EntityRendererProvider.Context context) {
        super(context, new TropicraftDolphinModel(context.bakeLayer(ClientSetup.DOLPHIN_LAYER)), 0.5F);
        shadowStrength = 0.5f;
    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(TropicraftDolphinEntity dolphin) {
        return TropicraftRenderUtils.getTextureEntity(dolphin.getTexture());
    }
}
