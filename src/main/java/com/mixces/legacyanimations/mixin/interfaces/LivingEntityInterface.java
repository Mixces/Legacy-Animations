package com.mixces.legacyanimations.mixin.interfaces;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityInterface
{

    @Invoker int invokeGetHandSwingDuration();

    @Invoker float invokeGetHealth();

}
