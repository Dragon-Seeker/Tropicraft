package net.tropicraft.core.mixin;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.LevelStem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(LevelStem.class)
public interface DimensionsOptionsAccessor {
    @Accessor("BUILTIN_ORDER")
    public static Set<ResourceKey<LevelStem>> getBuiltInOrder(){
        throw new AssertionError();
    }

    @Accessor("BUILTIN_ORDER")
    @Mutable @Final
    public static void setBuiltInOrder(Set<ResourceKey<LevelStem>> keys){
        throw new AssertionError();
    }


}
