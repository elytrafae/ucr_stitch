package com.cmdgod.mc.ucr_stitch.registrers;

import java.util.ArrayList;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
import com.cmdgod.mc.ucr_stitch.items.GravityDuperPartItem;
import com.cmdgod.mc.ucr_stitch.items.OrbOfGreatRegret;
import com.cmdgod.mc.ucr_stitch.items.TooltippedItem;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemRegistrer {

    private static ArrayList<Item> ITEMS = new ArrayList<>();

    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder
            .create(new Identifier(UCRStitch.MOD_NAMESPACE, "misc_group"))
            .icon(() -> new ItemStack(Items.BARRIER))
            .build();

    public static Item EDIFIED_ENERGY_BAR = new Item(new FabricItemSettings().group(ITEM_GROUP).food(new FoodComponent.Builder()
            .hunger(1)
            .saturationModifier(6f)
            .alwaysEdible()
            .statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 1800, 0), 1)
            .statusEffect(new StatusEffectInstance(StatusEffects.HASTE, 1800, 0), 1)
            .build()));

    public static Item EDIFIED_APPLE = new Item(new FabricItemSettings().group(ITEM_GROUP).food(new FoodComponent.Builder()
            .hunger(6)
            .saturationModifier(1.2f)
            .alwaysEdible()
            .statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 2400, 1), 1)
            .statusEffect(new StatusEffectInstance(StatusEffects.HASTE, 1800, 0), 1)
            .statusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1), 1)
            .build()));

    public static final Item GRAVITY_DUPER_TOP = new GravityDuperPartItem(new FabricItemSettings().group(ITEM_GROUP), GravityDuperPartItem.PartType.TOP);
    public static final Item GRAVITY_DUPER_BOTTOM = new GravityDuperPartItem(new FabricItemSettings().group(ITEM_GROUP), GravityDuperPartItem.PartType.BOTTOM);
    public static final Item GRAVITY_DUPER_SIDE = new GravityDuperPartItem(new FabricItemSettings().group(ITEM_GROUP), GravityDuperPartItem.PartType.SIDE);

    public static final Item HEAD_FRAGMENT = new TooltippedItem(new FabricItemSettings().group(ITEM_GROUP));
    public static final Item ORB_OF_GREAT_REGRET = new OrbOfGreatRegret(new FabricItemSettings().group(ITEM_GROUP));

    public static void registerAll() {
        UCRStitch.LOGGER.info("UCR Stitch: Items registered!");
        registerItem("edified_energy_bar", EDIFIED_ENERGY_BAR);
        registerItem("edified_apple", EDIFIED_APPLE);

        registerItem("gravity_duper_top", GRAVITY_DUPER_TOP);
        registerItem("gravity_duper_bottom", GRAVITY_DUPER_BOTTOM);
        registerItem("gravity_duper_side", GRAVITY_DUPER_SIDE);

        registerItem("head_fragment", HEAD_FRAGMENT);
        registerItem("orb_of_great_regret", ORB_OF_GREAT_REGRET);
    }

    public static Item GRAVITY_DUPER_ITEM;

    public static void registerItem(String id, Item item) {
        Registry.register(Registry.ITEM, new Identifier(UCRStitch.MOD_NAMESPACE, id), item);
        ITEMS.add(item);
    }

    private static void populateCreativeTab() {
        /*
        ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP).register(content -> {
            ITEMS.forEach((item) -> {
                content.add(item);
            });
        });
        */
    }

}
