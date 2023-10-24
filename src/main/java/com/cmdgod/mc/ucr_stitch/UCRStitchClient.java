package com.cmdgod.mc.ucr_stitch;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.lwjgl.glfw.GLFW;

import com.cmdgod.mc.ucr_stitch.items.CustomFishingRodItem;
import com.cmdgod.mc.ucr_stitch.networking.PVPTogglePacket;
import com.cmdgod.mc.ucr_stitch.registrers.ModBlocks;
import com.cmdgod.mc.ucr_stitch.registrers.ModItems;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FishingRodItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;
import net.fabricmc.api.EnvType;

@Environment(EnvType.CLIENT)
public class UCRStitchClient implements ClientModInitializer {

    private static KeyBinding keyBinding;

    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.VOIDBERRY_VINE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.COPPER_LANTERN, RenderLayer.getCutout());
        // Replace `RenderLayer.getCutout()` with `RenderLayer.getTranslucent()` if you have a translucent texture.

        ModelPredicateProviderRegistry.register(ModItems.VOIDBERRY_VINE, new Identifier("fruit"), (itemStack, clientWorld, livingEntity, i) -> {
            NbtCompound nbt = itemStack.getOrCreateNbt();
            if (!nbt.contains(BlockItem.BLOCK_STATE_TAG_KEY, NbtElement.COMPOUND_TYPE)) {
                return 0F;
            }
            NbtCompound blockStateNbt = nbt.getCompound(BlockItem.BLOCK_STATE_TAG_KEY);
            if (!blockStateNbt.contains("fruit", NbtElement.STRING_TYPE)) {
                return 0F;
            }
            return Math.max(Integer.parseInt(blockStateNbt.getString("fruit")), 0);
        });

        for (CustomFishingRodItem item : ModItems.FISHING_RODS.values()) {
            ModelPredicateProviderRegistry.register(item, new Identifier("cast"), (stack, world, entity, seed) -> {
                boolean bl2;
                if (entity == null) {
                    return 0.0f;
                }
                boolean bl = entity.getMainHandStack() == stack;
                boolean bl3 = bl2 = entity.getOffHandStack() == stack;
                if (entity.getMainHandStack().getItem() instanceof FishingRodItem) {
                    bl2 = false;
                }
                return (bl || bl2) && entity instanceof PlayerEntity && ((PlayerEntity)entity).fishHook != null ? 1.0f : 0.0f;
            });
        }

        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.ucr_stitch.toggle_pvp", // The translation key of the keybinding's name
            InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
            GLFW.GLFW_KEY_P, // The keycode of the key
            "category.ucr_stitch.main" // The translation key of the keybinding's category.
        ));

        MutableBoolean wasPressed = new MutableBoolean(false);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (keyBinding.isPressed()) {
                if (wasPressed.isFalse()) {
                    //client.player.sendMessage(Text.literal("Key 1 was pressed!"), false);
                    UCRStitch.PVP_TOGGLE_CHANNEL.clientHandle().send(new PVPTogglePacket());
                }
                wasPressed.setTrue();
            } else {
                wasPressed.setFalse();
            }
        });


    }
}