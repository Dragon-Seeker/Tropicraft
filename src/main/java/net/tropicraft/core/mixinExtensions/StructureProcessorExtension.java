package net.tropicraft.core.mixinExtensions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public interface StructureProcessorExtension {

    @Deprecated
    default StructureTemplate.StructureBlockInfo process(LevelReader world, BlockPos seedPos, BlockPos pos2, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings placementSettingsIn) {
           return blockInfo;
        }

    default StructureTemplate.StructureEntityInfo processEntity(LevelReader world, BlockPos seedPos, StructureTemplate.StructureEntityInfo rawEntityInfo, StructureTemplate.StructureEntityInfo entityInfo, StructurePlaceSettings placementSettings, StructureTemplate template) {
        return entityInfo;
    }

    default StructureTemplate.StructureBlockInfo process(LevelReader world, BlockPos seedPos, BlockPos pos2, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings placementSettingsIn, StructureTemplate template) {
        return process(world, seedPos, pos2, originalBlockInfo, blockInfo, placementSettingsIn);
    }
}
