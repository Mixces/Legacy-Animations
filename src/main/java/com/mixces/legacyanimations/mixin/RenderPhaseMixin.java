package com.mixces.legacyanimations.mixin;

import net.minecraft.client.render.RenderPhase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPhase.class)
public class RenderPhaseMixin
{

    @Inject(method = "setupGlintTexturing", at = @At(value = "HEAD"))
    private static void legacyAnimations$injectGlint(float scale, CallbackInfo ci)
    {
//        System.out.println("glint rendering");
    }

}
