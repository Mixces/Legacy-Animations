package com.mixces.oldanimations.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

	@Shadow public ClientPlayerInteractionManager interactionManager;
	@Shadow public HitResult crosshairTarget;
	@Shadow public ClientWorld world;
	@Shadow @Final public ParticleManager particleManager;
	@Shadow public ClientPlayerEntity player;

	@Inject(method = "handleBlockBreaking", at = @At("HEAD"))
	public void handleBlockBreaking_blockHitAnimation(boolean breaking, CallbackInfo ci) {
		if (player.isUsingItem()) {
			if (breaking && crosshairTarget != null && crosshairTarget.getType() == HitResult.Type.BLOCK) {
				Hand hand = player.preferredHand;
				BlockHitResult blockHitResult = (BlockHitResult) crosshairTarget;
				Direction direction = blockHitResult.getSide();
				BlockPos blockPos = blockHitResult.getBlockPos();
				if (!world.getBlockState(blockPos).isAir()) {
					particleManager.addBlockBreakingParticles(blockPos, direction);

					if (!player.handSwinging || player.handSwingTicks >= 3 || player.handSwingTicks < 0) {
						player.handSwingTicks = -1;
						player.handSwinging = true;
						player.preferredHand = hand;
					}
				}
				return;
			}
			interactionManager.cancelBlockBreaking();
		}
	}

}