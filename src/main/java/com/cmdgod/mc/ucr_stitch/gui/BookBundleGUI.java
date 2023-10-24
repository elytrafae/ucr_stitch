package com.cmdgod.mc.ucr_stitch.gui;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.mutable.MutableInt;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
//import com.cmdgod.mc.ucr_stitch.config.UCRStitchConfig;
import com.cmdgod.mc.ucr_stitch.inventories.BookBundleInventory;
import com.cmdgod.mc.ucr_stitch.items.BookkeeperBundle;
import com.cmdgod.mc.ucr_stitch.registrers.ModTags;
import com.cmdgod.mc.ucr_stitch.tools.Utility;

import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public class BookBundleGUI extends SimpleGui {

    private BookBundleInventory inventory;
    private ItemStack stack;
    private boolean playExitSound = true;

    public BookBundleGUI(ServerPlayerEntity player, ItemStack bundleStack) {
        super(getScreenHandlerType(UCRStitch.CONFIG.bookkeeperBundleSlots()), player, true);
        this.inventory = new BookBundleInventory(bundleStack);
        this.stack = bundleStack;
        setTitle(bundleStack.getName());
        setLockPlayerInventory(true);
        
        reflectBookInv();
        reflectPlayerInv();

        ItemStack lockedSlotStack = new ItemStack(Items.GRAY_STAINED_GLASS_PANE);
        lockedSlotStack.setCustomName(Text.of(""));
        for (int i=inventory.size(); i < getVirtualSize(); i++) {
            setSlot(i, lockedSlotStack);
        }

    }

    private void reflectBookInv() {
        MutableInt selectedSlot = new MutableInt(BookkeeperBundle.getSelectedBookSlot(this.stack));
        for (int i=0; i < inventory.size(); i++) {
            ItemStack displayStack = inventory.getStack(i).copy();
            if (selectedSlot.getValue() == i) {
                Utility.addLinesToLore(displayStack, Text.translatable("item.ucr_stitch.bookkeeper_bundle.selected"));
            }
            if (BookkeeperBundle.hasOpenInstruction(displayStack)) {
                Utility.addLinesToLore(displayStack, Text.translatable("item.ucr_stitch.bookkeeper_bundle.open"));
            }
            Utility.addLinesToLore(displayStack, Text.translatable("item.ucr_stitch.bookkeeper_bundle.takeout"));

            setSlot(i, displayStack, (index, type, action, gui) -> {
                if (type.shift) {
                    getPlayer().getInventory().offerOrDrop(inventory.removeAndShift(index));
                    if (selectedSlot.getValue() == index) {
                        selectedSlot.setValue(-1);
                    } else if (selectedSlot.getValue() > index) {
                        selectedSlot.add(-1);
                    }
                    BookkeeperBundle.setSelectedBookSlot(this.stack, selectedSlot.getValue());
                    playMoveSound();
                    reflectBookInv();
                    reflectPlayerInv();
                } else {
                    ItemStack stack = inventory.getStack(index).copy();
                    if (BookkeeperBundle.hasOpenInstruction(stack)) {
                        BookkeeperBundle.setSelectedBookSlot(this.stack, index);
                        playExitSound = false;
                        close();
                        BookkeeperBundle.openBook(stack, getPlayer());
                    } else if (!stack.isEmpty()) {
                        playErrorSound();
                    }
                }
            });
        }
    }

    private void reflectPlayerInv() {
        PlayerInventory inv = getPlayer().getInventory();
        for (int i=getVirtualSize(); i < getSize(); i++) {
            int playerInvIndex = (i-getVirtualSize()+9)%36;
            ItemStack displayStack = inv.getStack(playerInvIndex).copy();
            if (displayStack.isIn(ModTags.Items.ALL_BOOKS)) {
                Utility.addLinesToLore(displayStack, Text.translatable("item.ucr_stitch.bookkeeper_bundle.insert"));
            } else {
                Utility.addLinesToLore(displayStack, Text.translatable("item.ucr_stitch.bookkeeper_bundle.invalid"));
            }
            setSlot(i, displayStack, (index, type, action, gui) -> {
                ItemStack stack = inv.getStack(playerInvIndex);
                if (type.shift && stack.isIn(ModTags.Items.ALL_BOOKS)) {
                    if (inventory.appendStack(stack)) {
                        inv.removeStack(playerInvIndex);
                        reflectBookInv();
                        reflectPlayerInv();
                        playMoveSound();
                    } else if (!stack.isEmpty()){
                        playErrorSound();
                    }
                } else if (!stack.isEmpty()) {
                    playErrorSound();
                }
            });
        }
    }

    public void onClose() {
        if (playExitSound) {
            playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1f, 0.7f);
        }
    }

    private void playSound(SoundEvent event, float volume, float pitch) {
        Utility.playSound(getPlayer(), event, volume, pitch);
    }

    private void playMoveSound() {
        playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1f, 1.5f);
    }

    private void playErrorSound() {
        playSound(SoundEvents.ENTITY_PLAYER_BURP, 1f, 1.5f);
    }

    private static ScreenHandlerType<?> getScreenHandlerType(int slots) {
        int rows = (int)Math.ceil(((double)slots)/9);
        ArrayList<ScreenHandlerType<?>> types = new ArrayList<>(List.of(ScreenHandlerType.GENERIC_9X1, ScreenHandlerType.GENERIC_9X2, ScreenHandlerType.GENERIC_9X3, ScreenHandlerType.GENERIC_9X4, ScreenHandlerType.GENERIC_9X5, ScreenHandlerType.GENERIC_9X6));
        return types.get(Math.max(Math.min(rows-1, types.size()-1), 0));
    }
        
}
