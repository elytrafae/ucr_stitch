package com.cmdgod.mc.ucr_stitch.worldgen;

import com.cmdgod.mc.ucr_stitch.blocks.VoidberryVine;
import com.cmdgod.mc.ucr_stitch.registrers.ModBlocks;
import com.mojang.serialization.Codec;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class VoidberryVineFeature extends Feature<VoidberryVineFeatureConfig> {


    public VoidberryVineFeature(Codec<VoidberryVineFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<VoidberryVineFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        // the origin is the place where the game starts trying to place the feature
        BlockPos origin = context.getOrigin();
        // we won't use the random here, but we could if we wanted to
        Random random = context.getRandom();
        VoidberryVineFeatureConfig config = context.getConfig();
 
        // don't worry about where these come from-- we'll implement these methods soon
        boolean isPlanted = config.isPlanted();
 
        // find a good surface
        BlockPos testPos = new BlockPos(origin).withY(world.getBottomY());
        int height = 0;
        while (testPos.getY() < world.getTopY() && world.getBlockState(testPos).isOf(Blocks.AIR)) {
            height++;
            testPos = testPos.up();
        }

        if (height < 6 || height > 20 || !world.getBlockState(testPos).isOf(Blocks.END_STONE)) {
            return false;
        }

        int doubleFruitCount = isPlanted ? 0 : height/3;
        int singleFruitCount = isPlanted ? height/2 : height/3;

        for (int i=0; i < height; i++) {
            testPos = testPos.down();
            int fruits = i < doubleFruitCount ? 2 : (i < (doubleFruitCount + singleFruitCount) ? 1 : 0);
            world.setBlockState(testPos, ModBlocks.VOIDBERRY_VINE.getDefaultState().with(VoidberryVine.FRUIT, fruits), Block.NOTIFY_ALL);
        }
        return true;
    }
    
}
