package com.cmdgod.mc.ucr_stitch.villagers;

import com.cmdgod.mc.ucr_stitch.dataconnection.WoodTypeAssociation;
import com.cmdgod.mc.ucr_stitch.dataconnection.WoodTypeAssociation.WoodTypeConnection;

import net.minecraft.village.TradeOffers;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;

public class VillagerTrades {
    
    public static TradeOffers.Factory createLumberjackSaplingLogTrade() {
        return (entity, random) -> {
            WoodTypeConnection wtc = WoodTypeAssociation.getRandom(random);
            return new TradeOffer(
                new ItemStack(wtc.getSapling(), 3),
                new ItemStack(wtc.getLog(), 24),
                5, 4, 0.02f);
        };
    }

    public static TradeOffers.Factory createLumberjackSignTrade() {
        return (entity, random) -> {
            WoodTypeConnection wtc = WoodTypeAssociation.getRandom(random);
            return new TradeOffer(
                new ItemStack(wtc.getLog(), 10),
                new ItemStack(Items.STICK, 10),
                new ItemStack(wtc.getSign(), 30),
                6, 8, 0.02f);
        };
    }

    public static TradeOffers.Factory createLumberjackLogUncraftTrade() {
        return (entity, random) -> {
            WoodTypeConnection wtc = WoodTypeAssociation.getRandom(random);
            return new TradeOffer(
                new ItemStack(wtc.getPlanks(), 16),
                new ItemStack(wtc.getLog(), 4),
                6, 4, 0.02f);
        };
    }

    public static TradeOffers.Factory createLumberjackStairTrade() {
        return (entity, random) -> {
            WoodTypeConnection wtc = WoodTypeAssociation.getRandom(random);
            return new TradeOffer(
                new ItemStack(wtc.getPlanks(), 12),
                new ItemStack(wtc.getStairs(), 16),
                6, 9, 0.02f);
        };
    }

}
