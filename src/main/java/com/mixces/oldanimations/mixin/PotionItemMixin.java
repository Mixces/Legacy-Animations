package com.mixces.oldanimations.mixin;

import com.mixces.oldanimations.config.OldAnimationsSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.PotionUtil;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PotionItem.class)
public class PotionItemMixin extends Item {

    public PotionItemMixin(Settings settings) {
        super(settings);
    }

    /**
     * Taken from OverlayTweaks :) <3
     */
    @Override
    public boolean hasGlint(ItemStack stack) {
        return OldAnimationsSettings.CONFIG.instance().potionGlint && !PotionUtil.getPotionEffects(stack).isEmpty();
    }

}
