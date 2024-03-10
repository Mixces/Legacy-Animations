package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import com.mixces.legacyanimations.util.HandUtils;
import com.mixces.legacyanimations.util.TransformationModeUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @Shadow @Final private MinecraftClient client;

    @ModifyArg(method = "renderBakedItemModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderBakedItemQuads(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Ljava/util/List;Lnet/minecraft/item/ItemStack;II)V", ordinal = 1), index = 2)
    private List<BakedQuad> changeToSprite(List<BakedQuad> quads, @Local(ordinal = 0) BakedModel model) {
        if (LegacyAnimationsSettings.CONFIG.instance().fastItems && client.player != null && TransformationModeUtils.shouldBeSprite() && !model.hasDepth()) {
            boolean isLeftHand = HandUtils.isLeftHand(client.player, client.getEntityRenderDispatcher());
            boolean isFrontView = client.getEntityRenderDispatcher().gameOptions.getPerspective().isFrontView();
            Direction perspectiveFace = legacyAnimations$determineDirection(isFrontView, isLeftHand);
            if (TransformationModeUtils.getTransformationMode() == ModelTransformationMode.GROUND) {
                return legacyAnimations$filterQuadsByDirection(quads, perspectiveFace);
            }
            return legacyAnimations$filterQuadsByDirection(quads, Direction.SOUTH);
        }
        return quads;
    }

    @Inject(
            method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
            at = @At(
                    value = "HEAD"
            )
    )
    private void getTransformationMode(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
        if (LegacyAnimationsSettings.CONFIG.instance().fastItems) {
            TransformationModeUtils.transformationMode = renderMode;
        }
    }

    @Unique
    private static Direction legacyAnimations$determineDirection(boolean isFrontView, boolean isLeftHand) {
        if (isFrontView) {
            return isLeftHand ? Direction.SOUTH : Direction.NORTH;
        }
        return isLeftHand ? Direction.NORTH : Direction.SOUTH;
    }

    @Unique
    private static List<BakedQuad> legacyAnimations$filterQuadsByDirection(List<BakedQuad> quads, Direction face) {
        return quads.stream().filter(baked -> baked.getFace() == face).collect(Collectors.toList());
    }

}
