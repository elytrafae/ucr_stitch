package com.cmdgod.mc.ucr_stitch.items;

import java.util.ArrayList;
import java.util.List;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
import com.cmdgod.mc.ucr_stitch.registrers.ModItems;

import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootTable;
import net.minecraft.util.Identifier;

public class OreNecklace extends TrinketItem {

    ArrayList<Identifier> lootTableIds;
    int damagePerRoll = 20;

    public OreNecklace(Settings settings, Identifier lootTable) {
        this(settings, new ArrayList<>(List.of(lootTable)));
    }

    public OreNecklace(Settings settings, ArrayList<Identifier> lootTables) {
        super(settings.maxDamageIfAbsent(200));
        this.lootTableIds = lootTables;
    }

    public boolean isPartOfLootTableList(LootManager manager, LootTable table) {
        for (int i=0; i < lootTableIds.size(); i++) {
            if (manager.getTable(lootTableIds.get(i)).equals(table)) {
                return true;
            }
        }
        return false;
    }

    public int getDamagePerRoll() {
        return UCRStitch.CONFIG.necklaceDamage();
    }

    public static ArrayList<Identifier> getRegularOreTables(String namespace, String oreName) {
        return new ArrayList<>(List.of(new Identifier(namespace, "blocks/deepslate_" + oreName + "_ore"), new Identifier(namespace, "blocks/" + oreName + "_ore"), new Identifier("meadow", "alpine_" + oreName + "_ore")));
    }

    public static ArrayList<Identifier> getRegularOreTables(String oreName) {
        return getRegularOreTables("minecraft", oreName);
    }
    
}
