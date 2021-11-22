package net.tropicraft.core.common.dimension.feature.jigsaw;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public abstract class StructurePassProcessor extends StructureProcessor {
    @Deprecated
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader world, BlockPos seedPos, BlockPos pos2, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings placementSettingsIn) {
        return blockInfo;
    }

    public StructureTemplate.StructureEntityInfo processEntity(LevelReader world, BlockPos seedPos, StructureTemplate.StructureEntityInfo rawEntityInfo, StructureTemplate.StructureEntityInfo entityInfo, StructurePlaceSettings placementSettings, StructureTemplate template) {
        return entityInfo;
    }

    public StructureTemplate.StructureBlockInfo process(LevelReader world, BlockPos seedPos, BlockPos pos2, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings placementSettingsIn, StructureTemplate template) {
        return processBlock(world, seedPos, pos2, originalBlockInfo, blockInfo, placementSettingsIn);
    }
}
