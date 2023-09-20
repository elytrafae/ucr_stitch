package com.cmdgod.mc.ucr_stitch.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

import org.apache.commons.lang3.mutable.MutableInt;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

// NOTE: I could not import Hexcasting, so this is all improvised. DO NOT USE IN FUTURE PROJECTS!
// IDs for Media Holders: 
// hexcasting:amethyst_dust: 10000
// minecraft:amethyst_shard: 5*DUST
// hexcasting:charged_amethyst: 10*DUST
// hexcasting:battery: Varies
// hexcasting:media_unlocker: INFINITE
public class MediaUtility {

    // TODO: Fix creative unlocker!

    private static final Identifier DUST_ID = new Identifier("hexcasting", "amethyst_dust");
    private static final Identifier SHARD_ID = new Identifier("minecraft", "amethyst_shard");
    private static final Identifier CRYSTAL_ID = new Identifier("hexcasting", "charged_amethyst");
    private static final Identifier CREATIVE_ID = new Identifier("hexcasting", "creative_unlocker");
    private static final Identifier BATTERY_ID = new Identifier("hexcasting", "battery");

    private static final int DUST_VALUE = 10000;
    private static final HashMap<Identifier, Function<ItemStack, MyINT>> VALUE_MAP = new HashMap<>();
    static {
        VALUE_MAP.put(DUST_ID, (stack) -> new MyINT(DUST_VALUE));
        VALUE_MAP.put(SHARD_ID, (stack) -> new MyINT(DUST_VALUE*5));
        VALUE_MAP.put(CRYSTAL_ID, (stack) -> new MyINT(DUST_VALUE*10));
        VALUE_MAP.put(CREATIVE_ID, (stack) -> MyINT.INF);
        VALUE_MAP.put(BATTERY_ID, (stack) -> {
            NbtCompound nbt = stack.getOrCreateNbt();
            if (nbt.contains("hexcasting:media", NbtElement.INT_TYPE)) {
                return new MyINT(nbt.getInt("hexcasting:media"));
            }
            return new MyINT(0);
        });
    }
    
    public static int getMediaInInventory(Inventory inv) {
        MyINT count = new MyINT(0);
        for (var i=0; i < inv.size(); i++) {
            ItemStack stack = inv.getStack(i);
            Identifier itemId = Registry.ITEM.getId(stack.getItem());
            if (VALUE_MAP.containsKey(itemId)) {
                count.add(VALUE_MAP.get(itemId).apply(stack).multiply(stack.getCount()));
            }
        }
        return count.getValue();
    }

    public static double mediaToAmethystDust(int media) {
        return ((double)media)/DUST_VALUE;
    }

    // This should ONLY be called after you are certain the user has enough Media for this!
    public static void consumeMediaFromInventory(Inventory inv, int amount) {
        InventoryMediaSummary summary = new InventoryMediaSummary(inv);
        if (summary.creative.size() > 0) {
            // If we have sugar daddy's credit card, pay with it and don't care about the consequences.
            return;
        }
        int mediaInBatteries = mediaInBatteries(summary.battery);
        int mediaRequired = amount - mediaInBatteries;
        if (mediaRequired > 0) {
            int remaining = payWithItems(summary, mediaRequired, true);
            amount += remaining; // This SHOULD be negative or 0!
        }
        payWithBatteries(summary.battery, amount);
    }

    // The return value is the amount not paid. Can be negative for wasted media!
    private static int payWithItems(InventoryMediaSummary summary, int amount, boolean above) {
        amount = payWithItem(summary.crystal, amount, DUST_VALUE*10);
        amount = payWithItem(summary.shard, amount, DUST_VALUE*5);
        amount = payWithItem(summary.dust, amount, DUST_VALUE);
        if (amount > 0 && above) {
            if (summary.dust.size() > 0) {
                summary.dust.get(0).decrement(1);
                amount -= DUST_VALUE;
            } else if (summary.shard.size() > 0) {
                summary.shard.get(0).decrement(1);
                amount -= DUST_VALUE*5;
            } else if (summary.crystal.size() > 0) {
                summary.shard.get(0).decrement(1);
                amount -= DUST_VALUE*10;
            }
        }
        return amount;
    }

    // Returns remaining
    private static int payWithItem(ArrayList<ItemStack> list, int amount, int valuePerItem) {
        for (var i=0; i < list.size(); i++) {
            ItemStack stack = list.get(i);
            int itemsRequired = amount/valuePerItem;
            if (itemsRequired <= 0) {
                return amount;
            }
            int consume = Math.min(stack.getCount(), itemsRequired);
            stack.decrement(consume);
            amount -= consume * valuePerItem;
            if (stack.isEmpty()) {
                list.remove(i);
                i--;
            }
        }
        return amount;
    }

    private static void payWithBatteries(ArrayList<ItemStack> list, int amount) {
        for (var i=0; i < list.size(); i++) {
            ItemStack stack = list.get(i);
            int amountInStack = VALUE_MAP.get(BATTERY_ID).apply(stack).getValue();
            if (amountInStack <= 0) {
                continue;
            }
            int consume = Math.min(amount, amountInStack);
            amount -= consume;
            setMediaInBatteryStack(stack, amountInStack - consume);
            if (amount <= 0) {
                return;
            }
        }
    }

    private static void setMediaInBatteryStack(ItemStack stack, int amount) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putInt("hexcasting:media", amount);
    }

    private static int mediaInBatteries(ArrayList<ItemStack> list) {
        MyINT count = new MyINT(0);
        list.forEach((stack) -> {
            count.add(VALUE_MAP.get(BATTERY_ID).apply(stack).multiply(stack.getCount()));
        });
        return count.getValue();
    }


    public static class InventoryMediaSummary {

        public ArrayList<ItemStack> dust = new ArrayList<>();
        public ArrayList<ItemStack> shard = new ArrayList<>();
        public ArrayList<ItemStack> crystal = new ArrayList<>();
        public ArrayList<ItemStack> battery = new ArrayList<>();
        public ArrayList<ItemStack> creative = new ArrayList<>();

        public InventoryMediaSummary(Inventory inv) {
            for (var i=0; i < inv.size(); i++) {
                ItemStack stack = inv.getStack(i);
                Identifier itemId = Registry.ITEM.getId(stack.getItem());
                if (itemId.equals(DUST_ID)) {
                    dust.add(stack);
                } else if (itemId.equals(SHARD_ID)) {
                    shard.add(stack);
                } else if (itemId.equals(CRYSTAL_ID)) {
                    crystal.add(stack);
                } else if (itemId.equals(BATTERY_ID)) {
                    battery.add(stack);
                } else if (itemId.equals(CREATIVE_ID)) {
                    creative.add(stack);
                }
            }
        }

    }

    // This is for handling the concept of infinity.
    public static class MyINT {
        public boolean infinite = false;
        public int value = 0;

        public static final MyINT INF = new MyINT(0);
        static {
            INF.infinite = true;
        }

        public MyINT(int i) {
            this.value = i;
        }

        public MyINT add(MyINT other) {
            this.value += other.value;
            this.infinite = this.infinite && other.infinite;
            return this;
        }

        public MyINT multiply(int i) {
            if (this.infinite) {
                return this;
            }
            this.value *= i;
            return this;
        }

        public int getValue() {
            if (this.infinite) {
                return Integer.MAX_VALUE;
            }
            return this.value;
        }
    }

}
