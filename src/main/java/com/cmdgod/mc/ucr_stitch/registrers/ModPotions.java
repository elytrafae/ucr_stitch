package com.cmdgod.mc.ucr_stitch.registrers;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
import com.cmdgod.mc.ucr_stitch.mixin.BrewingRecipeRegistryMixin;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModPotions {

    public static final int REGULAR_DURATION = 3600;
    public static final int LONG_DURATION = 9600;
 
    public static final Potion VOID_REPELLENT = register("void_repellent", new StatusEffectInstance(ModStatusEffects.VOID_REPELLENT, REGULAR_DURATION, 0));
    public static final Potion VOID_REPELLENT_LONG = register("void_repellent_long", new StatusEffectInstance(ModStatusEffects.VOID_REPELLENT, LONG_DURATION, 0));
    public static final Potion VOID_REPELLENT_STRONG = register("void_repellent_strong", new StatusEffectInstance(ModStatusEffects.VOID_REPELLENT, REGULAR_DURATION, 1));

    public static final Potion TOTEM_POPPER = register("totem_popper", new StatusEffectInstance(ModStatusEffects.TOTEM_POPPER, 1, 0));
 
    public static void registerPotions(){
 
    }

    public static Potion register(String id, StatusEffectInstance instance) {
        return Registry.register(Registry.POTION, new Identifier(UCRStitch.MOD_NAMESPACE, id), new Potion(instance));
    }
 
    public static void registerPotionsRecipes() {
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(Potions.AWKWARD, ModItems.VOIDBERRY, VOID_REPELLENT);
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(VOID_REPELLENT, Items.REDSTONE, VOID_REPELLENT_LONG);
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(VOID_REPELLENT, Items.GLOWSTONE_DUST, VOID_REPELLENT_STRONG);
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(VOID_REPELLENT_STRONG, Items.REDSTONE, VOID_REPELLENT_LONG);
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(VOID_REPELLENT_LONG, Items.GLOWSTONE_DUST, VOID_REPELLENT_STRONG);

        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(VOID_REPELLENT, Items.FERMENTED_SPIDER_EYE, TOTEM_POPPER);
    }
}