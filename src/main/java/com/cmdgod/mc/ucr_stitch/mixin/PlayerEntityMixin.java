package com.cmdgod.mc.ucr_stitch.mixin;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
import com.cmdgod.mc.ucr_stitch.blocks.CustomBlockEventListener;
import com.cmdgod.mc.ucr_stitch.gui.PVPToggleScreen;
import com.cmdgod.mc.ucr_stitch.mixininterfaces.IPlayerEntityMixin;
import com.cmdgod.mc.ucr_stitch.networking.PVPTogglePacket;
import com.cmdgod.mc.ucr_stitch.networking.RecallParticlePacket;
import com.cmdgod.mc.ucr_stitch.powers.ForceEdgeClipPower;
import com.cmdgod.mc.ucr_stitch.powers.PreventDismountPower;
import com.cmdgod.mc.ucr_stitch.tools.Utility;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.command.TeleportCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements IPlayerEntityMixin {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
        //TODO Auto-generated constructor stub
    }

    private static final String CUSTOM_OBJ_KEY = "UCRStitchData";
    private static final String ASSIST_TICKS_KEY = "AssistTicks";
    private static final String PVP_TOGGLE_BAN_KEY = "PVPToggleBan";
    private static final String PVP_OFF_TIME_KEY = "PVPOffTimer";
    private static final String PVP_KEY = "PVP";

    private static final String PVP_OFF_POS_X_KEY = "PVPOffPosX";
    private static final String PVP_OFF_POS_Y_KEY = "PVPOffPosY";
    private static final String PVP_OFF_POS_Z_KEY = "PVPOffPosZ";
    

    private int sneakTick = 0;
    private int assistTicks = 0;
    private boolean isPvpOn = false;
    private int pvpToggleBan = UCRStitch.CONFIG.pvpToggleBan();
    private int pvpOffTimer = -1;

    private Vec3d pvpOffPos = new Vec3d(0, 0, 0);

    private PVPToggleScreen lastScreen = null;

    @Inject(at = @At("HEAD"), method = "shouldDismount()Z", cancellable = true)
    protected void shouldDismount(CallbackInfoReturnable<Boolean> info) {
        PlayerEntity self = (PlayerEntity)(Object)this;
        if (PowerHolderComponent.hasPower(self, PreventDismountPower.class)) {
            info.setReturnValue(false);
            info.cancel();
        }
    }
    
    @Inject(at = @At("HEAD"), method = "tick()V")
    public void tick(CallbackInfo info) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        if (player.isAlive()) {
            pvpTick();
        }
        if (player.isSneaky()) {
            sneakTick++;
        } else {
            sneakTick = 0;
        }

        if (isFirstTickSneak() && player.isOnGround()) {
            World world = player.getWorld();
            BlockPos pos = player.getLandingPos();
            BlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            if (block instanceof CustomBlockEventListener) {
                ((CustomBlockEventListener)block).onFirstSneakTick(world, pos, state, player);
            }
        }

        /*
        if (player.isFallFlying() && player instanceof ClientPlayerEntity) {
            //ElytraEntityModel;
            // FireworkRocketItem;
            // PlayerEntityRenderer;
            ClientPlayerEntity clinetPlayer = (ClientPlayerEntity)player;
            ElytraTrail trail = new RedElytraTrail();
            World world = player.getWorld();
            Vec3d velocity = player.getVelocity();
            Vec3d position = player.getPos();
            Vec3d offset = velocity.normalize().multiply(trail.getFrequency());
            int i=0;
            
            while (i==0 || offset.multiply(i).lengthSquared() < velocity.lengthSquared()) {
                Vec3d particlePos = position.subtract(offset.multiply(i));
                trail.renderParticle(world, clinetPlayer, particlePos);
                i++;
            }
        }
        */
    }

    private void pvpTick() {
        PlayerEntity player = (PlayerEntity)(Object)this;
        if (assistTicks > 0) {
            assistTicks--;
        }
        if (pvpToggleBan > 0) {
            pvpToggleBan--;
        }
        if (pvpOffTimer > 0 && player instanceof ServerPlayerEntity) {
            //player.setPos(pvpOffPos.x, pvpOffPos.y, pvpOffPos.z);
            //player.setVelocity(0, 0, 0);
            //player.velocityDirty = true;
            //TeleportCommand;
            Set<PlayerPositionLookS2CPacket.Flag> set = EnumSet.noneOf(PlayerPositionLookS2CPacket.Flag.class);
            player.teleport(pvpOffPos.x, pvpOffPos.y, pvpOffPos.z, false);
            ((ServerPlayerEntity)player).networkHandler.requestTeleport(pvpOffPos.x, pvpOffPos.y, pvpOffPos.z, player.getYaw(), player.getPitch(), set);
            if (pvpOffTimer % 10 == 0) {
                spawnRecallParticles();
            }
            if (pvpOffTimer % 20 == 0) {
                Utility.playSound(player, SoundEvents.UI_BUTTON_CLICK, 1f, pvpOffTimer % 40 == 0 ? 0.6f : 0.8f);
                player.sendMessage(Text.of("PVP off in " + pvpOffTimer/20 + " seconds!").getWithStyle(Style.EMPTY.withColor(Formatting.GOLD)).get(0), true);
            }
            pvpOffTimer--;
            if (pvpOffTimer == 0) {
                pvpOffTimer = -1;
                disablePVP();
            }
        }
    }

    @Inject( at = @At("RETURN"), method = "clipAtLedge()Z", cancellable = true)
    protected void clipAtLedge(CallbackInfoReturnable<Boolean> info) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        List<ForceEdgeClipPower> powers = PowerHolderComponent.getPowers(player, ForceEdgeClipPower.class);
        if (powers.size() <= 0) {
            // If there are no powers like these, don't do anything.
            return;
        }
        info.setReturnValue(powers.get(0).isClippingOn());
    }


    public boolean isFirstTickSneak() {
        return sneakTick == 1;
    }

    public int getAssistedKillTicks() {
        return assistTicks;
    }

    @Inject(at = @At("HEAD"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", cancellable = true)
	private void damage(DamageSource source, float damage, CallbackInfoReturnable<Boolean> info) {
        PlayerEntity self = (PlayerEntity)(Object)this;
        PlayerEntity player = getCausedPlayer(source);

        if (pvpOffTimer > 0) {
            cancelPvpOff();
        }

        if (Utility.canHarmEachOther(self, player)) {
            if (!self.equals(player)) {
                assistTicks = UCRStitch.CONFIG.assistTicks();
            }
            return;
        }
        info.setReturnValue(false);
        info.cancel();
	}

    @Inject(at = @At("HEAD"), method = "onDeath(Lnet/minecraft/entity/damage/DamageSource;)V", cancellable = false)
    public void onDeath(DamageSource damageSource, CallbackInfo info) {
        //disablePVP();
        pvpToggleBan = UCRStitch.CONFIG.pvpToggleBan();
    }

    private PlayerEntity getCausedPlayer(DamageSource source) {
        Entity entity = source.getAttacker();
        while (entity != null) {
            if (entity instanceof PlayerEntity) {
                return (PlayerEntity)entity;
            } else if (entity instanceof ProjectileEntity) {
                entity = ((ProjectileEntity)entity).getOwner();
            } else if (entity instanceof Tameable) {
                entity = ((Tameable)entity).getOwner();
            } else if (entity instanceof TntEntity) {
                entity = ((TntEntity)entity).getCausingEntity();
            } else {
                entity = null;
            }
        }
        return null;
    }

    @Inject(at = @At("RETURN"), method = "writeCustomDataToNbt(Lnet/minecraft/nbt/NbtCompound;)V")
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        NbtCompound myNbt = new NbtCompound();
        myNbt.putInt(ASSIST_TICKS_KEY, assistTicks);
        myNbt.putInt(PVP_TOGGLE_BAN_KEY, pvpToggleBan);
        myNbt.putInt(PVP_OFF_TIME_KEY, pvpOffTimer);
        myNbt.putBoolean(PVP_KEY, isPvpOn);

        if (pvpOffTimer > 0) {
            myNbt.putDouble(PVP_OFF_POS_X_KEY, pvpOffPos.x);
            myNbt.putDouble(PVP_OFF_POS_Y_KEY, pvpOffPos.y);
            myNbt.putDouble(PVP_OFF_POS_Z_KEY, pvpOffPos.z);
        }

        nbt.put(CUSTOM_OBJ_KEY, myNbt);
    }

    @Inject(at = @At("RETURN"), method = "readCustomDataFromNbt(Lnet/minecraft/nbt/NbtCompound;)V")
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        PlayerEntity self = (PlayerEntity)(Object)this;
        NbtCompound myNbt;
        if (nbt.contains(CUSTOM_OBJ_KEY, NbtCompound.COMPOUND_TYPE)) {
            myNbt = nbt.getCompound(CUSTOM_OBJ_KEY);
        } else {
            myNbt = new NbtCompound();
        }

        assistTicks = getOptionalInt(myNbt, ASSIST_TICKS_KEY, 0);
        pvpToggleBan = getOptionalInt(myNbt, PVP_TOGGLE_BAN_KEY, UCRStitch.CONFIG.pvpToggleBan());
        pvpOffTimer = getOptionalInt(myNbt, PVP_OFF_TIME_KEY, -1);
        if (pvpOffTimer > 0) {
            double x = getOptionalDouble(myNbt, PVP_OFF_POS_X_KEY, self.getX());
            double y = getOptionalDouble(myNbt, PVP_OFF_POS_Y_KEY, self.getY());
            double z = getOptionalDouble(myNbt, PVP_OFF_POS_Z_KEY, self.getZ());
            pvpOffPos = new Vec3d(x, y, z);
        }
        isPvpOn = myNbt.getBoolean(PVP_KEY);
        //self.sendMessage(Text.of("PVPBan from NBT: " + pvpToggleBan), false);
    }

    private int getOptionalInt(NbtCompound compound, String key, int defVal) {
        if (compound.contains(key, NbtCompound.INT_TYPE)) {
            return compound.getInt(key);
        } else {
            return defVal;
        }
    }

    private double getOptionalDouble(NbtCompound compound, String key, double defVal) {
        if (compound.contains(key, NbtCompound.DOUBLE_TYPE)) {
            return compound.getDouble(key);
        } else {
            return defVal;
        }
    }

    @Override
    public boolean getPVPStatus() {
        return isPvpOn;
    }

    @Override
    public void enablePVP() {
        PlayerEntity self = (PlayerEntity)(Object)this;
        isPvpOn = true;
        Utility.playSound(self, SoundEvents.ENTITY_PLAYER_LEVELUP, 3f, 0.7f);
        self.sendMessage(Text.of("PVP is now turned on for you!").getWithStyle(Style.EMPTY.withColor(Formatting.GREEN)).get(0), false);
        pvpToggleBan = 100;
    }

    @Override
    public void disablePVP() {
        PlayerEntity self = (PlayerEntity)(Object)this;
        isPvpOn = false;
        Utility.playSound(self, SoundEvents.ENTITY_VILLAGER_YES, 3f, 0.8f);
        self.sendMessage(Text.of("PVP is now turned off for you!").getWithStyle(Style.EMPTY.withColor(Formatting.GREEN)).get(0), false);
        pvpToggleBan = 100;
    }

    @Override
    public void startDisablingPVP() {
        PlayerEntity self = (PlayerEntity)(Object)this;
        pvpOffTimer = UCRStitch.CONFIG.pvpOffTime();
        Utility.playSound(self, SoundEvents.BLOCK_BELL_USE, 3f, 0.5f);
        Utility.playSound(self, SoundEvents.BLOCK_BELL_RESONATE, 3f, 0.5f);
        pvpOffPos = new Vec3d(self.getX(), self.getY(), self.getZ());
    }

    @Override
    public void setPvpToggleBan(int time) {
        this.pvpToggleBan = time;
        PlayerEntity self = (PlayerEntity)(Object)this;
        //self.sendMessage(Text.of("Set PVP Toggle Ban to " + time), false);
    }

    private void spawnRecallParticles() {
        PlayerEntity player = (PlayerEntity)(Object)this;
        World world = player.getWorld();
        world.getPlayers().forEach((player2) -> {
            if (player2.distanceTo(player) < 100) {
                UCRStitch.RECALL_PARTICLE_CHANNEL.serverHandle(player).send(new RecallParticlePacket(player.getPos()));
            }
        });
        
    }

    private void cancelPvpOff() {
        PlayerEntity self = (PlayerEntity)(Object)this;
        pvpOffTimer = -1;
        isPvpOn = true;
        pvpToggleBan = UCRStitch.CONFIG.pvpToggleBan()/4;
        self.sendMessage(Text.of("PVP off ritual canceled!").getWithStyle(Style.EMPTY.withColor(Formatting.BLACK)).get(0), false);
        Utility.playSound(self, SoundEvents.ENTITY_VILLAGER_DEATH, 3f, 0.6f);
    }

    @Override
    public void togglePVPRequest() {
        PlayerEntity self = (PlayerEntity)(Object)this;
        if (!(self instanceof ServerPlayerEntity)) {
            return;
        }
        if (pvpToggleBan > 0 || pvpOffTimer > 0) {
            self.sendMessage(Text.translatable("message.ucr_stitch.cannot_toggle_pvp", Math.max(pvpToggleBan, pvpOffTimer)/20), true);
            Utility.playSound(self, SoundEvents.ENTITY_VILLAGER_NO, 2f, 0.6f);
            return;
        }
        if (lastScreen == null || !lastScreen.isOpen()) {
            lastScreen = new PVPToggleScreen((ServerPlayerEntity)self, !getPVPStatus());
            lastScreen.open();
        }
    }



}
