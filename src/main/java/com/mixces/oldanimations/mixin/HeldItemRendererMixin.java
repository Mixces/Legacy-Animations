package com.mixces.oldanimations.mixin;

import com.mixces.oldanimations.config.OldAnimationsSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Final;
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
    @Shadow protected abstract void applyEquipOffset(MatrixStack matrices, Arm arm, float equipProgress);
    @Shadow public abstract void renderItem(LivingEntity entity, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light);
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;isUsingItem()Z", ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD)
    private void renderItem_blockHit(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci, boolean bl) {
        Arm arm = bl ? player.getMainArm() : player.getMainArm().getOpposite();
        boolean bl2 = arm == Arm.RIGHT;
        ModelTransformationMode renderMode = bl2 ? ModelTransformationMode.FIRST_PERSON_RIGHT_HAND : ModelTransformationMode.FIRST_PERSON_LEFT_HAND;
        int side = bl && bl2 ? 1 : -1;
        //
        // player.isUsingItem() && player.getActiveItem().getUseAction() == UseAction.BLOCK
        if (OldAnimationsSettings.INSTANCE.getConfig().swordHitAnimation && item.getItem() instanceof SwordItem && client.options.useKey.isPressed()) {
            applyEquipOffset(matrices, arm, equipProgress);
            applySwingOffset2(player, tickDelta, hand, matrices);
            doBlockTransformation(side, matrices);
            renderItem(player, item, renderMode, !bl2, matrices, vertexConsumers, light);
        }
    }

    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V", shift = At.Shift.AFTER,ordinal = 0))
    private void renderItem_crossBowHit(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (OldAnimationsSettings.INSTANCE.getConfig().crossBowHitAnimation) {
            applySwingOffset2(player, tickDelta, hand, matrices);
        }
    }

    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V", shift = At.Shift.AFTER,ordinal = 3))
    private void renderItem_eatDrinkHit(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (OldAnimationsSettings.INSTANCE.getConfig().eatDrinkHitAnimation) {
            applySwingOffset2(player, tickDelta, hand, matrices);
        }
    }

    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V", shift = At.Shift.AFTER, ordinal = 5))
    private void renderItem_bowHit(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (OldAnimationsSettings.INSTANCE.getConfig().bowHitAnimation) {
            applySwingOffset2(player, tickDelta, hand, matrices);
        }
    }

    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V", shift = At.Shift.AFTER, ordinal = 6))
    private void renderItem_tridentHit(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (OldAnimationsSettings.INSTANCE.getConfig().tridentHitAnimation) {
            applySwingOffset2(player, tickDelta, hand, matrices);
        }
    }

    @Redirect(method = "updateHeldItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getAttackCooldownProgress(F)F"))
    public float simplified$renderItemInFirstPerson(ClientPlayerEntity instance, float v) {
        return 1.0F;
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
