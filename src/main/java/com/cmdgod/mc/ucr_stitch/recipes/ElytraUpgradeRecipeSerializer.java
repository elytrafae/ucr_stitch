package com.cmdgod.mc.ucr_stitch.recipes;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
import com.cmdgod.mc.ucr_stitch.recipes.ElytraUpgradeRecipe.JsonFormat;
import com.cmdgod.mc.ucr_stitch.tools.Utility;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;

public class ElytraUpgradeRecipeSerializer implements RecipeSerializer<ElytraUpgradeRecipe> {

    private ElytraUpgradeRecipeSerializer() {
    }

    public static final ElytraUpgradeRecipeSerializer INSTANCE = new ElytraUpgradeRecipeSerializer();

    // This will be the "type" field in the json
    public static final Identifier ID = new Identifier(UCRStitch.MOD_NAMESPACE, "elytra_upgrade_recipe");

    @Override
    public ElytraUpgradeRecipe read(Identifier id, JsonObject json) {
        JsonFormat recipeJson = new Gson().fromJson(json, JsonFormat.class);

        if (recipeJson.base == null || recipeJson.addition == null || recipeJson.result == null || recipeJson.power == null) {
            throw new JsonSyntaxException("A required attribute is missing!");
        }

        Ingredient baseIngredient = Ingredient.fromJson(recipeJson.base);
        Ingredient additionIngredient = Ingredient.fromJson(recipeJson.addition);
        ItemStack resultStack = ShapedRecipe.outputFromJson(recipeJson.result);
        Identifier powerId = new Identifier(recipeJson.power);
        String nameColor = Utility.stringOr(recipeJson.nameColor, "");
        String descColor = Utility.stringOr(recipeJson.descriptionColor, "");

        // Do not error if tags were not loaded yet!
        /*
        if (baseIngredient.getMatchingStacks().length <= 0 || additionIngredient.getMatchingStacks().length <= 0) {
            throw new JsonSyntaxException("Base or Addition ingredient is empty!");
        }
        */

        // Add check for power existing!

        return new ElytraUpgradeRecipe(id, baseIngredient, additionIngredient, resultStack, powerId, nameColor, descColor);
    }

    @Override
    public ElytraUpgradeRecipe read(Identifier id, PacketByteBuf packetData) {
        Ingredient base = Ingredient.fromPacket(packetData);
        Ingredient addition = Ingredient.fromPacket(packetData);
        ItemStack result = packetData.readItemStack();
        Identifier powerId = new Identifier(packetData.readString());
        String nameColor = packetData.readString();
        String descColor = packetData.readString();
        return new ElytraUpgradeRecipe(id, base, addition, result, powerId, nameColor, descColor);
    }

    @Override
    public void write(PacketByteBuf packetData, ElytraUpgradeRecipe recipe) {
        recipe.getBase().write(packetData);
        recipe.getAddition().write(packetData);
        packetData.writeItemStack(recipe.getResult());
        packetData.writeString(recipe.getPowerId().toString());
        packetData.writeString(recipe.getNameTextColor());
        packetData.writeString(recipe.getDescriptionTextColor());
    }
    
}
