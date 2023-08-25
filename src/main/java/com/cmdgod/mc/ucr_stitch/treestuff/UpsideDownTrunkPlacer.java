package com.cmdgod.mc.ucr_stitch.treestuff;

import java.util.List;
import java.util.function.BiConsumer;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;

public class UpsideDownTrunkPlacer extends TrunkPlacer {
    // Use the fillTrunkPlacerFields to create our codec
    public static final Codec<UpsideDownTrunkPlacer> CODEC = RecordCodecBuilder.create(instance -> 
        fillTrunkPlacerFields(instance).apply(instance, UpsideDownTrunkPlacer::new));
 
    public UpsideDownTrunkPlacer(int baseHeight, int firstRandomHeight, int secondRandomHeight) {
        super(baseHeight, firstRandomHeight, secondRandomHeight);
    }
 
    @Override
    protected TrunkPlacerType<?> getType() {
        return UCRStitch.UPSIDE_DOWN_TRUNK_PLACER;
    }
 
    @Override
    public List<FoliagePlacer.TreeNode> generate(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, int height, BlockPos startPos, TreeFeatureConfig config) {
        StraightTrunkPlacer.setToDirt(world, replacer, random, startPos.up(), config);
        for (int i = 0; i < height; ++i) {
            this.getAndSetState(world, replacer, random, startPos.down(i), config);
        }
        return ImmutableList.of(new FoliagePlacer.TreeNode(startPos.down(height), 0, false));
    }
}