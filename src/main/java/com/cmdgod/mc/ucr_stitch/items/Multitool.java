package com.cmdgod.mc.ucr_stitch.items;

import java.util.Map;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
import com.google.common.base.Predicate;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.StructureBlockBlockEntity.Action;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.tag.TagKey;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import com.mojang.datafixers.util.Pair;

public class Multitool extends MiningToolItem {

    // Copied from the HoeItem class
    //protected static final Map<Block, Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>>> TILLING_ACTIONS = Maps.newHashMap(ImmutableMap.of(Blocks.GRASS_BLOCK, Pair.of(HoeItem::canTillFarmland, HoeItem.createTillAction(Blocks.FARMLAND.getDefaultState())), Blocks.DIRT_PATH, Pair.of(HoeItem::canTillFarmland, HoeItem.createTillAction(Blocks.FARMLAND.getDefaultState())), Blocks.DIRT, Pair.of(HoeItem::canTillFarmland, HoeItem.createTillAction(Blocks.FARMLAND.getDefaultState())), Blocks.COARSE_DIRT, Pair.of(HoeItem::canTillFarmland, HoeItem.createTillAction(Blocks.DIRT.getDefaultState())), Blocks.ROOTED_DIRT, Pair.of(itemUsageContext -> true, HoeItem.createTillAndDropAction(Blocks.DIRT.getDefaultState(), Items.HANGING_ROOTS))));

    HoeItem hoe;
    ShovelItem shovel;
    AxeItem axe;
    PickaxeItem pickaxe;

    public Multitool(float attackDamage, float attackSpeed, ToolMaterial material, Settings settings, HoeItem hoe, ShovelItem shovel, AxeItem axe, PickaxeItem pickaxe) {
        super(attackDamage, attackSpeed, material, TagKey.of(Registry.BLOCK_KEY, new Identifier(UCRStitch.MOD_NAMESPACE, "mineable/multitool")), settings.maxDamage(material.getDurability() * 3) );
        this.hoe = hoe;
        this.shovel = shovel;
        this.axe = axe;
        this.pickaxe = pickaxe;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        if (player == null) {
            return ActionResult.FAIL;
        }
        ActionResult result;
        boolean crouched = player.isSneaking();

        // First try
        if (crouched) {
            result = hoe.useOnBlock(context);
            if (result.compareTo(ActionResult.SUCCESS) == 0) {
                return result;
            }
        } else {
            result = shovel.useOnBlock(context);
            if (result.compareTo(ActionResult.SUCCESS) == 0) {
                return result;
            }
        }

        // Second try, reversed. Make sure the code below is the same as above, but copy-pasted!
        if (!crouched) {
            result = hoe.useOnBlock(context);
            if (result.compareTo(ActionResult.SUCCESS) == 0) {
                return result;
            }
        } else {
            result = shovel.useOnBlock(context);
            if (result.compareTo(ActionResult.SUCCESS) == 0) {
                return result;
            }
        }

        // Now, if nothing happened yet, axe
        result = axe.useOnBlock(context);
        if (result.compareTo(ActionResult.SUCCESS) == 0) {
            return result;
        }

        // If all else fails, pickaxe
        return pickaxe.useOnBlock(context);
    }
    
}
