package com.mixces.legacyanimations.mixin;

import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShieldItem.class)
public class ShieldItemMixin extends Item {

    public ShieldItemMixin(Settings settings) {
        super(settings);
    }

//    @Inject(
//            method = "getUseAction",
//            at = @At(
//                    value = "HEAD"
//            ),
//            cancellable = true
//    )
//    public void getUseAction(ItemStack stack, CallbackInfoReturnable<UseAction> cir) {
//        ClientPlayerEntity player = MinecraftClient.getInstance().player;
//        if (LegacyAnimationsSettings.CONFIG.instance().oldSwordBlock && player != null && player.getMainHandStack().getItem() instanceof SwordItem) {
//            cir.setReturnValue(UseAction.NONE);
//        }
//    }
//
//    @Inject(
//            method = "getMaxUseTime",
//            at = @At(
//                    value = "HEAD"
//            ),
//            cancellable = true
//    )
//    public void lowerMaxUseTime(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
//        ClientPlayerEntity player = MinecraftClient.getInstance().player;
//        if (LegacyAnimationsSettings.CONFIG.instance().oldSwordBlock && player != null && player.getMainHandStack().getItem() instanceof SwordItem) {
//            cir.setReturnValue(0);
//        }
//    }
//
//    @Inject(
//            method = "use",
//            at = @At(
//                    value = "HEAD"
//            ),
//            cancellable = true
//    )
//    public void disableUseWithSword(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
//        ClientPlayerEntity player = MinecraftClient.getInstance().player;
//        if (LegacyAnimationsSettings.CONFIG.instance().oldSwordBlock && player != null && player.getMainHandStack().getItem() instanceof SwordItem) {
//            cir.setReturnValue(TypedActionResult.pass(user.getStackInHand(hand)));
//        }
//    }

}
