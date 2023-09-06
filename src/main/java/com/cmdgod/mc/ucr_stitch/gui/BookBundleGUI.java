package com.cmdgod.mc.ucr_stitch.gui;

import org.jetbrains.annotations.Nullable;

import com.cmdgod.mc.ucr_stitch.inventories.BookBundleInventory;
import com.cmdgod.mc.ucr_stitch.items.BookkeeperBundle;
import com.cmdgod.mc.ucr_stitch.registrers.ModTags;

import eu.pb4.sgui.api.gui.BaseSlotGui;
import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

public class BookBundleGUI extends SimpleGui {

    private BookBundleInventory inventory;
    private ItemStack stack;

    public BookBundleGUI(ServerPlayerEntity player, ItemStack bundleStack) {
        super(ScreenHandlerType.GENERIC_9X2, player, true);
        this.inventory = new BookBundleInventory(bundleStack);
        this.stack = bundleStack;
        setTitle(bundleStack.getName());
        setLockPlayerInventory(true);
        
        reflectBookInv();
        reflectPlayerInv();

        ItemStack lockedSlotStack = new ItemStack(Items.GRAY_STAINED_GLASS_PANE);
        lockedSlotStack.setCustomName(Text.of(""));
        for (int i=BookBundleInventory.SLOTS; i < getVirtualSize(); i++) {
            setSlot(i, lockedSlotStack);
        }

    }

    private void reflectBookInv() {
        for (int i=0; i < BookBundleInventory.SLOTS; i++) {
            setSlot(i, inventory.getStack(i), (index, type, action, gui) -> {
                if (type.shift) {
                    getPlayer().getInventory().offerOrDrop(inventory.removeAndShift(index));
                    reflectBookInv();
                    reflectPlayerInv();
                } else {
                    ItemStack stack = inventory.getStack(index).copy();
                    if (BookkeeperBundle.hasOpenInstruction(stack)) {
                        BookkeeperBundle.setSelectedBookSlot(this.stack, index);
                        close();
                        BookkeeperBundle.openBook(stack, getPlayer());
                    }
                }
            });
        }
    }

    private void reflectPlayerInv() {
        PlayerInventory inv = getPlayer().getInventory();
        for (int i=getVirtualSize(); i < getSize(); i++) {
            int playerInvIndex = (i-getVirtualSize()+9)%36;
            setSlot(i, inv.getStack(playerInvIndex), (index, type, action, gui) -> {
                ItemStack stack = inv.getStack(playerInvIndex);
                if (type.shift && stack.isIn(ModTags.Items.ALL_BOOKS)) {
                    if (inventory.appendStack(stack)) {
                        inv.removeStack(playerInvIndex);
                        reflectBookInv();
                        reflectPlayerInv();
                    }
                }
            });
        }
    }


        
}
