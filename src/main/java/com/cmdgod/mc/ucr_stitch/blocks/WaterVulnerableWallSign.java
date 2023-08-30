package com.cmdgod.mc.ucr_stitch.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallSignBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.SignType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class WaterVulnerableWallSign extends WallSignBlock {

    public WaterVulnerableWallSign(Settings settings, SignType signType) {
        super(settings, signType);
        //TODO Auto-generated constructor stub
    }
    
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        world.createAndScheduleBlockTick(pos, (Block) this, WaterVulnerableBlock.EVAPORATE_DELAY);
        super.onBlockAdded(state, world, pos, oldState, notify);
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        world.createAndScheduleBlockTick(pos, (Block) this, WaterVulnerableBlock.EVAPORATE_DELAY);
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        boolean adjacentFluid = state.get(WATERLOGGED);

        // Only run further checks if the block is not waterlogged
        if (!adjacentFluid) {
            for (Direction dir : Direction.values()) {
                if (dir == Direction.DOWN) {continue;}
                BlockPos newPos = pos.add(dir.getVector());
                FluidState fState = world.getFluidState(newPos);
                if (fState.isOf(Fluids.WATER) || fState.isOf(Fluids.FLOWING_WATER)) {
                    adjacentFluid = true;
                    break;
                }
            }
        }
        
        if (adjacentFluid) {
            world.breakBlock(pos, true);
        }
        
    }

}
