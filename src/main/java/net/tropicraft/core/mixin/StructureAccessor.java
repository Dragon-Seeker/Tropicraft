package net.tropicraft.core.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Optional;

@Mixin(StructureTemplate.class)
public interface StructureAccessor {
    @Invoker("createEntityIgnoreException")
    static Optional<Entity> getEntity(ServerLevelAccessor world, CompoundTag nbt) {
        throw new AssertionError();
    }
}
