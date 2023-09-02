package com.cmdgod.mc.ucr_stitch.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.gen.feature.FeatureConfig;

public record VoidberryVineFeatureConfig(boolean isPlanted) implements FeatureConfig {
    public VoidberryVineFeatureConfig(boolean isPlanted) {
        this.isPlanted = isPlanted;
    }
 
    public static Codec<VoidberryVineFeatureConfig> CODEC = RecordCodecBuilder.create(
        instance ->
                instance.group(
                        // you can add as many of these as you want, one for each parameter
                        Codec.BOOL.fieldOf("blockID").forGetter(VoidberryVineFeatureConfig::blockID))
                .apply(instance, VoidberryVineFeatureConfig::new));
 
    public boolean blockID() {
        return isPlanted;
    }

}