package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.api.forge.entity.ExtEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.entity.SeaTurtleEntity;
import net.tropicraft.core.common.entity.placeable.BeachFloatEntity;
import net.tropicraft.core.common.entity.placeable.FurnitureEntity;
import net.tropicraft.core.events.PlayerRenderEvents;

//@EventBusSubscriber(value = Dist.CLIENT, modid = Constants.MODID)
@Environment(EnvType.CLIENT)
public class PlayerRotationHandler implements PlayerRenderEvents.Pre, PlayerRenderEvents.Post {

    private static float rotationYawHead, prevRotationYawHead, rotationPitch, prevRotationPitch;
    
    private static float interpolateAndWrap(float cur, float prev, float partial) {
        return Mth.wrapDegrees(prev + ((cur - prev) * partial));
    }

    @Override
    public boolean pre(Player player, PlayerRenderer renderer, float partialRenderTick, PoseStack stack, MultiBufferSource buffers, int light) {
        Entity riding = player.getVehicle();
        if (riding instanceof BeachFloatEntity) {
            FurnitureEntity floaty = (FurnitureEntity) riding;

            stack.pushPose();
            stack.mulPose(Vector3f.YP.rotationDegrees(-(floaty.yRotO + (partialRenderTick * (floaty.getYRot() - floaty.yRotO)))));
            stack.translate(0, 1.55, 1.55);
            stack.mulPose(Vector3f.XN.rotationDegrees(90));

            // Cancel out player camera rotation
            float f = interpolateAndWrap(player.yBodyRot, player.yBodyRotO, partialRenderTick);
            stack.mulPose(Vector3f.YP.rotationDegrees(f));

            // Lock in head
            rotationYawHead = player.yHeadRot;
            prevRotationYawHead = player.yHeadRotO;
            player.yHeadRot = player.yBodyRot;
            player.yHeadRotO = player.yBodyRotO;
            rotationPitch = player.getXRot();
            prevRotationPitch = player.xRotO;
            player.setXRot(10f);
            player.xRotO = 10f;

            // Cancel limb swing
            player.animationPosition = 0;
            player.animationSpeed = 0;
            player.animationSpeedOld = 0;
        }
        if (riding instanceof SeaTurtleEntity) {
            SeaTurtleEntity turtle = (SeaTurtleEntity) riding;
            stack.pushPose();

            // Cancel out player camera rotation
            float pitch = interpolateAndWrap(turtle.getXRot(), turtle.xRotO, partialRenderTick);
            float yaw = interpolateAndWrap(turtle.yHeadRot, turtle.yHeadRotO, partialRenderTick);

            stack.translate(0, turtle.getPassengersRidingOffset() - player.getMyRidingOffset(), 0);
            stack.mulPose(Vector3f.YN.rotationDegrees(yaw));
            stack.translate(0, -0.1, 0); // TODO figure out why this budging is needed
            stack.mulPose(Vector3f.XP.rotationDegrees(pitch));
            stack.translate(0, 0.1, 0);
            stack.mulPose(Vector3f.YP.rotationDegrees(yaw));
            stack.translate(0, -turtle.getPassengersRidingOffset() + player.getMyRidingOffset(), 0);

            Vec3 passengerOffset = (new Vec3(-0.25f, 0.0D, 0.0D)).yRot((float) (-Math.toRadians(yaw) - (Math.PI / 2)));
            stack.translate(passengerOffset.x(), 0, passengerOffset.z());

            // Lock in head
            rotationPitch = player.getXRot();
            prevRotationPitch = player.xRotO;
            player.setXRot(10f);
            player.xRotO = 10f;
        }

        return true;
    }

