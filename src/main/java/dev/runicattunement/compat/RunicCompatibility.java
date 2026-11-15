package dev.runicattunement.compat;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.runicattunement.RunicAttunement;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.ServerStopped;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.class_1799;
import net.minecraft.class_1887;
import net.minecraft.class_2540;
import net.minecraft.class_2960;
import net.minecraft.class_3264;
import net.minecraft.class_3298;
import net.minecraft.class_3300;
import net.minecraft.class_6862;
import net.minecraft.class_7923;
import net.minecraft.class_7924;

public final class RunicCompatibility {
   private static final String PREFIX = "runic_attunement/compatibility";
   private static final int MAX_SYNC_BYTES = 131072;
   private static volatile Map<String, RunicCompatibility.Rule> serverRules = Map.of();
   private static volatile Map<String, RunicCompatibility.Rule> clientRules = Map.of();
   private static volatile boolean serverInitialized = false;

   private RunicCompatibility() {
   }

   public static void initialize() {
      ResourceManagerHelper.get(class_3264.field_14190).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
         public class_2960 getFabricId() {
            return RunicAttunement.id("enchantment_compatibility");
         }

         public void method_14491(class_3300 var1) {
            RunicCompatibility.reload(var1);
         }
      });
      // This 1.3.8 source-recovery build uses intermediary Minecraft names, while
      // the packaged Fabric lifecycle API may resolve MinecraftServer by its named
      // descriptor. A normal lambda can therefore throw AbstractMethodError when
      // SERVER_STOPPED fires. Proxy the runtime interface to match its actual ABI.
      ServerStopped stoppedListener = (ServerStopped) java.lang.reflect.Proxy.newProxyInstance(
         ServerStopped.class.getClassLoader(),
         new Class<?>[]{ServerStopped.class},
         (proxy, method, args) -> {
            if (method.getName().equals("onServerStopped") && method.getParameterCount() == 1) {
               serverRules = Map.of();
               serverInitialized = false;
               return null;
            }
            if (method.getDeclaringClass() == Object.class) {
               return switch (method.getName()) {
                  case "equals" -> proxy == args[0];
                  case "hashCode" -> System.identityHashCode(proxy);
                  case "toString" -> "RunicAttunementServerStoppedListener";
                  default -> throw new UnsupportedOperationException(method.toString());
               };
            }
            throw new UnsupportedOperationException(method.toString());
         });
      ServerLifecycleEvents.SERVER_STOPPED.register(stoppedListener);
   }

   private static synchronized void reload(class_3300 var0) {
      LinkedHashMap var1 = new LinkedHashMap();
      int var2 = 0;

      for (Entry var4 : var0.method_14488("runic_attunement/compatibility", var0x -> var0x.method_12832().endsWith(".json")).entrySet()) {
         try (InputStreamReader var5 = new InputStreamReader(((class_3298)var4.getValue()).method_14482(), StandardCharsets.UTF_8)) {
            JsonObject var6 = JsonParser.parseReader(var5).getAsJsonObject();
            RunicCompatibility.Rule var7 = parse(var6);
            if (var1.putIfAbsent(var7.enchantment(), var7) != null) {
               RunicAttunement.LOGGER.warn("Runic compatibility: duplicate rule for {} at {}; keeping first", new Object[]{var7.enchantment(), var4.getKey()});
               var2++;
            }
         } catch (Exception var10) {
            var2++;
            RunicAttunement.LOGGER.warn("Runic compatibility: invalid rule {}: {}", new Object[]{var4.getKey(), var10.toString()});
         }
      }

      serverRules = Map.copyOf(var1);
      serverInitialized = true;
      RunicAttunement.LOGGER.info("Runic compatibility loaded {} override(s), {} rejected; native rules remain the fallback", new Object[]{var1.size(), var2});
   }

   private static RunicCompatibility.Rule parse(JsonObject var0) {
      if (var0 != null && var0.has("enchantment")) {
         String var1 = var0.get("enchantment").getAsString();
         validateId(var1);
         String var2 = var0.has("mode") ? var0.get("mode").getAsString().toLowerCase(Locale.ROOT) : "extend";
         if (!var2.equals("extend") && !var2.equals("replace")) {
            throw new IllegalArgumentException("mode must be extend or replace");
         }

         List var3 = parseList(var0, "allow");
         List var4 = parseList(var0, "deny");
         return new RunicCompatibility.Rule(var1, var2.equals("replace"), selectors(var3), selectors(var4), List.copyOf(var3), List.copyOf(var4));
      } else {
         throw new IllegalArgumentException("Missing enchantment ID");
      }
   }

   private static List<String> parseList(JsonObject var0, String var1) {
      ArrayList var2 = new ArrayList();
      if (!var0.has(var1)) {
         return var2;
      }

      JsonElement var3 = var0.get(var1);
      if (!var3.isJsonArray()) {
         throw new IllegalArgumentException(var1 + " must be a JSON array");
      }

      JsonArray var4 = var3.getAsJsonArray();
      if (var4.size() > 1024) {
         throw new IllegalArgumentException(var1 + " exceeds 1024 entries");
      }

      for (JsonElement var6 : var4) {
         String var7 = var6.getAsString();
         validateId(var7.startsWith("#") ? var7.substring(1) : var7);
         var2.add(var7);
      }

      return var2;
   }

   private static void validateId(String var0) {
      if (var0.length() > 256 || !var0.matches("[a-z0-9_.-]+:[a-z0-9_./-]+")) {
         throw new IllegalArgumentException("Invalid namespaced identifier: " + var0);
      }
   }

   private static List<RunicCompatibility.Selector> selectors(List<String> var0) {
      ArrayList var1 = new ArrayList();

      for (String var3 : var0) {
         if (var3.startsWith("#")) {
            class_6862 var4 = class_6862.method_40092(class_7924.field_41197, new class_2960(var3.substring(1)));
            var1.add(new RunicCompatibility.Selector(var3, null, var4));
         } else {
            var1.add(new RunicCompatibility.Selector(var3, var3, null));
         }
      }

      return List.copyOf(var1);
   }

   public static boolean accepts(class_1887 var0, class_1799 var1) {
      boolean var2 = var0.method_8192(var1);
      class_2960 var3 = class_7923.field_41176.method_10221(var0);
      if (var3 == null) {
         return var2;
      }

      RunicCompatibility.Rule var4 = (serverInitialized ? serverRules : clientRules).get(var3.toString());
      if (var4 == null) {
         return var2;
      }

      class_2960 var5 = class_7923.field_41178.method_10221(var1.method_7909());
      return var4.accepts(var2, var1, var5 == null ? "" : var5.toString());
   }

   public static void writeSync(class_2540 var0) {
      JsonArray var1 = new JsonArray();

      for (RunicCompatibility.Rule var3 : serverRules.values()) {
         JsonObject var4 = new JsonObject();
         var4.addProperty("enchantment", var3.enchantment());
         var4.addProperty("mode", var3.replace() ? "replace" : "extend");
         JsonArray var5 = new JsonArray();

         for (String var7 : var3.rawAllow()) {
            var5.add(var7);
         }

         JsonArray var10 = new JsonArray();

         for (String var8 : var3.rawDeny()) {
            var10.add(var8);
         }

         var4.add("allow", var5);
         var4.add("deny", var10);
         var1.add(var4);
      }

      byte[] var9 = var1.toString().getBytes(StandardCharsets.UTF_8);
      if (var9.length > 131072) {
         RunicAttunement.LOGGER.warn("Runic compatibility sync is too large ({} bytes); sending no overrides", new Object[]{var9.length});
         var9 = "[]".getBytes(StandardCharsets.UTF_8);
      }

      var0.method_10804(var9.length);
      var0.writeBytes(var9);
   }

   public static void readSync(class_2540 var0) {
      int var1 = var0.method_10816();
      if (var1 >= 0 && var1 <= 131072) {
         byte[] var2 = new byte[var1];
         var0.readBytes(var2);
         LinkedHashMap var3 = new LinkedHashMap();
         JsonElement var4 = JsonParser.parseString(new String(var2, StandardCharsets.UTF_8));
         if (!var4.isJsonArray()) {
            throw new IllegalArgumentException("Invalid compatibility sync JSON");
         }

         for (JsonElement var6 : var4.getAsJsonArray()) {
            RunicCompatibility.Rule var7 = parse(var6.getAsJsonObject());
            if (var3.putIfAbsent(var7.enchantment(), var7) != null) {
               throw new IllegalArgumentException("Duplicate synchronized enchantment ID");
            }
         }

         clientRules = Map.copyOf(var3);
      } else {
         throw new IllegalArgumentException("Runic compatibility sync exceeds maximum size");
      }
   }

   private record Rule(
      String enchantment,
      boolean replace,
      List<RunicCompatibility.Selector> allow,
      List<RunicCompatibility.Selector> deny,
      List<String> rawAllow,
      List<String> rawDeny
   ) {
      boolean matches(List<RunicCompatibility.Selector> var1, class_1799 var2, String var3) {
         for (RunicCompatibility.Selector var5 : var1) {
            if (var5.matches(var2, var3)) {
               return true;
            }
         }

         return false;
      }

      boolean accepts(boolean var1, class_1799 var2, String var3) {
         if (this.matches(this.deny, var2, var3)) {
            return false;
         } else {
            return this.replace ? this.matches(this.allow, var2, var3) : var1 || this.matches(this.allow, var2, var3);
         }
      }
   }

   private record Selector(String source, String item, class_6862 tag) {
      boolean matches(class_1799 var1, String var2) {
         return this.tag != null ? var1.method_31573(this.tag) : this.item.equals(var2);
      }
   }
}
