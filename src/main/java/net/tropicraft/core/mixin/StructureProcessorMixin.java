package net.tropicraft.core.mixin;

import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.tropicraft.core.mixinExtensions.StructureProcessorExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(StructureProcessor.class)
public class StructureProcessorMixin implements StructureProcessorExtension {

}
