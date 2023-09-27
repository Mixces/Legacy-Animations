package com.mixces.oldanimations.mixin;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.mixces.oldanimations.config.OldAnimationsSettings;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(DebugHud.class)
public class DebugHudMixin {

    @WrapWithCondition(method = "drawText", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V"))
    private boolean removeDebugBackground(DrawContext instance, int x1, int y1, int x2, int y2, int color) {
        return !OldAnimationsSettings.CONFIG.instance().oldDebug;
    }

    @ModifyArg(method = "drawText", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;IIIZ)I"), index = 5)
    private boolean addDebugShadow(boolean shadow) {
        return OldAnimationsSettings.CONFIG.instance().oldDebug;
    }

}
