/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonObject
 *  net.minecraft.class_2960
 *  net.minecraft.class_3298
 *  net.minecraft.class_3300
 */
package dev.runicattunement.loot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.runicattunement.RunicAttunement;
import dev.runicattunement.config.RunicConfig;
import dev.runicattunement.loot.LootProfile;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.class_2960;
import net.minecraft.class_3298;
import net.minecraft.class_3300;

public final class LootProfileManager {
    private static final Gson GSON = new GsonBuilder().setLenient().create();
    private static final String PROFILE_PATH = "runic_attunement/loot_profiles";
    private static volatile class_3300 loadedFrom;
    private static volatile Map<class_2960, LootProfile> profiles;

    private LootProfileManager() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void ensureLoaded(class_3300 manager) {
        if (loadedFrom == manager) {
            return;
        }
        Class<LootProfileManager> clazz = LootProfileManager.class;
        synchronized (LootProfileManager.class) {
            if (loadedFrom == manager) {
                // ** MonitorExit[var1_1] (shouldn't be in output)
                return;
            }
            LootProfileManager.reload(manager);
            loadedFrom = manager;
            // ** MonitorExit[var1_1] (shouldn't be in output)
            return;
        }
    }

    private static void reload(class_3300 manager) {
        RunicConfig.reload();
        LinkedHashMap<class_2960, LootProfile> next = new LinkedHashMap<class_2960, LootProfile>();
        LinkedHashMap<class_2960, class_2960> sourceByTarget = new LinkedHashMap<class_2960, class_2960>();
        HashSet<class_2960> ambiguousTargets = new HashSet<class_2960>();
        ArrayList resources = new ArrayList(manager.method_14488(PROFILE_PATH, id -> id.method_12832().endsWith(".json")).entrySet());
        resources.sort(Comparator.comparing(entry -> ((class_2960)entry.getKey()).toString()));
        for (Map.Entry entry2 : resources) {
            class_2960 resourceId = (class_2960)entry2.getKey();
            try (InputStreamReader reader = new InputStreamReader(((class_3298)entry2.getValue()).method_14482(), StandardCharsets.UTF_8);){
                double chance;
                JsonObject root = (JsonObject)GSON.fromJson((Reader)reader, JsonObject.class);
                if (root == null) {
                    throw new IllegalArgumentException("Profile root is empty");
                }
                if (!root.has("loot_table")) {
                    throw new IllegalArgumentException("Missing required 'loot_table' id");
                }
                class_2960 lootTableId = new class_2960(root.get("loot_table").getAsString());
                boolean enabled = !root.has("enabled") || root.get("enabled").getAsBoolean();
                double d = chance = root.has("tablet_chance") ? root.get("tablet_chance").getAsDouble() : 0.0;
                if (enabled && !root.has("tablet_chance")) {
                    throw new IllegalArgumentException("Enabled profile is missing required 'tablet_chance'");
                }
                if (!Double.isFinite(chance) || chance < 0.0 || chance > 1.0) {
                    throw new IllegalArgumentException("tablet_chance must be between 0.0 and 1.0 inclusive; found " + chance);
                }
                class_2960 previousSource = sourceByTarget.putIfAbsent(lootTableId, resourceId);
                if (previousSource != null) {
                    ambiguousTargets.add(lootTableId);
                    RunicAttunement.LOGGER.error("Duplicate Runic Attunement loot profiles target {} from {} and {}. Override the same resource path instead; injection for this loot table is disabled for this reload.", new Object[]{lootTableId, previousSource, resourceId});
                    continue;
                }
                next.put(lootTableId, new LootProfile(resourceId, lootTableId, enabled, chance));
            }
            catch (Exception exception) {
                RunicAttunement.LOGGER.error("Failed to load Runic Attunement loot profile {}", (Object)resourceId, (Object)exception);
            }
        }
        for (class_2960 class_29602 : ambiguousTargets) {
            next.remove(class_29602);
        }
        profiles = Collections.unmodifiableMap(next);
        long enabledCount = profiles.values().stream().filter(LootProfile::enabled).count();
        RunicAttunement.LOGGER.info("Loaded {} Runic Attunement loot profile(s): {} enabled, {} disabled{}", new Object[]{profiles.size(), enabledCount, (long)profiles.size() - enabledCount, ambiguousTargets.isEmpty() ? "" : "; " + ambiguousTargets.size() + " ambiguous target(s) disabled"});
    }

    public static LootProfile get(class_2960 lootTableId) {
        return profiles.get(lootTableId);
    }

    public static Map<class_2960, LootProfile> snapshot() {
        return profiles;
    }

    static {
        profiles = Map.of();
    }
}

