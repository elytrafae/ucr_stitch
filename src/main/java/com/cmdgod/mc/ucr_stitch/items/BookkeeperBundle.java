package com.cmdgod.mc.ucr_stitch.items;

import org.apache.logging.log4j.core.jmx.Server;

import com.cmdgod.mc.ucr_stitch.gui.BookBundleGUI;
import com.cmdgod.mc.ucr_stitch.inventories.BookBundleInventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WritableBookItem;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.common.base.PatchouliSounds;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.item.ItemModBook;

public class BookkeeperBundle extends Item {

    public static final String SELECTED_SLOT_KEY = "SelectedSlot";

    public BookkeeperBundle(Settings settings) {
        super(settings.maxCount(1));
        //TODO Auto-generated constructor stub
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (world.isClient) {
            return TypedActionResult.success(stack, true);
        }
        if (user.isSneaking()) {
            BookBundleGUI gui = new BookBundleGUI((ServerPlayerEntity)user, stack);
            gui.open();
        } else {
            ItemStack bookStack = getSelectedStack(stack);
            if (bookStack.isEmpty() || !hasOpenInstruction(bookStack)) {
                user.sendMessage(Text.of("There is no valid selected book"), true);
                user.sendMessage(Text.of("There is no valid selected book"), false);
                user.sendMessage(Text.of("Please pick one with Shift+Right Click!"), false);
            } else {
                openBook(bookStack, (ServerPlayerEntity)user);
            }
        }
        
        return TypedActionResult.success(stack, true);
    }

    public static boolean hasOpenInstruction(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof ItemModBook || item instanceof WrittenBookItem || item instanceof WritableBookItem;
    }

    public static void openBook(ItemStack stack, ServerPlayerEntity player) {
        Item item = stack.getItem();
        if (item instanceof ItemModBook) {
            Book book = ItemModBook.getBook(stack);
            PatchouliAPI.get().openBookGUI((ServerPlayerEntity) player, book.id);
			// This plays the sound to others nearby, playing to the actual opening player handled from the packet
			SoundEvent sfx = PatchouliSounds.getSound(book.openSound, PatchouliSounds.BOOK_OPEN);
			player.playSound(sfx, 1F, (float) (0.7 + Math.random() * 0.4));
        } else if (item instanceof WrittenBookItem || item instanceof WritableBookItem) {
            player.useBook(stack, Hand.MAIN_HAND);
            player.incrementStat(Stats.USED.getOrCreateStat(item));
        }
    }

    public static void setSelectedBookSlot(ItemStack stack, int slot) {
        stack.getOrCreateNbt().putInt(SELECTED_SLOT_KEY, slot);
    }

    public static ItemStack getSelectedStack(ItemStack bundleStack) {
        NbtCompound nbt = bundleStack.getOrCreateNbt();
        ItemStack bookStack = ItemStack.EMPTY;
        if (nbt.contains(SELECTED_SLOT_KEY, NbtCompound.INT_TYPE)) {
            int slot = nbt.getInt(SELECTED_SLOT_KEY);
            BookBundleInventory inv = new BookBundleInventory(bundleStack);
            if (slot < inv.size()) {
                bookStack = inv.getStack(slot);
            }
        }
        return bookStack;
    }
    
}
