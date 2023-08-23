package com.cmdgod.mc.ucr_stitch.statuseffects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class VoidRepellentEffect extends StatusEffect {

    public VoidRepellentEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0x1b006e);
    }

    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
    
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        World world = entity.getWorld();
        Vec3d vec = entity.getVelocity();
        if (entity.getPos().y <= world.getBottomY() && vec.y < 0) {
            entity.setVelocity(vec.x, (amplifier+2) * 1.5, vec.z);
        }
    }

}
