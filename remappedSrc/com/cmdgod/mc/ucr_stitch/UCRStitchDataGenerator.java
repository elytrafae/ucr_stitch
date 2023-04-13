package com.cmdgod.mc.ucr_stitch;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;

public class UCRStitchDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		//fabricDataGenerator.addProvider(UCRStitchLanguageFile::new);
		fabricDataGenerator.addProvider(UCRStitchModelGenerator::new);
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

}

