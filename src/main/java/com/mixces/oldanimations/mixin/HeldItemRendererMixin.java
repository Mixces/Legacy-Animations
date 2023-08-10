package com.mixces.oldanimations.mixin;

import com.mixces.oldanimations.config.OldAnimationsSettings;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

    @Shadow protected abstract void applySwingOffset(MatrixStack matrices, Arm arm, float swingProgress);

    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void renderItem_bowHit(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci, boolean bl) {
        Arm arm = bl ? player.getMainArm() : player.getMainArm().getOpposite();
        boolean bl2 = arm == Arm.RIGHT;
        int side = bl && bl2 ? 1 : -1;
        if (player.isUsingItem() && player.getItemUseTimeLeft() > 0 && player.getActiveHand() == hand) {
            if (OldAnimationsSettings.INSTANCE.getConfig().punchDuringUsage && item.getUseAction() != UseAction.NONE) {
                applySwingOffset2(player, tickDelta, hand, matrices);
            }
            if (item.getUseAction() == UseAction.BLOCK) {
                doBlockTransformation(side, matrices);
            }
        }
    }

    @Redirect(method = "updateHeldItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getAttackCooldownProgress(F)F"))
    public float simplified$renderItemInFirstPerson(ClientPlayerEntity instance, float v) {
        if (OldAnimationsSettings.INSTANCE.getConfig().noCooldown)
            return 1.0F;
        return instance.getAttackCooldownProgress(v);
    }

    @Inject(method = "resetEquipProgress", at = @At("HEAD"), cancellable = true)
    private void resetEquipProgress_removeDelay(Hand hand, CallbackInfo ci) {
        if (OldAnimationsSettings.INSTANCE.getConfig().noCooldown) {
            ci.cancel();
        }
    }

    @Unique
    public void applySwingOffset2(AbstractClientPlayerEntity player, float tickDelta, Hand hand, MatrixStack matrices) {
        boolean bl = hand == Hand.MAIN_HAND;
        Arm arm = bl ? player.getMainArm() : player.getMainArm().getOpposite();
        applySwingOffset(matrices, arm, player.getHandSwingProgress(tickDelta));
    }

    @Unique
    public void doBlockTransformation(int side, MatrixStack matrices) {
        matrices.translate(side * -0.14142136F, 0.08F, 0.14142136F);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-102.25F));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(side * 13.365F));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(side * 78.05F));
    }

}
