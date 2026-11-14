package dev.runicattunement.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.runicattunement.RunicAttunement;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3298;

public final class RunicUiTheme {
   private static final class_2960 THEME_ID = RunicAttunement.id("ui/theme.json");
   private static final class_2960 DEFAULT_TEXTURE = RunicAttunement.id("textures/gui/runic_inscription.png");
   private final class_2960 backgroundTexture;
   private final int title;
   private final int text;
   private final int muted;
   private final int accent;
   private final int accentBright;
   private final int border;
   private final int panel;
   private final int panelHover;
   private final int panelSelected;
   private final int compatible;
   private final int incompatible;
   private final int maximum;
   private final int tier1;
   private final int tier2;
   private final int tier3;
   private final int button;
   private final int buttonHover;
   private final int buttonDisabled;

   private RunicUiTheme(
      class_2960 backgroundTexture,
      int title,
      int text,
      int muted,
      int accent,
      int accentBright,
      int border,
      int panel,
      int panelHover,
      int panelSelected,
      int compatible,
      int incompatible,
      int maximum,
      int tier1,
      int tier2,
      int tier3,
      int button,
      int buttonHover,
      int buttonDisabled
   ) {
      this.backgroundTexture = backgroundTexture;
      this.title = title;
      this.text = text;
      this.muted = muted;
      this.accent = accent;
      this.accentBright = accentBright;
      this.border = border;
      this.panel = panel;
      this.panelHover = panelHover;
      this.panelSelected = panelSelected;
      this.compatible = compatible;
      this.incompatible = incompatible;
      this.maximum = maximum;
      this.tier1 = tier1;
      this.tier2 = tier2;
      this.tier3 = tier3;
      this.button = button;
      this.buttonHover = buttonHover;
      this.buttonDisabled = buttonDisabled;
   }

   public static RunicUiTheme load() {
      RunicUiTheme defaults = defaults();
      class_310 client = class_310.method_1551();
      if (client == null) {
         return defaults;
      }

      try {
         Optional<class_3298> resource = client.method_1478().method_14486(THEME_ID);
         if (resource.isEmpty()) {
            return defaults;
         }

         try (InputStreamReader reader = new InputStreamReader(resource.get().method_14482(), StandardCharsets.UTF_8)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            JsonObject colours = root.has("colours") && root.get("colours").isJsonObject() ? root.getAsJsonObject("colours") : new JsonObject();
            class_2960 texture = defaults.backgroundTexture;
            if (root.has("background_texture")) {
               class_2960 parsed = class_2960.method_12829(root.get("background_texture").getAsString());
               if (parsed != null) {
                  texture = parsed;
               }
            }

            return new RunicUiTheme(
               texture,
               colour(colours, "title", defaults.title),
               colour(colours, "text", defaults.text),
               colour(colours, "muted", defaults.muted),
               colour(colours, "accent", defaults.accent),
               colour(colours, "accent_bright", defaults.accentBright),
               colour(colours, "border", defaults.border),
               colour(colours, "panel", defaults.panel),
               colour(colours, "panel_hover", defaults.panelHover),
               colour(colours, "panel_selected", defaults.panelSelected),
               colour(colours, "compatible", defaults.compatible),
               colour(colours, "incompatible", defaults.incompatible),
               colour(colours, "maximum", defaults.maximum),
               colour(colours, "tier_1", defaults.tier1),
               colour(colours, "tier_2", defaults.tier2),
               colour(colours, "tier_3", defaults.tier3),
               colour(colours, "button", defaults.button),
               colour(colours, "button_hover", defaults.buttonHover),
               colour(colours, "button_disabled", defaults.buttonDisabled)
            );
         }
      } catch (Exception ex) {
         RunicAttunement.LOGGER.warn("Could not load Runic Attunement UI theme; using built-in defaults", ex);
         return defaults;
      }
   }

   private static RunicUiTheme defaults() {
      return new RunicUiTheme(
         DEFAULT_TEXTURE,
         15259814,
         14014679,
         8357255,
         10189128,
         14071925,
         5857642,
         2369839,
         3160128,
         4015696,
         7652746,
         13987176,
         14202206,
         7588243,
         14781264,
         12159462,
         4931635,
         7100217,
         2698288
      );
   }

   private static int colour(JsonObject object, String key, int fallback) {
      if (!object.has(key)) {
         return fallback;
      }

      try {
         String raw = object.get(key).getAsString().trim();
         if (raw.startsWith("#")) {
            raw = raw.substring(1);
         }

         return raw.length() != 6 ? fallback : Integer.parseInt(raw, 16) & 16777215;
      } catch (Exception ignored) {
         return fallback;
      }
   }

   public class_2960 backgroundTexture() {
      return this.backgroundTexture;
   }

   public int title() {
      return this.title;
   }

   public int text() {
      return this.text;
   }

   public int muted() {
      return this.muted;
   }

   public int accent() {
      return this.accent;
   }

   public int accentBright() {
      return this.accentBright;
   }

   public int border() {
      return this.border;
   }

   public int panel() {
      return this.panel;
   }

   public int panelHover() {
      return this.panelHover;
   }

   public int panelSelected() {
      return this.panelSelected;
   }

   public int compatible() {
      return this.compatible;
   }

   public int incompatible() {
      return this.incompatible;
   }

   public int maximum() {
      return this.maximum;
   }

   public int button() {
      return this.button;
   }

   public int buttonHover() {
      return this.buttonHover;
   }

   public int buttonDisabled() {
      return this.buttonDisabled;
   }

   public int tier(int tier) {
      return switch (tier) {
         case 1 -> this.tier1;
         case 2 -> this.tier2;
         case 3 -> this.tier3;
         default -> this.text;
      };
   }
}
