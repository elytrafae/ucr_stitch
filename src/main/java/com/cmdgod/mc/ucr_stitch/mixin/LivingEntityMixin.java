package com.cmdgod.mc.ucr_stitch.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
import com.cmdgod.mc.ucr_stitch.blocks.CustomBlockEventListener;
import com.cmdgod.mc.ucr_stitch.powers.PreventDismountPower;
import com.cmdgod.mc.ucr_stitch.registrers.ModAttributes;
import com.cmdgod.mc.ucr_stitch.registrers.ModStatusEffects;
import com.cmdgod.mc.ucr_stitch.tools.ElytraUpgradeUtil;
import com.cmdgod.mc.ucr_stitch.tools.PowerUtil;

import io.github.apace100.apoli.command.PowerCommand;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    
    @Inject(at = @At("HEAD"), method = "modifyAppliedDamage(Lnet/minecraft/entity/damage/DamageSource;F)F", cancellable = true)
	private void modifyAppliedDamage(DamageSource source, float damage, CallbackInfoReturnable<Float> info) {
        LivingEntity entity = (LivingEntity)(Object)this;
        if (source.isOutOfWorld() && entity.hasStatusEffect(ModStatusEffects.VOID_REPELLENT)) {
            info.setReturnValue(0f);
            info.cancel();
        }
        // MobEntity;
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

    

    @Inject(at = @At("HEAD"), method = "onEquipStack(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)V", cancellable = false) 
    public void onEquipStack(EquipmentSlot slot, ItemStack oldStack, ItemStack newStack, CallbackInfo info) {
        LivingEntity entity = (LivingEntity)(Object)this;
        boolean bl = newStack.isEmpty() && oldStack.isEmpty();
        if (!bl && !ItemStack.areItemsEqual(oldStack, newStack)) {
            if ((!oldStack.isEmpty()) && oldStack.getItem() instanceof ElytraItem) {
                ElytraUpgradeUtil.revokeAllElytraPowers(entity);
            }
            if ((!newStack.isEmpty()) && newStack.getItem() instanceof ElytraItem) {
                ElytraUpgradeUtil.giveEntityElytraPowers(newStack, entity);
            }
        }
    }

    

}
