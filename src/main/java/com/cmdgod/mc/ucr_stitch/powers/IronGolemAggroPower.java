package com.cmdgod.mc.ucr_stitch.powers;

import com.cmdgod.mc.ucr_stitch.UCRStitch;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.IronGolemLookGoal;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.util.Identifier;

public class IronGolemAggroPower extends Power {

    boolean affectNatural;
    boolean affectArtificial;

    public IronGolemAggroPower(PowerType<?> type, LivingEntity entity, boolean affectNatural, boolean affectArtificial) {
        super(type, entity);
        this.affectNatural = affectNatural;
        this.affectArtificial = affectArtificial;
    }

    public boolean doesAffectNatural() {
        return affectNatural;
    }

    public boolean doesAffectArtificial() {
        return affectArtificial;
    }
    
    // Later, a bientity condition could bbe added . . .
    public static PowerFactory createFactory() {
        return new PowerFactory<>(new Identifier(UCRStitch.MOD_NAMESPACE, "aggro_iron_golems"),
            new SerializableData()
                .add("natural", SerializableDataTypes.BOOLEAN, true)
                .add("artificial", SerializableDataTypes.BOOLEAN, false),
            data ->
                (type, player) -> new IronGolemAggroPower(type, player, data.getBoolean("natural"), data.getBoolean("artificial")))
            //.allowCondition();
            ;
    }

}
