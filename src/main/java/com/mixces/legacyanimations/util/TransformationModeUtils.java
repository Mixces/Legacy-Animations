package com.mixces.legacyanimations.util;

import net.minecraft.client.render.model.json.ModelTransformationMode;

import java.util.EnumSet;

public class TransformationModeUtils {

    public static ModelTransformationMode transformationMode;
    private static final EnumSet<ModelTransformationMode> cameraTypes =
            EnumSet.of(
                    ModelTransformationMode.GROUND,
                    ModelTransformationMode.FIXED
            );

    public static boolean isValidPerspective(ModelTransformationMode mode) {
        return mode == ModelTransformationMode.FIRST_PERSON_LEFT_HAND ||
                mode == ModelTransformationMode.FIRST_PERSON_RIGHT_HAND ||
                mode == ModelTransformationMode.THIRD_PERSON_LEFT_HAND ||
                mode == ModelTransformationMode.THIRD_PERSON_RIGHT_HAND;
    }

    public static ModelTransformationMode getTransformationMode() {
        return transformationMode;
    }

    public static boolean shouldBeSprite() {
        return shouldNotHaveGlint() || isRenderingInGUI();
    }

    public static boolean isRenderingInGUI() {
        return transformationMode == ModelTransformationMode.GUI;
    }

    public static boolean shouldNotHaveGlint() {
        return cameraTypes.contains(transformationMode);
    }

}
