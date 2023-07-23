package com.mixces.oldanimations.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin  {

    @Inject(method = "getArmPose", at = @At(value = "HEAD"), cancellable = true)
    private static void getArmPose_blockArm(AbstractClientPlayerEntity player, Hand hand, CallbackInfoReturnable<BipedEntityModel.ArmPose> cir) {
        // player.isUsingItem() && player.getActiveItem().getUseAction() == UseAction.BLOCK
        if (player.getStackInHand(hand).getItem() instanceof SwordItem && MinecraftClient.getInstance().options.useKey.isPressed()) {
            cir.setReturnValue(BipedEntityModel.ArmPose.BLOCK);
        } else if (player.getStackInHand(hand).getItem() instanceof ShieldItem) {
            cir.setReturnValue(BipedEntityModel.ArmPose.EMPTY);
        }
    }

}
