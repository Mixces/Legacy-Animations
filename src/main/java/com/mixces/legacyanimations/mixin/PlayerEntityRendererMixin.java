package com.mixces.legacyanimations.mixin;

import net.minecraft.client.render.entity.PlayerEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin  {

//    @Inject(method = "getArmPose", at = @At(value = "HEAD"), cancellable = true)
//    private static void getArmPose_blockArm(AbstractClientPlayerEntity player, Hand hand, CallbackInfoReturnable<BipedEntityModel.ArmPose> cir) {
//        if (LegacyAnimationsSettings.CONFIG.instance().hideShields && player.getStackInHand(hand).getItem() instanceof ShieldItem) {
//            cir.setReturnValue(BipedEntityModel.ArmPose.EMPTY);
//        }
//    }

}
