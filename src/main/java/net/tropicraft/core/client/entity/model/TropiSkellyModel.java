package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.HandSide;
import net.tropicraft.core.common.entity.hostile.TropiSkellyEntity;

public class TropiSkellyModel extends BipedModel<TropiSkellyEntity> implements IHasArm {
    public TropiSkellyModel() {
        super(0.0F, 0.0F, 64, 64);
        float g = 0.0F;
        textureWidth = 64;
        textureHeight = 64;
        bipedRightArm = new ModelRenderer(this, 40, 16);
        bipedRightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, g);
        bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        bipedLeftArm = new ModelRenderer(this, 40, 16);
        bipedLeftArm.mirror = true;
        bipedLeftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, g);
        bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        bipedRightLeg = new ModelRenderer(this, 0, 16);
        bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, g);
        bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
        bipedLeftLeg = new ModelRenderer(this, 0, 16);
        bipedLeftLeg.mirror = true;
        bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, g);
        bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);

        // Hula Skirt
        bipedBody.addBox(40, 0, -4.0F, 12.0F, -2.0F, 8, 3, 4, 0.0F);
    }

    @Override
    public void translateHand(HandSide side, final MatrixStack stack) {
        stack.translate(0.09375F, 0.1875F, 0.0F);
    }
}