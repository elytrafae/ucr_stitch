package com.cmdgod.mc.ucr_stitch.registrers;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
import com.cmdgod.mc.ucr_stitch.mixininterfaces.IPlayerEntityMixin;
import com.cmdgod.mc.ucr_stitch.tools.GetAllPotionEffectsHelper;
import com.cmdgod.mc.ucr_stitch.tools.MediaUtility;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import dev.emi.trinkets.api.SlotGroup;
import dev.emi.trinkets.api.TrinketsApi;
import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.Placeholders;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.command.argument.TextArgumentType;
import net.minecraft.command.argument.MessageArgumentType.MessageFormat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.GiveCommand;
import net.minecraft.server.command.SayCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.TellRawCommand;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static net.minecraft.server.command.CommandManager.*;

import java.util.ArrayList;
import java.util.Map;

public class ModCommands {

    private static final SimpleCommandExceptionType GET_MULTIPLE_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.data.get.multiple"));
    
    public static void registerAll() {
        //CommandDispatcher;
        /*
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
        literal("getassist")
        .then(CommandManager.argument("target", EntityArgumentType.player()))
            .executes(context -> {
            // For versions below 1.19, replace "Text.literal" with "new LiteralText".
            ServerCommandSource source = context.getSource();
            System.out.println("Got here!");
            //EntityArgumentType player = context.getArgument("target", EntityArgumentType.class);
            PlayerEntity player = EntityArgumentType.getPlayer(context, "target");
            System.out.println("Got here! 2");
            if (player == null) {
                //source.sendMessage(Text.literal("The command was not called by a player!"));
                throw new SimpleCommandExceptionType(Text.literal("The command was not called by a player!")).create();
            }
            int assistTicks = ((IPlayerEntityMixin)(Object)player).getAssistedKillTicks();
            source.sendFeedback(Text.literal("Remaining assist ticks: " + assistTicks), false);
            return assistTicks;
            }
        )));
        */

        /*
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
        literal("getpotionlist")
        .executes(context -> {
            ServerCommandSource source = context.getSource();
            ArrayList<Identifier> ids = GetAllPotionEffectsHelper.getAllPotionEffects((effect) -> {return effect.isBeneficial() && !effect.isInstant();});
            StringBuilder strbuild = new StringBuilder();
            ids.forEach(id -> {strbuild.append(id); strbuild.append("\n");});
            UCRStitch.LOGGER.info(strbuild.toString());
            source.sendFeedback(Text.literal("List generated!"), false);
            return 1;
            }
        )));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
        literal("getpotionlistinstant")
        .executes(context -> {
            ServerCommandSource source = context.getSource();
            ArrayList<Identifier> ids = GetAllPotionEffectsHelper.getAllPotionEffects((effect) -> {return effect.isBeneficial() && effect.isInstant();});
            StringBuilder strbuild = new StringBuilder();
            ids.forEach(id -> {strbuild.append(id); strbuild.append("\n");});
            UCRStitch.LOGGER.info(strbuild.toString());
            source.sendFeedback(Text.literal("List generated!"), false);
            return 1;
            }
        )));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
        literal("getmedia")
        .executes(context -> {
            ServerCommandSource source = context.getSource();
            int media = 0;
            if (source.isExecutedByPlayer()) {
                media = MediaUtility.getMediaInInventory(source.getPlayer().getInventory());
            }
            source.sendFeedback(Text.literal("Media: " + media + " (" + MediaUtility.mediaToAmethystDust(media) + " Amethyst Dust)"), false);
            return media;
            }
        )));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
        literal("testplaceholder").then(CommandManager.argument("message", TextArgumentType.text())
        .executes(context -> {
            ServerCommandSource source = context.getSource();
            Text stringMessage = TextArgumentType.getTextArgument(context, "message");
            source.sendFeedback(Placeholders.parseText(stringMessage, PlaceholderContext.of(source)), false);
            return 0;
            }
        ))));
        */

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
        literal("testtrinket")
        .executes(context -> {
            ServerCommandSource source = context.getSource();
            if (source.isExecutedByPlayer()) {
                PlayerEntity player = source.getPlayer();
                Map<String, SlotGroup> slotMap = TrinketsApi.getEntitySlots(player.getWorld(), player.getType());
                System.out.println(TrinketsApi.getEntitySlots(player.getWorld(), player.getType()));
            }
            return 0;
            }
        )));
    }

}
