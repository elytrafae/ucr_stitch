package com.cmdgod.mc.ucr_stitch.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.cmdgod.mc.ucr_stitch.powers.NoAIPower;
import com.cmdgod.mc.ucr_stitch.registrers.ModAttributes;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.mob.MobEntity;

@Mixin(MobEntity.class)
public class MobEntityMixin {
    
    @Inject(at = @At("HEAD"), method = "isAiDisabled()Z", cancellable = true)
    private void isAiDisabled(CallbackInfoReturnable<Boolean> info) {
        MobEntity self = (MobEntity)(Object)this;
        if (PowerHolderComponent.hasPower(self, NoAIPower.class)) {
            info.setReturnValue(true);
            info.cancel();
        }
    }

}
