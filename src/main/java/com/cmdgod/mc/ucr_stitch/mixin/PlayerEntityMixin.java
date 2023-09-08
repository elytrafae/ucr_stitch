package com.cmdgod.mc.ucr_stitch.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.cmdgod.mc.ucr_stitch.blocks.CustomBlockEventListener;
import com.cmdgod.mc.ucr_stitch.elytratrails.ElytraTrail;
import com.cmdgod.mc.ucr_stitch.elytratrails.RedElytraTrail;
import com.cmdgod.mc.ucr_stitch.mixininterfaces.IPlayerEntityMixin;
import com.cmdgod.mc.ucr_stitch.powers.ForceEdgeClipPower;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.wispforest.owo.particles.systems.ParticleSystem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DustColorTransitionParticleEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements IPlayerEntityMixin {

    private int sneakTick = 0;
    
    @Inject(at = @At("HEAD"), method = "tick()V")
    public void tick(CallbackInfo info) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        if (player.isSneaky()) {
            sneakTick++;
        } else {
            sneakTick = 0;
        }

        if (isFirstTickSneak() && player.isOnGround()) {
            World world = player.getWorld();
            BlockPos pos = player.getLandingPos();
            BlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            if (block instanceof CustomBlockEventListener) {
                ((CustomBlockEventListener)block).onFirstSneakTick(world, pos, state, player);
            }
        }

        /*
        if (player.isFallFlying() && player instanceof ClientPlayerEntity) {
            //ElytraEntityModel;
            // FireworkRocketItem;
            // PlayerEntityRenderer;
            ClientPlayerEntity clinetPlayer = (ClientPlayerEntity)player;
            ElytraTrail trail = new RedElytraTrail();
            World world = player.getWorld();
            Vec3d velocity = player.getVelocity();
            Vec3d position = player.getPos();
            Vec3d offset = velocity.normalize().multiply(trail.getFrequency());
            int i=0;
            
            while (i==0 || offset.multiply(i).lengthSquared() < velocity.lengthSquared()) {
                Vec3d particlePos = position.subtract(offset.multiply(i));
                trail.renderParticle(world, clinetPlayer, particlePos);
                i++;
            }
        }
        */
    }

    @Inject( at = @At("RETURN"), method = "clipAtLedge()Z", cancellable = true)
    protected void clipAtLedge(CallbackInfoReturnable<Boolean> info) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        List<ForceEdgeClipPower> powers = PowerHolderComponent.getPowers(player, ForceEdgeClipPower.class);
        if (powers.size() <= 0) {
            // If there are no powers like these, don't do anything.
            return;
        }
        info.setReturnValue(powers.get(0).isClippingOn());
    }


    public boolean isFirstTickSneak() {
        return sneakTick == 1;
    }

}
