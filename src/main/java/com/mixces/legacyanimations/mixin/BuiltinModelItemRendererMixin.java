package com.mixces.legacyanimations.mixin;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BuiltinModelItemRenderer.class)
public class BuiltinModelItemRendererMixin {

//    @Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
//    private void render_disableShield(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {
//        if (LegacyAnimationsSettings.CONFIG.instance().hideShields) {
//            if (
//                    mode == ModelTransformationMode.FIRST_PERSON_LEFT_HAND ||
//                            mode == ModelTransformationMode.FIRST_PERSON_RIGHT_HAND ||
//                            mode == ModelTransformationMode.THIRD_PERSON_LEFT_HAND ||
//                            mode == ModelTransformationMode.THIRD_PERSON_RIGHT_HAND
//            )
//            {
//                if (stack.isOf(Items.SHIELD)) {
//                    ci.cancel();
//                }
//            }
//        }
//    }

}
