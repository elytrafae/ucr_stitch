package com.cmdgod.mc.ucr_stitch;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import com.mojang.datafixers.types.templates.Tag;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.ItemTagProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

public class UCRStitchDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		UCRStitch.LOGGER.info("Data generation initiated!");
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		//fabricDataGenerator.addProvider(UCRStitchLanguageFile::new);
		//fabricDataGenerator.addProvider(UCRStitchModelGenerator::new);

		pack.addProvider(BundleRecipeGenerator::new);
		pack.addProvider(CommonItemTagGenerator::new);
	}

	/* 
	private static class UCRStitchLanguageFile extends FabricLanguageProvider {
		private UCRStitchLanguageFile(FabricDataGenerator dataGenerator) {
			super(dataGenerator, "en_us");
		}
	 
		@Override
		public void generateTranslations(TranslationBuilder translationBuilder) {
			translationBuilder.add(UCRStitch.HeadFragment, "Head Fragment");
		}
	}
	*/

	/*
	private static class UCRStitchModelGenerator extends FabricModelProvider {
		private UCRStitchModelGenerator(FabricDataGenerator generator) {
			super(generator);
		}
	 
		@Override
		public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
			// ...
		}
	 
		@Override
		public void generateItemModels(ItemModelGenerator itemModelGenerator) {
			// ...
		}
	}
	*/

	private static class BundleRecipeGenerator extends FabricRecipeProvider {
		private BundleRecipeGenerator(FabricDataOutput generator) {
			super(generator);
		}

		@Override
		public void generate(Consumer<RecipeJsonProvider> exporter) {
			
			for (DyeColor color : DyeColor.values()) {
				Identifier dyeId = new Identifier(color.getName() + "_dye");
				if ((!Registries.ITEM.containsId(dyeId)) || (!UCRStitch.BUNDLES.containsKey(color))) {
					UCRStitch.LOGGER.warn("No recipe could be created for bundle-dye pair: " + color.getName());
					continue;
				}
				Item dye = Registries.ITEM.get(dyeId);
				Item bundle = UCRStitch.BUNDLES.get(color);
				TagKey<Item> leather = TagKey.of(RegistryKeys.ITEM, new Identifier("c:leather"));
				TagKey<Item> string = TagKey.of(RegistryKeys.ITEM, new Identifier("c:string"));
				ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, bundle, 1)
				.input('L', leather)
				.input('S', string)
				.input('D', dye)
				.pattern("SLS")
				.pattern("LDL")
				.pattern("LLL")
				.offerTo(exporter, new Identifier(UCRStitch.MOD_NAMESPACE, FabricRecipeProvider.getRecipeName(bundle)));
			}
			
		}
	}

	private static class CommonItemTagGenerator extends ItemTagProvider {

		private static final TagKey<Item> LEATHER = TagKey.of(RegistryKeys.ITEM, new Identifier("c:leather"));
		private static final TagKey<Item> STRING = TagKey.of(RegistryKeys.ITEM, new Identifier("c:string"));

		private CommonItemTagGenerator(FabricDataOutput generator, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
			super(generator, completableFuture);
		}

		@Override
		protected void configure(WrapperLookup arg) {
			getOrCreateTagBuilder(LEATHER).add(Items.LEATHER).add(Items.RABBIT_HIDE);
			getOrCreateTagBuilder(STRING).add(Items.STRING);
		}
	 }

}

