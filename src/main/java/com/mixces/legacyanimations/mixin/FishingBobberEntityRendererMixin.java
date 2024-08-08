package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import com.mixces.legacyanimations.duck.EntityInterface;
import com.mixces.legacyanimations.mixin.access.ICameraMixin;
import com.mixces.legacyanimations.util.HandUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.FishingBobberEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingBobberEntityRenderer.class)
public abstract class FishingBobberEntityRendererMixin extends EntityRenderer<FishingBobberEntity> {

    protected FishingBobberEntityRendererMixin(EntityRendererFactory.Context ctx)
    {
        super(ctx);
    }

    @Inject(
            method = "render(Lnet/minecraft/entity/projectile/FishingBobberEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;peek()Lnet/minecraft/client/util/math/MatrixStack$Entry;",
                    ordinal = 0
            )
    )
    public void legacyAnimations$shiftRodBob(FishingBobberEntity fishingBobberEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (LegacyAnimationsSettings.getInstance().oldProjectiles) {
            final ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player == null) return;
            matrixStack.translate(HandUtils.INSTANCE.handMultiplier(player, dispatcher) * 0.25F, 0.0F, 0.0F);
        }
    }

    @WrapOperation(
            method = "getHandPos",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;getCameraPosVec(F)Lnet/minecraft/util/math/Vec3d;"
            )
    )
    public Vec3d legacyAnimations$useInterpolatedEyeHeight(PlayerEntity instance, float v, Operation<Vec3d> original) {
        final float lastCameraY = ((ICameraMixin) dispatcher.camera).getLastCameraY();
        final float cameraY = ((ICameraMixin) dispatcher.camera).getCameraY();
        final float eyeHeight = MathHelper.lerp(v, lastCameraY, cameraY);
        return ((EntityInterface) instance).legacyAnimations$getCameraPosVec(v, eyeHeight);
    }

    @ModifyExpressionValue(
            method = "getHandPos",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
            )
    )
    public boolean legacyAnimations$removeUselessCondition(boolean original, PlayerEntity player, float f, float tickDelta) {
        return true;
    }
}
