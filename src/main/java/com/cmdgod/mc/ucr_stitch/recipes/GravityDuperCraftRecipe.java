package com.cmdgod.mc.ucr_stitch.recipes;

import java.util.Optional;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
import com.cmdgod.mc.ucr_stitch.blockentities.GravityDuperBlockEntity;
import com.cmdgod.mc.ucr_stitch.blocks.GravityDuperBlock;
import com.cmdgod.mc.ucr_stitch.registrers.ItemRegistrer;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class GravityDuperCraftRecipe extends SpecialCraftingRecipe {

    public GravityDuperCraftRecipe(Identifier id) {
        super(id);
    }

    // Recipe Form: 
    // STS
    // DDD
    // SBS
    // S: Side, T: Top, B: Bottom, D: Block to be duped

    @Override
    public boolean matches(CraftingInventory inv, World world) {
        boolean hasStructure =      inv.getStack(0).isOf(ItemRegistrer.GRAVITY_DUPER_SIDE) &&
                                    inv.getStack(1).isOf(ItemRegistrer.GRAVITY_DUPER_TOP) &&
                                    inv.getStack(2).isOf(ItemRegistrer.GRAVITY_DUPER_SIDE) &&
                                    inv.getStack(6).isOf(ItemRegistrer.GRAVITY_DUPER_SIDE) &&
                                    inv.getStack(7).isOf(ItemRegistrer.GRAVITY_DUPER_BOTTOM) &&
                                    inv.getStack(8).isOf(ItemRegistrer.GRAVITY_DUPER_SIDE);
                                
        if (!hasStructure) {
            return false;
        }

        if (!(inv.getStack(3).isItemEqual(inv.getStack(4)) && inv.getStack(3).isItemEqual(inv.getStack(5)))) {
            return false;
        }

        // At this point, the crafting recipe is correct, now we just have to see if the items in questions are allowed to be duped
        return getRecipeFor(inv.getStack(3), world) != null; 
    }

    @Override
    public ItemStack craft(CraftingInventory inv) {
        ItemStack stack = new ItemStack(ItemRegistrer.GRAVITY_DUPER_ITEM, 1);
        NbtCompound blockNbt = new NbtCompound();
        blockNbt.putString(GravityDuperBlockEntity.BLOCK_KEY, Registry.ITEM.getId(inv.getStack(3).getItem()).toString());
        NbtCompound itemNbt = stack.getOrCreateNbt();
        itemNbt.put("BlockEntityTag", blockNbt);
        return stack;
    }

    @Override
    public boolean fits(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Serializer extends SpecialRecipeSerializer<GravityDuperCraftRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "gravity_duper_craft_recipe";

        private Serializer() {
            super(GravityDuperCraftRecipe::new);
        }
    }

    private GravityDuperRecipe getRecipeFor(ItemStack stack, World world) {
        SimpleInventory inv2 = new SimpleInventory(stack.copy());
        Optional<GravityDuperRecipe> recipe = world.getRecipeManager().getFirstMatch(GravityDuperRecipe.Type.INSTANCE, inv2, world);
        if (recipe.isPresent()) {
            return recipe.get();
        }
        return null;
    }
    
}
