package com.cmdgod.mc.ucr_stitch.banners;

import com.cmdgod.mc.ucr_stitch.UCRStitch;

import net.minecraft.block.entity.BannerPattern;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CustomBannerTags {
    
    public static final TagKey<BannerPattern> STARS_PATTERN_ITEM = CustomBannerTags.of("pattern_item/stars");

    private static TagKey<BannerPattern> of(String id) {
        return TagKey.of(Registry.BANNER_PATTERN_KEY, new Identifier(UCRStitch.MOD_NAMESPACE, id));
    }

}
