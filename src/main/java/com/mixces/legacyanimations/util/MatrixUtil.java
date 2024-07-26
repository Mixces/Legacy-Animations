package com.mixces.legacyanimations.util;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

public class MatrixUtil {

    private final MatrixStack stack;

    public MatrixUtil (MatrixStack stack)
    {
        this.stack = stack;
    }

    public MatrixUtil pitch(float pitch)
    {
        stack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitch));
        return this;
    }

    public MatrixUtil yaw(float yaw)
    {
        stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw));
        return this;
    }

    public MatrixUtil roll(float roll)
    {
        stack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(roll));
        return this;
    }

}
