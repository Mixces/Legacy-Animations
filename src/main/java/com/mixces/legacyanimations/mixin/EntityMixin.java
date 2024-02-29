package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixin {

    @Final @Shadow private EntityType<?> type;
    @Final @Shadow protected DataTracker dataTracker;
    @Final @Shadow protected static TrackedData<EntityPose> POSE;

    @ModifyReturnValue(method = "getPose", at = @At("RETURN"))
    public EntityPose revertSwimPose(EntityPose original) {
        if (LegacyAnimationsSettings.CONFIG.instance().oldSwim && type == EntityType.PLAYER) {
            EntityPose pose = dataTracker.get(POSE);
            if (pose == EntityPose.SWIMMING) {
                pose = EntityPose.STANDING;
            }
            return pose;
        }
        return original;
    }

}
