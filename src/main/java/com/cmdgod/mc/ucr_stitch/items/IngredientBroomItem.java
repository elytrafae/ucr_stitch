package com.cmdgod.mc.ucr_stitch.items;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
import com.cmdgod.mc.ucr_stitch.recipes.ElytraUpgradeRecipe;
import com.cmdgod.mc.ucr_stitch.registrers.ModItems;
import com.cmdgod.mc.ucr_stitch.tools.ElytraUpgradeUtil;
import com.cmdgod.mc.ucr_stitch.tools.Utility;

import io.github.apace100.apoli.power.PowerType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;

public class IngredientBroomItem extends Item {

    public IngredientBroomItem() {
        super(new FabricItemSettings().group(ModItems.ITEM_GROUP).maxCount(1));
    }
    
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.ucr_stitch.crafting_ingredient_desc"));
    }

}
