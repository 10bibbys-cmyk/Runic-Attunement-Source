/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  net.fabricmc.loader.api.FabricLoader
 *  net.minecraft.class_2960
 */
package dev.runicattunement.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.runicattunement.RunicAttunement;
import dev.runicattunement.unravel.RunicUnravelRuntime;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_2960;

public final class RunicConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_NAME = "runic_attunement.json";
    private static volatile ConfigState state = ConfigState.defaults();

    private RunicConfig() {
    }

    public static void initialize() {
        RunicConfig.ensureExists();
        RunicConfig.reload();
    }

    public static void reload() {
        Path path = RunicConfig.path();
        if (!Files.exists(path, new LinkOption[0])) {
            RunicConfig.ensureExists();
        }
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);){
            JsonObject root = (JsonObject)GSON.fromJson((Reader)reader, JsonObject.class);
            if (root == null) {
                throw new IllegalArgumentException("Config root is empty");
            }
            boolean migrated = RunicConfig.ensureCurrentSchema(root);
            LinkedHashSet<class_2960> blacklist = new LinkedHashSet<class_2960>();
            JsonArray blacklisted = root.getAsJsonArray("blacklisted_enchantments");
            if (blacklisted != null) {
                for (JsonElement element : blacklisted) {
                    try {
                        blacklist.add(new class_2960(element.getAsString()));
                    }
                    catch (RuntimeException exception) {
                        RunicAttunement.LOGGER.warn("Ignoring invalid blacklisted enchantment id '{}'", (Object)element, (Object)exception);
                    }
                }
            }
            boolean debugLogging = RunicConfig.getBoolean(root, "debug_logging", false);
            JsonObject loot = root.getAsJsonObject("loot_generation");
            boolean lootEnabled = RunicConfig.getBoolean(loot, "enabled", true);
            double multiplier = RunicConfig.nonNegativeFinite(RunicConfig.getDouble(loot, "global_drop_chance_multiplier", 1.0), 1.0, "loot_generation.global_drop_chance_multiplier");
            JsonObject weights = loot == null ? null : loot.getAsJsonObject("tier_weights");
            TierWeights tierWeights = new TierWeights(RunicConfig.nonNegativeFinite(RunicConfig.getDouble(weights, "tier_1", 65.0), 65.0, "loot_generation.tier_weights.tier_1"), RunicConfig.nonNegativeFinite(RunicConfig.getDouble(weights, "tier_2", 30.0), 30.0, "loot_generation.tier_weights.tier_2"), RunicConfig.nonNegativeFinite(RunicConfig.getDouble(weights, "tier_3", 5.0), 5.0, "loot_generation.tier_weights.tier_3"));
            if (tierWeights.total() <= 0.0) {
                RunicAttunement.LOGGER.warn("All Runic Attunement loot tier weights are zero; loot profiles will not generate Tablets");
            }
            LootGeneration lootGeneration = new LootGeneration(lootEnabled, multiplier, tierWeights);
            JsonObject bypass = root.getAsJsonObject("enchantment_bypass_control");
            EnchantmentBypassControl bypassControl = new EnchantmentBypassControl(RunicConfig.getBoolean(bypass, "disable_natural_enchanted_books", true), RunicConfig.getBoolean(bypass, "disable_villager_enchanted_book_trades", true), RunicConfig.getBoolean(bypass, "disable_natural_enchanted_gear", true), RunicConfig.getBoolean(bypass, "disable_enchanted_gear_trades", true), RunicConfig.getBoolean(bypass, "disable_anvil_enchanting", true), RunicConfig.getBoolean(bypass, "disable_fishing_enchanted_books", true), RunicConfig.getBoolean(bypass, "disable_mob_spawn_enchanted_gear", true));
            JsonObject reforging = root.getAsJsonObject("tablet_reforging");
            TabletReforging tabletReforging = new TabletReforging(RunicConfig.getBoolean(reforging, "enabled", true), RunicConfig.boundedChance(RunicConfig.getDouble(reforging, "tier_1_to_2_chance", 0.5), 0.5, "tablet_reforging.tier_1_to_2_chance"), RunicConfig.boundedChance(RunicConfig.getDouble(reforging, "tier_2_to_3_chance", 0.5), 0.5, "tablet_reforging.tier_2_to_3_chance"), RunicConfig.boundedChance(RunicConfig.getDouble(reforging, "tier_1_to_3_chance", 0.25), 0.25, "tablet_reforging.tier_1_to_3_chance"));
            JsonObject experienceObject = root.getAsJsonObject("experience_costs");
            ExperienceCosts experienceCosts = new ExperienceCosts(RunicConfig.getBoolean(experienceObject, "enabled", true), RunicConfig.nonNegativeFinite(RunicConfig.getDouble(experienceObject, "global_multiplier", 1.0), 1.0, "experience_costs.global_multiplier"), RunicConfig.nonNegativeFinite(RunicConfig.getDouble(experienceObject, "transmutation_multiplier", 0.5), 0.5, "experience_costs.transmutation_multiplier"));
            JsonObject transmutationObject = root.getAsJsonObject("transmutation");
            Transmutation transmutation = new Transmutation(RunicConfig.getBoolean(transmutationObject, "enabled", true), RunicConfig.boundedChance(RunicConfig.getDouble(transmutationObject, "source_item_destruction_chance", 0.0), 0.0, "transmutation.source_item_destruction_chance"), RunicConfig.nonNegativeFinite(RunicConfig.getDouble(transmutationObject, "base_enchant_units", 1.0), 1.0, "transmutation.base_enchant_units"), RunicConfig.nonNegativeFinite(RunicConfig.getDouble(transmutationObject, "tier_units_per_tier", 1.0), 1.0, "transmutation.tier_units_per_tier"), RunicConfig.nonNegativeFinite(RunicConfig.getDouble(transmutationObject, "extra_level_units", 1.0), 1.0, "transmutation.extra_level_units"), RunicConfig.nonNegativeFinite(RunicConfig.getDouble(transmutationObject, "curse_retention_cost_multiplier", 1.0), 1.0, "transmutation.curse_retention_cost_multiplier"));
            state = new ConfigState(Set.copyOf(blacklist), debugLogging, lootGeneration, bypassControl, tabletReforging, experienceCosts, transmutation);
            RunicAttunement.LOGGER.info("Loaded Runic Attunement config: {} blacklisted enchantment(s); loot generation {}; multiplier {}; tier weights {}/{}/{}; bypass controls {}; tablet reforging {} ({}/{}/{}); XP costs {} (global {}, transmutation {}); transmutation {} (destroy {}, base {}, tier {}, level {}, curse {})", new Object[]{blacklist.size(), lootGeneration.enabled() ? "enabled" : "disabled", lootGeneration.globalDropChanceMultiplier(), lootGeneration.tierWeights().tier1(), lootGeneration.tierWeights().tier2(), lootGeneration.tierWeights().tier3(), bypassControl.anyEnabled() ? "enabled" : "disabled", tabletReforging.enabled() ? "enabled" : "disabled", tabletReforging.tier1To2Chance(), tabletReforging.tier2To3Chance(), tabletReforging.tier1To3Chance(), experienceCosts.enabled() ? "enabled" : "disabled", experienceCosts.globalMultiplier(), experienceCosts.transmutationMultiplier(), transmutation.enabled() ? "enabled" : "disabled", transmutation.sourceItemDestructionChance(), transmutation.baseEnchantUnits(), transmutation.tierUnitsPerTier(), transmutation.extraLevelUnits(), transmutation.curseRetentionCostMultiplier()});
            if (migrated) {
                RunicConfig.writeJson(path, root);
                RunicAttunement.LOGGER.info("Updated {} with V1.0 config annotations/defaults while preserving existing settings", (Object)path);
            }
        }
        catch (Exception exception) {
            RunicAttunement.LOGGER.error("Failed to load {}; keeping the previous in-memory Runic Attunement config", (Object)path, (Object)exception);
        }
        RunicUnravelRuntime.ensureConfig();
    }

    public static boolean isBlacklisted(class_2960 enchantmentId) {
        return state.blacklistedEnchantments().contains(enchantmentId);
    }

    public static Set<class_2960> blacklistedEnchantments() {
        return state.blacklistedEnchantments();
    }

    public static boolean debugLogging() {
        return state.debugLogging();
    }

    public static boolean setDebugLogging(boolean enabled) {
        Path path = RunicConfig.path();
        try {
            JsonObject root;
            try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);){
                root = (JsonObject)GSON.fromJson((Reader)reader, JsonObject.class);
            }
            if (root == null) {
                root = RunicConfig.defaultJson();
            }
            root.addProperty("debug_logging", Boolean.valueOf(enabled));
            RunicConfig.writeJson(path, root);
            RunicConfig.reload();
            return state.debugLogging() == enabled;
        }
        catch (Exception exception) {
            RunicAttunement.LOGGER.error("Failed to update debug_logging in {}", (Object)path, (Object)exception);
            return false;
        }
    }

    public static LootGeneration lootGeneration() {
        return state.lootGeneration();
    }

    public static EnchantmentBypassControl enchantmentBypassControl() {
        return state.enchantmentBypassControl();
    }

    public static TabletReforging tabletReforging() {
        return state.tabletReforging();
    }

    public static ExperienceCosts experienceCosts() {
        return state.experienceCosts();
    }

    public static Transmutation transmutation() {
        return state.transmutation();
    }

    public static Path path() {
        return FabricLoader.getInstance().getConfigDir().resolve(FILE_NAME);
    }

    private static void ensureExists() {
        Path path = RunicConfig.path();
        if (Files.exists(path, new LinkOption[0])) {
            return;
        }
        try {
            Files.createDirectories(path.getParent(), new FileAttribute[0]);
            RunicConfig.writeJson(path, RunicConfig.defaultJson());
            RunicAttunement.LOGGER.info("Generated default Runic Attunement config at {}", (Object)path);
        }
        catch (Exception exception) {
            RunicAttunement.LOGGER.error("Failed to generate default Runic Attunement config at {}", (Object)path, (Object)exception);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void writeJson(Path path, JsonObject root) throws Exception {
        Files.createDirectories(path.getParent(), new FileAttribute[0]);
        Path temp = Files.createTempFile(path.getParent(), path.getFileName().toString() + ".", ".tmp", new FileAttribute[0]);
        try {
            try (BufferedWriter writer = Files.newBufferedWriter(temp, StandardCharsets.UTF_8, new OpenOption[0]);){
                GSON.toJson((JsonElement)root, (Appendable)writer);
            }
            try {
                Files.move(temp, path, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            }
            catch (IOException | UnsupportedOperationException atomicFailure) {
                if (Files.exists(temp, new LinkOption[0])) {
                    Files.move(temp, path, StandardCopyOption.REPLACE_EXISTING);
                } else if (!Files.exists(path, new LinkOption[0])) {
                    throw atomicFailure;
                }
            }
        }
        finally {
            try {
                Files.deleteIfExists(temp);
            }
            catch (IOException cleanupFailure) {
                RunicAttunement.LOGGER.warn("Could not remove temporary Runic Attunement config file {}", (Object)temp, (Object)cleanupFailure);
            }
        }
    }

    private static JsonObject defaultJson() {
        JsonObject root = new JsonObject();
        root.addProperty("_comment", "Runic Attunement V1.0 configuration. Edit this file, then run /reload or restart. Keys beginning with '_' are documentation only and may be left unchanged.");
        root.addProperty("_config_version", (Number)1);
        root.addProperty("_help_debug_logging", "When true, writes extra diagnostic information about loot injection and definition loading to the log. Leave false for normal play.");
        root.addProperty("debug_logging", Boolean.valueOf(false));
        root.addProperty("_help_blacklisted_enchantments", "List enchantment IDs such as minecraft:mending. Blacklisted enchantments are removed from Runic tier pools, ignored by the table, and never generated on Tablets. Malformed IDs are ignored with a warning; valid IDs may be listed even when their providing mod is not currently installed.");
        root.add("blacklisted_enchantments", (JsonElement)new JsonArray());
        root.add("loot_generation", (JsonElement)RunicConfig.defaultLootGenerationJson());
        root.add("enchantment_bypass_control", (JsonElement)RunicConfig.defaultEnchantmentBypassControlJson());
        root.add("tablet_reforging", (JsonElement)RunicConfig.defaultTabletReforgingJson());
        root.add("experience_costs", (JsonElement)RunicConfig.defaultExperienceCostsJson());
        root.add("transmutation", (JsonElement)RunicConfig.defaultTransmutationJson());
        return root;
    }

    private static JsonObject defaultLootGenerationJson() {
        JsonObject loot = new JsonObject();
        loot.addProperty("_comment", "Controls Runic Tablet injection into loot tables defined by built-in or datapack loot profiles.");
        loot.addProperty("_help_enabled", "Master switch for Runic Tablet chest/structure loot generation. false disables only injected Tablet loot; existing Tablets are unaffected.");
        loot.addProperty("enabled", Boolean.valueOf(true));
        loot.addProperty("_help_global_drop_chance_multiplier", "Multiplies every loot profile's tablet_chance. 1.0 = normal, 0.5 = half chance, 2.0 = double chance. Final chance is clamped to 100%. Must be non-negative.");
        loot.addProperty("global_drop_chance_multiplier", (Number)1.0);
        JsonObject weights = new JsonObject();
        weights.addProperty("_comment", "Relative potency weights used after a Tablet roll succeeds. Values do not need to total 100; zero disables that potency from generated loot.");
        weights.addProperty("_help_tier_1", "Relative weight for Latent Tablets.");
        weights.addProperty("tier_1", (Number)65.0);
        weights.addProperty("_help_tier_2", "Relative weight for Resonant Tablets.");
        weights.addProperty("tier_2", (Number)30.0);
        weights.addProperty("_help_tier_3", "Relative weight for Pristine Tablets.");
        weights.addProperty("tier_3", (Number)5.0);
        loot.add("tier_weights", (JsonElement)weights);
        return loot;
    }

    private static JsonObject defaultEnchantmentBypassControlJson() {
        JsonObject bypass = new JsonObject();
        bypass.addProperty("_comment", "Controls for closing vanilla/standard enchantment acquisition routes so Runic Attunement is authoritative by default. These inspect Minecraft's normal enchantment data and can therefore affect compatible modded enchantments too. All default to true and can be individually disabled to restore a route.");
        bypass.addProperty("_help_disable_natural_enchanted_books", "Replace enchanted books produced by non-fishing loot tables with ordinary books.");
        bypass.addProperty("disable_natural_enchanted_books", Boolean.valueOf(true));
        bypass.addProperty("_help_disable_villager_enchanted_book_trades", "Remove villager/merchant offers whose sold item is an enchanted book.");
        bypass.addProperty("disable_villager_enchanted_book_trades", Boolean.valueOf(true));
        bypass.addProperty("_help_disable_natural_enchanted_gear", "Strip direct enchantments from equipment generated by loot tables. Does not delete the item itself.");
        bypass.addProperty("disable_natural_enchanted_gear", Boolean.valueOf(true));
        bypass.addProperty("_help_disable_enchanted_gear_trades", "Remove villager/merchant offers that sell directly enchanted equipment.");
        bypass.addProperty("disable_enchanted_gear_trades", Boolean.valueOf(true));
        bypass.addProperty("_help_disable_anvil_enchanting", "Block anvil operations that add or increase enchantments, while leaving ordinary repair/rename operations available. Runic Tablet reforging remains supported.");
        bypass.addProperty("disable_anvil_enchanting", Boolean.valueOf(true));
        bypass.addProperty("_help_disable_fishing_enchanted_books", "Replace enchanted books obtained from fishing loot with ordinary books, independently of other loot tables.");
        bypass.addProperty("disable_fishing_enchanted_books", Boolean.valueOf(true));
        bypass.addProperty("_help_disable_mob_spawn_enchanted_gear", "Prevent Minecraft's mob-spawn enchantment pass from enchanting mob equipment.");
        bypass.addProperty("disable_mob_spawn_enchanted_gear", Boolean.valueOf(true));
        return bypass;
    }

    private static JsonObject defaultTabletReforgingJson() {
        JsonObject reforging = new JsonObject();
        reforging.addProperty("_comment", "Controls Tablet recycling and Runic Reforging. Chances are decimal probabilities from 0.0 to 1.0.");
        reforging.addProperty("_help_enabled", "Master switch for grinding configured Tablets into same-potency Dust and using Tablets + Dust in anvils for rerolls/upgrades.");
        reforging.addProperty("enabled", Boolean.valueOf(true));
        reforging.addProperty("_help_tier_1_to_2_chance", "Chance that a Latent Tablet reforged with Resonant Dust upgrades to Resonant. Failure keeps it Latent but still rerolls its attunement.");
        reforging.addProperty("tier_1_to_2_chance", (Number)0.5);
        reforging.addProperty("_help_tier_2_to_3_chance", "Chance that a Resonant Tablet reforged with Pristine Dust upgrades to Pristine. Failure keeps it Resonant but still rerolls its attunement.");
        reforging.addProperty("tier_2_to_3_chance", (Number)0.5);
        reforging.addProperty("_help_tier_1_to_3_chance", "Chance that a Latent Tablet reforged directly with Pristine Dust jumps to Pristine. Failure keeps it Latent but still rerolls its attunement.");
        reforging.addProperty("tier_1_to_3_chance", (Number)0.25);
        return reforging;
    }

    private static JsonObject defaultExperienceCostsJson() {
        JsonObject experience = new JsonObject();
        experience.addProperty("_comment", "Runic Attunement spends raw XP points, never experience levels. Per-level base point costs come from enchantment datapack definitions.");
        experience.addProperty("_help_enabled", "Master switch for all Runic Attunement XP-point costs. false makes Attunement and Transmutation cost 0 XP but leaves material costs intact.");
        experience.addProperty("enabled", Boolean.valueOf(true));
        experience.addProperty("_help_global_multiplier", "Scales all Runic XP-point costs. 1.0 = normal, 0.5 = half, 2.0 = double. Final costs round up to the next whole point. Must be non-negative.");
        experience.addProperty("global_multiplier", (Number)1.0);
        experience.addProperty("_help_transmutation_multiplier", "Additional multiplier applied only to Transmutation after global_multiplier. The base Transmutation XP value is the cumulative configured XP value of transferred enchantment levels.");
        experience.addProperty("transmutation_multiplier", (Number)0.5);
        return experience;
    }

    private static JsonObject defaultTransmutationJson() {
        JsonObject transmutation = new JsonObject();
        transmutation.addProperty("_comment", "Controls Runic Transmutation. Selected supported non-curse enchantments move from source to compatible target. Compatible curses follow automatically unless retained on the source at the configured material surcharge.");
        transmutation.addProperty("_help_enabled", "Master switch for the Transmutation tab/action. false leaves ordinary Attunement available.");
        transmutation.addProperty("enabled", Boolean.valueOf(true));
        transmutation.addProperty("_help_source_item_destruction_chance", "Chance from 0.0 to 1.0 that the source item is destroyed after a successful Transmutation. 0.0 = never; 1.0 = always. The target is not affected.");
        transmutation.addProperty("source_item_destruction_chance", (Number)0.0);
        transmutation.addProperty("_help_base_enchant_units", "Base material-cost units charged for each selected non-curse enchantment transferred. Must be non-negative.");
        transmutation.addProperty("base_enchant_units", (Number)1.0);
        transmutation.addProperty("_help_tier_units_per_tier", "Additional material-cost units per Tablet potency tier of each transferred enchantment definition. With 1.0: Latent adds 1, Resonant 2, Pristine 3 units.");
        transmutation.addProperty("tier_units_per_tier", (Number)1.0);
        transmutation.addProperty("_help_extra_level_units", "Additional material-cost units for each enchantment level above I. With 1.0, level III adds 2 extra units.");
        transmutation.addProperty("extra_level_units", (Number)1.0);
        transmutation.addProperty("_help_curse_retention_cost_multiplier", "Scales the datapack curse-retention material surcharge for each curse left on the source instead of transferring. 0.0 makes curse retention free; 1.0 uses normal cost.");
        transmutation.addProperty("curse_retention_cost_multiplier", (Number)1.0);
        return transmutation;
    }

    private static boolean ensureCurrentSchema(JsonObject root) {
        JsonObject defaults;
        boolean changed = false;
        JsonObject rootDefaults = RunicConfig.defaultJson();
        for (String key : List.of("_comment", "_config_version", "_help_debug_logging", "_help_blacklisted_enchantments")) {
            JsonElement expected = rootDefaults.get(key);
            if (root.has(key) && root.get(key).equals(expected)) continue;
            root.add(key, expected.deepCopy());
            changed = true;
        }
        if (!root.has("debug_logging")) {
            root.addProperty("debug_logging", Boolean.valueOf(false));
            changed = true;
        }
        if (!root.has("blacklisted_enchantments") || !root.get("blacklisted_enchantments").isJsonArray()) {
            root.add("blacklisted_enchantments", (JsonElement)new JsonArray());
            changed = true;
        }
        if (!root.has("loot_generation") || !root.get("loot_generation").isJsonObject()) {
            root.add("loot_generation", (JsonElement)RunicConfig.defaultLootGenerationJson());
            changed = true;
        } else {
            JsonObject loot = root.getAsJsonObject("loot_generation");
            JsonElement lootComment = RunicConfig.defaultLootGenerationJson().get("_comment");
            if (!loot.has("_comment") || !loot.get("_comment").equals(lootComment)) {
                loot.add("_comment", lootComment.deepCopy());
                changed = true;
            }
            if (!loot.has("enabled")) {
                loot.addProperty("enabled", Boolean.valueOf(true));
                changed = true;
            }
            if (!loot.has("global_drop_chance_multiplier")) {
                loot.addProperty("global_drop_chance_multiplier", (Number)1.0);
                changed = true;
            }
            if (!loot.has("tier_weights") || !loot.get("tier_weights").isJsonObject()) {
                loot.add("tier_weights", (JsonElement)RunicConfig.defaultLootGenerationJson().getAsJsonObject("tier_weights"));
                changed = true;
            } else {
                JsonObject weights = loot.getAsJsonObject("tier_weights");
                JsonElement weightsComment = RunicConfig.defaultLootGenerationJson().getAsJsonObject("tier_weights").get("_comment");
                if (!weights.has("_comment") || !weights.get("_comment").equals(weightsComment)) {
                    weights.add("_comment", weightsComment.deepCopy());
                    changed = true;
                }
                if (!weights.has("tier_1")) {
                    weights.addProperty("tier_1", (Number)65.0);
                    changed = true;
                }
                if (!weights.has("tier_2")) {
                    weights.addProperty("tier_2", (Number)30.0);
                    changed = true;
                }
                if (!weights.has("tier_3")) {
                    weights.addProperty("tier_3", (Number)5.0);
                    changed = true;
                }
            }
            JsonObject lootDefaults = RunicConfig.defaultLootGenerationJson();
            for (String key : lootDefaults.keySet()) {
                if (key.equals("tier_weights")) continue;
                boolean documentation = key.startsWith("_");
                if (loot.has(key) && (!documentation || loot.get(key).equals(lootDefaults.get(key)))) continue;
                loot.add(key, lootDefaults.get(key).deepCopy());
                changed = true;
            }
            JsonObject weightDefaults = lootDefaults.getAsJsonObject("tier_weights");
            JsonObject weights = loot.getAsJsonObject("tier_weights");
            for (String key : weightDefaults.keySet()) {
                boolean documentation = key.startsWith("_");
                if (weights.has(key) && (!documentation || weights.get(key).equals(weightDefaults.get(key)))) continue;
                weights.add(key, weightDefaults.get(key).deepCopy());
                changed = true;
            }
        }
        if (!root.has("enchantment_bypass_control") || !root.get("enchantment_bypass_control").isJsonObject()) {
            JsonObject legacy;
            bypass = RunicConfig.defaultEnchantmentBypassControlJson();
            JsonObject jsonObject = legacy = root.has("future_mechanics") && root.get("future_mechanics").isJsonObject() ? root.getAsJsonObject("future_mechanics") : null;
            if (legacy != null) {
                RunicConfig.copyBooleanIfPresent(legacy, bypass, "disable_natural_enchanted_books", "disable_natural_enchanted_books");
                RunicConfig.copyBooleanIfPresent(legacy, bypass, "disable_villager_enchanted_book_trades", "disable_villager_enchanted_book_trades");
                RunicConfig.copyBooleanIfPresent(legacy, bypass, "disable_natural_enchanted_gear", "disable_natural_enchanted_gear");
                RunicConfig.copyBooleanIfPresent(legacy, bypass, "disable_enchanted_gear_trades", "disable_enchanted_gear_trades");
                RunicConfig.copyBooleanIfPresent(legacy, bypass, "disable_anvil_enchanted_book_application", "disable_anvil_enchanting");
            }
            root.add("enchantment_bypass_control", (JsonElement)bypass);
            changed = true;
        } else {
            bypass = root.getAsJsonObject("enchantment_bypass_control");
            JsonObject defaults2 = RunicConfig.defaultEnchantmentBypassControlJson();
            for (String key : defaults2.keySet()) {
                boolean documentation = key.startsWith("_");
                if (bypass.has(key) && (!documentation || bypass.get(key).equals(defaults2.get(key)))) continue;
                bypass.add(key, defaults2.get(key).deepCopy());
                changed = true;
            }
        }
        if (!root.has("tablet_reforging") || !root.get("tablet_reforging").isJsonObject()) {
            root.add("tablet_reforging", (JsonElement)RunicConfig.defaultTabletReforgingJson());
            changed = true;
        } else {
            JsonObject reforging = root.getAsJsonObject("tablet_reforging");
            defaults = RunicConfig.defaultTabletReforgingJson();
            for (String key : defaults.keySet()) {
                boolean documentation = key.startsWith("_");
                if (reforging.has(key) && (!documentation || reforging.get(key).equals(defaults.get(key)))) continue;
                reforging.add(key, defaults.get(key).deepCopy());
                changed = true;
            }
        }
        if (!root.has("experience_costs") || !root.get("experience_costs").isJsonObject()) {
            root.add("experience_costs", (JsonElement)RunicConfig.defaultExperienceCostsJson());
            changed = true;
        } else {
            JsonObject experience = root.getAsJsonObject("experience_costs");
            defaults = RunicConfig.defaultExperienceCostsJson();
            for (String key : defaults.keySet()) {
                boolean documentation = key.startsWith("_");
                if (experience.has(key) && (!documentation || experience.get(key).equals(defaults.get(key)))) continue;
                experience.add(key, defaults.get(key).deepCopy());
                changed = true;
            }
        }
        if (!root.has("transmutation") || !root.get("transmutation").isJsonObject()) {
            root.add("transmutation", (JsonElement)RunicConfig.defaultTransmutationJson());
            changed = true;
        } else {
            JsonObject transmutation = root.getAsJsonObject("transmutation");
            defaults = RunicConfig.defaultTransmutationJson();
            for (String key : defaults.keySet()) {
                boolean documentation = key.startsWith("_");
                if (transmutation.has(key) && (!documentation || transmutation.get(key).equals(defaults.get(key)))) continue;
                transmutation.add(key, defaults.get(key).deepCopy());
                changed = true;
            }
        }
        if (root.has("future_mechanics")) {
            root.remove("future_mechanics");
            changed = true;
        }
        return changed;
    }

    private static void copyBooleanIfPresent(JsonObject source, JsonObject target, String sourceKey, String targetKey) {
        if (source.has(sourceKey)) {
            target.addProperty(targetKey, Boolean.valueOf(source.get(sourceKey).getAsBoolean()));
        }
    }

    private static boolean getBoolean(JsonObject object, String key, boolean fallback) {
        if (object == null || !object.has(key)) {
            return fallback;
        }
        try {
            return object.get(key).getAsBoolean();
        }
        catch (RuntimeException exception) {
            RunicAttunement.LOGGER.warn("Invalid boolean config value for '{}'; using {}", (Object)key, (Object)fallback);
            return fallback;
        }
    }

    private static double getDouble(JsonObject object, String key, double fallback) {
        if (object == null || !object.has(key)) {
            return fallback;
        }
        try {
            return object.get(key).getAsDouble();
        }
        catch (RuntimeException exception) {
            RunicAttunement.LOGGER.warn("Invalid numeric config value for '{}'; using {}", (Object)key, (Object)fallback);
            return fallback;
        }
    }

    private static double nonNegativeFinite(double value, double fallback, String path) {
        if (!Double.isFinite(value) || value < 0.0) {
            RunicAttunement.LOGGER.warn("Invalid {} value {}; using {}", new Object[]{path, value, fallback});
            return fallback;
        }
        return value;
    }

    private static double boundedChance(double value, double fallback, String path) {
        if (!Double.isFinite(value) || value < 0.0 || value > 1.0) {
            RunicAttunement.LOGGER.warn("Invalid {} value {}; using {}", new Object[]{path, value, fallback});
            return fallback;
        }
        return value;
    }

    public record TierWeights(double tier1, double tier2, double tier3) {
        public double weightFor(int tier) {
            return switch (tier) {
                case 1 -> this.tier1;
                case 2 -> this.tier2;
                case 3 -> this.tier3;
                default -> 0.0;
            };
        }

        public double total() {
            return this.tier1 + this.tier2 + this.tier3;
        }
    }

    public record LootGeneration(boolean enabled, double globalDropChanceMultiplier, TierWeights tierWeights) {
        private static LootGeneration defaults() {
            return new LootGeneration(true, 1.0, new TierWeights(65.0, 30.0, 5.0));
        }

        public double effectiveChance(double baseChance) {
            return Math.max(0.0, Math.min(1.0, baseChance * this.globalDropChanceMultiplier));
        }
    }

    public record EnchantmentBypassControl(boolean disableNaturalEnchantedBooks, boolean disableVillagerEnchantedBookTrades, boolean disableNaturalEnchantedGear, boolean disableEnchantedGearTrades, boolean disableAnvilEnchanting, boolean disableFishingEnchantedBooks, boolean disableMobSpawnEnchantedGear) {
        private static EnchantmentBypassControl defaults() {
            return new EnchantmentBypassControl(true, true, true, true, true, true, true);
        }

        public boolean anyEnabled() {
            return this.disableNaturalEnchantedBooks || this.disableVillagerEnchantedBookTrades || this.disableNaturalEnchantedGear || this.disableEnchantedGearTrades || this.disableAnvilEnchanting || this.disableFishingEnchantedBooks || this.disableMobSpawnEnchantedGear;
        }
    }

    public record TabletReforging(boolean enabled, double tier1To2Chance, double tier2To3Chance, double tier1To3Chance) {
        private static TabletReforging defaults() {
            return new TabletReforging(true, 0.5, 0.5, 0.25);
        }

        public double upgradeChance(int startTier, int dustTier) {
            if (startTier == dustTier) {
                return 1.0;
            }
            if (startTier == 1 && dustTier == 2) {
                return this.tier1To2Chance;
            }
            if (startTier == 2 && dustTier == 3) {
                return this.tier2To3Chance;
            }
            if (startTier == 1 && dustTier == 3) {
                return this.tier1To3Chance;
            }
            return 0.0;
        }
    }

    public record ExperienceCosts(boolean enabled, double globalMultiplier, double transmutationMultiplier) {
        private static ExperienceCosts defaults() {
            return new ExperienceCosts(true, 1.0, 0.5);
        }
    }

    public record Transmutation(boolean enabled, double sourceItemDestructionChance, double baseEnchantUnits, double tierUnitsPerTier, double extraLevelUnits, double curseRetentionCostMultiplier) {
        private static Transmutation defaults() {
            return new Transmutation(true, 0.0, 1.0, 1.0, 1.0, 1.0);
        }

        public double unitsFor(int tabletTier, int enchantmentLevel) {
            return this.baseEnchantUnits + (double)Math.max(0, tabletTier) * this.tierUnitsPerTier + (double)Math.max(0, enchantmentLevel - 1) * this.extraLevelUnits;
        }
    }

    private record ConfigState(Set<class_2960> blacklistedEnchantments, boolean debugLogging, LootGeneration lootGeneration, EnchantmentBypassControl enchantmentBypassControl, TabletReforging tabletReforging, ExperienceCosts experienceCosts, Transmutation transmutation) {
        private static ConfigState defaults() {
            return new ConfigState(Set.of(), false, LootGeneration.defaults(), EnchantmentBypassControl.defaults(), TabletReforging.defaults(), ExperienceCosts.defaults(), Transmutation.defaults());
        }
    }
}

