package com.mixces.legacyanimations.mixin;

import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import com.mixces.legacyanimations.util.ItemUtils;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemFeatureRenderer.class)
public class HeldItemFeatureRendererMixin {

//    @Inject(
//            method = "renderItem",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"
//            )
//    )
//    private void legacyAnimations$swordBlockTransform(LivingEntity entity, ItemStack stack, ModelTransformationMode transformationMode, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci)
//    {
//        if (!LegacyAnimationsSettings.CONFIG.instance().oldSwordBlock)
//        {
//            return;
//        }
//
//        if (!ItemUtils.INSTANCE.isSwordInMainHand(stack) || !ItemUtils.INSTANCE.isShieldInOffHand(stack))
//        {
//            return;
//        }
//
//        matrices.translate(0.05f, 0.0f, -0.1f);
//        matrices.multiply(RotationAxis.POSITIVE_Y.rotation(-50.0F));
//        matrices.multiply(RotationAxis.POSITIVE_X.rotation(-10.0F));
//        matrices.multiply(RotationAxis.POSITIVE_Z.rotation(-60.0F));
//    }

}
