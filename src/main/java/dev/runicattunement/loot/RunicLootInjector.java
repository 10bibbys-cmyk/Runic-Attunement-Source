/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.loot.v2.LootTableEvents
 *  net.minecraft.class_1935
 *  net.minecraft.class_219
 *  net.minecraft.class_44
 *  net.minecraft.class_55
 *  net.minecraft.class_55$class_56
 *  net.minecraft.class_5658
 *  net.minecraft.class_77
 *  net.minecraft.class_79$class_80
 */
package dev.runicattunement.loot;

import dev.runicattunement.RunicAttunement;
import dev.runicattunement.config.RunicConfig;
import dev.runicattunement.data.RunicDefinitionManager;
import dev.runicattunement.loot.GenerateRunicTabletLootFunction;
import dev.runicattunement.loot.LootProfile;
import dev.runicattunement.loot.LootProfileManager;
import dev.runicattunement.registry.ModItems;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.class_1935;
import net.minecraft.class_219;
import net.minecraft.class_44;
import net.minecraft.class_55;
import net.minecraft.class_5658;
import net.minecraft.class_77;
import net.minecraft.class_79;

public final class RunicLootInjector {
    private RunicLootInjector() {
    }

    public static void initialize() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            RunicDefinitionManager.ensureLoaded(resourceManager);
            LootProfileManager.ensureLoaded(resourceManager);
            RunicConfig.LootGeneration settings = RunicConfig.lootGeneration();
            if (!settings.enabled()) {
                return;
            }
            LootProfile profile = LootProfileManager.get(id);
            if (profile == null || !profile.enabled()) {
                return;
            }
            double effectiveChance = settings.effectiveChance(profile.tabletChance());
            if (effectiveChance <= 0.0) {
                return;
            }
            class_55.class_56 pool = class_55.method_347().method_352((class_5658)class_44.method_32448((float)1.0f)).method_356(class_219.method_932((float)((float)effectiveChance)));
            int entries = 0;
            for (int tier = 1; tier <= 3; ++tier) {
                double configuredWeight = settings.tierWeights().weightFor(tier);
                if (configuredWeight <= 0.0 || RunicDefinitionManager.tierPool(tier).isEmpty()) continue;
                long scaledWeight = Math.round(Math.min(configuredWeight, 2147483.647) * 1000.0);
                int lootWeight = (int)Math.max(1L, Math.min(Integer.MAX_VALUE, scaledWeight));
                pool.method_351((class_79.class_80)class_77.method_411((class_1935)ModItems.byTier(tier)).method_437(lootWeight).method_438(GenerateRunicTabletLootFunction.builder()));
                ++entries;
            }
            if (entries == 0) {
                return;
            }
            tableBuilder.method_336(pool);
            if (RunicConfig.debugLogging()) {
                RunicAttunement.LOGGER.info("Injected Runic Tablet roll into {} from profile {} at effective chance {}", new Object[]{id, profile.sourceResource(), effectiveChance});
            }
        });
    }
}

