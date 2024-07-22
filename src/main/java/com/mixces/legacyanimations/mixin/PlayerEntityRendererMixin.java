package com.mixces.legacyanimations.mixin;

import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import com.mixces.legacyanimations.util.ItemUtils;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin
{

    @Inject(
            method = "getArmPose",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private static void legacyAnimations$removeShieldArm(AbstractClientPlayerEntity player, Hand hand, CallbackInfoReturnable<BipedEntityModel.ArmPose> cir) {
        if (!LegacyAnimationsSettings.CONFIG.instance().hideShields)
        {
            return;
        }

        final Hand offhand = Hand.OFF_HAND;

        if (!ItemUtils.INSTANCE.isShieldInOffHand(player.getStackInHand(offhand)))
        {
            return;
        }

        if (!ItemUtils.INSTANCE.isSwordInMainHand(player.getStackInHand(Hand.MAIN_HAND)))
        {
            return;
        }

        if (hand == offhand)
        {
            cir.setReturnValue(BipedEntityModel.ArmPose.EMPTY);
        }
    }

    @Redirect(
            method = "getPositionOffset(Lnet/minecraft/client/network/AbstractClientPlayerEntity;F)Lnet/minecraft/util/math/Vec3d;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;isInSneakingPose()Z"
            )
    )
    private boolean legacyAnimations$disableSneakOffset(AbstractClientPlayerEntity player) {
        if (!LegacyAnimationsSettings.CONFIG.instance().oldSneaking)
        {
            return player.isInSneakingPose();
        }
        return false;
    }

}
