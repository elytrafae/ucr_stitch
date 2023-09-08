package com.cmdgod.mc.ucr_stitch.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.ElytraEntityModel;

@Mixin(ElytraEntityModel.class)
public interface ElytraEntityModelAccessor {
    
    @Accessor
    public ModelPart getRightWing();

    @Accessor
    public ModelPart getLeftWing();

}
