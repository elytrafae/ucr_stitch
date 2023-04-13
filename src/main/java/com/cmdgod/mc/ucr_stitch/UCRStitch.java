package com.cmdgod.mc.ucr_stitch;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.datafixer.fix.BedItemColorFix;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BundleItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.condition.LootConditionTypes;
import net.minecraft.loot.condition.RandomChanceWithLootingLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.LootNumberProviderTypes;
import net.minecraft.recipe.ArmorDyeRecipe;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmdgod.mc.ucr_stitch.blockentities.CompressedCraftingTableEntity;
import com.cmdgod.mc.ucr_stitch.blocks.CompressedCraftingTable;
import com.cmdgod.mc.ucr_stitch.recipes.HeadFragmentCraftRecipe;

public class UCRStitch implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_NAMESPACE = "ucr_stitch";
	public static final Logger LOGGER = LoggerFactory.getLogger("ucr_stitch");

	static public ArrayList<Item> ITEMS = new ArrayList<Item>(); // For automatically adding everything to the Creative Menu.

	static public Item HEAD_FRAGMENT = new Item(new FabricItemSettings().rarity(Rarity.COMMON).maxCount(64));
	static public Block COMPRESSED_CRAFTING_TABLE = new CompressedCraftingTable();
	//static public Item COMPRESSED_CRAFTING_TABLE_ITEM = new BlockItem(COMPRESSED_CRAFTING_TABLE, new FabricItemSettings());
	static public HashMap<DyeColor, Item> BUNDLES = new HashMap<DyeColor, Item>();

	public static final BlockEntityType<CompressedCraftingTableEntity> COMPRESSED_CRAFTING_TABLE_ENTITY = Registry.register(
        Registries.BLOCK_ENTITY_TYPE,
        new Identifier(MOD_NAMESPACE, "compressed_crafting_table_entity"),
        FabricBlockEntityTypeBuilder.create(CompressedCraftingTableEntity::new, COMPRESSED_CRAFTING_TABLE).build()
    );

	private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder(new Identifier(MOD_NAMESPACE, "misc_group")).icon(() -> new ItemStack(HEAD_FRAGMENT)).build();

	@Override
	public void onInitialize() {
		LOGGER.info("UCRStitch Initialized!");

		registerAndAddToCreativeMenu(HEAD_FRAGMENT, "head_fragment");
		registerAndAddToCreativeMenu(COMPRESSED_CRAFTING_TABLE, "compressed_crafting_table");

		for (DyeColor color : DyeColor.values()) {
			BundleItem bundle = new BundleItem(new FabricItemSettings().rarity(Rarity.COMMON).maxCount(1));
			BUNDLES.put(color, bundle);
			registerAndAddToCreativeMenu(bundle, color.getName() + "_bundle");
		}

		Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(MOD_NAMESPACE, "head_fragment"), HeadFragmentCraftRecipe.Serializer.INSTANCE);

		populateCreativeTab();
		modifyLootTables();
	}

	public void registerAndAddToCreativeMenu(Item item, String id) {
		ITEMS.add(item);
		Registry.register(Registries.ITEM, new Identifier(MOD_NAMESPACE, id), item);
	}

	public void registerAndAddToCreativeMenu(Block block, String id) {
		Registry.register(Registries.BLOCK, new Identifier(MOD_NAMESPACE, id), block);
		registerAndAddToCreativeMenu(new BlockItem(block, new FabricItemSettings()), id);
	}

	public void populateCreativeTab() {
		ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP).register(content -> {
			ITEMS.forEach((item) -> {
				content.add(item);
			});
		});
	}

	public void modifyLootTables() {
		Identifier ZOMBIE_LOOT_DROP_ID = new Identifier("minecraft:entities/zombie");

		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			// Let's only modify built-in loot tables and leave data pack loot tables untouched by checking the source.
			// We also check that the loot table ID is equal to the ID we want.
			if (source.isBuiltin() && ZOMBIE_LOOT_DROP_ID.equals(id)) {
				LootPool.Builder poolBuilder = LootPool.builder()
												.with(ItemEntry.builder(Items.ZOMBIE_HEAD).weight(1))
												.conditionally(RandomChanceWithLootingLootCondition.builder(0.05f, 0.02f));
 
        		tableBuilder.pool(poolBuilder);
			}
		});
	}
}