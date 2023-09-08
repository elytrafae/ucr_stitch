package com.cmdgod.mc.ucr_stitch.gui;

import com.cmdgod.mc.ucr_stitch.blockentities.GravityDuperBlockEntity;
import com.cmdgod.mc.ucr_stitch.tools.Utility;

import eu.pb4.sgui.api.gui.SimpleGui;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack.TooltipSection;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class GravityDuperGUI extends SimpleGui {

    GravityDuperBlockEntity gravityDuper;

    public GravityDuperGUI(ServerPlayerEntity player, GravityDuperBlockEntity gravityDuper) {
        super(ScreenHandlerType.GENERIC_9X3, player, false);
        this.gravityDuper = gravityDuper;
        
        setSlotRedirect(10, new FilteredInputSlot(gravityDuper, 0, (stack) -> {return Utility.isStackFuel(stack);}));
        setSlotRedirect(16, new FilteredInputSlot(gravityDuper, 1, (stack) -> {return false;}));

        updateProgress(0, 1, 0);
        
        ItemStack unusedStack = new ItemStack(Items.PURPLE_STAINED_GLASS_PANE);
        unusedStack.setCustomName(Text.of(""));
        for (int i=0; i < getVirtualSize(); i++) {
            if (getSlot(i) == null && getSlotRedirect(i) == null) {
                setSlot(i, unusedStack.copy());
            }
        }
    }

    private static final int PROGRESS_SLOT_COUNT = 5;
    public void updateProgress(int ticksLeft, int totalTime, int fuelTicks) {
        setSlotRedirect(10, new FilteredInputSlot(gravityDuper, 0, (stack) -> {return Utility.isStackFuel(stack);}));
        setSlotRedirect(16, new FilteredInputSlot(gravityDuper, 1, (stack) -> {return false;}));

        Text progressText = Text.of((totalTime-ticksLeft)/20 + "/" + totalTime/20 + " sec.").getWithStyle(Style.EMPTY.withItalic(false).withColor(Formatting.LIGHT_PURPLE)).get(0);
        int litUpSlots = (int)Math.ceil(((double)(totalTime-ticksLeft))*PROGRESS_SLOT_COUNT/totalTime);
        for (int i=0; i < PROGRESS_SLOT_COUNT; i++) {
            ItemStack stack = new ItemStack(i < litUpSlots ? Items.MAGENTA_STAINED_GLASS_PANE : Items.PINK_STAINED_GLASS_PANE);
            stack.setCustomName(progressText);
            setSlot(11+i, stack);
        }

        // Fuel
        ItemStack stack = new ItemStack(Items.BLAZE_POWDER);
        stack.setCustomName(Text.of("Fuel: " + fuelTicks/20 + " sec.").getWithStyle(Style.EMPTY.withItalic(false).withColor(Formatting.GOLD)).get(0));
        stack.addHideFlag(TooltipSection.ENCHANTMENTS);
        if (fuelTicks > 0) {
            stack.addEnchantment(Enchantments.AQUA_AFFINITY, 1);
        }
        setSlot(19, stack);
    }

    public void updateInvalid() {
        ItemStack invalidStack = new ItemStack(Items.BARRIER);
        invalidStack.setCustomName(Text.of("An error occured!").getWithStyle(Style.EMPTY.withItalic(false).withColor(Formatting.RED)).get(0));
        Utility.addLinesToLore(invalidStack, Text.of("Please break the block and make another Gravity Duper!").getWithStyle(Style.EMPTY.withItalic(false).withColor(Formatting.RED)).get(0));

        setSlot(10, invalidStack.copy());
        setSlot(16, invalidStack.copy());

        for (int i=0; i < PROGRESS_SLOT_COUNT; i++) {
            setSlot(11+i, invalidStack.copy());
        }

        // Fuel
        ItemStack fuelStack = new ItemStack(Items.COBWEB);
        fuelStack.setCustomName(Text.of(""));
        setSlot(19, fuelStack);
    }
    
}
