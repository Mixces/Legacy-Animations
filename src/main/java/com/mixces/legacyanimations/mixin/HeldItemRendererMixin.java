package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import com.mixces.legacyanimations.util.HandUtils;
import com.mixces.legacyanimations.util.ItemUtils;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin
{

    //todo: re-write the whole thing :)
    @Shadow protected abstract void applySwingOffset(MatrixStack matrices, Arm arm, float swingProgress);
    @Shadow private float equipProgressOffHand;
    @Shadow private ItemStack mainHand;
    @Shadow private ItemStack offHand;
    @Shadow @Final private EntityRenderDispatcher entityRenderDispatcher;

    @Inject(
            method = "renderFirstPersonItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V",
                    ordinal = 1
            )
    )
    private void legacyAnimations$preBowTransform(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (!LegacyAnimationsSettings.CONFIG.instance().itemPositions)
        {
            return;
        }

        final int l = HandUtils.INSTANCE.handMultiplier((ClientPlayerEntity) player, entityRenderDispatcher);

        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(l * -335));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(l * -50.0F));
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
        if (!LegacyAnimationsSettings.CONFIG.instance().itemPositions)
        {
            return;
        }

        final int l = HandUtils.INSTANCE.handMultiplier((ClientPlayerEntity) player, entityRenderDispatcher);

        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(l * 50.0F));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(l * 335));
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
                            target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEatOrDrinkTransformation(Lnet/minecraft/client/util/math/MatrixStack;FLnet/minecraft/util/Arm;Lnet/minecraft/item/ItemStack;)V"
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyBrushTransformation(Lnet/minecraft/client/util/math/MatrixStack;FLnet/minecraft/util/Arm;Lnet/minecraft/item/ItemStack;F)V"
                    )
            )
    )
    private void legacyAnimations$addSwingOffset(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci, @Local Arm arm)
    {
        if (!LegacyAnimationsSettings.CONFIG.instance().punchDuringUsage)
        {
            return;
        }

        applySwingOffset(matrices, arm, swingProgress);
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
    private void legacyAnimations$addBlockTranslation(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci)
    {
        if (!LegacyAnimationsSettings.CONFIG.instance().oldSwordBlock)
        {
            return;
        }

        if (!ItemUtils.INSTANCE.isSwordInMainHand(mainHand) || !ItemUtils.INSTANCE.isShieldInOffHand(offHand))
        {
            return;
        }

        final int l = HandUtils.INSTANCE.handMultiplier((ClientPlayerEntity) player, entityRenderDispatcher);

        matrices.translate(l * -0.14142136F, 0.08F, 0.14142136F);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-102.25F));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(l * 13.365F));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(l * 78.05F));
    }

    @Inject(
            method = "renderFirstPersonItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
                    ordinal = 1
            )
    )
    private void legacyAnimations$oldItemPositions(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci)
    {
        if (!LegacyAnimationsSettings.CONFIG.instance().itemPositions)
        {
            return;
        }

        final int l = HandUtils.INSTANCE.handMultiplier((ClientPlayerEntity) player, entityRenderDispatcher);

        if (ItemUtils.INSTANCE.shouldRotateAroundWhenRendering(item, true))
        {
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(l * 180.0F));
        }

        final float scale = 0.7585F / 0.86F;

        matrices.scale(scale, scale, scale);
        matrices.translate(l * -0.084F, 0.059F, 0.08F);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(l * 5.0F));
    }

    @ModifyExpressionValue(
            method = "updateHeldItems",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;getAttackCooldownProgress(F)F"
            )
    )
    public float legacyAnimations$removeCoolDownSpeed(float original)
    {
        if (!LegacyAnimationsSettings.CONFIG.instance().noCooldown) {
            return original;
        }
        return 1.0f;
    }

    @Inject(
            method = "resetEquipProgress",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void legacyAnimations$removeStartDelay(Hand hand, CallbackInfo ci)
    {
        if (!LegacyAnimationsSettings.CONFIG.instance().noCooldown) {
            return;
        }

        ci.cancel();
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
//        if (LegacyAnimationsSettings.CONFIG.instance().hideShields && ItemUtils.INSTANCE.isShieldInOffHand(offHand))
//        {
//            return (mainHand == itemStack ? 1.0F : 0.0F) - equipProgressOffHand;
//        }
//        return value;
//    }

}
