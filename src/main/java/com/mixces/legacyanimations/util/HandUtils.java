package com.mixces.legacyanimations.util;

import com.google.common.base.MoreObjects;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;

//todo: simplify
public class HandUtils
{

    public static HandUtils INSTANCE = new HandUtils();

    /**
     * Hands.
     *
     * @param player        Player instance.
     * @param dispatcher    Used to help determine the perspective of the player!
     * @return              Returns the multiplier for which we can use to achieve perfect symmetry between hands!
     */
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

    /**
     * My weaker hand.
     *
     * @param player        Player instance.
     * @param dispatcher    Used to help determine the perspective of the player!
     * @return              Returns whether the player is left-handed or not!
     */
    public boolean isLeftHand(ClientPlayerEntity player, EntityRenderDispatcher dispatcher)
    {
        return handMultiplier(player, dispatcher) == -1;
    }

    /**
     * Is this my right hand?!
     *
     * @param player        Player instance.
     * @param dispatcher    Used to help determine the perspective of the player!
     * @return              Returns whether the player is right-handed or not!
     */
    public boolean isRightHand(ClientPlayerEntity player, EntityRenderDispatcher dispatcher)
    {
        return handMultiplier(player, dispatcher) == 1;
    }

}
