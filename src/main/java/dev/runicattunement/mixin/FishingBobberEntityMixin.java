/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1536
 *  net.minecraft.class_1799
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package dev.runicattunement.mixin;

import dev.runicattunement.bypass.RunicBypassContext;
import net.minecraft.class_1536;
import net.minecraft.class_1799;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_1536.class})
public abstract class FishingBobberEntityMixin {
    @Inject(method={"use"}, at={@At(value="HEAD")})
    private void runicAttunement$enterFishing(class_1799 usedItem, CallbackInfoReturnable<Integer> cir) {
        RunicBypassContext.enterFishing();
    }

    @Inject(method={"use"}, at={@At(value="RETURN")})
    private void runicAttunement$exitFishing(class_1799 usedItem, CallbackInfoReturnable<Integer> cir) {
        RunicBypassContext.exitFishing();
    }
}

