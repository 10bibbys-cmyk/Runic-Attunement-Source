package dev.runicattunement.data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.class_2540;
import net.minecraft.class_2960;

public record EnchantmentDefinition(class_2960 enchantmentId, int tabletTier, Map<Integer, LevelCost> levels) {
   public EnchantmentDefinition(class_2960 enchantmentId, int tabletTier, Map<Integer, LevelCost> levels) {
      if (tabletTier >= 1 && tabletTier <= 3) {
         levels = Map.copyOf(levels);
         this.enchantmentId = enchantmentId;
         this.tabletTier = tabletTier;
         this.levels = levels;
      } else {
         throw new IllegalArgumentException("Tablet tier must be 1-3");
      }
   }

   public Optional<LevelCost> level(int targetLevel) {
      return Optional.ofNullable(this.levels.get(targetLevel));
   }

   public int highestConfiguredLevel() {
      return this.levels.keySet().stream().mapToInt(Integer::intValue).max().orElse(0);
   }

   public void write(class_2540 buf) {
      buf.method_10812(this.enchantmentId);
      buf.method_10804(this.tabletTier);
      List<Integer> sorted = this.levels.keySet().stream().sorted().toList();
      buf.method_10804(sorted.size());

      for (int level : sorted) {
         this.levels.get(level).write(buf);
      }
   }

   public static EnchantmentDefinition read(class_2540 buf) {
      class_2960 enchantmentId = buf.method_10810();
      int tabletTier = buf.method_10816();
      int count = buf.method_10816();
      Map<Integer, LevelCost> levels = new LinkedHashMap<>();

      for (int i = 0; i < count; i++) {
         LevelCost levelCost = LevelCost.read(buf);
         levels.put(levelCost.level(), levelCost);
      }

      return new EnchantmentDefinition(enchantmentId, tabletTier, levels);
   }
}
