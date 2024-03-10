package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import com.mixces.legacyanimations.util.ItemUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.UseAction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @WrapOperation(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"))
    private boolean cancelOffhandHotbar(ItemStack itemStack, Operation<Boolean> original) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            ItemStack stack = player.getMainHandStack();
            UseAction action = stack.getUseAction();
            if (LegacyAnimationsSettings.CONFIG.instance().hideShieldHotbar && ItemUtils.isValidItem(stack, action)) {
                return original.call(itemStack) || player.getOffHandStack().isOf(Items.SHIELD);
            }
        }
        return original.call(itemStack);
    }

    @WrapWithCondition(
            method = "renderHealthBar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawHeart(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/gui/hud/InGameHud$HeartType;IIZZZ)V",
                    ordinal = 2
            )
    )
    private boolean legacyAnimations$cancelFlash(InGameHud instance, DrawContext context, InGameHud.HeartType type, int x, int y, boolean hardcore, boolean blinking, boolean half) {
        return !LegacyAnimationsSettings.CONFIG.instance().oldHearts;
    }

    @ModifyExpressionValue(
            method = "renderCrosshair",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/option/Perspective;isFirstPerson()Z"
            )
    )
    private boolean removePerspectiveCheck(boolean original) {
        return LegacyAnimationsSettings.CONFIG.instance().perspectiveCrosshair || original;
    }

}
