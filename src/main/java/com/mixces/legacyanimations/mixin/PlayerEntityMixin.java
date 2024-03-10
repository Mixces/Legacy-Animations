package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import com.mixces.legacyanimations.util.ItemUtils;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.entity.player.PlayerEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin {

//    @Inject(method = "resetLastAttackedTicks", at = @At("HEAD"), cancellable = true)
//    private void resetLastAttackedTicks_removeDelay(CallbackInfo ci) {
//        ci.cancel();
//    }

    @ModifyReturnValue(
            method = "getActiveEyeHeight",
            at = @At(
                    value = "RETURN",
                    ordinal = 1
            )
    )
    private float modifyEyeHeight(float constant) {
        if (LegacyAnimationsSettings.CONFIG.instance().oldSneaking) {
            if (ItemUtils.getBlock(0, 1, 0) instanceof SlabBlock || ItemUtils.getBlock(0, 1, 0) instanceof StairsBlock) {
                return constant;
            } else {
                return constant + 0.27F;
            }
        }
        return constant;
    }

    @Inject(
            method = "tickMovement",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;strideDistance:F",
                    opcode = Opcodes.PUTFIELD,
                    shift = At.Shift.AFTER
            )
    )
    private void getPitchFromVelocity(CallbackInfo ci) {
        if (LegacyAnimationsSettings.CONFIG.instance().oldViewBob) {
            PlayerEntity entity = (PlayerEntity) (Object) this;
            double velocityY = ((InvokerEntity) entity).invokeGetVelocity().y;
            float f1 = (float) (Math.atan(-velocityY * 0.2F) * 15.0F);
            if (((InvokerEntity) entity).invokeIsOnGround() || ((LivingEntityInvoker) entity).invokeGetHealth() <= 0.0F) {
                f1 = 0.0F;
            }
            legacyAnimations$cameraPitch += (f1 - legacyAnimations$cameraPitch) * 0.8F;
        }
    }

//    @Redirect(
//            method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/entity/player/PlayerEntity;swingHand(Lnet/minecraft/util/Hand;)V"
//            )
//    )
//    private void removeSwingAnimation2(PlayerEntity instance, Hand hand) {
//        if (!LegacyAnimationsSettings.CONFIG.instance().noDropSwing) {
//            instance.swingHand(hand);
//        }
//    }

}
