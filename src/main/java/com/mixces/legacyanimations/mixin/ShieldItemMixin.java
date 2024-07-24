package com.mixces.legacyanimations.mixin;

import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import com.mixces.legacyanimations.util.ItemUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShieldItem.class)
public class ShieldItemMixin {

    @Inject(
        method = "getUseAction",
        at = @At(
                value = "HEAD"
        ),
        cancellable = true
    )
    public void legacyAnimations$getUseAction(ItemStack stack, CallbackInfoReturnable<UseAction> cir)
    {
        if (!LegacyAnimationsSettings.getInstance().oldSwordBlock)
        {
            return;
        }

        if (!ItemUtils.INSTANCE.isSwordInMainHand(null))
        {
            return;
        }

        cir.setReturnValue(UseAction.NONE);
    }

    @Inject(
            method = "getMaxUseTime",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void legacyAnimations$lowerMaxUseTime(ItemStack stack, CallbackInfoReturnable<Integer> cir)
    {
        if (!LegacyAnimationsSettings.getInstance().oldSwordBlock)
        {
            return;
        }

        if (!ItemUtils.INSTANCE.isSwordInMainHand(null))
        {
            return;
        }

        cir.setReturnValue(0);
    }

    @Inject(
            method = "use",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void disableUseWithSword(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir)
    {
        if (!LegacyAnimationsSettings.getInstance().oldSwordBlock)
        {
            return;
        }

        if (!ItemUtils.INSTANCE.isSwordInMainHand(null))
        {
            return;
        }

        cir.setReturnValue(TypedActionResult.pass(user.getStackInHand(hand)));
    }

}
