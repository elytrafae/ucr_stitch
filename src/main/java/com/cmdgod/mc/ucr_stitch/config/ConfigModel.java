package com.cmdgod.mc.ucr_stitch.config;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

import com.cmdgod.mc.ucr_stitch.tools.ElytraUpgradeUtil;

import io.wispforest.owo.config.Option.SyncMode;
import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.Nest;
import io.wispforest.owo.config.annotation.RangeConstraint;
import io.wispforest.owo.config.annotation.Sync;
import net.minecraft.util.Identifier;

@Modmenu(modId = "ucr_stitch")
@Config(name = "ucr_stitch", wrapperName = "UCRStitchConfig")
@Sync(SyncMode.NONE)
public class ConfigModel {
    @RangeConstraint(min = 0, max = 32)
    public int sugarcaneHeight = 6;
    @RangeConstraint(min = 0, max = 32)
    public int cactusHeight = 6;

    @RangeConstraint(min = 1, max = 54)
    public int bookkeeperBundleSlots = 18;

    public int necklaceDamage = 20;
    public int assistTicks = 200;
    public int pvpToggleBan = 2400;
    public int pvpOffTime = 1200;

    public String pvpOnSign = "☠";
    public String pvpOffSign = "✌";

    public ElytraUpgradeUtil.DescriptionShowType descriptionShowType = ElytraUpgradeUtil.DescriptionShowType.SHIFT_SHOW;
}
