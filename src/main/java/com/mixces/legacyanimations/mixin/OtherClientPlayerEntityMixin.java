package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.network.OtherClientPlayerEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(OtherClientPlayerEntity.class)
public class OtherClientPlayerEntityMixin {

    //todo: fix this
//    @ModifyExpressionValue(
//            method = "tickMovement",
//            at = @At(
//                    value = "FIELD",
//                    opcode = Opcodes.GETFIELD,
//                    target = "Lnet/minecraft/client/network/OtherClientPlayerEntity;headTrackingIncrements:I",
//                    ordinal = 0
//            )
//    )
//    private int legacyAnimations$cancelHeadUpdate(int original) {
//        return 0;
//    }

}
