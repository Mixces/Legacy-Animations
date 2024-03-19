package com.mixces.legacyanimations.util;

import com.google.common.base.MoreObjects;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;

public class HandUtils {

    public static HandUtils INSTANCE = new HandUtils();

    public int handMultiplier(ClientPlayerEntity player, EntityRenderDispatcher dispatcher) {
        Hand hand = MoreObjects.firstNonNull(player.preferredHand, Hand.MAIN_HAND);
        boolean bl = hand == Hand.MAIN_HAND;
        Arm arm = bl ? player.getMainArm() : player.getMainArm().getOpposite();
        boolean bl2 = arm == Arm.RIGHT;
        boolean isFirstPerson = dispatcher.gameOptions.getPerspective().isFirstPerson();
        int perspectiveMultiplier = isFirstPerson ? 1 : -1;
        return bl2 ? perspectiveMultiplier : -perspectiveMultiplier;
    }

    public boolean isLeftHand(ClientPlayerEntity player, EntityRenderDispatcher dispatcher) {
        return handMultiplier(player, dispatcher) == -1;
    }

    public boolean isRightHand(ClientPlayerEntity player, EntityRenderDispatcher dispatcher) {
        return handMultiplier(player, dispatcher) == 1;
    }

}
