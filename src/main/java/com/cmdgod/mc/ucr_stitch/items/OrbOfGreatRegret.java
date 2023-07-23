package com.cmdgod.mc.ucr_stitch.items;

import org.apache.logging.log4j.core.jmx.Server;

import com.cmdgod.mc.ucr_stitch.items.GravityDuperPartItem.PartType;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class OrbOfGreatRegret extends TooltippedItem {

    public OrbOfGreatRegret(Settings settings) {
        super(settings);
        //TODO Auto-generated constructor stub
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            return TypedActionResult.success(user.getStackInHand(hand));
        }
        world.getServer().getCommandManager().executeWithPrefix(user.getCommandSource().withMaxLevel(2).withOutput(CommandOutput.DUMMY), "function ucr_stitch:skill_point_reset/start");
        return TypedActionResult.success(user.getStackInHand(hand));
    }
    
}
