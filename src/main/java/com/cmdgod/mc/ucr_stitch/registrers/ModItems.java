package com.cmdgod.mc.ucr_stitch.registrers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
import com.cmdgod.mc.ucr_stitch.items.CustomFishingRodItem;
import com.cmdgod.mc.ucr_stitch.items.GravityDuperPartItem;
import com.cmdgod.mc.ucr_stitch.items.Multitool;
import com.cmdgod.mc.ucr_stitch.items.OrbOfGreatRegret;
import com.cmdgod.mc.ucr_stitch.items.OreNecklace;
import com.cmdgod.mc.ucr_stitch.items.TooltippedItem;
import com.cmdgod.mc.ucr_stitch.items.VoidberryItem;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SignItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {

    private static ArrayList<Item> ITEMS = new ArrayList<>();
    public static HashMap<ToolMaterial, Multitool> MULTITOOLS = new HashMap<>();
    public static HashMap<ToolMaterial, CustomFishingRodItem> FISHING_RODS = new HashMap<>();
    public static HashMap<ToolMaterial, OreNecklace> ORE_NECKLACES = new HashMap<>();

    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder
            .create(new Identifier(UCRStitch.MOD_NAMESPACE, "misc_group"))
            .icon(() -> new ItemStack(Items.BARRIER))
            .build();

    /*
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
    */

    public static final Item GRAVITY_DUPER_TOP = new GravityDuperPartItem(new FabricItemSettings().group(ITEM_GROUP), GravityDuperPartItem.PartType.TOP);
    public static final Item GRAVITY_DUPER_BOTTOM = new GravityDuperPartItem(new FabricItemSettings().group(ITEM_GROUP), GravityDuperPartItem.PartType.BOTTOM);
    public static final Item GRAVITY_DUPER_SIDE = new GravityDuperPartItem(new FabricItemSettings().group(ITEM_GROUP), GravityDuperPartItem.PartType.SIDE);

    public static final Item HEAD_FRAGMENT = new TooltippedItem(new FabricItemSettings().group(ITEM_GROUP));
    public static final Item ORB_OF_GREAT_REGRET = new OrbOfGreatRegret(new FabricItemSettings().group(ITEM_GROUP).maxCount(16));

    public static final Item WOODEN_MULTITOOL = new Multitool(new FabricItemSettings().group(ITEM_GROUP), 
        (HoeItem)Items.WOODEN_HOE, 
        (ShovelItem)Items.WOODEN_SHOVEL,
        (AxeItem)Items.WOODEN_AXE,
        (PickaxeItem)Items.WOODEN_PICKAXE
    );

    public static final Item STONE_MULTITOOL = new Multitool(new FabricItemSettings().group(ITEM_GROUP), 
        (HoeItem)Items.STONE_HOE, 
        (ShovelItem)Items.STONE_SHOVEL,
        (AxeItem)Items.STONE_AXE,
        (PickaxeItem)Items.STONE_PICKAXE
    );

    public static final Item IRON_MULTITOOL = new Multitool(new FabricItemSettings().group(ITEM_GROUP), 
        (HoeItem)Items.IRON_HOE, 
        (ShovelItem)Items.IRON_SHOVEL,
        (AxeItem)Items.IRON_AXE,
        (PickaxeItem)Items.IRON_PICKAXE
    );

    public static final Item GOLDEN_MULTITOOL = new Multitool(new FabricItemSettings().group(ITEM_GROUP), 
        (HoeItem)Items.GOLDEN_HOE, 
        (ShovelItem)Items.GOLDEN_SHOVEL,
        (AxeItem)Items.GOLDEN_AXE,
        (PickaxeItem)Items.GOLDEN_PICKAXE
    );

    public static final Item DIAMOND_MULTITOOL = new Multitool(new FabricItemSettings().group(ITEM_GROUP), 
        (HoeItem)Items.DIAMOND_HOE, 
        (ShovelItem)Items.DIAMOND_SHOVEL,
        (AxeItem)Items.DIAMOND_AXE,
        (PickaxeItem)Items.DIAMOND_PICKAXE
    );

    public static final Item NETHERITE_MULTITOOL = new Multitool(new FabricItemSettings().group(ITEM_GROUP).fireproof(), 
        (HoeItem)Items.NETHERITE_HOE, 
        (ShovelItem)Items.NETHERITE_SHOVEL,
        (AxeItem)Items.NETHERITE_AXE,
        (PickaxeItem)Items.NETHERITE_PICKAXE
    );

    public static final CustomFishingRodItem STONE_FISHING_ROD = new CustomFishingRodItem(ToolMaterials.STONE, 0.25);
    public static final CustomFishingRodItem IRON_FISHING_ROD = new CustomFishingRodItem(ToolMaterials.IRON, 0.5);
    public static final CustomFishingRodItem GOLDEN_FISHING_ROD = new CustomFishingRodItem(ToolMaterials.GOLD, 0.25);
    public static final CustomFishingRodItem DIAMOND_FISHING_ROD = new CustomFishingRodItem(ToolMaterials.DIAMOND, 0.75);
    public static final CustomFishingRodItem NETHERITE_FISHING_ROD = new CustomFishingRodItem(ToolMaterials.NETHERITE, 1, new FabricItemSettings().fireproof());

    public static final OreNecklace AMETHYST_ORE_NECKLACE = new OreNecklace(new FabricItemSettings(), new Identifier("minecraft", "blocks/amethyst_cluster"));
    public static final OreNecklace COPPER_ORE_NECKLACE = new OreNecklace(new FabricItemSettings(), OreNecklace.getRegularOreTables("copper"));
    public static final OreNecklace IRON_ORE_NECKLACE = new OreNecklace(new FabricItemSettings(), OreNecklace.getRegularOreTables("iron"));
    public static final OreNecklace GOLD_ORE_NECKLACE = new OreNecklace(new FabricItemSettings(), OreNecklace.getRegularOreTables("gold"));
    public static final OreNecklace DIAMOND_ORE_NECKLACE = new OreNecklace(new FabricItemSettings(), OreNecklace.getRegularOreTables("diamond"));
    public static final OreNecklace EMERALD_ORE_NECKLACE = new OreNecklace(new FabricItemSettings(), OreNecklace.getRegularOreTables("emerald"));
    public static final OreNecklace LAPIS_ORE_NECKLACE = new OreNecklace(new FabricItemSettings(), OreNecklace.getRegularOreTables("lapis"));
    public static final OreNecklace REDSTONE_ORE_NECKLACE = new OreNecklace(new FabricItemSettings(), OreNecklace.getRegularOreTables("redstone"));
    public static final OreNecklace NETHER_QUARTZ_ORE_NECKLACE = new OreNecklace(new FabricItemSettings(), new Identifier("minecraft", "blocks/nether_quartz_ore"));
    public static final OreNecklace NETHERITE_ORE_NECKLACE = new OreNecklace(new FabricItemSettings().fireproof(), new Identifier("minecraft", "blocks/ancient_debris")).setDamagePerRoll(40);
    public static final OreNecklace SILVER_ORE_NECKLACE = new OreNecklace(new FabricItemSettings(), OreNecklace.getRegularOreTables("bewitchment", "silver"));
    

    public static final Item HEAVY_BOULDER = new TooltippedItem(new FabricItemSettings().maxCount(16).group(ITEM_GROUP));

    public static final Item VOIDBERRY = new VoidberryItem(new FabricItemSettings().group(ITEM_GROUP));

    public static void registerAll() {
        UCRStitch.LOGGER.info("UCR Stitch: Items registered!");
        //registerItem("edified_energy_bar", EDIFIED_ENERGY_BAR);
        //registerItem("edified_apple", EDIFIED_APPLE);

        registerItem("gravity_duper_top", GRAVITY_DUPER_TOP);
        registerItem("gravity_duper_bottom", GRAVITY_DUPER_BOTTOM);
        registerItem("gravity_duper_side", GRAVITY_DUPER_SIDE);

        registerItem("head_fragment", HEAD_FRAGMENT);
        registerItem("orb_of_great_regret", ORB_OF_GREAT_REGRET);
        registerItem("heavy_boulder", HEAVY_BOULDER);

        registerItem("wooden_multitool", WOODEN_MULTITOOL);
        registerItem("stone_multitool", STONE_MULTITOOL);
        registerItem("iron_multitool", IRON_MULTITOOL);
        registerItem("golden_multitool", GOLDEN_MULTITOOL);
        registerItem("diamond_multitool", DIAMOND_MULTITOOL);
        registerItem("netherite_multitool", NETHERITE_MULTITOOL);

        registerItem("stone_fishing_rod", STONE_FISHING_ROD);
        registerItem("iron_fishing_rod", IRON_FISHING_ROD);
        registerItem("golden_fishing_rod", GOLDEN_FISHING_ROD);
        registerItem("diamond_fishing_rod", DIAMOND_FISHING_ROD);
        registerItem("netherite_fishing_rod", NETHERITE_FISHING_ROD);

        registerItem("amethyst_necklace", AMETHYST_ORE_NECKLACE);
        registerItem("copper_necklace", COPPER_ORE_NECKLACE);
        registerItem("iron_necklace", IRON_ORE_NECKLACE);
        registerItem("gold_necklace", GOLD_ORE_NECKLACE);
        registerItem("diamond_necklace", DIAMOND_ORE_NECKLACE);
        registerItem("emerald_necklace", EMERALD_ORE_NECKLACE);
        registerItem("lapis_necklace", LAPIS_ORE_NECKLACE);
        registerItem("redstone_necklace", REDSTONE_ORE_NECKLACE);
        registerItem("nether_quartz_necklace", NETHER_QUARTZ_ORE_NECKLACE);
        registerItem("netherite_necklace", NETHERITE_ORE_NECKLACE);
        registerItem("silver_necklace", SILVER_ORE_NECKLACE);

        registerItem("voidberry", VOIDBERRY);
    }

    public static Item GRAVITY_DUPER_ITEM;
    public static Item VOIDSHROOM_CAP;
    public static Item VOIDBERRY_VINE;
    public static Item VOIDSHROOM_STEM;
    public static Item STRIPPED_VOIDSHROOM_STEM;
    public static Item VOIDSHROOM_SAPLING;
    public static Item VOIDSHROOM_PLANKS;
    public static Item VOIDSHROOM_HYPHAE;
    public static Item STRIPPED_VOIDSHROOM_HYPHAE;
    public static SignItem VOIDSHROOM_SIGN;
    public static Item TELEVATOR;

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
