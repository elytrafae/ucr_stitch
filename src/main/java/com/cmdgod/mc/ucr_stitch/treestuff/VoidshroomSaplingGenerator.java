package com.cmdgod.mc.ucr_stitch.treestuff;

import org.jetbrains.annotations.Nullable;

import com.cmdgod.mc.ucr_stitch.UCRStitch;

import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

public class VoidshroomSaplingGenerator extends SaplingGenerator {
  @Nullable
  @Override
  protected RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> getTreeFeature(Random random, boolean bees) {
    return UCRStitch.VOIDSHROOM_TREE;
  }
}
