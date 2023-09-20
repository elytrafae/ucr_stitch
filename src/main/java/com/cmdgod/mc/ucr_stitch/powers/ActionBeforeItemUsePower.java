package com.cmdgod.mc.ucr_stitch.powers;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.function.Consumer;
import java.util.function.Predicate;

import com.cmdgod.mc.ucr_stitch.UCRStitch;

public class ActionBeforeItemUsePower extends Power {

    private final Predicate<ItemStack> itemCondition;
    private final Consumer<Entity> entityAction;
    private final Consumer<Pair<World, ItemStack>> itemAction;
    private final boolean instant;

    public ActionBeforeItemUsePower(PowerType<?> type, LivingEntity entity, Predicate<ItemStack> itemCondition, Consumer<Entity> entityAction, Consumer<Pair<World, ItemStack>> itemAction, boolean instant) {
        super(type, entity);
        this.itemCondition = itemCondition;
        this.entityAction = entityAction;
        this.itemAction = itemAction;
        this.instant = instant;
    }

    public boolean isInstant() {
        return instant;
    }

    public boolean doesApply(ItemStack stack) {
        return itemCondition == null || itemCondition.test(stack);
    }

    public void executeActions(Pair<World, ItemStack> pair) {
        if(itemAction != null) {
            itemAction.accept(pair);
        }
        if(entityAction != null) {
            entityAction.accept(entity);
        }
    }

    public static PowerFactory createFactory() {
        return new PowerFactory<>(new Identifier(UCRStitch.MOD_NAMESPACE, "action_before_item_use"),
            new SerializableData()
                .add("entity_action", ApoliDataTypes.ENTITY_ACTION, null)
                .add("item_action", ApoliDataTypes.ITEM_ACTION, null)
                .add("item_condition", ApoliDataTypes.ITEM_CONDITION, null)
                .add("instant", SerializableDataTypes.BOOLEAN, false),
            data ->
                (type, player) -> new ActionBeforeItemUsePower(type, player,
                    (ConditionFactory<ItemStack>.Instance)data.get("item_condition"),
                    (ActionFactory<Entity>.Instance)data.get("entity_action"),
                    (ActionFactory<Pair<World, ItemStack>>.Instance)data.get("item_action"),
                    data.getBoolean("instant")))
            .allowCondition();
    }
}
