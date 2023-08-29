package com.cmdgod.mc.ucr_stitch.recipes;

import com.google.gson.JsonObject;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class GravityDuperRecipe implements Recipe<SimpleInventory> {

    private Ingredient output;
    private int time;
    private int count;

    private Identifier id;
    private void setId(Identifier id) {
        this.id = id;
    }

    public GravityDuperRecipe(Ingredient output, int outputCount, int time, Identifier id) {
        setOutput(output);
        setCount(outputCount);
        setTime(time);
        setId(id);
    }

    public void setOutput(Ingredient output) {
        this.output = output;
    }

    public Ingredient getOutputVal() {
        return this.output;
    }

    public int getTime() {
        return this.time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean matches(SimpleInventory inv, World world) {
        if(inv.size() < 1) return false;
        return output.test(inv.getStack(0));
    }

    @Override
    public ItemStack craft(SimpleInventory inv) {
        ItemStack stack = inv.getStack(2).copy();
        stack.setCount(getCount());
        return stack;
    }

    @Override
    public boolean fits(int var1, int var2) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        //throw new IllegalCallerException();
        // This should be called for visual reasons only!
        if (this.output.getMatchingStacks().length <= 0) {
            return ItemStack.EMPTY;
        }
        return this.output.getMatchingStacks()[0];
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
        JsonObject output;
        int output_count;
        int time;
    }

}
