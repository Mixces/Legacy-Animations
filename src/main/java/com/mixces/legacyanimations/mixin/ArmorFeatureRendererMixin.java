package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin<T extends LivingEntity, A extends BipedEntityModel<T>> {

    @Unique
    private T legacyAnimations$entity;

    @Inject(
            method = "renderArmor",
            at = @At(
                    value = "HEAD"
            )
    )
    private void legacyAnimations$captureEntity(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, EquipmentSlot armorSlot, int light, A model, CallbackInfo ci) {
        legacyAnimations$entity = entity;
    }

    @ModifyArg(
            method = "renderArmorParts",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/VertexConsumerProvider;getBuffer(Lnet/minecraft/client/render/RenderLayer;)Lnet/minecraft/client/render/VertexConsumer;"
            )
    )
    private RenderLayer legacyAnimations$useEntityLayerRendererArmor(RenderLayer var1, @Local(ordinal = 0, argsOnly = true) Identifier overlay) {
        return LegacyAnimationsSettings.getInstance().armorTint ? RenderLayer.getEntityCutoutNoCullZOffset(overlay) : var1;
    }

    @WrapOperation(
            method = "renderTrim",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/texture/Sprite;getTextureSpecificVertexConsumer(Lnet/minecraft/client/render/VertexConsumer;)Lnet/minecraft/client/render/VertexConsumer;"
            )
    )
    private VertexConsumer legacyAnimations$useEntityLayerRendererTrim(Sprite instance, VertexConsumer consumer, Operation<VertexConsumer> original, @Local(ordinal = 0, argsOnly = true) VertexConsumerProvider vertexConsumers) {
        if (LegacyAnimationsSettings.getInstance().armorTint && (legacyAnimations$entity.deathTime > 0 || legacyAnimations$entity.hurtTime > 0)) {
            return instance.getTextureSpecificVertexConsumer(vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCullZOffset(instance.getAtlasId())));
        }
        return original.call(instance, consumer);
    }

    @ModifyArg(
            method = {
                    "renderArmorParts",
                    "renderTrim"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V"
            ),
            index = 3
    )
    private int legacyAnimations$useDamageUVOverlay(int par3) {
        if (LegacyAnimationsSettings.getInstance().armorTint) {
            return OverlayTexture.packUv(
                    OverlayTexture.getU(0.0F),
                    OverlayTexture.getV(legacyAnimations$entity.deathTime > 0 || legacyAnimations$entity.hurtTime > 0)
            );
        }
        return par3;
    }
}
