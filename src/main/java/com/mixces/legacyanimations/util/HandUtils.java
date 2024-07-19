package com.mixces.legacyanimations.util;

import com.google.common.base.MoreObjects;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;

public class HandUtils
{

    //todo: simplify
    public static HandUtils INSTANCE = new HandUtils();

    public int handMultiplier(ClientPlayerEntity player, EntityRenderDispatcher dispatcher)
    {
        final Hand hand = MoreObjects.firstNonNull(player.preferredHand, Hand.MAIN_HAND);
        final boolean bl = hand == Hand.MAIN_HAND;
        final Arm arm = bl ? player.getMainArm() : player.getMainArm().getOpposite();
        final boolean bl2 = arm == Arm.RIGHT;
        final boolean isFirstPerson = dispatcher.gameOptions.getPerspective().isFirstPerson();
        final int perspectiveMultiplier = isFirstPerson ? 1 : -1;

        return bl2 ? perspectiveMultiplier : -perspectiveMultiplier;
    }

    public boolean isLeftHand(ClientPlayerEntity player, EntityRenderDispatcher dispatcher)
    {
        return handMultiplier(player, dispatcher) == -1;
    }

    public boolean isRightHand(ClientPlayerEntity player, EntityRenderDispatcher dispatcher)
    {
        return handMultiplier(player, dispatcher) == 1;
    }

}
