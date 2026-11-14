package pota.runicmpfix.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pota.runicmpfix.RunicMpFixClient;

@Mixin(targets = "dev.runicattunement.client.RunicInscriptionScreen", remap = false)
public abstract class RunicInscriptionScreenMixin {
   @Inject(method = "clickServerButton", at = @At("HEAD"), cancellable = true, remap = false)
   private void pota$runicMpFix$redirectFinalActions(int var1, CallbackInfo var2) {
      if (RunicMpFixClient.interceptFinalAction(this, var1)) {
         var2.cancel();
      }
   }
}
