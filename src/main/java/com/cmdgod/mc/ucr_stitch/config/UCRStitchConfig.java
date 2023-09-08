package com.cmdgod.mc.ucr_stitch.config;

import io.wispforest.owo.config.ConfigWrapper;
import io.wispforest.owo.config.Option;
import io.wispforest.owo.util.Observable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class UCRStitchConfig extends ConfigWrapper<com.cmdgod.mc.ucr_stitch.config.ConfigModel> {

    private final Option<java.lang.Integer> sugarcaneHeight = this.optionForKey(new Option.Key("sugarcaneHeight"));
    private final Option<java.lang.Integer> cactusHeight = this.optionForKey(new Option.Key("cactusHeight"));
    private final Option<java.lang.Integer> bookkeeperBundleSlots = this.optionForKey(new Option.Key("bookkeeperBundleSlots"));
    private final Option<java.lang.Integer> necklaceDamage = this.optionForKey(new Option.Key("necklaceDamage"));

    private UCRStitchConfig() {
        super(com.cmdgod.mc.ucr_stitch.config.ConfigModel.class);
    }

    public static UCRStitchConfig createAndLoad() {
        var wrapper = new UCRStitchConfig();
        wrapper.load();
        return wrapper;
    }

    public int sugarcaneHeight() {
        return sugarcaneHeight.value();
    }

    public void sugarcaneHeight(int value) {
        sugarcaneHeight.set(value);
    }

    public int cactusHeight() {
        return cactusHeight.value();
    }

    public void cactusHeight(int value) {
        cactusHeight.set(value);
    }

    public int bookkeeperBundleSlots() {
        return bookkeeperBundleSlots.value();
    }

    public void bookkeeperBundleSlots(int value) {
        bookkeeperBundleSlots.set(value);
    }

    public int necklaceDamage() {
        return necklaceDamage.value();
    }

    public void necklaceDamage(int value) {
        necklaceDamage.set(value);
    }




}

