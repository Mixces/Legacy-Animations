package com.mixces.oldanimations.mixin;

import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow private float cameraY;
    @Shadow public abstract Entity getFocusedEntity();

    @Inject(method = "updateEyeHeight", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/Camera;cameraY:F", ordinal = 1), cancellable = true)
    private void updateEyeHeight_oldSneak(CallbackInfo ci) {
        if (getFocusedEntity().getStandingEyeHeight() < cameraY) {
            cameraY = getFocusedEntity().getStandingEyeHeight();
        } else {
            cameraY += (getFocusedEntity().getStandingEyeHeight() - cameraY) * 0.5F;
        }
        ci.cancel();
    }

}
