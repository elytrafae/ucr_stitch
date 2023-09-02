package com.cmdgod.mc.ucr_stitch.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.FishingBobberEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;

@Mixin(FishingBobberEntityRenderer.class)
public interface FishingBobberEntityRendererAccessor {
    
    @Accessor
    Identifier getTexture();

    @Accessor
    RenderLayer getLayer();

    @Invoker("vertex")
    public static void vertex(VertexConsumer buffer, Matrix4f matrix, Matrix3f normalMatrix, int light, float x, int y, int u, int v) {
    }

    @Invoker("renderFishingLine")
    public static void renderFishingLine(float x, float y, float z, VertexConsumer buffer, MatrixStack.Entry matrices, float segmentStart, float segmentEnd) {

    }

    @Invoker("percentage")
    public static float percentage(int value, int max) {
        return 0;
    }
}
