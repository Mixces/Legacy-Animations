package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import com.mixces.legacyanimations.duck.EntityInterface;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityInterface {

    //todo: revert swimming mechanic (double jump to leave)

    @Shadow
    public double prevX;

    @Shadow
    public double prevY;

    @Shadow
    public double prevZ;

    @Shadow
    public abstract double getX();

    @Shadow
    public abstract double getY();

    @Shadow
    public abstract double getZ();

    @Shadow
    public abstract boolean isSwimming();

    @ModifyReturnValue(
            method = "getPose",
            at = @At(
                    value = "RETURN"
            )
    )
    public EntityPose legacyAnimations$revertSwimPose(EntityPose original) {
        return LegacyAnimationsSettings.getInstance().oldSwim && isSwimming() ? EntityPose.STANDING : original;
    }

    @Override
    public Vec3d legacyAnimations$getCameraPosVec(float tickDelta, float eyeHeight) {
        final double d = MathHelper.lerp(tickDelta, prevX, getX());
        final double e = MathHelper.lerp(tickDelta, prevY, getY()) + (double) eyeHeight;
        final double f = MathHelper.lerp(tickDelta, prevZ, getZ());
        return new Vec3d(d, e, f);
    }
}
