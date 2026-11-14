/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package pota.runicmpfix.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pota.runicmpfix.RunicMpFixClient;

@Mixin(targets={"dev.runicattunement.client.RunicInscriptionScreen"}, remap=false)
public abstract class RunicInscriptionScreenMixin {
    @Inject(method="clickServerButton", at=@At(value="HEAD"), cancellable=true, remap=false)
    private void pota$runicMpFix$redirectFinalActions(int n, CallbackInfo callbackInfo) {
        if (RunicMpFixClient.interceptFinalAction(this, n)) {
            callbackInfo.cancel();
        }
    }
}

