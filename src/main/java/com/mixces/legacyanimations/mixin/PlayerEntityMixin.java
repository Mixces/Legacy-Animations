package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import com.mixces.legacyanimations.mixin.interfaces.IEntityMixin;
import com.mixces.legacyanimations.mixin.interfaces.ILivingEntityMixin;
import com.mixces.legacyanimations.util.ItemUtils;
import com.mixces.legacyanimations.util.ServerUtils;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin
{
    //todo: hypixel

//    @ModifyReturnValue(
//            method = "getEquippedStack",
//            at = @At(
//                    value = "RETURN",
//                    ordinal = 1
//            )
//    )
//    private ItemStack legacyAnimations$fakeShield(ItemStack original)
//    {
//        if (!ServerUtils.INSTANCE.isValidServer())
//        {
//            return original;
//        }
//
//        if (isBlocking())
//        {
//            return original;
//        }
//
//        if (!ItemUtils.INSTANCE.isSwordInMainHand(null))
//        {
//            return original;
//        }
//
//        return new ItemStack(Items.SHIELD);
//    }

//    @Inject(
//            method = "resetLastAttackedTicks",
//            at = @At(
//                    value = "HEAD"
//            ),
//            cancellable = true
//    )
//    private void legacyAnimations$removeAttackDelay(CallbackInfo ci)
//    {
//        if (!ServerUtils.INSTANCE.isValidServer())
//        {
//            return;
//        }
//
//        ci.cancel();
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
