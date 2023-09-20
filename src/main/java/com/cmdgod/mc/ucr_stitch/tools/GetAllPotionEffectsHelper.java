package com.cmdgod.mc.ucr_stitch.tools;

import java.util.ArrayList;
import java.util.function.Predicate;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class GetAllPotionEffectsHelper {
    
    public static ArrayList<Identifier> getAllPotionEffects(Predicate<StatusEffect> pred) {
        ArrayList<Identifier> list = new ArrayList<>();

        Registry.STATUS_EFFECT.getKeys().forEach((registryKey) -> {
            Identifier id = registryKey.getValue();
            StatusEffect effect = Registry.STATUS_EFFECT.get(id);
            if (pred.test(effect)) {
                list.add(id);
            }
        });

        return list;
    }

}
