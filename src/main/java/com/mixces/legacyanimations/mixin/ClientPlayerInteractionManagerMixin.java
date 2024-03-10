package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {

    @Shadow @Final private MinecraftClient client;
    @Shadow public abstract boolean isBreakingBlock();

    @ModifyExpressionValue(
            method = "updateBlockBreakingProgress",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;isCurrentlyBreaking(Lnet/minecraft/util/math/BlockPos;)Z"
            )
    )
    public boolean fixBreakingBlockCheck(boolean original) {
        return (!LegacyAnimationsSettings.CONFIG.instance().punchDuringUsage || isBreakingBlock()) && original;
    }

    @Inject(
            method = "updateBlockBreakingProgress",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;syncSelectedSlot()V",
                    shift = At.Shift.AFTER
            ),
            cancellable = true)
    public void cancelIllegalDestroy(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (LegacyAnimationsSettings.CONFIG.instance().punchDuringUsage && client.player != null && client.player.isUsingItem()) {
            cir.setReturnValue(true);
        }
    }

}
