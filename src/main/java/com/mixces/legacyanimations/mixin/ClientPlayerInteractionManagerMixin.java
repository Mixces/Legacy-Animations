package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {

//    @Shadow @Final private MinecraftClient client;
//    @Shadow private int blockBreakingCooldown;

    @Shadow public abstract boolean isBreakingBlock();

    @ModifyExpressionValue(method = "updateBlockBreakingProgress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;isCurrentlyBreaking(Lnet/minecraft/util/math/BlockPos;)Z"))
    public boolean fixBreakingBlockCheck(boolean original) {
        return original && isBreakingBlock();
    }

//    @SuppressWarnings("ConstantConditions")
//    @Inject(method = "updateBlockBreakingProgress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;syncSelectedSlot()V", shift = At.Shift.AFTER), cancellable = true)
//    public void updateBlockBreakingProgress_cancelDestroy(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
//        if (LegacyAnimationsSettings.CONFIG.instance().punchDuringUsage && client.player.isUsingItem()) {
//            cancelBlockBreaking();
//            blockBreakingCooldown = 5;
//            cir.setReturnValue(true);
//        }
//    }

//    @Shadow public abstract void cancelBlockBreaking();
//    @Shadow protected abstract boolean isCurrentlyBreaking(BlockPos pos);

}
