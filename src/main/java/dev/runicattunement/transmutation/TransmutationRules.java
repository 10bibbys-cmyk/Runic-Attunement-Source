package dev.runicattunement.transmutation;

import dev.runicattunement.compat.RunicCompatibility;
import dev.runicattunement.config.RunicConfig;
import dev.runicattunement.data.CostKind;
import dev.runicattunement.data.EnchantmentDefinition;
import dev.runicattunement.data.IngredientCost;
import dev.runicattunement.data.TransmutationCosts;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.class_1799;
import net.minecraft.class_1887;
import net.minecraft.class_1890;
import net.minecraft.class_2960;
import net.minecraft.class_7923;

public final class TransmutationRules {
   private TransmutationRules() {
   }

   public static List<class_2960> sourceEnchantments(class_1799 source) {
      if (source.method_7960()) {
         return List.of();
      }

      List<class_2960> ids = new ArrayList<>();

      for (class_1887 enchantment : class_1890.method_8222(source).keySet()) {
         class_2960 id = class_7923.field_41176.method_10221(enchantment);
         if (id != null) {
            ids.add(id);
         }
      }

      ids.sort(Comparator.comparing(class_2960::toString));
      return List.copyOf(ids);
   }

   public static TransmutationRules.Status normalStatus(
      class_2960 enchantmentId,
      class_1799 source,
      class_1799 target,
      Map<class_2960, EnchantmentDefinition> definitions,
      Set<class_2960> selectedNormal,
      Set<class_2960> removeCurses
   ) {
      class_1887 enchantment = (class_1887)class_7923.field_41176.method_17966(enchantmentId).orElse(null);
      if (enchantment == null) {
         return TransmutationRules.Status.UNDEFINED;
      }

      if (enchantment.method_8195()) {
         return TransmutationRules.Status.CURSE;
      }

      EnchantmentDefinition definition = definitions.get(enchantmentId);
      if (definition == null) {
         return TransmutationRules.Status.UNDEFINED;
      }

      int sourceLevel = class_1890.method_8225(enchantment, source);
      if (sourceLevel > 0 && !definition.level(sourceLevel).isEmpty()) {
         if (!target.method_7960() && RunicCompatibility.accepts(enchantment, target)) {
            int targetLevel = class_1890.method_8225(enchantment, target);
            if (targetLevel >= sourceLevel) {
               return TransmutationRules.Status.TARGET_EQUAL_OR_HIGHER;
            }

            for (class_1887 existing : class_1890.method_8222(target).keySet()) {
               if (existing != enchantment && !enchantment.method_8188(existing)) {
                  return TransmutationRules.Status.CONFLICT;
               }
            }

            for (class_2960 otherId : selectedNormal) {
               if (!otherId.equals(enchantmentId)) {
                  class_1887 other = (class_1887)class_7923.field_41176.method_17966(otherId).orElse(null);
                  if (other != null && !enchantment.method_8188(other)) {
                     return TransmutationRules.Status.CONFLICT;
                  }
               }
            }

            for (class_2960 curseId : sourceEnchantments(source)) {
               if (!removeCurses.contains(curseId)) {
                  class_1887 curse = (class_1887)class_7923.field_41176.method_17966(curseId).orElse(null);
                  if (curse != null && curse.method_8195() && curseCanTransfer(curseId, source, target, Set.of()) && !enchantment.method_8188(curse)) {
                     return TransmutationRules.Status.CONFLICT;
                  }
               }
            }

            return TransmutationRules.Status.TRANSFERABLE;
         } else {
            return TransmutationRules.Status.INCOMPATIBLE;
         }
      } else {
         return TransmutationRules.Status.LEVEL_UNSUPPORTED;
      }
   }

