package com.mixces.oldanimations.mixin;

import com.mixces.oldanimations.config.OldAnimationsSettings;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin {
    
//    @Shadow @Final public ModelPart rightArm;
//    @Shadow @Final public ModelPart leftArm;
//
//    @Inject(method = {"positionLeftArm"}, at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;rightArm:Lnet/minecraft/client/model/ModelPart;", ordinal = 0))
//    private void positionLeftArm_oldBowSwing(CallbackInfo ci) {
//        if (OldAnimationsSettings.CONFIG.instance()..punchDuringUsage) {
//            rightArm.roll = 0.0F;
//            leftArm.roll = 0.0F;
//        }
//    }
//
//    @Inject(method = "positionRightArm", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;rightArm:Lnet/minecraft/client/model/ModelPart;", ordinal = 10))
//    private void positionRightArm_oldBowSwing(CallbackInfo ci) {
//        if (OldAnimationsSettings.CONFIG.instance().punchDuringUsage) {
//            rightArm.roll = 0.0F;
//            leftArm.roll = 0.0F;
//        }
//    }

}