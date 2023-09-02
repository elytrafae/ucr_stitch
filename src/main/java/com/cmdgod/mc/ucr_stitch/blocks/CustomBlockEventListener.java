package com.cmdgod.mc.ucr_stitch.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface CustomBlockEventListener {
    
    public default void onFirstSneakTick(World world, BlockPos pos, BlockState state, PlayerEntity player) {};
    public default boolean onFirstJumpTick(World world, BlockPos pos, BlockState state, LivingEntity player) {return false;};

}
