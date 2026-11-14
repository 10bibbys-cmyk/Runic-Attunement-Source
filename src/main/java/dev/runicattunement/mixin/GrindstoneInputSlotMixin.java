package dev.runicattunement.mixin;

import dev.runicattunement.config.RunicConfig;
import dev.runicattunement.reforging.RunicReforging;
import net.minecraft.class_1799;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = {"net.minecraft.class_3803$2", "net.minecraft.class_3803$3"}, remap = false)
public abstract class GrindstoneInputSlotMixin {
   @Inject(method = "method_7680", at = @At("HEAD"), cancellable = true, remap = false)
   private void runicAttunement$allowConfiguredTablets(class_1799 stack, CallbackInfoReturnable<Boolean> cir) {
      if (RunicConfig.tabletReforging().enabled() && RunicReforging.isReclaimableTablet(stack)) {
         cir.setReturnValue(true);
      }
   }
}
