package com.cmdgod.mc.ucr_stitch.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.cmdgod.mc.ucr_stitch.registrers.ModItems;

import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.random.Random;

@Mixin(DispenserBlockEntity.class)
public class DispenserMixin  {
    
    @Inject(at = @At("HEAD"), method = "chooseNonEmptySlot(Lnet/minecraft/util/math/random/Random;)I", cancellable = true)
	private void init(Random random, CallbackInfoReturnable<Integer> info) {
        DispenserBlockEntity be = (DispenserBlockEntity)(Object)this;
		int i = -1;
        int j = 1;
        for (int k = 0; k < DispenserBlockEntity.INVENTORY_SIZE; ++k) {
            ItemStack s = be.getStack(k);
            if (s.isEmpty() || s.isOf(ModItems.HEAVY_BOULDER) || random.nextInt(j++) != 0) continue;
            i = k;
        }
        info.setReturnValue(i);
        info.cancel();
	}

}
