package com.cmdgod.mc.ucr_stitch.recipes;

import java.util.ArrayList;

import com.cmdgod.mc.ucr_stitch.UCRStitch;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.minecraft.recipe.SpecialCraftingRecipe;

public class HeadFragmentCraftRecipe extends SpecialCraftingRecipe {

    ArrayList<ArrayList<Pair<Item, Integer>>> recipe = new ArrayList<ArrayList<Pair<Item, Integer>>>();
    
    public HeadFragmentCraftRecipe(Identifier id, CraftingRecipeCategory category) {
        super(id, category);
        ArrayList<Pair<Item, Integer>> row1 = new ArrayList<>();
        ArrayList<Pair<Item, Integer>> row2 = new ArrayList<>();
        ArrayList<Pair<Item, Integer>> row3 = new ArrayList<>();
        row1.add(new Pair<Item, Integer>(Items.ROTTEN_FLESH, 32));
        row1.add(new Pair<Item, Integer>(Items.LEATHER, 32));
        row1.add(new Pair<Item, Integer>(Items.ROTTEN_FLESH, 32));
        row2.add(new Pair<Item, Integer>(Items.LEATHER, 32));
        row2.add(new Pair<Item, Integer>(Items.ENDER_EYE, 1));
        row2.add(new Pair<Item, Integer>(Items.LEATHER, 32));
        row3.add(new Pair<Item, Integer>(Items.ROTTEN_FLESH, 32));
        row3.add(new Pair<Item, Integer>(Items.LEATHER, 32));
        row3.add(new Pair<Item, Integer>(Items.ROTTEN_FLESH, 32));

        recipe.add(row1);
        recipe.add(row2);
        recipe.add(row3);
    }

    @Override
    public boolean matches(CraftingInventory craftInv, World world) {
        // Makes sure the crafting inventory is big enough
        if (!fits(craftInv.getWidth(), craftInv.getHeight())) {
            return false;
        }

        for (int i=0; i < recipe.size(); i++) {
            ArrayList<Pair<Item, Integer>> row = recipe.get(i);
            for (int j=0; j < row.size(); j++) {
                Pair<Item, Integer> condition = row.get(j);
                ItemStack item = getFromPos(craftInv, i, j);
                if (!((item.isEmpty() && condition.getLeft() == Items.AIR) || (item.isOf(condition.getLeft()) && item.getCount() >= condition.getRight()))) {
                    return false;
                }
            } 
        }
        return true;
    }

    @Override
    public DefaultedList<ItemStack> getRemainder(CraftingInventory craftInv) {
        DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(craftInv.size(), ItemStack.EMPTY);

        // I know, this entire thing is illegal, but desparate times call for desparate measures
        for (int i=0; i < recipe.size(); i++) {
            ArrayList<Pair<Item, Integer>> row = recipe.get(i);
            for (int j=0; j < row.size(); j++) {
                Pair<Item, Integer> condition = row.get(j);
                ItemStack item = getFromPos(craftInv, i, j);

                if (item.isEmpty()) {
                    //defaultedList.set(getIndexForPos(i, j, craftInv.getWidth()), ItemStack.EMPTY);
                    continue;
                }
                int reductionAmount = condition.getRight() - 1;
                if (item.getCount() <= reductionAmount) {
                    craftInv.setStack(getIndexForPos(i, j, craftInv.getWidth()), ItemStack.EMPTY);
                }
                item.setCount(item.getCount() - reductionAmount);
            } 
        }
        return defaultedList;
    }

    @Override
    public boolean fits(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }
    
    public static class Serializer extends SpecialRecipeSerializer<HeadFragmentCraftRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        private Serializer() {
            super(HeadFragmentCraftRecipe::new);
        }
    }

    @Override
    public ItemStack craft(CraftingInventory craftInv, DynamicRegistryManager var2) {
        return new ItemStack(UCRStitch.HEAD_FRAGMENT, 1);
    }

    private ItemStack getFromPos(CraftingInventory inv, int x, int y) {
        if (inv.size() <= getIndexForPos(x, y, inv.getWidth())) {
            return ItemStack.EMPTY;
        }
        return inv.getStack(getIndexForPos(x, y, inv.getWidth()));
    }

    private int getIndexForPos(int x, int y, int width) {
        return x*width + y;
    }

}
