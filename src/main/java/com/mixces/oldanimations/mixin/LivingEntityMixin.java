package com.mixces.oldanimations.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.UseAction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow public abstract boolean isUsingItem();
    @Shadow public abstract ItemStack getActiveItem();

    @Inject(method = "isBlocking", at = @At("HEAD"), cancellable = true)
    private void isBlocking_fixSync(CallbackInfoReturnable<Boolean> cir) {
        if (isUsingItem() && getActiveItem().getItem().getUseAction(getActiveItem()) == UseAction.BLOCK)
            cir.setReturnValue(true);
    }

    @ModifyConstant(method = "tick", constant = @Constant(floatValue = 180.0f, ordinal = 0))
    private float tick_revertWalk(float constant) {
        return 0.0F;
    }

}
