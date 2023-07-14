package com.cmdgod.mc.ucr_stitch.villagers;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
import com.cmdgod.mc.ucr_stitch.dataconnection.WoodTypeAssociation;
import com.cmdgod.mc.ucr_stitch.dataconnection.WoodTypeAssociation.WoodTypeConnection;
import com.google.common.collect.ImmutableSet;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;

import java.util.function.Predicate;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.PotionUtil;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

public class VillagersRegister {

    private static ItemStack lumberAxe = createLumberAxe();
    private static ItemStack lumberDrink = createLumberDrink();

    public static PointOfInterestType LUMBERJACK_POI = registerPOI("lumberjack_poi", UCRStitch.WOODCUTTER_BLOCK);
    public static VillagerProfession LUMBERJACK = registerProfession("lumberjack", 
        RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, new Identifier(UCRStitch.MOD_NAMESPACE, "lumberjack_poi")), 
        SoundEvents.BLOCK_WOOD_HIT);
    
    public static VillagerProfession registerProfession(String name, RegistryKey<PointOfInterestType> poiType, SoundEvent sound) {
        Predicate<RegistryEntry<PointOfInterestType>> workstationPredicate = (entry) -> entry.matchesKey(poiType);
        return Registry.register(Registries.VILLAGER_PROFESSION, new Identifier(UCRStitch.MOD_NAMESPACE, name), 
            new VillagerProfession(
                name, 
                workstationPredicate, 
                workstationPredicate, 
                ImmutableSet.of(), 
                ImmutableSet.of(), 
                sound
            )
        );
    }

    public static PointOfInterestType registerPOI(String name, Block block) {
        return registerPOI(name, block, 1, 1);
    }

    public static PointOfInterestType registerPOI(String name, Block block, int ticketCount, int searchDistance) {
        return PointOfInterestHelper.register(new Identifier(UCRStitch.MOD_NAMESPACE, name), ticketCount, searchDistance, ImmutableSet.copyOf(block.getStateManager().getStates()));
    }

    public static void registerTrades() {
        TradeOfferHelper.registerVillagerOffers(LUMBERJACK, 1, 
            factories -> {
                factories.add(0, VillagerTrades.createLumberjackSaplingLogTrade());
                factories.add(1, (entity, random) -> {
                    return new TradeOffer(
                        new ItemStack(Items.STONE_AXE, 1),
                        new ItemStack(Items.EMERALD, 6),
                        3, 6, 0.02f);
                });
            }
        );

        TradeOfferHelper.registerVillagerOffers(LUMBERJACK, 2, 
            factories -> {
                factories.add(0, VillagerTrades.createLumberjackSaplingLogTrade());
                factories.add(1, VillagerTrades.createLumberjackSignTrade());
            }
        );

        TradeOfferHelper.registerVillagerOffers(LUMBERJACK, 3, 
            factories -> {
                factories.add(0, VillagerTrades.createLumberjackLogUncraftTrade());
                factories.add(1, (entity, random) -> {
                    return new TradeOffer(
                        new ItemStack(Items.IRON_AXE, 1),
                        new ItemStack(Items.EMERALD, 12),
                        3, 8, 0.02f);
                });
                factories.add(2, (entity, random) -> {
                    WoodTypeConnection wtc = WoodTypeAssociation.getRandom(random);
                    return new TradeOffer(
                        new ItemStack(Items.STICK, 20),
                        new ItemStack(wtc.getLog(), 3),
                        12, 2, 0.02f);
                });
            }
        );

        TradeOfferHelper.registerVillagerOffers(LUMBERJACK, 4, 
            factories -> {
                factories.add(0, VillagerTrades.createLumberjackStairTrade());
                factories.add(1, VillagerTrades.createLumberjackStairTrade());
            }
        );

        TradeOfferHelper.registerVillagerOffers(LUMBERJACK, 5, 
            factories -> {
                factories.add(0, (entity, random) -> new TradeOffer(
                    new ItemStack(Items.GOLDEN_AXE, 1),
                    new ItemStack(Items.EMERALD, 40),
                    lumberAxe.copy(),
                    3, 15, 0.02f
                ));
                factories.add(1, (entity, random) -> new TradeOffer(
                    new ItemStack(Items.GLASS_BOTTLE),
                    new ItemStack(Items.EMERALD, 25),
                    lumberDrink.copy(),
                    5, 10, 0.02f
                ));
            }
        );
    }

    private static ItemStack createLumberAxe() {
        ItemStack axe = new ItemStack(Items.GOLDEN_AXE);
        axe.addEnchantment(Enchantments.UNBREAKING, 4);
        axe.addEnchantment(Enchantments.EFFICIENCY, 6);
        axe.addEnchantment(Enchantments.FORTUNE, 6);
        axe.setCustomName(Text.of("Master Lumberjack's Axe"));
        return axe;
    }

    private static ItemStack createLumberDrink() {
        ItemStack drink = new ItemStack(Items.POTION);
        ImmutableSet<StatusEffectInstance> effects = ImmutableSet.of(
            new StatusEffectInstance(StatusEffects.HASTE, 3*60*20, 1),
            new StatusEffectInstance(StatusEffects.SPEED, 3*60*20, 0),
            new StatusEffectInstance(StatusEffects.WEAKNESS, 3*60*20, 1)
        );
        PotionUtil.setCustomPotionEffects(drink, effects);
        NbtCompound nbtCompound = drink.getNbt();
        if (nbtCompound != null) {
            nbtCompound.putInt(PotionUtil.CUSTOM_POTION_COLOR_KEY, PotionUtil.getColor(effects));
        }
        drink.setCustomName(Text.of("Master Lumberjack's Energizer"));
        return drink;
    }

    public static void registerVillagers() {
        UCRStitch.LOGGER.info("Villagers are being initialized!");

        
    }

}
