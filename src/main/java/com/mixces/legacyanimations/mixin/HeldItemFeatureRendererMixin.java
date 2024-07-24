package com.mixces.legacyanimations.mixin;

import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import com.mixces.legacyanimations.hook.TransformHook;
import com.mixces.legacyanimations.util.ItemUtils;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemFeatureRenderer.class)
public class HeldItemFeatureRendererMixin {

    @Inject(
            method = "renderItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"
            )
    )
    private void legacyAnimations$swordBlockTransform(LivingEntity entity, ItemStack stack, ModelTransformationMode transformationMode, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci)
    {
        if (!LegacyAnimationsSettings.getInstance().oldSwordBlock)
        {
            return;
        }

        if (!entity.isBlocking())
        {
            return;
        }

        if (!ItemUtils.INSTANCE.isShieldInOffHand(entity.getOffHandStack()) || !ItemUtils.INSTANCE.isSwordInMainHand(entity.getMainHandStack()))
        {
            return;
        }

//        matrices.translate(TransformHook.translationX, TransformHook.translationY, TransformHook.translationZ);
//        matrices.multiply(RotationAxis.POSITIVE_X.rotation(TransformHook.rotationX));
//        matrices.multiply(RotationAxis.POSITIVE_Y.rotation(TransformHook.rotationY));
//        matrices.multiply(RotationAxis.POSITIVE_Z.rotation(TransformHook.rotationZ));

        matrices.translate(-0.2F, 0.0F, 0.1F);
        matrices.multiply(RotationAxis.POSITIVE_X.rotation(21.0F));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotation(90.0F));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotation(-90.0F));

        // x: -0.2 y: 0.0 z: 0.1 yaw: 21.0 pitch: 90.0 roll: -90.0
    }

}
