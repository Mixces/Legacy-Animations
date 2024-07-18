package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class EntityMixin
{

    //todo: revert swimming mechanic (double jump to leave)

    @ModifyReturnValue(
            method = "getPose",
            at = @At(
                    value = "RETURN"
            )
    )
    public EntityPose legacyAnimations$revertSwimPose(EntityPose original)
    {
        if (!LegacyAnimationsSettings.CONFIG.instance().oldSwim)
        {
            return original;
        }

        final Entity entity = (Entity) (Object) this;

        if (entity.isSwimming())
        {
            return EntityPose.STANDING;
        }
        return original;
    }

}
