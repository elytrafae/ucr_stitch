package com.cmdgod.mc.ucr_stitch.treestuff;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
import com.cmdgod.mc.ucr_stitch.blocks.VoidberryVine;
import com.cmdgod.mc.ucr_stitch.registrers.ModBlocks;
import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

public class VoidshroomTreeDecorator extends TreeDecorator {
    public static final VoidshroomTreeDecorator INSTANCE = new VoidshroomTreeDecorator();
    // Our constructor doesn't have any arguments, so we create a unit codec that returns the singleton instance
    public static final Codec<VoidshroomTreeDecorator> CODEC = Codec.unit(() -> INSTANCE);
 
    private VoidshroomTreeDecorator() {}
 
    @Override
    protected TreeDecoratorType<?> getType() {
        return UCRStitch.VOIDSHROOM_TREE_DECORATOR;
    }
 
    @Override
    public void generate(TreeDecorator.Generator generator) {
        // Iterate through block positions
        generator.getLeavesPositions().forEach(pos -> {
            Random random = generator.getRandom();
            // Pick a value from 0 (inclusive) to 4 (exclusive) and if it's 0, continue
            // This is the chance for spawning the gold block
            if (random.nextInt(2) == 0) {
                int maxLength = 0;
                BlockPos testPos = pos.offset(Direction.DOWN, 1);
                while (generator.getWorld().testBlockState(testPos, (blockState) -> {return blockState == Blocks.AIR.getDefaultState();})) {
                    maxLength++;
                    testPos = testPos.offset(Direction.DOWN, 1);
                }

                maxLength = Math.min(Math.max(maxLength, 0), 10);

                int fullBerries = Math.max((maxLength / 3) + random.nextBetween(0, 2), 0);
                int halfFullBerries = Math.max((maxLength / 3) + random.nextBetween(0, 2), 0);

                if (fullBerries + halfFullBerries > maxLength) {
                    fullBerries -= 1;
                    halfFullBerries -= 2;
                }

                for (var i=1; i <= maxLength; i++) {
                    BlockPos targetPosition = pos.offset(Direction.DOWN, i);
                    generator.replace(targetPosition, ModBlocks.VOIDBERRY_VINE.getDefaultState().with(VoidberryVine.FRUIT, i <= fullBerries ? 2 : (i <= halfFullBerries ? 1 : 0)));;
                }
            }
        });
    }
}