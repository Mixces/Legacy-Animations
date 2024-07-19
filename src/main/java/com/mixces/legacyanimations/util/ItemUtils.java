package com.mixces.legacyanimations.util;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.UseAction;

@Setter
@Getter
public class ItemUtils
{

    public static ItemUtils INSTANCE = new ItemUtils();

    private BakedModel model;

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

    public boolean isSwordInMainHand() {
        final ClientPlayerEntity player = MinecraftClient.getInstance().player;

        if (player == null)
        {
            return false;
        }

        return player.getMainHandStack().getItem() instanceof SwordItem;
    }

    public boolean isShieldInOffHand() {
        final ClientPlayerEntity player = MinecraftClient.getInstance().player;

        if (player == null)
        {
            return false;
        }

        return player.getOffHandStack().getItem() instanceof ShieldItem;
    }

    public boolean isUsing(ClientPlayerEntity player)
    {
        if (ServerUtils.INSTANCE.isOnHypixel())
        {
            return MinecraftClient.getInstance().options.useKey.isPressed();
        }
        return player.isUsingItem();
    }

}
