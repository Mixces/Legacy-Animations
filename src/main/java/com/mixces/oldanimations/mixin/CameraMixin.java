package com.mixces.oldanimations.mixin;

import com.mixces.oldanimations.config.OldAnimationsSettings;
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

    @Inject(method = "updateEyeHeight", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getStandingEyeHeight()F"), cancellable = true)
    private void updateEyeHeight_oldSneak(CallbackInfo ci) {
        if (OldAnimationsSettings.CONFIG.instance().oldSneaking && getFocusedEntity().getStandingEyeHeight() < cameraY) {
            cameraY = getFocusedEntity().getStandingEyeHeight();
            ci.cancel();
        }
    }

}
