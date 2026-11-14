/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1799
 *  net.minecraft.class_1887
 *  net.minecraft.class_1890
 *  net.minecraft.class_2960
 *  net.minecraft.class_7923
 */
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
        ArrayList<class_2960> ids = new ArrayList<class_2960>();
        for (class_1887 enchantment : class_1890.method_8222((class_1799)source).keySet()) {
            class_2960 id = class_7923.field_41176.method_10221((Object)enchantment);
            if (id == null) continue;
            ids.add(id);
        }
        ids.sort(Comparator.comparing(class_2960::toString));
        return List.copyOf(ids);
    }

    public static Status normalStatus(class_2960 enchantmentId, class_1799 source, class_1799 target, Map<class_2960, EnchantmentDefinition> definitions, Set<class_2960> selectedNormal, Set<class_2960> removeCurses) {
        class_1887 enchantment = class_7923.field_41176.method_17966(enchantmentId).orElse(null);
        if (enchantment == null) {
            return Status.UNDEFINED;
        }
        if (enchantment.method_8195()) {
            return Status.CURSE;
        }
        EnchantmentDefinition definition = definitions.get(enchantmentId);
        if (definition == null) {
            return Status.UNDEFINED;
        }
        int sourceLevel = class_1890.method_8225((class_1887)enchantment, (class_1799)source);
        if (sourceLevel <= 0 || definition.level(sourceLevel).isEmpty()) {
            return Status.LEVEL_UNSUPPORTED;
        }
        if (target.method_7960() || !RunicCompatibility.accepts(enchantment, target)) {
            return Status.INCOMPATIBLE;
        }
        int targetLevel = class_1890.method_8225((class_1887)enchantment, (class_1799)target);
        if (targetLevel >= sourceLevel) {
            return Status.TARGET_EQUAL_OR_HIGHER;
        }
        for (class_1887 existing : class_1890.method_8222((class_1799)target).keySet()) {
            if (existing == enchantment || enchantment.method_8188(existing)) continue;
            return Status.CONFLICT;
        }
        for (class_2960 otherId : selectedNormal) {
            class_1887 other;
            if (otherId.equals((Object)enchantmentId) || (other = (class_1887)class_7923.field_41176.method_17966(otherId).orElse(null)) == null || enchantment.method_8188(other)) continue;
            return Status.CONFLICT;
        }
        for (class_2960 curseId : TransmutationRules.sourceEnchantments(source)) {
            class_1887 curse;
            if (removeCurses.contains(curseId) || (curse = (class_1887)class_7923.field_41176.method_17966(curseId).orElse(null)) == null || !curse.method_8195() || !TransmutationRules.curseCanTransfer(curseId, source, target, Set.of()) || enchantment.method_8188(curse)) continue;
            return Status.CONFLICT;
        }
        return Status.TRANSFERABLE;
    }

    public static boolean curseCanTransfer(class_2960 curseId, class_1799 source, class_1799 target, Set<class_2960> selectedNormal) {
        class_1887 curse = class_7923.field_41176.method_17966(curseId).orElse(null);
        if (curse == null || !curse.method_8195()) {
            return false;
        }
        int sourceLevel = class_1890.method_8225((class_1887)curse, (class_1799)source);
        if (sourceLevel <= 0 || target.method_7960() || !RunicCompatibility.accepts(curse, target)) {
            return false;
        }
        if (class_1890.method_8225((class_1887)curse, (class_1799)target) >= sourceLevel) {
            return false;
        }
        for (class_1887 existing : class_1890.method_8222((class_1799)target).keySet()) {
            if (existing == curse || curse.method_8188(existing)) continue;
            return false;
        }
        for (class_2960 normalId : selectedNormal) {
            class_1887 normal = class_7923.field_41176.method_17966(normalId).orElse(null);
            if (normal == null || curse.method_8188(normal)) continue;
            return false;
        }
        return true;
    }

    public static Set<class_2960> retainedCurses(class_1799 source, class_1799 target, Set<class_2960> selectedNormal, Set<class_2960> removeCurses) {
        LinkedHashSet<class_2960> retained = new LinkedHashSet<class_2960>();
        for (class_2960 id : TransmutationRules.sourceEnchantments(source)) {
            class_1887 enchantment = class_7923.field_41176.method_17966(id).orElse(null);
            if (enchantment == null || !enchantment.method_8195() || !removeCurses.contains(id) && TransmutationRules.curseCanTransfer(id, source, target, selectedNormal)) continue;
            retained.add(id);
        }
        return Set.copyOf(retained);
    }

    public static Set<class_2960> transferredCurses(class_1799 source, class_1799 target, Set<class_2960> selectedNormal, Set<class_2960> removeCurses) {
        LinkedHashSet<class_2960> result = new LinkedHashSet<class_2960>();
        for (class_2960 id : TransmutationRules.sourceEnchantments(source)) {
            class_1887 enchantment = class_7923.field_41176.method_17966(id).orElse(null);
            if (enchantment == null || !enchantment.method_8195() || removeCurses.contains(id) || !TransmutationRules.curseCanTransfer(id, source, target, selectedNormal)) continue;
            result.add(id);
        }
        return Set.copyOf(result);
    }

    public static List<IngredientCost> calculateCosts(class_1799 source, class_1799 target, Map<class_2960, EnchantmentDefinition> definitions, Set<class_2960> selectedNormal, Set<class_2960> removeCurses, TransmutationCosts costDefinition, RunicConfig.Transmutation settings) {
        if (!settings.enabled() || !costDefinition.available()) {
            return List.of();
        }
        LinkedHashMap<class_2960, Integer> totals = new LinkedHashMap<class_2960, Integer>();
        for (class_2960 class_29602 : selectedNormal) {
            class_1887 enchantment = class_7923.field_41176.method_17966(class_29602).orElse(null);
            EnchantmentDefinition definition = definitions.get(class_29602);
            if (enchantment == null || definition == null || enchantment.method_8195() || TransmutationRules.normalStatus(class_29602, source, target, definitions, selectedNormal, removeCurses) != Status.TRANSFERABLE) continue;
            int level = class_1890.method_8225((class_1887)enchantment, (class_1799)source);
            double units = settings.unitsFor(definition.tabletTier(), level);
            for (IngredientCost material : costDefinition.unitMaterials()) {
                int count = TransmutationRules.safeCeil((double)material.count() * units);
                if (count <= 0) continue;
                totals.merge(material.itemId(), count, TransmutationRules::safeAdd);
            }
        }
        int retainedCurseCount = TransmutationRules.retainedCurses(source, target, selectedNormal, removeCurses).size();
        if (retainedCurseCount > 0) {
            for (IngredientCost material : costDefinition.curseRetentionCosts()) {
                int each = TransmutationRules.safeCeil((double)material.count() * settings.curseRetentionCostMultiplier());
                if (each <= 0) continue;
                totals.merge(material.itemId(), TransmutationRules.safeMultiply(each, retainedCurseCount), TransmutationRules::safeAdd);
            }
        }
        ArrayList<IngredientCost> arrayList = new ArrayList<IngredientCost>();
        for (Map.Entry entry : totals.entrySet()) {
            if ((Integer)entry.getValue() <= 0) continue;
            arrayList.add(new IngredientCost((class_2960)entry.getKey(), (Integer)entry.getValue(), CostKind.UNIVERSAL));
        }
        return List.copyOf(arrayList);
    }

    public static boolean hasValidNormalTransfer(class_1799 source, class_1799 target, Map<class_2960, EnchantmentDefinition> definitions, Set<class_2960> selectedNormal, Set<class_2960> removeCurses) {
        for (class_2960 id : selectedNormal) {
            if (TransmutationRules.normalStatus(id, source, target, definitions, selectedNormal, removeCurses) != Status.TRANSFERABLE) continue;
            return true;
        }
        return false;
    }

    private static int safeCeil(double value) {
        if (Double.isNaN(value) || value <= 0.0) {
            return 0;
        }
        if (value >= 2.147483647E9 || value == Double.POSITIVE_INFINITY) {
            return Integer.MAX_VALUE;
        }
        return (int)Math.ceil(value);
    }

    private static int safeAdd(int left, int right) {
        return (int)Math.min(Integer.MAX_VALUE, (long)left + (long)right);
    }

    private static int safeMultiply(int value, int multiplier) {
        long result = (long)value * (long)multiplier;
        return (int)Math.min(Integer.MAX_VALUE, result);
    }

    public static enum Status {
        TRANSFERABLE,
        CURSE,
        UNDEFINED,
        LEVEL_UNSUPPORTED,
        INCOMPATIBLE,
        CONFLICT,
        TARGET_EQUAL_OR_HIGHER;

    }
}

