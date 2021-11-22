package net.tropicraft.core.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.tropicraft.core.mixinExtensions.StructureExtensions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;

@Mixin(StructureTemplate.class)
public abstract class StructureMixin implements StructureExtensions {
    @Shadow
    @Final
    private List<StructureTemplate.StructureEntityInfo> entityInfoList;

    @Unique
    @Override
    public List<StructureTemplate.StructureEntityInfo> tropic$getEntities() {
        return entityInfoList;
    }

    @Inject(at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate;placeEntities(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Mirror;Lnet/minecraft/world/level/block/Rotation;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;Z)V"),
            method = "placeInWorld", cancellable = true)
    public void tropic$place(ServerLevelAccessor worldAccess, BlockPos blockPos, BlockPos blockPos2, StructurePlaceSettings placementSettings, Random random, int i, CallbackInfoReturnable<Boolean> cir) {
        tropic$addEntitiesToWorld(worldAccess, blockPos, placementSettings);
        cir.setReturnValue(true);
    }

    //TODO: REPLACE THIS AS FAST AS POSSIBLE!!!!
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate;processBlockInfos(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;Ljava/util/List;)Ljava/util/List;"),
            method = "placeInWorld")
    private List<StructureTemplate.StructureBlockInfo> tropic$procees(LevelAccessor world, BlockPos blockPos, BlockPos blockPos2, StructurePlaceSettings placementSettings, List<StructureTemplate.StructureBlockInfo> list) {
        return tropic$process(world, blockPos, blockPos2, placementSettings, list);//, structure);
        //cir.setReturnValue(true);
    }




}
