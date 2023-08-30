package com.cmdgod.mc.ucr_stitch.registrers;

import com.cmdgod.mc.ucr_stitch.UCRStitch;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModAttributes {
    
    public static final EntityAttribute GENERIC_BONUS_FISH_CHANCE = register("generic.bonus_fish_chance", new ClampedEntityAttribute("attribute.name.ucr_stitch.generic.bonus_fish_chance", 1, 0, 1024.0).setTracked(true));

    private static EntityAttribute register(String id, EntityAttribute attribute) {
        return Registry.register(Registry.ATTRIBUTE, new Identifier(UCRStitch.MOD_NAMESPACE, id), attribute);
    }

}
