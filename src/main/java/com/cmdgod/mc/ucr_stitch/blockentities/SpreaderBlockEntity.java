package com.cmdgod.mc.ucr_stitch.blockentities;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
import com.cmdgod.mc.ucr_stitch.inventories.ImplementedInventory;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class SpreaderBlockEntity extends BlockEntity implements ImplementedInventory {
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(1, ItemStack.EMPTY); // 9 crafting slots + 1 output

    public SpreaderBlockEntity(BlockPos pos, BlockState state) {
        super(UCRStitch.COMPRESSED_CRAFTING_TABLE_ENTITY, pos, state);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, items);
    }
 
    @Override
    public void writeNbt(NbtCompound nbt) {
        Inventories.writeNbt(nbt, items);
        super.writeNbt(nbt);
    }

    @Override
    public int[] getAvailableSlots(Direction dir) {
        // Just return an array of all slots
        int[] result = new int[getItems().size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }
 
        return result;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction direction) {
        return slot != 9;
    }
 
    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction direction) {
        return slot == 9;
    }

    
}
