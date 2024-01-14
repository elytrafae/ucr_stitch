package com.cmdgod.mc.ucr_stitch.registrers;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
import com.cmdgod.mc.ucr_stitch.tools.MediaUtility;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.ModifyStatusEffectAmplifierPower;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
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
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class ModActions {

    private static final int FLUGEL_LIMIT = 1200;
    
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

        register(new ActionFactory<Entity>(new Identifier(UCRStitch.MOD_NAMESPACE, "action_on_trinkets"), new SerializableData()
            .add("item_condition", ApoliDataTypes.ITEM_CONDITION)
            .add("item_action", ApoliDataTypes.ITEM_ACTION),
            (data, entity) -> {
                if (!(entity instanceof LivingEntity)) {
                    return;
                }
                LivingEntity livingEntity = (LivingEntity)entity;
                Optional<TrinketComponent> opt = TrinketsApi.getTrinketComponent(livingEntity);
                if (!opt.isPresent()) {
                    return;
                }
                TrinketComponent component = opt.get();
                ConditionFactory<ItemStack>.Instance itemCondition = ((ConditionFactory<ItemStack>.Instance) data.get("item_condition"));
                ActionFactory<Pair<World, ItemStack>>.Instance itemAction = ((ActionFactory<Pair<World, ItemStack>>.Instance) data.get("item_action"));
                List<Pair<SlotReference, ItemStack>> slots = component.getEquipped((stack) -> {return itemCondition.test(stack);});
                slots.forEach((pair) -> {
                    itemAction.accept(new Pair<>(livingEntity.getWorld(), pair.getRight()));
                });
            }));

        
        registerItemAction(new ActionFactory<Pair<World, ItemStack>>(new Identifier(UCRStitch.MOD_NAMESPACE, "add_tiara_flight"), new SerializableData()
            .add("amount", SerializableDataTypes.INT, 1),
            (data, pair) -> {
                ItemStack stack = pair.getRight();
                NbtCompound nbt = stack.getOrCreateNbt();
                int currentTime = FLUGEL_LIMIT;
                if (nbt.contains("timeLeft", NbtElement.INT_TYPE)) {
                    currentTime = nbt.getInt("timeLeft");
                }
                nbt.putInt("timeLeft", Math.min(Math.max(currentTime + data.getInt("amount"), 0), FLUGEL_LIMIT));
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
