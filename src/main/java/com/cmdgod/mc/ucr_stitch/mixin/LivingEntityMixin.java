package com.cmdgod.mc.ucr_stitch.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.cmdgod.mc.ucr_stitch.blocks.CustomBlockEventListener;
import com.cmdgod.mc.ucr_stitch.registrers.ModAttributes;
import com.cmdgod.mc.ucr_stitch.registrers.ModStatusEffects;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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

    @Inject(at = @At("RETURN"), method = "createLivingAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;")
    private static void createLivingAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> info) {
        info.getReturnValue().add(ModAttributes.GENERIC_BONUS_FISH_CHANCE);
    }

    @Inject(at = @At("HEAD"), method = "jump()V", cancellable = true) 
    private void jump(CallbackInfo info) {
        LivingEntity entity = (LivingEntity)(Object)this;
        World world = entity.getWorld();
        BlockPos pos = entity.getLandingPos();
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block instanceof CustomBlockEventListener) {
            boolean cancel = ((CustomBlockEventListener)block).onFirstJumpTick(world, pos, state, (LivingEntity)(Object)this);
            if (cancel) {
                info.cancel();
            }
        }
    }

}
