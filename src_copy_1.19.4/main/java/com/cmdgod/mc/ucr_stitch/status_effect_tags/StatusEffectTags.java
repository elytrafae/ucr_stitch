package com.cmdgod.mc.ucr_stitch.status_effect_tags;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.include.com.google.gson.Gson;

import com.cmdgod.mc.ucr_stitch.UCRStitch;
import com.google.gson.JsonElement;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagBuilder;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class StatusEffectTags {

    public static final TagKey<StatusEffect> POSITIVE = StatusEffectTags.of("positive");
    public static final TagKey<StatusEffect> NEGATIVE = StatusEffectTags.of("negative");
    public static final TagKey<StatusEffect> NEUTRAL = StatusEffectTags.of("neutral");
    
    private static TagKey<StatusEffect> of(String id) {
        return TagKey.of(RegistryKeys.STATUS_EFFECT, new Identifier(UCRStitch.MOD_NAMESPACE, id));
    }

    public static void initializeTags() {
        List<String> POSITIVE_LIST = new ArrayList<>(), NEGATIVE_LIST = new ArrayList<>(), NEUTRAL_LIST = new ArrayList<>();
        Registries.STATUS_EFFECT.forEach( (effect) -> {
            switch (effect.getCategory()) {
                case BENEFICIAL:
                    POSITIVE_LIST.add("\"" + Registries.STATUS_EFFECT.getId(effect).toString() + "\"");
                    break;
                case HARMFUL:
                    NEGATIVE_LIST.add("\"" + Registries.STATUS_EFFECT.getId(effect).toString() + "\"");
                    break;
                case NEUTRAL:
                    NEUTRAL_LIST.add("\"" + Registries.STATUS_EFFECT.getId(effect).toString() + "\"");
            }
        });
        System.out.println(POSITIVE_LIST);
        System.out.println(NEGATIVE_LIST);
        System.out.println(NEUTRAL_LIST);
        //Gson gson = new Gson();
        //System.out.println(gson.toJson((JsonElement)POSITIVE_LIST));
    }

}
