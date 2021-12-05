package net.tropicraft.core.client.entity.experimental;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.ThrownTridentRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.tropicraft.Constants;
import net.tropicraft.core.client.ClientSetup;
import net.tropicraft.core.common.entity.spear.ThrownBambooSpear;

public class ThrownBambooSpearRenderer extends EntityRenderer<ThrownBambooSpear> {
    public static final ResourceLocation BAMBOO_SPEAR_LOCATION = new ResourceLocation(Constants.MODID, "textures/entity/spear/bamboo_spear.png");
    private final BambooSpearModel model;

    public ThrownBambooSpearRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new BambooSpearModel(context.bakeLayer(ClientSetup.BAMBOO_SPEAR));
    }

    public void render(ThrownBambooSpear pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        pMatrixStack.pushPose();
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot()) - 90.0F));
        pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot()) + 90.0F));
        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(pBuffer, this.model.renderType(this.getTextureLocation(pEntity)), false, pEntity.isFoil());
        this.model.renderToBuffer(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        pMatrixStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getTextureLocation(ThrownBambooSpear pEntity) {
        return BAMBOO_SPEAR_LOCATION;
    }
}
