package com.cmdgod.mc.ucr_stitch.registrers;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
import com.cmdgod.mc.ucr_stitch.statuseffects.TotemPopperEffect;
import com.cmdgod.mc.ucr_stitch.statuseffects.VoidRepellentEffect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModStatusEffects {
    
    final static public StatusEffect VOID_REPELLENT = register("void_repellent", new VoidRepellentEffect());
    final static public StatusEffect TOTEM_POPPER = register("totem_popper", new TotemPopperEffect());

    static StatusEffect register(String id, StatusEffect effect) {
        return Registry.register(Registry.STATUS_EFFECT, new Identifier(UCRStitch.MOD_NAMESPACE, id), effect);
    }

    public static void registerAll() {
        UCRStitch.LOGGER.info("UCR Stitch: Effects Loaded!");
    }

}
