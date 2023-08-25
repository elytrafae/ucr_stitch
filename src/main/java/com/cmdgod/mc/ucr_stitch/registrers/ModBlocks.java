package com.cmdgod.mc.ucr_stitch.registrers;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
import com.cmdgod.mc.ucr_stitch.blockentities.GravityDuperBlockEntity;
import com.cmdgod.mc.ucr_stitch.blocks.GravityDuperBlock;
import com.cmdgod.mc.ucr_stitch.blocks.VoidberryVine;
import com.cmdgod.mc.ucr_stitch.blocks.VoidshroomSapling;
import com.cmdgod.mc.ucr_stitch.blocks.WaterVulnerableBlock;
import com.cmdgod.mc.ucr_stitch.blocks.WaterVulnerablePillar;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {

    public static final Block GRAVITY_DUPER = new GravityDuperBlock();

    public static final Block VOIDSHROOM_CAP = new WaterVulnerableBlock(FabricBlockSettings.copy(Blocks.NETHER_WART_BLOCK).mapColor(MapColor.LIGHT_BLUE));
    public static final Block VOIDBERRY_VINE = new VoidberryVine(FabricBlockSettings.copy(Blocks.CAVE_VINES).mapColor(MapColor.LIGHT_BLUE));
    public static final Block VOIDSHROOM_STEM = new WaterVulnerablePillar(FabricBlockSettings.copy(Blocks.CRIMSON_STEM).mapColor(MapColor.LIGHT_BLUE));
    public static final Block STRIPPED_VOIDSHROOM_STEM = new WaterVulnerablePillar(FabricBlockSettings.copy(Blocks.STRIPPED_CRIMSON_STEM).mapColor(MapColor.LIGHT_BLUE));
    public static final Block VOIDSHROOM_SAPLING = new VoidshroomSapling(FabricBlockSettings.copy(Blocks.OAK_SAPLING).mapColor(MapColor.LIGHT_BLUE));
    
    public static final BlockEntityType<GravityDuperBlockEntity> GRAVITY_DUPER_ENTITY = Registry.register(
        Registry.BLOCK_ENTITY_TYPE,
        new Identifier(UCRStitch.MOD_NAMESPACE, "gravity_duper_entity"),
        FabricBlockEntityTypeBuilder.create(GravityDuperBlockEntity::new, GRAVITY_DUPER).build()
    );

    public static void registerAll() {
        UCRStitch.LOGGER.info("UCR Stitch: Blocks registered!");

        ModItems.GRAVITY_DUPER_ITEM = registerBlock("gravity_duper", GRAVITY_DUPER, null);
        ModItems.VOIDSHROOM_CAP = registerBlock("voidshroom_cap", VOIDSHROOM_CAP);
        ModItems.VOIDBERRY_VINE = registerBlock("voidberry_vine", VOIDBERRY_VINE);
        ModItems.VOIDSHROOM_STEM = registerBlock("voidshroom_stem", VOIDSHROOM_STEM);
        ModItems.STRIPPED_VOIDSHROOM_STEM = registerBlock("stripped_voidshroom_stem", STRIPPED_VOIDSHROOM_STEM);
        ModItems.VOIDSHROOM_SAPLING = registerBlock("voidshroom_sapling", VOIDSHROOM_SAPLING);

        StrippableBlockRegistry.register(VOIDSHROOM_STEM, STRIPPED_VOIDSHROOM_STEM);
    }

    private static Item registerBlock(String id, Block block) {
        return registerBlock(id, block, ModItems.ITEM_GROUP);
    }

    private static Item registerBlock(String id, Block block, ItemGroup group) {
        Registry.register(Registry.BLOCK, new Identifier(UCRStitch.MOD_NAMESPACE, id), block);
        FabricItemSettings settings = new FabricItemSettings().maxCount(64);
        if (group != null) {
            settings = settings.group(ModItems.ITEM_GROUP);
        }
        Item item = new BlockItem(block, settings);
        ModItems.registerItem(id, item);
        return item;
    }

}
