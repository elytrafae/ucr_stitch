package com.cmdgod.mc.ucr_stitch.elytratrails;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DustColorTransitionParticleEffect;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class RedElytraTrail implements ElytraTrail {
    
    @Override
    public void renderParticle(World world, ClientPlayerEntity player, Vec3d particlePos) {
        world.addParticle(new DustParticleEffect(new Vec3f(1, 0, 0), 1f), false, particlePos.getX(), particlePos.getY(), particlePos.getZ(), 0, 0, 0);
    }

    @Override
    public void renderBoostParticle(World world, ClientPlayerEntity player, Vec3d particlePos) {
        //Vec3d testPos = particlePos.relativize(particlePos);
        player.sendMessage(Text.of("Firework used!"));
        for (int i=0; i < 360; i+=10) {
            world.addParticle(new DustParticleEffect(new Vec3f(0.9f, 0, 0), 5f), false, particlePos.getX(), particlePos.getY(), particlePos.getZ(), Math.sin(Math.toRadians(i))*5, Math.cos(Math.toRadians(i))*5, 1);
        }
    }

}
