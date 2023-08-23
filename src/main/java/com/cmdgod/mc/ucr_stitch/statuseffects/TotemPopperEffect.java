package com.cmdgod.mc.ucr_stitch.statuseffects;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class TotemPopperEffect extends StatusEffect {

    public TotemPopperEffect() {
        super(StatusEffectCategory.HARMFUL, 0x8f3f03);
    }

    @Override
    public boolean isInstant() {
        return true;
    }

    public void applyInstantEffect(@Nullable Entity source, @Nullable Entity attacker, LivingEntity target, int amplifier, double proximity) {
        float health = target.getHealth();
        boolean popped = target.tryUseTotem(DamageSource.ANVIL);
        target.setHealth(health);
        if (popped && attacker != null && attacker instanceof LivingEntity) {
            LivingEntity livingAttacker = (LivingEntity)attacker;
            livingAttacker.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
            livingAttacker.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
            livingAttacker.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0));
        }
    }
    
}
