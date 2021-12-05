package net.tropicraft.core.client.entity.experimental;

// Made with Blockbench 4.0.5
// Exported for Minecraft version 1.17 with Mojang mappings
// Paste this class into your mod and generate all required imports

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.tropicraft.core.common.entity.spear.ThrownBambooSpear;

public class BambooSpearModel<T extends ThrownBambooSpear> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	private final ModelPart pole;
	private final ModelPart rDent;

	public BambooSpearModel(ModelPart root) {
		this.pole = root.getChild("pole");
		this.rDent = root.getChild("rDent");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition pole = partdefinition.addOrReplaceChild("pole", CubeListBuilder.create().texOffs(7, 0).mirror().addBox(-0.5F, 0.0F, -0.5F, 1.0F, 27.0F, 1.0F, new CubeDeformation(0.01F)).mirror(false)
		.texOffs(0, 0).addBox(-1.0F, -2.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.01F))
		.texOffs(22, 16).addBox(-1.5F, -3.15F, -1.3F, 3.0F, 4.0F, 2.0F, new CubeDeformation(-0.7F))
		.texOffs(22, 16).addBox(-1.5F, -3.15F, -0.7F, 3.0F, 4.0F, 2.0F, new CubeDeformation(-0.7F))
		.texOffs(24, 28).addBox(-1.0F, -3.4F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.5F))
		.texOffs(24, 28).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.7F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rDent = partdefinition.addOrReplaceChild("rDent", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		pole.render(poseStack, buffer, packedLight, packedOverlay);
		rDent.render(poseStack, buffer, packedLight, packedOverlay);
	}
}