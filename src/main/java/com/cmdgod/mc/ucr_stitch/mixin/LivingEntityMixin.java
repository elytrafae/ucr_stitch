package com.cmdgod.mc.ucr_stitch.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.cmdgod.mc.ucr_stitch.registrers.ModStatusEffects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    
    @Inject(at = @At("HEAD"), method = "modifyAppliedDamage(Lnet/minecraft/entity/damage/DamageSource;F)F", cancellable = true)
	private void modifyAppliedDamage(DamageSource source, float damage, CallbackInfoReturnable<Float> info) {
        LivingEntity entity = (LivingEntity)(Object)this;
        if (source.isOutOfWorld() && entity.hasStatusEffect(ModStatusEffects.VOID_REPELLENT)) {
            info.setReturnValue(0f);
            info.cancel();
        }
	}

}
