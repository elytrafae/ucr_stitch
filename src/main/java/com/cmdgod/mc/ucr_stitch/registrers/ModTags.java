package com.cmdgod.mc.ucr_stitch.registrers;

import com.cmdgod.mc.ucr_stitch.UCRStitch;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModTags {

    public static class Blocks {

        public static final TagKey<Block> ALL_CARPETS = createTag("all_carpets");

        private static TagKey<Block> createTag(String name) {
            return TagKey.of(Registry.BLOCK_KEY, new Identifier(UCRStitch.MOD_NAMESPACE, name));
        }

    }
    
    public static class Items {

        public static final TagKey<Item> WATER_VULNERABLE = createTag("water_vulnerable");
        public static final TagKey<Item> VOID_BOUNCING = createTag("void_bouncing");
        public static final TagKey<Item> FISHING_RODS = createTag("fishing_rods");
        public static final TagKey<Item> ALL_BOOKS = createTag("all_books");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(Registry.ITEM_KEY, new Identifier(UCRStitch.MOD_NAMESPACE, name));
        }

    }

}
