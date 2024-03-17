package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

	@Shadow @Nullable public ClientPlayerEntity player;

	@Shadow private static MinecraftClient instance;

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
			method = "tick",
			at = @At(
					value = "HEAD"
			)
	)
	private void fakeShield(CallbackInfo ci) {
//		if (LegacyAnimationsSettings.CONFIG.instance().punchDuringUsage && instance.isUsingItem()) {
//			legacyAnimations$fakeSwingHand(instance, hand);
//		} else {
//			instance.swingHand(hand);
//		}

		if (player == null) return;

		if (player.getInventory().offHand.isEmpty()) {

			player.getInventory().offHand.set(EquipmentSlot.OFFHAND.getEntitySlotId(), new ItemStack(Items.SHIELD));
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