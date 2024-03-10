package com.mixces.legacyanimations.util;

import com.mixces.legacyanimations.mixin.FoodComponentAccessor;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;

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

    public static Block getBlock(int x, int y, int z) {
        ClientWorld world = MinecraftClient.getInstance().world;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null && MinecraftClient.getInstance().world != null) {
            return world.getBlockState(new BlockPos(player.getBlockPos().getX() + x, player.getBlockPos().getY() + y, player.getBlockPos().getZ() + z)).getBlock();
        }
        return null;
    }

}
