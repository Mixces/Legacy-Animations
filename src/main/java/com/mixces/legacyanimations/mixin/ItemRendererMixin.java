package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import com.mixces.legacyanimations.util.HandUtils;
import com.mixces.legacyanimations.util.ItemUtils;
import com.mixces.legacyanimations.util.TransformationModeUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.render.model.json.Transformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin
{

    @Shadow @Final private MinecraftClient client;

    @Inject(
            method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
            at = @At(
                    value = "HEAD"
            )
    )
    private void legacyAnimations$getTransformationMode(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci)
    {
        TransformationModeUtils.setTransformationMode(renderMode);
        ItemUtils.INSTANCE.setModel(model);
    }

    @Inject(
            method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/model/json/Transformation;apply(ZLnet/minecraft/client/util/math/MatrixStack;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void legacyAnimations$modelTransforms(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci, @Local(ordinal = 1, index = 9) boolean bl)
    {
        if (!LegacyAnimationsSettings.CONFIG.instance().itemPositions)
        {
            return;
        }

        if (bl)
        {
            return;
        }

        if (ItemUtils.INSTANCE.shouldRotateAroundWhenRendering(stack,true))
        {
            if (renderMode == ModelTransformationMode.FIRST_PERSON_LEFT_HAND || renderMode == ModelTransformationMode.FIRST_PERSON_RIGHT_HAND)
            {
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(50.0F));

                //todo: add more translations
            }
        }
    }

    //todo: unfuck left handed sprites and re-write code
    @ModifyArg(method = "renderBakedItemModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderBakedItemQuads(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Ljava/util/List;Lnet/minecraft/item/ItemStack;II)V", ordinal = 1), index = 2)
    private List<BakedQuad> legacyAnimations$changeToSprite(List<BakedQuad> quads, @Local(ordinal = 0, argsOnly = true) BakedModel model)
    {
        if (!LegacyAnimationsSettings.CONFIG.instance().fastItems) {
            return quads;
        }

        if (client.player == null)
        {
            return quads;
        }

        if (!TransformationModeUtils.shouldBeSprite() || model.hasDepth())
        {
            return quads;
        }

        final boolean isLeftHand = HandUtils.INSTANCE.isLeftHand(client.player, client.getEntityRenderDispatcher());
        final boolean isFrontView = client.getEntityRenderDispatcher().gameOptions.getPerspective().isFrontView();
        final Direction perspectiveFace = legacyAnimations$determineDirection(isFrontView, isLeftHand);

        if (TransformationModeUtils.getTransformationMode() == ModelTransformationMode.GROUND)
        {
            return legacyAnimations$filterQuadsByDirection(quads, perspectiveFace);
        }
        return legacyAnimations$filterQuadsByDirection(quads, Direction.SOUTH);
    }

    @Unique
    private static Direction legacyAnimations$determineDirection(boolean isFrontView, boolean isLeftHand)
    {
        if (isFrontView)
        {
            return isLeftHand ? Direction.SOUTH : Direction.NORTH;
        }
        return isLeftHand ? Direction.NORTH : Direction.SOUTH;
    }

    @Unique
    private static List<BakedQuad> legacyAnimations$filterQuadsByDirection(List<BakedQuad> quads, Direction face)
    {
        return quads.stream().filter(baked -> baked.getFace() == face).collect(Collectors.toList());
    }

}
