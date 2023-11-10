package com.cmdgod.mc.ucr_stitch.tools;

import java.util.List;
import java.util.Locale;

import com.cmdgod.mc.ucr_stitch.mixininterfaces.IPlayerEntityMixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.text.Texts;
import net.minecraft.text.Text.Serializer;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Utility {
    
    public static void addLinesToLore(ItemStack stack, Text ...text) {
        if (stack.isEmpty()) {
            return;
        }
        NbtCompound nbt = stack.getOrCreateNbt();
        NbtCompound displayNbt;
        NbtList loreList;
        if (nbt.contains("display", 10)) {
            displayNbt = nbt.getCompound("display");
        } else {
            displayNbt = new NbtCompound();
            nbt.put("display", displayNbt);
        }
        if (displayNbt.getType("Lore") == 9) {
            loreList = displayNbt.getList("Lore", 8);
        } else {
            loreList = new NbtList();
            displayNbt.put("Lore", loreList);
        }
        for (int i=0; i < text.length; i++) {
            loreList.add(NbtString.of(Serializer.toJson(text[i])));
        }
    }

    public static void playSound(PlayerEntity player, SoundEvent event, float volume, float pitch) {
        Vec3d pos = player.getPos();
        player.getWorld().playSound(null, pos.x, pos.y, pos.z, event, SoundCategory.PLAYERS, volume, pitch);
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

    public static IPlayerEntityMixin getInterfacePlayer(PlayerEntity player) {
        return (IPlayerEntityMixin)(Object)player;
    }

    public static boolean canHarmEachOther(PlayerEntity p1, PlayerEntity p2) {
        return p1 == null || p2 == null || p1.equals(p2) || (getInterfacePlayer(p1).getPVPStatus() && getInterfacePlayer(p2).getPVPStatus());
    }

    public static Text addStyle(Text text, Style style) {
        return text.getWithStyle(style).get(0);
    }

    @Environment(EnvType.CLIENT)
    public static AbstractClientPlayerEntity getMainPlayer(World world) {
        if (world instanceof ClientWorld) {
            ClientWorld clientWorld = (ClientWorld) world;
            AbstractClientPlayerEntity mainPlayer = null;
            List<AbstractClientPlayerEntity> players = clientWorld.getPlayers();
            for (int i=0; i < players.size(); i++) {
                AbstractClientPlayerEntity p = players.get(i);
                if (p.isMainPlayer()) {
                    mainPlayer = p;
                }
            }
            return mainPlayer;
        }
        return null;
    }

    public static MutableText colorWithString(MutableText text, String string) {
        if (string == null || string == "") {
            return text;
        }
        return text.styled((style) -> {return style.withColor(TextColor.parse(string));});
    }

    public static String stringOr(String main, String ifNone) {
        if (main == null || main == "") {
            return ifNone;
        }
        return main;
    }

}
