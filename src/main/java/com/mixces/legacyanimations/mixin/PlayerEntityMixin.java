package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import com.mixces.legacyanimations.util.ServerUtils;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin {

    @Inject(method = "resetLastAttackedTicks", at = @At("HEAD"), cancellable = true)
    private void resetLastAttackedTicks_removeDelay(CallbackInfo ci) {
        if (ServerUtils.INSTANCE.isOnHypixel()) {
            ci.cancel();
        }
    }

    @Shadow protected abstract boolean canChangeIntoPose(EntityPose pose);

    @ModifyReturnValue(
            method = "getActiveEyeHeight",
            at = @At(
                    value = "RETURN",
                    ordinal = 1
            )
    )
    private float modifyEyeHeight(float constant) {
        if (LegacyAnimationsSettings.CONFIG.instance().oldSneaking) {
            if (!canChangeIntoPose(EntityPose.STANDING)) {
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

    @Inject(
            method = "getEquippedStack",
            at = @At(
                    value = "RETURN",
                    ordinal = 1
            ),
            cancellable = true)
    private void hypixelShieldBug(EquipmentSlot slot, CallbackInfoReturnable<ItemStack> cir) {
        if (ServerUtils.INSTANCE.isOnHypixel() && getMainHandStack().getItem() instanceof SwordItem && !isUsingItem()) {
            cir.setReturnValue(new ItemStack(Items.SHIELD));
        }
    }

}
