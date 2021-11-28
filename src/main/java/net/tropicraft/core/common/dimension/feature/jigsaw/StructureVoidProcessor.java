package net.tropicraft.core.common.dimension.feature.jigsaw;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.tropicraft.Constants;
import net.tropicraft.core.mixinExtensions.StructureExtensions;
import org.jetbrains.annotations.Nullable;

public class StructureVoidProcessor extends StructureProcessor implements StructurePassProcessor{
    static final StructureProcessorType<StructureVoidProcessor> TYPE = Registry.register(Registry.STRUCTURE_PROCESSOR, Constants.MODID + ":structure_void", () -> Codec.unit(new StructureVoidProcessor()));

    @Nullable
    @Override
    public StructureBlockInfo process(LevelReader levelReader, BlockPos blockPos, BlockPos blockPos2, StructureBlockInfo structureBlockInfo, StructureBlockInfo structureBlockInfo2, StructurePlaceSettings structurePlaceSettings, StructureTemplate template) {
        if (structureBlockInfo2.state.getBlock() == Blocks.STRUCTURE_VOID) {
            return new StructureBlockInfo(structureBlockInfo2.pos, Blocks.AIR.defaultBlockState(), structureBlockInfo2.nbt);
        }
        return structureBlockInfo2;
    }

    @Nullable
    @Override
    public StructureBlockInfo processBlock(LevelReader levelReader, BlockPos blockPos, BlockPos blockPos2, StructureBlockInfo structureBlockInfo, StructureBlockInfo structureBlockInfo2, StructurePlaceSettings structurePlaceSettings) {
        return process(levelReader, blockPos, blockPos2, structureBlockInfo, structureBlockInfo2, structurePlaceSettings, ((StructureExtensions)new StructureTemplate()).testGrab());
        //return structureBlockInfo2;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return TYPE;
    }
}
