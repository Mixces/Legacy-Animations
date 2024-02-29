package com.mixces.legacyanimations.mixin;

import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow private float cameraY;
    @Shadow private Entity focusedEntity;

    @Inject(method = "updateEyeHeight", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/client/render/Camera;cameraY:F"), cancellable = true)
    private void addOldSneakCalculation(CallbackInfo ci) {
        if (LegacyAnimationsSettings.CONFIG.instance().oldSneaking && focusedEntity.getStandingEyeHeight() < cameraY) {
            cameraY = focusedEntity.getStandingEyeHeight();
            ci.cancel();
        }
    }

}
