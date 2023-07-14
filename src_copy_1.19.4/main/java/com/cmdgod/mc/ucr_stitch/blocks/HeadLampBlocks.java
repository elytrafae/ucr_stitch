package com.cmdgod.mc.ucr_stitch.blocks;

import java.util.function.ToIntFunction;

import org.jetbrains.annotations.Nullable;

import com.cmdgod.mc.ucr_stitch.UCRStitch;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.WallSkullBlock;
import net.minecraft.block.SkullBlock.SkullType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.VerticallyAttachableBlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class HeadLampBlocks {

    // TODO: Investigate how to make head lamps not invisible when placed.

    private static final int LUMINOSITY = 15;

    private static ToIntFunction<BlockState> ALWAYS_LIT = (blockState) -> {return LUMINOSITY;};
    private static ToIntFunction<BlockState> RESDSTONE_LIT = (blockState) -> {return blockState.get(Properties.LIT) != false ? LUMINOSITY : 0;};

    public static void registerHeads() {
        for (SkullType skullType : SkullBlock.Type.values()) {
            String name = skullTypeToIdName(skullType, false);
            String nameWall = skullTypeToIdName(skullType, true);
            HeadLampBlock headLamp = new HeadLampBlock(skullType);
            HeadLampBlockRedstone headLampRedstone = new HeadLampBlockRedstone(skullType);
            HeadLampWallBlock headLampWall = new HeadLampWallBlock(skullType, headLamp);
            HeadLampWallBlockRedstone headLampWallRedstone = new HeadLampWallBlockRedstone(skullType, headLampRedstone);

            Registry.register(Registries.BLOCK, new Identifier(UCRStitch.MOD_NAMESPACE, name + "_lamp"), headLamp);
            Registry.register(Registries.BLOCK, new Identifier(UCRStitch.MOD_NAMESPACE, name + "_redstone_lamp"), headLampRedstone);
            Registry.register(Registries.BLOCK, new Identifier(UCRStitch.MOD_NAMESPACE, nameWall + "_lamp"), headLampWall);
            Registry.register(Registries.BLOCK, new Identifier(UCRStitch.MOD_NAMESPACE, nameWall + "_redstone_lamp"), headLampWallRedstone);

            Item item = new VerticallyAttachableBlockItem(headLamp, headLampWall, new Item.Settings().rarity(Rarity.RARE), Direction.DOWN);
            UCRStitch.registerAndAddToCreativeMenu(item, name + "_lamp");
            Item itemRedstone = new VerticallyAttachableBlockItem(headLampRedstone, headLampWallRedstone, new Item.Settings().rarity(Rarity.RARE), Direction.DOWN);
            UCRStitch.registerAndAddToCreativeMenu(itemRedstone, name + "_redstone_lamp");

            System.out.println(name + "_lamp.json");
            System.out.println(name + "_redstone_lamp.json");
        }

    }

    public static String skullTypeToIdName(SkullType st, boolean isWall) {
        String pref = "", suff = "";
        if (st.equals(SkullBlock.Type.PLAYER)) {
            pref = "player";
            suff = "head";
        } else if (st.equals(SkullBlock.Type.CREEPER)) {
            pref = "creeper";
            suff = "head";
        } else if (st.equals(SkullBlock.Type.DRAGON)) {
            pref = "dragon";
            suff = "head";
        } else if (st.equals(SkullBlock.Type.PIGLIN)) {
            pref = "piglin";
            suff = "head";
        } else if (st.equals(SkullBlock.Type.SKELETON)) {
            pref = "skeleton";
            suff = "skull";
        } else if (st.equals(SkullBlock.Type.WITHER_SKELETON)) {
            pref = "wither_skeleton";
            suff = "skull";
        } else if (st.equals(SkullBlock.Type.ZOMBIE)) {
            pref = "zombie";
            suff = "head";
        } else {
            pref = "error";
            suff = "error";
        }
        return pref + (isWall ? "_wall_" : "_") + suff;
    }

    static public class HeadLampBlock extends SkullBlock {
        public HeadLampBlock(SkullType skullType) {
            super(skullType, AbstractBlock.Settings.of(Material.DECORATION).strength(1.0f).luminance(ALWAYS_LIT));
        }
    }

    static public class HeadLampWallBlock extends WallSkullBlock {
        public HeadLampWallBlock(SkullType skullType, Block dropAs) {
            super(skullType, AbstractBlock.Settings.of(Material.DECORATION).strength(1.0f).luminance(ALWAYS_LIT).dropsLike(dropAs));
        }
    }

    static public class HeadLampBlockRedstone extends SkullBlock {
        public HeadLampBlockRedstone(SkullType skullType) {
            super(skullType, AbstractBlock.Settings.of(Material.DECORATION).strength(1.0f).luminance(RESDSTONE_LIT));
        }

        @Override
        @Nullable
        public BlockState getPlacementState(ItemPlacementContext ctx) {
            BlockState superState = super.getPlacementState(ctx);
            if (superState == null) {
                superState = this.getDefaultState();
            }
            return (BlockState)superState.with(Properties.LIT, ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos()));
        }

        @Override
        protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
            super.appendProperties(builder);
            builder.add(Properties.LIT);
        }

        @Override
        public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
            if (world.isClient) {
                return;
            }
            boolean bl = state.get(Properties.LIT);
            if (bl != world.isReceivingRedstonePower(pos)) {
                if (bl) {
                    world.scheduleBlockTick(pos, this, 4);
                } else {
                    world.setBlockState(pos, (BlockState)state.cycle(Properties.LIT), Block.NOTIFY_LISTENERS);
                }
            }
        }

        @Override
        public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
            if (state.get(Properties.LIT).booleanValue() && !world.isReceivingRedstonePower(pos)) {
                world.setBlockState(pos, (BlockState)state.cycle(Properties.LIT), Block.NOTIFY_LISTENERS);
            }
        }
    }

    static public class HeadLampWallBlockRedstone extends WallSkullBlock {
        public HeadLampWallBlockRedstone(SkullType skullType, Block dropAs) {
            super(skullType, AbstractBlock.Settings.of(Material.DECORATION).strength(1.0f).luminance(RESDSTONE_LIT).dropsLike(dropAs));
        }

        @Override
        @Nullable
        public BlockState getPlacementState(ItemPlacementContext ctx) {
            BlockState superState = super.getPlacementState(ctx);
            if (superState == null) {
                superState = this.getDefaultState();
            }
            return (BlockState)superState.with(Properties.LIT, ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos()));
        }

        @Override
        protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
            super.appendProperties(builder);
            builder.add(Properties.LIT);
        }

        @Override
        public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
            if (world.isClient) {
                return;
            }
            boolean bl = state.get(Properties.LIT);
            if (bl != world.isReceivingRedstonePower(pos)) {
                if (bl) {
                    world.scheduleBlockTick(pos, this, 4);
                } else {
                    world.setBlockState(pos, (BlockState)state.cycle(Properties.LIT), Block.NOTIFY_LISTENERS);
                }
            }
        }

        @Override
        public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
            if (state.get(Properties.LIT).booleanValue() && !world.isReceivingRedstonePower(pos)) {
                world.setBlockState(pos, (BlockState)state.cycle(Properties.LIT), Block.NOTIFY_LISTENERS);
            }
        }
    }

}
