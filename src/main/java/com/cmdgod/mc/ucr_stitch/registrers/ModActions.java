package com.cmdgod.mc.ucr_stitch.registrers;

import java.util.Optional;

import com.cmdgod.mc.ucr_stitch.UCRStitch;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;

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

        UCRStitch.LOGGER.info("UCR Stitch: Entity Actions Registered!");
    }
    

    private static void register(ActionFactory<Entity> actionFactory) {
        Registry.register(ApoliRegistries.ENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }

}
