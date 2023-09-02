package com.cmdgod.mc.ucr_stitch.tools;

import java.util.ArrayList;
import java.util.Set;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class GetAllItems {
    
    private static ArrayList<Identifier> getItemKeysList() {
        Set<Identifier> s = Registry.ITEM.getIds(); 
        int n = s.size();
        ArrayList<Identifier> aList = new ArrayList<Identifier>(n);
        for (Identifier x : s) {
            if (x.getNamespace() != "minecraft") {
                aList.add(x);
            }
        }
        return aList;
    }

    private static String getEnglishDisplayName(Identifier itemId) {
        Item item = Registry.ITEM.get(itemId);
        String trKey = item.getTranslationKey();
        //Text text = Text.translatable(trKey);
        return TranslationStorage.getInstance().get(trKey);
    }

    private static String getTexture(Identifier itemId) {
        AbstractTexture texture = MinecraftClient.getInstance().getTextureManager().getTexture(itemId);
        return "";
    }

    private static ArrayList<ItemJson> keysToItemJson(ArrayList<Identifier> aList) {
        ArrayList<ItemJson> iList = new ArrayList<ItemJson>(aList.size());
        for (Identifier x : aList) {
            ItemJson i = new ItemJson();
            i.id = x;
            i.name = getEnglishDisplayName(i.id);
            iList.add(i);
        }
        return iList;
    }

    public static ArrayList<ItemJson> getJsonArrayList() {
        return keysToItemJson(getItemKeysList());
    }

    private static class ItemJson {
        public Identifier id;
        public String name;
        public String texture = "";

        @Override
        public String toString() {
            return "{ \"id\": \"" + id.toString() + "\", \"name\": \"" + name + "\", \"texture\": \"" + texture + "\" }";
        }
    }

}
