package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import com.mixces.legacyanimations.util.ItemUtils;
import com.mixces.legacyanimations.util.MatrixUtil;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(HeldItemFeatureRenderer.class)
public class HeldItemFeatureRendererMixin {

    @ModifyArgs(
            method = "renderItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V"
            )
    )
    private void legacyAnimations$revertTranslation(Args args) {
        args.setAll((float) args.get(0) * -1.0F, 0.4375F, (float) args.get(2) / 10 * -1.0F);
    }

    @WrapWithCondition(
            method = "renderItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionf;)V",
                    ordinal = 0
            )
    )
    private boolean legacyAnimations$removeExtraMultiply(MatrixStack instance, Quaternionf quaternion) {
        return !LegacyAnimationsSettings.getInstance().itemPositions;
    }

    @WrapWithCondition(
            method = "renderItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionf;)V",
                    ordinal = 1
            )
    )
    private boolean legacyAnimations$removeExtraMultiply2(MatrixStack instance, Quaternionf quaternion) {
        return !LegacyAnimationsSettings.getInstance().itemPositions;
    }

    @Inject(
            method = "renderItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"
            )
    )
    private void legacyAnimations$swordBlockTransform(LivingEntity entity, ItemStack stack, ModelTransformationMode transformationMode, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
//        if (LegacyAnimationsSettings.getInstance().oldSwordBlock && entity.isBlocking() &&
//                ItemUtils.INSTANCE.isShieldInOffHand(entity.getOffHandStack()) &&
//                ItemUtils.INSTANCE.isSwordInMainHand(entity.getMainHandStack())) {
//
//            matrices.translate(TransformHook.translationX, TransformHook.translationY, TransformHook.translationZ);
//            matrix.yaw(TransformHook.rotationX).pitch(TransformHook.rotationY).roll(TransformHook.rotationZ);
//        }
        final MatrixUtil matrix = new MatrixUtil(matrices);
//        matrix.yaw(TransformHook.rotationX).pitch(TransformHook.rotationY).roll(TransformHook.rotationZ);
//        matrices.translate(TransformHook.translationX, TransformHook.translationY, TransformHook.translationZ);

        Item item = stack.getItem();
        float var7;
        if (item instanceof BlockItem) {
            var7 = 0.375F;
            matrices.translate(0.0F, 0.1875F, -0.3125F);
            matrix.pitch(20.0F).yaw(45.0F);
            matrices.scale(-var7, -var7, var7);
        } else if (item == Items.BOW) {
            var7 = 0.625F;
            matrices.translate(0.0F, 0.125F, 0.3125F);
            matrix.yaw(-20.0F);
            matrices.scale(var7, -var7, var7);
            matrix.pitch(-100.0F).yaw(45.0F);
        } else if (ItemUtils.INSTANCE.isFull3d(stack)) {
            var7 = 0.625F;
            if (ItemUtils.INSTANCE.shouldRotateAroundWhenRendering(stack, true)) {
                matrix.roll(180.0F);
                matrices.translate(0.0F, -0.125F, 0.0F);
            }
            if (entity instanceof PlayerEntity && entity.getItemUseTime() > 0 && entity.isBlocking()) {
                matrices.translate(0.05F, 0.0F, -0.1F);
                matrix.yaw(-50.0F).pitch(-10.0F).roll(-60.0F);
            }
            matrices.translate(0.0F, 0.1875F, 0.0F);
            matrices.scale(var7, var7, var7);
            matrix.pitch(-100.0F).yaw(45.0F);
        } else {
            var7 = 0.375F;
            matrices.translate(0.25F, 0.1875F, -0.1875F);
            matrices.scale(var7, var7, var7);
            matrix.roll(60.0F).pitch(-90.0F).roll(20.0F);
        }
        // x: -0.2 y: 0.0 z: 0.1 yaw: 21.0 pitch: 90.0 roll: -90.0
    }

    @ModifyArg(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/feature/HeldItemFeatureRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;Lnet/minecraft/util/Arm;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"
            ),
            index = 2
    )
    private ModelTransformationMode legacyAnimations$changeTransformType(ModelTransformationMode renderMode) {
        return LegacyAnimationsSettings.getInstance().itemPositions ? ModelTransformationMode.NONE : renderMode;
    }
}
