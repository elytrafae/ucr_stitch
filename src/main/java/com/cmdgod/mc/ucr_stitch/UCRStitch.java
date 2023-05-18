package com.cmdgod.mc.ucr_stitch;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.LoomBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPatterns;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.gui.screen.ingame.LoomScreen;
import net.minecraft.client.render.block.entity.BannerBlockEntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BannerPatternItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BundleItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceWithLootingLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BannerPatternTags;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.ArrayList;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmdgod.mc.ucr_stitch.banners.CustomBannerPatterns;
import com.cmdgod.mc.ucr_stitch.banners.CustomBannerTags;
import com.cmdgod.mc.ucr_stitch.blockentities.CompressedCraftingTableEntity;
import com.cmdgod.mc.ucr_stitch.blockentities.SpreaderBlockEntity;
import com.cmdgod.mc.ucr_stitch.blocks.CompressedCraftingTable;
import com.cmdgod.mc.ucr_stitch.blocks.SpreaderBlock;
import com.cmdgod.mc.ucr_stitch.recipes.BundleRecolorRecipe;
import com.cmdgod.mc.ucr_stitch.recipes.HeadFragmentCraftRecipe;
import com.cmdgod.mc.ucr_stitch.villagers.VillagersRegister;

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

	static public Block SPREADER_BLOCK = new SpreaderBlock();
	public static final BlockEntityType<SpreaderBlockEntity> SPREADER_BLOCK_ENTITY = Registry.register(
        Registries.BLOCK_ENTITY_TYPE,
        new Identifier(MOD_NAMESPACE, "spreader_block_entity"),
        FabricBlockEntityTypeBuilder.create(SpreaderBlockEntity::new, COMPRESSED_CRAFTING_TABLE).build()
    );

	public static final Item ESSENCE_FOOD_MATTER = new Item(new FabricItemSettings().rarity(Rarity.COMMON).maxCount(64));

	public static final Item ESSENCE_BAR = new Item(new FabricItemSettings().rarity(Rarity.COMMON).maxCount(32).food(
		new FoodComponent.Builder()
			.hunger(1)
			.saturationModifier(6f)
			.alwaysEdible()
			.statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 1800, 0), 1)
			.statusEffect(new StatusEffectInstance(StatusEffects.HASTE, 1800, 0), 1)
			.build()
	));

	public static final Item ESSENCE_APPLE = new Item(new FabricItemSettings().rarity(Rarity.COMMON).maxCount(16).food(
		new FoodComponent.Builder()
			.hunger(6)
			.saturationModifier(1.2f)
			.alwaysEdible()
			.statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 2400, 1), 1)
			.statusEffect(new StatusEffectInstance(StatusEffects.HASTE, 1800, 0), 1)
			.statusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1), 1)
			.build()
	));

	public static final Block WOODCUTTER_BLOCK = new Block(FabricBlockSettings.of(Material.WOOD, MapColor.SPRUCE_BROWN).strength(2));

	private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder(new Identifier(MOD_NAMESPACE, "misc_group")).icon(() -> new ItemStack(HEAD_FRAGMENT)).build();

	@Override
	public void onInitialize() {
		LOGGER.info("UCRStitch Initialized!");

		registerAndAddToCreativeMenu(HEAD_FRAGMENT, "head_fragment");
		registerAndAddToCreativeMenu(COMPRESSED_CRAFTING_TABLE, "compressed_crafting_table");
		registerAndAddToCreativeMenu(ESSENCE_FOOD_MATTER, "essence_food_matter");
		registerAndAddToCreativeMenu(ESSENCE_BAR, "essence_bar");
		registerAndAddToCreativeMenu(ESSENCE_APPLE, "essence_apple");
		registerAndAddToCreativeMenu(SPREADER_BLOCK, "spreader");
		registerAndAddToCreativeMenu(WOODCUTTER_BLOCK, "woodcutter");

		registerAndAddToCreativeMenu(new BannerPatternItem(CustomBannerTags.STARS_PATTERN_ITEM, new FabricItemSettings().rarity(Rarity.COMMON).maxCount(64)), "stars_banner_pattern");

		for (DyeColor color : DyeColor.values()) {
			BundleItem bundle = new BundleItem(new FabricItemSettings().rarity(Rarity.COMMON).maxCount(1).recipeRemainder(null));
			BUNDLES.put(color, bundle);
			registerAndAddToCreativeMenu(bundle, color.getName() + "_bundle");
		}

		Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(MOD_NAMESPACE, "head_fragment"), HeadFragmentCraftRecipe.Serializer.INSTANCE);
		Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(MOD_NAMESPACE, "bundle_recolor"), BundleRecolorRecipe.Serializer.INSTANCE);

		populateCreativeTab();
		modifyLootTables();

		VillagersRegister.registerVillagers();
		VillagersRegister.registerTrades();
		CustomBannerPatterns.registerAndGetDefault(Registries.BANNER_PATTERN);
		LivingEntityFeatureRendererRegistrationCallback.EVENT.register(new Identifier(MOD_NAMESPACE, "player_cosmetics"), (entityType, entityRenderer, registrationHelper, context) -> {
			if (EntityType.getId(entityType) != new Identifier("player")) {
				return;
			}
			entityRenderer.getModel(); //. . . what now?;
			//registrationHelper.register(new FeatureRenderer<PlayerEntity,PlayerEntityModel<PlayerEntity>>());
		});
		// BannerBlockEntityRenderer fix to allow partial transparency in pattern texture
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
		Identifier ZOMBIE_VILLAGER_LOOT_DROP_ID = new Identifier("minecraft:entities/zombie_villager");

		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			// Let's only modify built-in loot tables and leave data pack loot tables untouched by checking the source.
			// We also check that the loot table ID is equal to the ID we want.
			if (source.isBuiltin() && (ZOMBIE_LOOT_DROP_ID.equals(id) || ZOMBIE_VILLAGER_LOOT_DROP_ID.equals(id))) {
				LootPool.Builder poolBuilder = LootPool.builder()
												.with(ItemEntry.builder(Items.ZOMBIE_HEAD).weight(1))
												.conditionally(RandomChanceWithLootingLootCondition.builder(0.05f, 0.02f));
 
        		tableBuilder.pool(poolBuilder);
			}
		});
	}
}