/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1772
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_1890
 *  net.minecraft.class_1914
 *  net.minecraft.class_1916
 *  net.minecraft.class_1935
 */
package dev.runicattunement.bypass;

import dev.runicattunement.config.RunicConfig;
import java.util.Map;
import net.minecraft.class_1772;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1890;
import net.minecraft.class_1914;
import net.minecraft.class_1916;
import net.minecraft.class_1935;

public final class RunicEnchantmentBypass {
    private RunicEnchantmentBypass() {
    }

    public static class_1799 sanitizeGeneratedLoot(class_1799 stack, boolean fishing) {
        if (stack == null || stack.method_7960()) {
            return stack;
        }
        RunicConfig.EnchantmentBypassControl control = RunicConfig.enchantmentBypassControl();
        if (RunicEnchantmentBypass.hasStoredEnchantments(stack)) {
            boolean blockBook;
            boolean bl = blockBook = fishing ? control.disableFishingEnchantedBooks() : control.disableNaturalEnchantedBooks();
            if (blockBook) {
                return new class_1799((class_1935)class_1802.field_8529, stack.method_7947());
            }
        }
        if (control.disableNaturalEnchantedGear() && RunicEnchantmentBypass.hasDirectEnchantments(stack)) {
            class_1890.method_8214(Map.of(), (class_1799)stack);
        }
        return stack;
    }

    public static void sanitizeTradeOffers(class_1916 offers) {
        if (offers == null || offers.isEmpty()) {
            return;
        }
        RunicConfig.EnchantmentBypassControl control = RunicConfig.enchantmentBypassControl();
        if (!control.disableVillagerEnchantedBookTrades() && !control.disableEnchantedGearTrades()) {
            return;
        }
        offers.removeIf(offer -> RunicEnchantmentBypass.shouldRemoveTrade(offer, control));
    }

    private static boolean shouldRemoveTrade(class_1914 offer, RunicConfig.EnchantmentBypassControl control) {
        class_1799 sold = offer.method_8250();
        if (sold.method_7960()) {
            return false;
        }
        if (control.disableVillagerEnchantedBookTrades() && RunicEnchantmentBypass.hasStoredEnchantments(sold)) {
            return true;
        }
        return control.disableEnchantedGearTrades() && !RunicEnchantmentBypass.hasStoredEnchantments(sold) && RunicEnchantmentBypass.hasDirectEnchantments(sold);
    }

    public static boolean wouldAddOrIncreaseEnchantments(class_1799 base, class_1799 result) {
        if (base == null || result == null || base.method_7960() || result.method_7960()) {
            return false;
        }
        Map before = class_1890.method_8222((class_1799)base);
        Map after = class_1890.method_8222((class_1799)result);
        for (Map.Entry entry : after.entrySet()) {
            if ((Integer)entry.getValue() <= before.getOrDefault(entry.getKey(), 0)) continue;
            return true;
        }
        return false;
    }

    public static boolean hasStoredEnchantments(class_1799 stack) {
        return stack != null && !stack.method_7960() && !class_1772.method_7806((class_1799)stack).isEmpty();
    }

    public static boolean hasDirectEnchantments(class_1799 stack) {
        return stack != null && !stack.method_7960() && !stack.method_7921().isEmpty();
    }
}

