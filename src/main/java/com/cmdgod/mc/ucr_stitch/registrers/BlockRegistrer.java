package com.cmdgod.mc.ucr_stitch.registrers;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
import com.cmdgod.mc.ucr_stitch.blockentities.GravityDuperBlockEntity;
import com.cmdgod.mc.ucr_stitch.blocks.GravityDuperBlock;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Item.Settings;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlockRegistrer {

    public static final Block GRAVITY_DUPER = new GravityDuperBlock();
    
    public static final BlockEntityType<GravityDuperBlockEntity> GRAVITY_DUPER_ENTITY = Registry.register(
        Registry.BLOCK_ENTITY_TYPE,
        new Identifier(UCRStitch.MOD_NAMESPACE, "gravity_duper_entity"),
        FabricBlockEntityTypeBuilder.create(GravityDuperBlockEntity::new, GRAVITY_DUPER).build()
    );

    public static void registerAll() {
        UCRStitch.LOGGER.info("UCR Stitch: Blocks registered!");

        ItemRegistrer.GRAVITY_DUPER_ITEM = registerBlock("gravity_duper", GRAVITY_DUPER, null);
    }

    private static Item registerBlock(String id, Block block) {
        return registerBlock(id, block, ItemRegistrer.ITEM_GROUP);
    }

    private static Item registerBlock(String id, Block block, ItemGroup group) {
        Registry.register(Registry.BLOCK, new Identifier(UCRStitch.MOD_NAMESPACE, id), block);
        FabricItemSettings settings = new FabricItemSettings().maxCount(64);
        if (group != null) {
            settings = settings.group(ItemRegistrer.ITEM_GROUP);
        }
        Item item = new BlockItem(block, settings);
        ItemRegistrer.registerItem(id, item);
        return item;
    }

}
