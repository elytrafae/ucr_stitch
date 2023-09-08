package com.cmdgod.mc.ucr_stitch.gui;

import java.util.function.Predicate;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class FilteredInputSlot extends Slot {

    Predicate<ItemStack> predicate;

    public FilteredInputSlot(Inventory inventory, int index, Predicate<ItemStack> predicate) {
        this(inventory, index, predicate, -80, -80);
    }

    public FilteredInputSlot(Inventory inventory, int index, Predicate<ItemStack> predicate, int x, int y) {
        super(inventory, index, x, y);
        this.predicate = predicate;
    }

    public boolean canInsert(ItemStack stack) {
      return predicate.test(stack);
    }
    
}
