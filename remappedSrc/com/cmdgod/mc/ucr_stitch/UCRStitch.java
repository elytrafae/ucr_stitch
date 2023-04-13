package com.cmdgod.mc.ucr_stitch;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registry;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmdgod.mc.ucr_stitch.blockentities.CompressedCraftingTableEntity;
import com.cmdgod.mc.ucr_stitch.blocks.CompressedCraftingTable;

public class UCRStitch implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_NAMESPACE = "ucr_stitch";
	public static final Logger LOGGER = LoggerFactory.getLogger("ucr_stitch");

	static public Item HEAD_FRAGMENT = new Item(new Item.Settings().group(ItemGroup.MISC).rarity(Rarity.COMMON).maxCount(64));
	static public Block COMPRESSED_CRAFTING_TABLE = new CompressedCraftingTable();

	public static final BlockEntityType<CompressedCraftingTableEntity> COMPRESSED_CRAFTING_TABLE_ENTITY = Registry.register(
        Registry.BLOCK_ENTITY_TYPE,
        new Identifier(MOD_NAMESPACE, "compressed_crafting_table_entity"),
        FabricBlockEntityTypeBuilder.create(CompressedCraftingTableEntity::new, COMPRESSED_CRAFTING_TABLE).build()
    );

	@Override
	public void onInitialize() {
		Registries.register(Registry.ITEM, new Identifier(MOD_NAMESPACE, "head_fragment"), HEAD_FRAGMENT);
		Registry.register(Registry.BLOCK, new Identifier(MOD_NAMESPACE, "compressed_crafting_table"), COMPRESSED_CRAFTING_TABLE);
		Registry.register(Registry.ITEM, new Identifier(MOD_NAMESPACE, "compressed_crafting_table"), new BlockItem(COMPRESSED_CRAFTING_TABLE, new FabricItemSettings()));

		for (DyeColor color : DyeColor.values()) {
			LOGGER.info(color.getName());
		}
	}
}