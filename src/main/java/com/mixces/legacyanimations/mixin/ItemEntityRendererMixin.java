package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import com.mixces.legacyanimations.util.HandUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntityRenderer.class)
public abstract class ItemEntityRendererMixin extends EntityRenderer<ItemEntity> {

    protected ItemEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @ModifyArg(
            method = "render(Lnet/minecraft/entity/ItemEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionf;)V"
            )
    )
    private Quaternionf facePlayer(Quaternionf quaternion, @Local(ordinal = 0) boolean bl) {
        return LegacyAnimationsSettings.CONFIG.instance().fastItems && !bl ? dispatcher.getRotation() : quaternion;
    }

    @Inject(
            method = "render(Lnet/minecraft/entity/ItemEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionf;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void rotateItemAccordingly(ItemEntity itemEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci, @Local(ordinal = 0) boolean bl) {
        if (LegacyAnimationsSettings.CONFIG.instance().fastItems && MinecraftClient.getInstance().player != null && !bl) {
            if (dispatcher.gameOptions.getPerspective().isFrontView()) {
                if (HandUtils.isLeftHand(MinecraftClient.getInstance().player, dispatcher)) {
                    matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
                }
            } else {
                if (HandUtils.isRightHand(MinecraftClient.getInstance().player, dispatcher)) {
                    matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
                }
            }
        }
    }

}
