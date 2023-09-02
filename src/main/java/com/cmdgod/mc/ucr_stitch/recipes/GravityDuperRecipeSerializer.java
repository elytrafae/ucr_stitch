package com.cmdgod.mc.ucr_stitch.recipes;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
import com.cmdgod.mc.ucr_stitch.recipes.GravityDuperRecipe.JsonFormat;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;

public class GravityDuperRecipeSerializer implements RecipeSerializer<GravityDuperRecipe> {

    private GravityDuperRecipeSerializer() {
    }

    public static final GravityDuperRecipeSerializer INSTANCE = new GravityDuperRecipeSerializer();

    // This will be the "type" field in the json
    public static final Identifier ID = new Identifier(UCRStitch.MOD_NAMESPACE, "gravity_duper_recipe");

    @Override
    public GravityDuperRecipe read(Identifier id, JsonObject json) {
        JsonFormat recipeJson = new Gson().fromJson(json, JsonFormat.class);

        if (recipeJson.output == null) {
            throw new JsonSyntaxException("A required attribute is missing!");
        }

        Ingredient outputIngredient = Ingredient.fromJson(recipeJson.output);

        /*
        if (outputIngredient.getMatchingStacks().length <= 0) {
            throw new JsonSyntaxException("Output ingredient is empty!");
        }
        */

        /*
        Item outputItem = Registry.ITEM.getOrEmpty(new Identifier(recipeJson.output))
            // Validate the inputted item actually exists
            .orElseThrow(() -> new JsonSyntaxException("No such item " + recipeJson.output));
        */

        int output_count = Math.max(recipeJson.output_count, 1);
        int time = Math.max(recipeJson.time, 1);

        return new GravityDuperRecipe(outputIngredient, output_count, time, id);
    }

    @Override
    public GravityDuperRecipe read(Identifier id, PacketByteBuf packetData) {
        Ingredient output = Ingredient.fromPacket(packetData);
        int time = packetData.readInt();
        int count = packetData.readInt();
        return new GravityDuperRecipe(output, count, time, id);
    }

    @Override
    public void write(PacketByteBuf packetData, GravityDuperRecipe recipe) {
        recipe.getOutputVal().write(packetData);
        packetData.writeInt(recipe.getTime());
        packetData.writeInt(recipe.getCount());
    }
    
}
