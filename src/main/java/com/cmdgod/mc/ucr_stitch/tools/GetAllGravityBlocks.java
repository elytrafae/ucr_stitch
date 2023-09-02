package com.cmdgod.mc.ucr_stitch.tools;

import java.util.ArrayList;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.FallingBlock;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class GetAllGravityBlocks {
    
    public static String getAllGravityBlocks() {
        ArrayList<Identifier> gravityBlocks = new ArrayList<>();
        Set<Identifier> ids = Registry.BLOCK.getIds();
        for (Identifier id : ids) {
            Block block = Registry.BLOCK.get(id);
            if (block instanceof FallingBlock) {
                gravityBlocks.add(id);
            }
        }
        return gravityBlocks.toString();
    }

}
