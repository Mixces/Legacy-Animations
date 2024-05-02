package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin  {

//    @Inject(method = "getArmPose", at = @At(value = "HEAD"), cancellable = true)
//    private static void getArmPose_blockArm(AbstractClientPlayerEntity player, Hand hand, CallbackInfoReturnable<BipedEntityModel.ArmPose> cir) {
//        if (LegacyAnimationsSettings.CONFIG.instance().hideShields && player.getStackInHand(hand).getItem() instanceof ShieldItem) {
//            cir.setReturnValue(BipedEntityModel.ArmPose.EMPTY);
//        }
//    }

//    @ModifyExpressionValue(
//            method = "getArmPose",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"
//            )
//    )
//    private static boolean removeBlockArm(boolean original, @Local(ordinal = 0) ItemStack itemStack) {
//        if (LegacyAnimationsSettings.CONFIG.instance().hideShields) {
//            return original || itemStack.isOf(Items.SHIELD);
//        }
//        return original;
//    }

//    @Redirect(method = "getPositionOffset(Lnet/minecraft/client/network/AbstractClientPlayerEntity;F)Lnet/minecraft/util/math/Vec3d;",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;isInSneakingPose()Z"))
//    private boolean disableSneakPositionOffset(AbstractClientPlayerEntity player) {
//        return false && player.isInSneakingPose();
//    }

}
