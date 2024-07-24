package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import com.mixces.legacyanimations.util.HandUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FlyingItemEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FlyingItemEntityRenderer.class)
public abstract class FlyingItemEntityRendererMixin<T extends Entity & FlyingItemEntity> extends EntityRenderer<T>
{

    protected FlyingItemEntityRendererMixin(EntityRendererFactory.Context ctx)
    {
        super(ctx);
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.GETFIELD,
                    target = "Lnet/minecraft/entity/Entity;age:I"
            )
    )
    public int legacyAnimations$disableDelay(Entity instance, Operation<Integer> original)
    {
        if (!LegacyAnimationsSettings.getInstance().oldProjectiles)
        {
            return original.call(instance);
        }
        return original.call(instance) + 2;
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/ItemRenderer;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;IILnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;I)V"
            )
    )
    public void legacyAnimations$shiftProjectile(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci)
    {
        if (!LegacyAnimationsSettings.getInstance().oldProjectiles)
        {
            return;
        }

        matrices.translate((!dispatcher.gameOptions.getPerspective().isFrontView() ? 1 : -1) * 0.25F, 0.0F, 0.0F);
    }

    //todo: shit is missing in left handed mode

    @ModifyArg(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/RotationAxis;rotationDegrees(F)Lorg/joml/Quaternionf;"
            ),
            index = 0
    )
    private float legacyAnimations$rotateProjectileAccordingly(float deg)
    {
        if (LegacyAnimationsSettings.getInstance().oldProjectiles)
        {
            return deg;
        }

        final ClientPlayerEntity player = MinecraftClient.getInstance().player;

        if (player == null)
        {
            return deg;
        }

        final boolean isLeftHand = HandUtils.INSTANCE.isLeftHand(MinecraftClient.getInstance().player, dispatcher);

        if (dispatcher.gameOptions.getPerspective().isFrontView())
        {
            return isLeftHand ? deg : deg - 180.0F;
        }
        else
        {
            return isLeftHand ? deg - 180.0F : deg;
        }
    }

}
