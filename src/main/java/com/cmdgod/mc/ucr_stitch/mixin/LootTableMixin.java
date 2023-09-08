package com.cmdgod.mc.ucr_stitch.mixin;

import java.util.Optional;
import java.util.function.Consumer;

import org.apache.commons.lang3.mutable.MutableInt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.cmdgod.mc.ucr_stitch.items.OreNecklace;
import com.cmdgod.mc.ucr_stitch.registrers.ModAttributes;
import com.cmdgod.mc.ucr_stitch.tools.Utility;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.random.Random;

@Mixin(LootTable.class)
public class LootTableMixin {
    
    @Inject(at = @At("HEAD"), method = "generateLoot(Lnet/minecraft/loot/context/LootContext;)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;", cancellable = true)
    private void changeLootTable(LootContext context, CallbackInfoReturnable<ObjectArrayList<ItemStack>> info) {
        //System.out.println("generateLoot");
        if (context.get(LootContextParameters.BLOCK_STATE) != null) {
            info.setReturnValue(handleMiningLoot(context));
            info.cancel();
        }
        Entity bobber = context.get(LootContextParameters.THIS_ENTITY);
        if (bobber == null || !(bobber instanceof FishingBobberEntity)) {
            return;
        }
        FishingBobberEntity actualBobber = (FishingBobberEntity)bobber;
        int count = getFishCountFromChance(actualBobber.getPlayerOwner().getAttributeValue(ModAttributes.GENERIC_BONUS_FISH_CHANCE), Random.create());
        LootTable table = (LootTable)(Object)this;
        ObjectArrayList<ItemStack> objectArrayList = new ObjectArrayList<ItemStack>();
        for (int i=0; i < count; i++) {
            table.generateLoot(context, objectArrayList::add);
        }
        info.setReturnValue(objectArrayList);
        info.cancel();
    }

    /*
    @Inject(at = @At("HEAD"), method = "generateLoot(Lnet/minecraft/loot/context/LootContext;Ljava/util/function/Consumer;)V", cancellable = false)
    public void generateLoot(LootContext context, Consumer<ItemStack> lootConsumer, CallbackInfo info) {
        PlayerEntity player = null;
        Entity entity = context.get(LootContextParameters.THIS_ENTITY);
        if (entity instanceof PlayerEntity) {
            player = (PlayerEntity)entity;
        } else {
            player = context.get(LootContextParameters.LAST_DAMAGE_PLAYER);
        }

        if (player == null) {
            return;
        }

        LootContextParameters.TOOL
    }
    */

    private int getFishCountFromChance(double chance, Random random) {
        int nr = (int)Math.floor(chance);
        chance -= nr;
        if (chance > 0 && random.nextFloat() < chance) {
            nr++;
        }
        return nr;
    }

    private ObjectArrayList<ItemStack> handleMiningLoot(LootContext context) {
        MutableInt count = new MutableInt(1);
        LootTable table = (LootTable)(Object)this;
        ItemStack toolStack = context.get(LootContextParameters.TOOL);
        Entity entity = context.get(LootContextParameters.THIS_ENTITY);
        if ((entity instanceof ServerPlayerEntity) && (EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, toolStack) <= 0)) {
            ServerPlayerEntity player = (ServerPlayerEntity)entity;
            Optional<TrinketComponent> componentOpt = TrinketsApi.getTrinketComponent(player);
            if (componentOpt.isPresent()) {
                componentOpt.get().getEquipped((itemStack) -> {return itemStack.getItem() instanceof OreNecklace;}).forEach((pair) -> {
                    OreNecklace necklace = (OreNecklace)pair.getRight().getItem();
                    ServerWorld world = context.getWorld();
                    if (necklace.isPartOfLootTableList(world.getServer().getLootManager(), table) /*&& world.getRandom().nextInt(100) < 15*/) {
                        count.add(1);
                        pair.getRight().damage(necklace.getDamagePerRoll(), player, (p) -> {
                            Utility.playSound(player, SoundEvents.ENTITY_ITEM_BREAK, 1.0f, 1.4f);
                        });
                    }
                });
            }
            ;
        }
        ObjectArrayList<ItemStack> objectArrayList = new ObjectArrayList<ItemStack>();
        for (int i=0; i < count.getValue(); i++) {
            table.generateLoot(context, objectArrayList::add);
        }
        return objectArrayList;
    }

}
