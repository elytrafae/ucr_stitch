package com.cmdgod.mc.ucr_stitch.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class VoidberryItem extends Item {

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
            if (user instanceof PlayerEntity) {
                ((PlayerEntity)user).getItemCooldownManager().set(this, 20);
            }
        }
        return itemStack;
    }
    
}
