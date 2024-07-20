package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import com.mixces.legacyanimations.mixin.interfaces.IEntityMixin;
import com.mixces.legacyanimations.mixin.interfaces.ILivingEntityMixin;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin
{

    //todo: hypixel
//    @Inject(method = "resetLastAttackedTicks", at = @At("HEAD"), cancellable = true)
//    private void legacyAnimations$removeAttackDelay(CallbackInfo ci)
//    {
//        if (ServerUtils.INSTANCE.isOnHypixel())
//        {
//            ci.cancel();
//        }
//    }

    //todo: hypixel
//    @Inject(method = "getMainArm", at = @At("HEAD"), cancellable = true)
//    public void legacyAnimations$correctHandSide(CallbackInfoReturnable<Arm> cir)
//    {
//        final PlayerEntity entity = (PlayerEntity) (Object) this;
//
//        if (entity instanceof ClientPlayerEntity && ServerUtils.INSTANCE.isOnHypixel())
//        {
//            cir.setReturnValue(MinecraftClient.getInstance().options.getMainArm().getValue());
//        }
//    }

    @Inject(
            method = "tickMovement",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;strideDistance:F",
                    opcode = Opcodes.PUTFIELD,
                    shift = At.Shift.AFTER
            )
    )
    private void legacyAnimations$getPitchFromVelocity(CallbackInfo ci)
    {
        if (!LegacyAnimationsSettings.CONFIG.instance().oldViewBob)
        {
            return;
        }

        final PlayerEntity entity = (PlayerEntity) (Object) this;
        final double velocityY = ((IEntityMixin) entity).invokeGetVelocity().y;
        float f1 = (float) (Math.atan(-velocityY * 0.2F) * 15.0F);

        if (((IEntityMixin) entity).invokeIsOnGround() || ((ILivingEntityMixin) entity).invokeGetHealth() <= 0.0F)
        {
            f1 = 0.0F;
        }
        legacyAnimations$cameraPitch += (f1 - legacyAnimations$cameraPitch) * 0.8F;
    }

//    @Inject(
//            method = "getEquippedStack",
//            at = @At(
//                    value = "RETURN",
//                    ordinal = 1
//            ),
//            cancellable = true)
//    private void legacyAnimations$hypixelShieldBug(EquipmentSlot slot, CallbackInfoReturnable<ItemStack> cir)
//    {
//        if (ServerUtils.INSTANCE.isOnHypixel() && getMainHandStack().getItem() instanceof SwordItem && !isUsingItem())
//        {
//            cir.setReturnValue(new ItemStack(Items.SHIELD));
//        }
//    }

    @ModifyReturnValue(
            method = "getBaseDimensions",
            at = @At(
                    value = "RETURN"
            )
    )
    private EntityDimensions legacyAnimations$modifyStandingEyeHeight(EntityDimensions original, EntityPose pose)
    {
        if (!LegacyAnimationsSettings.CONFIG.instance().oldSneaking)
        {
            return original;
        }

        //todo: eyeheight under slabs

        if (pose != EntityPose.CROUCHING) {
            return original;
        }
        return original.withEyeHeight(original.eyeHeight() + 0.27F);
    }

}
