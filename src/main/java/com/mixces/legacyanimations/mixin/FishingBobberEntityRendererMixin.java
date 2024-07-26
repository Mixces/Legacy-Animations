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
public abstract class FishingBobberEntityRendererMixin extends EntityRenderer<FishingBobberEntity>
{

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
    public void legacyAnimations$shiftRodBob(FishingBobberEntity fishingBobberEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci)
    {
        if (!LegacyAnimationsSettings.getInstance().oldProjectiles)
        {
            return;
        }

        final ClientPlayerEntity player = MinecraftClient.getInstance().player;

        if (player == null)
        {
            return;
        }

        matrixStack.translate(HandUtils.INSTANCE.handMultiplier(player, dispatcher) * 0.25F, 0.0F, 0.0F);
    }

    @WrapOperation(
            method = "getHandPos",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;getCameraPosVec(F)Lnet/minecraft/util/math/Vec3d;"
            )
    )
    public Vec3d legacyAnimations$useInterpolatedEyeHeight(PlayerEntity instance, float v, Operation<Vec3d> original)
    {
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
    public boolean legacyAnimations$removeUselessCondition(boolean original, PlayerEntity player, float f, float tickDelta)
    {
        return true;
    }

//    /**
//     * @author a
//     * @reason a
//     */
//    @Overwrite
//    private Vec3d getHandPos(PlayerEntity player, float f, float tickDelta)
//    {
//        final float eyeHeight = MathHelper.lerp(dispatcher.camera.getLastTickDelta(), ((ICameraMixin) dispatcher.camera).getLastCameraY(), ((ICameraMixin) dispatcher.camera).getCameraY());
//
//        int i = player.getMainArm() == Arm.RIGHT ? 1 : -1;
//        if (!this.dispatcher.gameOptions.getPerspective().isFirstPerson() || player != MinecraftClient.getInstance().player)
//        {
//            float g = MathHelper.lerp(tickDelta, player.prevBodyYaw, player.bodyYaw) * ((float)Math.PI / 180);
//            double d = MathHelper.sin(g);
//            double e = MathHelper.cos(g);
//            float h = player.getScale();
//            double j = (double)i * 0.35 * (double)h;
//            double k = 0.8 * (double)h;
//            float l = player.isInSneakingPose() ? -0.1875f : 0.0f;
//            return ((EntityInterface) player).legacyAnimations$getCameraPosVec(tickDelta, eyeHeight).add(-e * j - d * k, (double)l - 0.45 * (double)h, -d * j + e * k);
//        }
//        double m = 960.0 / (double) this.dispatcher.gameOptions.getFov().getValue();
//        Vec3d vec3d = this.dispatcher.camera.getProjection().getPosition((float)i * 0.525f, -0.1F).multiply(m).rotateY(f * 0.5f).rotateX(-f * 0.7f).rotateZ(f * 0.5f).add(TransformHook.translationX, TransformHook.translationY, TransformHook.translationZ);
////        vec3d;
//
//        return ((EntityInterface) player).legacyAnimations$getCameraPosVec(tickDelta, eyeHeight).add(vec3d);
//    }

}
