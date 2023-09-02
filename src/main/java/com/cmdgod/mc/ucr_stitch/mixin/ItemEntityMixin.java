package com.cmdgod.mc.ucr_stitch.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.cmdgod.mc.ucr_stitch.items.ThrownGlowingItem;
import com.cmdgod.mc.ucr_stitch.items.VoidBounceEventListener;
import com.cmdgod.mc.ucr_stitch.registrers.ModTags;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    
    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
        throw new UnsupportedOperationException("Unimplemented constructor 'ItemEntityMixin'");
    }

    @Inject(at = @At("RETURN"), method = "tick()V")
    private void tick(CallbackInfo info) {
        ItemEntity ie = (ItemEntity)(Object)this;
        World world = ie.getWorld();
        Vec3d vel = ie.getVelocity();
        if (ie.getStack().isIn(ModTags.Items.VOID_BOUNCING) && ie.getY() <= world.getBottomY() && vel.y < 0) {
            ie.setVelocity(new Vec3d(vel.x, 1.2, vel.z));
            Item item = ie.getStack().getItem();
            if (item instanceof VoidBounceEventListener) {
                ((VoidBounceEventListener)item).onVoidBounce(ie);
                if (ie.isRemoved() || !ie.isAlive()) {
                    return;
                }
            }
        }
        if (ie.isTouchingWater() && ie.getStack().isIn(ModTags.Items.WATER_VULNERABLE)) {
            world.playSound(null, ie.getX(), ie.getY(), ie.getZ(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 1f, 1.5f);
            ie.damage(DamageSource.DROWN, 100);
            return;
        }
    }

    public boolean isInvulnerableTo(DamageSource damageSource) {
        ItemEntity ie = (ItemEntity)(Object)this;
        if (damageSource.isOutOfWorld() && ie.getStack().isIn(ModTags.Items.VOID_BOUNCING)) {
            return true;
        }
        return super.isInvulnerableTo(damageSource);
    }

    @Override
    public boolean isGlowing() {
        ItemEntity ie = (ItemEntity)(Object)this;
        ItemStack stack = ie.getStack();
        Item item = stack.getItem();
        if (item instanceof ThrownGlowingItem) {
            int glowColor = ((ThrownGlowingItem)item).getGlowColor(stack, ie);
            return glowColor > -1;
        }
        return super.isGlowing();
    }

    @Override
    public int getTeamColorValue() {
        int color = super.getTeamColorValue();
        if (color != 0xFFFFFF) {
            return color;
        }
        ItemEntity ie = (ItemEntity)(Object)this;
        ItemStack stack = ie.getStack();
        Item item = stack.getItem();
        if (!(item instanceof ThrownGlowingItem)) {
            return 0xFFFFFF;
        }
        return ((ThrownGlowingItem)item).getGlowColor(stack, ie);
    }

}
