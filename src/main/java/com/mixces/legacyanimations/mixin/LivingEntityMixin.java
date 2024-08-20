package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import com.mixces.legacyanimations.duck.PlayerPitchInterface;
import com.mixces.legacyanimations.util.ServerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements PlayerPitchInterface {

    @Shadow
    public abstract boolean isUsingItem();

    @Shadow
    protected ItemStack activeItemStack;

    @Shadow
    public float bodyYaw;

    @Unique
    public float legacyAnimations$prevCameraPitch;

    @Unique
    public float legacyAnimations$cameraPitch;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    //todo: hypixel rahh
    @Inject(
            method = "isBlocking",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void legacyAnimations$fixSync(CallbackInfoReturnable<Boolean> cir) {
        if (LegacyAnimationsSettings.getInstance().noShieldDelay) {
            final UseAction action = activeItemStack.getItem().getUseAction(activeItemStack);
            cir.setReturnValue(isUsingItem() && action == UseAction.BLOCK);
        }
    }

    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/MathHelper;abs(F)F"
            )
    )
    private float legacyAnimations$revertBackwardsWalk(float value, Operation<Float> original) {
        return LegacyAnimationsSettings.getInstance().oldWalking ? 0.0F : original.call(value);
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
    private void legacyAnimations$setPrevCameraPitch(CallbackInfo ci) {
        if (LegacyAnimationsSettings.getInstance().oldViewBob) {
            legacyAnimations$prevCameraPitch = legacyAnimations$cameraPitch;
        }
    }

    //todo: make this not be overwritten
    //note for lowercasebtw: this code right here is necessary to make the old backwards walk to look 1:1 with 1.8
    /**
     * @author Mixces
     * @reason Head movement
     */
    @Overwrite
    public float turnHead(float bodyRotation, float headRotation) {
        final float f = MathHelper.wrapDegrees(bodyRotation - bodyYaw);
        bodyYaw += f * 0.3F;
        float f1 = MathHelper.wrapDegrees(getYaw() - bodyYaw);
        final boolean flag = f1 < -90.0F || f1 >= 90.0F;
        if (f1 < -75.0F) f1 = -75.0F;
        if (f1 >= 75.0F) f1 = 75.0F;
        bodyYaw = getYaw() - f1;
        if (f1 * f1 > 2500.0F) bodyYaw += f1 * 0.2F;
        if (flag) headRotation *= -1.0F;
        return headRotation;
    }
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
    public float legacyAnimations$getPrevPlayerPitch() {
        return legacyAnimations$prevCameraPitch;
    }

    @Override
    public float legacyAnimations$getPlayerPitch() {
        return legacyAnimations$cameraPitch;
    }
}
