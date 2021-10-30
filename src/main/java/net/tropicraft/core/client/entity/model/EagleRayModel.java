package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.tropicraft.core.common.entity.underdasea.EagleRayEntity;

public class EagleRayModel extends SegmentedModel<EagleRayEntity> {
	/**
	 * Wing joint amplitudes, linearly interpolated between previous tick and this tick using partialTicks.
	 */
	private float[] interpolatedWingAmplitudes = new float[EagleRayEntity.WING_JOINTS];

	private ModelRenderer body;

	public EagleRayModel() {
		texWidth = 128;
		texHeight = 64;

		body = new ModelRenderer(this, 32, 0);
		body.addBox(-2F, 0F, 0F, 5, 3, 32);
		body.setPos(0F, 0F, -8F);
		body.setTexSize(128, 64);
		body.mirror = true;
	}

	@Override
	public void setupAnim(EagleRayEntity eagleRay, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public Iterable<ModelRenderer> parts() {
		return ImmutableList.of(body);
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		super.renderToBuffer(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		renderWings(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		renderTailSimple(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}

	private void renderTailSimple(MatrixStack stack, IVertexBuilder buffer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		float minU = 0.75f;
		float maxU = 1.0f;
		float minV = 0.0f;
		float maxV = 0.5f;

		stack.pushPose();
		stack.translate(0.55f, 0f, 1.5f);
		stack.mulPose(Vector3f.YP.rotationDegrees(-90f));
		stack.scale(1.5f, 1f, 1f);
		vertex(buffer, stack.last().pose(), stack.last().normal(), 0, 0, 0, red, green, blue, alpha, minU, minV, packedLightIn, packedOverlayIn);
		vertex(buffer, stack.last().pose(), stack.last().normal(), 0, 0, 1, red, green, blue, alpha, minU, maxV, packedLightIn, packedOverlayIn);
		vertex(buffer, stack.last().pose(), stack.last().normal(), 1, 0, 1, red, green, blue, alpha, maxU, maxV, packedLightIn, packedOverlayIn);
		vertex(buffer, stack.last().pose(), stack.last().normal(), 1, 0, 0, red, green, blue, alpha, maxU, minV, packedLightIn, packedOverlayIn);
		stack.popPose();
	}

	private static void vertex(IVertexBuilder bufferIn, Matrix4f matrixIn, Matrix3f matrixNormalIn, float x, float y, float z, float red, float green, float blue, float alpha, float texU, float texV, int packedLight, int packedOverlay) {
		bufferIn.vertex(matrixIn, x, y, z).color(red, green, blue, alpha).uv(texU, texV).overlayCoords(packedOverlay).uv2(packedLight).normal(matrixNormalIn, 0.0F, -1.0F, 0.0F).endVertex();
	}

	private void renderWings(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		matrixStackIn.pushPose();
		matrixStackIn.translate(0.5f / 16f, 0, -0.5f); // Center on body
		matrixStackIn.scale(2f, 0.5f, 2f); // Scale to correct size
		
		renderWing(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha, false);
		
		// Rotate around center
		matrixStackIn.translate(0, 0, 0.5f);
		matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180f));
		matrixStackIn.translate(0, 0, -0.5f);
		
		renderWing(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha, true);
		
		matrixStackIn.popPose();
	}

	private void renderWing(MatrixStack stack, IVertexBuilder buffer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha, boolean reverse) {
		float minUFront = 0f;
		float maxUFront = 0.25f;
		float minVFront = 0f;
		float maxVFront = 0.5f;

		float minUBack = 0f;
		float maxUBack = 0.25f;
		float minVBack = 0.5f;
		float maxVBack = 1f;
		
		stack.pushPose();
		stack.translate(1.25f / 16f, 0, 0); // Translate out to body edge

		for (int i = 1; i < EagleRayEntity.WING_JOINTS; i++) {
			float prevAmplitude = interpolatedWingAmplitudes[i - 1];
			float amplitude = interpolatedWingAmplitudes[i];

			float prevX = (i-1)/(EagleRayEntity.WING_JOINTS - 1f);
			float x = i/(EagleRayEntity.WING_JOINTS - 1f);

			float prevUFront = minUFront + (maxUFront-minUFront) * prevX;
			float uFront = minUFront + (maxUFront-minUFront) * x;
			float prevUBack = minUBack + (maxUBack-minUBack) * prevX;
			float uBack = minUBack + (maxUBack-minUBack) * x;

			float offset = -0.001f;
			// Bottom
			final Matrix4f matrix = stack.last().pose();
			final Matrix3f normal = stack.last().normal();

			vertex(buffer, matrix, normal, x, amplitude-offset, 0, red, green, blue, alpha, uBack, reverse ? maxVBack : minVBack, packedLightIn, packedOverlayIn);
			vertex(buffer, matrix, normal, x, amplitude-offset, 1, red, green, blue, alpha, uBack, reverse ? minVBack : maxVBack, packedLightIn, packedOverlayIn);
			vertex(buffer, matrix, normal, prevX, prevAmplitude-offset, 1, red, green, blue, alpha, prevUBack, reverse ? minVBack : maxVBack, packedLightIn, packedOverlayIn);
			vertex(buffer, matrix, normal, prevX, prevAmplitude-offset, 0, red, green, blue, alpha, prevUBack, reverse ? maxVBack : minVBack, packedLightIn, packedOverlayIn);

			// Top
			vertex(buffer, matrix, normal, prevX, prevAmplitude, 0, red, green, blue, alpha, prevUFront, reverse ? maxVFront : minVFront, packedLightIn, packedOverlayIn);
			vertex(buffer, matrix, normal, prevX, prevAmplitude, 1, red, green, blue, alpha, prevUFront, reverse ? minVFront : maxVFront, packedLightIn, packedOverlayIn);
			vertex(buffer, matrix, normal, x, amplitude, 1, red, green, blue, alpha, uFront, reverse ? minVFront : maxVFront, packedLightIn, packedOverlayIn);
			vertex(buffer, matrix, normal, x, amplitude, 0, red, green, blue, alpha, uFront, reverse ? maxVFront : minVFront, packedLightIn, packedOverlayIn);
		}
		
		stack.popPose();
	}

	@Override
	public void prepareMobModel(EagleRayEntity ray, float par2, float par3, float partialTicks) {
		final float[] prevWingAmplitudes = ray.getPrevWingAmplitudes();
		final float[] wingAmplitudes = ray.getWingAmplitudes();

		for (int i = 1; i < EagleRayEntity.WING_JOINTS; i++) {
			final float prevWingAmplitude = prevWingAmplitudes[i];
			final float wingAmplitude = wingAmplitudes[i];
			interpolatedWingAmplitudes[i] = prevWingAmplitude + partialTicks * (wingAmplitude - prevWingAmplitude);
		}
	}
}
