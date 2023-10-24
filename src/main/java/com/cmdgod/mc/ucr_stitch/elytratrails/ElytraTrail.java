package com.cmdgod.mc.ucr_stitch.elytratrails;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public interface ElytraTrail {
    
    default double getFrequency() {
        return 0.2;
    }

    void renderParticle(World world, ClientPlayerEntity player, Vec3d particlePos);

    void renderBoostParticle(World world, ClientPlayerEntity player, Vec3d particlePos);

}
