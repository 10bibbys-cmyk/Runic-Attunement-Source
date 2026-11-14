package dev.runicattunement.mixin;

import dev.runicattunement.advancement.RunicAdvancements;
import dev.runicattunement.item.RunicDustItem;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_3222;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.class_3803$4", remap = false)
public abstract class GrindstoneOutputSlotMixin {
   @Inject(method = "method_7667", at = @At("HEAD"), remap = false)
   private void runicAttunement$grindAdvancement(class_1657 player, class_1799 stack, CallbackInfo ci) {
      if (!player.method_37908().field_9236 && player instanceof class_3222 serverPlayer && stack.method_7909() instanceof RunicDustItem) {
         RunicAdvancements.triggerGrind(serverPlayer);
      }
   }
}
