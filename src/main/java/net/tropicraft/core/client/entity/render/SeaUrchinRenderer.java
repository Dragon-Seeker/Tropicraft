package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.SeaUrchinModel;
import net.tropicraft.core.common.entity.underdasea.SeaUrchinEntity;

import javax.annotation.Nullable;

public class SeaUrchinRenderer extends MobRenderer<SeaUrchinEntity, SeaUrchinModel> {
	/**
	 * Amount freshly hatched sea urchins are scaled down while rendering.
	 */
	public static final float BABY_RENDER_SCALE = 0.5f;

	/**
	 * Amount mature sea urchins are scaled down while rendering.
	 */
	public static final float ADULT_RENDER_SCALE = 1f;

	public SeaUrchinRenderer(EntityRendererManager renderManager) {
		super(renderManager, new SeaUrchinModel(), 0.5f);
	}

	@Override
	protected void preRenderCallback(SeaUrchinEntity urchin, float partialTickTime) {
		shadowSize = 0.15f;
		shadowOpaque = 0.5f;
		float growthProgress = urchin.getGrowthProgress();
		final float scale = BABY_RENDER_SCALE + growthProgress * (ADULT_RENDER_SCALE - BABY_RENDER_SCALE);
		final float scaleAmt = 0.5f * scale;

		GlStateManager.scalef(scaleAmt, scaleAmt, scaleAmt);
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(SeaUrchinEntity entity) {
		return TropicraftRenderUtils.bindTextureEntity("seaurchin");
	}
}