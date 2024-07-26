package com.mixces.legacyanimations.mixin;

import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PotionItem.class)
public class PotionItemMixin extends Item {

    public PotionItemMixin(Item.Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        if (LegacyAnimationsSettings.getInstance().oldPotionGlint)
        {
            final PotionContentsComponent potionContents = stack.get(DataComponentTypes.POTION_CONTENTS);

            if (potionContents == null)
            {
                return false;
            }

            return potionContents.hasEffects();
        }

        return super.hasGlint(stack);
    }

}
