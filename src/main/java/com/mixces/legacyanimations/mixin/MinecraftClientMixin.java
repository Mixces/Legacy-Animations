package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import com.mixces.legacyanimations.mixin.access.ILivingEntityMixin;
import com.mixces.legacyanimations.util.ServerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

	@Shadow public ClientPlayerEntity player;
	@Shadow public int attackCooldown;
	@Shadow public Screen currentScreen;
	@Shadow @Final public GameOptions options;
	@Shadow @Final public Mouse mouse;

	@ModifyExpressionValue(
			method = "doItemUse",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;isBreakingBlock()Z"
			)
	)
	private boolean legacyAnimations$interruptBlockBreaking(boolean original)
	{
		if (!LegacyAnimationsSettings.getInstance().punchDuringUsage)
		{
			return original;
		}
		return false;
	}

	@ModifyExpressionValue(
			method = "handleBlockBreaking",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"
			)
	)
	private boolean legacyAnimations$allowWhileUsingItem(boolean original)
	{
		if (!LegacyAnimationsSettings.getInstance().punchDuringUsage)
		{
			return original;
		}
		return false;
	}

	@WrapOperation(
			method = "handleBlockBreaking",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/network/ClientPlayerEntity;swingHand(Lnet/minecraft/util/Hand;)V"
			)
	)
	private void legacyAnimations$swapForFakeSwing(ClientPlayerEntity instance, Hand hand, Operation<Void> original)
	{
		if (!LegacyAnimationsSettings.getInstance().punchDuringUsage)
		{
			original.call(instance, hand);

		}

		if (instance.isUsingItem())
		{
			legacyAnimations$fakeSwingHand(instance, hand);
		}
		else
		{
			original.call(instance, hand);
		}
	}

	@Inject(
			method = "handleInputEvents",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z",
					ordinal = 0
			)
	)
	private void legacyAnimations$addLeftClickCheck(CallbackInfo ci)
	{
		if (!ServerUtils.INSTANCE.isValidServer())
		{
			return;
		}

		if (currentScreen != null || !options.attackKey.isPressed() || !mouse.isCursorLocked())
		{
			attackCooldown = 0;
		}
	}

	//todo: find better way to do this?
	@ModifyArg(
			method = "handleInputEvents",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/MinecraftClient;handleBlockBreaking(Z)V"
			)
	)
	private boolean legacyAnimations$removeCondition(boolean original)
	{
		if (!LegacyAnimationsSettings.getInstance().punchDuringUsage)
		{
			return original;
		}
		return currentScreen == null && options.attackKey.isPressed() && mouse.isCursorLocked();
	}

	@Unique
	private static void legacyAnimations$fakeSwingHand(ClientPlayerEntity player, Hand hand)
	{
		final int handSwingDuration = ((ILivingEntityMixin) player).invokeGetHandSwingDuration();

		if (player.handSwinging && player.handSwingTicks < handSwingDuration / 2 && player.handSwingTicks >= 0)
		{
			return;
		}

		player.handSwingTicks = -1;
		player.handSwinging = true;
		player.preferredHand = hand;
	}

}