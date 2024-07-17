package com.mixces.legacyanimations.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.UseAction;

public class ItemUtils
{

    public static ItemUtils INSTANCE = new ItemUtils();

    public boolean isValidItem(ItemStack heldStack, UseAction action)
    {
        if (ItemUtils.INSTANCE.isValidHeldItem(heldStack) ||
                (action == UseAction.EAT && heldStack.get(DataComponentTypes.FOOD) != null &&
                        heldStack.get(DataComponentTypes.FOOD).canAlwaysEat()))
        {
            return true;
        }
        else if (action != UseAction.EAT)
        {
            return action != UseAction.NONE;
        }
        return false;
    }

    public boolean isValidHeldItem(ItemStack heldStack)
    {
        return heldStack.getItem() instanceof SwordItem || heldStack.getItem() instanceof FishingRodItem;
    }

    public boolean isUsing(ClientPlayerEntity player)
    {
        return ServerUtils.INSTANCE.isOnHypixel() ? MinecraftClient.getInstance().options.useKey.isPressed() : player.isUsingItem();
    }

}
