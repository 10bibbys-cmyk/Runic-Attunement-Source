/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1799
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package dev.runicattunement.mixin;

import dev.runicattunement.config.RunicConfig;
import dev.runicattunement.reforging.RunicReforging;
import net.minecraft.class_1799;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets={"net.minecraft.screen.GrindstoneScreenHandler$2", "net.minecraft.screen.GrindstoneScreenHandler$3"})
public abstract class GrindstoneInputSlotMixin {
    @Inject(method={"canInsert"}, at={@At(value="HEAD")}, cancellable=true)
    private void runicAttunement$allowConfiguredTablets(class_1799 stack, CallbackInfoReturnable<Boolean> cir) {
        if (RunicConfig.tabletReforging().enabled() && RunicReforging.isReclaimableTablet(stack)) {
            cir.setReturnValue((Object)true);
        }
    }
}

