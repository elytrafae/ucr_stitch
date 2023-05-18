package com.cmdgod.mc.ucr_stitch.banners;

import com.cmdgod.mc.ucr_stitch.UCRStitch;

import net.minecraft.block.entity.BannerPattern;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class CustomBannerPatterns {
    
    public static final RegistryKey<BannerPattern> STARS = CustomBannerPatterns.of("stars");

    private static RegistryKey<BannerPattern> of(String id) {
        return RegistryKey.of(RegistryKeys.BANNER_PATTERN, new Identifier(UCRStitch.MOD_NAMESPACE, id));
    }

    public static BannerPattern registerAndGetDefault(Registry<BannerPattern> registry) {
        return Registry.register(registry, STARS, new BannerPattern(UCRStitch.MOD_NAMESPACE + "_stars"));
    }

}
