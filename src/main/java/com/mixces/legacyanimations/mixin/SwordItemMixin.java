package com.mixces.legacyanimations.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.UseAction;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SwordItem.class)
public class SwordItemMixin extends Item {

    public SwordItemMixin(Settings settings) {
        super(settings);
    }

//    @Override
//    public UseAction getUseAction(ItemStack stack) {
//        ClientPlayerEntity player = MinecraftClient.getInstance().player;
//        assert player != null;
//        if ((LegacyAnimationsSettings.CONFIG.instance().oldSwordBlock && !LegacyAnimationsSettings.CONFIG.instance().blockWithShieldOnly) ||
//                (LegacyAnimationsSettings.CONFIG.instance().oldSwordBlock && player.getOffHandStack().isOf(Items.SHIELD)))
//            return UseAction.BLOCK;
//        return UseAction.NONE;
//    }
//
//    @Override
//    public int getMaxUseTime(ItemStack stack) {
//        if (LegacyAnimationsSettings.CONFIG.instance().oldSwordBlock)
//            return 72000;
//        return 0;
//    }
//
//    @Override
//    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
//        ClientPlayerEntity player = MinecraftClient.getInstance().player;
//        assert player != null;
//        if ((LegacyAnimationsSettings.CONFIG.instance().oldSwordBlock && !LegacyAnimationsSettings.CONFIG.instance().blockWithShieldOnly) ||
//                (LegacyAnimationsSettings.CONFIG.instance().oldSwordBlock && player.getOffHandStack().isOf(Items.SHIELD))) {
//            ItemStack itemStack = user.getStackInHand(hand);
//            user.setCurrentHand(hand);
//            return TypedActionResult.consume(itemStack);
//        }
//        return TypedActionResult.pass(user.getStackInHand(hand));
//    }

}
