package com.mixces.legacyanimations.mixin;

import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import com.mixces.legacyanimations.util.ItemUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SwordItem.class)
public class SwordItemMixin extends Item {

    public SwordItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        if (LegacyAnimationsSettings.getInstance().oldSwordBlock && ItemUtils.INSTANCE.isShieldInOffHand(null)) {
            return UseAction.BLOCK;
        }
        return UseAction.NONE;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        if (LegacyAnimationsSettings.getInstance().oldSwordBlock && ItemUtils.INSTANCE.isShieldInOffHand(null)) {
            return 72000;
        }
        return 0;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (LegacyAnimationsSettings.getInstance().oldSwordBlock && ItemUtils.INSTANCE.isShieldInOffHand(null)) {
            ItemStack itemStack = user.getStackInHand(hand);
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }
}
