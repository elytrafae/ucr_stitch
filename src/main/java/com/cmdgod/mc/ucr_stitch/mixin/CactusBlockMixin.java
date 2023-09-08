package com.cmdgod.mc.ucr_stitch.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.cmdgod.mc.ucr_stitch.UCRStitch;

import net.minecraft.block.BlockState;
import net.minecraft.block.CactusBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

@Mixin(CactusBlock.class)
public class CactusBlockMixin {

    @Inject(at = @At("HEAD"), method = "randomTick(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/random/Random;)V", cancellable = true)
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo info) {
        CactusBlock block = (CactusBlock)(Object)this;
        BlockPos blockPos = pos.up();
        if (world.isAir(blockPos)) {
            int i;
            for (i = 1; world.getBlockState(pos.down(i)).isOf(block); ++i) {
            }

            if (i < UCRStitch.CONFIG.cactusHeight()) {
                int j = (Integer) state.get(CactusBlock.AGE);
                if (j == 15) {
                    world.setBlockState(blockPos, block.getDefaultState());
                    BlockState blockState = (BlockState) state.with(CactusBlock.AGE, 0);
                    world.setBlockState(pos, blockState, 4);
                    world.updateNeighbor(blockState, blockPos, block, pos, false);
                } else {
                    world.setBlockState(pos, (BlockState) state.with(CactusBlock.AGE, j + 1), 4);
                }

            }
        }
        info.cancel();
    }
    

}
