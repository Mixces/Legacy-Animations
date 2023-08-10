package com.mixces.oldanimations.mixin;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

//    @Inject(method = "resetLastAttackedTicks", at = @At("HEAD"), cancellable = true)
//    private void resetLastAttackedTicks_removeDelay(CallbackInfo ci) {
//        ci.cancel();
//    }

}
