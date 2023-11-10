package com.cmdgod.mc.ucr_stitch.mixin;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.cmdgod.mc.ucr_stitch.recipes.ElytraUpgradeRecipe;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.world.World;

@Mixin(SmithingScreenHandler.class)
public class SmithingScreenHandlerMixin {
    
    //@Inject(at = @At("RETURN"), method = "SmithingScreenHandler(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)Lnet/minecraft/screen/SmithingScreenHandler;", cancellable = false)
    @Inject(at = @At("TAIL"), method = "<init>", cancellable = false)
    //public void SmithingScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, CallbackInfo info) {
    public void SmithingScreenHandler(int syncId, PlayerInventory playerInventory, CallbackInfo info) {
        SmithingScreenHandler smithingScreenHandler = (SmithingScreenHandler)(Object)this;
        List<SmithingRecipe> recipes = ((SmithingScreenHandlerAccessor)(Object)(this)).getRecipes();
        World world = ((SmithingScreenHandlerAccessor)(Object)(this)).getWorld();

        List<ElytraUpgradeRecipe> newRecipes = world.getRecipeManager().listAllOfType(ElytraUpgradeRecipe.Type.INSTANCE);
        //((SmithingScreenHandlerAccessor)(Object)(this)).setRecipes(merge(recipes, newRecipes));
        recipes = merge(recipes, newRecipes);

    }

    /*
    @Inject(at = @At(value = "INVOKE", target="Lnet/minecraft/recipe/RecipeManager;getAllMatches(Lnet/minecraft/recipe/RecipeType;Lnet/minecraft/inventory/Inventory;Lnet/minecraft/world/World;)Ljava/util/List;", shift = At.Shift.BY, by = 2), method = "updateResult()V", cancellable = false, locals = LocalCapture.CAPTURE_FAILHARD)
    public void updateResult(CallbackInfo info, List<SmithingRecipe> list) {
        System.out.println("Update call");
        World world = ((SmithingScreenHandlerAccessor)(Object)(this)).getWorld();
        Inventory input = ((ForgingScreenHandlerAccessor)(Object)(this)).getInput();
        List<ElytraUpgradeRecipe> newRecipes = world.getRecipeManager().getAllMatches(ElytraUpgradeRecipe.Type.INSTANCE, input, world);
        list = merge(list, newRecipes);
    }
    */

    @ModifyVariable(method = "updateResult()V", at = @At("STORE"), ordinal = 0)
    private List<SmithingRecipe> injected(List<SmithingRecipe> list) {
        World world = ((SmithingScreenHandlerAccessor)(Object)(this)).getWorld();
        Inventory input = ((ForgingScreenHandlerAccessor)(Object)(this)).getInput();
        return merge(list, world.getRecipeManager().getAllMatches(ElytraUpgradeRecipe.Type.INSTANCE, input, world));
    }

    private List<SmithingRecipe> merge(List<SmithingRecipe> recipes, List<ElytraUpgradeRecipe> newRecipes) {
        ArrayList<SmithingRecipe> finalRecipes = new ArrayList<>(recipes);
        newRecipes.forEach((newRecipe) -> {
            finalRecipes.add(newRecipe);
        });
        return finalRecipes;
    }

}
