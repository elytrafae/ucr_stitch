package com.cmdgod.mc.ucr_stitch.items;

import java.util.HashMap;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.cmdgod.mc.ucr_stitch.gui.BookBundleGUI;
import com.cmdgod.mc.ucr_stitch.inventories.BookBundleInventory;
import com.cmdgod.mc.ucr_stitch.tools.Utility;

import eu.pb4.sgui.api.gui.BookGui;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WritableBookItem;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.common.base.PatchouliSounds;
import vazkii.patchouli.common.item.ItemModBook;

public class BookkeeperBundle extends Item {

    public static final String SELECTED_SLOT_KEY = "SelectedSlot";

    public static final HashMap<Identifier, Identifier> PATCHOULI_EXCEPTIONS = new HashMap<>();

    public BookkeeperBundle(Settings settings) {
        super(settings.maxCount(1));
        
        if (PATCHOULI_EXCEPTIONS.size() <= 0) {
            PATCHOULI_EXCEPTIONS.put(new Identifier("botania", "lexicon"), new Identifier("botania", "lexicon"));
            PATCHOULI_EXCEPTIONS.put(new Identifier("zenith", "book"), new Identifier("zenith", "apoth_chronicle"));
            PATCHOULI_EXCEPTIONS.put(new Identifier("bewitchment", "book_of_shadows"), new Identifier("bewitchment", "book_of_shadows"));
        }
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
            Utility.playSound(user, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1f, 0.7f);
        } else {
            ItemStack bookStack = getSelectedStack(stack);
            if (bookStack.isEmpty() || !hasOpenInstruction(bookStack)) {
                user.sendMessage(Text.translatable("item.ucr_stitch.bookkeeper_bundle.noselect1"), true);
                user.sendMessage(Text.translatable("item.ucr_stitch.bookkeeper_bundle.noselect1"), false);
                user.sendMessage(Text.translatable("item.ucr_stitch.bookkeeper_bundle.noselect2"), false);
                Utility.playSound(user, SoundEvents.ENTITY_PLAYER_BURP, 1f, 1.5f);
                
            } else {
                openBook(bookStack, (ServerPlayerEntity)user);
            }
        }
        
        return TypedActionResult.success(stack, true);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        ItemStack selectedStack = getSelectedStack(stack);
        if (!selectedStack.isEmpty()) {
            tooltip.add(Text.translatable("item.ucr_stitch.bookkeeper_bundle.select_tooltip", selectedStack.getName()));
        }
    }

    public static boolean hasOpenInstruction(ItemStack stack) {
        Item item = stack.getItem();
        Identifier id = Registry.ITEM.getId(item);
        return item instanceof ItemModBook || item instanceof WrittenBookItem || item instanceof WritableBookItem || PATCHOULI_EXCEPTIONS.containsKey(id);
    }

    public static void openBook(ItemStack stack, ServerPlayerEntity player) {
        Item item = stack.getItem();
        Identifier id = Registry.ITEM.getId(item);
        if (item instanceof ItemModBook || PATCHOULI_EXCEPTIONS.containsKey(id)) {
            Identifier bookId;
            if (PATCHOULI_EXCEPTIONS.containsKey(id)) {
                bookId = PATCHOULI_EXCEPTIONS.get(id);
            } else {
                bookId = ItemModBook.getBook(stack).id;
            }
            PatchouliAPI.get().openBookGUI((ServerPlayerEntity) player, bookId);
			// This plays the sound to others nearby, playing to the actual opening player handled from the packet
			SoundEvent sfx = PatchouliSounds.BOOK_OPEN;//PatchouliSounds.getSound(book.openSound, PatchouliSounds.BOOK_OPEN);
			player.playSound(sfx, 1F, (float) (0.7 + Math.random() * 0.4));
        } else if (item instanceof WrittenBookItem || item instanceof WritableBookItem) {
            Utility.playSound(player, SoundEvents.ITEM_BOOK_PAGE_TURN, 1f, 0.8f);
            BookGui bookGUI = new BookGui(player, stack);
            bookGUI.open();
            //player.useBook(stack, Hand.MAIN_HAND);
            player.incrementStat(Stats.USED.getOrCreateStat(item));
        }
    }

    public static void setSelectedBookSlot(ItemStack stack, int slot) {
        stack.getOrCreateNbt().putInt(SELECTED_SLOT_KEY, slot);
    }

    public static int getSelectedBookSlot(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if (!nbt.contains(SELECTED_SLOT_KEY, NbtCompound.INT_TYPE)) {
            return -1;
        }
        int slot = nbt.getInt(SELECTED_SLOT_KEY);
        if (slot < 0) {
            return -1;
        }
        BookBundleInventory inv = new BookBundleInventory(stack);
        if (slot < inv.size() && !inv.getStack(slot).isEmpty()) {
            return slot;
        }
        return -1;
    }

    public static ItemStack getSelectedStack(ItemStack bundleStack) {
        int slot = getSelectedBookSlot(bundleStack);
        if (slot < 0) {
            return ItemStack.EMPTY;
        }
        BookBundleInventory inv = new BookBundleInventory(bundleStack);
        return inv.getStack(slot);
    }
    
}
