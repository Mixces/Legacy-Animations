package com.mixces.legacyanimations.util;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.*;
import net.minecraft.util.UseAction;

@Setter
@Getter
public class ItemUtils
{

    public static ItemUtils INSTANCE = new ItemUtils();

    //todo: move this
    private BakedModel model;

    /**
     * In order to smartly hide the shield, we need to check which items are capable of rendering the shield useless.
     * Items that are apply are swords, rods, and food.
     *
     * @return Returns whether the main hand item can be used, and if so, it is a valid item.
     */
    public boolean isValidItem(ItemStack heldStack, UseAction action)
    {
        if (!ItemUtils.INSTANCE.isValidHeldItem())
        {
            return false;
        }

        final boolean isEating = action == UseAction.EAT;
        final FoodComponent foodItem = heldStack.get(DataComponentTypes.FOOD);

        if (foodItem == null)
        {
            return false;
        }

        if (isEating && foodItem.canAlwaysEat())
        {
            return true;
        }
        else if (!isEating)
        {
            return action != UseAction.NONE;
        }
        return false;
    }

    /**
     * Since player's can't use a shield while holding a fishing rod, why not include it in this check?
     *
     * @return Returns whether the main hand item is a sword, a fishing rod, or a fishing rod variant.
     */
    public boolean isValidHeldItem()
    {
        return isSwordInMainHand(null) || shouldRotateAroundWhenRendering(null,false);
    }

    /**
     * Are you a sword!?
     *
     * @param stack     We need the currently held item!
     * @return          Returns whether the main hand item is a sword or not.
     */
    public boolean isSwordInMainHand(ItemStack stack) {
        final ClientPlayerEntity player = MinecraftClient.getInstance().player;

        if (player == null)
        {
            return false;
        }

        final Item item = stack == null ? player.getMainHandStack().getItem() : stack.getItem();

        if (item == null)
        {
            return false;
        }

        return item instanceof SwordItem;
    }

    /**
     * This name of this method should be a throwback!
     *
     * @param stack         We need the currently held item!
     * @param allowVariant  If carrot on a stick or warped fungus on a stick should be included.
     * @return              Returns whether the main hand item is a fishing rod or a variant of one.
     */
    public boolean shouldRotateAroundWhenRendering(ItemStack stack, boolean allowVariant) {
        final ClientPlayerEntity player = MinecraftClient.getInstance().player;

        if (player == null)
        {
            return false;
        }

        final Item item = stack == null ? player.getMainHandStack().getItem() : stack.getItem();

        if (item == null)
        {
            return false;
        }

        return item instanceof FishingRodItem || (item instanceof OnAStickItem && allowVariant);
    }

    /**
     * Shields?!
     *
     * @param stack     We need the currently held item!
     * @return          Returns whether the offhand item is a shield or not.
     */
    public boolean isShieldInOffHand(ItemStack stack) {
        final ClientPlayerEntity player = MinecraftClient.getInstance().player;

        if (player == null)
        {
            return false;
        }

        final Item item = stack == null ? player.getOffHandStack().getItem() : stack.getItem();

        if (item == null)
        {
            return false;
        }

        return item instanceof ShieldItem;
    }

    /**
     * Hypixel parity.
     *
     * @return If the player is on Hypixel, let the blocking animation play when right-clicking rather than while the item is in usage.
     *         This visually bypasses Hypixel's buggy shield.
     */
    public boolean isUsing(ClientPlayerEntity player)
    {
        if (ServerUtils.INSTANCE.isOnHypixel())
        {
            return MinecraftClient.getInstance().options.useKey.isPressed();
        }
        return player.isUsingItem();
    }

}
