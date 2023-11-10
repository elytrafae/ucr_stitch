package com.cmdgod.mc.ucr_stitch.recipes;

import java.util.ArrayList;

import com.cmdgod.mc.ucr_stitch.tools.ElytraUpgradeUtil;
import com.google.gson.JsonObject;

import io.github.apace100.apoli.power.Power;
import net.minecraft.client.gui.screen.ingame.SmithingScreen;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ElytraUpgradeRecipe extends SmithingRecipe {

    private Ingredient base;
    private Ingredient addition;
    private ItemStack result;
    private Identifier powerId;
    private String nameTextColor = "";
    private String descriptionTextColor = "";

    public ElytraUpgradeRecipe(Identifier id, Ingredient base, Ingredient addition, ItemStack result, Identifier powerId, String nameTextColor, String descriptionTextColor) {
        super(id, base, addition, result);
        this.base = base;
        this.addition = addition;
        this.result = result;
        this.powerId = powerId;
        this.nameTextColor = nameTextColor;
        this.descriptionTextColor = descriptionTextColor;
    }

    public ItemStack craft(Inventory inventory) {
        ItemStack outputStack = inventory.getStack(0).copy();
        ElytraUpgradeUtil.setUpgradeForElytra(outputStack, this);
        return outputStack;
    }

    public Ingredient getBase() {
        return this.base;
    }

    public Ingredient getAddition() {
        return this.addition;
    }

    public ItemStack getResult() {
        return this.result;
    }

    public Identifier getPowerId() {
        return this.powerId;
    }

    public String getNameTextColor() {
        return this.nameTextColor;
    }

    public String getDescriptionTextColor() {
        return this.descriptionTextColor;
    }

    public class JsonFormat {
        JsonObject base;
        JsonObject addition;
        JsonObject result;
        String power;
        String nameColor;
        String descriptionColor;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return GravityDuperRecipeSerializer.INSTANCE;
    }


    public static class Type implements RecipeType<ElytraUpgradeRecipe> {
		private Type() {}
		public static final Type INSTANCE = new Type();
		public static final String ID = "elytra_upgrade_recipe";
	}

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }
    
}
