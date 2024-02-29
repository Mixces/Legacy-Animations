package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

    @Shadow protected abstract void applySwingOffset(MatrixStack matrices, Arm arm, float swingProgress);

    @Inject(
            method = "renderFirstPersonItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V",
                    shift = At.Shift.AFTER
            )
            ,
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
    private void addSwingOffset(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci,
                                               @Local Arm arm) {
            if (LegacyAnimationsSettings.CONFIG.instance().punchDuringUsage) {
                applySwingOffset(matrices, arm, swingProgress);
            }
//            if (item.getUseAction() == UseAction.BLOCK) {
//                matrices.translate(1 * -0.14142136F, 0.08F, 0.14142136F);
//                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-102.25F));
//                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(1 * 13.365F));
//                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(1 * 78.05F));
//            }
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

//    @Inject(method = "resetEquipProgress", at = @At("HEAD"), cancellable = true)
//    private void resetEquipProgress_removeDelay(Hand hand, CallbackInfo ci) {
//        if (LegacyAnimationsSettings.CONFIG.instance().noCooldown) {
//            ci.cancel();
//        }
//    }

}
