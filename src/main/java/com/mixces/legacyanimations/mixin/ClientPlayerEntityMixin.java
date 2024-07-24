package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @WrapOperation(
            method = "tickMovement",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/input/Input;hasForwardMovement()Z"
            )
    )
    public boolean legacyAnimations$revertMovement2(Input instance, Operation<Boolean> original) {
        if (!LegacyAnimationsSettings.getInstance().oldMovement)
        {
            return original.call(instance);
        }
        return instance.movementForward >= 0.8;
    }

    @ModifyExpressionValue(
            method = "isWalking",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;isSubmergedInWater()Z"
            )
    )
    public boolean legacyAnimations$revertMovement3(boolean original) {
        if (!LegacyAnimationsSettings.getInstance().oldMovement)
        {
            return original;
        }
        return false;
    }

}
