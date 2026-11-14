package dev.runicattunement.reforging;

import dev.runicattunement.config.RunicConfig;
import dev.runicattunement.data.RunicDefinitionManager;
import dev.runicattunement.item.RunicDustItem;
import dev.runicattunement.item.RunicTabletItem;
import dev.runicattunement.registry.ModItems;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.class_156;
import net.minecraft.class_1799;
import net.minecraft.class_2960;
import net.minecraft.class_5819;

public final class RunicReforging {
   private RunicReforging() {
   }

   public static boolean isReclaimableTablet(class_1799 stack) {
      return stack.method_7909() instanceof RunicTabletItem && RunicTabletItem.getEnchantmentId(stack).isPresent();
   }

   public static class_1799 createDustResult(class_1799 tabletStack) {
      if (RunicConfig.tabletReforging().enabled() && isReclaimableTablet(tabletStack)) {
         int tier = ((RunicTabletItem)tabletStack.method_7909()).tier();
         return new class_1799(ModItems.dustByTier(tier));
      } else {
         return class_1799.field_8037;
      }
   }

   public static boolean isRunicReforge(class_1799 tabletStack, class_1799 dustStack) {
      if (!RunicConfig.tabletReforging().enabled()) {
         return false;
      }

      if (!RunicTabletItem.isConfiguredTablet(tabletStack)) {
         return false;
      }

      if (dustStack.method_7909() instanceof RunicDustItem dust) {
         if (!(tabletStack.method_7909() instanceof RunicTabletItem tablet)) {
            return false;
         } else {
            int var7 = tablet.tier();
            int dustTier = dust.tier();
            if (dustTier < var7 || dustTier > 3) {
               return false;
            } else {
               return RunicDefinitionManager.tierPool(var7).isEmpty() ? false : dustTier == var7 || !RunicDefinitionManager.tierPool(dustTier).isEmpty();
            }
         }
      } else {
         return false;
      }
   }

   public static class_1799 createPreview(class_1799 tabletStack, class_1799 dustStack) {
      Optional<RunicReforging.ReforgeOdds> odds = odds(tabletStack, dustStack);
      if (odds.isEmpty()) {
         return class_1799.field_8037;
      }

      RunicReforging.ReforgeOdds value = odds.get();
      class_1799 preview = new class_1799(ModItems.byTier(value.startTier()));
      RunicTabletItem.setReforgePreview(preview, value.startTier(), value.upgradeTier(), value.upgradeChance());
      return preview;
   }

   public static class_1799 createRandomResult(class_1799 tabletStack, class_1799 dustStack, class_5819 random) {
      if (!isRunicReforge(tabletStack, dustStack)) {
         return class_1799.field_8037;
      }

      int startTier = ((RunicTabletItem)tabletStack.method_7909()).tier();
      int dustTier = ((RunicDustItem)dustStack.method_7909()).tier();
      int resultTier = startTier;
      if (dustTier > startTier) {
         double chance = RunicConfig.tabletReforging().upgradeChance(startTier, dustTier);
         if (random.method_43058() < chance) {
            resultTier = dustTier;
         }
      }

      List<class_2960> candidates = new ArrayList<>(RunicDefinitionManager.tierPool(resultTier));
      if (candidates.isEmpty()) {
         return class_1799.field_8037;
      }

      class_2960 currentId = RunicTabletItem.getEnchantmentId(tabletStack).orElse(null);
      if (resultTier == startTier && currentId != null && candidates.size() > 1) {
         candidates.remove(currentId);
      }

      if (candidates.isEmpty()) {
         return class_1799.field_8037;
      }

      class_2960 resultId = (class_2960)class_156.method_32309(candidates, random);
      class_1799 result = new class_1799(ModItems.byTier(resultTier));
      RunicTabletItem.setEnchantment(result, resultId);
      return result;
   }

   public static Optional<RunicReforging.ReforgeOdds> odds(class_1799 tabletStack, class_1799 dustStack) {
      if (!isRunicReforge(tabletStack, dustStack)) {
         return Optional.empty();
      }

      int startTier = ((RunicTabletItem)tabletStack.method_7909()).tier();
      int dustTier = ((RunicDustItem)dustStack.method_7909()).tier();
      double chance = dustTier == startTier ? 1.0 : RunicConfig.tabletReforging().upgradeChance(startTier, dustTier);
      return Optional.of(new RunicReforging.ReforgeOdds(startTier, dustTier, chance));
   }

   public record ReforgeOdds(int startTier, int upgradeTier, double upgradeChance) {
   }
}
