package com.cmdgod.mc.ucr_stitch.dataconnection;

import com.google.common.collect.ImmutableList;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.random.Random;

public class WoodTypeAssociation {

    // This is it for now. Will try to make a more robust system when I get there . . .
    public static final WoodTypeConnection OAK = new WoodTypeConnection(/*WoodType.OAK,*/ Items.OAK_LOG, Items.OAK_SAPLING, Items.OAK_SIGN, Items.OAK_PLANKS, Items.OAK_STAIRS);
    public static final WoodTypeConnection SPRUCE = new WoodTypeConnection(/*WoodType.SPRUCE,*/ Items.SPRUCE_LOG, Items.SPRUCE_SAPLING, Items.SPRUCE_SIGN, Items.SPRUCE_PLANKS, Items.SPRUCE_STAIRS);
    public static final WoodTypeConnection DARK_OAK = new WoodTypeConnection(/*WoodType.DARK_OAK,*/ Items.DARK_OAK_LOG, Items.DARK_OAK_SAPLING, Items.DARK_OAK_SIGN, Items.DARK_OAK_PLANKS, Items.DARK_OAK_STAIRS);
    public static final WoodTypeConnection BIRCH = new WoodTypeConnection(/*WoodType.BIRCH,*/ Items.BIRCH_LOG, Items.BIRCH_SAPLING, Items.BIRCH_SIGN, Items.BIRCH_PLANKS, Items.BIRCH_STAIRS);
    public static final WoodTypeConnection ACACIA = new WoodTypeConnection(/*WoodType.ACACIA,*/ Items.ACACIA_LOG, Items.ACACIA_SAPLING, Items.ACACIA_SIGN, Items.ACACIA_PLANKS, Items.ACACIA_STAIRS);
    public static final WoodTypeConnection JUNGLE = new WoodTypeConnection(/*WoodType.JUNGLE,*/ Items.JUNGLE_LOG, Items.JUNGLE_SAPLING, Items.JUNGLE_SIGN, Items.JUNGLE_PLANKS, Items.JUNGLE_STAIRS);
    public static final WoodTypeConnection MANGROVE = new WoodTypeConnection(/*WoodType.MANGROVE,*/ Items.MANGROVE_LOG, Items.MANGROVE_PROPAGULE, Items.MANGROVE_SIGN, Items.MANGROVE_PLANKS, Items.MANGROVE_STAIRS);

    public static WoodTypeConnection getRandom(Random random) {
        ImmutableList<WoodTypeConnection> list = getWoodTypes();
        return list.get(random.nextBetweenExclusive(0, list.size()));
    }

    public static ImmutableList<WoodTypeConnection> getWoodTypes() {
        return ImmutableList.of(OAK, SPRUCE, DARK_OAK, BIRCH, ACACIA, JUNGLE);
    }

    public static class WoodTypeConnection {
        // I shall add more when I need it.
        //private WoodType type;
        private Item log;
        private Item sapling;
        private Item sign;
        private Item plank;
        private Item stairs;

        WoodTypeConnection(/*WoodType type,*/ Item log, Item sapling, Item sign, Item plank, Item stairs) {
            //this.type = type;
            this.log = log;
            this.sapling = sapling;
            this.sign = sign;
            this.plank = plank;
            this.stairs = stairs;
        }

        /*
        public WoodType getType() {
            return type;
        }
        */

        public Item getLog() {
            return log;
        }

        public Item getSapling() {
            return sapling;
        }

        public Item getSign() {
            return sign;
        }

        public Item getPlanks() {
            return plank;
        }

        public Item getStairs() {
            return stairs;
        }

    }

}
