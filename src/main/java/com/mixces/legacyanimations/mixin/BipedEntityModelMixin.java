package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Arm;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin<T extends LivingEntity>
{

    @Shadow public BipedEntityModel.ArmPose leftArmPose;
    @Shadow public BipedEntityModel.ArmPose rightArmPose;
    @Shadow @Final public ModelPart rightArm;
    @Shadow @Final public ModelPart leftArm;
    @Shadow @Final public ModelPart head;
    @Shadow @Final public ModelPart rightLeg;
    @Shadow @Final public ModelPart leftLeg;
    @Shadow protected abstract ModelPart getArm(Arm arm);

    @Inject(
            method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;animateArms(Lnet/minecraft/entity/LivingEntity;F)V"
            )
    )
    public void legacyAnimations$adjustArmYaw(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci)
    {
        if (!LegacyAnimationsSettings.getInstance().oldSwordBlock)
        {
            return;
        }

        final ClientPlayerEntity player = MinecraftClient.getInstance().player;

        if (player == null)
        {
            return;
        }

        getArm(player.getMainArm()).yaw = 0.0F;
    }

    @Inject(
            method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;rightArmPose:Lnet/minecraft/client/render/entity/model/BipedEntityModel$ArmPose;",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 1
            )
    )
    private void legacyAnimations$fixIncorrectArmPlacement2(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci)
    {
        if (!LegacyAnimationsSettings.getInstance().punchDuringUsage)
        {
            return;
        }

        final BipedEntityModel.ArmPose BOW_AND_ARROW = BipedEntityModel.ArmPose.BOW_AND_ARROW;
        final boolean isRightArmPose = rightArmPose == BOW_AND_ARROW;
        final boolean isLeftArmPose = leftArmPose == BOW_AND_ARROW;

        if (!isRightArmPose && !isLeftArmPose)
        {
            return;
        }

        if (isRightArmPose)
        {
            rightArm.roll = 0.0F;
            rightArm.yaw = -0.1F + head.yaw;
            leftArm.yaw = 0.1F + head.yaw + 0.4F;
        }

        if (isLeftArmPose)
        {
            leftArm.roll = 0.0F;
            rightArm.yaw = -0.1F + head.yaw - 0.4F;
            leftArm.yaw = 0.1F + head.yaw;
        }

        rightArm.pitch = (float) (-Math.PI / 2) + head.pitch;
        leftArm.pitch = (float) (-Math.PI / 2) + head.pitch;
    }

    @Inject(
            method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V",
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.GETFIELD,
                    target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;leftLeg:Lnet/minecraft/client/model/ModelPart;"
            ),
            slice = @Slice(
                    from = @At(
                            value = "FIELD",
                            opcode = Opcodes.GETFIELD,
                            target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;rightLeg:Lnet/minecraft/client/model/ModelPart;",
                            ordinal = 7
                    ),
                    to = @At(
                            value = "FIELD",
                            opcode = Opcodes.GETFIELD,
                            target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;body:Lnet/minecraft/client/model/ModelPart;",
                            ordinal = 2
                    )
            )
    )
    private void legacyAnimations$oldSneakValue1(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci)
    {
        if (!LegacyAnimationsSettings.getInstance().oldSneaking)
        {
            return;
        }

        rightLeg.pivotY = 9.0f;
    }

    @Inject(
            method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V",
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.GETFIELD,
                    target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;head:Lnet/minecraft/client/model/ModelPart;"
            ),
            slice = @Slice(
                    from = @At(
                            value = "FIELD",
                            opcode = Opcodes.GETFIELD,
                            target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;leftLeg:Lnet/minecraft/client/model/ModelPart;",
                            ordinal = 7
                    ),
                    to = @At(
                            value = "FIELD",
                            opcode = Opcodes.GETFIELD,
                            target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;leftArm:Lnet/minecraft/client/model/ModelPart;",
                            ordinal = 7
                    )
            )
    )
    private void legacyAnimations$oldSneakValue2(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci)
    {
        if (!LegacyAnimationsSettings.getInstance().oldSneaking)
        {
            return;
        }

        leftLeg.pivotY = 9.0f;
    }

    @Inject(
            method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V",
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.GETFIELD,
                    target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;leftLeg:Lnet/minecraft/client/model/ModelPart;",
                    ordinal = 9
            )
    )
    private void legacyAnimations$oldSneakValue3(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci)
    {
        if (!LegacyAnimationsSettings.getInstance().oldSneaking)
        {
            return;
        }

        rightLeg.pivotZ = 0.1f;
    }

    @Inject(
            method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V",
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.GETFIELD,
                    target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;head:Lnet/minecraft/client/model/ModelPart;",
                    ordinal = 5
            )
    )
    private void legacyAnimations$oldSneakValue4(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci)
    {
        if (!LegacyAnimationsSettings.getInstance().oldSneaking)
        {
            return;
        }

        leftLeg.pivotZ = 0.1f;
    }

    @Inject(
            method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V",
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.GETFIELD,
                    target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;body:Lnet/minecraft/client/model/ModelPart;",
                    ordinal = 2
            )
    )
    private void legacyAnimations$oldSneakValue5(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci)
    {
        if (!LegacyAnimationsSettings.getInstance().oldSneaking)
        {
            return;
        }

        head.pivotY = 1.0f;
    }

    @WrapWithCondition(
            method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V",
            slice = @Slice(
                    from = @At(
                            value = "FIELD",
                            opcode = Opcodes.PUTFIELD,
                            target = "Lnet/minecraft/client/model/ModelPart;pivotY:F",
                            ordinal = 3
                    ),
                    to = @At(
                            value = "FIELD",
                            opcode = Opcodes.PUTFIELD,
                            target = "Lnet/minecraft/client/model/ModelPart;pitch:F",
                            ordinal = 15
                    )
            ),
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.PUTFIELD,
                    target = "Lnet/minecraft/client/model/ModelPart;pivotY:F"
            )
    )
    public boolean legacyAnimations$removeConflictingFields1(ModelPart instance, float value)
    {
        return !LegacyAnimationsSettings.getInstance().oldSneaking;
    }

    @WrapWithCondition(
            method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V",
            slice = @Slice(
                    from = @At(
                            value = "FIELD",
                            opcode = Opcodes.PUTFIELD,
                            target = "Lnet/minecraft/client/model/ModelPart;pivotY:F",
                            ordinal = 9
                    ),
                    to = @At(
                            value = "FIELD",
                            opcode = Opcodes.GETFIELD,
                            target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;rightArmPose:Lnet/minecraft/client/render/entity/model/BipedEntityModel$ArmPose;",
                            ordinal = 1
                    )
            ),
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.PUTFIELD,
                    target = "Lnet/minecraft/client/model/ModelPart;pivotY:F"
            )
    )
    public boolean legacyAnimations$removeConflictingFields2(ModelPart instance, float value)
    {
        return !LegacyAnimationsSettings.getInstance().oldSneaking;
    }

    @ModifyExpressionValue(
            method = "animateArms",
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.GETFIELD,
                    target = "Lnet/minecraft/client/model/ModelPart;yaw:F",
                    ordinal = 9
            )
    )
    public float legacyAnimations$mirrorSwing1(float original, @Local Arm arm)
    {
        return (arm == Arm.LEFT ? -1 : 1) * original;
    }

    @WrapOperation(
            method = "animateArms",
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.GETFIELD,
                    target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;leftArm:Lnet/minecraft/client/model/ModelPart;",
                    ordinal = 3
            )
    )
    public ModelPart legacyAnimations$mirrorSwing2(BipedEntityModel<T> instance, Operation<ModelPart> original, @Local ModelPart modelPart)
    {
        return modelPart;
    }

    @ModifyExpressionValue(
            method = "animateArms",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/MathHelper;sin(F)F",
                    ordinal = 5
            )
    )
    public float legacyAnimations$mirrorSwing3(float original, @Local Arm arm)
    {
        return (arm == Arm.LEFT ? -1 : 1) * original;
    }

    @Inject(
            method = "positionBlockingArm",
            at = @At(
                    value = "TAIL"
            )
    )
    public void legacyAnimations$oldBlockingArm(ModelPart arm, boolean rightArm, CallbackInfo ci)
    {
        if (!LegacyAnimationsSettings.getInstance().oldSwordBlock)
        {
            return;
        }

        arm.pitch = arm.pitch * 0.5F - (float) (Math.PI / 10) * 3;
        arm.yaw = (rightArm ? -1.0f : 1.0f) * (float) (-Math.PI / 6);
    }

//    @WrapOperation(
//            method = "positionRightArm",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel$ArmPose;ordinal()I"
//            )
//    )
//    private int shit(BipedEntityModel.ArmPose instance, Operation<Integer> original)
//    {
//        return 2;
//    }

}