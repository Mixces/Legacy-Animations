package com.mixces.legacyanimations.util;

import net.minecraft.block.SkullBlock;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ItemBlacklist {

    // Map to store blacklisted items
    // Some items are not quite compatible with the generic item position (crossbows, banners, skull items, etc)
    private static final Map<Class<?>, Boolean> blacklistedItems = new HashMap<>() {{
        put(SkullBlock.class, true);
        put(BannerItem.class, true);
    }};

    // Method to check if an item is blacklisted
    public static boolean isPresent(ItemStack stack) {
        return blacklistedItems.containsKey(stack.getItem().getClass());
    }
}