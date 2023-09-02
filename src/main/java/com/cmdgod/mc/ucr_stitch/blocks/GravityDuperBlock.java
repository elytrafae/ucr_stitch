package com.cmdgod.mc.ucr_stitch.blocks;

import java.util.List;

import com.cmdgod.mc.ucr_stitch.blockentities.GravityDuperBlockEntity;
import com.cmdgod.mc.ucr_stitch.registrers.ModBlocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class GravityDuperBlock extends BlockWithEntity {

    public static final BooleanProperty WORKING = BooleanProperty.of("working"); // Whether or not it has fuel
    public static final BooleanProperty STUCK = BooleanProperty.of("stuck"); // Whether or not its recipe was removed!

    public GravityDuperBlock() {
        super(FabricBlockSettings.of(Material.STONE, MapColor.PALE_PURPLE).hardness(3.5f).resistance(9.5f));
        setDefaultState(getDefaultState().with(WORKING, false).with(STUCK, false));
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new GravityDuperBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult blockHitResult) {
        Inventory blockEntity = (Inventory) world.getBlockEntity(blockPos);
        ItemStack handStack = player.getStackInHand(hand);

        ItemStack fuelStack = blockEntity.getStack(GravityDuperBlockEntity.FUEL_SLOT);

        if (!GravityDuperBlockEntity.isStackFuel(fuelStack) && !fuelStack.isEmpty()) {
            player.getInventory().offerOrDrop(fuelStack.copy());
            playItemGetSound(player);
            fuelStack = ItemStack.EMPTY;
            blockEntity.setStack(GravityDuperBlockEntity.FUEL_SLOT, fuelStack);
            blockEntity.markDirty();
        }

        if (GravityDuperBlockEntity.isStackFuel(handStack) && (fuelStack.isEmpty() || ItemStack.canCombine(handStack, fuelStack))) {
            int blockCount = 0;
            int maxCount = handStack.getMaxCount();
            int handCount = handStack.getCount();
            if (!fuelStack.isEmpty()) {
                blockCount = fuelStack.getCount();
            }
            int transferCount = Math.min(maxCount - blockCount, handCount);
            
            if (fuelStack.isEmpty()) {
                fuelStack = new ItemStack(handStack.getItem(), transferCount);
                blockEntity.setStack(GravityDuperBlockEntity.FUEL_SLOT, fuelStack);
            } else {
                fuelStack.increment(transferCount);
            }
            handStack.decrement(transferCount);
            blockEntity.markDirty();
            //world.getBlockEntity(blockPos).markDirty();
            return ActionResult.SUCCESS;
        }

        if (handStack.isEmpty() || (ItemStack.canCombine(handStack, blockEntity.getStack(GravityDuperBlockEntity.OUTPUT_SLOT)))) {
            if (!blockEntity.getStack(GravityDuperBlockEntity.OUTPUT_SLOT).isEmpty()) {
                playItemGetSound(player);
            }
            player.getInventory().offerOrDrop(blockEntity.getStack(GravityDuperBlockEntity.OUTPUT_SLOT));
            blockEntity.setStack(GravityDuperBlockEntity.OUTPUT_SLOT, ItemStack.EMPTY);
            blockEntity.markDirty();
            //world.getBlockEntity(blockPos).markDirty();
            return ActionResult.SUCCESS;
        }
        
 
        return ActionResult.PASS;
    }

    private void playItemGetSound(PlayerEntity player) {
        //if (player.getWorld().isClient) {
            player.playSound(SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 1, 1);
        //}
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        // With inheriting from BlockWithEntity this defaults to INVISIBLE, so we need to change that!
        return BlockRenderType.MODEL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlocks.GRAVITY_DUPER_ENTITY, (world1, pos, state1, be) -> GravityDuperBlockEntity.tick(world1, pos, state1, be));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WORKING).add(STUCK);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.isOf(newState.getBlock())) {
            return;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof Inventory) {
            ItemScatterer.spawn(world, pos, (Inventory)((Object)blockEntity));
            world.updateComparators(pos, this);
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }
    
    @Override
    public void appendTooltip(ItemStack itemStack, BlockView world, List<Text> tooltip, TooltipContext tooltipContext) {
        if (!itemStack.getOrCreateNbt().contains("BlockEntityTag", NbtElement.COMPOUND_TYPE)) {
            tooltip.add(Text.translatable("block.ucr_stitch.gravity_duper.error").formatted(Formatting.RED));
            return;
        }
        NbtCompound blockEntityNbt = itemStack.getOrCreateNbt().getCompound("BlockEntityTag");
        Identifier blockId = null;
        if (blockEntityNbt.contains(GravityDuperBlockEntity.BLOCK_KEY, NbtElement.STRING_TYPE)) {
            blockId = new Identifier(blockEntityNbt.getString(GravityDuperBlockEntity.BLOCK_KEY));
        }
        if (blockId != null && Registry.ITEM.containsId(blockId)) {
            tooltip.add(Text.translatable("block.ucr_stitch.gravity_duper.duped_block", Text.translatable(Registry.ITEM.get(blockId).getTranslationKey())).formatted(Formatting.GRAY));
        } else {
            tooltip.add(Text.translatable("block.ucr_stitch.gravity_duper.error").formatted(Formatting.RED));
        }
    }

}
