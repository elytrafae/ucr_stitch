package com.cmdgod.mc.ucr_stitch.recipes;

import com.google.gson.JsonObject;

import net.minecraft.block.GravelBlock;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class GravityDuperRecipe implements Recipe<SimpleInventory> {

    private ItemStack output;
    private int time;

    private Identifier id;
    private void setId(Identifier id) {
        this.id = id;
    }

    public GravityDuperRecipe(ItemStack output, int time, Identifier id) {
        setOutput(output);
        setTime(time);
        setId(id);
    }

    public void setOutput(ItemStack output) {
        this.output = output;
    }

    public int getTime() {
        return this.time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public boolean matches(SimpleInventory inv, World world) {
        if(inv.size() < 1) return false;
        return output.isItemEqual(inv.getStack(0));
    }

    @Override
    public ItemStack craft(SimpleInventory var1) {
        return this.getOutput().copy();
    }

    @Override
    public boolean fits(int var1, int var2) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        return this.output;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return GravityDuperRecipeSerializer.INSTANCE;
    }


    public static class Type implements RecipeType<GravityDuperRecipe> {
		private Type() {}
		public static final Type INSTANCE = new Type();
		public static final String ID = "gravity_duper_recipe";
	}

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }
    
    public class JsonFormat {
        String output;
        int output_count;
        int time;
    }

}