    @Override
    public void post(Player player, PlayerRenderer renderer, float partialRenderTick, PoseStack stack, MultiBufferSource buffers, int light) {
        Player p = Minecraft.getInstance().player;
        if (p.getVehicle() instanceof BeachFloatEntity || p.getVehicle() instanceof SeaTurtleEntity) {
            stack.popPose();
            p.setXRot(rotationPitch);
            p.xRotO = prevRotationPitch;
        }
        if (p.getVehicle() instanceof BeachFloatEntity) {
            p.yHeadRot = rotationYawHead;
            p.yHeadRotO = prevRotationYawHead;
        }
    }

//    @SubscribeEvent
//    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
//        PoseStack stack = event.getMatrixStack();
//        MultiBufferSource buffers = event.getBuffers();
//        Player p = event.getPlayer();
//        Entity riding = p.getVehicle();
//        if (riding instanceof BeachFloatEntity) {
//            FurnitureEntity floaty = (FurnitureEntity) riding;
//
//            stack.pushPose();
//            stack.mulPose(Vector3f.YP.rotationDegrees(-(floaty.yRotO + (event.getPartialRenderTick() * (floaty.getYRot() - floaty.yRotO)))));
//            stack.translate(0, 1.55, 1.55);
//            stack.mulPose(Vector3f.XN.rotationDegrees(90));
//
//            // Cancel out player camera rotation
//            float f = interpolateAndWrap(p.yBodyRot, p.yBodyRotO, event.getPartialRenderTick());
//            stack.mulPose(Vector3f.YP.rotationDegrees(f));
//
//            // Lock in head
//            rotationYawHead = p.yHeadRot;
//            prevRotationYawHead = p.yHeadRotO;
//            p.yHeadRot = p.yBodyRot;
//            p.yHeadRotO = p.yBodyRotO;
//            rotationPitch = p.getXRot();
//            prevRotationPitch = p.xRotO;
//            p.setXRot(10f);
//            p.xRotO = 10f;
//
//            // Cancel limb swing
//            p.animationPosition = 0;
//            p.animationSpeed = 0;
//            p.animationSpeedOld = 0;
//        }
//        if (riding instanceof SeaTurtleEntity) {
//            SeaTurtleEntity turtle = (SeaTurtleEntity) riding;
//            stack.pushPose();
//
//            // Cancel out player camera rotation
//            float pitch = interpolateAndWrap(turtle.getXRot(), turtle.xRotO, event.getPartialRenderTick());
//            float yaw = interpolateAndWrap(turtle.yHeadRot, turtle.yHeadRotO, event.getPartialRenderTick());
//
//            stack.translate(0, turtle.getPassengersRidingOffset() - p.getMyRidingOffset(), 0);
//            stack.mulPose(Vector3f.YN.rotationDegrees(yaw));
//            stack.translate(0, -0.1, 0); // TODO figure out why this budging is needed
//            stack.mulPose(Vector3f.XP.rotationDegrees(pitch));
//            stack.translate(0, 0.1, 0);
//            stack.mulPose(Vector3f.YP.rotationDegrees(yaw));
//            stack.translate(0, -turtle.getPassengersRidingOffset() + p.getMyRidingOffset(), 0);
//
//            Vec3 passengerOffset = (new Vec3(-0.25f, 0.0D, 0.0D)).yRot((float) (-Math.toRadians(yaw) - (Math.PI / 2)));
//            stack.translate(passengerOffset.x(), 0, passengerOffset.z());
//
//            // Lock in head
//            rotationPitch = p.getXRot();
//            prevRotationPitch = p.xRotO;
//            p.setXRot(10f);
//            p.xRotO = 10f;
//        }
//    }
//
//    @SubscribeEvent
//    public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
//        Player p = event.getPlayer();
//        if (p.getVehicle() instanceof BeachFloatEntity || p.getVehicle() instanceof SeaTurtleEntity) {
//            event.getMatrixStack().popPose();
//            p.setXRot(rotationPitch);
//            p.xRotO = prevRotationPitch;
//        }
//        if (p.getVehicle() instanceof BeachFloatEntity) {
//            p.yHeadRot = rotationYawHead;
//            p.yHeadRotO = prevRotationYawHead;
//        }
//    }

    //TODO: [FABRIC]: FIND A PORT FOR SUCH EVENT AND WHY ITS NEEDED TOO

//    @SubscribeEvent
//    public static void onRenderPlayerSpecials(RenderNameplateEvent event) {
//        if (event.getEntity().getVehicle() instanceof BeachFloatEntity) {
//            event.setResult(Result.DENY);
//        }
//    }


}
