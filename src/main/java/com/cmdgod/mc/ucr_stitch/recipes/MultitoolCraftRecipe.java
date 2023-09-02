package com.cmdgod.mc.ucr_stitch.recipes;

import com.cmdgod.mc.ucr_stitch.items.Multitool;
import com.cmdgod.mc.ucr_stitch.registrers.ModItems;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class MultitoolCraftRecipe extends SpecialCraftingRecipe {

    public MultitoolCraftRecipe(Identifier id) {
        super(id);
    }

    // Recipe Form: 
    // Shapeless, needs a Shovel, Axe, Pickaxe and Hoe of the same material.

    @Override
    public boolean matches(CraftingInventory inv, World world) {
        ItemStack hoe = null, axe = null, pickaxe = null, shovel = null;
        for (int i=0; i < inv.size(); i++) {
            ItemStack stack = inv.getStack(i);
            Item item = stack.getItem();
            if (item instanceof HoeItem) {
                hoe = stack;
            } else if (item instanceof AxeItem) {
                axe = stack;
            } else if (item instanceof PickaxeItem) {
                pickaxe = stack;
            } else if (item instanceof ShovelItem) {
                shovel = stack;
            } else if (!stack.isEmpty()) {
                return false;
            }
        }
        if (hoe == null || axe == null || pickaxe == null || shovel == null) {
            return false;
        }
        ToolMaterial material = getMaterial(hoe);
        if (!ModItems.MULTITOOLS.containsKey(material)) {
            return false;
        }
        return (material == getMaterial(axe) && material == getMaterial(pickaxe) && material == getMaterial(shovel));
    }

    @Override
    public ItemStack craft(CraftingInventory inv) {
        ToolMaterial material = getMaterial(getFirstNonEmptyStack(inv));
        if (material == null) {
            return new ItemStack(Items.STONE);
        }
        if (!ModItems.MULTITOOLS.containsKey(material)) {
            return new ItemStack(Items.STONE);
        }
        Multitool tool = ModItems.MULTITOOLS.get(material);
        ItemStack stack = new ItemStack(tool);
        stack.setDamage((int)Math.floor(getDamageRelative(inv) * stack.getMaxDamage()));
        return stack;
    }

    @Override
    public boolean fits(int width, int height) {
        return (width * height) >= 4;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Serializer extends SpecialRecipeSerializer<MultitoolCraftRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "multitool_craft_recipe";

        private Serializer() {
            super(MultitoolCraftRecipe::new);
        }
    }

    private static ToolMaterial getMaterial(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return null;
        }
        Item item = stack.getItem();
        if (!(item instanceof ToolItem)) {
            return null;
        }
        return ((ToolItem)item).getMaterial();
    }

    private static ItemStack getFirstNonEmptyStack(CraftingInventory inv) {
        for (int i=0; i < inv.size(); i++) {
            ItemStack stack = inv.getStack(i);
            if (!stack.isEmpty()) {
                return stack;
            }
        }
        return null;
    }

    private static float getDamageRelative(CraftingInventory inv) {
        int total = 0;
        int maxTotal = 0;
        for (int i=0; i < inv.size(); i++) {
            ItemStack stack = inv.getStack(i);
            if (stack.getItem() instanceof ToolItem) {
                maxTotal += stack.getMaxDamage();
                total += stack.getDamage();
            }
        }
        // Prevent (not really possible) division by 0
        if (maxTotal == 0) {
            return 0;
        }
        return ((float)total)/maxTotal;
    }
    
}
