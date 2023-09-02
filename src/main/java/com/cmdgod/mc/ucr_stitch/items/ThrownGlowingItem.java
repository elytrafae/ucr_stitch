package com.cmdgod.mc.ucr_stitch.items;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;

public interface ThrownGlowingItem {
    
    public default int getGlowColor(ItemStack stack, ItemEntity entity) { 
        // -1 means no glow
        return -1;
    };

}
