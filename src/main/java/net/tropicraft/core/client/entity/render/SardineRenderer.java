package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.tropicraft.core.client.entity.model.PiranhaModel;
import net.tropicraft.core.client.entity.model.SardineModel;
import net.tropicraft.core.common.entity.underdasea.PiranhaEntity;
import net.tropicraft.core.common.entity.underdasea.SardineEntity;

public class SardineRenderer extends TropicraftFishRenderer<SardineEntity, SardineModel> {
    public SardineRenderer(EntityRenderDispatcher manager) {
        super(manager, new SardineModel(), 0.2f);
    }
}
