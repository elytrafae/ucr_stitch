package com.cmdgod.mc.ucr_stitch.items;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.cmdgod.mc.ucr_stitch.registrers.ModItems;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.AnvilBlock;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.text.Text;

public class CustomFishingRodItem extends FishingRodItem {

    ToolMaterial material;
    FishCountData countData;

    public CustomFishingRodItem(ToolMaterial material, int doubleChance) {
        this(material, doubleChance, new Item.Settings());
    }

    public static int calcDurability(ToolMaterial material) {
        return (int)(((float)material.getDurability())*64.0/ToolMaterials.WOOD.getDurability());
    }

    // This was mainly made so that stuff like "fireproof" can be added
    public CustomFishingRodItem(ToolMaterial material, int doubleChance, Item.Settings baseSettings) {
        super(baseSettings.maxDamage(calcDurability(material)).group(ModItems.ITEM_GROUP));
        this.material = material;
        this.countData = getFishCountData(doubleChance);
        ModItems.FISHING_RODS.put(material, this);
    }

    @Override
    public int getEnchantability() {
        return material.getEnchantability()/5;
    }
    
    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return this.material.getRepairIngredient().test(ingredient) || super.canRepair(stack, ingredient);
    }

    private FishCountData getFishCountData(int chanceNr) {
        int nr = 1 + (chanceNr/100);
        int remainingChance = chanceNr - (nr-1)*100;
        return new FishCountData(nr, remainingChance);
    }

    public int getFishCount(Random random) {
        int nr = 0;
        if (countData.chance > 0 && countData.chance > random.nextInt(100)) {
            nr++;
        }
        return countData.guaranteed + nr;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (countData.guaranteed > 1) {
            tooltip.add(Text.translatable("tooltip.ucr_stitch.fishing_rods.guaranteed", countData.guaranteed).formatted(Formatting.DARK_BLUE));
        }
        if (countData.chance > 0) {
            tooltip.add(Text.translatable("tooltip.ucr_stitch.fishing_rods.chance", countData.chance).formatted(Formatting.DARK_AQUA));
        }
    }

    class FishCountData {
        public int guaranteed = 0;
        public int chance = 0;
        public FishCountData(int g, int c) {
            guaranteed = g;
            chance = c;
        }
    }

}
