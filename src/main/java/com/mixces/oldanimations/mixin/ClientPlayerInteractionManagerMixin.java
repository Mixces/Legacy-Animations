package com.mixces.oldanimations.mixin;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {

    @Shadow protected abstract void syncSelectedSlot();
    @Shadow protected abstract boolean isCurrentlyBreaking(BlockPos pos);
    @Shadow private boolean breakingBlock;

    @Inject(method = "breakBlock", at = @At("HEAD"))
    private void breakBlock_damageSync(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        syncSelectedSlot();
    }

    @Inject(method = "attackBlock", at = @At("HEAD"))
    private void attackBlock_damageSync(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        syncSelectedSlot();
    }

    @Redirect(method = "updateBlockBreakingProgress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;isCurrentlyBreaking(Lnet/minecraft/util/math/BlockPos;)Z"))
    public boolean updateBlockBreakingProgress_fixDestroy(ClientPlayerInteractionManager instance, BlockPos pos) {
        return isCurrentlyBreaking(pos) && breakingBlock;
    }

    @Inject(method = "isBreakingBlock", at = @At("HEAD"), cancellable = true)
    private void isBreakingBlock_allowPunching(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

}
