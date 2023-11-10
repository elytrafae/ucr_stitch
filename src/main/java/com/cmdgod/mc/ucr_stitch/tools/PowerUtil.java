package com.cmdgod.mc.ucr_stitch.tools;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class PowerUtil {
    
    // This is from Apoli's PowerCommand class
    public static boolean grantPower(LivingEntity entity, PowerType<?> power, Identifier source) {
		PowerHolderComponent component = PowerHolderComponent.KEY.get(entity);
		boolean success = component.addPower(power, source);
		if(success) {
			component.sync();
			return true;
		}
		return false;
	}

    // This, is not
    public static void revokeAllPowers(LivingEntity entity, Identifier source) {
		PowerHolderComponent component = PowerHolderComponent.KEY.get(entity);
        int i = component.removeAllPowersFromSource(source); // IDK what the return value is pls help
        component.sync();
	}

}
