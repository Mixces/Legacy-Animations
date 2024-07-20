package com.mixces.legacyanimations.mixin.interfaces;

import net.minecraft.client.render.model.BasicBakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BasicBakedModel.Builder.class)
public interface IBasicBakedModelMixin {

    @SuppressWarnings("unused")
    @Invoker("<init>")
    static BasicBakedModel.Builder Builder
            (boolean usesAo, boolean isSideLit, boolean hasDepth, ModelTransformation transformation, ModelOverrideList itemPropertyOverrides) {
        return null;
    }

}
