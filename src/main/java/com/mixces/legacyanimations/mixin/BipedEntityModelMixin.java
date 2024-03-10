package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
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
public abstract class BipedEntityModelMixin<T extends LivingEntity> {

    @Shadow public BipedEntityModel.ArmPose leftArmPose;
    @Shadow public BipedEntityModel.ArmPose rightArmPose;
    @Shadow @Final public ModelPart rightArm;
    @Shadow @Final public ModelPart leftArm;
    @Shadow @Final public ModelPart head;

    @Inject(
            method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V",
            at = @At(
                    value = "JUMP",
                    opcode = Opcodes.IF_ACMPEQ,
                    ordinal = 0
            )
    )
    private void fixIncorrectArmPlacement(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        if (LegacyAnimationsSettings.CONFIG.instance().punchDuringUsage) {
            rightArm.roll = 0.0F;
            leftArm.roll = 0.0F;
            if (rightArmPose == BipedEntityModel.ArmPose.BOW_AND_ARROW) {
                rightArm.yaw = -0.1F + head.yaw;
                leftArm.yaw = 0.1F + head.yaw + 0.4F;
                rightArm.pitch = (float) (-Math.PI / 2) + head.pitch;
                leftArm.pitch = (float) (-Math.PI / 2) + head.pitch;
            }
            if (leftArmPose == BipedEntityModel.ArmPose.BOW_AND_ARROW) {
                rightArm.yaw = -0.1F + head.yaw - 0.4F;
                leftArm.yaw = 0.1F + head.yaw;
                rightArm.pitch = (float) (-Math.PI / 2) + head.pitch;
                leftArm.pitch = (float) (-Math.PI / 2) + head.pitch;
            }
        }
    }

    @ModifyConstant(
            method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V",
            constant = @Constant(
                    floatValue = 12.2F
            )
    )
    private float oldSneakValue1(float constant) {
        return LegacyAnimationsSettings.CONFIG.instance().oldSneaking ? 9.0F : constant;
    }

    @ModifyConstant(
            method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V",
            slice = @Slice(
                    from = @At(
                            value = "FIELD",
                            opcode = Opcodes.PUTFIELD,
                            target = "Lnet/minecraft/client/model/ModelPart;pitch:F",
                            ordinal = 15
                    ),
                    to = @At(
                            value = "FIELD",
                            opcode = Opcodes.PUTFIELD,
                            target = "Lnet/minecraft/client/model/ModelPart;pivotY:F",
                            ordinal = 6
                    )
            ),
            constant = @Constant(
                    floatValue = 0.0F
            )
    )
    private float oldSneakValue2(float constant) {
        return LegacyAnimationsSettings.CONFIG.instance().oldSneaking ? 0.1F : constant;
    }

    @ModifyConstant(
            method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V",
            constant = @Constant(
                    floatValue = 4.2F
            )
    )
    private float oldSneakValue3(float constant) {
        return LegacyAnimationsSettings.CONFIG.instance().oldSneaking ? 1.0F : constant;
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
    public boolean removeConflictingFields(ModelPart instance, float value) {
        return !LegacyAnimationsSettings.CONFIG.instance().oldSneaking;
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
    public boolean removeFields2(ModelPart instance, float value) {
        return !LegacyAnimationsSettings.CONFIG.instance().oldSneaking;
    }

    @Redirect(
            method = "positionBlockingArm",
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.PUTFIELD,
                    target = "Lnet/minecraft/client/model/ModelPart;pitch:F"
            )
    )
    public void oldBlockingArmPitch(ModelPart instance, float value) {
        if (LegacyAnimationsSettings.CONFIG.instance().oldSwordBlock) {
            instance.pitch = instance.pitch * 0.5F - (float) (Math.PI / 3);
        } else {
            instance.pitch = value;
        }
    }

    @Redirect(
            method = "positionBlockingArm",
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.PUTFIELD,
                    target = "Lnet/minecraft/client/model/ModelPart;yaw:F"
            )
    )
    public void oldBlockingArmYaw(ModelPart instance, float value) {
        if (LegacyAnimationsSettings.CONFIG.instance().oldSwordBlock) {
            instance.yaw = 0.0F;
        } else {
            instance.yaw = value;
        }
    }

    @ModifyArg(
            method = "positionLeftArm",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;positionBlockingArm(Lnet/minecraft/client/model/ModelPart;Z)V"
            ),
            index = 0
    )
    private ModelPart switchBlockingArm(ModelPart arm) {
        return LegacyAnimationsSettings.CONFIG.instance().oldSwordBlock ? rightArm : arm;
    }

    @ModifyArg(
            method = "positionRightArm",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;positionBlockingArm(Lnet/minecraft/client/model/ModelPart;Z)V"
            ),
            index = 0
    )
    private ModelPart switchBlockingArm2(ModelPart arm) {
        return LegacyAnimationsSettings.CONFIG.instance().oldSwordBlock ? leftArm : arm;
    }

}