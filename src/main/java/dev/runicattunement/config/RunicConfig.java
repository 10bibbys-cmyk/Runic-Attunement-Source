package dev.runicattunement.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.runicattunement.RunicAttunement;
import dev.runicattunement.unravel.RunicUnravelRuntime;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_2960;

public final class RunicConfig {
   private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
   private static final String FILE_NAME = "runic_attunement.json";
   private static volatile RunicConfig.ConfigState state = RunicConfig.ConfigState.defaults();

   private RunicConfig() {
   }

   public static void initialize() {
      ensureExists();
      reload();
   }

   public static void reload() {
      Path path = path();
      if (!Files.exists(path)) {
         ensureExists();
      }

      try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
         JsonObject root = (JsonObject)GSON.fromJson(reader, JsonObject.class);
         if (root == null) {
            throw new IllegalArgumentException("Config root is empty");
         }

         boolean migrated = ensureCurrentSchema(root);
         Set<class_2960> blacklist = new LinkedHashSet<>();
         JsonArray blacklisted = root.getAsJsonArray("blacklisted_enchantments");
         if (blacklisted != null) {
            for (JsonElement element : blacklisted) {
               try {
                  blacklist.add(new class_2960(element.getAsString()));
               } catch (RuntimeException exception) {
                  RunicAttunement.LOGGER.warn("Ignoring invalid blacklisted enchantment id '{}'", element, exception);
               }
            }
         }

         boolean debugLogging = getBoolean(root, "debug_logging", false);
         JsonObject loot = root.getAsJsonObject("loot_generation");
         boolean lootEnabled = getBoolean(loot, "enabled", true);
         double multiplier = nonNegativeFinite(getDouble(loot, "global_drop_chance_multiplier", 1.0), 1.0, "loot_generation.global_drop_chance_multiplier");
         JsonObject weights = loot == null ? null : loot.getAsJsonObject("tier_weights");
         RunicConfig.TierWeights tierWeights = new RunicConfig.TierWeights(
            nonNegativeFinite(getDouble(weights, "tier_1", 65.0), 65.0, "loot_generation.tier_weights.tier_1"),
            nonNegativeFinite(getDouble(weights, "tier_2", 30.0), 30.0, "loot_generation.tier_weights.tier_2"),
            nonNegativeFinite(getDouble(weights, "tier_3", 5.0), 5.0, "loot_generation.tier_weights.tier_3")
         );
         if (tierWeights.total() <= 0.0) {
            RunicAttunement.LOGGER.warn("All Runic Attunement loot tier weights are zero; loot profiles will not generate Tablets");
         }

         RunicConfig.LootGeneration lootGeneration = new RunicConfig.LootGeneration(lootEnabled, multiplier, tierWeights);
         JsonObject bypass = root.getAsJsonObject("enchantment_bypass_control");
         RunicConfig.EnchantmentBypassControl bypassControl = new RunicConfig.EnchantmentBypassControl(
            getBoolean(bypass, "disable_natural_enchanted_books", true),
            getBoolean(bypass, "disable_villager_enchanted_book_trades", true),
            getBoolean(bypass, "disable_natural_enchanted_gear", true),
            getBoolean(bypass, "disable_enchanted_gear_trades", true),
            getBoolean(bypass, "disable_anvil_enchanting", true),
            getBoolean(bypass, "disable_fishing_enchanted_books", true),
            getBoolean(bypass, "disable_mob_spawn_enchanted_gear", true)
         );
         JsonObject reforging = root.getAsJsonObject("tablet_reforging");
         RunicConfig.TabletReforging tabletReforging = new RunicConfig.TabletReforging(
            getBoolean(reforging, "enabled", true),
            boundedChance(getDouble(reforging, "tier_1_to_2_chance", 0.5), 0.5, "tablet_reforging.tier_1_to_2_chance"),
            boundedChance(getDouble(reforging, "tier_2_to_3_chance", 0.5), 0.5, "tablet_reforging.tier_2_to_3_chance"),
            boundedChance(getDouble(reforging, "tier_1_to_3_chance", 0.25), 0.25, "tablet_reforging.tier_1_to_3_chance")
         );
         JsonObject experienceObject = root.getAsJsonObject("experience_costs");
         RunicConfig.ExperienceCosts experienceCosts = new RunicConfig.ExperienceCosts(
            getBoolean(experienceObject, "enabled", true),
            nonNegativeFinite(getDouble(experienceObject, "global_multiplier", 1.0), 1.0, "experience_costs.global_multiplier"),
            nonNegativeFinite(getDouble(experienceObject, "transmutation_multiplier", 0.5), 0.5, "experience_costs.transmutation_multiplier")
         );
         JsonObject transmutationObject = root.getAsJsonObject("transmutation");
         RunicConfig.Transmutation transmutation = new RunicConfig.Transmutation(
            getBoolean(transmutationObject, "enabled", true),
            boundedChance(getDouble(transmutationObject, "source_item_destruction_chance", 0.0), 0.0, "transmutation.source_item_destruction_chance"),
            nonNegativeFinite(getDouble(transmutationObject, "base_enchant_units", 1.0), 1.0, "transmutation.base_enchant_units"),
            nonNegativeFinite(getDouble(transmutationObject, "tier_units_per_tier", 1.0), 1.0, "transmutation.tier_units_per_tier"),
            nonNegativeFinite(getDouble(transmutationObject, "extra_level_units", 1.0), 1.0, "transmutation.extra_level_units"),
            nonNegativeFinite(getDouble(transmutationObject, "curse_retention_cost_multiplier", 1.0), 1.0, "transmutation.curse_retention_cost_multiplier")
         );
         state = new RunicConfig.ConfigState(
            Set.copyOf(blacklist), debugLogging, lootGeneration, bypassControl, tabletReforging, experienceCosts, transmutation
         );
         RunicAttunement.LOGGER
            .info(
               "Loaded Runic Attunement config: {} blacklisted enchantment(s); loot generation {}; multiplier {}; tier weights {}/{}/{}; bypass controls {}; tablet reforging {} ({}/{}/{}); XP costs {} (global {}, transmutation {}); transmutation {} (destroy {}, base {}, tier {}, level {}, curse {})",
               new Object[]{
                  blacklist.size(),
                  lootGeneration.enabled() ? "enabled" : "disabled",
                  lootGeneration.globalDropChanceMultiplier(),
                  lootGeneration.tierWeights().tier1(),
                  lootGeneration.tierWeights().tier2(),
                  lootGeneration.tierWeights().tier3(),
                  bypassControl.anyEnabled() ? "enabled" : "disabled",
                  tabletReforging.enabled() ? "enabled" : "disabled",
                  tabletReforging.tier1To2Chance(),
                  tabletReforging.tier2To3Chance(),
                  tabletReforging.tier1To3Chance(),
                  experienceCosts.enabled() ? "enabled" : "disabled",
                  experienceCosts.globalMultiplier(),
                  experienceCosts.transmutationMultiplier(),
                  transmutation.enabled() ? "enabled" : "disabled",
                  transmutation.sourceItemDestructionChance(),
                  transmutation.baseEnchantUnits(),
                  transmutation.tierUnitsPerTier(),
                  transmutation.extraLevelUnits(),
                  transmutation.curseRetentionCostMultiplier()
               }
            );
         if (migrated) {
            writeJson(path, root);
            RunicAttunement.LOGGER.info("Updated {} with V1.0 config annotations/defaults while preserving existing settings", path);
         }
      } catch (Exception exception) {
         RunicAttunement.LOGGER.error("Failed to load {}; keeping the previous in-memory Runic Attunement config", path, exception);
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
      Path path = path();

      try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
         JsonObject root = (JsonObject)GSON.fromJson(reader, JsonObject.class);
         if (root == null) {
            root = defaultJson();
         }

         root.addProperty("debug_logging", enabled);
         writeJson(path, root);
         reload();
         return state.debugLogging() == enabled;
      } catch (Exception exception) {
         RunicAttunement.LOGGER.error("Failed to update debug_logging in {}", path, exception);
         return false;
      }
   }

   public static RunicConfig.LootGeneration lootGeneration() {
      return state.lootGeneration();
   }

   public static RunicConfig.EnchantmentBypassControl enchantmentBypassControl() {
      return state.enchantmentBypassControl();
   }

   public static RunicConfig.TabletReforging tabletReforging() {
      return state.tabletReforging();
   }

   public static RunicConfig.ExperienceCosts experienceCosts() {
      return state.experienceCosts();
   }

   public static RunicConfig.Transmutation transmutation() {
      return state.transmutation();
   }

   public static Path path() {
      return FabricLoader.getInstance().getConfigDir().resolve("runic_attunement.json");
   }

   private static void ensureExists() {
      Path path = path();
      if (!Files.exists(path)) {
         try {
            Files.createDirectories(path.getParent());
            writeJson(path, defaultJson());
            RunicAttunement.LOGGER.info("Generated default Runic Attunement config at {}", path);
         } catch (Exception exception) {
            RunicAttunement.LOGGER.error("Failed to generate default Runic Attunement config at {}", path, exception);
         }
      }
   }

   private static void writeJson(Path path, JsonObject root) throws Exception {
      Files.createDirectories(path.getParent());
      Path temp = Files.createTempFile(path.getParent(), path.getFileName().toString() + ".", ".tmp");

      try {
         try (Writer writer = Files.newBufferedWriter(temp, StandardCharsets.UTF_8)) {
            GSON.toJson(root, writer);
         }

         try {
            Files.move(temp, path, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
         } catch (IOException | UnsupportedOperationException atomicFailure) {
            if (Files.exists(temp)) {
               Files.move(temp, path, StandardCopyOption.REPLACE_EXISTING);
            } else if (!Files.exists(path)) {
               throw atomicFailure;
            }
         }
      } finally {
         try {
            Files.deleteIfExists(temp);
         } catch (IOException cleanupFailure) {
            RunicAttunement.LOGGER.warn("Could not remove temporary Runic Attunement config file {}", temp, cleanupFailure);
         }
      }
   }

   private static JsonObject defaultJson() {
      JsonObject root = new JsonObject();
      root.addProperty(
         "_comment",
         "Runic Attunement V1.0 configuration. Edit this file, then run /reload or restart. Keys beginning with '_' are documentation only and may be left unchanged."
      );
      root.addProperty("_config_version", 1);
      root.addProperty(
         "_help_debug_logging",
         "When true, writes extra diagnostic information about loot injection and definition loading to the log. Leave false for normal play."
      );
      root.addProperty("debug_logging", false);
      root.addProperty(
         "_help_blacklisted_enchantments",
         "List enchantment IDs such as minecraft:mending. Blacklisted enchantments are removed from Runic tier pools, ignored by the table, and never generated on Tablets. Malformed IDs are ignored with a warning; valid IDs may be listed even when their providing mod is not currently installed."
      );
      root.add("blacklisted_enchantments", new JsonArray());
      root.add("loot_generation", defaultLootGenerationJson());
      root.add("enchantment_bypass_control", defaultEnchantmentBypassControlJson());
      root.add("tablet_reforging", defaultTabletReforgingJson());
      root.add("experience_costs", defaultExperienceCostsJson());
      root.add("transmutation", defaultTransmutationJson());
      return root;
   }

   private static JsonObject defaultLootGenerationJson() {
      JsonObject loot = new JsonObject();
      loot.addProperty("_comment", "Controls Runic Tablet injection into loot tables defined by built-in or datapack loot profiles.");
      loot.addProperty(
         "_help_enabled",
         "Master switch for Runic Tablet chest/structure loot generation. false disables only injected Tablet loot; existing Tablets are unaffected."
      );
      loot.addProperty("enabled", true);
      loot.addProperty(
         "_help_global_drop_chance_multiplier",
         "Multiplies every loot profile's tablet_chance. 1.0 = normal, 0.5 = half chance, 2.0 = double chance. Final chance is clamped to 100%. Must be non-negative."
      );
      loot.addProperty("global_drop_chance_multiplier", 1.0);
      JsonObject weights = new JsonObject();
      weights.addProperty(
         "_comment",
         "Relative potency weights used after a Tablet roll succeeds. Values do not need to total 100; zero disables that potency from generated loot."
      );
      weights.addProperty("_help_tier_1", "Relative weight for Latent Tablets.");
      weights.addProperty("tier_1", 65.0);
      weights.addProperty("_help_tier_2", "Relative weight for Resonant Tablets.");
      weights.addProperty("tier_2", 30.0);
      weights.addProperty("_help_tier_3", "Relative weight for Pristine Tablets.");
      weights.addProperty("tier_3", 5.0);
      loot.add("tier_weights", weights);
      return loot;
   }

   private static JsonObject defaultEnchantmentBypassControlJson() {
      JsonObject bypass = new JsonObject();
      bypass.addProperty(
         "_comment",
         "Controls for closing vanilla/standard enchantment acquisition routes so Runic Attunement is authoritative by default. These inspect Minecraft's normal enchantment data and can therefore affect compatible modded enchantments too. All default to true and can be individually disabled to restore a route."
      );
      bypass.addProperty("_help_disable_natural_enchanted_books", "Replace enchanted books produced by non-fishing loot tables with ordinary books.");
      bypass.addProperty("disable_natural_enchanted_books", true);
      bypass.addProperty("_help_disable_villager_enchanted_book_trades", "Remove villager/merchant offers whose sold item is an enchanted book.");
      bypass.addProperty("disable_villager_enchanted_book_trades", true);
      bypass.addProperty(
         "_help_disable_natural_enchanted_gear", "Strip direct enchantments from equipment generated by loot tables. Does not delete the item itself."
      );
      bypass.addProperty("disable_natural_enchanted_gear", true);
      bypass.addProperty("_help_disable_enchanted_gear_trades", "Remove villager/merchant offers that sell directly enchanted equipment.");
      bypass.addProperty("disable_enchanted_gear_trades", true);
      bypass.addProperty(
         "_help_disable_anvil_enchanting",
         "Block anvil operations that add or increase enchantments, while leaving ordinary repair/rename operations available. Runic Tablet reforging remains supported."
      );
      bypass.addProperty("disable_anvil_enchanting", true);
      bypass.addProperty(
         "_help_disable_fishing_enchanted_books", "Replace enchanted books obtained from fishing loot with ordinary books, independently of other loot tables."
      );
      bypass.addProperty("disable_fishing_enchanted_books", true);
      bypass.addProperty("_help_disable_mob_spawn_enchanted_gear", "Prevent Minecraft's mob-spawn enchantment pass from enchanting mob equipment.");
      bypass.addProperty("disable_mob_spawn_enchanted_gear", true);
      return bypass;
   }

   private static JsonObject defaultTabletReforgingJson() {
      JsonObject reforging = new JsonObject();
      reforging.addProperty("_comment", "Controls Tablet recycling and Runic Reforging. Chances are decimal probabilities from 0.0 to 1.0.");
      reforging.addProperty(
         "_help_enabled", "Master switch for grinding configured Tablets into same-potency Dust and using Tablets + Dust in anvils for rerolls/upgrades."
      );
      reforging.addProperty("enabled", true);
      reforging.addProperty(
         "_help_tier_1_to_2_chance",
         "Chance that a Latent Tablet reforged with Resonant Dust upgrades to Resonant. Failure keeps it Latent but still rerolls its attunement."
      );
      reforging.addProperty("tier_1_to_2_chance", 0.5);
      reforging.addProperty(
         "_help_tier_2_to_3_chance",
         "Chance that a Resonant Tablet reforged with Pristine Dust upgrades to Pristine. Failure keeps it Resonant but still rerolls its attunement."
      );
      reforging.addProperty("tier_2_to_3_chance", 0.5);
      reforging.addProperty(
         "_help_tier_1_to_3_chance",
         "Chance that a Latent Tablet reforged directly with Pristine Dust jumps to Pristine. Failure keeps it Latent but still rerolls its attunement."
      );
      reforging.addProperty("tier_1_to_3_chance", 0.25);
      return reforging;
   }

   private static JsonObject defaultExperienceCostsJson() {
      JsonObject experience = new JsonObject();
      experience.addProperty(
         "_comment", "Runic Attunement spends raw XP points, never experience levels. Per-level base point costs come from enchantment datapack definitions."
      );
      experience.addProperty(
         "_help_enabled",
         "Master switch for all Runic Attunement XP-point costs. false makes Attunement and Transmutation cost 0 XP but leaves material costs intact."
      );
      experience.addProperty("enabled", true);
      experience.addProperty(
         "_help_global_multiplier",
         "Scales all Runic XP-point costs. 1.0 = normal, 0.5 = half, 2.0 = double. Final costs round up to the next whole point. Must be non-negative."
      );
      experience.addProperty("global_multiplier", 1.0);
      experience.addProperty(
         "_help_transmutation_multiplier",
         "Additional multiplier applied only to Transmutation after global_multiplier. The base Transmutation XP value is the cumulative configured XP value of transferred enchantment levels."
      );
      experience.addProperty("transmutation_multiplier", 0.5);
      return experience;
   }

   private static JsonObject defaultTransmutationJson() {
      JsonObject transmutation = new JsonObject();
      transmutation.addProperty(
         "_comment",
         "Controls Runic Transmutation. Selected supported non-curse enchantments move from source to compatible target. Compatible curses follow automatically unless retained on the source at the configured material surcharge."
      );
      transmutation.addProperty("_help_enabled", "Master switch for the Transmutation tab/action. false leaves ordinary Attunement available.");
      transmutation.addProperty("enabled", true);
      transmutation.addProperty(
         "_help_source_item_destruction_chance",
         "Chance from 0.0 to 1.0 that the source item is destroyed after a successful Transmutation. 0.0 = never; 1.0 = always. The target is not affected."
      );
      transmutation.addProperty("source_item_destruction_chance", 0.0);
      transmutation.addProperty(
         "_help_base_enchant_units", "Base material-cost units charged for each selected non-curse enchantment transferred. Must be non-negative."
      );
      transmutation.addProperty("base_enchant_units", 1.0);
      transmutation.addProperty(
         "_help_tier_units_per_tier",
         "Additional material-cost units per Tablet potency tier of each transferred enchantment definition. With 1.0: Latent adds 1, Resonant 2, Pristine 3 units."
      );
      transmutation.addProperty("tier_units_per_tier", 1.0);
      transmutation.addProperty(
         "_help_extra_level_units", "Additional material-cost units for each enchantment level above I. With 1.0, level III adds 2 extra units."
      );
      transmutation.addProperty("extra_level_units", 1.0);
      transmutation.addProperty(
         "_help_curse_retention_cost_multiplier",
         "Scales the datapack curse-retention material surcharge for each curse left on the source instead of transferring. 0.0 makes curse retention free; 1.0 uses normal cost."
      );
      transmutation.addProperty("curse_retention_cost_multiplier", 1.0);
      return transmutation;
   }

   private static boolean ensureCurrentSchema(JsonObject root) {
      boolean changed = false;
      JsonObject rootDefaults = defaultJson();

      for (String key : List.of("_comment", "_config_version", "_help_debug_logging", "_help_blacklisted_enchantments")) {
         JsonElement expected = rootDefaults.get(key);
         if (!root.has(key) || !root.get(key).equals(expected)) {
            root.add(key, expected.deepCopy());
            changed = true;
         }
      }

      if (!root.has("debug_logging")) {
         root.addProperty("debug_logging", false);
         changed = true;
      }

      if (!root.has("blacklisted_enchantments") || !root.get("blacklisted_enchantments").isJsonArray()) {
         root.add("blacklisted_enchantments", new JsonArray());
         changed = true;
      }

      if (root.has("loot_generation") && root.get("loot_generation").isJsonObject()) {
         JsonObject loot = root.getAsJsonObject("loot_generation");
         JsonElement lootComment = defaultLootGenerationJson().get("_comment");
         if (!loot.has("_comment") || !loot.get("_comment").equals(lootComment)) {
            loot.add("_comment", lootComment.deepCopy());
            changed = true;
         }

         if (!loot.has("enabled")) {
            loot.addProperty("enabled", true);
            changed = true;
         }

         if (!loot.has("global_drop_chance_multiplier")) {
            loot.addProperty("global_drop_chance_multiplier", 1.0);
            changed = true;
         }

         if (loot.has("tier_weights") && loot.get("tier_weights").isJsonObject()) {
            JsonObject weights = loot.getAsJsonObject("tier_weights");
            JsonElement weightsComment = defaultLootGenerationJson().getAsJsonObject("tier_weights").get("_comment");
            if (!weights.has("_comment") || !weights.get("_comment").equals(weightsComment)) {
               weights.add("_comment", weightsComment.deepCopy());
               changed = true;
            }

            if (!weights.has("tier_1")) {
               weights.addProperty("tier_1", 65.0);
               changed = true;
            }

            if (!weights.has("tier_2")) {
               weights.addProperty("tier_2", 30.0);
               changed = true;
            }

            if (!weights.has("tier_3")) {
               weights.addProperty("tier_3", 5.0);
               changed = true;
            }
         } else {
            loot.add("tier_weights", defaultLootGenerationJson().getAsJsonObject("tier_weights"));
            changed = true;
         }

         JsonObject lootDefaults = defaultLootGenerationJson();

         for (String key : lootDefaults.keySet()) {
            if (!key.equals("tier_weights")) {
               boolean documentation = key.startsWith("_");
               if (!loot.has(key) || documentation && !loot.get(key).equals(lootDefaults.get(key))) {
                  loot.add(key, lootDefaults.get(key).deepCopy());
                  changed = true;
               }
            }
         }

         JsonObject weightDefaults = lootDefaults.getAsJsonObject("tier_weights");
         JsonObject weights = loot.getAsJsonObject("tier_weights");

         for (String key : weightDefaults.keySet()) {
            boolean documentation = key.startsWith("_");
            if (!weights.has(key) || documentation && !weights.get(key).equals(weightDefaults.get(key))) {
               weights.add(key, weightDefaults.get(key).deepCopy());
               changed = true;
            }
         }
      } else {
         root.add("loot_generation", defaultLootGenerationJson());
         changed = true;
      }

      if (root.has("enchantment_bypass_control") && root.get("enchantment_bypass_control").isJsonObject()) {
         JsonObject bypass = root.getAsJsonObject("enchantment_bypass_control");
         JsonObject defaults = defaultEnchantmentBypassControlJson();

         for (String key : defaults.keySet()) {
            boolean documentation = key.startsWith("_");
            if (!bypass.has(key) || documentation && !bypass.get(key).equals(defaults.get(key))) {
               bypass.add(key, defaults.get(key).deepCopy());
               changed = true;
            }
         }
      } else {
         JsonObject bypass = defaultEnchantmentBypassControlJson();
         JsonObject legacy = root.has("future_mechanics") && root.get("future_mechanics").isJsonObject() ? root.getAsJsonObject("future_mechanics") : null;
         if (legacy != null) {
            copyBooleanIfPresent(legacy, bypass, "disable_natural_enchanted_books", "disable_natural_enchanted_books");
            copyBooleanIfPresent(legacy, bypass, "disable_villager_enchanted_book_trades", "disable_villager_enchanted_book_trades");
            copyBooleanIfPresent(legacy, bypass, "disable_natural_enchanted_gear", "disable_natural_enchanted_gear");
            copyBooleanIfPresent(legacy, bypass, "disable_enchanted_gear_trades", "disable_enchanted_gear_trades");
            copyBooleanIfPresent(legacy, bypass, "disable_anvil_enchanted_book_application", "disable_anvil_enchanting");
         }

         root.add("enchantment_bypass_control", bypass);
         changed = true;
      }

      if (root.has("tablet_reforging") && root.get("tablet_reforging").isJsonObject()) {
         JsonObject reforging = root.getAsJsonObject("tablet_reforging");
         JsonObject defaults = defaultTabletReforgingJson();

         for (String key : defaults.keySet()) {
            boolean documentation = key.startsWith("_");
            if (!reforging.has(key) || documentation && !reforging.get(key).equals(defaults.get(key))) {
               reforging.add(key, defaults.get(key).deepCopy());
               changed = true;
            }
         }
      } else {
         root.add("tablet_reforging", defaultTabletReforgingJson());
         changed = true;
      }

      if (root.has("experience_costs") && root.get("experience_costs").isJsonObject()) {
         JsonObject experience = root.getAsJsonObject("experience_costs");
         JsonObject defaults = defaultExperienceCostsJson();

         for (String key : defaults.keySet()) {
            boolean documentation = key.startsWith("_");
            if (!experience.has(key) || documentation && !experience.get(key).equals(defaults.get(key))) {
               experience.add(key, defaults.get(key).deepCopy());
               changed = true;
            }
         }
      } else {
         root.add("experience_costs", defaultExperienceCostsJson());
         changed = true;
      }

      if (root.has("transmutation") && root.get("transmutation").isJsonObject()) {
         JsonObject transmutation = root.getAsJsonObject("transmutation");
         JsonObject defaults = defaultTransmutationJson();

         for (String key : defaults.keySet()) {
            boolean documentation = key.startsWith("_");
            if (!transmutation.has(key) || documentation && !transmutation.get(key).equals(defaults.get(key))) {
               transmutation.add(key, defaults.get(key).deepCopy());
               changed = true;
            }
         }
      } else {
         root.add("transmutation", defaultTransmutationJson());
         changed = true;
      }

      if (root.has("future_mechanics")) {
         root.remove("future_mechanics");
         changed = true;
      }

      return changed;
   }

   private static void copyBooleanIfPresent(JsonObject source, JsonObject target, String sourceKey, String targetKey) {
      if (source.has(sourceKey)) {
         target.addProperty(targetKey, source.get(sourceKey).getAsBoolean());
      }
   }

   private static boolean getBoolean(JsonObject object, String key, boolean fallback) {
      if (object != null && object.has(key)) {
         try {
            return object.get(key).getAsBoolean();
         } catch (RuntimeException exception) {
            RunicAttunement.LOGGER.warn("Invalid boolean config value for '{}'; using {}", key, fallback);
            return fallback;
         }
      } else {
         return fallback;
      }
   }

   private static double getDouble(JsonObject object, String key, double fallback) {
      if (object != null && object.has(key)) {
         try {
            return object.get(key).getAsDouble();
         } catch (RuntimeException exception) {
            RunicAttunement.LOGGER.warn("Invalid numeric config value for '{}'; using {}", key, fallback);
            return fallback;
         }
      } else {
         return fallback;
      }
   }

   private static double nonNegativeFinite(double value, double fallback, String path) {
      if (Double.isFinite(value) && !(value < 0.0)) {
         return value;
      }

      RunicAttunement.LOGGER.warn("Invalid {} value {}; using {}", new Object[]{path, value, fallback});
      return fallback;
   }

   private static double boundedChance(double value, double fallback, String path) {
      if (Double.isFinite(value) && !(value < 0.0) && !(value > 1.0)) {
         return value;
      }

      RunicAttunement.LOGGER.warn("Invalid {} value {}; using {}", new Object[]{path, value, fallback});
      return fallback;
   }

   private record ConfigState(
      Set<class_2960> blacklistedEnchantments,
      boolean debugLogging,
      RunicConfig.LootGeneration lootGeneration,
      RunicConfig.EnchantmentBypassControl enchantmentBypassControl,
      RunicConfig.TabletReforging tabletReforging,
      RunicConfig.ExperienceCosts experienceCosts,
      RunicConfig.Transmutation transmutation
   ) {
      private static RunicConfig.ConfigState defaults() {
         return new RunicConfig.ConfigState(
            Set.of(),
            false,
            RunicConfig.LootGeneration.defaults(),
            RunicConfig.EnchantmentBypassControl.defaults(),
            RunicConfig.TabletReforging.defaults(),
            RunicConfig.ExperienceCosts.defaults(),
            RunicConfig.Transmutation.defaults()
         );
      }
   }

   public record EnchantmentBypassControl(
      boolean disableNaturalEnchantedBooks,
      boolean disableVillagerEnchantedBookTrades,
      boolean disableNaturalEnchantedGear,
      boolean disableEnchantedGearTrades,
      boolean disableAnvilEnchanting,
      boolean disableFishingEnchantedBooks,
      boolean disableMobSpawnEnchantedGear
   ) {
      private static RunicConfig.EnchantmentBypassControl defaults() {
         return new RunicConfig.EnchantmentBypassControl(true, true, true, true, true, true, true);
      }

      public boolean anyEnabled() {
         return this.disableNaturalEnchantedBooks
            || this.disableVillagerEnchantedBookTrades
            || this.disableNaturalEnchantedGear
            || this.disableEnchantedGearTrades
            || this.disableAnvilEnchanting
            || this.disableFishingEnchantedBooks
            || this.disableMobSpawnEnchantedGear;
      }
   }

   public record ExperienceCosts(boolean enabled, double globalMultiplier, double transmutationMultiplier) {
      private static RunicConfig.ExperienceCosts defaults() {
         return new RunicConfig.ExperienceCosts(true, 1.0, 0.5);
      }
   }

   public record LootGeneration(boolean enabled, double globalDropChanceMultiplier, RunicConfig.TierWeights tierWeights) {
      private static RunicConfig.LootGeneration defaults() {
         return new RunicConfig.LootGeneration(true, 1.0, new RunicConfig.TierWeights(65.0, 30.0, 5.0));
      }

      public double effectiveChance(double baseChance) {
         return Math.max(0.0, Math.min(1.0, baseChance * this.globalDropChanceMultiplier));
      }
   }

   public record TabletReforging(boolean enabled, double tier1To2Chance, double tier2To3Chance, double tier1To3Chance) {
      private static RunicConfig.TabletReforging defaults() {
         return new RunicConfig.TabletReforging(true, 0.5, 0.5, 0.25);
      }

      public double upgradeChance(int startTier, int dustTier) {
         if (startTier == dustTier) {
            return 1.0;
         } else if (startTier == 1 && dustTier == 2) {
            return this.tier1To2Chance;
         } else if (startTier == 2 && dustTier == 3) {
            return this.tier2To3Chance;
         } else {
            return startTier == 1 && dustTier == 3 ? this.tier1To3Chance : 0.0;
         }
      }
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

   public record Transmutation(
      boolean enabled,
      double sourceItemDestructionChance,
      double baseEnchantUnits,
      double tierUnitsPerTier,
      double extraLevelUnits,
      double curseRetentionCostMultiplier
   ) {
      private static RunicConfig.Transmutation defaults() {
         return new RunicConfig.Transmutation(true, 0.0, 1.0, 1.0, 1.0, 1.0);
      }

      public double unitsFor(int tabletTier, int enchantmentLevel) {
         return this.baseEnchantUnits + Math.max(0, tabletTier) * this.tierUnitsPerTier + Math.max(0, enchantmentLevel - 1) * this.extraLevelUnits;
      }
   }
}
