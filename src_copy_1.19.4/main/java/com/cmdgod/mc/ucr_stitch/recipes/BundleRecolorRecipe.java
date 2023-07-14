package com.cmdgod.mc.ucr_stitch.recipes;

import com.cmdgod.mc.ucr_stitch.UCRStitch;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.world.World;
import net.minecraft.recipe.SpecialCraftingRecipe;

public class BundleRecolorRecipe extends SpecialCraftingRecipe {
    
    public BundleRecolorRecipe(Identifier id, CraftingRecipeCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingInventory craftInv, World world) {
        // Makes sure the crafting inventory is big enough
        if (!fits(craftInv.getWidth(), craftInv.getHeight())) {
            return false;
        }

        ItemStack dye = ItemStack.EMPTY;
        ItemStack bundle = ItemStack.EMPTY;

        for (int i=0; i < craftInv.size(); i++) {
            ItemStack stack = craftInv.getStack(i);
            if (stack.isEmpty()) {
                continue; // Is the stack is empty, move onto the next one
            }
            if (UCRStitch.BUNDLES.values().contains(stack.getItem())) {
                if (bundle.isEmpty()) {
                    bundle = stack;
                    continue;
                } else {
                    return false; // If we already have a bundle in the grid, the recipe is invalid
                }
            }
            if (stack.getItem() instanceof DyeItem) {
                if (dye.isEmpty()) {
                    dye = stack;
                    continue;
                } else {
                    return false; // If we already have a dye in the grid, the recipe is invalid
                }
            }
            return false; // If there is something other than a dye or bundle inside, the recipe is invalid
        }
        if (dye.isEmpty() || bundle.isEmpty()) {
            return false;
        }
        // Cosmetic Check: Don't allow two a bundle to be recolored into itself.
        if (UCRStitch.BUNDLES.get(((DyeItem)dye.getItem()).getColor()).equals(bundle.getItem())) {
            return false;
        }
        return true;
    }

    @Override
    public boolean fits(int width, int height) {
        return width*height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }
    
    public static class Serializer extends SpecialRecipeSerializer<BundleRecolorRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        private Serializer() {
            super(BundleRecolorRecipe::new);
        }
    }

    @Override
    public ItemStack craft(CraftingInventory craftInv, DynamicRegistryManager var2) {
        boolean err = false;
        Pair<ItemStack,ItemStack> bundleAndDye = getBundleAndDye(craftInv);
        DyeColor dyeColor = ((DyeItem)bundleAndDye.getRight().getItem()).getColor();
        Item newColoredBundleItem = UCRStitch.BUNDLES.get(dyeColor);
        if (newColoredBundleItem == null) {
            newColoredBundleItem = UCRStitch.BUNDLES.get(DyeColor.WHITE);
            err = true;
        }
        ItemStack newStack = new ItemStack(newColoredBundleItem, bundleAndDye.getLeft().getCount());
        newStack.setNbt(bundleAndDye.getLeft().getOrCreateNbt().copy());
        if (err) {
            newStack.setCustomName(Text.of("ERROR: NO BUNDLE FOUND FOR COLOR " + dyeColor.getName()));
        }
        return newStack;
    }

    // A version of the "matches" code, but with far less checks
    private Pair<ItemStack,ItemStack> getBundleAndDye(CraftingInventory craftInv) {
        ItemStack dye = ItemStack.EMPTY;
        ItemStack bundle = ItemStack.EMPTY;

        for (int i=0; i < craftInv.size(); i++) {
            ItemStack stack = craftInv.getStack(i);
            if (stack.isEmpty()) {
                continue; // Is the stack is empty, move onto the next one
            }
            if (UCRStitch.BUNDLES.values().contains(stack.getItem())) {
                bundle = stack;
                continue;
            }
            dye = stack;
        }
        return new Pair<ItemStack,ItemStack>(bundle, dye);
    }

}
