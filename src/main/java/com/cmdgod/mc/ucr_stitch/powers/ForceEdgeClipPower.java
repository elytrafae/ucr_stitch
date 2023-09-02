package com.cmdgod.mc.ucr_stitch.powers;

import com.cmdgod.mc.ucr_stitch.UCRStitch;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class ForceEdgeClipPower extends Power {

    boolean on;

    public ForceEdgeClipPower(PowerType<?> type, LivingEntity entity, boolean on) {
        super(type, entity);
        this.on = on;
    }

    public boolean isClippingOn() {
        return this.on;
    }
    
    public static PowerFactory createFactory() {
        return new PowerFactory<>(new Identifier(UCRStitch.MOD_NAMESPACE, "force_edge_clip"),
            new SerializableData()
                .add("on", SerializableDataTypes.BOOLEAN, true),
            data ->
                (type, player) -> new ForceEdgeClipPower(type, player, data.getBoolean("on")))
            .allowCondition();
    }

}
