package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import com.mixces.legacyanimations.duck.PlayerPitchInterface;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin
{

    @Inject(
            method = "bobView",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionf;)V",
                    ordinal = 1,
                    shift = At.Shift.AFTER
            )
    )
    private void legacyAnimations$addOldPitchRotation(MatrixStack matrices, float tickDelta, CallbackInfo ci, @Local(ordinal = 0) PlayerEntity playerEntity)
    {
        if (!LegacyAnimationsSettings.getInstance().oldViewBob)
        {
            return;
        }

        final float prevPlayerPitch = ((PlayerPitchInterface) playerEntity).legacyAnimations$getPrevPlayerPitch();
        final float playerPitch = ((PlayerPitchInterface) playerEntity).legacyAnimations$getPlayerPitch();
        final float h = MathHelper.lerp(tickDelta, prevPlayerPitch, playerPitch);

        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(h));
    }

}
