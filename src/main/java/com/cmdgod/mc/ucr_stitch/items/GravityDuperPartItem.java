package com.cmdgod.mc.ucr_stitch.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class GravityDuperPartItem extends Item {

    private PartType partType;

    private static final String SEPARATOR = "§5--------------------------------§r";

    public GravityDuperPartItem(Settings settings, PartType partType) {
        super(settings);
        this.partType = partType;
    }
    
    private String getPartTextKey(PartType textType) {
        String key = "poem.ucr_stitch.gravity_duper.line";
        if (textType == PartType.TOP) {
            key += "1";
        } else if (textType == PartType.BOTTOM) {
            key += "2";
        } else if (textType == PartType.SIDE) {
            key += "3";
        } else {
            key += "4";
        }
        return key;
    }

    private void sendPartMessage(PartType textType, PlayerEntity user) {
        user.sendMessage(Text.translatable(getPartTextKey(textType)).formatted((textType == PartType.NONE || textType == partType) ? Formatting.LIGHT_PURPLE : Formatting.OBFUSCATED, Formatting.LIGHT_PURPLE));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            return TypedActionResult.success(user.getStackInHand(hand));
        }
        user.sendMessage(Text.of(SEPARATOR));
        sendPartMessage(PartType.TOP, user);
        sendPartMessage(PartType.BOTTOM, user);
        sendPartMessage(PartType.SIDE, user);
        sendPartMessage(PartType.NONE, user);
        user.sendMessage(Text.of(SEPARATOR));
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    public static enum PartType {
        TOP,
        BOTTOM,
        SIDE,
        NONE
    }

}
