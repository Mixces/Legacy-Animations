package com.mixces.legacyanimations.mixin;

import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import net.minecraft.client.render.entity.feature.CapeFeatureRenderer;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = CapeFeatureRenderer.class)
public class CapeFeatureRendererMixin {

    @Redirect(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/network/AbstractClientPlayerEntity;FFFFFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/MathHelper;lerpAngleDegrees(FFF)F"
            )
    )
    private float legacyAnimations$useLerp(float delta, float start, float end) {
        return LegacyAnimationsSettings.getInstance().oldCape ? MathHelper.lerp(delta, start, end) : MathHelper.lerpAngleDegrees(delta, start, end);
    }

    @Redirect(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/network/AbstractClientPlayerEntity;FFFFFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/MathHelper;clamp(FFF)F",
                    ordinal = 2
            )
    )
    private float legacyAnimations$removeClamp(float value, float min, float max) {
        return LegacyAnimationsSettings.getInstance().oldCape ? value : MathHelper.clamp(value, min, max);
    }
}
