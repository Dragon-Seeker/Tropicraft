package net.tropicraft.core.client.tileentity;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.tropicraft.Constants;
import net.tropicraft.core.client.ClientSetup;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.tileentity.BambooChestTileEntity;

import java.util.HashMap;

//@OnlyIn(Dist.CLIENT)
//@EventBusSubscriber(modid = Constants.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class BambooChestRenderer<T extends BlockEntity & LidBlockEntity> implements BlockEntityRenderer<BambooChestTileEntity> {
    private static final HashMap<Block, RenderType[]> LAYERS = Maps.newHashMap();
    private static final RenderType[] defaultLayer;
    private static final RenderType[] bambooChestLayer;

    private static ItemStack stack = new ItemStack(TropicraftBlocks.BAMBOO_CHEST, 1);


    private static final int ID_NORMAL = 0;
    private static final int ID_LEFT = 1;
    private static final int ID_RIGHT = 2;

    private boolean christmas;
    private final ModelPart partA; //singleChestLid
    private final ModelPart partC; //singleChestBase
    private final ModelPart partB; //singleChestLatch
    private final ModelPart partRightA; //doubleChestRightLid
    private final ModelPart partRightC; //doubleChestRightBase
    private final ModelPart partRightB; //doubleChestRightLatch
    private final ModelPart partLeftA;  //doubleChestLeftLid
    private final ModelPart partLeftC;  //doubleChestLeftBase
    private final ModelPart partLeftB; //doubleChestLeftLatch


    public BambooChestRenderer(BlockEntityRendererProvider.Context ctx) {
        ModelPart modelPart = ctx.bakeLayer(ClientSetup.BAMBOO_CHEST);
        this.partC = modelPart.getChild("bottom");
        this.partA = modelPart.getChild("lid");
        this.partB = modelPart.getChild("lock");

        ModelPart doubleChestLeft = ctx.bakeLayer(ClientSetup.BAMBOO_DOUBLE_CHEST_RIGHT);
        this.partRightC = doubleChestLeft.getChild("bottom");
        this.partRightA = doubleChestLeft.getChild("lid");
        this.partRightB = doubleChestLeft.getChild("lock");

        ModelPart doubleChestRight = ctx.bakeLayer(ClientSetup.BAMBOO_DOUBLE_CHEST_LEFT);
        this.partLeftC = doubleChestRight.getChild("bottom");
        this.partLeftA = doubleChestRight.getChild("lid");
        this.partLeftB = doubleChestRight.getChild("lock");

    }

    public static LayerDefinition getSingleTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        /*
        this.partC = new ModelPart(64, 64, 0, 19);
        this.partC.addCuboid(1.0F, 0.0F, 1.0F, 14.0F, 9.0F, 14.0F, 0.0F);

        this.partA = new ModelPart(64, 64, 0, 0);
        this.partA.addCuboid(1.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F, 0.0F);
        this.partA.pivotY = 9.0F;
        this.partA.pivotZ = 1.0F;

        this.partB = new ModelPart(64, 64, 0, 0);
        this.partB.addCuboid(7.0F, -1.0F, 15.0F, 2.0F, 4.0F, 1.0F, 0.0F);
        this.partB.pivotY = 8.0F;
         */

        modelPartData.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 19).addBox(1.0F, 0.0F, 1.0F, 14.0F, 9.0F, 14.0F), PartPose.ZERO);
        modelPartData.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F), PartPose.offset(0.0F, 9.0F, 1.0F));
        modelPartData.addOrReplaceChild("lock", CubeListBuilder.create().texOffs(0, 0).addBox(7.0F, -1.0F, 15.0F, 2.0F, 4.0F, 1.0F), PartPose.offset(0.0F, 8.0F, 0.0F));

        return LayerDefinition.create(modelData, 64, 64);
    }

    public static LayerDefinition getRightDoubleTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        /*
        this.partRightC = new ModelPart(64, 64, 0, 19);
        this.partRightC.addCuboid(1.0F, 0.0F, 1.0F, 15.0F, 9.0F, 14.0F, 0.0F);

        this.partRightA = new ModelPart(64, 64, 0, 0);
        this.partRightA.addCuboid(1.0F, 0.0F, 0.0F, 15.0F, 5.0F, 14.0F, 0.0F);
        this.partRightA.pivotY = 9.0F;
        this.partRightA.pivotZ = 1.0F;

        this.partRightB = new ModelPart(64, 64, 0, 0);
        this.partRightB.addCuboid(15.0F, -1.0F, 15.0F, 1.0F, 4.0F, 1.0F, 0.0F);
        this.partRightB.pivotY = 8.0F;
         */

        modelPartData.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 19).addBox(1.0F, 0.0F, 1.0F, 15.0F, 9.0F, 14.0F), PartPose.ZERO);
        modelPartData.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, 0.0F, 0.0F, 15.0F, 5.0F, 14.0F), PartPose.offset(0.0F, 9.0F, 1.0F));
        modelPartData.addOrReplaceChild("lock", CubeListBuilder.create().texOffs(0, 0).addBox(15.0F, -1.0F, 15.0F, 1.0F, 4.0F, 1.0F), PartPose.offset(0.0F, 8.0F, 0.0F));

        return LayerDefinition.create(modelData, 64, 64);
    }

    public static LayerDefinition getLeftDoubleTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        /*
        this.partLeftC = new ModelPart(64, 64, 0, 19);
        this.partLeftC.addCuboid(0.0F, 0.0F, 1.0F, 15.0F, 9.0F, 14.0F, 0.0F);

        this.partLeftA = new ModelPart(64, 64, 0, 0);
        this.partLeftA.addCuboid(0.0F, 0.0F, 0.0F, 15.0F, 5.0F, 14.0F, 0.0F);
        this.partLeftA.pivotY = 9.0F;
        this.partLeftA.pivotZ = 1.0F;

        this.partLeftB = new ModelPart(64, 64, 0, 0);
        this.partLeftB.addCuboid(0.0F, -1.0F, 15.0F, 1.0F, 4.0F, 1.0F, 0.0F);
        this.partLeftB.pivotY = 8.0F;
         */

        modelPartData.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 19).addBox(0.0F, 0.0F, 1.0F, 15.0F, 9.0F, 14.0F), PartPose.ZERO);
        modelPartData.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 15.0F, 5.0F, 14.0F), PartPose.offset(0.0F, 9.0F, 1.0F));
        modelPartData.addOrReplaceChild("lock", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -1.0F, 15.0F, 1.0F, 4.0F, 1.0F), PartPose.offset(0.0F, 8.0F, 0.0F));

        return LayerDefinition.create(modelData, 64, 64);
    }

    public void render(BambooChestTileEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        Level world = entity.getLevel();
        boolean worldExists = world != null;
        BlockState blockState = worldExists ? entity.getBlockState() : Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
        ChestType chestType = blockState.hasProperty(ChestBlock.TYPE) ? blockState.getValue(ChestBlock.TYPE) : ChestType.SINGLE;
        Block block = blockState.getBlock();
        if (block instanceof AbstractChestBlock) {
            AbstractChestBlock<?> abstractChestBlock = (AbstractChestBlock<?>) block;
            boolean isDouble = chestType != ChestType.SINGLE;
            float f = blockState.getValue(ChestBlock.FACING).toYRot();
            DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> propertySource;



            matrices.pushPose();
            matrices.translate(0.5D, 0.5D, 0.5D);
            matrices.mulPose(Vector3f.YP.rotationDegrees(-f));
            matrices.translate(-0.5D, -0.5D, -0.5D);

            if (worldExists) {
                propertySource = abstractChestBlock.combine(blockState, world, entity.getBlockPos(), true);
            } else {
                propertySource = DoubleBlockCombiner.Combiner::acceptNone;
            }

            float pitch = propertySource.apply(ChestBlock.opennessCombiner((LidBlockEntity)entity)).get(tickDelta);
            pitch = 1.0F - pitch;
            pitch = 1.0F - pitch * pitch * pitch;
            @SuppressWarnings({ "unchecked", "rawtypes" })
            int blockLight = ((Int2IntFunction) propertySource.apply(new BrightnessCombiner())).applyAsInt(light);

            VertexConsumer vertexConsumer = getConsumer(vertexConsumers, block, chestType);

            if (isDouble) {
                if (chestType == ChestType.LEFT) {
                    this.renderParts(matrices, vertexConsumer, this.partLeftA, this.partLeftB, this.partLeftC, pitch, blockLight, overlay);
                } else {
                    this.renderParts(matrices, vertexConsumer, this.partRightA, this.partRightB, this.partRightC, pitch, blockLight, overlay);
                }
            } else {
                this.renderParts(matrices, vertexConsumer, this.partA, this.partB, this.partC, pitch, blockLight, overlay);
            }

            matrices.popPose();
        }
    }

    private void renderParts(PoseStack matrices, VertexConsumer vertices, ModelPart chestlid, ModelPart chestlatch, ModelPart chestbase, float pitch, int light, int overlay) {
        chestlid.xRot = -(pitch * 1.5707964F);
        chestlatch.xRot = chestlid.xRot;
        chestlid.render(matrices, vertices, light, overlay);
        chestlatch.render(matrices, vertices, light, overlay);
        chestbase.render(matrices, vertices, light, overlay);
    }

    private static RenderType getChestTexture(ChestType type, RenderType[] layers) {
        switch (type) {
            case LEFT:
                return layers[ID_LEFT];
            case RIGHT:
                return layers[ID_RIGHT];
            case SINGLE:
            default:
                return layers[ID_NORMAL];
        }
    }

    public static VertexConsumer getConsumer(MultiBufferSource provider, Block block, ChestType chestType) {
        RenderType[] layers = LAYERS.getOrDefault(block, bambooChestLayer);
        return provider.getBuffer(getChestTexture(chestType, layers));
    }

    //For the future of dynamic creation of chests rather than hard coding such

    public static void registerRenderLayer(Block block) {
        ResourceLocation blockId = Registry.BLOCK.getKey(block);
        String modId = blockId.getNamespace();
        String path = blockId.getPath();
        LAYERS.put(block, new RenderType[] {
                RenderType.entityCutout(new ResourceLocation(modId, "textures/entity/chest/" + path + ".png")),
                RenderType.entityCutout(new ResourceLocation(modId, "textures/entity/chest/" + path + "_left.png")),
                RenderType.entityCutout(new ResourceLocation(modId, "textures/entity/chest/" + path + "_right.png"))
        });
    }

    static {
        defaultLayer = new RenderType[] {
                RenderType.entityCutout(new ResourceLocation("textures/entity/chest/normal.png")),
                RenderType.entityCutout(new ResourceLocation("textures/entity/chest/normal_left.png")),
                RenderType.entityCutout(new ResourceLocation("textures/entity/chest/normal_right.png"))
        };

        bambooChestLayer = new RenderType[] {
                RenderType.entityCutout(new ResourceLocation(Constants.MODID, "textures/entity/chest/" + Registry.BLOCK.getKey(TropicraftBlocks.BAMBOO_CHEST).getPath() + "_normal.png")),
                RenderType.entityCutout(new ResourceLocation(Constants.MODID, "textures/entity/chest/" + Registry.BLOCK.getKey(TropicraftBlocks.BAMBOO_CHEST).getPath() + "_left.png")),
                RenderType.entityCutout(new ResourceLocation(Constants.MODID, "textures/entity/chest/" + Registry.BLOCK.getKey(TropicraftBlocks.BAMBOO_CHEST).getPath() + "_right.png"))
        };
    }
}
