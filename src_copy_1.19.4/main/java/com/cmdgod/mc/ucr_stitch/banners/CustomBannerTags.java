package com.cmdgod.mc.ucr_stitch.banners;

import com.cmdgod.mc.ucr_stitch.UCRStitch;

import net.minecraft.block.entity.BannerPattern;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class CustomBannerTags {
    
    public static final TagKey<BannerPattern> STARS_PATTERN_ITEM = CustomBannerTags.of("pattern_item/stars");

    private static TagKey<BannerPattern> of(String id) {
        return TagKey.of(RegistryKeys.BANNER_PATTERN, new Identifier(UCRStitch.MOD_NAMESPACE, id));
    }

}
