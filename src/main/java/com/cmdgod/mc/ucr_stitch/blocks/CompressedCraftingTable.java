package com.cmdgod.mc.ucr_stitch.blocks;

import com.cmdgod.mc.ucr_stitch.blockentities.CompressedCraftingTableEntity;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class CompressedCraftingTable extends BlockWithEntity {

    public CompressedCraftingTable() {
        super(FabricBlockSettings.of(Material.BAMBOO).strength(4.0f));
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CompressedCraftingTableEntity(pos, state);
    }
    
}
