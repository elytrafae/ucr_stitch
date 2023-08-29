package com.cmdgod.mc.ucr_stitch.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.cmdgod.mc.ucr_stitch.items.CustomFishingRodItem;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents.Custom;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.math.random.Random;

@Mixin(LootTable.class)
public class LootTableMixin {
    
    @Inject(at = @At("HEAD"), method = "generateLoot(Lnet/minecraft/loot/context/LootContext;)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;", cancellable = true)
    private void changeFishingLootTable(LootContext context, CallbackInfoReturnable<ObjectArrayList<ItemStack>> info) {
        //System.out.println("generateLoot");
        Entity bobber = context.get(LootContextParameters.THIS_ENTITY);
        if (bobber == null || !(bobber instanceof FishingBobberEntity)) {
            return;
        }
        //System.out.println("Fishing Loot");
        ItemStack tool = context.get(LootContextParameters.TOOL);
        Item item = tool.getItem();
        if (!(item instanceof CustomFishingRodItem)) {
            return;
        }
        //System.out.println("Special Fishing!");
        LootTable table = (LootTable)(Object)this;
        CustomFishingRodItem rod = (CustomFishingRodItem)item;
        int count = rod.getFishCount(Random.create());
        ObjectArrayList<ItemStack> objectArrayList = new ObjectArrayList<ItemStack>();
        for (int i=0; i < count; i++) {
            table.generateLoot(context, objectArrayList::add);
        }
        info.setReturnValue(objectArrayList);
        info.cancel();
    }

}
