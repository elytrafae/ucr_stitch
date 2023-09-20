package com.cmdgod.mc.ucr_stitch.registrers;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
import com.cmdgod.mc.ucr_stitch.tools.MediaUtility;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.ModifyStatusEffectAmplifierPower;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.apoli.util.modifier.Modifier;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class ModActions {
    
    public static void registerAll() {
        //EntityActions;
        register(new ActionFactory<>(new Identifier(UCRStitch.MOD_NAMESPACE, "tp_to_respawn_point"), new SerializableData()
            .add("success_action", ApoliDataTypes.ENTITY_ACTION, null)
            .add("fail_action", ApoliDataTypes.ENTITY_ACTION, null)
            .add("incorrect_type_action", ApoliDataTypes.ENTITY_ACTION, null),
            (data, entity) -> {
                if (!(entity instanceof ServerPlayerEntity)) {
                    if(data.isPresent("incorrect_type_action")) {
                        ((ActionFactory<Entity>.Instance)data.get("incorrect_type_action")).accept(entity);
                    }
                    return;
                }
                ServerPlayerEntity player = (ServerPlayerEntity)entity;
                BlockPos blockPos = player.getSpawnPointPosition();
                float f = player.getSpawnAngle();
                ServerWorld serverWorld = player.server.getWorld(player.getSpawnPointDimension());
                Optional<Vec3d> optional = serverWorld != null && blockPos != null ? PlayerEntity.findRespawnPosition(serverWorld, blockPos, f, true, true) : Optional.empty();
                if (!optional.isPresent()) {
                    if(data.isPresent("fail_action")) {
                        ((ActionFactory<Entity>.Instance)data.get("fail_action")).accept(entity);
                    }
                    return;
                }
                Vec3d spawnPos = optional.get();
                player.teleport(serverWorld, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), player.getYaw(), player.getPitch());
                if(data.isPresent("success_action")) {
                    ((ActionFactory<Entity>.Instance)data.get("success_action")).accept(entity);
                }
            }));

        register(new ActionFactory<>(new Identifier(UCRStitch.MOD_NAMESPACE, "apply_random_effect"), new SerializableData()
            .add("status_effects", SerializableDataTypes.STRINGS, null)
            .add("duration", SerializableDataTypes.INT, 0)
            .add("amplifier", SerializableDataTypes.INT, 0),
            (data, entity) -> {
                if (!(entity instanceof LivingEntity)) {
                    return;
                }
                if (entity.getWorld().isClient) {
                    return;
                }
                List<StatusEffect> statusEffects = new LinkedList<>();
                LivingEntity le = (LivingEntity)entity;
                List<String> stringList = data.get("status_effects");
                for (String id : stringList) {
                    if (Registry.STATUS_EFFECT.containsId(new Identifier(id))) {
                        statusEffects.add(Registry.STATUS_EFFECT.get(new Identifier(id)));
                    }
                }
                StatusEffect effect = statusEffects.get(le.getRandom().nextBetweenExclusive(0, statusEffects.size()));
                le.addStatusEffect(new StatusEffectInstance(effect, data.getInt("duration"), data.getInt("amplifier")));
                //ModifyStatusEffectAmplifierPower
            }));

        register(new ActionFactory<>(new Identifier(UCRStitch.MOD_NAMESPACE, "consume_media"), new SerializableData()
            .add("amount", SerializableDataTypes.INT),
            (data, entity) -> {
                if (!(entity instanceof ServerPlayerEntity)) {
                    return;
                }
                ServerPlayerEntity player = (ServerPlayerEntity)entity;
                MediaUtility.consumeMediaFromInventory(player.getInventory(), data.getInt("amount"));
            }));

        registerItemAction(new ActionFactory<Pair<World, ItemStack>>(new Identifier(UCRStitch.MOD_NAMESPACE, "increment_ignoremax"), new SerializableData()
            .add("amount", SerializableDataTypes.INT, 1),
            (data, pair) -> {
                pair.getRight().increment(data.getInt("amount"));
            }));

        UCRStitch.LOGGER.info("UCR Stitch: Entity Actions Registered!");
    }
    

    private static void register(ActionFactory<Entity> actionFactory) {
        Registry.register(ApoliRegistries.ENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }

    private static void registerItemAction(ActionFactory<Pair<World, ItemStack>> actionFactory) {
        Registry.register(ApoliRegistries.ITEM_ACTION, actionFactory.getSerializerId(), actionFactory);
    }

}
