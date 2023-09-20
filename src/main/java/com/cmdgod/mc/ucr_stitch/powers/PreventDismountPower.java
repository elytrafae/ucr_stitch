package com.cmdgod.mc.ucr_stitch.powers;

import com.cmdgod.mc.ucr_stitch.UCRStitch;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class PreventDismountPower extends Power {

    public PreventDismountPower(PowerType<?> type, LivingEntity entity) {
        super(type, entity);
        // IronGolemEntity
    }
    
    public static PowerFactory createFactory() {
        return new PowerFactory<>(new Identifier(UCRStitch.MOD_NAMESPACE, "prevent_dismount"),
            new SerializableData(),
            data ->
                (type, player) -> new PreventDismountPower(type, player))
            .allowCondition();
    }

}
