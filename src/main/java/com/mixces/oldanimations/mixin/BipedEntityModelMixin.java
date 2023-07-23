package com.mixces.oldanimations.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin<T extends LivingEntity> {

    @Shadow protected abstract void positionRightArm(T entity);
    @Shadow protected abstract void positionLeftArm(T entity);

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;animateArms(Lnet/minecraft/entity/LivingEntity;F)V", shift = At.Shift.BEFORE))
    private void setAngles_replaceArm(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        Hand hand = player.getActiveHand();
        boolean bl = hand == Hand.MAIN_HAND;
        Arm arm = bl ? player.getMainArm() : player.getMainArm().getOpposite();
        boolean bl2 = arm == Arm.RIGHT;
        if (player.getActiveItem().getItem() instanceof ShieldItem) {
            if (!bl2) {
                positionRightArm(livingEntity);
            } else {
                positionLeftArm(livingEntity);
            }
        }
    }

}
