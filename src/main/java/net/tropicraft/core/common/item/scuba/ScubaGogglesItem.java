package net.tropicraft.core.common.item.scuba;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.bermuda.common.forge.ItemEx;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.ItemStack;
import net.tropicraft.Constants;
import net.tropicraft.core.client.data.TropicraftLangKeys;

import java.util.UUID;

//@EventBusSubscriber(modid = Constants.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
public class ScubaGogglesItem extends ScubaArmorItem implements ItemEx {

    private static final ResourceLocation GOGGLES_OVERLAY_TEX_PATH = new ResourceLocation(Constants.MODID, "textures/gui/goggles.png");

    //public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Constants.MODID);

    // This is never registered to any entities, so it's not used in any logic
    // Just here for the nice tooltip
    private static final Attribute UNDERWATER_VISIBILITY = Registry.register(Registry.ATTRIBUTE,
            "underwater_visibility",
            new RangedAttribute(TropicraftLangKeys.SCUBA_VISIBILITY_STAT.getKey(), 0, -1, 1)
    );
    private static final AttributeModifier VISIBILITY_BOOST = new AttributeModifier(UUID.fromString("b09a907f-8264-455b-af81-997c06aa2268"), Constants.MODID + ".underwater.visibility", 0.25, Operation.MULTIPLY_BASE);

    private final LazyLoadedValue<Multimap<Attribute, AttributeModifier>> boostedModifiers;

    public ScubaGogglesItem(ScubaType type, Properties builder) {
        super(type, EquipmentSlot.HEAD, builder);

        // lazily initialize because attributes are registered after items
        this.boostedModifiers = new LazyLoadedValue<>(() ->
                ImmutableMultimap.<Attribute, AttributeModifier>builder()
                        .putAll(super.getDefaultAttributeModifiers(EquipmentSlot.HEAD))
                        .put(UNDERWATER_VISIBILITY, VISIBILITY_BOOST)
                        .build()
        );
    }
    
    // Taken from ForgeIngameGui#renderPumpkinOverlay
//    @Override
//    @Environment(EnvType.CLIENT)
//    public void renderHelmetOverlay(ItemStack stack, Player player, int width, int height, float partialTicks) {
//        RenderSystem.disableDepthTest();
//        RenderSystem.depthMask(false);
//        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//        //RenderSystem.disableAlphaTest();
//        Minecraft mc = Minecraft.getInstance();
//        double scaledWidth = mc.getWindow().getGuiScaledWidth();
//        double scaledHeight = mc.getWindow().getGuiScaledHeight();
//        RenderSystem.setShaderTexture(0, GOGGLES_OVERLAY_TEX_PATH); //mc.getTextureManager().bind(GOGGLES_OVERLAY_TEX_PATH);
//        Tesselator tessellator = Tesselator.getInstance();
//        BufferBuilder bufferbuilder = tessellator.getBuilder();
//        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//        bufferbuilder.vertex(0.0D, scaledHeight, -90.0D).uv(0.0f, 1.0f).endVertex();
//        bufferbuilder.vertex(scaledWidth, scaledHeight, -90.0D).uv(1.0f, 1.0f).endVertex();
//        bufferbuilder.vertex(scaledWidth, 0.0D, -90.0D).uv(1.0f, 0.0f).endVertex();
//        bufferbuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0f, 0.0f).endVertex();
//        tessellator.end();
//        RenderSystem.depthMask(true);
//        RenderSystem.enableDepthTest();
//        //RenderSystem.enableAlphaTest();
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//
//    }

    //TODO [PORT]: It is just solid blue so... something must be fixed for such
//    @SubscribeEvent
//    @OnlyIn(Dist.CLIENT)
//    public static void renderWaterFog(FogDensity event) {
//        Camera info = event.getInfo();
//        //FluidState fluid = info.getFluidInCamera();
//        FogType fogType = info.getFluidInCamera();
//        if (/*fluid.is(FluidTags.WATER) &&*/fogType == FogType.WATER && info.getEntity() instanceof LocalPlayer player) {
//            if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ScubaGogglesItem) {
//                // Taken from FogRenderer#setupFog in the case where the player is in fluid
//                //RenderSystem.fogMode(GlStateManager.FogMode.EXP2);
//                float f = 0.05F - player.getWaterVision() * player.getWaterVision() * 0.03F;
//
//                // Reduce fog slightly
//                f *= 0.75F;
//
//                event.setDensity(f);
//                event.setCanceled(true);
//            }
//        }
//    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.HEAD) {
            return boostedModifiers.get();
        } else {
            return super.getDefaultAttributeModifiers(slot);
        }
    }

    public static void init(){

    }
}
