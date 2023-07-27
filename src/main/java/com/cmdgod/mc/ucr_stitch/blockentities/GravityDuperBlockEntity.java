package com.cmdgod.mc.ucr_stitch.blockentities;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
import com.cmdgod.mc.ucr_stitch.blocks.GravityDuperBlock;
import com.cmdgod.mc.ucr_stitch.inventories.ImplementedInventory;
import com.cmdgod.mc.ucr_stitch.recipes.GravityDuperRecipe;
import com.cmdgod.mc.ucr_stitch.registrers.BlockRegistrer;
import com.google.gson.JsonSyntaxException;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.Recipe;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class GravityDuperBlockEntity extends BlockEntity implements ImplementedInventory, SidedInventory {

    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(2, ItemStack.EMPTY);
    public static final int FUEL_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;
    public static final String TICKS_TILL_NEXT_KEY = "ticks_till_next";
    public static final String FUEL_TICKS_KEY = "fuel_ticks";
    public static final String RECIPE_KEY = "recipe";
    public static final String BLOCK_KEY = "dupe_block";
    private int ticksTillNext = 0;
    private int fuelTicks = 0;
    private Identifier recipeId = null;
    private Identifier blockId = null;
    private boolean locked_up = false;

    public GravityDuperBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistrer.GRAVITY_DUPER_ENTITY, pos, state);
    }
    
    public static void tick(World world, BlockPos pos, BlockState state, GravityDuperBlockEntity be) {
        // Fuel always ticks down, no matter the player's shenanigans
        if (be.fuelTicks > 0) {
            be.fuelTicks--;
        }

        GravityDuperRecipe recipe = be.getRecipe(world);
        if (recipe == null) {
            world.setBlockState(pos, state.with(GravityDuperBlock.WORKING, false).with(GravityDuperBlock.STUCK, true));
            if (!be.locked_up) {
                System.out.println("A Gravity duper at location " + pos.toString() + " has an invalid recipe!");
                be.locked_up = true;
            }
            return;
        }
        be.locked_up = false;
        world.setBlockState(pos, state.with(GravityDuperBlock.WORKING, be.fuelTicks > 0).with(GravityDuperBlock.STUCK, false));

        ItemStack recipeOutput = recipe.craft(be.createSimpleInventory());
        ItemStack outputStack = be.getStack(OUTPUT_SLOT);

        boolean isHalted = false;
        // Reset progress every tick while cannot input to output stack
        if (!be.canBeCombined(recipeOutput, outputStack)) {
            System.out.println("Can't be combined: " + recipeOutput.getItem() + " and " + outputStack.getItem());
            be.ticksTillNext = recipe.getTime() + 1;
            isHalted = true;
        }

        if (!isHalted && be.fuelTicks > 0) {
            be.ticksTillNext--;
            if (be.ticksTillNext <= 0) {
                be.ticksTillNext = recipe.getTime();
                
                // Assumes the above checks for the ability to put items in and out is correct . . .
                if (outputStack.isEmpty()) {
                    be.setStack(OUTPUT_SLOT, recipeOutput);
                } else {
                    outputStack.increment(recipeOutput.getCount());
                }
            }
        }
        
        ItemStack fuelStack = be.getStack(FUEL_SLOT);
        if (be.fuelTicks <= 0 && !isHalted && isStackFuel(fuelStack)) {
            be.fuelTicks = FuelRegistry.INSTANCE.get(fuelStack.getItem());
            if (fuelStack.getCount() == 1) {
                be.setStack(FUEL_SLOT, new ItemStack(fuelStack.getItem().getRecipeRemainder()));
            } else {
                fuelStack.decrement(1);
            }
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        // Save the current value of the number to the nbt
        if (recipeId != null) {
            nbt.putString(RECIPE_KEY, recipeId.toString());
        }
        if (blockId != null) {
            nbt.putString(BLOCK_KEY, blockId.toString());
        }
        nbt.putInt(TICKS_TILL_NEXT_KEY, ticksTillNext);
        nbt.putInt(FUEL_TICKS_KEY, fuelTicks);
        Inventories.writeNbt(nbt, items);
 
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, items);
        fuelTicks = nbt.getInt(FUEL_TICKS_KEY);
        ticksTillNext = nbt.getInt(TICKS_TILL_NEXT_KEY);
        if (nbt.contains(RECIPE_KEY)) {
            recipeId = new Identifier(nbt.getString(RECIPE_KEY));
        } else {
            recipeId = null;
        }
        if (nbt.contains(BLOCK_KEY)) {
            blockId = new Identifier(nbt.getString(BLOCK_KEY));
        } else {
            blockId = null;
        }
        
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
    
    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Override
    public int[] getAvailableSlots(Direction var1) {
        int[] result = new int[getItems().size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }
 
        return result;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction direction) {
        return slot == FUEL_SLOT && isStackFuel(stack);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction direction) {
        return slot == OUTPUT_SLOT || (slot == FUEL_SLOT && !isStackFuel(stack));
    }

    static public boolean isStackFuel(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        Item item = stack.getItem();
        int fuelTime = 0;
        try {
            fuelTime = FuelRegistry.INSTANCE.get(item);
        } catch (Exception e) {}
        return fuelTime > 0;
    }

    private SimpleInventory createSimpleInventory() {
        SimpleInventory inv = new SimpleInventory(items.size());
        for (int i=0; i < items.size(); i++) {
            inv.setStack(i, items.get(i));
        }
        return inv;
    }

    private GravityDuperRecipe getRecipe(World world) {
        GravityDuperRecipe recipe = getRecipe(world, this.recipeId, this.blockId);
        if (recipe != null) {
            this.recipeId = recipe.getId();
            this.blockId = Registry.ITEM.getId(recipe.getOutput().getItem());
        }
        return recipe;
    }

    public static GravityDuperRecipe getRecipe(World world, @Nullable Identifier recipeId, @Nullable Identifier blockId) {
        if (recipeId != null) {
            return getRecipeFromId(world, recipeId);
        }
        return getRecipeFromBlock(world, blockId);
    }

    private static GravityDuperRecipe getRecipeFromId(World world, @Nullable Identifier recipeId) {
        if (recipeId == null) {
            return null;
        }
        Optional<? extends Recipe<?>> recipe = world.getRecipeManager().get(recipeId);
        if (!recipe.isPresent()) {
            return null;
        }
        if (recipe.get() instanceof GravityDuperRecipe) {
            return (GravityDuperRecipe)recipe.get();
        }
        return null;
    }

    private static GravityDuperRecipe getRecipeFromBlock(World world, @Nullable Identifier blockId) {
        if (blockId == null) {
            return null;
        }
        Optional<Item> outputItem = Registry.ITEM.getOrEmpty(blockId);
        if (!outputItem.isPresent()) {
            return null;
        }
        SimpleInventory simpleInventory = new SimpleInventory(new ItemStack(outputItem.get()));
        Optional<GravityDuperRecipe> recipe = world.getRecipeManager().getFirstMatch(GravityDuperRecipe.Type.INSTANCE, simpleInventory, world);
        if (!recipe.isPresent()) {
            return null;
        }
        return recipe.get();
    }

    private boolean canBeCombined(ItemStack s1, ItemStack s2) {
        return s1.isEmpty() || s2.isEmpty() || (ItemStack.canCombine(s1, s2) && (s1.getMaxCount() >= s1.getCount() + s2.getCount()));
    }

    @Override
    public void markDirty() {
        if (this.world != null) {
            BlockEntity.markDirty(this.world, this.pos, this.getCachedState());
        }
    }

}
