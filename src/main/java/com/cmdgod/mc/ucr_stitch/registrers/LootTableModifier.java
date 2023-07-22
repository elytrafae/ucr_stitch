package com.cmdgod.mc.ucr_stitch.registrers;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Set;

import com.cmdgod.mc.ucr_stitch.UCRStitch;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.SetNameLootFunction;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class LootTableModifier {

    private static final Identifier RANDOM_PROFESSION_LOOT_TABLE = new Identifier(UCRStitch.MOD_NAMESPACE, "random_villager_profession");

    // Decided to not add it for balancing and technical concerns with workstations
    private static void addRandomProfessionLootTable() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (RANDOM_PROFESSION_LOOT_TABLE.equals(id)) {
                LootPool.Builder pool = LootPool.builder();
                Set<Identifier> professions = Registry.VILLAGER_PROFESSION.getIds();
                Iterator<Identifier> profession = professions.iterator();
                while (profession.hasNext()) {
                    pool = pool.with(ItemEntry.builder(Items.STONE).apply(SetNameLootFunction.builder(Text.of(profession.toString()))));
                }
                tableBuilder.pool(pool);
            }
        });
    }

}