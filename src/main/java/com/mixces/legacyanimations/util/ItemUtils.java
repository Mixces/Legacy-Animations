package com.mixces.legacyanimations.util;

import com.mixces.legacyanimations.mixin.FoodComponentAccessor;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.UseAction;

public class ItemUtils {

    public static boolean isValidItem(ItemStack heldStack, UseAction action) {
        if (isValidHeldItem(heldStack) ||
                (action == UseAction.EAT && heldStack.getItem().getFoodComponent() != null &&
                        ((FoodComponentAccessor) heldStack.getItem().getFoodComponent()).getAlwaysEdible())) {
            return true;
        } else if (action != UseAction.EAT) {
            return action != UseAction.NONE;
        }
        return false;
    }

    public static boolean isValidHeldItem(ItemStack heldStack) {
        return heldStack.getItem() instanceof SwordItem || heldStack.getItem() instanceof FishingRodItem;
    }

}
