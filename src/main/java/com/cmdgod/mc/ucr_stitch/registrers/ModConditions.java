package com.cmdgod.mc.ucr_stitch.registrers;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
import com.cmdgod.mc.ucr_stitch.tools.MediaUtility;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModConditions {
    
    public static void registerAll() {

        register(new ConditionFactory<Entity>(new Identifier(UCRStitch.MOD_NAMESPACE, "media"), new SerializableData()
            .add("comparison", ApoliDataTypes.COMPARISON)
            .add("compare_to", SerializableDataTypes.INT),
            (data, entity) -> {
                if (!(entity instanceof ServerPlayerEntity)) {
                    return false;
                }
                ServerPlayerEntity player = (ServerPlayerEntity)entity;
                int media = MediaUtility.getMediaInInventory(player.getInventory());
                Comparison comparison = data.get("comparison");
                return comparison.compare(media, data.getInt("compare_to"));
            }));

        register(new ConditionFactory<Entity>(new Identifier(UCRStitch.MOD_NAMESPACE, "velocity"), new SerializableData()
            .add("comparison", ApoliDataTypes.COMPARISON)
            .add("compare_to", SerializableDataTypes.FLOAT),
            (data, entity) -> {
                Comparison comparison = data.get("comparison");
                return comparison.compare(entity.getVelocity().length(), data.getFloat("compare_to"));
            }));

    }

    private static void register(ConditionFactory<Entity> conditionFactory) {
        Registry.register(ApoliRegistries.ENTITY_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }

}
