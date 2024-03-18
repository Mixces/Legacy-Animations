package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import com.mixces.legacyanimations.util.ServerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

    @Shadow protected abstract void applySwingOffset(MatrixStack matrices, Arm arm, float swingProgress);
    @Shadow private float equipProgressOffHand;
    @Shadow private ItemStack mainHand;
    @Shadow private ItemStack offHand;

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
    private void addSwingOffset(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci, @Local Arm arm) {
        if (LegacyAnimationsSettings.CONFIG.instance().punchDuringUsage) {
            applySwingOffset(matrices, arm, swingProgress);
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
    private void addSwordBlock(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        boolean isUsing = ServerUtils.INSTANCE.isOnHypixel() ? MinecraftClient.getInstance().options.useKey.isPressed() : player.isUsingItem();
        if (LegacyAnimationsSettings.CONFIG.instance().oldSwordBlock && item.getItem() instanceof SwordItem && player.getOffHandStack().getItem() instanceof ShieldItem && isUsing) {
            boolean bl = hand == Hand.MAIN_HAND;
            Arm arm = bl ? player.getMainArm() : player.getMainArm().getOpposite();
            boolean bl2 = arm == Arm.RIGHT;
            int l = bl2 ? 1 : -1;
            matrices.translate(l * -0.14142136F, 0.08F, 0.14142136F);
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-102.25F));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(l * 13.365F));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(l * 78.05F));
        }
    }

    @WrapWithCondition(
            method = "renderFirstPersonItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V",
                    ordinal = 12
            )
    )
    private boolean disableSwingTranslation(MatrixStack instance, float x, float y, float z, AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        boolean isUsing = ServerUtils.INSTANCE.isOnHypixel() ? MinecraftClient.getInstance().options.useKey.isPressed() : player.isUsingItem();
        return !LegacyAnimationsSettings.CONFIG.instance().oldSwordBlock || !(item.getItem() instanceof SwordItem) || !(player.getOffHandStack().getItem() instanceof ShieldItem) || !isUsing;
    }

    @WrapOperation(
            method = "updateHeldItems",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;getAttackCooldownProgress(F)F"
            )
    )
    public float removeCoolDownSpeed(ClientPlayerEntity instance, float v, Operation<Float> original) {
        if (LegacyAnimationsSettings.CONFIG.instance().noCooldown)
            return 1.0F;
        return original.call(instance, v);
    }

    @Inject(method = "resetEquipProgress", at = @At("HEAD"), cancellable = true)
    private void removeStartDelay(Hand hand, CallbackInfo ci) {
        if (LegacyAnimationsSettings.CONFIG.instance().noCooldown) {
            ci.cancel();
        }
    }

    @ModifyArg(
            method = "updateHeldItems",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/MathHelper;clamp(FFF)F",
                    ordinal = 3
            ),
            index = 0
    )
    private float conditionallyUpdateShield(float value, @Local(ordinal = 0) ItemStack itemStack) {
        if (LegacyAnimationsSettings.CONFIG.instance().hideShields && offHand.getItem() instanceof ShieldItem) {
            return (mainHand == itemStack ? 1.0F : 0.0F) - equipProgressOffHand;
        }
        return value;
    }

}
