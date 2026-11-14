package dev.runicattunement.loot;

import dev.runicattunement.RunicAttunement;
import dev.runicattunement.config.RunicConfig;
import dev.runicattunement.data.RunicDefinitionManager;
import dev.runicattunement.registry.ModItems;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents.Modify;
import net.minecraft.class_219;
import net.minecraft.class_44;
import net.minecraft.class_55;
import net.minecraft.class_77;
import net.minecraft.class_55.class_56;

public final class RunicLootInjector {
   private RunicLootInjector() {
   }

   public static void initialize() {
      LootTableEvents.MODIFY
         .register(
            (Modify)(resourceManager, lootManager, id, tableBuilder, source) -> {
               RunicDefinitionManager.ensureLoaded(resourceManager);
               LootProfileManager.ensureLoaded(resourceManager);
               RunicConfig.LootGeneration settings = RunicConfig.lootGeneration();
               if (settings.enabled()) {
                  LootProfile profile = LootProfileManager.get(id);
                  if (profile != null && profile.enabled()) {
                     double effectiveChance = settings.effectiveChance(profile.tabletChance());
                     if (!(effectiveChance <= 0.0)) {
                        class_56 pool = class_55.method_347().method_352(class_44.method_32448(1.0F)).method_356(class_219.method_932((float)effectiveChance));
                        int entries = 0;

                        for (int tier = 1; tier <= 3; tier++) {
                           double configuredWeight = settings.tierWeights().weightFor(tier);
                           if (!(configuredWeight <= 0.0) && !RunicDefinitionManager.tierPool(tier).isEmpty()) {
                              long scaledWeight = Math.round(Math.min(configuredWeight, 2147483.647) * 1000.0);
                              int lootWeight = (int)Math.max(1L, Math.min(2147483647L, scaledWeight));
                              pool.method_351(
                                 class_77.method_411(ModItems.byTier(tier)).method_437(lootWeight).method_438(GenerateRunicTabletLootFunction.builder())
                              );
                              entries++;
                           }
                        }

                        if (entries != 0) {
                           tableBuilder.method_336(pool);
                           if (RunicConfig.debugLogging()) {
                              RunicAttunement.LOGGER
                                 .info(
                                    "Injected Runic Tablet roll into {} from profile {} at effective chance {}",
                                    new Object[]{id, profile.sourceResource(), effectiveChance}
                                 );
                           }
                        }
                     }
                  }
               }
            }
         );
   }
}
