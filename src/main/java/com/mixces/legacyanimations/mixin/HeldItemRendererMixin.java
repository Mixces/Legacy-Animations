package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import com.mixces.legacyanimations.hook.TransformHook;
import com.mixces.legacyanimations.util.HandUtils;
import com.mixces.legacyanimations.util.ItemUtils;
import com.mixces.legacyanimations.util.MatrixUtil;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

    //todo: re-write the whole thing :)
    @Shadow
    protected abstract void applySwingOffset(MatrixStack matrices, Arm arm, float swingProgress);

    @Shadow
    private float equipProgressOffHand;

    @Shadow
    private ItemStack mainHand;

    @Shadow
    private ItemStack offHand;

    @Shadow
    @Final
    private EntityRenderDispatcher entityRenderDispatcher;

    @Inject(
            method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/ItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;III)V"
            )
    )
    private void legacyAnimations$oldItemTransformations(LivingEntity entity, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (LegacyAnimationsSettings.getInstance().oldSwordBlock) {
            final MatrixUtil matrix = new MatrixUtil(matrices);
            matrices.translate(0.0F, -0.3F, 0.0F);
            matrices.scale(1.5F, 1.5F, 1.5F);
            matrix.yaw(50.0F).roll(335.0F);
            matrices.translate(-0.9375F, -0.0625F, 0.0F);
            /* idk */
            matrix.yaw(180.0F);
            matrices.translate(-0.5F, 0.5F, 0.03125F);
        }
    }

    @Inject(
            method = "renderFirstPersonItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V",
                    ordinal = 1
            )
    )
    private void legacyAnimations$preBowTransform(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (LegacyAnimationsSettings.getInstance().itemPositions) {
            final int l = HandUtils.INSTANCE.handMultiplier((ClientPlayerEntity) player, entityRenderDispatcher);
            //todo: fix this
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(l * -335));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(l * -50.0F));
        }
    }

    @Inject(
            method = "renderFirstPersonItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V",
                    ordinal = 1,
                    shift = At.Shift.AFTER
            )
    )
    private void legacyAnimations$postBowTransform(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (LegacyAnimationsSettings.getInstance().itemPositions) {
            final int l = HandUtils.INSTANCE.handMultiplier((ClientPlayerEntity) player, entityRenderDispatcher);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(l * 50.0F));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(l * 335));
        }
    }

    @Inject(
            method = "renderFirstPersonItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V",
                    shift = At.Shift.AFTER
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEatOrDrinkTransformation(Lnet/minecraft/client/util/math/MatrixStack;FLnet/minecraft/util/Arm;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/PlayerEntity;)V"
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyBrushTransformation(Lnet/minecraft/client/util/math/MatrixStack;FLnet/minecraft/util/Arm;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/PlayerEntity;F)V"
                    )
            )
    )
    private void legacyAnimations$addSwingOffset(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci, @Local Arm arm) {
        if (LegacyAnimationsSettings.getInstance().punchDuringUsage) {
            applySwingOffset(matrices, arm, swingProgress);
        }
    }

    @Inject(
            method = "applySwingOffset",
            at = @At("TAIL")
    )
    private void legacyAnimations$addOldScale(MatrixStack matrices, Arm arm, float swingProgress, CallbackInfo ci) {
        if (LegacyAnimationsSettings.getInstance().itemPositions) {
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(45.0F));
            matrices.scale(0.4F, 0.4F, 0.4F);
        }
    }

    @Inject(
            method = "renderFirstPersonItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V",
                    ordinal = 4,
                    shift = At.Shift.AFTER
            )
    )
    private void legacyAnimations$addBlockTranslation(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (LegacyAnimationsSettings.getInstance().oldSwordBlock) {
            if (!ItemUtils.INSTANCE.isSwordInMainHand(mainHand) || !ItemUtils.INSTANCE.isShieldInOffHand(offHand)) return;
            final int l = HandUtils.INSTANCE.handMultiplier((ClientPlayerEntity) player, entityRenderDispatcher);
            final MatrixUtil matrix = new MatrixUtil(matrices);
//            matrices.translate(l * -0.14142136F, 0.08F, 0.14142136F);
//            matrix.pitch(-102.25F).yaw(l * 13.365F).roll(l * 78.05F);

            matrices.translate(-0.5F, 0.2F, 0.0F);
            matrix.yaw(30.0F).pitch(-80.0F).yaw(60.0F);
        }
    }

    @Inject(
            method = "renderFirstPersonItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
                    ordinal = 1
            )
    )
    private void legacyAnimations$oldItemPositions(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (LegacyAnimationsSettings.getInstance().itemPositions) {
            final int l = HandUtils.INSTANCE.handMultiplier((ClientPlayerEntity) player, entityRenderDispatcher);
            if (ItemUtils.INSTANCE.shouldRotateAroundWhenRendering(item, true)) {
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(l * 180.0F));
            }
//            final MatrixUtil matrix = new MatrixUtil(matrices);
//            final float scale = 0.7585F / 0.86F;
//            matrices.scale(scale, scale, scale);
//            matrices.translate(l * -0.084F, 0.059F, 0.08F);
//            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(l * 5.0F));

        }
    }

    @ModifyArg(
            method = "renderFirstPersonItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"
            ),
            index = 2
    )
    private ModelTransformationMode legacyAnimations$changeTransformType(ModelTransformationMode renderMode) {
        return LegacyAnimationsSettings.getInstance().itemPositions ? ModelTransformationMode.NONE : renderMode;
    }

    @ModifyExpressionValue(
            method = "updateHeldItems",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;getAttackCooldownProgress(F)F"
            )
    )
    public float legacyAnimations$removeCoolDownSpeed(float original) {
        return LegacyAnimationsSettings.getInstance().noCooldown ? 1.0f : original;
    }

    @Inject(
            method = "resetEquipProgress",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void legacyAnimations$removeStartDelay(Hand hand, CallbackInfo ci) {
        if (LegacyAnimationsSettings.getInstance().noCooldown) {
            ci.cancel();
        }
    }

    //todo: old re-equip logic needed frfr
//    @ModifyArg(
//            method = "updateHeldItems",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/util/math/MathHelper;clamp(FFF)F",
//                    ordinal = 3
//            ),
//            index = 0
//    )
//    private float legacyAnimations$conditionallyUpdateShield(float value, @Local(ordinal = 0) ItemStack itemStack)
//    {
//        if (LegacyAnimationsSettings.getInstance().hideShields && ItemUtils.INSTANCE.isShieldInOffHand(offHand))
//        {
//            return (mainHand == itemStack ? 1.0F : 0.0F) - equipProgressOffHand;
//        }
//        return value;
//    }
}
