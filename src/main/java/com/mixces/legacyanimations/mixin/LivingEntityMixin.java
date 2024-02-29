package com.mixces.legacyanimations.mixin;

import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

//    @ModifyConstant(method = "isBlocking", constant = @Constant(intValue = 5))
//    private int isBlocking_fixSync(int constant) {
//        if (LegacyAnimationsSettings.CONFIG.instance().noCooldown) {
//            return 0;
//        }
//        return constant;
//    }

    @ModifyConstant(method = "tick", constant = @Constant(floatValue = 180.0f, ordinal = 0))
    private float revertBackwardsWalk(float constant) {
        if (LegacyAnimationsSettings.CONFIG.instance().oldWalking)
            return 0.0F;
        return constant;
    }

}
