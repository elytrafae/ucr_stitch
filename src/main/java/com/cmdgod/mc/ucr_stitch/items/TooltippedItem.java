package com.cmdgod.mc.ucr_stitch.items;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class TooltippedItem extends Item {

    private int tooltipRows;

     public TooltippedItem(Settings settings, int tr) {
        super(settings);
        tooltipRows = tr;
    }

    public TooltippedItem(Settings settings) {
        this(settings, 1);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (tooltipRows == 1) {
            tooltip.add(Text.translatable(getTranslationKey(stack) + ".desc"));
            return;
        }
        for (int i=1; i <= tooltipRows; i++) {
            tooltip.add(Text.translatable(getTranslationKey(stack) + ".desc" + i));
        }
    }
    
}
