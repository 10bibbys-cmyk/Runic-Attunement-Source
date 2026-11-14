/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_124
 *  net.minecraft.class_1792
 *  net.minecraft.class_1792$class_1793
 *  net.minecraft.class_1799
 *  net.minecraft.class_1836
 *  net.minecraft.class_1887
 *  net.minecraft.class_1937
 *  net.minecraft.class_2487
 *  net.minecraft.class_2520
 *  net.minecraft.class_2561
 *  net.minecraft.class_2960
 *  net.minecraft.class_7923
 *  org.jetbrains.annotations.Nullable
 */
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
import net.minecraft.class_2520;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_7923;
import org.jetbrains.annotations.Nullable;

public class RunicTabletItem
extends class_1792 {
    private static final String NBT_ROOT = "RunicAttunement";
    private static final String NBT_ENCHANTMENT = "Enchantment";
    private static final String NBT_REFORGE_PREVIEW = "ReforgePreview";
    private static final String NBT_REFORGE_START_TIER = "ReforgeStartTier";
    private static final String NBT_REFORGE_DUST_TIER = "ReforgeDustTier";
    private static final String NBT_REFORGE_UPGRADE_CHANCE = "ReforgeUpgradeChance";
    private final int tier;

    public RunicTabletItem(int tier, class_1792.class_1793 settings) {
        super(settings.method_7889(1));
        if (tier < 1 || tier > 3) {
            throw new IllegalArgumentException("Runic Tablet tier must be 1-3");
        }
        this.tier = tier;
    }

    public int tier() {
        return this.tier;
    }

    public boolean method_7886(class_1799 stack) {
        return true;
    }

    public static void setEnchantment(class_1799 stack, class_2960 enchantmentId) {
        class_2487 root = stack.method_7948().method_10562(NBT_ROOT);
        root.method_10551(NBT_REFORGE_PREVIEW);
        root.method_10551(NBT_REFORGE_START_TIER);
        root.method_10551(NBT_REFORGE_DUST_TIER);
        root.method_10551(NBT_REFORGE_UPGRADE_CHANCE);
        root.method_10582(NBT_ENCHANTMENT, enchantmentId.toString());
        stack.method_7948().method_10566(NBT_ROOT, (class_2520)root);
    }

    public static void setReforgePreview(class_1799 stack, int startTier, int dustTier, double upgradeChance) {
        if (!(stack.method_7909() instanceof RunicTabletItem)) {
            return;
        }
        class_2487 root = stack.method_7948().method_10562(NBT_ROOT);
        root.method_10551(NBT_ENCHANTMENT);
        root.method_10556(NBT_REFORGE_PREVIEW, true);
        root.method_10569(NBT_REFORGE_START_TIER, startTier);
        root.method_10569(NBT_REFORGE_DUST_TIER, dustTier);
        root.method_10549(NBT_REFORGE_UPGRADE_CHANCE, upgradeChance);
        stack.method_7948().method_10566(NBT_ROOT, (class_2520)root);
    }

    public static boolean isReforgePreview(class_1799 stack) {
        if (!(stack.method_7909() instanceof RunicTabletItem)) {
            return false;
        }
        class_2487 nbt = stack.method_7969();
        return nbt != null && nbt.method_10545(NBT_ROOT) && nbt.method_10562(NBT_ROOT).method_10577(NBT_REFORGE_PREVIEW);
    }

    public static Optional<class_2960> getEnchantmentId(class_1799 stack) {
        if (!(stack.method_7909() instanceof RunicTabletItem)) {
            return Optional.empty();
        }
        class_2487 nbt = stack.method_7969();
        if (nbt == null || !nbt.method_10545(NBT_ROOT)) {
            return Optional.empty();
        }
        class_2487 root = nbt.method_10562(NBT_ROOT);
        if (!root.method_10545(NBT_ENCHANTMENT)) {
            return Optional.empty();
        }
        try {
            return Optional.of(new class_2960(root.method_10558(NBT_ENCHANTMENT)));
        }
        catch (RuntimeException ignored) {
            return Optional.empty();
        }
    }

    public static Optional<class_1887> getEnchantment(class_1799 stack) {
        return RunicTabletItem.getEnchantmentId(stack).flatMap(id -> class_7923.field_41176.method_17966(id));
    }

    public static boolean isConfiguredTablet(class_1799 stack) {
        class_1792 class_17922;
        Optional<class_2960> enchantmentId = RunicTabletItem.getEnchantmentId(stack);
        if (enchantmentId.isEmpty()) {
            return false;
        }
        EnchantmentDefinition definition = RunicDefinitionManager.get(enchantmentId.get());
        if (definition == null || !((class_17922 = stack.method_7909()) instanceof RunicTabletItem)) {
            return false;
        }
        RunicTabletItem tablet = (RunicTabletItem)class_17922;
        return definition.tabletTier() == tablet.tier();
    }

    public void method_7851(class_1799 stack, @Nullable class_1937 world, List<class_2561> tooltip, class_1836 context) {
        if (RunicTabletItem.isReforgePreview(stack)) {
            class_2487 root = stack.method_7948().method_10562(NBT_ROOT);
            int startTier = root.method_10550(NBT_REFORGE_START_TIER);
            int dustTier = root.method_10550(NBT_REFORGE_DUST_TIER);
            double chance = root.method_10574(NBT_REFORGE_UPGRADE_CHANCE);
            tooltip.add((class_2561)class_2561.method_43471((String)"tooltip.runic_attunement.reforge_preview").method_27692(class_124.field_1065));
            tooltip.add((class_2561)class_2561.method_43471((String)"tooltip.runic_attunement.reforge_random_attunement").method_27692(class_124.field_1080));
            if (dustTier > startTier) {
                int percent = (int)Math.round(chance * 100.0);
                tooltip.add((class_2561)class_2561.method_43469((String)"tooltip.runic_attunement.reforge_upgrade_chance", (Object[])new Object[]{percent, RunicPotency.name(dustTier)}).method_27692(this.tierFormatting(dustTier)));
                tooltip.add((class_2561)class_2561.method_43469((String)"tooltip.runic_attunement.reforge_failure_tier", (Object[])new Object[]{100 - percent, RunicPotency.name(startTier)}).method_27692(class_124.field_1063));
            } else {
                tooltip.add((class_2561)class_2561.method_43469((String)"tooltip.runic_attunement.reforge_same_tier", (Object[])new Object[]{RunicPotency.name(startTier)}).method_27692(this.tierFormatting(startTier)));
            }
            EnchantedTooltipsBridge.appendTabletTooltip(stack, tooltip);
            return;
        }
        Optional<class_2960> enchantmentId = RunicTabletItem.getEnchantmentId(stack);
        if (enchantmentId.isEmpty()) {
            class_2487 nbt = stack.method_7969();
            boolean malformed = nbt != null && nbt.method_10545(NBT_ROOT) && nbt.method_10562(NBT_ROOT).method_10545(NBT_ENCHANTMENT);
            tooltip.add((class_2561)class_2561.method_43471((String)(malformed ? "tooltip.runic_attunement.invalid_data" : "tooltip.runic_attunement.unattuned")).method_27692(malformed ? class_124.field_1061 : class_124.field_1063));
            EnchantedTooltipsBridge.appendTabletTooltip(stack, tooltip);
            return;
        }
        Optional enchantment = class_7923.field_41176.method_17966(enchantmentId.get());
        if (enchantment.isPresent()) {
            tooltip.add((class_2561)class_2561.method_43469((String)"tooltip.runic_attunement.attuned_to", (Object[])new Object[]{class_2561.method_43471((String)((class_1887)enchantment.get()).method_8184())}).method_27692(RunicPotency.formatting(this.tier)));
            tooltip.add((class_2561)class_2561.method_43473());
            tooltip.add((class_2561)class_2561.method_43471((String)"tooltip.runic_attunement.table_use").method_27692(class_124.field_1080));
            tooltip.add((class_2561)class_2561.method_43471((String)"tooltip.runic_attunement.hold_shift").method_27695(new class_124[]{class_124.field_1063, class_124.field_1056}));
        } else {
            tooltip.add((class_2561)class_2561.method_43471((String)"tooltip.runic_attunement.invalid_data").method_27692(class_124.field_1061));
            tooltip.add((class_2561)class_2561.method_43470((String)enchantmentId.get().toString()).method_27692(class_124.field_1079));
        }
        EnchantedTooltipsBridge.appendTabletTooltip(stack, tooltip);
    }

    private class_124 tierFormatting(int displayTier) {
        return RunicPotency.formatting(displayTier);
    }
}

