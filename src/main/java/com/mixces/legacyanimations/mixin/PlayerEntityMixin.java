package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

//    @Inject(method = "resetLastAttackedTicks", at = @At("HEAD"), cancellable = true)
//    private void resetLastAttackedTicks_removeDelay(CallbackInfo ci) {
//        ci.cancel();
//    }
    @ModifyReturnValue(
            method = "getActiveEyeHeight",
            at = @At(
                    value = "RETURN",
                    ordinal = 1
            )
    )
    private float increaseEyeHeight(float constant) {
        return LegacyAnimationsSettings.CONFIG.instance().oldSneaking ? 1.54F : constant;
    }

}
