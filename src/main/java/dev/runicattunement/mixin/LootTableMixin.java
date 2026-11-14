package dev.runicattunement.mixin;

import dev.runicattunement.bypass.RunicBypassContext;
import dev.runicattunement.bypass.RunicEnchantmentBypass;
import dev.runicattunement.config.RunicConfig;
import java.util.function.Consumer;
import net.minecraft.class_1799;
import net.minecraft.class_52;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(class_52.class)
public abstract class LootTableMixin {
   @ModifyVariable(method = "method_320(Lnet/minecraft/class_47;Ljava/util/function/Consumer;)V", at = @At("HEAD"), argsOnly = true, remap = false)
   private Consumer<class_1799> runicAttunement$sanitizeGeneratedLoot(Consumer<class_1799> original) {
      RunicConfig.EnchantmentBypassControl control = RunicConfig.enchantmentBypassControl();
      if (!control.disableNaturalEnchantedBooks() && !control.disableNaturalEnchantedGear() && !control.disableFishingEnchantedBooks()) {
         return original;
      }

      boolean fishing = RunicBypassContext.isFishing();
      return stack -> original.accept(RunicEnchantmentBypass.sanitizeGeneratedLoot(stack, fishing));
   }
}
