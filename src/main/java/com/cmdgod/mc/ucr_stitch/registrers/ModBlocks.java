package com.cmdgod.mc.ucr_stitch.registrers;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
import com.cmdgod.mc.ucr_stitch.blockentities.GravityDuperBlockEntity;
import com.cmdgod.mc.ucr_stitch.blocks.GravityDuperBlock;
import com.cmdgod.mc.ucr_stitch.blocks.VoidberryVine;
import com.cmdgod.mc.ucr_stitch.blocks.VoidshroomSapling;
import com.cmdgod.mc.ucr_stitch.blocks.WaterVulnerableBlock;
import com.cmdgod.mc.ucr_stitch.blocks.WaterVulnerablePillar;
import com.cmdgod.mc.ucr_stitch.blocks.WaterVulnerableSign;

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
import net.minecraft.item.Items;
import net.minecraft.item.SignItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.SignType;
import net.minecraft.util.registry.Registry;

public class ModBlocks {

    public static final Block GRAVITY_DUPER = new GravityDuperBlock();

    public static final MapColor VOIDSHROOM_MAP_COLOR = MapColor.PURPLE;

    public static final Block VOIDSHROOM_CAP = new WaterVulnerableBlock(FabricBlockSettings.copy(Blocks.NETHER_WART_BLOCK).mapColor(VOIDSHROOM_MAP_COLOR));
    public static final Block VOIDBERRY_VINE = new VoidberryVine(FabricBlockSettings.copy(Blocks.CAVE_VINES).mapColor(VOIDSHROOM_MAP_COLOR));
    public static final Block VOIDSHROOM_STEM = new WaterVulnerablePillar(FabricBlockSettings.copy(Blocks.CRIMSON_STEM).mapColor(VOIDSHROOM_MAP_COLOR));
    public static final Block STRIPPED_VOIDSHROOM_STEM = new WaterVulnerablePillar(FabricBlockSettings.copy(Blocks.STRIPPED_CRIMSON_STEM).mapColor(VOIDSHROOM_MAP_COLOR));
    public static final Block VOIDSHROOM_HYPHAE = new WaterVulnerablePillar(FabricBlockSettings.copy(Blocks.CRIMSON_HYPHAE).mapColor(VOIDSHROOM_MAP_COLOR));
    public static final Block STRIPPED_VOIDSHROOM_HYPHAE = new WaterVulnerablePillar(FabricBlockSettings.copy(Blocks.STRIPPED_CRIMSON_HYPHAE).mapColor(VOIDSHROOM_MAP_COLOR));
    public static final Block VOIDSHROOM_SAPLING = new VoidshroomSapling(FabricBlockSettings.copy(Blocks.OAK_SAPLING).mapColor(VOIDSHROOM_MAP_COLOR));
    public static final Block VOIDSHROOM_PLANKS = new WaterVulnerableBlock(FabricBlockSettings.copy(Blocks.CRIMSON_PLANKS).mapColor(VOIDSHROOM_MAP_COLOR));
    public static final Block VOIDSHROOM_SIGN = registerNoBlockItem("voidshroom_sign", new WaterVulnerableSign(FabricBlockSettings.copy(Blocks.CRIMSON_SIGN), SignType.CRIMSON));
    public static final Block VOIDSHROOM_WALL_SIGN = registerNoBlockItem("voidshroom_wall_sign", new WaterVulnerableSign(FabricBlockSettings.copy(Blocks.CRIMSON_WALL_SIGN).dropsLike(VOIDSHROOM_SIGN), SignType.CRIMSON));
    
    public static final BlockEntityType<GravityDuperBlockEntity> GRAVITY_DUPER_ENTITY = Registry.register(
        Registry.BLOCK_ENTITY_TYPE,
        new Identifier(UCRStitch.MOD_NAMESPACE, "gravity_duper_entity"),
        FabricBlockEntityTypeBuilder.create(GravityDuperBlockEntity::new, GRAVITY_DUPER).build()
    );

    public static void registerAll() {
        UCRStitch.LOGGER.info("UCR Stitch: Blocks registered!");
        // Blocks.OAK_PLANKS;

        ModItems.GRAVITY_DUPER_ITEM = registerBlock("gravity_duper", GRAVITY_DUPER, null);

        ModItems.VOIDSHROOM_CAP = registerBlock("voidshroom_cap", VOIDSHROOM_CAP);
        ModItems.VOIDBERRY_VINE = registerBlock("voidberry_vine", VOIDBERRY_VINE);
        ModItems.VOIDSHROOM_STEM = registerBlock("voidshroom_stem", VOIDSHROOM_STEM);
        ModItems.STRIPPED_VOIDSHROOM_STEM = registerBlock("stripped_voidshroom_stem", STRIPPED_VOIDSHROOM_STEM);
        ModItems.VOIDSHROOM_HYPHAE = registerBlock("voidshroom_hyphae", VOIDSHROOM_HYPHAE);
        ModItems.STRIPPED_VOIDSHROOM_HYPHAE = registerBlock("stripped_voidshroom_hyphae", STRIPPED_VOIDSHROOM_HYPHAE);
        ModItems.VOIDSHROOM_SAPLING = registerBlock("voidshroom_sapling", VOIDSHROOM_SAPLING);
        ModItems.VOIDSHROOM_PLANKS = registerBlock("voidshroom_planks", VOIDSHROOM_PLANKS);
        //ModItems.VOIDSHROOM_SIGN = new SignItem(new Item.Settings().maxCount(16).group(ModItems.ITEM_GROUP), VOIDSHROOM_SIGN, VOIDSHROOM_WALL_SIGN);

        StrippableBlockRegistry.register(VOIDSHROOM_STEM, STRIPPED_VOIDSHROOM_STEM);
        StrippableBlockRegistry.register(VOIDSHROOM_HYPHAE, STRIPPED_VOIDSHROOM_HYPHAE);
    }

    private static Item registerBlock(String id, Block block) {
        return registerBlock(id, block, ModItems.ITEM_GROUP);
    }

    private static Item registerBlock(String id, Block block, ItemGroup group) {
        registerNoBlockItem(id, block);
        FabricItemSettings settings = new FabricItemSettings().maxCount(64);
        if (group != null) {
            settings = settings.group(ModItems.ITEM_GROUP);
        }
        Item item = new BlockItem(block, settings);
        ModItems.registerItem(id, item);
        return item;
    }

    private static Block registerNoBlockItem(String id, Block block) {
        return Registry.register(Registry.BLOCK, new Identifier(UCRStitch.MOD_NAMESPACE, id), block);
    }

}
