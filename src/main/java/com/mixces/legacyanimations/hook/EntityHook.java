package com.mixces.legacyanimations.hook;

import net.minecraft.entity.LivingEntity;

public class EntityHook {

    public static LivingEntity entity;

    public static boolean isEntityDying() {
        return entity.deathTime > 0 || entity.hurtTime > 0;
    }

}
