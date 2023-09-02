package com.cmdgod.mc.ucr_stitch.registrers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.cmdgod.mc.ucr_stitch.UCRStitch;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceWithLootingLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetNameLootFunction;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class LootTableModifier {

    private static final Identifier RANDOM_PROFESSION_LOOT_TABLE = new Identifier(UCRStitch.MOD_NAMESPACE, "random_villager_profession");
    private static HashMap<Identifier, Double> NAUTILUS_CHANCES = new HashMap<>();

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

    private static void addHeadsToZombieLootTables() {
		Identifier ZOMBIE_LOOT_DROP_ID = new Identifier("minecraft:entities/zombie");
		Identifier ZOMBIE_VILLAGER_LOOT_DROP_ID = new Identifier("minecraft:entities/zombie_villager");

		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			// Let's only modify built-in loot tables and leave data pack loot tables untouched by checking the source.
			// We also check that the loot table ID is equal to the ID we want.
			if (source.isBuiltin() && (ZOMBIE_LOOT_DROP_ID.equals(id) || ZOMBIE_VILLAGER_LOOT_DROP_ID.equals(id))) {
				LootPool.Builder poolBuilder = LootPool.builder()
												.with(ItemEntry.builder(Items.ZOMBIE_HEAD).weight(1))
												.conditionally(RandomChanceWithLootingLootCondition.builder(0.05f, 0.02f));
 
        		tableBuilder.pool(poolBuilder);
			}
		});
	}

    private static void addExtraNautilus() {
        NAUTILUS_CHANCES.put(new Identifier("minecraft:entities/drowned"), 0.01);
        NAUTILUS_CHANCES.put(new Identifier("minecraft:entities/guardian"), 0.1);

        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			if (source.isBuiltin() && NAUTILUS_CHANCES.containsKey(id)) {
                double d = NAUTILUS_CHANCES.get(id);
                float f = (float)d;
				LootPool.Builder poolBuilder = LootPool.builder()
												.with(ItemEntry.builder(Items.NAUTILUS_SHELL).weight(1))
												.conditionally(RandomChanceWithLootingLootCondition.builder(f, 0.01f));
 
        		tableBuilder.pool(poolBuilder);
			}
		});
    }

    private static void addRedSandToHusks() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			if (source.isBuiltin() && id.equals(new Identifier("minecraft:entities/husk"))) {
				LootPool.Builder poolBuilder = LootPool.builder()
												.with(ItemEntry.builder(Items.RED_SAND).weight(1))
                                                .with(ItemEntry.builder(Items.RED_SANDSTONE).weight(1))
												.conditionally(RandomChanceWithLootingLootCondition.builder(0.2f, 0.01f));
 
        		tableBuilder.pool(poolBuilder);
			}
		});
    }

    public static void doAllChanges() {
        UCRStitch.LOGGER.info("UCR Stitch: Loot tables modified!");
        addHeadsToZombieLootTables();
        addExtraNautilus();
        addRedSandToHusks();
    }

}