package com.cmdgod.mc.ucr_stitch.elytratrails;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public interface ElytraTrail {
    
    default double getFrequency() {
        return 0.2;
    }

    void renderParticle(World world, ClientPlayerEntity player, Vec3d particlePos);

    void renderBoostParticle(World world, ClientPlayerEntity player, Vec3d particlePos);

}
