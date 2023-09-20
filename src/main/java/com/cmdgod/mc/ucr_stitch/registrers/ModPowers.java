package com.cmdgod.mc.ucr_stitch.registrers;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
import com.cmdgod.mc.ucr_stitch.powers.ActionBeforeItemUsePower;
import com.cmdgod.mc.ucr_stitch.powers.ForceEdgeClipPower;
import com.cmdgod.mc.ucr_stitch.powers.IronGolemAggroPower;
import com.cmdgod.mc.ucr_stitch.powers.NoAIPower;
import com.cmdgod.mc.ucr_stitch.powers.PreventDismountPower;
import com.cmdgod.mc.ucr_stitch.powers.VillagerScarePower;

import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.util.registry.Registry;

public class ModPowers {
    
    public static PowerFactory VILLAGER_SCARE_POWER = register(VillagerScarePower.createFactory());
    public static PowerFactory FORCE_EDGE_CLIP_POWER = register(ForceEdgeClipPower.createFactory());
    public static PowerFactory IRON_GOLEM_AGGRO_POWER = register(IronGolemAggroPower.createFactory());
    public static PowerFactory ACTION_BEFORE_ITEM_USE_POWER = register(ActionBeforeItemUsePower.createFactory());
    public static PowerFactory PREVENT_DISMOUNT_POWER = register(PreventDismountPower.createFactory());
    public static PowerFactory NO_AI_POWER = register(NoAIPower.createFactory());

    private static PowerFactory register(PowerFactory<?> powerFactory) {
        Registry.register(ApoliRegistries.POWER_FACTORY, powerFactory.getSerializerId(), powerFactory);
        return powerFactory;
    }

    public static void registerAll() {
        UCRStitch.LOGGER.info("UCR Stitch: Powers Registered!");
    }


}
