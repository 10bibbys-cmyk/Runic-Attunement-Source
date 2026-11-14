/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
 *  net.fabricmc.fabric.api.resource.ResourceManagerHelper
 *  net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
 *  net.minecraft.class_1887
 *  net.minecraft.class_2960
 *  net.minecraft.class_3264
 *  net.minecraft.class_3298
 *  net.minecraft.class_3300
 *  net.minecraft.class_7923
 */
package dev.runicattunement.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.runicattunement.RunicAttunement;
import dev.runicattunement.config.RunicConfig;
import dev.runicattunement.data.CostKind;
import dev.runicattunement.data.EnchantmentDefinition;
import dev.runicattunement.data.IngredientCost;
import dev.runicattunement.data.LevelCost;
import dev.runicattunement.data.TransmutationCosts;
import dev.runicattunement.util.RunicExperience;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.class_1887;
import net.minecraft.class_2960;
import net.minecraft.class_3264;
import net.minecraft.class_3298;
import net.minecraft.class_3300;
import net.minecraft.class_7923;

public final class RunicDefinitionManager {
    private static final Gson GSON = new GsonBuilder().setLenient().create();
    private static final String ENCHANTMENT_PATH = "runic_attunement/enchantments";
    private static final class_2960 GLOBAL_COSTS = RunicAttunement.id("runic_attunement/global_costs.json");
    private static final class_2960 TRANSMUTATION_COSTS = RunicAttunement.id("runic_attunement/transmutation_costs.json");
    private static final class_2960 LAPIS_LAZULI = new class_2960("minecraft", "lapis_lazuli");
    private static volatile class_3300 loadedFrom;
    private static volatile Map<class_2960, EnchantmentDefinition> definitions;
    private static volatile Map<Integer, List<IngredientCost>> universalCosts;
    private static volatile TransmutationCosts transmutationCosts;

    private RunicDefinitionManager() {
    }

