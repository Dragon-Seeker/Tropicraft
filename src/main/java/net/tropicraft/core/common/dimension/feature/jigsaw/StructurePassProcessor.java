package net.tropicraft.core.common.dimension.feature.jigsaw;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public interface StructurePassProcessor {

    default StructureTemplate.StructureBlockInfo process(LevelReader world, BlockPos seedPos, BlockPos pos2, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings placementSettingsIn, StructureTemplate template) {
        return blockInfo;
    }
}
