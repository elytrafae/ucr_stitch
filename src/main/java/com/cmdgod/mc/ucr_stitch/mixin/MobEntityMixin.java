package com.cmdgod.mc.ucr_stitch.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.cmdgod.mc.ucr_stitch.powers.NoAIPower;
import com.cmdgod.mc.ucr_stitch.registrers.ModAttributes;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;

@Mixin(MobEntity.class)
public class MobEntityMixin {

    boolean emergency_off = false;
    
    @Inject(at = @At("HEAD"), method = "isAiDisabled()Z", cancellable = true)
    private void isAiDisabled(CallbackInfoReturnable<Boolean> info) {
        MobEntity self = (MobEntity)(Object)this;
        if (emergency_off) {
            emergency_off = false;
            return;
        }
        if (PowerHolderComponent.hasPower(self, NoAIPower.class)) {
            info.setReturnValue(true);
            info.cancel();
        }
    }

    @Inject(at = @At("TAIL"), method = "writeCustomDataToNbt(Lnet/minecraft/nbt/NbtCompound;)V")
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        nbt.putBoolean("NoAI", isAiTrulyDisabled());
    }

    // Checks if the AI is disabled even in data!
    private boolean isAiTrulyDisabled() {
        MobEntity self = (MobEntity)(Object)this;
        emergency_off = true;
        return self.isAiDisabled();
    }

}
