package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {

    @ModifyExpressionValue(
            method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;isAlive()Z"
            )
    )
    private boolean oldDeathLimbs(boolean original) {
        return LegacyAnimationsSettings.CONFIG.instance().oldDeath || original;
    }

    @Inject(
            method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V",
                    ordinal = 1
            )
    )
    private void updateThirdPersonSneak(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (LegacyAnimationsSettings.CONFIG.instance().oldSneaking) {
            PlayerEntity clientPlayer = MinecraftClient.getInstance().player;
            if (livingEntity instanceof PlayerEntity && clientPlayer != null) {
                Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
                boolean player = livingEntity.getName().equals(clientPlayer.getName());
                float eyeHeight = player ? MathHelper.lerp(g, ((AccessorCamera) camera).getLastCameraY(), ((AccessorCamera) camera).getCameraY()) : livingEntity.getStandingEyeHeight();
                matrixStack.translate(0.0F, 1.62F - eyeHeight, 0.0F);
            }
        }
    }

}
