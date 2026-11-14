package dev.runicattunement.item;

import dev.runicattunement.compat.EnchantedTooltipsBridge;
import dev.runicattunement.data.EnchantmentDefinition;
import dev.runicattunement.data.RunicDefinitionManager;
import dev.runicattunement.util.RunicPotency;
import java.util.List;
import java.util.Optional;
import net.minecraft.class_124;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1836;
import net.minecraft.class_1887;
import net.minecraft.class_1937;
import net.minecraft.class_2487;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_7923;
import net.minecraft.class_1792.class_1793;
import org.jetbrains.annotations.Nullable;

public class RunicTabletItem extends class_1792 {
   private static final String NBT_ROOT = "RunicAttunement";
   private static final String NBT_ENCHANTMENT = "Enchantment";
   private static final String NBT_REFORGE_PREVIEW = "ReforgePreview";
   private static final String NBT_REFORGE_START_TIER = "ReforgeStartTier";
   private static final String NBT_REFORGE_DUST_TIER = "ReforgeDustTier";
   private static final String NBT_REFORGE_UPGRADE_CHANCE = "ReforgeUpgradeChance";
   private final int tier;

   public RunicTabletItem(int tier, class_1793 settings) {
      super(settings.method_7889(1));
      if (tier >= 1 && tier <= 3) {
         this.tier = tier;
      } else {
         throw new IllegalArgumentException("Runic Tablet tier must be 1-3");
      }
   }

   public int tier() {
      return this.tier;
   }

   public boolean method_7886(class_1799 stack) {
      return true;
   }

   public static void setEnchantment(class_1799 stack, class_2960 enchantmentId) {
      class_2487 root = stack.method_7948().method_10562("RunicAttunement");
      root.method_10551("ReforgePreview");
      root.method_10551("ReforgeStartTier");
      root.method_10551("ReforgeDustTier");
      root.method_10551("ReforgeUpgradeChance");
      root.method_10582("Enchantment", enchantmentId.toString());
      stack.method_7948().method_10566("RunicAttunement", root);
   }

   public static void setReforgePreview(class_1799 stack, int startTier, int dustTier, double upgradeChance) {
      if (stack.method_7909() instanceof RunicTabletItem) {
         class_2487 root = stack.method_7948().method_10562("RunicAttunement");
         root.method_10551("Enchantment");
         root.method_10556("ReforgePreview", true);
         root.method_10569("ReforgeStartTier", startTier);
         root.method_10569("ReforgeDustTier", dustTier);
         root.method_10549("ReforgeUpgradeChance", upgradeChance);
         stack.method_7948().method_10566("RunicAttunement", root);
      }
   }

   public static boolean isReforgePreview(class_1799 stack) {
      if (!(stack.method_7909() instanceof RunicTabletItem)) {
         return false;
      }

      class_2487 nbt = stack.method_7969();
      return nbt != null && nbt.method_10545("RunicAttunement") && nbt.method_10562("RunicAttunement").method_10577("ReforgePreview");
   }

   public static Optional<class_2960> getEnchantmentId(class_1799 stack) {
      if (!(stack.method_7909() instanceof RunicTabletItem)) {
         return Optional.empty();
      }

      class_2487 nbt = stack.method_7969();
      if (nbt != null && nbt.method_10545("RunicAttunement")) {
         class_2487 root = nbt.method_10562("RunicAttunement");
         if (!root.method_10545("Enchantment")) {
            return Optional.empty();
         }

         try {
            return Optional.of(new class_2960(root.method_10558("Enchantment")));
         } catch (RuntimeException ignored) {
            return Optional.empty();
         }
      } else {
         return Optional.empty();
      }
   }

   public static Optional<class_1887> getEnchantment(class_1799 stack) {
      return getEnchantmentId(stack).flatMap(id -> class_7923.field_41176.method_17966(id));
   }

