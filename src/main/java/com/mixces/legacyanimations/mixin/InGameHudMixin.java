package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
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

    //todo: shield shit
//    @WrapOperation(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"))
//    private boolean cancelOffhandHotbar(ItemStack itemStack, Operation<Boolean> original)
//    {
//        ClientPlayerEntity player = MinecraftClient.getInstance().player;
//        if (player != null) {
//            ItemStack mainStack = player.getMainHandStack();
//            UseAction action = mainStack.getUseAction();
//            if (LegacyAnimationsSettings.getInstance().hideShieldHotbar && ItemUtils.INSTANCE.isValidItem(mainStack, action))
//            {
//                return original.call(itemStack) || itemStack.isOf(Items.SHIELD);
//            }
//        }
//        return original.call(itemStack);
//    }

    @WrapWithCondition(
            method = "renderHealthBar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawHeart(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/gui/hud/InGameHud$HeartType;IIZZZ)V",
                    ordinal = 2
            )
    )
    private boolean legacyAnimations$cancelFlash(InGameHud instance, DrawContext context, InGameHud.HeartType type, int x, int y, boolean hardcore, boolean blinking, boolean half) {
        return !LegacyAnimationsSettings.getInstance().oldHearts;
    }

    @ModifyExpressionValue(
            method = "renderCrosshair",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/option/Perspective;isFirstPerson()Z"
            )
    )
    private boolean legacyAnimations$removePerspectiveCheck(boolean original) {
        return LegacyAnimationsSettings.getInstance().perspectiveCrosshair || original;
    }
}
