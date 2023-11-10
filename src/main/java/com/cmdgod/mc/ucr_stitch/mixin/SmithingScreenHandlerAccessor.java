package com.cmdgod.mc.ucr_stitch.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.world.World;

@Mixin(SmithingScreenHandler.class)
public interface SmithingScreenHandlerAccessor {
    
    @Accessor
    List<SmithingRecipe> getRecipes();

    @Accessor
    void setRecipes(List<SmithingRecipe> recipes);

    @Accessor
    World getWorld();

}
