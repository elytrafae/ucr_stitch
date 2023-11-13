package com.cmdgod.mc.ucr_stitch.mixin;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
import com.cmdgod.mc.ucr_stitch.recipes.ElytraUpgradeRecipe;
import com.cmdgod.mc.ucr_stitch.tools.ElytraUpgradeUtil;
import com.cmdgod.mc.ucr_stitch.tools.Utility;

import io.github.apace100.apoli.power.PowerType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Wearable;
import net.minecraft.item.Item.Settings;
import net.minecraft.world.World;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;

@Mixin(Item.class)
public class ItemMixin {

    @Environment(EnvType.CLIENT)
    @Inject(method = "appendTooltip(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Ljava/util/List;Lnet/minecraft/client/item/TooltipContext;)V", at = @At("RETURN"))
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo info) {
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
