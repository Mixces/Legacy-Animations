package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import com.mixces.legacyanimations.util.ItemUtils;
import com.mixces.legacyanimations.util.TransformationModeUtils;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BuiltinModelItemRenderer.class)
public abstract class BuiltinModelItemRendererMixin
{

    //todo: re-write shields
//    @ModifyExpressionValue(
//            method = "render",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
//                    ordinal = 0
//            )
//    )
//    private boolean legacyAnimations$disableShieldRendering(boolean original, ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay)
//    {
//        if (LegacyAnimationsSettings.CONFIG.instance().hideShields && TransformationModeUtils.isValidPerspective(mode))
//        {
//            return original;
//        }
//        return !ItemUtils.INSTANCE.isValidItem(stack, stack.getUseAction());
//    }

}
