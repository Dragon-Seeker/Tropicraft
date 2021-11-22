package net.tropicraft.core.mixin;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(StructureTemplatePool.Projection.class)
public interface ProjectionFactory {
    @SuppressWarnings("InvokerTarget")
    @Invoker("<init>")
    static StructureTemplatePool.Projection newProjection(String internalName, int internalOrdinal, String id, ImmutableList<StructureProcessor> processors) {
        throw new AssertionError();
    }
}