   public static boolean isConfiguredTablet(class_1799 stack) {
      Optional<class_2960> enchantmentId = getEnchantmentId(stack);
      if (enchantmentId.isEmpty()) {
         return false;
      }

      EnchantmentDefinition definition = RunicDefinitionManager.get(enchantmentId.get());
      return definition != null && stack.method_7909() instanceof RunicTabletItem tablet ? definition.tabletTier() == tablet.tier() : false;
   }

   public void method_7851(class_1799 stack, @Nullable class_1937 world, List<class_2561> tooltip, class_1836 context) {
      if (isReforgePreview(stack)) {
         class_2487 root = stack.method_7948().method_10562("RunicAttunement");
         int startTier = root.method_10550("ReforgeStartTier");
         int dustTier = root.method_10550("ReforgeDustTier");
         double chance = root.method_10574("ReforgeUpgradeChance");
         tooltip.add(class_2561.method_43471("tooltip.runic_attunement.reforge_preview").method_27692(class_124.field_1065));
         tooltip.add(class_2561.method_43471("tooltip.runic_attunement.reforge_random_attunement").method_27692(class_124.field_1080));
         if (dustTier > startTier) {
            int percent = (int)Math.round(chance * 100.0);
            tooltip.add(
               class_2561.method_43469("tooltip.runic_attunement.reforge_upgrade_chance", new Object[]{percent, RunicPotency.name(dustTier)})
                  .method_27692(this.tierFormatting(dustTier))
            );
            tooltip.add(
               class_2561.method_43469("tooltip.runic_attunement.reforge_failure_tier", new Object[]{100 - percent, RunicPotency.name(startTier)})
                  .method_27692(class_124.field_1063)
            );
         } else {
            tooltip.add(
               class_2561.method_43469("tooltip.runic_attunement.reforge_same_tier", new Object[]{RunicPotency.name(startTier)})
                  .method_27692(this.tierFormatting(startTier))
            );
         }

         EnchantedTooltipsBridge.appendTabletTooltip(stack, tooltip);
      } else {
         Optional<class_2960> enchantmentId = getEnchantmentId(stack);
         if (!enchantmentId.isEmpty()) {
            Optional<class_1887> enchantment = class_7923.field_41176.method_17966(enchantmentId.get());
            if (enchantment.isPresent()) {
               tooltip.add(
                  class_2561.method_43469("tooltip.runic_attunement.attuned_to", new Object[]{class_2561.method_43471(enchantment.get().method_8184())})
                     .method_27692(RunicPotency.formatting(this.tier))
               );
               tooltip.add(class_2561.method_43473());
               tooltip.add(class_2561.method_43471("tooltip.runic_attunement.table_use").method_27692(class_124.field_1080));
               tooltip.add(
                  class_2561.method_43471("tooltip.runic_attunement.hold_shift").method_27695(new class_124[]{class_124.field_1063, class_124.field_1056})
               );
            } else {
               tooltip.add(class_2561.method_43471("tooltip.runic_attunement.invalid_data").method_27692(class_124.field_1061));
               tooltip.add(class_2561.method_43470(enchantmentId.get().toString()).method_27692(class_124.field_1079));
            }

            EnchantedTooltipsBridge.appendTabletTooltip(stack, tooltip);
         } else {
            class_2487 nbt = stack.method_7969();
            boolean malformed = nbt != null && nbt.method_10545("RunicAttunement") && nbt.method_10562("RunicAttunement").method_10545("Enchantment");
            tooltip.add(
               class_2561.method_43471(malformed ? "tooltip.runic_attunement.invalid_data" : "tooltip.runic_attunement.unattuned")
                  .method_27692(malformed ? class_124.field_1061 : class_124.field_1063)
            );
            EnchantedTooltipsBridge.appendTabletTooltip(stack, tooltip);
         }
      }
   }

   private class_124 tierFormatting(int displayTier) {
      return RunicPotency.formatting(displayTier);
   }
}
