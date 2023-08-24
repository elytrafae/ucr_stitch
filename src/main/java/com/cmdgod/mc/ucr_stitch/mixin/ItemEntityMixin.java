package com.cmdgod.mc.ucr_stitch.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
import com.cmdgod.mc.ucr_stitch.registrers.ModTags;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    
    @Inject(at = @At("RETURN"), method = "tick()V")
    private void tick(CallbackInfo info) {
        ItemEntity ie = (ItemEntity)(Object)this;
        World world = ie.getWorld();
        if (ie.isTouchingWater() && ie.getStack().isIn(ModTags.Items.WATER_VULNERABLE)) {
            world.playSound(null, ie.getX(), ie.getY(), ie.getZ(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 1f, 1.5f);
            ie.damage(DamageSource.DROWN, 100); 
        }
    }

}
