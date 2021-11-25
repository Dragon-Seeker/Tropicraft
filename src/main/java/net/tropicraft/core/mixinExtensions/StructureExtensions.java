package net.tropicraft.core.mixinExtensions;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.dimension.feature.jigsaw.*;
import net.tropicraft.core.mixin.StructureAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

public interface StructureExtensions {

    default List<StructureTemplate.StructureBlockInfo> tropic$process(LevelAccessor world, BlockPos pos, BlockPos blockPos, StructurePlaceSettings placementData, List<StructureTemplate.StructureBlockInfo> list){//, @Nullable Structure structure) {
        List<StructureTemplate.StructureBlockInfo> list2 = Lists.newArrayList();
        Iterator var6 = list.iterator();

        while(var6.hasNext()) {
            StructureTemplate.StructureBlockInfo structureBlockInfo = (StructureTemplate.StructureBlockInfo)var6.next();
            BlockPos blockPos2 = StructureTemplate.calculateRelativePosition(placementData, structureBlockInfo.pos).offset(pos);
            StructureTemplate.StructureBlockInfo structureBlockInfo2 = new StructureTemplate.StructureBlockInfo(blockPos2, structureBlockInfo.state, structureBlockInfo.nbt != null ? structureBlockInfo.nbt.copy() : null);

            for(Iterator iterator = placementData.getProcessors().iterator(); structureBlockInfo2 != null && iterator.hasNext();) {
                StructureProcessor processor = ((StructureProcessor)iterator.next());

                if(processor instanceof StructurePassProcessor) {
                    structureBlockInfo2 = ((StructurePassProcessor)processor).process(world, pos, blockPos, structureBlockInfo, structureBlockInfo2, placementData, (StructureTemplate) this);
                }

                else {
                    structureBlockInfo2 = processor.processBlock(world, pos, blockPos, structureBlockInfo, structureBlockInfo2, placementData);
                }
            }

            if (structureBlockInfo2 != null) {
                list2.add(structureBlockInfo2);
            }
        }

        return list2;
    }
}
