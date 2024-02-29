package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin<T extends LivingEntity> {

    @Shadow protected abstract void positionRightArm(T entity);
    @Shadow protected abstract void positionLeftArm(T entity);
    @Shadow public BipedEntityModel.ArmPose leftArmPose;
    @Shadow public BipedEntityModel.ArmPose rightArmPose;

    @ModifyExpressionValue(
            method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;isUsingItem()Z",
                    ordinal = 0
            )
    )
    public boolean removeCondition(boolean original) {
        return !LegacyAnimationsSettings.CONFIG.instance().oldSneaking && original;
    }

    @WrapWithCondition(
            method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;positionRightArm(Lnet/minecraft/entity/LivingEntity;)V"
            )
    )
    public boolean removeMethodCall1(BipedEntityModel<?> instance, T entity) {
        return !LegacyAnimationsSettings.CONFIG.instance().oldSneaking;
    }

    @WrapWithCondition(
            method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;positionLeftArm(Lnet/minecraft/entity/LivingEntity;)V"
            )
    )
    public boolean removeMethodCall2(BipedEntityModel<?> instance, T entity) {
        return !LegacyAnimationsSettings.CONFIG.instance().oldSneaking;
    }


    @Inject(
            method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V",
            at = @At(
                    value = "JUMP",
                    opcode = Opcodes.IF_ACMPEQ,
                    ordinal = 0
            )
    )
    private void positionRightArm_oldBowSwing(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        if (LegacyAnimationsSettings.CONFIG.instance().oldSneaking) {
            boolean bl3 = livingEntity.getMainArm() == Arm.RIGHT;
            if (livingEntity.isUsingItem()) {
                boolean bl4 = livingEntity.getActiveHand() == Hand.MAIN_HAND;
                if (bl4 == bl3) {
                    positionRightArm(livingEntity);
                } else {
                    positionLeftArm(livingEntity);
                }
            } else {
                boolean bl4 = bl3 ? leftArmPose.isTwoHanded() : rightArmPose.isTwoHanded();
                if (bl3 != bl4) {
                    positionLeftArm(livingEntity);
                    positionRightArm(livingEntity);
                } else {
                    positionRightArm(livingEntity);
                    positionLeftArm(livingEntity);
                }
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
    public boolean removeFields1(ModelPart instance, float value) {
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

}