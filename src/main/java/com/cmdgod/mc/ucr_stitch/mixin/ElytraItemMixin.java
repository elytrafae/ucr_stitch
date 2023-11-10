package com.cmdgod.mc.ucr_stitch.mixin;

import java.lang.Character.UnicodeScript;
import java.util.List;
import java.util.function.UnaryOperator;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
import com.cmdgod.mc.ucr_stitch.recipes.ElytraUpgradeRecipe;
import com.cmdgod.mc.ucr_stitch.tools.ElytraUpgradeUtil;
import com.cmdgod.mc.ucr_stitch.tools.Utility;

import io.github.apace100.apoli.power.PowerType;
import io.wispforest.owo.client.screens.ScreenInternals.Client;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Wearable;
import net.minecraft.world.World;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.text.TextContent;
import net.minecraft.util.Formatting;

@Mixin(ElytraItem.class)
public class ElytraItemMixin extends Item implements Wearable {

    // Constructor ignored!
    public ElytraItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        ElytraUpgradeRecipe recipe = ElytraUpgradeUtil.getUpgradeForElytra(stack, world);
        PowerType power = ElytraUpgradeUtil.getPowerFromRecipe(recipe);
        if (power == null) {
            return;
        }
        MutableText text = MutableText.of(TextContent.EMPTY);
        //text.append(Text.translatable("ucr_stitch.elytra_upgrade"));
        text.append(Utility.colorWithString(power.getName(), recipe.getNameTextColor()));
        tooltip.add(text);
        ElytraUpgradeUtil.DescriptionShowType showType = UCRStitch.CONFIG.descriptionShowType();
        if (showType == ElytraUpgradeUtil.DescriptionShowType.SHOW || (showType == ElytraUpgradeUtil.DescriptionShowType.SHIFT_SHOW && Screen.hasShiftDown())) {
            tooltip.add(Utility.colorWithString(power.getDescription(), recipe.getDescriptionTextColor()));
        }
    }
    
}
