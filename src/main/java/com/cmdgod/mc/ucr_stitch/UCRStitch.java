package com.cmdgod.mc.ucr_stitch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmdgod.mc.ucr_stitch.recipes.GravityDuperCraftRecipe;
import com.cmdgod.mc.ucr_stitch.recipes.GravityDuperRecipe;
import com.cmdgod.mc.ucr_stitch.recipes.GravityDuperRecipeSerializer;
import com.cmdgod.mc.ucr_stitch.recipes.MultitoolCraftRecipe;
import com.cmdgod.mc.ucr_stitch.registrers.BlockRegistrer;
import com.cmdgod.mc.ucr_stitch.registrers.ItemRegistrer;
import com.cmdgod.mc.ucr_stitch.registrers.LootTableModifier;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.block.entity.DropperBlockEntity;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class UCRStitch implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_NAMESPACE = "ucr_stitch";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAMESPACE);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		//LivingEntity;
		//ProtectionEnchantment;
		//DispenserBlockEntity;

		Registry.register(Registry.RECIPE_SERIALIZER, GravityDuperRecipeSerializer.ID, GravityDuperRecipeSerializer.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, new Identifier(MOD_NAMESPACE, GravityDuperRecipe.Type.ID), GravityDuperRecipe.Type.INSTANCE);

		Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MOD_NAMESPACE, GravityDuperCraftRecipe.Serializer.ID), GravityDuperCraftRecipe.Serializer.INSTANCE);
		Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MOD_NAMESPACE, MultitoolCraftRecipe.Serializer.ID), MultitoolCraftRecipe.Serializer.INSTANCE);

		BlockRegistrer.registerAll();
		ItemRegistrer.registerAll();
		LootTableModifier.doAllChanges();

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