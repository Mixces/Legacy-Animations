package com.mixces.legacyanimations.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mixces.legacyanimations.config.LegacyAnimationsSettings;
import com.mixces.legacyanimations.mixin.access.IEntityMixin;
import com.mixces.legacyanimations.mixin.access.ILivingEntityMixin;
import com.mixces.legacyanimations.util.ItemUtils;
import com.mixces.legacyanimations.util.ServerUtils;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin {

    public PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    //todo: hypixel
//    @ModifyReturnValue(
//            method = "getEquippedStack",
//            at = @At(
//                    value = "RETURN",
//                    ordinal = 1
//            )
//    )
//    private ItemStack legacyAnimations$fakeShield(ItemStack original) {
//        if (ItemUtils.INSTANCE.isSwordInMainHand(null) && ServerUtils.INSTANCE.isValidServer()) {
//            return new ItemStack(Items.SHIELD);
//        }
//        return original;
//    }

    //todo: viaversion
//    @Inject(
//            method = "resetLastAttackedTicks",
//            at = @At(
//                    value = "HEAD"
//            ),
//            cancellable = true
//    )
//    private void legacyAnimations$removeAttackDelay(CallbackInfo ci) {
//        if (ServerUtils.INSTANCE.isValidServer()) {
//            ci.cancel();
//        }
//    }

    @Inject(
            method = "tickMovement",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;strideDistance:F",
                    opcode = Opcodes.PUTFIELD,
                    shift = At.Shift.AFTER
            )
    )
    private void legacyAnimations$getPitchFromVelocity(CallbackInfo ci) {
        if (LegacyAnimationsSettings.getInstance().oldViewBob) {
            final PlayerEntity entity = (PlayerEntity) (Object) this;
            final double velocityY = ((IEntityMixin) entity).invokeGetVelocity().y;
            float f1 = (float) (Math.atan(-velocityY * 0.2F) * 15.0F);
            if (((IEntityMixin) entity).invokeIsOnGround() || ((ILivingEntityMixin) entity).invokeGetHealth() <= 0.0F) {
                f1 = 0.0F;
            }
            legacyAnimations$cameraPitch += (f1 - legacyAnimations$cameraPitch) * 0.8F;
        }
    }

    //todo: add toggle for eyeheight && eyeheight under slabs
    @ModifyReturnValue(
            method = "getBaseDimensions",
            at = @At(
                    value = "RETURN"
            )
    )
    private EntityDimensions legacyAnimations$modifyStandingEyeHeight(EntityDimensions original, EntityPose pose) {
        if (LegacyAnimationsSettings.getInstance().oldSneaking && pose == EntityPose.CROUCHING) {
            return original.withEyeHeight(original.eyeHeight() + 0.27F);
        }
        return original;
//        EntityDimensions.changing(0.6f, 1.5f).withEyeHeight(original.eyeHeight() + 0.27F).withAttachments(EntityAttachments.builder().add(EntityAttachmentType.VEHICLE, VEHICLE_ATTACHMENT_POS))
    }
}
