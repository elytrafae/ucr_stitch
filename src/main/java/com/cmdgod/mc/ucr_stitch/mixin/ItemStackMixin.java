package com.cmdgod.mc.ucr_stitch.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.cmdgod.mc.ucr_stitch.powers.ActionBeforeItemUsePower;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.ActionOnItemUsePower;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.item.ThrowablePotionItem;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    
    @Inject(method = "finishUsing", at = @At("HEAD"))
    public void applyBeforeUse(World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack self = (ItemStack)(Object)this;
        if(user instanceof PlayerEntity) {
            // ItemStack returnStack = cir.getReturnValue();
            PowerHolderComponent component = PowerHolderComponent.KEY.get(user);
            for(ActionBeforeItemUsePower p : component.getPowers(ActionBeforeItemUsePower.class)) {
                if((!p.isInstant()) && p.doesApply(self)) {
                    p.executeActions(new Pair<World, ItemStack>(world, self));
                    // PotionItem;
                    // ThrowablePotionItem;
                }
            }
        }
    }

    @Inject(method = "use", at = @At("HEAD"))
    public void applyBeforeUseInstant(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack self = (ItemStack)(Object)this;
        // ItemStack returnStack = cir.getReturnValue();
        PowerHolderComponent component = PowerHolderComponent.KEY.get(user);
        for(ActionBeforeItemUsePower p : component.getPowers(ActionBeforeItemUsePower.class)) {
            if(p.isInstant() && p.doesApply(self)) {
                p.executeActions(new Pair<World, ItemStack>(world, self));
            }
        }
    }

}
