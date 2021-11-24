package net.tropicraft.core.client.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.tropicraft.Constants;
import net.tropicraft.core.client.ClientSetup;

public class BambooChestRenderer<T extends ChestBlockEntity> extends ChestRenderer {

    public static final Material BAMBOO_CHEST_MATERIAL = getChestMaterial("bamboo_chest/normal");
    public static final Material BAMBOO_CHEST_LEFT_MATERIAL = getChestMaterial("bamboo_chest/normal_left");
    public static final Material BAMBOO_CHEST_RIGHT_MATERIAL = getChestMaterial("bamboo_chest/normal_right");

    private static Material getChestMaterial(ChestType chestType, Material normalMaterial, Material leftMaterial, Material rightMaterial) {
        switch(chestType) {
            case LEFT:
                return leftMaterial;
            case RIGHT:
                return rightMaterial;
            case SINGLE:
            default:
                return normalMaterial;
        }
    }

    private static Material getChestMaterial(String chestName) {
        return new Material(Sheets.CHEST_SHEET, new ResourceLocation(Constants.MODID, "textures/entity/be/" + chestName + ".png"));
    }

    public BambooChestRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        ModelPart modelPart = context.bakeLayer(ClientSetup.BAMBOO_CHEST);
        this.bottom = modelPart.getChild("bottom");
        this.lid = modelPart.getChild("lid");
        this.lock = modelPart.getChild("lock");
        ModelPart modelPart2 = context.bakeLayer(ClientSetup.BAMBOO_DOUBLE_CHEST_LEFT);
        this.doubleLeftBottom = modelPart2.getChild("bottom");
        this.doubleLeftLid = modelPart2.getChild("lid");
        this.doubleLeftLock = modelPart2.getChild("lock");
        ModelPart modelPart3 = context.bakeLayer(ClientSetup.BAMBOO_DOUBLE_CHEST_RIGHT);
        this.doubleRightBottom = modelPart3.getChild("bottom");
        this.doubleRightLid = modelPart3.getChild("lid");
        this.doubleRightLock = modelPart3.getChild("lock");
    }

    protected Material getMaterial(T tileEntity, ChestType chestType) {
        return getChestMaterial(chestType, BAMBOO_CHEST_MATERIAL, BAMBOO_CHEST_LEFT_MATERIAL, BAMBOO_CHEST_RIGHT_MATERIAL);
    }

    @Override
    public void render(BlockEntity blockEntity, float tickDelta, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay) {
        Level level = blockEntity.getLevel();
        boolean bl = level != null;
        BlockState blockState = bl ? blockEntity.getBlockState() : (BlockState)Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
        ChestType chestType = blockState.hasProperty(ChestBlock.TYPE) ? (ChestType)blockState.getValue(ChestBlock.TYPE) : ChestType.SINGLE;
        Block block = blockState.getBlock();
        if (block instanceof AbstractChestBlock) {
            AbstractChestBlock<?> abstractChestBlock = (AbstractChestBlock)block;
            boolean isDouble = chestType != ChestType.SINGLE;
            poseStack.pushPose();
            float g = ((Direction)blockState.getValue(ChestBlock.FACING)).toYRot();
            poseStack.translate(0.5D, 0.5D, 0.5D);
            poseStack.mulPose(Vector3f.YP.rotationDegrees(-g));
            poseStack.translate(-0.5D, -0.5D, -0.5D);
            DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> neighborCombineResult;
            if (bl) {
                neighborCombineResult = abstractChestBlock.combine(blockState, level, blockEntity.getBlockPos(), true);
            } else {
                neighborCombineResult = DoubleBlockCombiner.Combiner::acceptNone;
            }

            float pitch = neighborCombineResult.apply(ChestBlock.opennessCombiner((LidBlockEntity)blockEntity)).get(tickDelta);
            pitch = 1.0F - pitch;
            pitch = 1.0F - pitch * pitch * pitch;
            @SuppressWarnings({ "unchecked", "rawtypes" })
            int blockLight = ((Int2IntFunction) neighborCombineResult.apply(new BrightnessCombiner())).applyAsInt(light);

            Material material = getMaterial((T) blockEntity, chestType);
            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entityCutout(material.texture()));//getConsumer(vertexConsumers, block, chestType);

            if (isDouble) {
                if (chestType == ChestType.LEFT) {
                    this.render(poseStack, vertexConsumer, this.doubleLeftLid, this.doubleLeftLock, this.doubleLeftBottom, pitch, blockLight, overlay);
                } else {
                    this.render(poseStack, vertexConsumer, this.doubleRightLid, this.doubleRightLock, this.doubleRightBottom, pitch, blockLight, overlay);
                }
            } else {
                this.render(poseStack, vertexConsumer, this.lid, this.lock, this.bottom, pitch, blockLight, overlay);
            }

            poseStack.popPose();
        }
    }
}
