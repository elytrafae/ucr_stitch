package com.cmdgod.mc.ucr_stitch.items;

import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.cmdgod.mc.ucr_stitch.registrers.ModAttributes;
import com.cmdgod.mc.ucr_stitch.registrers.ModItems;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.AnvilBlock;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
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

    // Modified version of the attack speed modifier UUID.
    protected static final UUID BONUS_FISH_CHANCE_MODIFIER_ID = UUID.fromString("FA222E1C-4180-4865-B01B-BCCE6985ACA3");

    ToolMaterial material;
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    public CustomFishingRodItem(ToolMaterial material, double doubleChance) {
        this(material, doubleChance, new Item.Settings());
    }

    public static int calcDurability(ToolMaterial material) {
        return (int)(((float)material.getDurability())*64.0/ToolMaterials.WOOD.getDurability());
    }

    // This was mainly made so that stuff like "fireproof" can be added
    public CustomFishingRodItem(ToolMaterial material, double doubleChance, Item.Settings baseSettings) {
        super(baseSettings.maxDamage(calcDurability(material)).group(ModItems.ITEM_GROUP));
        this.material = material;
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(ModAttributes.GENERIC_BONUS_FISH_CHANCE, new EntityAttributeModifier(BONUS_FISH_CHANCE_MODIFIER_ID, "Fish chance modifier", doubleChance, EntityAttributeModifier.Operation.MULTIPLY_BASE));
        this.attributeModifiers = builder.build();
        ModItems.FISHING_RODS.put(material, this);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            return this.attributeModifiers;
        }
        return super.getAttributeModifiers(slot);
    }

    @Override
    public int getEnchantability() {
        return material.getEnchantability()/5;
    }
    
    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return this.material.getRepairIngredient().test(ingredient) || super.canRepair(stack, ingredient);
    }

    /*
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (countData.guaranteed > 1) {
            tooltip.add(Text.translatable("tooltip.ucr_stitch.fishing_rods.guaranteed", countData.guaranteed).formatted(Formatting.DARK_BLUE));
        }
        if (countData.chance > 0) {
            tooltip.add(Text.translatable("tooltip.ucr_stitch.fishing_rods.chance", countData.chance).formatted(Formatting.DARK_AQUA));
        }
    }
    */

}
