/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1263
 *  net.minecraft.class_1799
 *  net.minecraft.class_3803
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package dev.runicattunement.mixin;

import dev.runicattunement.reforging.RunicReforging;
import net.minecraft.class_1263;
import net.minecraft.class_1799;
import net.minecraft.class_3803;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_3803.class})
public abstract class GrindstoneScreenHandlerMixin {
    @Shadow
    @Final
    class_1263 field_16772;
    @Shadow
    @Final
    private class_1263 field_16773;

    @Inject(method={"updateResult"}, at={@At(value="HEAD")}, cancellable=true)
    private void runicAttunement$reclaimTablet(CallbackInfo ci) {
        class_1799 first = this.field_16772.method_5438(0);
        class_1799 second = this.field_16772.method_5438(1);
        boolean firstTablet = RunicReforging.isReclaimableTablet(first);
        boolean secondTablet = RunicReforging.isReclaimableTablet(second);
        if (!firstTablet && !secondTablet) {
            return;
        }
        class_1799 output = class_1799.field_8037;
        if (firstTablet && second.method_7960()) {
            output = RunicReforging.createDustResult(first);
        } else if (secondTablet && first.method_7960()) {
            output = RunicReforging.createDustResult(second);
        }
        this.field_16773.method_5447(0, output);
        ((class_3803)this).method_7623();
        ci.cancel();
    }
}

