package com.mixces.oldanimations.mixin;

import com.mixces.oldanimations.config.OldAnimationsSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {

    @Shadow @Final private MinecraftClient client;
    @Shadow private int blockBreakingCooldown;

    @Inject(method = {"breakBlock", "attackBlock", "isCurrentlyBreaking"}, at = @At("HEAD"))
    private void breakBlock_attackBlock_isCurrentlyBreaking_damageSync(CallbackInfoReturnable<Boolean> cir) {
        syncSelectedSlot();
    }

    @Redirect(method = "updateBlockBreakingProgress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;isCurrentlyBreaking(Lnet/minecraft/util/math/BlockPos;)Z"))
    public boolean updateBlockBreakingProgress_fixDestroy(ClientPlayerInteractionManager instance, BlockPos pos) {
        return isCurrentlyBreaking(pos) && isBreakingBlock();
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "updateBlockBreakingProgress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;syncSelectedSlot()V", shift = At.Shift.AFTER), cancellable = true)
    public void updateBlockBreakingProgress_cancelDestroy(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (OldAnimationsSettings.CONFIG.instance().punchDuringUsage && client.player.isUsingItem()) {
            cancelBlockBreaking();
            blockBreakingCooldown = 5;
            cir.setReturnValue(true);
        }
    }

    @Shadow public abstract boolean isBreakingBlock();
    @Shadow public abstract void cancelBlockBreaking();
    @Shadow protected abstract void syncSelectedSlot();
    @Shadow protected abstract boolean isCurrentlyBreaking(BlockPos pos);

}
