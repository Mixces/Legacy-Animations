package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import com.mixces.legacyanimations.util.ItemUtils;
import com.mixces.legacyanimations.util.TransformationModeUtils;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.util.math.Vec3i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(VertexConsumer.class)
public interface VertexConsumerMixin {

    //todo: maybe there's a better way?
    @ModifyArgs(
            method = "quad(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/model/BakedQuad;[FFFFF[IIZ)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack$Entry;transformNormal(FFFLorg/joml/Vector3f;)Lorg/joml/Vector3f;"
            )
    )
    default void legacyAnimations$modifyNormal(Args args, @Local(ordinal = 0) Vec3i vec3i)
    {
        if (!LegacyAnimationsSettings.CONFIG.instance().fastItems)
        {
            return;
        }

        if (TransformationModeUtils.getTransformationMode() != ModelTransformationMode.GROUND)
        {
            return;
        }

        if (ItemUtils.INSTANCE.getModel().hasDepth())
        {
            return;
        }

        args.set(1, (float) args.get(2));
        args.set(2, (float) args.get(1));
    }

}
