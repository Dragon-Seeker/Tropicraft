package net.tropicraft.core.client;

import java.util.HashMap;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.tropicraft.core.registry.ItemRegistry;

public class ScubaHandler {
	
	private static HashMap<UUID, PlayerSwimData> rotationMap = new HashMap<UUID, PlayerSwimData>();
	private HashMap<Item, Float> flipperSpeedMap = new HashMap<Item, Float>();


	public ScubaHandler() {
		flipperSpeedMap.put(ItemRegistry.pinkFlippers, 1f);
		flipperSpeedMap.put(ItemRegistry.yellowFlippers, 1.5f);
	}
	
	@SubscribeEvent
	public void onRenderViewTick(EntityViewRenderEvent.CameraSetup event) {
		if(event.getEntity().equals(Minecraft.getMinecraft().player)) {
			if(Minecraft.getMinecraft().player != null && !Minecraft.getMinecraft().player.isDead) {
					EntityPlayer p = Minecraft.getMinecraft().player;
					PlayerSwimData d = getData(p);
					if(p.isInWater())
					event.setRoll(-d.currentRotationRoll*0.25f);
					event.setPitch(event.getPitch()+(d.currentHeadPitchOffset*0.25f));
				if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
				//	updateSwimDataAngles(p);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onRenderViewTick(RenderHandEvent event) {
		
		
	}
	
	

	@SubscribeEvent
	public void onRenderTick(RenderWorldLastEvent event) {
		if(Minecraft.getMinecraft().player != null && !Minecraft.getMinecraft().player.isDead) {
			if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0 || Minecraft.getMinecraft().currentScreen != null) {
				EntityPlayer p = Minecraft.getMinecraft().player;
				if(!p.isInWater())
				updateSwimDataAngles(p);
				updateSwimRenderAngles(p);
			}
		}
	}

	@SubscribeEvent
	public void onTickPlayer(PlayerTickEvent event) {
		if(! event.type.equals(TickEvent.Type.PLAYER)) return;
		EntityPlayer p = event.player;
		PlayerSwimData d = getData(p);

		BlockPos bp = new BlockPos((int) p.posX, (int) p.posY + 1, (int) p.posZ);
		boolean liquidAbove = event.player.world.getBlockState(bp).getMaterial().isLiquid();

		if (p.isInWater() && liquidAbove && isPlayerWearingFlippers(p)) {
			p.setNoGravity(true);
			d.targetSwimSpeed = 0f;
			
			if(GameSettings.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindForward)) {
				d.targetSwimSpeed = getFlipperSpeed(p);
			}
			if(GameSettings.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindBack)) {
				d.targetSwimSpeed = -getFlipperSpeed(p);
			}
			if(p.moveStrafing != 0) {
				d.targetSwimSpeed = getFlipperSpeed(p)/2;
			}
			
			
			if(d.currentSwimSpeed < d.targetSwimSpeed) {
				d.currentSwimSpeed+= d.swimSpeedAccel;
				
				if(d.currentSwimSpeed > d.targetSwimSpeed) {
					d.currentSwimSpeed = d.targetSwimSpeed;
				}
			}else if(d.currentSwimSpeed > d.targetSwimSpeed) {
				d.currentSwimSpeed-= d.swimSpeedAccel;
				
				if(d.currentSwimSpeed < d.targetSwimSpeed) {
					d.currentSwimSpeed = d.targetSwimSpeed;
				}
			}

			float currentSpeed = d.currentSwimSpeed * 0.1f;	
			float offset = 0f;

			p.motionX = currentSpeed * Math.sin(-(d.currentRotationYaw+offset) * (Math.PI / 180.0));
			p.motionZ = currentSpeed * Math.cos(-(d.currentRotationYaw+offset) * (Math.PI / 180.0));
			p.motionY = (currentSpeed) * Math.sin((d.currentRotationPitch+d.currentHeadPitchOffset) * (Math.PI / 180.0));

			if (p.isSneaking()) {
				p.setSneaking(false);
				if (p.motionY > -0.2f) {
					p.motionY -= 0.02f;
				} else {
					p.motionY = -0.2f;
				}
			}

		} else {
			d.targetSwimSpeed = 0f;
			p.setNoGravity(false);
		}
	}

	@SubscribeEvent
	public void onRenderPlayer(RenderPlayerEvent.Pre event) {
		EntityPlayer p = event.getEntityPlayer();
		PlayerSwimData d = getData(p);
		
		//
		//   EntityRenderRegistry.feetModel = new ModelScubaGear(0, EntityEquipmentSlot.FEET);
		//
		
		updateSwimRenderAngles(p);

		if (p.isInWater()) 
		{	
			p.limbSwingAmount = 0.2f+(d.currentSwimSpeed/20);		
			
			if(p.onGround && !this.isPlayerWearingFlippers(p)) {
				p.limbSwingAmount = p.moveForward*0.3f;

			}
		}else {
			if(d.currentRotationPitch == 0f && d.currentRotationRoll == 0f) {
				return;
			}
		}

		GlStateManager.pushMatrix();

			GlStateManager.translate(0, 1.5f, 0f);
			GlStateManager.rotate(d.currentRotationYaw, 0f, -1f, 0f);
			GlStateManager.rotate(d.currentRotationPitch, 1f, 0f, 0f);
			GlStateManager.rotate(d.currentRotationRoll, 0f, 0f, -1f);
			GlStateManager.translate(0, -1.5f, 0f);

			
			updateSwimDataAngles(p);
				
			clearPlayerRenderAngles(p);
			
			
			// Some basic paddling for now
		//}
	}

	@SubscribeEvent
	public void onRenderPlayer(RenderPlayerEvent.Post event) {
		EntityPlayer p = event.getEntityPlayer();
		
		if(!p.isInWater()){
			if(getData(p).currentRotationPitch == 0f && getData(p).currentRotationRoll == 0f) {
				return;
			}
		}
		//if (p.isInWater()) 
		{
			restorePlayerRenderAngles(p);
		}	

		GlStateManager.popMatrix();
	}
	
	public boolean isPlayerWearingFlippers(EntityPlayer p) {
		ItemStack bootSlot = p.inventory.armorItemInSlot(0);
		if(bootSlot != null) 
			return bootSlot.getItem().equals(ItemRegistry.pinkFlippers)
				|| bootSlot.getItem().equals(ItemRegistry.yellowFlippers);
		return false;
	}

	
	public float getFlipperSpeed(EntityPlayer p) {
		ItemStack bootSlot = p.inventory.armorItemInSlot(0);
		if(bootSlot != null) {
			return flipperSpeedMap.get(bootSlot.getItem());
		}
		return 0f;
	}
	
	public void updateSwimRenderAngles(EntityPlayer p) {
		PlayerSwimData d = getData(p);
		float ps = d.pitchSpeed;
		float ys = d.yawSpeed;
		float rs = d.rollSpeed;
		
		if(p.isInWater()) {
			d.targetRotationYaw = p.rotationYaw;
			d.targetHeadPitchOffset = 45f;
			d.targetRotationRoll = (float) p.moveStrafing * 90;
	
			// Not moving, level out
			if (p.moveForward == 0f) {
				d.targetRotationPitch = p.rotationPitch + 90f;
			}
			
			if(d.targetRotationRoll != 0) {
				d.targetRotationPitch -= 180f;
			}
			
			if(GameSettings.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump)
					&& p.equals(Minecraft.getMinecraft().player)) {
				d.targetSwimSpeed = getFlipperSpeed(p);
				d.targetRotationPitch = 0f;
				d.targetHeadPitchOffset = 45f;
			}
			
			if(GameSettings.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak)
					&& p.equals(Minecraft.getMinecraft().player)) {
				d.targetSwimSpeed = getFlipperSpeed(p);
				d.targetRotationPitch = 180f;
				d.targetHeadPitchOffset = 45f;
			}
			d.targetRotationPitch += d.targetRotationRoll*2f;
			
	
			// Backpaddle body and head adjustments
			if (p.moveForward < 0f) {
				d.targetRotationPitch = p.rotationPitch-65f;
				d.targetHeadPitchOffset = p.rotationPitch-45f;
			}
	
			// Full speed ahead cap'n
			if (p.moveForward > 0f) {
				d.targetRotationPitch = p.rotationPitch + 90f;
				d.targetHeadPitchOffset = p.rotationPitch + 90f;
	
				if (d.targetHeadPitchOffset > 90f) {
					d.targetHeadPitchOffset = 90f;
	
				}
			}
			
			// If moving sideways, look ahead 
			if(p.moveStrafing != 0) {
				d.targetHeadPitchOffset = p.rotationPitch + 55f;
			}
			
			
			BlockPos bp2 = new BlockPos((int) p.posX, (int) p.posY - 1, (int) p.posZ);
	
			// Above a floor and not attempting to move
			if(!p.world.getBlockState(bp2).getMaterial().isLiquid() && d.targetSwimSpeed == 0f && !this.isPlayerWearingFlippers(p)) {
				d.targetRotationPitch = 0f;
				d.targetHeadPitchOffset = 0f;
				d.targetRotationRoll = 0f;
				ps = ps*4;
				rs = rs*4;
				ys = ys*4;
			}
		
		}else {
			
			d.targetRotationPitch = 0f;
			d.targetRotationRoll = 0f;
			d.targetHeadPitchOffset = 0f;
			d.targetRotationYaw = p.rotationYaw;
			ps = ps*4;
			rs = rs*4;
			ys = ys*4;
		}
		
		
		d.currentRotationPitch = lerp(MathHelper.wrapDegrees(d.currentRotationPitch), MathHelper.wrapDegrees(d.targetRotationPitch), ps);
		d.currentHeadPitchOffset = lerp(MathHelper.wrapDegrees(d.currentHeadPitchOffset), MathHelper.wrapDegrees(d.targetHeadPitchOffset), ps);

		d.currentRotationYaw = lerp(d.currentRotationYaw, d.targetRotationYaw, ys);
		d.currentRotationRoll = lerp(d.currentRotationRoll, d.targetRotationRoll, rs);

	}

	public void updateSwimDataAngles(EntityPlayer p) {
		PlayerSwimData d = getData(p);

			d.rotationYawHead = p.rotationYawHead;
			d.prevRotationYawHead = p.prevRotationYawHead;
			
			d.rotationYaw = p.rotationYaw;
			d.prevRotationYaw = p.prevRotationYaw;
			d.renderYawOffset = p.renderYawOffset;
			d.prevRenderYawOffset = p.prevRenderYawOffset;
			d.rotationPitch = p.rotationPitch;
			d.prevRotationPitch = p.prevRotationPitch;
	
	}
	
	public void clearPlayerRenderAngles(EntityPlayer p) {
		PlayerSwimData d = getData(p);


			p.rotationYawHead = 0f;
			p.prevRotationYawHead = 0f;
		
		p.rotationYaw = 0f;
		p.prevRotationYaw = 0f;
		p.renderYawOffset = 0f;
		p.prevRenderYawOffset = 0f;
		p.rotationPitch = -MathHelper.wrapDegrees(d.currentHeadPitchOffset);
		p.prevRotationPitch = -MathHelper.wrapDegrees(d.currentHeadPitchOffset);

	}
	
	public void restorePlayerRenderAngles(EntityPlayer p) {
		PlayerSwimData d = getData(p);


			p.rotationYawHead = d.rotationYawHead;
			p.prevRotationYawHead = d.prevRotationYawHead;
		
		p.rotationYaw = d.rotationYaw;
		p.prevRotationYaw = d.prevRotationYaw;
		p.renderYawOffset = d.renderYawOffset;
		p.prevRenderYawOffset = d.prevRenderYawOffset;
		p.rotationPitch = d.rotationPitch;
		p.prevRotationPitch = d.prevRotationPitch;
	}

	public float lerp(float x1, float x2, float t) {
		float f = MathHelper.wrapDegrees(x2 - x1);
		if (f > t)
			f = t;
		if (f < -t)
			f = -t;
		return x1 + f;
	}
	
	public static PlayerSwimData getData(EntityPlayer p) {
		if (!rotationMap.containsKey(p.getUniqueID())) {
			rotationMap.put(p.getUniqueID(), new PlayerSwimData());
		}
		return rotationMap.get(p.getUniqueID());
	}
	
	public static class PlayerSwimData {
		public float rotationYawHead = 0f;
		public float prevRotationYawHead = 0f;
		public float rotationYaw = 0f;
		public float prevRotationYaw = 0f;
		public float renderYawOffset = 0f;
		public float prevRenderYawOffset = 0f;
		public float rotationPitch = 0f;
		public float prevRotationPitch = 0f;

		public float targetRotationPitch = 0f;
		public float targetRotationYaw = 0f;
		public float targetRotationRoll = 0f;
		public float currentRotationPitch = 0f;
		public float currentRotationYaw = 0f;
		public float currentRotationRoll = 0f;
		public float pitchSpeed = 1f;
		public float yawSpeed = 2f;
		public float rollSpeed = 0.5f;

		public float targetHeadPitchOffset = 0f;
		public float currentHeadPitchOffset = 0f;

		public float currentSwimSpeed = 1f;
		public float targetSwimSpeed = 0f;
		public float swimSpeedAccel = 0.004f;
	}
}