   public static boolean curseCanTransfer(class_2960 curseId, class_1799 source, class_1799 target, Set<class_2960> selectedNormal) {
      class_1887 curse = (class_1887)class_7923.field_41176.method_17966(curseId).orElse(null);
      if (curse != null && curse.method_8195()) {
         int sourceLevel = class_1890.method_8225(curse, source);
         if (sourceLevel > 0 && !target.method_7960() && RunicCompatibility.accepts(curse, target)) {
            if (class_1890.method_8225(curse, target) >= sourceLevel) {
               return false;
            }

            for (class_1887 existing : class_1890.method_8222(target).keySet()) {
               if (existing != curse && !curse.method_8188(existing)) {
                  return false;
               }
            }

            for (class_2960 normalId : selectedNormal) {
               class_1887 normal = (class_1887)class_7923.field_41176.method_17966(normalId).orElse(null);
               if (normal != null && !curse.method_8188(normal)) {
                  return false;
               }
            }

            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static Set<class_2960> retainedCurses(class_1799 source, class_1799 target, Set<class_2960> selectedNormal, Set<class_2960> removeCurses) {
      Set<class_2960> retained = new LinkedHashSet<>();

      for (class_2960 id : sourceEnchantments(source)) {
         class_1887 enchantment = (class_1887)class_7923.field_41176.method_17966(id).orElse(null);
         if (enchantment != null && enchantment.method_8195() && (removeCurses.contains(id) || !curseCanTransfer(id, source, target, selectedNormal))) {
            retained.add(id);
         }
      }

      return Set.copyOf(retained);
   }

   public static Set<class_2960> transferredCurses(class_1799 source, class_1799 target, Set<class_2960> selectedNormal, Set<class_2960> removeCurses) {
      Set<class_2960> result = new LinkedHashSet<>();

      for (class_2960 id : sourceEnchantments(source)) {
         class_1887 enchantment = (class_1887)class_7923.field_41176.method_17966(id).orElse(null);
         if (enchantment != null && enchantment.method_8195() && !removeCurses.contains(id) && curseCanTransfer(id, source, target, selectedNormal)) {
            result.add(id);
         }
      }

      return Set.copyOf(result);
   }

   public static List<IngredientCost> calculateCosts(
      class_1799 source,
      class_1799 target,
      Map<class_2960, EnchantmentDefinition> definitions,
      Set<class_2960> selectedNormal,
      Set<class_2960> removeCurses,
      TransmutationCosts costDefinition,
      RunicConfig.Transmutation settings
   ) {
      if (settings.enabled() && costDefinition.available()) {
         Map<class_2960, Integer> totals = new LinkedHashMap<>();

         for (class_2960 id : selectedNormal) {
            class_1887 enchantment = (class_1887)class_7923.field_41176.method_17966(id).orElse(null);
            EnchantmentDefinition definition = definitions.get(id);
            if (enchantment != null
               && definition != null
               && !enchantment.method_8195()
               && normalStatus(id, source, target, definitions, selectedNormal, removeCurses) == TransmutationRules.Status.TRANSFERABLE) {
               int level = class_1890.method_8225(enchantment, source);
               double units = settings.unitsFor(definition.tabletTier(), level);

               for (IngredientCost material : costDefinition.unitMaterials()) {
                  int count = safeCeil(material.count() * units);
                  if (count > 0) {
                     totals.merge(material.itemId(), count, TransmutationRules::safeAdd);
                  }
               }
            }
         }

         int retainedCurseCount = retainedCurses(source, target, selectedNormal, removeCurses).size();
         if (retainedCurseCount > 0) {
            for (IngredientCost material : costDefinition.curseRetentionCosts()) {
               int each = safeCeil(material.count() * settings.curseRetentionCostMultiplier());
               if (each > 0) {
                  totals.merge(material.itemId(), safeMultiply(each, retainedCurseCount), TransmutationRules::safeAdd);
               }
            }
         }

         List<IngredientCost> result = new ArrayList<>();

         for (Entry<class_2960, Integer> entry : totals.entrySet()) {
            if (entry.getValue() > 0) {
               result.add(new IngredientCost(entry.getKey(), entry.getValue(), CostKind.UNIVERSAL));
            }
         }

         return List.copyOf(result);
      } else {
         return List.of();
      }
   }

   public static boolean hasValidNormalTransfer(
      class_1799 source, class_1799 target, Map<class_2960, EnchantmentDefinition> definitions, Set<class_2960> selectedNormal, Set<class_2960> removeCurses
   ) {
      for (class_2960 id : selectedNormal) {
         if (normalStatus(id, source, target, definitions, selectedNormal, removeCurses) == TransmutationRules.Status.TRANSFERABLE) {
            return true;
         }
      }

      return false;
   }

   private static int safeCeil(double value) {
      if (Double.isNaN(value) || value <= 0.0) {
         return 0;
      } else {
         return !(value >= 2.147483647E9) && value != Double.POSITIVE_INFINITY ? (int)Math.ceil(value) : Integer.MAX_VALUE;
      }
   }

   private static int safeAdd(int left, int right) {
      return (int)Math.min(2147483647L, (long)left + right);
   }

   private static int safeMultiply(int value, int multiplier) {
      long result = (long)value * multiplier;
      return (int)Math.min(2147483647L, result);
   }

   public enum Status {
      TRANSFERABLE,
      CURSE,
      UNDEFINED,
      LEVEL_UNSUPPORTED,
      INCOMPATIBLE,
      CONFLICT,
      TARGET_EQUAL_OR_HIGHER;
   }
}
