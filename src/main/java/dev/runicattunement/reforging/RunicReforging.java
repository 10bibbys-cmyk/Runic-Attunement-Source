/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_156
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_1935
 *  net.minecraft.class_2960
 *  net.minecraft.class_5819
 */
package dev.runicattunement.reforging;

import dev.runicattunement.config.RunicConfig;
import dev.runicattunement.data.RunicDefinitionManager;
import dev.runicattunement.item.RunicDustItem;
import dev.runicattunement.item.RunicTabletItem;
import dev.runicattunement.registry.ModItems;
import java.util.ArrayList;
import java.util.Optional;
import net.minecraft.class_156;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1935;
import net.minecraft.class_2960;
import net.minecraft.class_5819;

public final class RunicReforging {
    private RunicReforging() {
    }

    public static boolean isReclaimableTablet(class_1799 stack) {
        return stack.method_7909() instanceof RunicTabletItem && RunicTabletItem.getEnchantmentId(stack).isPresent();
    }

    public static class_1799 createDustResult(class_1799 tabletStack) {
        if (!RunicConfig.tabletReforging().enabled() || !RunicReforging.isReclaimableTablet(tabletStack)) {
            return class_1799.field_8037;
        }
        int tier = ((RunicTabletItem)tabletStack.method_7909()).tier();
        return new class_1799((class_1935)ModItems.dustByTier(tier));
    }

    public static boolean isRunicReforge(class_1799 tabletStack, class_1799 dustStack) {
        if (!RunicConfig.tabletReforging().enabled()) {
            return false;
        }
        if (!RunicTabletItem.isConfiguredTablet(tabletStack)) {
            return false;
        }
        class_1792 class_17922 = dustStack.method_7909();
        if (!(class_17922 instanceof RunicDustItem)) {
            return false;
        }
        RunicDustItem dust = (RunicDustItem)class_17922;
        class_1792 class_17923 = tabletStack.method_7909();
        if (!(class_17923 instanceof RunicTabletItem)) {
            return false;
        }
        RunicTabletItem tablet = (RunicTabletItem)class_17923;
        int tabletTier = tablet.tier();
        int dustTier = dust.tier();
        if (dustTier < tabletTier || dustTier > 3) {
            return false;
        }
        if (RunicDefinitionManager.tierPool(tabletTier).isEmpty()) {
            return false;
        }
        return dustTier == tabletTier || !RunicDefinitionManager.tierPool(dustTier).isEmpty();
    }

    public static class_1799 createPreview(class_1799 tabletStack, class_1799 dustStack) {
        Optional<ReforgeOdds> odds = RunicReforging.odds(tabletStack, dustStack);
        if (odds.isEmpty()) {
            return class_1799.field_8037;
        }
        ReforgeOdds value = odds.get();
        class_1799 preview = new class_1799((class_1935)ModItems.byTier(value.startTier()));
        RunicTabletItem.setReforgePreview(preview, value.startTier(), value.upgradeTier(), value.upgradeChance());
        return preview;
    }

    public static class_1799 createRandomResult(class_1799 tabletStack, class_1799 dustStack, class_5819 random) {
        ArrayList<class_2960> candidates;
        if (!RunicReforging.isRunicReforge(tabletStack, dustStack)) {
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
        if ((candidates = new ArrayList<class_2960>(RunicDefinitionManager.tierPool(resultTier))).isEmpty()) {
            return class_1799.field_8037;
        }
        class_2960 currentId = RunicTabletItem.getEnchantmentId(tabletStack).orElse(null);
        if (resultTier == startTier && currentId != null && candidates.size() > 1) {
            candidates.remove(currentId);
        }
        if (candidates.isEmpty()) {
            return class_1799.field_8037;
        }
        class_2960 resultId = (class_2960)class_156.method_32309(candidates, (class_5819)random);
        class_1799 result = new class_1799((class_1935)ModItems.byTier(resultTier));
        RunicTabletItem.setEnchantment(result, resultId);
        return result;
    }

    public static Optional<ReforgeOdds> odds(class_1799 tabletStack, class_1799 dustStack) {
        if (!RunicReforging.isRunicReforge(tabletStack, dustStack)) {
            return Optional.empty();
        }
        int startTier = ((RunicTabletItem)tabletStack.method_7909()).tier();
        int dustTier = ((RunicDustItem)dustStack.method_7909()).tier();
        double chance = dustTier == startTier ? 1.0 : RunicConfig.tabletReforging().upgradeChance(startTier, dustTier);
        return Optional.of(new ReforgeOdds(startTier, dustTier, chance));
    }

    public record ReforgeOdds(int startTier, int upgradeTier, double upgradeChance) {
    }
}

