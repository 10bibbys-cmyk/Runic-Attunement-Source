package dev.runicattunement.mixin;

import dev.runicattunement.config.RunicConfig;
import net.minecraft.class_1266;
import net.minecraft.class_1308;
import net.minecraft.class_5819;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_1308.class)
public abstract class MobEntityMixin {
   @Inject(method = "updateEnchantments", at = @At("HEAD"), cancellable = true)
   private void runicAttunement$disableSpawnEnchantments(class_5819 random, class_1266 difficulty, CallbackInfo ci) {
      if (RunicConfig.enchantmentBypassControl().disableMobSpawnEnchantedGear()) {
         ci.cancel();
      }
   }
}
