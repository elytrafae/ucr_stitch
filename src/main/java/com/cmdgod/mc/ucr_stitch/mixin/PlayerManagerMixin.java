package com.cmdgod.mc.ucr_stitch.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
import com.cmdgod.mc.ucr_stitch.mixininterfaces.IPlayerEntityMixin;
import com.cmdgod.mc.ucr_stitch.registrers.ModStatusEffects;
import com.cmdgod.mc.ucr_stitch.tools.ElytraUpgradeUtil;
import com.cmdgod.mc.ucr_stitch.tools.Utility;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.Power;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

@SuppressWarnings("rawtypes")
@Mixin(value = PlayerManager.class)
public class PlayerManagerMixin {
    
    @Inject(method = "respawnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;onSpawn()V"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void invokePowerRespawnCallback(ServerPlayerEntity player, boolean alive, CallbackInfoReturnable<ServerPlayerEntity> cir, BlockPos blockPos, float f, boolean bl, ServerWorld serverWorld, Optional optional2, ServerWorld serverWorld2, ServerPlayerEntity serverPlayerEntity, boolean b) {
		// "serverPlayerEntity" is the new player entity. IDK what kind of witchcraft grabs it

		// Only do this if the old player *is* dead!
		if (!alive) {
			IPlayerEntityMixin p = Utility.getInterfacePlayer(serverPlayerEntity);
        	p.disablePVP();
			p.setPvpToggleBan(UCRStitch.CONFIG.pvpToggleBan());
		} else {
			IPlayerEntityMixin pNew = Utility.getInterfacePlayer(serverPlayerEntity);
			IPlayerEntityMixin pOld = Utility.getInterfacePlayer(serverPlayerEntity);
			pNew.copyPvPData(pOld);
		}
		

		ElytraUpgradeUtil.revokeAllElytraPowers(serverPlayerEntity);
		ItemStack stack = serverPlayerEntity.getEquippedStack(EquipmentSlot.CHEST);
		if ((!stack.isEmpty()) && (stack.getItem() instanceof ElytraItem)) {
			ElytraUpgradeUtil.giveEntityElytraPowers(stack, serverPlayerEntity);
		}
	}

}
