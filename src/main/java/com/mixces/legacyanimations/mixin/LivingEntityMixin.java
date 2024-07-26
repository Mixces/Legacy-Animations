package com.mixces.legacyanimations.mixin;

import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import com.mixces.legacyanimations.duck.PlayerPitchInterface;
import com.mixces.legacyanimations.util.ServerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements PlayerPitchInterface
{

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract boolean isUsingItem();
    @Shadow protected ItemStack activeItemStack;
    @Shadow public float bodyYaw;
    @Unique public float legacyAnimations$prevCameraPitch;
    @Unique public float legacyAnimations$cameraPitch;

    //todo: hypixel rahh
    @Inject(
            method = "isBlocking",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void legacyAnimations$fixSync(CallbackInfoReturnable<Boolean> cir)
    {
        if (!LegacyAnimationsSettings.getInstance().noShieldDelay)
        {
            return;
        }

        final UseAction action = activeItemStack.getItem().getUseAction(activeItemStack);

        cir.setReturnValue(isUsingItem() && action == UseAction.BLOCK);
    }

    @ModifyConstant(
            method = "tick",
            constant = @Constant(
                    floatValue = 180.0f
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/util/math/MathHelper;abs(F)F"
                    ),
                    to = @At(
                            value = "FIELD",
                            opcode = Opcodes.GETFIELD,
                            target = "Lnet/minecraft/entity/LivingEntity;handSwingProgress:F"
                    )
            )
    )
    private float legacyAnimations$revertBackwardsWalk(float constant)
    {
        if (!LegacyAnimationsSettings.getInstance().oldWalking)
        {
            return constant;
        }
        return 0.0F;
    }

    @Inject(
            method = "baseTick",
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.GETFIELD,
                    target = "Lnet/minecraft/entity/LivingEntity;hurtTime:I",
                    ordinal = 0
            )
    )
    private void legacyAnimations$setPrevCameraPitch(CallbackInfo ci)
    {
        if (!LegacyAnimationsSettings.getInstance().oldViewBob)
        {
            return;
        }
        legacyAnimations$prevCameraPitch = legacyAnimations$cameraPitch;
    }

//    /**
//     * @author Mixces
//     * @reason Head movement
//     */
//    @Overwrite
//    public float turnHead(float bodyRotation, float headRotation)
//    {
//        final float f = MathHelper.wrapDegrees(bodyRotation - bodyYaw);
//        bodyYaw += f * 0.3F;
//        float f1 = MathHelper.wrapDegrees(getYaw() - bodyYaw);
//        final boolean flag = f1 < -90.0F || f1 >= 90.0F;
//
//        if (f1 < -75.0F)
//        {
//            f1 = -75.0F;
//        }
//
//        if (f1 >= 75.0F)
//        {
//            f1 = 75.0F;
//        }
//
//        bodyYaw = getYaw() - f1;
//
//        if (f1 * f1 > 2500.0F)
//        {
//            bodyYaw += f1 * 0.2F;
//        }
//
//        if (flag)
//        {
//            headRotation *= -1.0F;
//        }
//
//        return headRotation;
//    }
//
//    @ModifyExpressionValue(
//            method = "tickMovement",
//            at = @At(
//                    value = "FIELD",
//                    opcode = Opcodes.GETFIELD,
//                    target = "Lnet/minecraft/entity/LivingEntity;headTrackingIncrements:I",
//                    ordinal = 0
//            )
//    )
//    private int legacyAnimations$cancelHeadUpdate(int original) {
//        return 0;
//    }
//
//    @Inject(
//            method = "updateTrackedHeadRotation",
//            at = @At(
//                    value = "HEAD"
//            ),
//            cancellable = true
//    )
//    public void legacyAnimations$cancelHeadUpdate2(float yaw, int interpolationSteps, CallbackInfo ci) {
//        ci.cancel();
//    }

    @Override
    public float legacyAnimations$getPrevPlayerPitch()
    {
        return legacyAnimations$prevCameraPitch;
    }

    @Override
    public float legacyAnimations$getPlayerPitch()
    {
        return legacyAnimations$cameraPitch;
    }

}
