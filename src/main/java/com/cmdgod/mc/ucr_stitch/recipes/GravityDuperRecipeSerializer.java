package com.cmdgod.mc.ucr_stitch.recipes;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
import com.cmdgod.mc.ucr_stitch.recipes.GravityDuperRecipe.JsonFormat;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

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

        Item outputItem = Registry.ITEM.getOrEmpty(new Identifier(recipeJson.output))
            // Validate the inputted item actually exists
            .orElseThrow(() -> new JsonSyntaxException("No such item " + recipeJson.output));

        recipeJson.output_count = Math.max(recipeJson.output_count, 1);
        recipeJson.time = Math.max(recipeJson.time, 1);

        int output_count = recipeJson.output_count;
        int time = recipeJson.time;

        ItemStack outputStack = new ItemStack(outputItem, output_count);

        return new GravityDuperRecipe(outputStack, time, id);
    }

    @Override
    public GravityDuperRecipe read(Identifier id, PacketByteBuf packetData) {
        ItemStack output = packetData.readItemStack();
        int time = packetData.readInt();
        return new GravityDuperRecipe(output, time, id);
    }

    @Override
    public void write(PacketByteBuf packetData, GravityDuperRecipe recipe) {
        packetData.writeItemStack(recipe.getOutput());
        packetData.writeInt(recipe.getTime());
    }
    
}
