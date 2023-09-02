package com.cmdgod.mc.ucr_stitch.items;

import com.cmdgod.mc.ucr_stitch.UCRStitch;

import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionTypes;

public class VoidberryItem extends Item implements VoidBounceEventListener, ThrownGlowingItem {

    public VoidberryItem(Settings settings) {
        super(settings.food(new FoodComponent.Builder().alwaysEdible().hunger(1).saturationModifier(1).snack().build()));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        ItemStack itemStack = super.finishUsing(stack, world, user);
        if (!world.isClient) {
            user.teleport(user.getX(), user.getY() + 0.1, user.getZ(), false);
            user.fallDistance = 0;
            Vec3d direction = user.getRotationVector().normalize().multiply(2);
            user.setVelocity(direction);
            user.velocityModified = true;
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 200, 0));
            if (user instanceof PlayerEntity) {
                ((PlayerEntity)user).getItemCooldownManager().set(this, 20);
            }
        }
        user.playSound(SoundEvents.ENTITY_SHULKER_SHOOT, 1.0f, 1.5f);
        return itemStack;
    }

    @Override
    public void onVoidBounce(ItemEntity entity) {
        World world = entity.getWorld();
        if (world.isClient || !(world instanceof ServerWorld)) {
            return;
        }
        ServerWorld serverWorld = (ServerWorld) world;
        if (!DimensionTypes.THE_END.getValue().equals(world.getDimensionKey().getValue())) {
            return;
        }
        if (world.getRandom().nextInt(22) != 2) {
            return;
        }
        if (UCRStitch.WILD_VOIDBERRY_VINES.generate(serverWorld, serverWorld.getChunkManager().getChunkGenerator(), world.getRandom(), entity.getBlockPos())) {
            world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.BLOCKS, 3f, 0.6f);
            ItemStack stack = entity.getStack();
            if (stack.getCount() <= 1) {
                entity.remove(RemovalReason.DISCARDED);
            } else {
                stack.decrement(1);
            }
        }
    }

    @Override
    public int getGlowColor(ItemStack stack, ItemEntity entity) {
        // If the item has been alive for more than 4 minutes . . .
        if (entity.getItemAge() > 4800) {
            return 0x9B0096;
        }
        // If the item has been alive for more than 3 minutes . . .
        if (entity.getItemAge() > 3600) {
            return 0x6E009E;
        }
        return 0x4D00A0; 
    };
    
}
