package dev.runicattunement.loot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.runicattunement.RunicAttunement;
import dev.runicattunement.config.RunicConfig;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.class_2960;
import net.minecraft.class_3298;
import net.minecraft.class_3300;

public final class LootProfileManager {
   private static final Gson GSON = new GsonBuilder().setLenient().create();
   private static final String PROFILE_PATH = "runic_attunement/loot_profiles";
   private static volatile class_3300 loadedFrom;
   private static volatile Map<class_2960, LootProfile> profiles = Map.of();

   private LootProfileManager() {
   }

   public static void ensureLoaded(class_3300 manager) {
      if (loadedFrom != manager) {
         synchronized (LootProfileManager.class) {
            if (loadedFrom != manager) {
               reload(manager);
               loadedFrom = manager;
            }
         }
      }
   }

   private static void reload(class_3300 manager) {
      RunicConfig.reload();
      Map<class_2960, LootProfile> next = new LinkedHashMap<>();
      Map<class_2960, class_2960> sourceByTarget = new LinkedHashMap<>();
      Set<class_2960> ambiguousTargets = new HashSet<>();
      List<Entry<class_2960, class_3298>> resources = new ArrayList<>(
         manager.method_14488("runic_attunement/loot_profiles", id -> id.method_12832().endsWith(".json")).entrySet()
      );
      resources.sort(Comparator.comparing(entry -> entry.getKey().toString()));

      for (Entry<class_2960, class_3298> entry : resources) {
         class_2960 resourceId = entry.getKey();

         try (Reader reader = new InputStreamReader(entry.getValue().method_14482(), StandardCharsets.UTF_8)) {
            JsonObject root = (JsonObject)GSON.fromJson(reader, JsonObject.class);
            if (root == null) {
               throw new IllegalArgumentException("Profile root is empty");
            }

            if (!root.has("loot_table")) {
               throw new IllegalArgumentException("Missing required 'loot_table' id");
            }

            class_2960 lootTableId = new class_2960(root.get("loot_table").getAsString());
            boolean enabled = !root.has("enabled") || root.get("enabled").getAsBoolean();
            double chance = root.has("tablet_chance") ? root.get("tablet_chance").getAsDouble() : 0.0;
            if (enabled && !root.has("tablet_chance")) {
               throw new IllegalArgumentException("Enabled profile is missing required 'tablet_chance'");
            }

            if (!Double.isFinite(chance) || chance < 0.0 || chance > 1.0) {
               throw new IllegalArgumentException("tablet_chance must be between 0.0 and 1.0 inclusive; found " + chance);
            }

            class_2960 previousSource = sourceByTarget.putIfAbsent(lootTableId, resourceId);
            if (previousSource != null) {
               ambiguousTargets.add(lootTableId);
               RunicAttunement.LOGGER
                  .error(
                     "Duplicate Runic Attunement loot profiles target {} from {} and {}. Override the same resource path instead; injection for this loot table is disabled for this reload.",
                     new Object[]{lootTableId, previousSource, resourceId}
                  );
            } else {
               next.put(lootTableId, new LootProfile(resourceId, lootTableId, enabled, chance));
            }
         } catch (Exception exception) {
            RunicAttunement.LOGGER.error("Failed to load Runic Attunement loot profile {}", resourceId, exception);
         }
      }

      for (class_2960 ambiguous : ambiguousTargets) {
         next.remove(ambiguous);
      }

      profiles = Collections.unmodifiableMap(next);
      long enabledCount = profiles.values().stream().filter(LootProfile::enabled).count();
      RunicAttunement.LOGGER
         .info(
            "Loaded {} Runic Attunement loot profile(s): {} enabled, {} disabled{}",
            new Object[]{
               profiles.size(),
               enabledCount,
               profiles.size() - enabledCount,
               ambiguousTargets.isEmpty() ? "" : "; " + ambiguousTargets.size() + " ambiguous target(s) disabled"
            }
         );
   }

   public static LootProfile get(class_2960 lootTableId) {
      return profiles.get(lootTableId);
   }

   public static Map<class_2960, LootProfile> snapshot() {
      return profiles;
   }
}
