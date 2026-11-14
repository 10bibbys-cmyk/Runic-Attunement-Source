/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2540
 *  net.minecraft.class_2960
 */
package dev.runicattunement.data;

import dev.runicattunement.data.LevelCost;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.class_2540;
import net.minecraft.class_2960;

public record EnchantmentDefinition(class_2960 enchantmentId, int tabletTier, Map<Integer, LevelCost> levels) {
    public EnchantmentDefinition {
        if (tabletTier < 1 || tabletTier > 3) {
            throw new IllegalArgumentException("Tablet tier must be 1-3");
        }
        levels = Map.copyOf(levels);
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
        List sorted = this.levels.keySet().stream().sorted().toList();
        buf.method_10804(sorted.size());
        Iterator iterator = sorted.iterator();
        while (iterator.hasNext()) {
            int level = (Integer)iterator.next();
            this.levels.get(level).write(buf);
        }
    }

    public static EnchantmentDefinition read(class_2540 buf) {
        class_2960 enchantmentId = buf.method_10810();
        int tabletTier = buf.method_10816();
        int count = buf.method_10816();
        LinkedHashMap<Integer, LevelCost> levels = new LinkedHashMap<Integer, LevelCost>();
        for (int i = 0; i < count; ++i) {
            LevelCost levelCost = LevelCost.read(buf);
            levels.put(levelCost.level(), levelCost);
        }
        return new EnchantmentDefinition(enchantmentId, tabletTier, levels);
    }
}

