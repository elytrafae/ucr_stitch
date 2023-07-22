package com.cmdgod.mc.ucr_stitch;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.client.player.ClientPickBlockCallback;
import net.fabricmc.fabric.api.event.client.player.ClientPickBlockCallback.Container;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.SandBlock;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.registry.Registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmdgod.mc.ucr_stitch.recipes.GravityDuperCraftRecipe;
import com.cmdgod.mc.ucr_stitch.recipes.GravityDuperRecipe;
import com.cmdgod.mc.ucr_stitch.recipes.GravityDuperRecipeSerializer;
import com.cmdgod.mc.ucr_stitch.registrers.BlockRegistrer;
import com.cmdgod.mc.ucr_stitch.registrers.ItemRegistrer;
import com.cmdgod.mc.ucr_stitch.tools.GetAllGravityBlocks;
import com.cmdgod.mc.ucr_stitch.tools.GetAllItems;

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

		Registry.register(Registry.RECIPE_SERIALIZER, GravityDuperRecipeSerializer.ID, GravityDuperRecipeSerializer.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, new Identifier(MOD_NAMESPACE, GravityDuperRecipe.Type.ID), GravityDuperRecipe.Type.INSTANCE);

		Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MOD_NAMESPACE, GravityDuperCraftRecipe.Serializer.ID), GravityDuperCraftRecipe.Serializer.INSTANCE);

		BlockRegistrer.registerAll();
		ItemRegistrer.registerAll();

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

		LOGGER.info("Hello Fabric world!");
	}

	
}