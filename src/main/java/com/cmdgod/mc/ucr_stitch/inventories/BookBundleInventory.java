package com.cmdgod.mc.ucr_stitch.inventories;

import com.cmdgod.mc.ucr_stitch.UCRStitch;

//import com.cmdgod.mc.ucr_stitch.config.UCRStitchConfig;

import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public class BookBundleInventory implements ImplementedInventory {

    //public static int SLOTS = 18;
    private DefaultedList<ItemStack> items = DefaultedList.ofSize(UCRStitch.CONFIG.bookkeeperBundleSlots(), ItemStack.EMPTY);
    private ItemStack stack;

    public BookBundleInventory(ItemStack stack) {
        Inventories.readNbt(stack.getOrCreateNbt(), items);
        this.stack = stack;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Override
    public void markDirty() {
        Inventories.writeNbt(stack.getOrCreateNbt(), items);
    }

    public ItemStack removeAndShift(int slot) {
        ItemStack stack = getStack(slot);
        for (int i=slot + 1; i < items.size(); i++) {
            items.set(i-1, items.get(i));
        }
        markDirty();
        return stack;
    }

    public boolean appendStack(ItemStack stack) {
        for (int i=0; i < items.size(); i++) {
            if (items.get(i).isEmpty()) {
                items.set(i, stack);
                markDirty();
                return true;
            }
        }
        return false;
    }
    
}
