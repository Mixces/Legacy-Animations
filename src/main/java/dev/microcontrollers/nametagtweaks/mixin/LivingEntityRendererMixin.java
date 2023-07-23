package dev.microcontrollers.nametagtweaks.mixin;

import dev.microcontrollers.nametagtweaks.config.NametagTweaksConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/*
  This is taken from Easeify under LGPLV3
  https://github.com/Polyfrost/Easeify/blob/main/LICENSE
 */
@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    // TODO: Figure out if this can be done with @ModifyReturnVariable for better compatibility
    @Inject(method = "hasLabel(Lnet/minecraft/entity/LivingEntity;)Z", at = @At(value = "TAIL", shift = At.Shift.BEFORE), cancellable = true)
    public void showInThirdPerson(LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(
                (MinecraftClient.isHudEnabled() || NametagTweaksConfig.INSTANCE.getConfig().showNametagsInHiddenHud) && (NametagTweaksConfig.INSTANCE.getConfig().showOwnNametags || livingEntity != MinecraftClient.getInstance().getCameraEntity()) && !livingEntity.isInvisibleTo(MinecraftClient.getInstance().player) && !livingEntity.hasPassengers()
        );
    }
}
