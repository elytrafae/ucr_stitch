package com.cmdgod.mc.ucr_stitch.mixin;

import org.apache.logging.log4j.core.jmx.Server;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.cmdgod.mc.ucr_stitch.tools.ElytraUpgradeUtil;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    
    private int notSyncedServerTick = 0;

    @Inject(at = @At("HEAD"), method = "tick()V")
    private void serverTick(CallbackInfo info) {
        ServerPlayerEntity self = (ServerPlayerEntity)(Object)this;
        notSyncedServerTick++;
        if (notSyncedServerTick >= 100) {
            ElytraUpgradeUtil.syncPowerWithReality(self);
            notSyncedServerTick = 0;
        }
    }

}
