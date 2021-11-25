package net.tropicraft.core.mixin;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.dimension.feature.jigsaw.AdjustBuildingHeightProcessor;
import net.tropicraft.core.common.dimension.feature.jigsaw.StructurePassProcessor;
import net.tropicraft.core.mixinExtensions.StructureExtensions;
import net.tropicraft.core.mixinExtensions.StructureProcessorExtension;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Mixin(StructureTemplate.class)
public abstract class StructureMixin implements StructureExtensions {

    //TODO: REPLACE THIS AS FAST AS POSSIBLE!!!!
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate;processBlockInfos(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;Ljava/util/List;)Ljava/util/List;"),
            method = "placeInWorld")
    private List<StructureTemplate.StructureBlockInfo> tropic$procees(LevelAccessor world, BlockPos blockPos, BlockPos blockPos2, StructurePlaceSettings placementSettings, List<StructureTemplate.StructureBlockInfo> list) {
        return tropic$process(world, blockPos, blockPos2, placementSettings, list);//, structure);
        //cir.setReturnValue(true);
    }

//    @ModifyVariable(method ="processBlockInfos", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessor;processBlock(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$StructureBlockInfo;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$StructureBlockInfo;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;)Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$StructureBlockInfo;"))
//    private static StructureTemplate.StructureBlockInfo test(StructureTemplate.StructureBlockInfo structureBlockInfo){
//        return structureBlockInfo;
//    }

//    @Unique
//    private ServerLevelAccessor serverLevelAccessor;
//
//    @Unique
//    private BlockPos blockPos1;
//
//    @Unique
//    private BlockPos blockPos2;
//
//    @Unique
//    private StructurePlaceSettings structurePlaceSettings;
//
//    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate;processBlockInfos(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;Ljava/util/List;)Ljava/util/List;"),
//            method = "placeInWorld")
//    private void tropic$procsesss(ServerLevelAccessor serverLevelAccessor, BlockPos blockPos, BlockPos blockPos2, StructurePlaceSettings structurePlaceSettings, Random random, int i, CallbackInfoReturnable<Boolean> cir) {
//        this.serverLevelAccessor = serverLevelAccessor;
//        this.blockPos1 = blockPos;
//        this.blockPos2 = blockPos2;
//        this.structurePlaceSettings = structurePlaceSettings;
//    }
//
//    @ModifyVariable(method ="placeInWorld", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate;processBlockInfos(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;Ljava/util/List;)Ljava/util/List;"), ordinal = 1)
//    private List<StructureTemplate.StructureBlockInfo> test(List<StructureTemplate.StructureBlockInfo> list){
//        return reprocessBlockInfos(this.serverLevelAccessor, this.blockPos1, this.blockPos2, this.structurePlaceSettings, list);
//    }
//
//    @Unique
//    private List<StructureTemplate.StructureBlockInfo> reprocessBlockInfos(LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2, StructurePlaceSettings structurePlaceSettings, List<StructureTemplate.StructureBlockInfo> list) {
//        List<StructureTemplate.StructureBlockInfo> list2 = Lists.newArrayList();
//        Iterator var6 = list.iterator();
//
//        while(var6.hasNext()) {
//            StructureTemplate.StructureBlockInfo structureBlockInfo = (StructureTemplate.StructureBlockInfo)var6.next();
//            BlockPos blockPos3 = StructureTemplate.calculateRelativePosition(structurePlaceSettings, structureBlockInfo.pos).offset(blockPos);
//            StructureTemplate.StructureBlockInfo structureBlockInfo2 = new StructureTemplate.StructureBlockInfo(blockPos3, structureBlockInfo.state, structureBlockInfo.nbt != null ? structureBlockInfo.nbt.copy() : null);
//
//            for(Iterator iterator = structurePlaceSettings.getProcessors().iterator(); structureBlockInfo2 != null && iterator.hasNext(); structureBlockInfo2 = ((StructureProcessor)iterator.next() instanceof StructurePassProcessor) ? ((StructurePassProcessor)iterator.next()).process(levelAccessor, blockPos, blockPos2, structureBlockInfo, structureBlockInfo2, structurePlaceSettings, ((StructureTemplate) (Object) this)) : structureBlockInfo2){
//            }
//
//            if (structureBlockInfo2 != null) {
//                list2.add(structureBlockInfo2);
//            }
//        }
//
//        return list2;
//    }
}
