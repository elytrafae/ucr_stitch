package com.cmdgod.mc.ucr_stitch.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.enchantment.ProtectionEnchantment.Type;
import net.minecraft.entity.EquipmentSlot;

@Mixin(ProtectionEnchantment.class)
public abstract class ProtectionEnchantmentMixin extends Enchantment {

    protected ProtectionEnchantmentMixin(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
    }

    @Inject(at = @At("HEAD"), method = "canAccept(Lnet/minecraft/enchantment/Enchantment;)Z", cancellable = true)
    public void canAccept(Enchantment other, CallbackInfoReturnable<Boolean> info) {
        ProtectionEnchantment ench = (ProtectionEnchantment)(Object)this;
        if (other instanceof ProtectionEnchantment) {
            ProtectionEnchantment protectionEnchantment = (ProtectionEnchantment)other;
            if (ench.protectionType == protectionEnchantment.protectionType) {
                info.setReturnValue(false);
                info.cancel();
                return;
            }
            boolean compatibile = ench.protectionType == Type.FALL || protectionEnchantment.protectionType == Type.FALL 
                || ench.protectionType == Type.ALL || protectionEnchantment.protectionType == Type.ALL;
            info.setReturnValue(compatibile);
            info.cancel();
            return;
        }
        info.setReturnValue(super.canAccept(other));
        info.cancel();
    }
    
}
