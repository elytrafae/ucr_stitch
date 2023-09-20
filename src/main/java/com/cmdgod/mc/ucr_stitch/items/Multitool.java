package com.cmdgod.mc.ucr_stitch.items;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
import com.cmdgod.mc.ucr_stitch.registrers.ModItems;
import com.google.common.collect.Multimap;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.tag.TagKey;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Multitool extends MiningToolItem {

    // Copied from the HoeItem class
    //protected static final Map<Block, Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>>> TILLING_ACTIONS = Maps.newHashMap(ImmutableMap.of(Blocks.GRASS_BLOCK, Pair.of(HoeItem::canTillFarmland, HoeItem.createTillAction(Blocks.FARMLAND.getDefaultState())), Blocks.DIRT_PATH, Pair.of(HoeItem::canTillFarmland, HoeItem.createTillAction(Blocks.FARMLAND.getDefaultState())), Blocks.DIRT, Pair.of(HoeItem::canTillFarmland, HoeItem.createTillAction(Blocks.FARMLAND.getDefaultState())), Blocks.COARSE_DIRT, Pair.of(HoeItem::canTillFarmland, HoeItem.createTillAction(Blocks.DIRT.getDefaultState())), Blocks.ROOTED_DIRT, Pair.of(itemUsageContext -> true, HoeItem.createTillAndDropAction(Blocks.DIRT.getDefaultState(), Items.HANGING_ROOTS))));

    HoeItem hoe;
    ShovelItem shovel;
    AxeItem axe;
    PickaxeItem pickaxe;

    public Multitool(Settings settings, HoeItem hoe, ShovelItem shovel, AxeItem axe, PickaxeItem pickaxe) {
        super(
                getAverageAttackDamage(hoe, shovel, axe, pickaxe)/2, 
                getAverageAttackSpeed(hoe, shovel, axe, pickaxe)/4, 
                hoe.getMaterial(), 
                TagKey.of(Registry.BLOCK_KEY, new Identifier(UCRStitch.MOD_NAMESPACE, "mineable/multitool")), 
                settings.maxDamage(hoe.getMaterial().getDurability() * 3)
            );
        this.hoe = hoe;
        this.shovel = shovel;
        this.axe = axe;
        this.pickaxe = pickaxe;
        ModItems.MULTITOOLS.put(hoe.getMaterial(), this);
        //UCRStitch.LOGGER.info(material.toString() + " has attack damage of " + getAverageAttackDamage(hoe, shovel, axe, pickaxe) + " and an attack speed of " + getAverageAttackSpeed(hoe, shovel, axe, pickaxe));
    }

    private static float getAverageAttackDamage(HoeItem hoe, ShovelItem shovel, AxeItem axe, PickaxeItem pickaxe) {
        return (hoe.getAttackDamage() + shovel.getAttackDamage() + axe.getAttackDamage() + pickaxe.getAttackDamage())/4;
    }

    private static float getAverageAttackSpeed(HoeItem hoe, ShovelItem shovel, AxeItem axe, PickaxeItem pickaxe) {
        return (getAttackSpeedFrom(hoe) + getAttackSpeedFrom(pickaxe) + getAttackSpeedFrom(shovel) + getAttackSpeedFrom(pickaxe))/4;
    }

    private static float getAttackSpeedFrom(MiningToolItem toolItem) {
        Multimap<EntityAttribute, EntityAttributeModifier> modifiers = toolItem.getAttributeModifiers(EquipmentSlot.MAINHAND);
        if (modifiers.containsKey(EntityAttributes.GENERIC_ATTACK_SPEED)) {
            return (float)modifiers.get(EntityAttributes.GENERIC_ATTACK_SPEED).iterator().next().getValue();
        }
        return 0;
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
            if (result.compareTo(ActionResult.PASS) != 0) {
                return result;
            }
        } else {
            result = shovel.useOnBlock(context);
            if (result.compareTo(ActionResult.PASS) != 0) {
                return result;
            }
        }

        // Second try, reversed. Make sure the code below is the same as above, but copy-pasted!
        if (!crouched) {
            result = hoe.useOnBlock(context);
            if (result.compareTo(ActionResult.PASS) != 0) {
                return result;
            }
        } else {
            result = shovel.useOnBlock(context);
            if (result.compareTo(ActionResult.PASS) != 0) {
                return result;
            }
        }

        // Now, if nothing happened yet, axe
        result = axe.useOnBlock(context);
        if (result.compareTo(ActionResult.PASS) != 0) {
            return result;
        }

        // If all else fails, pickaxe
        return pickaxe.useOnBlock(context);
    }
    
}
