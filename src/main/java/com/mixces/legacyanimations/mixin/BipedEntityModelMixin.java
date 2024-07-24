package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
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

        arm.pitch *= 0.5F - (float) (Math.PI / 10) * 3;
        arm.yaw = 0.0F;
    }

}