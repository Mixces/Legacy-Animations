package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import com.mixces.legacyanimations.mixin.interfaces.CameraInterface;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity>
{

    @ModifyExpressionValue(
            method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;isAlive()Z"
            )
    )
    private boolean legacyAnimations$oldDeathLimbs(boolean original)
    {
        if (!LegacyAnimationsSettings.CONFIG.instance().oldDeath)
        {
            return true;
        }
        return original;
    }

    //todo: re-write this mess
    @Inject(
            method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V",
                    ordinal = 1
            )
    )
    private void legacyAnimations$modelSneak(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci)
    {
        if (!LegacyAnimationsSettings.CONFIG.instance().oldSneaking) {
            return;
        }

        final PlayerEntity clientPlayer = MinecraftClient.getInstance().player;
        final boolean entityPlayer = livingEntity instanceof PlayerEntity;

        if (!entityPlayer || clientPlayer == null) {
            return;
        }

        final Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        final boolean player = livingEntity.getName().equals(clientPlayer.getName());

        final float lerpCamera = MathHelper.lerp(g, ((CameraInterface) camera).getLastCameraY(), ((CameraInterface) camera).getCameraY());
        final float eyeHeight = player ? lerpCamera : livingEntity.getStandingEyeHeight();

        matrixStack.translate(0.0F, 1.62F - eyeHeight, 0.0F);
    }

}
