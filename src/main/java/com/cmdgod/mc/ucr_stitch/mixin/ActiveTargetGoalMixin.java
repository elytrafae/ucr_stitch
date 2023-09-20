package com.cmdgod.mc.ucr_stitch.mixin;

import java.util.Iterator;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.cmdgod.mc.ucr_stitch.powers.IronGolemAggroPower;
import com.cmdgod.mc.ucr_stitch.powers.VillagerScarePower;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.TargetActionOnHitPower;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(ActiveTargetGoal.class)
public abstract class ActiveTargetGoalMixin<T> {

    
    
    @Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/entity/mob/MobEntity;Ljava/lang/Class;IZZLjava/util/function/Predicate;)V")
	private void ironGolemAggro(MobEntity mob, Class<T> targetClass, int reciprocalChance, boolean checkVisibility, boolean checkCanNavigate, @Nullable Predicate<LivingEntity> targetPredicate, CallbackInfo info) {
		// System.out.println("This line is printed by by the active target goal mixin!");
        // TODO: Add functionality
        // TODO: Then test if it's too laggy or not
        // Alternative: A power that deals 1 DMG to Iron Golems every few seconds
        if (!(mob instanceof IronGolemEntity)) {
            return;
        }
        IronGolemEntity golem = (IronGolemEntity)mob;
        ActiveTargetGoal goal = (ActiveTargetGoal)(Object)this;
        TargetPredicate oldPred = ((ActiveTargetGoalAccessor)goal).getTargetPredicate();
        oldPred.setPredicate(targetPredicate.or((entity) -> {
            return getPowerStatus(entity, golem);
        }));
	}

    private boolean getPowerStatus(LivingEntity entity, IronGolemEntity golem) {
        Iterator<IronGolemAggroPower> iter = PowerHolderComponent.getPowers(entity, IronGolemAggroPower.class).iterator();
        while (iter.hasNext()) {
            IronGolemAggroPower power = iter.next();
            if ((power.doesAffectNatural() && !golem.isPlayerCreated()) || (power.doesAffectArtificial() && golem.isPlayerCreated())) {
                return true;
            }
        }
        return false;
    }

}
