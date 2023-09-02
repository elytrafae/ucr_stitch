package com.cmdgod.mc.ucr_stitch.blocks;

import com.cmdgod.mc.ucr_stitch.registrers.ModTags;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TelevatorBlock extends Block implements CustomBlockEventListener {

    //HashMap<PlayerEntity, Boolean> sneakStatus = new HashMap<PlayerEntity,Boolean>();
    public static final int MAX_TP_CHECK = 22;
    public static final SoundEvent TELEPORT_SOUND = SoundEvents.ENTITY_ENDERMAN_TELEPORT;
    public static final SoundEvent FAIL_SOUND = SoundEvents.ENTITY_ENDERMAN_HURT;

    public TelevatorBlock(Settings settings) {
        super(settings);
    }
    
    /*
    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (world.isClient || !(entity instanceof PlayerEntity)) {
            return;
        }
        PlayerEntity player = (PlayerEntity)entity;
        if (((IPlayerEntityMixin) player).isFirstTickSneak()) {
            attemptTeleport(world, pos, player, Direction.DOWN);
        }
    }
    */

    private boolean attemptTeleport(World world, BlockPos pos, LivingEntity entity, Direction dir, float pitch) {
        if (world.isClient) {
            return false;
        }
        Vec3d pastPos = entity.getPos();
        for (int i=1; i <= MAX_TP_CHECK; i++) {
            BlockPos newPos = pos.offset(dir, i);
            if ((!world.isInBuildLimit(newPos)) || newPos.getY() <= world.getBottomY()) {
                return false;
            }
            BlockState newState = world.getBlockState(newPos);
            if (newState.isOf(this)) {
                // This part of the code got WAAAAAAY too complicated just because I wanted to make the televators coverable with carpets
                BlockPos carpetPos = newPos.up();
                BlockState carpetState = world.getBlockState(carpetPos);
                boolean wasCarpet = carpetState.isIn(ModTags.Blocks.ALL_CARPETS);
                if (wasCarpet) {
                    //world.removeBlock(carpetPos, false);
                    world.setBlockState(carpetPos, Blocks.AIR.getDefaultState(), Block.FORCE_STATE + Block.SKIP_LIGHTING_UPDATES);
                }
                boolean tpSuccess = entity.teleport(newPos.getX() + 0.5f, newPos.getY()+1, newPos.getZ() + 0.5f, false);
                if (wasCarpet) {
                    world.setBlockState(carpetPos, carpetState, Block.FORCE_STATE + Block.SKIP_LIGHTING_UPDATES);
                }
                if (tpSuccess) {
                    world.playSound(null, pastPos.getX(), pastPos.getY(), pastPos.getZ(), TELEPORT_SOUND, SoundCategory.PLAYERS, 1.0f, pitch);
                    world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), TELEPORT_SOUND, SoundCategory.PLAYERS, 1.0f, pitch);
                    entity.playSound(TELEPORT_SOUND, 1.0f, pitch);
                    return true;
                }
            }
        }
        world.playSound(null, pastPos.getX(), pastPos.getY(), pastPos.getZ(), FAIL_SOUND, SoundCategory.PLAYERS, 1.0f, 1.3f);
        entity.playSound(FAIL_SOUND, 1.0f, 1.3f);
        return false;
    }

    @Override
    public void onFirstSneakTick(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        attemptTeleport(world, pos, player, Direction.DOWN, 1.2f);
    }

    @Override
    public boolean onFirstJumpTick(World world, BlockPos pos, BlockState state, LivingEntity entity) {
        attemptTeleport(world, pos, entity, Direction.UP, 1.4f);
        return false;
    }

}
