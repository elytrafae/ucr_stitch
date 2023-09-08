package com.cmdgod.mc.ucr_stitch.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.cmdgod.mc.ucr_stitch.UCRStitch;

import net.minecraft.block.BlockState;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

@Mixin(SugarCaneBlock.class)
public class SugarCaneBlockMixin {

    @Inject(at = @At("HEAD"), method = "randomTick(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/random/Random;)V", cancellable = true)
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo info) {
        SugarCaneBlock block = (SugarCaneBlock)(Object)this;
        if (world.isAir(pos.up())) {
            int i;
            for (i = 1; world.getBlockState(pos.down(i)).isOf(block); ++i) {
            }

            if (i < UCRStitch.CONFIG.sugarcaneHeight()) {
                int j = (Integer) state.get(SugarCaneBlock.AGE);
                if (j == 15) {
                    world.setBlockState(pos.up(), block.getDefaultState());
                    world.setBlockState(pos, (BlockState) state.with(SugarCaneBlock.AGE, 0), 4);
                } else {
                    world.setBlockState(pos, (BlockState) state.with(SugarCaneBlock.AGE, j + 1), 4);
                }
            }
        }
        info.cancel();
    }

}
