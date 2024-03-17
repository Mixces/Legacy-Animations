package com.mixces.legacyanimations.mixin;

import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import com.mixces.legacyanimations.duck.PlayerPitchInterface;
import com.mixces.legacyanimations.util.ServerUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements PlayerPitchInterface {

    @Shadow public abstract boolean isBlocking();

    @Shadow public abstract ItemStack getMainHandStack();

    @Shadow public abstract boolean isUsingItem();

    @Unique public float legacyAnimations$prevCameraPitch;
    @Unique public float legacyAnimations$cameraPitch;

    @ModifyConstant(method = "isBlocking", constant = @Constant(intValue = 5))
    private int isBlocking_fixSync(int constant) {
        if (ServerUtils.INSTANCE.isOnHypixel()) {
            return 0;
        }
        return constant;
    }

    @ModifyConstant(
            method = "tick",
            constant = @Constant(
                    floatValue = 180.0f
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/util/math/MathHelper;abs(F)F"
                    ),
                    to = @At(
                            value = "FIELD",
                            opcode = Opcodes.GETFIELD,
                            target = "Lnet/minecraft/entity/LivingEntity;handSwingProgress:F"
                    )
            )
    )
    private float revertBackwardsWalk(float constant) {
        if (LegacyAnimationsSettings.CONFIG.instance().oldWalking)
            return 0.0F;
        return constant;
    }

    @Inject(
            method = "baseTick",
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.GETFIELD,
                    target = "Lnet/minecraft/entity/LivingEntity;hurtTime:I",
                    ordinal = 0
            )
    )
    private void setPrevCameraPitch(CallbackInfo ci) {
        if (LegacyAnimationsSettings.CONFIG.instance().oldViewBob) {
            legacyAnimations$prevCameraPitch = legacyAnimations$cameraPitch;
        }
    }

    @Override
    public float legacyAnimations$getPrevPlayerPitch() {
        return legacyAnimations$prevCameraPitch;
    }

    @Override
    public float legacyAnimations$getPlayerPitch() {
        return legacyAnimations$cameraPitch;
    }

}
