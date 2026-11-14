package dev.runicattunement.mixin;

import dev.runicattunement.bypass.RunicEnchantmentBypass;
import net.minecraft.class_1916;
import net.minecraft.class_3988;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_3988.class)
public abstract class MerchantEntityMixin {
   @Inject(method = "getOffers", at = @At("RETURN"))
   private void runicAttunement$sanitizeOffers(CallbackInfoReturnable<class_1916> cir) {
      class_3988 self = (class_3988)(Object)this;
      if (!self.method_37908().field_9236) {
         RunicEnchantmentBypass.sanitizeTradeOffers((class_1916)cir.getReturnValue());
      }
   }
}