    public static void registerReloadListener() {
        ResourceManagerHelper.get((class_3264)class_3264.field_14190).registerReloadListener((IdentifiableResourceReloadListener)new SimpleSynchronousResourceReloadListener(){

            public class_2960 getFabricId() {
                return RunicAttunement.id("definitions");
            }

            public void method_14491(class_3300 manager) {
                RunicDefinitionManager.ensureLoaded(manager);
            }
        });
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void ensureLoaded(class_3300 manager) {
        if (loadedFrom == manager) {
            return;
        }
        Class<RunicDefinitionManager> clazz = RunicDefinitionManager.class;
        synchronized (RunicDefinitionManager.class) {
            if (loadedFrom == manager) {
                // ** MonitorExit[var1_1] (shouldn't be in output)
                return;
            }
            RunicDefinitionManager.reloadDefinitions(manager);
            loadedFrom = manager;
            // ** MonitorExit[var1_1] (shouldn't be in output)
            return;
        }
    }

    private static void reloadDefinitions(class_3300 manager) {
        RunicConfig.reload();
        Map<Integer, List<IngredientCost>> nextUniversal = RunicDefinitionManager.loadUniversalCosts(manager);
        TransmutationCosts nextTransmutationCosts = RunicDefinitionManager.loadTransmutationCosts(manager);
        LinkedHashMap<class_2960, EnchantmentDefinition> nextDefinitions = new LinkedHashMap<class_2960, EnchantmentDefinition>();
        int blacklisted = 0;
        for (Map.Entry entry : manager.method_14488(ENCHANTMENT_PATH, id -> id.method_12832().endsWith(".json")).entrySet()) {
            class_3298 resource = (class_3298)entry.getValue();
            try (InputStreamReader reader = new InputStreamReader(resource.method_14482(), StandardCharsets.UTF_8);){
                JsonObject root = (JsonObject)GSON.fromJson((Reader)reader, JsonObject.class);
                if (root == null || !root.has("enchantment")) {
                    throw new IllegalArgumentException("Missing required 'enchantment' id");
                }
                class_2960 configuredId = new class_2960(root.get("enchantment").getAsString());
                if (RunicConfig.isBlacklisted(configuredId)) {
                    ++blacklisted;
                    if (!RunicConfig.debugLogging()) continue;
                    RunicAttunement.LOGGER.info("Skipping blacklisted Runic Attunement definition {} from {}", (Object)configuredId, entry.getKey());
                    continue;
                }
                EnchantmentDefinition definition = RunicDefinitionManager.parseDefinition(root);
                RunicDefinitionManager.validateDefinition(definition, nextUniversal);
                if (nextDefinitions.put(definition.enchantmentId(), definition) != null) {
                    RunicAttunement.LOGGER.warn("Duplicate Runic Attunement definition for {}; later resource wins", (Object)definition.enchantmentId());
                }
                if (!RunicConfig.debugLogging()) continue;
                RunicAttunement.LOGGER.info("Registered {} in Runic Tablet Tier {} from {}", new Object[]{definition.enchantmentId(), definition.tabletTier(), entry.getKey()});
            }
            catch (Exception exception) {
                RunicAttunement.LOGGER.error("Failed to load Runic Attunement definition {}", entry.getKey(), (Object)exception);
            }
        }
        definitions = Collections.unmodifiableMap(nextDefinitions);
        universalCosts = Collections.unmodifiableMap(nextUniversal);
        transmutationCosts = nextTransmutationCosts;
        RunicAttunement.LOGGER.info("Loaded {} Runic Attunement enchantment definition(s): Tier 1 = {}, Tier 2 = {}, Tier 3 = {}{}", new Object[]{definitions.size(), RunicDefinitionManager.tierPool(1).size(), RunicDefinitionManager.tierPool(2).size(), RunicDefinitionManager.tierPool(3).size(), blacklisted == 0 ? "" : "; blacklisted = " + blacklisted});
    }

    private static Map<Integer, List<IngredientCost>> loadUniversalCosts(class_3300 manager) {
        LinkedHashMap<Integer, List<IngredientCost>> linkedHashMap;
        class_3298 resource = manager.method_14486(GLOBAL_COSTS).orElse(null);
        if (resource == null) {
            RunicAttunement.LOGGER.error("Missing required Runic Attunement global costs resource {}", (Object)GLOBAL_COSTS);
            return Map.of();
        }
        LinkedHashMap<Integer, List<IngredientCost>> result = new LinkedHashMap<Integer, List<IngredientCost>>();
        InputStreamReader reader = new InputStreamReader(resource.method_14482(), StandardCharsets.UTF_8);
        try {
            JsonObject root = (JsonObject)GSON.fromJson((Reader)reader, JsonObject.class);
            JsonArray levels = root.getAsJsonArray("levels");
            if (levels == null) {
                throw new IllegalArgumentException("Missing 'levels' array in global_costs.json");
            }
            for (JsonElement element : levels) {
                JsonObject level = element.getAsJsonObject();
                int targetLevel = level.get("level").getAsInt();
                if (targetLevel <= 0) {
                    throw new IllegalArgumentException("Invalid global target level " + targetLevel);
                }
                List<IngredientCost> costs = RunicDefinitionManager.parseIngredients(level.getAsJsonArray("ingredients"), CostKind.UNIVERSAL);
                for (IngredientCost cost : costs) {
                    if (class_7923.field_41178.method_10250(cost.itemId())) continue;
                    throw new IllegalArgumentException("Unknown global ingredient item " + String.valueOf(cost.itemId()));
                }
                if (result.put(targetLevel, List.copyOf(costs)) == null) continue;
                throw new IllegalArgumentException("Duplicate global target level " + targetLevel);
            }
            linkedHashMap = result;
        }
        catch (Throwable throwable) {
            try {
                try {
                    ((Reader)reader).close();
                }
                catch (Throwable throwable2) {
                    throwable.addSuppressed(throwable2);
                }
                throw throwable;
            }
            catch (Exception exception) {
                RunicAttunement.LOGGER.error("Failed to load Runic Attunement global costs; disabling definitions for this reload", (Throwable)exception);
                return Map.of();
            }
        }
        ((Reader)reader).close();
        return linkedHashMap;
    }

    private static TransmutationCosts loadTransmutationCosts(class_3300 manager) {
        TransmutationCosts transmutationCosts;
        class_3298 resource = manager.method_14486(TRANSMUTATION_COSTS).orElse(null);
        if (resource == null) {
            RunicAttunement.LOGGER.error("Missing required Runic Attunement transmutation costs resource {}", (Object)TRANSMUTATION_COSTS);
            return TransmutationCosts.unavailable();
        }
        InputStreamReader reader = new InputStreamReader(resource.method_14482(), StandardCharsets.UTF_8);
        try {
            JsonObject root = (JsonObject)GSON.fromJson((Reader)reader, JsonObject.class);
            if (root == null) {
                throw new IllegalArgumentException("Transmutation costs root is empty");
            }
            List<IngredientCost> materials = RunicDefinitionManager.parseTransmutationMaterials(root.getAsJsonArray("materials"));
            List<IngredientCost> curseRetention = RunicDefinitionManager.parseIngredients(root.getAsJsonArray("curse_retention"), CostKind.UNIVERSAL);
            if (materials.isEmpty()) {
                throw new IllegalArgumentException("Transmutation costs require at least one material");
            }
            RunicDefinitionManager.validateIngredientItems(materials, "transmutation materials");
            RunicDefinitionManager.validateIngredientItems(curseRetention, "curse retention");
            transmutationCosts = new TransmutationCosts(materials, curseRetention);
        }
        catch (Throwable throwable) {
            try {
                try {
                    ((Reader)reader).close();
                }
                catch (Throwable throwable2) {
                    throwable.addSuppressed(throwable2);
                }
                throw throwable;
            }
            catch (Exception exception) {
                RunicAttunement.LOGGER.error("Failed to load Runic Attunement transmutation costs; Transmutation will be unavailable for this reload", (Throwable)exception);
                return TransmutationCosts.unavailable();
            }
        }
        ((Reader)reader).close();
        return transmutationCosts;
    }

    private static List<IngredientCost> parseTransmutationMaterials(JsonArray array) {
        if (array == null) {
            return List.of();
        }
        ArrayList<IngredientCost> result = new ArrayList<IngredientCost>();
        for (JsonElement element : array) {
            JsonObject ingredient = element.getAsJsonObject();
            class_2960 itemId = new class_2960(ingredient.get("item").getAsString());
            int count = ingredient.get("count_per_unit").getAsInt();
            result.add(new IngredientCost(itemId, count, CostKind.UNIVERSAL));
        }
        return List.copyOf(result);
    }

    private static void validateIngredientItems(List<IngredientCost> costs, String context) {
        for (IngredientCost cost : costs) {
            if (class_7923.field_41178.method_10250(cost.itemId())) continue;
            throw new IllegalArgumentException("Unknown " + context + " item " + String.valueOf(cost.itemId()));
        }
    }

    private static EnchantmentDefinition parseDefinition(JsonObject root) {
        class_2960 enchantmentId = new class_2960(root.get("enchantment").getAsString());
        int tabletTier = root.get("tablet_tier").getAsInt();
        LinkedHashMap<Integer, LevelCost> levels = new LinkedHashMap<Integer, LevelCost>();
        JsonArray levelsJson = root.getAsJsonArray("levels");
        if (levelsJson == null || levelsJson.size() == 0) {
            throw new IllegalArgumentException("Definition " + String.valueOf(enchantmentId) + " has no levels");
        }
        for (JsonElement element : levelsJson) {
            int experiencePoints;
            JsonObject levelObject = element.getAsJsonObject();
            int targetLevel = levelObject.get("level").getAsInt();
            if (targetLevel <= 0) {
                throw new IllegalArgumentException("Invalid target level " + targetLevel + " for " + String.valueOf(enchantmentId));
            }
            int n = experiencePoints = levelObject.has("experience_points") ? levelObject.get("experience_points").getAsInt() : RunicExperience.defaultCost(tabletTier, targetLevel);
            if (experiencePoints < 0) {
                throw new IllegalArgumentException("Negative experience_points for " + String.valueOf(enchantmentId) + " level " + targetLevel);
            }
            List<IngredientCost> ingredients = RunicDefinitionManager.parseIngredients(levelObject.getAsJsonArray("ingredients"), CostKind.THEMATIC);
            if (levels.put(targetLevel, new LevelCost(targetLevel, experiencePoints, ingredients)) == null) continue;
            throw new IllegalArgumentException("Duplicate target level " + targetLevel + " for " + String.valueOf(enchantmentId));
        }
        return new EnchantmentDefinition(enchantmentId, tabletTier, levels);
    }

    private static void validateDefinition(EnchantmentDefinition definition, Map<Integer, List<IngredientCost>> universal) {
        class_1887 enchantment = (class_1887)class_7923.field_41176.method_17966(definition.enchantmentId()).orElseThrow(() -> new IllegalArgumentException("Unknown enchantment " + String.valueOf(definition.enchantmentId())));
        if (definition.tabletTier() < 1 || definition.tabletTier() > 3) {
            throw new IllegalArgumentException(String.valueOf(definition.enchantmentId()) + " has invalid tablet_tier " + definition.tabletTier() + "; expected 1-3");
        }
        int expectedLevel = 1;
        for (Integer n : definition.levels().keySet().stream().sorted().toList()) {
            if (n != expectedLevel) {
                throw new IllegalArgumentException(String.valueOf(definition.enchantmentId()) + " has a missing/non-contiguous level; expected " + expectedLevel + " but found " + n);
            }
            ++expectedLevel;
        }
        for (Map.Entry entry : definition.levels().entrySet()) {
            int level = (Integer)entry.getKey();
            if (level > enchantment.method_8183()) {
                throw new IllegalArgumentException(String.valueOf(definition.enchantmentId()) + " configures level " + level + " above vanilla/mod maximum " + enchantment.method_8183());
            }
            boolean hasLapis = universal.getOrDefault(level, List.of()).stream().anyMatch(cost -> cost.itemId().equals((Object)LAPIS_LAZULI) && cost.count() > 0);
            if (!hasLapis) {
                throw new IllegalArgumentException(String.valueOf(definition.enchantmentId()) + " level " + level + " has no universal Lapis Lazuli cost");
            }
            for (IngredientCost ingredient : ((LevelCost)entry.getValue()).ingredients()) {
                if (class_7923.field_41178.method_10250(ingredient.itemId())) continue;
                throw new IllegalArgumentException("Unknown ingredient item " + String.valueOf(ingredient.itemId()) + " in " + String.valueOf(definition.enchantmentId()) + " level " + level);
            }
        }
    }

    private static List<IngredientCost> parseIngredients(JsonArray array, CostKind defaultKind) {
        if (array == null) {
            return List.of();
        }
        ArrayList<IngredientCost> result = new ArrayList<IngredientCost>();
        for (JsonElement element : array) {
            JsonObject ingredient = element.getAsJsonObject();
            class_2960 itemId = new class_2960(ingredient.get("item").getAsString());
            int count = ingredient.get("count").getAsInt();
            CostKind kind = ingredient.has("kind") ? CostKind.fromString(ingredient.get("kind").getAsString()) : defaultKind;
            result.add(new IngredientCost(itemId, count, kind));
        }
        return result;
    }

    public static Map<class_2960, EnchantmentDefinition> snapshot() {
        return definitions;
    }

    public static EnchantmentDefinition get(class_2960 id) {
        return definitions.get(id);
    }

    public static boolean isEnabled(class_2960 id) {
        return definitions.containsKey(id);
    }

    public static List<class_2960> tierPool(int tier) {
        if (tier < 1 || tier > 3) {
            return List.of();
        }
        return definitions.values().stream().filter(definition -> definition.tabletTier() == tier).map(EnchantmentDefinition::enchantmentId).sorted().toList();
    }

    public static Map<Integer, List<IngredientCost>> universalSnapshot() {
        return universalCosts;
    }

    public static TransmutationCosts transmutationCostsSnapshot() {
        return transmutationCosts;
    }

    public static List<IngredientCost> combinedCosts(EnchantmentDefinition definition, int targetLevel) {
        ArrayList merged = new ArrayList();
        merged.addAll(universalCosts.getOrDefault(targetLevel, List.of()));
        definition.level(targetLevel).ifPresent(levelCost -> merged.addAll(levelCost.ingredients()));
        return List.copyOf(merged);
    }

    static {
        definitions = Map.of();
        universalCosts = Map.of();
        transmutationCosts = TransmutationCosts.unavailable();
    }
}

