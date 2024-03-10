package com.mixces.legacyanimations.mixin;

import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import com.mixces.legacyanimations.hook.EntityHook;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin {

    @Redirect(
            method = "renderArmorParts",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/RenderLayer;getArmorCutoutNoCull(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;"
            )
    )
    private RenderLayer useEntityLayerRenderer(Identifier texture) {
        if (LegacyAnimationsSettings.CONFIG.instance().armorTint) {
            return RenderLayer.getEntityCutoutNoCullZOffset(texture);
        }
        return RenderLayer.getArmorCutoutNoCull(texture);
    }

    @ModifyArg(
            method = "renderArmorParts",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V"
            ),
            index = 3
    )
    private int useDamageUVOverlay(int par3) {
        if (LegacyAnimationsSettings.CONFIG.instance().armorTint) {
            return OverlayTexture.packUv(OverlayTexture.getU(0.0F), OverlayTexture.getV(EntityHook.isEntityDying()));
        }
        return par3;
    }

}
