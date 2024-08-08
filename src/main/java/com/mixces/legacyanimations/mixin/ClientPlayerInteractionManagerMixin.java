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

    @Shadow
    public abstract boolean isBreakingBlock();

    @Shadow
    public abstract void cancelBlockBreaking();

    @Shadow
    private float currentBreakingProgress;

    @Shadow
    @Final
    private MinecraftClient client;

    @ModifyExpressionValue(
            method = "updateBlockBreakingProgress",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;isCurrentlyBreaking(Lnet/minecraft/util/math/BlockPos;)Z"
            )
    )
    public boolean legacyAnimations$fixBreakingBlockCheck(boolean original) {
        return (!LegacyAnimationsSettings.getInstance().punchDuringUsage || isBreakingBlock()) && original;
    }

    @Inject(
            method = "updateBlockBreakingProgress",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;syncSelectedSlot()V",
                    shift = At.Shift.AFTER
            ),
            cancellable = true
    )
    public void legacyAnimations$cancelIllegalDestroy(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (LegacyAnimationsSettings.getInstance().punchDuringUsage) {
            if (client.player == null) return;
            if (client.player.isUsingItem() && client.player.canModifyBlocks()) {
                if (currentBreakingProgress > 0.0f) cancelBlockBreaking();
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(
            method = "getBlockBreakingProgress",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void legacyAnimations$oldMiningProgress(CallbackInfoReturnable<Integer> cir) {
        if (LegacyAnimationsSettings.getInstance().oldBreakProgress) {
            cir.setReturnValue((int)(this.currentBreakingProgress * 10.0f) - 1);
        }
    }
}
