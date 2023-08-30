package com.cmdgod.mc.ucr_stitch.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.cmdgod.mc.ucr_stitch.powers.VillagerScarePower;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.sensor.VillagerHostilesSensor;

@Mixin(VillagerHostilesSensor.class)
public class VillagerHostilesSensorMixin {
    
    @Inject(at = @At("HEAD"), method = "isHostile(Lnet/minecraft/entity/LivingEntity;)Z", cancellable = true)
    private void isHostile(LivingEntity entity, CallbackInfoReturnable<Boolean> info) {
        if (PowerHolderComponent.hasPower(entity, VillagerScarePower.class)) {
            info.setReturnValue(true);
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "isCloseEnoughForDanger(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/LivingEntity;)Z", cancellable = true)
    private void isCloseEnoughForDanger(LivingEntity villager, LivingEntity target, CallbackInfoReturnable<Boolean> info) {
        if (PowerHolderComponent.hasPower(target, VillagerScarePower.class)) {
            float f = getSmallestDistance(target);
            info.setReturnValue(target.squaredDistanceTo(villager) <= (double)(f * f));
            info.cancel();   
        }
    }

    private float getSmallestDistance(LivingEntity target) {
        List<VillagerScarePower> powers = PowerHolderComponent.getPowers(target, VillagerScarePower.class);
        float f = Float.MAX_VALUE;
        for (VillagerScarePower power : powers) {
            float f2 = power.getDistance();
            if (f2 < f) {
                f = f2;
            }
        }
        return f;
    }

}
