package com.cmdgod.mc.ucr_stitch.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.cmdgod.mc.ucr_stitch.registrers.ModTags;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;

@Mixin(FishingBobberEntity.class)
public class FishingBobberEntityMixin {
    
    @Inject(at = @At("HEAD"), method = "removeIfInvalid(Lnet/minecraft/entity/player/PlayerEntity;)Z", cancellable = true)
    private void removeIfInvalid(PlayerEntity player, CallbackInfoReturnable<Boolean> info) {
        FishingBobberEntity bobber = (FishingBobberEntity)(Object)this;
        ItemStack itemStack = player.getMainHandStack();
        ItemStack itemStack2 = player.getOffHandStack();
        boolean bl = itemStack.isIn(ModTags.Items.FISHING_RODS);
        boolean bl2 = itemStack2.isIn(ModTags.Items.FISHING_RODS);
        if (player.isRemoved() || !player.isAlive() || !bl && !bl2 || bobber.squaredDistanceTo(player) > 1024.0) {
            return;
        }
        info.setReturnValue(false);
        info.cancel();
    }

}
