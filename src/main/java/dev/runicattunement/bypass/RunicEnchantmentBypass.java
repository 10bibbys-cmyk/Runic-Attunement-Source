package dev.runicattunement.bypass;

import dev.runicattunement.config.RunicConfig;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.class_1772;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1887;
import net.minecraft.class_1890;
import net.minecraft.class_1914;
import net.minecraft.class_1916;

public final class RunicEnchantmentBypass {
   private RunicEnchantmentBypass() {
   }

   public static class_1799 sanitizeGeneratedLoot(class_1799 stack, boolean fishing) {
      if (stack != null && !stack.method_7960()) {
         RunicConfig.EnchantmentBypassControl control = RunicConfig.enchantmentBypassControl();
         if (hasStoredEnchantments(stack)) {
            boolean blockBook = fishing ? control.disableFishingEnchantedBooks() : control.disableNaturalEnchantedBooks();
            if (blockBook) {
               return new class_1799(class_1802.field_8529, stack.method_7947());
            }
         }

         if (control.disableNaturalEnchantedGear() && hasDirectEnchantments(stack)) {
            class_1890.method_8214(Map.of(), stack);
         }

         return stack;
      } else {
         return stack;
      }
   }

   public static void sanitizeTradeOffers(class_1916 offers) {
      if (offers != null && !offers.isEmpty()) {
         RunicConfig.EnchantmentBypassControl control = RunicConfig.enchantmentBypassControl();
         if (control.disableVillagerEnchantedBookTrades() || control.disableEnchantedGearTrades()) {
            offers.removeIf(offer -> shouldRemoveTrade(offer, control));
         }
      }
   }

   private static boolean shouldRemoveTrade(class_1914 offer, RunicConfig.EnchantmentBypassControl control) {
      class_1799 sold = offer.method_8250();
      if (sold.method_7960()) {
         return false;
      } else {
         return control.disableVillagerEnchantedBookTrades() && hasStoredEnchantments(sold)
            ? true
            : control.disableEnchantedGearTrades() && !hasStoredEnchantments(sold) && hasDirectEnchantments(sold);
      }
   }

   public static boolean wouldAddOrIncreaseEnchantments(class_1799 base, class_1799 result) {
      if (base != null && result != null && !base.method_7960() && !result.method_7960()) {
         Map<class_1887, Integer> before = class_1890.method_8222(base);
         Map<class_1887, Integer> after = class_1890.method_8222(result);

         for (Entry<class_1887, Integer> entry : after.entrySet()) {
            if (entry.getValue() > before.getOrDefault(entry.getKey(), 0)) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static boolean hasStoredEnchantments(class_1799 stack) {
      return stack != null && !stack.method_7960() && !class_1772.method_7806(stack).isEmpty();
   }

   public static boolean hasDirectEnchantments(class_1799 stack) {
      return stack != null && !stack.method_7960() && !stack.method_7921().isEmpty();
   }
}
