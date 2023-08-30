package com.cmdgod.mc.ucr_stitch.registrers;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
import com.cmdgod.mc.ucr_stitch.powers.VillagerScarePower;

import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.util.registry.Registry;

public class ModPowers {
    
    public static PowerFactory VILLAGER_SCARE_POWER = register(VillagerScarePower.createFactory());

    private static PowerFactory register(PowerFactory<?> powerFactory) {
        Registry.register(ApoliRegistries.POWER_FACTORY, powerFactory.getSerializerId(), powerFactory);
        return powerFactory;
    }

    public static void registerAll() {
        UCRStitch.LOGGER.info("UCR Stitch: Powers Registered!");
    }


}
