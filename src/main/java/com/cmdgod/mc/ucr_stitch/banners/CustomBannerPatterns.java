package com.cmdgod.mc.ucr_stitch.banners;

import com.cmdgod.mc.ucr_stitch.UCRStitch;

import net.minecraft.block.entity.BannerPattern;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class CustomBannerPatterns {
    
    public static final RegistryKey<BannerPattern> STARS = CustomBannerPatterns.of("stars");

    private static RegistryKey<BannerPattern> of(String id) {
        return RegistryKey.of(Registry.BANNER_PATTERN_KEY, new Identifier(UCRStitch.MOD_NAMESPACE, id));
    }

    public static BannerPattern registerAndGetDefault(Registry<BannerPattern> registry) {
        return Registry.register(registry, STARS, new BannerPattern(UCRStitch.MOD_NAMESPACE + "_stars"));
    }

}
