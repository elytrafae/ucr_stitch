package com.cmdgod.mc.ucr_stitch.powers;

import com.cmdgod.mc.ucr_stitch.UCRStitch;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.ElytraFlightPower;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.power.factory.action.ActionType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class VillagerScarePower extends Power {

    float distance;

    public VillagerScarePower(PowerType<?> type, LivingEntity entity, float squaredDistance) {
        super(type, entity);
        this.distance = squaredDistance;
    }

    public float getDistance() {
        return distance;
    }
    
    public static PowerFactory createFactory() {
        return new PowerFactory<>(new Identifier(UCRStitch.MOD_NAMESPACE, "scare_villagers"),
            new SerializableData()
                .add("distance", SerializableDataTypes.FLOAT, 8f),
            data ->
                (type, player) -> new VillagerScarePower(type, player, data.getFloat("distance")))
            .allowCondition();
    }

}
