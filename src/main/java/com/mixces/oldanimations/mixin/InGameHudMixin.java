package com.mixces.oldanimations.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mixces.oldanimations.config.OldAnimationsSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @WrapOperation(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"))
    private boolean cancelOffhandHotbar(ItemStack itemStack, Operation<Boolean> original) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        assert player != null;
        return original.call(itemStack) || OldAnimationsSettings.CONFIG.instance().hideShieldHotbar && player.getOffHandStack().isOf(Items.SHIELD);
    }

}
