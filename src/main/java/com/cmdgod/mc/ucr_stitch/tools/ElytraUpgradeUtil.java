package com.cmdgod.mc.ucr_stitch.tools;

import java.util.Optional;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
import com.cmdgod.mc.ucr_stitch.recipes.ElytraUpgradeRecipe;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeReference;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import net.minecraft.block.SmithingTableBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ElytraUpgradeUtil {
    
    private static final Identifier ELYTRA_POWER_SOURCE = new Identifier(UCRStitch.MOD_NAMESPACE, "elytra_power");
    private static final String ELYTRA_POWER_NBT_KEY = new Identifier(UCRStitch.MOD_NAMESPACE, "elytra_power").toString();

    public static void revokeAllElytraPowers(LivingEntity entity) {
		PowerUtil.revokeAllPowers(entity, ELYTRA_POWER_SOURCE);
	}

    public static void setUpgradeForElytra(ItemStack stack, ElytraUpgradeRecipe recipe) {
        stack.getOrCreateNbt().putString(ELYTRA_POWER_NBT_KEY, recipe.getId().toString());
        // SmithingScreenHandler;
    }

    public static ElytraUpgradeRecipe getUpgradeForElytra(ItemStack stack, World world) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if (!nbt.contains(ELYTRA_POWER_NBT_KEY)) {
            return null;
        }
        Identifier id = new Identifier(stack.getOrCreateNbt().getString(ELYTRA_POWER_NBT_KEY));
        Optional<? extends Recipe<?>> recipeOpt = world.getRecipeManager().get(id);
        if (!recipeOpt.isPresent()) {
            return null;
        }
        if (!(recipeOpt.get() instanceof ElytraUpgradeRecipe)) {
            return null;
        }
        return (ElytraUpgradeRecipe)recipeOpt.get();
    }

    public static PowerType getPowerFromRecipe(ElytraUpgradeRecipe recipe) {
        if (recipe == null) {
            return null;
        }
        Identifier powerId = recipe.getPowerId();
        if (!PowerTypeRegistry.contains(powerId)) {
            return null;
        }
        return PowerTypeRegistry.get(powerId);
    }

    public static PowerType getPowerForElytra(ItemStack stack, World world) {
        return getPowerFromRecipe(getUpgradeForElytra(stack, world));
    }

    public static void giveEntityElytraPowers(ItemStack stack, LivingEntity entity) {
        PowerType power = getPowerForElytra(stack, entity.getWorld());
        if (power == null) {
            System.out.println("Power could not be found :(");
            return;
        } 
        PowerUtil.grantPower(entity, power, ELYTRA_POWER_SOURCE);
    }

    public enum DescriptionShowType {
        SHOW, SHIFT_SHOW, HIDE
    }

}
