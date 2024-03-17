package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import com.mixces.legacyanimations.util.ServerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

	@Shadow public ClientPlayerEntity player;

	@Shadow public int attackCooldown;

	@ModifyExpressionValue(
			method = "doItemUse",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;isBreakingBlock()Z"
			)
	)
	private boolean interruptBlockBreaking(boolean original) {
		return !LegacyAnimationsSettings.CONFIG.instance().punchDuringUsage && original;
	}

	@Redirect(
			method = "handleBlockBreaking",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"
			)
	)
	private boolean allowWhileUsingItem(ClientPlayerEntity instance) {
		if (LegacyAnimationsSettings.CONFIG.instance().punchDuringUsage) {
			return !instance.canModifyBlocks();
		}
		return instance.isUsingItem();
	}

	@Redirect(
			method = "handleBlockBreaking",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/network/ClientPlayerEntity;swingHand(Lnet/minecraft/util/Hand;)V"
			)
	)
	private void swapForFakeSwing(ClientPlayerEntity instance, Hand hand) {
		if (LegacyAnimationsSettings.CONFIG.instance().punchDuringUsage && instance.isUsingItem()) {
			legacyAnimations$fakeSwingHand(instance, hand);
		} else {
			instance.swingHand(hand);
		}
	}

	@Inject(
			method = "doAttack",
			at = @At(
					value = "HEAD"
			)
	)
	private void allowWhileUsingItema(CallbackInfoReturnable<Boolean> cir) {
		if (ServerUtils.INSTANCE.isOnHypixel()) {
			attackCooldown = 0;
		}
	}

	@Unique
	private static void legacyAnimations$fakeSwingHand(ClientPlayerEntity player, Hand hand) {
		if (!player.handSwinging || player.handSwingTicks >= ((LivingEntityInvoker) player).invokeGetHandSwingDuration() / 2 || player.handSwingTicks < 0) {
			player.handSwingTicks = -1;
			player.handSwinging = true;
			player.preferredHand = hand;
		}
	}

}