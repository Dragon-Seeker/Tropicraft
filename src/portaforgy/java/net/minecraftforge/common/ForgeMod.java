package net.minecraftforge.common;

import net.minecraft.core.Registry;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class ForgeMod {

    public static final Attribute SWIM_SPEED = register("swim_speed", new RangedAttribute("forge.swimSpeed", 1.0D, 0.0D, 1024.0D).setSyncable(true));

    private static Attribute register(String string, Attribute attribute) {
        return (Attribute) Registry.register(Registry.ATTRIBUTE, string, attribute);
    }

    public static void init(){

    }
}
