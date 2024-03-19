package com.mixces.legacyanimations.util;

import com.mixces.legacyanimations.mixin.FoodComponentAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.UseAction;

public class ItemUtils {

    public static ItemUtils INSTANCE = new ItemUtils();

    public boolean isValidItem(ItemStack heldStack, UseAction action) {
        if (ItemUtils.INSTANCE.isValidHeldItem(heldStack) ||
                (action == UseAction.EAT && heldStack.getItem().getFoodComponent() != null &&
                        ((FoodComponentAccessor) heldStack.getItem().getFoodComponent()).getAlwaysEdible())) {
            return true;
        } else if (action != UseAction.EAT) {
            return action != UseAction.NONE;
        }
        return false;
    }

    public boolean isValidHeldItem(ItemStack heldStack) {
        return heldStack.getItem() instanceof SwordItem || heldStack.getItem() instanceof FishingRodItem;
    }

    public boolean isUsing(ClientPlayerEntity player) {
        return ServerUtils.INSTANCE.isOnHypixel() ? MinecraftClient.getInstance().options.useKey.isPressed() : player.isUsingItem();
    }

}
