/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1657
 *  net.minecraft.class_1799
 *  net.minecraft.class_1887
 *  net.minecraft.class_1890
 *  net.minecraft.class_2960
 *  net.minecraft.class_7923
 */
package dev.runicattunement.util;

import dev.runicattunement.config.RunicConfig;
import dev.runicattunement.data.EnchantmentDefinition;
import dev.runicattunement.data.LevelCost;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1887;
import net.minecraft.class_1890;
import net.minecraft.class_2960;
import net.minecraft.class_7923;

public final class RunicExperience {
    private RunicExperience() {
    }

    public static int defaultCost(int tabletTier, int targetLevel) {
        int[][] table = new int[][]{new int[0], {0, 50, 75, 105, 140, 180}, {0, 90, 135, 185, 245, 315}, {0, 160, 235, 325, 425, 540}};
        int tier = Math.max(1, Math.min(3, tabletTier));
        int level = Math.max(1, targetLevel);
        if (level < table[tier].length) {
            return table[tier][level];
        }
        long last = table[tier][table[tier].length - 1];
        long extra = (long)level - (long)(table[tier].length - 1);
        long increment = switch (tier) {
            case 1 -> 50L;
            case 2 -> 75L;
            default -> 110L;
        };
        return (int)Math.min(Integer.MAX_VALUE, last + extra * increment);
    }

    public static int availablePoints(class_1657 player) {
        int level = Math.max(0, player.field_7520);
        long base = RunicExperience.pointsAtLevelLong(level);
        long progress = Math.max(0L, (long)Math.floor(player.field_7510 * (float)player.method_7349() + 1.0E-4f));
        return (int)Math.min(Integer.MAX_VALUE, Math.max(0L, base + progress));
    }

    public static int pointsAtLevel(int level) {
        return (int)Math.min(Integer.MAX_VALUE, RunicExperience.pointsAtLevelLong(level));
    }

    private static long pointsAtLevelLong(int level) {
        long l = Math.max(0, level);
        if (l >= 22000L) {
            return Integer.MAX_VALUE;
        }
        if (l <= 16L) {
            return l * l + 6L * l;
        }
        if (l <= 31L) {
            return (5L * l * l - 81L * l + 720L) / 2L;
        }
        return (9L * l * l - 325L * l + 4440L) / 2L;
    }

    public static int attunementCost(EnchantmentDefinition definition, int targetLevel, RunicConfig.ExperienceCosts settings) {
        if (!settings.enabled()) {
            return 0;
        }
        int base = definition.level(targetLevel).map(LevelCost::experiencePoints).orElse(0);
        return RunicExperience.scale(base, settings.globalMultiplier());
    }

    public static int transmutationCost(class_1799 source, Map<class_2960, EnchantmentDefinition> definitions, Set<class_2960> selectedNormal, Set<class_2960> transferredCurses, RunicConfig.ExperienceCosts settings) {
        if (!settings.enabled()) {
            return 0;
        }
        LinkedHashSet<class_2960> transferred = new LinkedHashSet<class_2960>(selectedNormal);
        transferred.addAll(transferredCurses);
        long base = 0L;
        for (class_2960 id : transferred) {
            EnchantmentDefinition definition = definitions.get(id);
            class_1887 enchantment = class_7923.field_41176.method_17966(id).orElse(null);
            if (definition == null || enchantment == null) continue;
            int level = class_1890.method_8225((class_1887)enchantment, (class_1799)source);
            for (int i = 1; i <= level; ++i) {
                base += (long)definition.level(i).map(LevelCost::experiencePoints).orElse(0).intValue();
            }
        }
        return RunicExperience.scale(base, settings.globalMultiplier() * settings.transmutationMultiplier());
    }

    private static int scale(long base, double multiplier) {
        if (base <= 0L || multiplier <= 0.0) {
            return 0;
        }
        return (int)Math.min(2.147483647E9, Math.ceil((double)base * multiplier));
    }
}

