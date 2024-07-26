package com.mixces.legacyanimations.handler;

import com.mixces.legacyanimations.mixin.access.ILivingEntityMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityAnimationS2CPacket;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;

public class FakeSwingHandler {

//    public static FakeSwingHandler INSTANCE = new FakeSwingHandler();
//
//    public boolean handSwinging;
//    public Hand preferredHand;
//    public int handSwingTicks;
//    public float lastHandSwingProgress;
//    public float handSwingProgress;
//
//    public void swingHand2(ClientPlayerEntity player, Hand hand) {
//        this.swingHand(player, hand);
//
//        if (MinecraftClient.getInstance().getNetworkHandler() == null)
//        {
//            return;
//        }
//
//        MinecraftClient.getInstance().getNetworkHandler().sendPacket(new HandSwingC2SPacket(hand));
//    }
//
//    public void swingHand(ClientPlayerEntity player, Hand hand) {
//        this.swingHand(player, hand, false);
//    }
//
//    public void swingHand(ClientPlayerEntity player, Hand hand, boolean fromServerPlayer) {
//        final int handSwingDuration = ((ILivingEntityMixin) player).invokeGetHandSwingDuration();
//
//        if (!this.handSwinging || this.handSwingTicks >= handSwingDuration / 2 || this.handSwingTicks < 0) {
//            this.handSwingTicks = -1;
//            this.handSwinging = true;
//            this.preferredHand = hand;
//            if (MinecraftClient.getInstance().g instanceof ServerWorld) {
//                EntityAnimationS2CPacket entityAnimationS2CPacket = new EntityAnimationS2CPacket(this, hand == Hand.MAIN_HAND ? EntityAnimationS2CPacket.SWING_MAIN_HAND : EntityAnimationS2CPacket.SWING_OFF_HAND);
//                ServerChunkManager serverChunkManager = ((ServerWorld)this.getWorld()).getChunkManager();
//                if (fromServerPlayer) {
//                    serverChunkManager.sendToNearbyPlayers(this, entityAnimationS2CPacket);
//                } else {
//                    serverChunkManager.sendToOtherNearbyPlayers(this, entityAnimationS2CPacket);
//                }
//            }
//        }
//    }
//
//    protected void tickHandSwing(ClientPlayerEntity player) {
//        int i = ((ILivingEntityMixin) player).invokeGetHandSwingDuration();
//        if (this.handSwinging) {
//            ++this.handSwingTicks;
//            if (this.handSwingTicks >= i) {
//                this.handSwingTicks = 0;
//                this.handSwinging = false;
//            }
//        } else {
//            this.handSwingTicks = 0;
//        }
//        this.handSwingProgress = (float)this.handSwingTicks / (float)i;
//    }
//
//    public float getHandSwingProgress(float tickDelta) {
//        float f = this.handSwingProgress - this.lastHandSwingProgress;
//        if (f < 0.0f) {
//            f += 1.0f;
//        }
//        return this.lastHandSwingProgress + f * tickDelta;
//    }
//
//    public void setHandSwingProgress()
//    {
//        this.lastHandSwingProgress = this.handSwingProgress;
//    }

}
