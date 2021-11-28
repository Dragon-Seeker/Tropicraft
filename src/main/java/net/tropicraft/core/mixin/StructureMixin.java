package net.tropicraft.core.mixin;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.StructureBlock;
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

//    //TODO: REPLACE THIS AS FAST AS POSSIBLE!!!!
//    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate;processBlockInfos(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;Ljava/util/List;)Ljava/util/List;"),
//            method = "placeInWorld")
//    private List<StructureTemplate.StructureBlockInfo> tropic$procees(LevelAccessor world, BlockPos blockPos, BlockPos blockPos2, StructurePlaceSettings placementSettings, List<StructureTemplate.StructureBlockInfo> list) {
//        return tropic$process(world, blockPos, blockPos2, placementSettings, list);//, structure);
//        //cir.setReturnValue(true);
//    }

    @Unique
    private static StructureTemplate structureTemplate;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate;processBlockInfos(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;Ljava/util/List;)Ljava/util/List;"),
            method = "placeInWorld")
    private void tropic$procsesss(ServerLevelAccessor serverLevelAccessor, BlockPos blockPos, BlockPos blockPos2, StructurePlaceSettings structurePlaceSettings, Random random, int i, CallbackInfoReturnable<Boolean> cir) {
        structureTemplate = ((StructureTemplate) (Object)this);
    }

    @Override
    public StructureTemplate testGrab() {
        return structureTemplate;
    }

}
