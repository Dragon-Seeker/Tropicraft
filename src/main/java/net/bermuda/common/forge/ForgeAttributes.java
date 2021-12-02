package net.bermuda.common.forge;

import net.minecraft.core.Registry;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class ForgeAttributes {

    public static final Attribute SWIM_SPEED = register("swim_speed", new RangedAttribute("forge.swimSpeed", 1.0D, 0.0D, 1024.0D).setSyncable(true));
    public static final Attribute ENTITY_GRAVITY = register("entity_gravity", new RangedAttribute("forge.entity_gravity", 0.08D, -8.0D, 8.0D).setSyncable(true));


    private static Attribute register(String string, Attribute attribute) {
        return (Attribute) Registry.register(Registry.ATTRIBUTE, string, attribute);
    }

    public static void init(){

    }
}
