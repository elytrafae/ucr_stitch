package com.cmdgod.mc.ucr_stitch.registrers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
import com.cmdgod.mc.ucr_stitch.tools.MediaUtility;

import dev.emi.trinkets.api.SlotGroup;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.SlotType;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.power.factory.condition.EntityConditions;
import io.github.apace100.apoli.power.factory.condition.ItemConditions;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder.Living;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;

public class ModConditions {
    
    public static void registerAll() {

        register(new ConditionFactory<Entity>(new Identifier(UCRStitch.MOD_NAMESPACE, "media"), new SerializableData()
            .add("comparison", ApoliDataTypes.COMPARISON)
            .add("compare_to", SerializableDataTypes.INT),
            (data, entity) -> {
                if (!(entity instanceof ServerPlayerEntity)) {
                    return false;
                }
                ServerPlayerEntity player = (ServerPlayerEntity)entity;
                int media = MediaUtility.getMediaInInventory(player.getInventory());
                Comparison comparison = data.get("comparison");
                return comparison.compare(media, data.getInt("compare_to"));
            }));

        register(new ConditionFactory<Entity>(new Identifier(UCRStitch.MOD_NAMESPACE, "velocity"), new SerializableData()
            .add("comparison", ApoliDataTypes.COMPARISON)
            .add("compare_to", SerializableDataTypes.FLOAT),
            (data, entity) -> {
                Comparison comparison = data.get("comparison");
                return comparison.compare(entity.getVelocity().length(), data.getFloat("compare_to"));
            }));

        /*
        register(new ConditionFactory<Entity>(new Identifier(UCRStitch.MOD_NAMESPACE, "trinket_equipped_item"), new SerializableData()
            .add("slot_group", SerializableDataTypes.STRING)
            .add("slot_type", SerializableDataTypes.STRING)
            .add("item_condition", ApoliDataTypes.ITEM_CONDITION)
            .add("must_all_conform", SerializableDataTypes.BOOLEAN),
            (data, entity) -> {
                if (!(entity instanceof LivingEntity)) {
                    return false;
                }
                LivingEntity livingEntity = (LivingEntity)entity;
                Map<String, SlotGroup> slotMap = TrinketsApi.getEntitySlots(livingEntity.getWorld(), livingEntity.getType());
                SlotGroup group = slotMap.get(data.getString("slot_group"));
                if (group == null) {
                    return false;
                }
                SlotType type = group.getSlots().get(data.getString("slot_type"));
                if (type == null) {
                    return false;
                }
                Optional<TrinketComponent> opt = TrinketsApi.getTrinketComponent(livingEntity);
                if (!opt.isPresent()) {
                    return false;
                }
                TrinketComponent component = opt.get();
                List<Pair<SlotReference, ItemStack>> slots = component.getEquipped((stack) -> {return true;});
                for (int i=0; i < slots.size(); i++) {
                    Pair<SlotReference, ItemStack> pair = slots.get(i);
                    if (pair.getLeft().) {

                    }
                }
                return false;
            }));
        */

        register(new ConditionFactory<Entity>(new Identifier(UCRStitch.MOD_NAMESPACE, "trinket_has_item"), new SerializableData()
            .add("item_condition", ApoliDataTypes.ITEM_CONDITION),
            (data, entity) -> {
                if (!(entity instanceof LivingEntity)) {
                    return false;
                }
                LivingEntity livingEntity = (LivingEntity)entity;
                Optional<TrinketComponent> opt = TrinketsApi.getTrinketComponent(livingEntity);
                if (!opt.isPresent()) {
                    return false;
                }
                TrinketComponent component = opt.get();
                List<Pair<SlotReference, ItemStack>> slots = component.getAllEquipped();
                ConditionFactory<ItemStack>.Instance itemCondition = ((ConditionFactory<ItemStack>.Instance) data.get("item_condition"));
                for (int i=0; i < slots.size(); i++) {
                    if (itemCondition.test(slots.get(i).getRight())) {
                        return true;
                    }
                }
                return false;
            }));

    }

    private static void register(ConditionFactory<Entity> conditionFactory) {
        Registry.register(ApoliRegistries.ENTITY_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }

}
