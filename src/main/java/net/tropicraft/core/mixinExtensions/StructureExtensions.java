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
import net.tropicraft.core.common.dimension.feature.jigsaw.AdjustBuildingHeightProcessor;
import net.tropicraft.core.common.dimension.feature.jigsaw.SmoothingGravityProcessor;
import net.tropicraft.core.common.dimension.feature.jigsaw.SteepPathProcessor;
import net.tropicraft.core.mixin.StructureAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

public interface StructureExtensions {
    List<StructureTemplate.StructureEntityInfo> tropic$getEntities();

    default Vec3 transformedVec3d(StructurePlaceSettings placementIn, Vec3 pos) {
        return StructureTemplate.transform(pos, placementIn.getMirror(), placementIn.getRotation(), placementIn.getRotationPivot());
    }

    default List<StructureTemplate.StructureEntityInfo> tropic$processEntityInfos(@Nullable StructureTemplate structure, LevelAccessor world, BlockPos blockPos, StructurePlaceSettings settings, List<StructureTemplate.StructureEntityInfo> infos) {
        List<StructureTemplate.StructureEntityInfo> list = Lists.newArrayList();
        for(StructureTemplate.StructureEntityInfo entityInfo : infos) {
            Vec3 pos = transformedVec3d(settings, entityInfo.pos).add(Vec3.atLowerCornerOf(blockPos));
            BlockPos blockpos = StructureTemplate.calculateRelativePosition(settings, entityInfo.blockPos).offset(blockPos);
            StructureTemplate.StructureEntityInfo info = new StructureTemplate.StructureEntityInfo(pos, blockpos, entityInfo.nbt);
            for (StructureProcessor proc : settings.getProcessors()) {
                if(proc instanceof AdjustBuildingHeightProcessor){
                    info = ((AdjustBuildingHeightProcessor) proc).processEntity(world, blockPos, entityInfo, info, settings, structure);
                }

                else{
                    info = ((StructureProcessorExtension) proc).processEntity(world, blockPos, entityInfo, info, settings, structure);
                }

                if (info == null)
                    break;
            }
            if (info != null)
                list.add(info);
        }
        return list;
    }

    default void tropic$addEntitiesToWorld(ServerLevelAccessor world, BlockPos blockPos, StructurePlaceSettings settings) {
        for(StructureTemplate.StructureEntityInfo Structure$entityinfo : tropic$processEntityInfos((StructureTemplate) this, world, blockPos, settings, this.tropic$getEntities())) {
            BlockPos blockpos = StructureTemplate.transform(Structure$entityinfo.blockPos, settings.getMirror(), settings.getRotation(), settings.getRotationPivot()).offset(blockPos);
            blockpos = Structure$entityinfo.blockPos;
            if (settings.getBoundingBox() == null || settings.getBoundingBox().isInside(blockpos)) {
                CompoundTag compoundnbt = Structure$entityinfo.nbt.copy();
                Vec3 vector3d1 = Structure$entityinfo.pos;
                ListTag listnbt = new ListTag();
                listnbt.add(DoubleTag.valueOf(vector3d1.x));
                listnbt.add(DoubleTag.valueOf(vector3d1.y));
                listnbt.add(DoubleTag.valueOf(vector3d1.z));
                compoundnbt.put("Pos", listnbt);
                compoundnbt.remove("UUID");
                StructureAccessor.getEntity(world, compoundnbt).ifPresent((entity) -> {
                    float f = entity.mirror(settings.getMirror());
                    f = f + (entity.getYRot() - entity.rotate(settings.getRotation()));
                    entity.moveTo(vector3d1.x, vector3d1.y, vector3d1.z, f, entity.getXRot());
                    if (settings.shouldFinalizeEntities() && entity instanceof Mob) {
                        ((Mob) entity).finalizeSpawn(world, world.getCurrentDifficultyAt(new BlockPos(vector3d1)), MobSpawnType.STRUCTURE, (SpawnGroupData)null, compoundnbt);
                    }

                    world.addFreshEntityWithPassengers(entity);
                });
            }
        }

    }


    default List<StructureTemplate.StructureBlockInfo> tropic$process(LevelAccessor world, BlockPos pos, BlockPos blockPos, StructurePlaceSettings placementData, List<StructureTemplate.StructureBlockInfo> list){//, @Nullable Structure structure) {
        List<StructureTemplate.StructureBlockInfo> list2 = Lists.newArrayList();
        Iterator var6 = list.iterator();

        while(var6.hasNext()) {
            StructureTemplate.StructureBlockInfo structureBlockInfo = (StructureTemplate.StructureBlockInfo)var6.next();
            BlockPos blockPos2 = StructureTemplate.calculateRelativePosition(placementData, structureBlockInfo.pos).offset(pos);
            StructureTemplate.StructureBlockInfo structureBlockInfo2 = new StructureTemplate.StructureBlockInfo(blockPos2, structureBlockInfo.state, structureBlockInfo.nbt != null ? structureBlockInfo.nbt.copy() : null);

            for(Iterator iterator = placementData.getProcessors().iterator(); structureBlockInfo2 != null && iterator.hasNext();) {
                StructureProcessor processor = ((StructureProcessor)iterator.next());
                if(processor instanceof AdjustBuildingHeightProcessor) {
                    structureBlockInfo2 = ((AdjustBuildingHeightProcessor)processor).process(world, pos, blockPos, structureBlockInfo, structureBlockInfo2, placementData, (StructureTemplate) this);
                }

                else if(processor instanceof SmoothingGravityProcessor) {
                    structureBlockInfo2 = ((SmoothingGravityProcessor)processor).process(world, pos, blockPos, structureBlockInfo, structureBlockInfo2, placementData, (StructureTemplate) this);
                }

                else if(processor instanceof SteepPathProcessor) {
                    structureBlockInfo2 = ((SteepPathProcessor)processor).process(world, pos, blockPos, structureBlockInfo, structureBlockInfo2, placementData, (StructureTemplate)this );
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
