package com.cmdgod.mc.ucr_stitch;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmdgod.mc.ucr_stitch.mixin.TreeDecoratorTypeInvoker;
import com.cmdgod.mc.ucr_stitch.mixin.TrunkPlacerTypeInvoker;
import com.cmdgod.mc.ucr_stitch.recipes.GravityDuperCraftRecipe;
import com.cmdgod.mc.ucr_stitch.recipes.GravityDuperRecipe;
import com.cmdgod.mc.ucr_stitch.recipes.GravityDuperRecipeSerializer;
import com.cmdgod.mc.ucr_stitch.recipes.MultitoolCraftRecipe;
import com.cmdgod.mc.ucr_stitch.registrers.ModBlocks;
import com.cmdgod.mc.ucr_stitch.registrers.ModItems;
import com.cmdgod.mc.ucr_stitch.registrers.LootTableModifier;
import com.cmdgod.mc.ucr_stitch.registrers.ModPotions;
import com.cmdgod.mc.ucr_stitch.registrers.ModStatusEffects;
import com.cmdgod.mc.ucr_stitch.treestuff.UpsideDownTrunkPlacer;
import com.cmdgod.mc.ucr_stitch.treestuff.VoidshroomTreeDecorator;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.mixin.content.registry.VillagerEntityMixin;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.entity.ai.brain.sensor.VillagerHostilesSensor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.foliage.RandomSpreadFoliagePlacer;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;

public class UCRStitch implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_NAMESPACE = "ucr_stitch";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAMESPACE);

	public static final TrunkPlacerType<UpsideDownTrunkPlacer> UPSIDE_DOWN_TRUNK_PLACER = TrunkPlacerTypeInvoker.callRegister("ucr_stitch:upside_down_trunk_placer", UpsideDownTrunkPlacer.CODEC);
	public static final TreeDecoratorType<VoidshroomTreeDecorator> VOIDSHROOM_TREE_DECORATOR = TreeDecoratorTypeInvoker.callRegister("ucr_stitch:voidshroom_tree_decorator", VoidshroomTreeDecorator.CODEC);

	public static final Identifier VOIDSHROOM_ID = new Identifier(MOD_NAMESPACE, "voidshroom");
	public static final RegistryEntry<ConfiguredFeature<TreeFeatureConfig, ?>> VOIDSHROOM_TREE = ConfiguredFeatures.register(VOIDSHROOM_ID.toString(), Feature.TREE,
	// Configure the feature using the builder
	new TreeFeatureConfig.Builder(
		BlockStateProvider.of(ModBlocks.VOIDSHROOM_STEM), // Trunk block provider
		new UpsideDownTrunkPlacer(6, 3, 1), // places a straight trunk
		BlockStateProvider.of(ModBlocks.VOIDSHROOM_CAP), // Foliage block provider
		new RandomSpreadFoliagePlacer(ConstantIntProvider.create(4), ConstantIntProvider.create(0),ConstantIntProvider.create(4), 50), // places leaves as a blob (radius, offset from trunk, height)
		new TwoLayersFeatureSize(-1, 0, -1) // The width of the tree at different layers; used to see how tall the tree can be without clipping into blocks
	).dirtProvider(BlockStateProvider.of(Blocks.END_STONE)).decorators(Collections.singletonList(VoidshroomTreeDecorator.INSTANCE)).build());

	// our PlacedFeature. this is what gets passed to the biome modification API to add to the biome.
    // public static PlacedFeature VOIDHSROOM_TREE_PLACED = new PlacedFeature(VOIDSHROOM_TREE, List.of(SquarePlacementModifier.of()));

	// TODO: Add voidshrooms to world generation: https://fabricmc.net/wiki/tutorial:features

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		//LivingEntity;
		//ProtectionEnchantment;
		//ChorusFruitItem;
		// FurnaceBlockEntity;
		// BlockItem;
		//VillagerEntity;
		//VillagerHostilesSensor;
		// FishingRodItem;

		Registry.register(Registry.RECIPE_SERIALIZER, GravityDuperRecipeSerializer.ID, GravityDuperRecipeSerializer.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, new Identifier(MOD_NAMESPACE, GravityDuperRecipe.Type.ID), GravityDuperRecipe.Type.INSTANCE);

		Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MOD_NAMESPACE, GravityDuperCraftRecipe.Serializer.ID), GravityDuperCraftRecipe.Serializer.INSTANCE);
		Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MOD_NAMESPACE, MultitoolCraftRecipe.Serializer.ID), MultitoolCraftRecipe.Serializer.INSTANCE);

		ModBlocks.registerAll();
		ModItems.registerAll();
		ModStatusEffects.registerAll();
		ModPotions.registerPotionsRecipes();
		LootTableModifier.doAllChanges();

		/*
		BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                // the feature is to be added while flowers and trees are being generated
                GenerationStep.Feature.VEGETAL_DECORATION,
                RegistryKey.of(Registry.PLACED_FEATURE_KEY, VOIDSHROOM_ID));
		*/

		/*
		ClientPickBlockCallback cb = new ClientPickBlockCallback() {
			@Override
			public boolean pick(PlayerEntity player, HitResult result, Container container) {
				System.out.println("List of all items: ");
				System.out.println(GetAllItems.getJsonArrayList());
				System.out.println("List of all gravity blocks: ");
				System.out.println(GetAllGravityBlocks.getAllGravityBlocks());
				return true;
			}
		};

		ClientPickBlockCallback.EVENT.register( Event.DEFAULT_PHASE, cb);
		*/

		LOGGER.info("UCR Stitch says Hello!");
	}

	
}