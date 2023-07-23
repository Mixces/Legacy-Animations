package dev.microcontrollers.nametagtweaks.mixin;

import dev.microcontrollers.nametagtweaks.config.NametagTweaksConfig;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.entity.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @ModifyArg(method = "renderLabelIfPresent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/text/Text;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/font/TextRenderer$TextLayerType;II)I"), index = 4)
    public boolean addNametagShadow(boolean shadow) {
        return NametagTweaksConfig.INSTANCE.getConfig().nametagTextShadow;
    }

    @Redirect(method = "renderLabelIfPresent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;getTextBackgroundOpacity(F)F"))
    public float setNametagBackground(GameOptions instance, float fallback) {
        return NametagTweaksConfig.INSTANCE.getConfig().nametagOpacity / 100F;
    }
}
