package com.cmdgod.mc.ucr_stitch;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmdgod.mc.ucr_stitch.config.UCRStitchConfig;
import com.cmdgod.mc.ucr_stitch.networking.PVPTogglePacket;
import com.cmdgod.mc.ucr_stitch.networking.RecallParticlePacket;
import com.cmdgod.mc.ucr_stitch.recipes.GravityDuperCraftRecipe;
import com.cmdgod.mc.ucr_stitch.recipes.GravityDuperRecipe;
import com.cmdgod.mc.ucr_stitch.recipes.GravityDuperRecipeSerializer;
import com.cmdgod.mc.ucr_stitch.recipes.MultitoolCraftRecipe;
import com.cmdgod.mc.ucr_stitch.registrers.ModBlocks;
import com.cmdgod.mc.ucr_stitch.registrers.ModCommands;
import com.cmdgod.mc.ucr_stitch.registrers.ModConditions;
import com.cmdgod.mc.ucr_stitch.registrers.ModItems;
import com.cmdgod.mc.ucr_stitch.registrers.LootTableModifier;
import com.cmdgod.mc.ucr_stitch.registrers.ModActions;
import com.cmdgod.mc.ucr_stitch.registrers.ModPotions;
import com.cmdgod.mc.ucr_stitch.registrers.ModPowers;
import com.cmdgod.mc.ucr_stitch.registrers.ModStatusEffects;
import com.cmdgod.mc.ucr_stitch.tools.Utility;
import com.cmdgod.mc.ucr_stitch.worldgen.VoidberryVineFeature;
import com.cmdgod.mc.ucr_stitch.worldgen.VoidberryVineFeatureConfig;

import io.wispforest.owo.network.OwoNetChannel;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.SmokerBlock;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.effect.InstantStatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.Items;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.heightprovider.VeryBiasedToBottomHeightProvider;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;

public class UCRStitch implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_NAMESPACE = "ucr_stitch";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAMESPACE);

	// PotionEntity;
	// Particle
	

	public static final Identifier VOIDBERRY_VINES_FEATURE_ID = new Identifier(MOD_NAMESPACE, "voidberry_vines");
    public static Feature<VoidberryVineFeatureConfig> VOIDBERRY_VINES_FEATURE = new VoidberryVineFeature(VoidberryVineFeatureConfig.CODEC);

	public static ConfiguredFeature<VoidberryVineFeatureConfig, VoidberryVineFeature> WILD_VOIDBERRY_VINES = new ConfiguredFeature<>(
                    (VoidberryVineFeature) VOIDBERRY_VINES_FEATURE,
                    new VoidberryVineFeatureConfig(false)
    );

	public static PlacedFeature WILD_VOIDBERRY_VINES_PLACED = new PlacedFeature(
            RegistryEntry.of(
                    WILD_VOIDBERRY_VINES
//                    the SquarePlacementModifier makes the feature generate a cluster of pillars each time
            ), List.of(SquarePlacementModifier.of(), RarityFilterPlacementModifier.of(22), HeightRangePlacementModifier.of(VeryBiasedToBottomHeightProvider.create(YOffset.fixed(5), YOffset.fixed(20), 22)))
    );

	public static final UCRStitchConfig CONFIG = UCRStitchConfig.createAndLoad();
	public static final OwoNetChannel PVP_TOGGLE_CHANNEL = OwoNetChannel.create(new Identifier(MOD_NAMESPACE, "toggle_pvp"));
	public static final OwoNetChannel RECALL_PARTICLE_CHANNEL = OwoNetChannel.create(new Identifier(MOD_NAMESPACE, "recall_particle"));

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		// Items;

		Registry.register(Registry.RECIPE_SERIALIZER, GravityDuperRecipeSerializer.ID, GravityDuperRecipeSerializer.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, new Identifier(MOD_NAMESPACE, GravityDuperRecipe.Type.ID), GravityDuperRecipe.Type.INSTANCE);

		Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MOD_NAMESPACE, GravityDuperCraftRecipe.Serializer.ID), GravityDuperCraftRecipe.Serializer.INSTANCE);
		Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MOD_NAMESPACE, MultitoolCraftRecipe.Serializer.ID), MultitoolCraftRecipe.Serializer.INSTANCE);

		ModBlocks.registerAll();
		ModItems.registerAll();
		ModStatusEffects.registerAll();
		ModPotions.registerPotionsRecipes();
		LootTableModifier.doAllChanges();
		ModPowers.registerAll();
		ModActions.registerAll();
		ModConditions.registerAll();
		ModCommands.registerAll();

		Registry.register(Registry.FEATURE, VOIDBERRY_VINES_FEATURE_ID, VOIDBERRY_VINES_FEATURE);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, VOIDBERRY_VINES_FEATURE_ID, WILD_VOIDBERRY_VINES);
		Registry.register(BuiltinRegistries.PLACED_FEATURE, VOIDBERRY_VINES_FEATURE_ID, WILD_VOIDBERRY_VINES_PLACED);

		BiomeModifications.addFeature(
                BiomeSelectors.foundInTheEnd(),
                // the feature is to be added while flowers and trees are being generated
                GenerationStep.Feature.VEGETAL_DECORATION,
                RegistryKey.of(Registry.PLACED_FEATURE_KEY, VOIDBERRY_VINES_FEATURE_ID));

		LOGGER.info("UCR Stitch says Hello!");

		PVP_TOGGLE_CHANNEL.registerServerbound(PVPTogglePacket.class, (message, access) -> {
			Utility.getInterfacePlayer(access.player()).togglePVPRequest();
        });

		RECALL_PARTICLE_CHANNEL.registerClientbound(RecallParticlePacket.class, (message, access) -> {
			PlayerEntity player = access.player();
			Vec3d position = message.position();
			float c = 0.2f + player.getRandom().nextFloat()*0.2f;
			for (double j=0; j < 3; j+=0.3) {
				for (int i=0; i < 360; i+=10) {
					player.getWorld().addParticle(new DustParticleEffect(new Vec3f(c, c, 0.8f), 1f), false, position.getX() + Math.sin(Math.toRadians(i))*(5-j*2), position.getY() + j + 0.1, position.getZ() + Math.cos(Math.toRadians(i))*(5-j*2), 0, 0, 0);
				}
			}
			
        });

		// This is just to force gradle to fucking load Patchouli!
	}

	
}