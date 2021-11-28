package net.tropicraft.core.common.block.huge_plant;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

//@Mod.EventBusSubscriber(modid = Constants.MODID, value = Dist.CLIENT)
public final class HugePlantBlockHighlight implements WorldRenderEvents.BlockOutline {
    private static final Minecraft CLIENT = Minecraft.getInstance();

    @Override
    public boolean onBlockOutline(WorldRenderContext worldRenderContext, WorldRenderContext.BlockOutlineContext blockOutlineContext) {
        ClientLevel world = CLIENT.level;
        if (world == null) return false;

        BlockPos pos = blockOutlineContext.blockPos();
        BlockState state = blockOutlineContext.blockState();
        if (state.getBlock() instanceof HugePlantBlock) {
            return renderHugePlantHighlight(worldRenderContext, blockOutlineContext, world, pos, state);
        }

        return true;
    }

    private boolean renderHugePlantHighlight(WorldRenderContext worldRenderContext, WorldRenderContext.BlockOutlineContext blockOutlineContext, ClientLevel world, BlockPos pos, BlockState state) {
        HugePlantBlock.Shape shape = HugePlantBlock.Shape.matchIncomplete(state.getBlock(), world, pos);
        if (shape == null) return true;

        VertexConsumer builder = worldRenderContext.consumers().getBuffer(RenderType.lines());

        Vec3 view = worldRenderContext.camera().getPosition();//event.getInfo().getPosition();
        AABB aabb = shape.asAabb().move(-view.x, -view.y, -view.z);
        LevelRenderer.renderLineBox(worldRenderContext.matrixStack(), builder, aabb, 0.0F, 0.0F, 0.0F, 0.4F);

        return false;
    }


}
