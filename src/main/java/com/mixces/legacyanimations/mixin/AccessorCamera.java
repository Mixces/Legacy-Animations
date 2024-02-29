package com.mixces.legacyanimations.mixin;

import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Camera.class)
public interface AccessorCamera {

    @Accessor float getCameraY();

    @Accessor float getLastCameraY();

}
