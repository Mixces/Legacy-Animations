package com.mixces.legacyanimations.mixin;

import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import com.mixces.legacyanimations.duck.PlayerPitchInterface;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.UseAction;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements PlayerPitchInterface
{

    @Shadow public abstract boolean isUsingItem();
    @Shadow protected ItemStack activeItemStack;
    @Unique public float legacyAnimations$prevCameraPitch;
    @Unique public float legacyAnimations$cameraPitch;

    //todo: hypixel rahh
    @Inject(
            method = "isBlocking",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void legacyAnimations$fixSync(CallbackInfoReturnable<Boolean> cir)
    {
//        if (!ServerUtils.INSTANCE.isValidServer())
//        {
//            return;
//        }

        //todo: shield delay?

        final UseAction action = activeItemStack.getItem().getUseAction(activeItemStack);

        cir.setReturnValue(isUsingItem() && action == UseAction.BLOCK);
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
    private float legacyAnimations$revertBackwardsWalk(float constant)
    {
        if (!LegacyAnimationsSettings.CONFIG.instance().oldWalking)
        {
            return constant;
        }
        return 0.0F;
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
    private void legacyAnimations$setPrevCameraPitch(CallbackInfo ci)
    {
        if (!LegacyAnimationsSettings.CONFIG.instance().oldViewBob)
        {
            return;
        }
        legacyAnimations$prevCameraPitch = legacyAnimations$cameraPitch;
    }

    @Override
    public float legacyAnimations$getPrevPlayerPitch()
    {
        return legacyAnimations$prevCameraPitch;
    }

    @Override
    public float legacyAnimations$getPlayerPitch()
    {
        return legacyAnimations$cameraPitch;
    }

}
