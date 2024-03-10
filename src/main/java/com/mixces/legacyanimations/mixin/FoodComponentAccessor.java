package com.mixces.legacyanimations.mixin;

import net.minecraft.item.FoodComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FoodComponent.class)
public interface FoodComponentAccessor {

    @Accessor boolean getAlwaysEdible();

}