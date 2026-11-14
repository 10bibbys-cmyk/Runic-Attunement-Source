package dev.runicattunement.unravel;

import dev.runicattunement.effect.RunicEffects;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.class_3222;

public final class RunicUnravelRuntime {
   private static final int BUTTON_BASE = 6000;
   private static final String LEGACY_LEDGER = "runic_attunement_investment_v1";
   private static final Pattern UNRAVEL = Pattern.compile("\\\"unravel\\\"\\s*:\\s*\\{([^}]*)\\}", 32);
   private static final String DEFAULT_SECTION = "  \"unravel\": {\n    \"enabled\": true,\n    \"material_refund_percent\": 50,\n    \"xp_refund_percent\": 50\n  }\n";
   private static final int MAX_ENCHANTMENT_LEVEL = 256;
   private static final int MAX_MATERIALS_PER_ID = 65536;
   private static final int MAX_MATERIAL_TYPES = 128;
   private static boolean warned;
   private static final Set<String> REPORTED_DENIALS = Collections.synchronizedSet(new HashSet<>());

   private RunicUnravelRuntime() {
   }

   public static Object accessField(Object var0, String var1) {
      if (var0 == null) {
         return null;
      }

      try {
         for (Class var2 = var0 instanceof Class ? (Class)var0 : var0.getClass(); var2 != null; var2 = var2.getSuperclass()) {
            try {
               Field var3 = var2.getDeclaredField(var1);
               var3.setAccessible(true);
               return var3.get(var0 instanceof Class ? null : var0);
            } catch (NoSuchFieldException var4) {
            }
         }
      } catch (Throwable var5) {
      }

      return null;
   }

   private static boolean fits(Class<?> var0, Object var1) {
      if (var1 == null) {
         return !var0.isPrimitive();
      }

      if (var0.isPrimitive()) {
         if (var0 == int.class) {
            return var1 instanceof Integer;
         } else if (var0 == boolean.class) {
            return var1 instanceof Boolean;
         } else if (var0 == double.class) {
            return var1 instanceof Double;
         } else if (var0 == long.class) {
            return var1 instanceof Long;
         } else if (var0 == float.class) {
            return var1 instanceof Float;
         } else if (var0 == short.class) {
            return var1 instanceof Short;
         } else {
            return var0 == byte.class ? var1 instanceof Byte : false;
         }
      } else {
         return var0.isAssignableFrom(var1.getClass());
      }
   }

   public static Object invoke(Object var0, String var1, Object... var2) {
      if (var0 == null) {
         throw new IllegalArgumentException("Null receiver for " + var1);
      }

      Class var3 = var0 instanceof Class ? (Class)var0 : var0.getClass();

      for (Class var4 = var3; var4 != null; var4 = var4.getSuperclass()) {
         for (Method var8 : var4.getDeclaredMethods()) {
            if (var8.getName().equals(var1) && var8.getParameterCount() == var2.length) {
               Class[] var9 = var8.getParameterTypes();
               boolean var10 = true;

               for (int var11 = 0; var11 < var2.length; var11++) {
                  if (!fits(var9[var11], var2[var11])) {
                     var10 = false;
                     break;
                  }
               }

               if (var10) {
                  try {
                     var8.setAccessible(true);
                     return var8.invoke(var0 instanceof Class ? null : var0, var2);
                  } catch (InvocationTargetException var12) {
                     throw new IllegalStateException("Invoking " + var1, var12.getCause());
                  } catch (ReflectiveOperationException var13) {
                     throw new IllegalStateException("Invoking " + var1, var13);
                  }
               }
            }
         }
      }

      for (Method var19 : var3.getMethods()) {
         if (var19.getName().equals(var1) && var19.getParameterCount() == var2.length) {
            Class[] var20 = var19.getParameterTypes();
            boolean var21 = true;

            for (int var22 = 0; var22 < var2.length; var22++) {
               if (!fits(var20[var22], var2[var22])) {
                  var21 = false;
                  break;
               }
            }

            if (var21) {
               try {
                  return var19.invoke(var0 instanceof Class ? null : var0, var2);
               } catch (InvocationTargetException var14) {
                  throw new IllegalStateException("Invoking interface " + var1, var14.getCause());
               } catch (ReflectiveOperationException var15) {
                  throw new IllegalStateException("Invoking interface " + var1, var15);
               }
            }
         }
      }

      throw new IllegalStateException("Missing " + var3.getName() + "." + var1 + "/" + var2.length);
   }

   private static Class<?> mc(String var0) throws ClassNotFoundException {
      return Class.forName("net.minecraft." + var0);
   }

   private static int number(Object var0) {
      return var0 instanceof Number ? ((Number)var0).intValue() : 0;
   }

   private static void warn(String var0, Throwable var1) {
      if (!warned) {
         warned = true;
         System.err.println("[Runic Unravel] " + var0 + " failed safely: " + var1);
      }
   }

   private static double configNumber(String var0, String var1, double var2) {
      Matcher var4 = Pattern.compile("\\\"" + Pattern.quote(var1) + "\\\"\\s*:\\s*(-?[0-9]+(?:\\.[0-9]+)?)").matcher(var0);
      if (!var4.find()) {
         return var2;
      }

      try {
         return Double.parseDouble(var4.group(1));
      } catch (NumberFormatException var6) {
         return var2;
      }
   }

   private static int percent(String var0, String var1, String var2) {
      double var3 = configNumber(var0, var1, Double.NaN);
      if (!Double.isFinite(var3)) {
         var3 = 100.0 * configNumber(var0, var2, 0.5);
      }

      if (!Double.isFinite(var3)) {
         var3 = 50.0;
      }

      return (int)Math.round(Math.max(0.0, Math.min(100.0, var3)));
   }

   private static RunicUnravelRuntime.Config parseConfigSection(String var0) {
      RunicUnravelRuntime.Config var1 = new RunicUnravelRuntime.Config();
      var1.materials = percent(var0, "material_refund_percent", "material_refund_multiplier");
      var1.xp = percent(var0, "xp_refund_percent", "xp_refund_multiplier");
      Matcher var2 = Pattern.compile("\\\"enabled\\\"\\s*:\\s*(true|false)", 2).matcher(var0);
      if (var2.find()) {
         var1.enabled = Boolean.parseBoolean(var2.group(1));
      }

      return var1;
   }

   private static RunicUnravelRuntime.Config config() {
      RunicUnravelRuntime.Config var0 = new RunicUnravelRuntime.Config();

      try {
         Path var1 = (Path)invoke(Class.forName("dev.runicattunement.config.RunicConfig"), "path");
         if (Files.isRegularFile(var1)) {
            Matcher var2 = UNRAVEL.matcher(Files.readString(var1, StandardCharsets.UTF_8));
            if (var2.find()) {
               var0 = parseConfigSection(var2.group(1));
            }
         }
      } catch (Throwable var3) {
         warn("reading configuration", var3);
      }

      return var0;
   }

   public static void ensureConfig() {
      try {
         Path var0 = (Path)invoke(Class.forName("dev.runicattunement.config.RunicConfig"), "path");
         if (!Files.isRegularFile(var0)) {
            return;
         }

         String var1 = Files.readString(var0, StandardCharsets.UTF_8);
         Matcher var2 = UNRAVEL.matcher(var1);
         String var3;
         if (var2.find()) {
            String var4 = var2.group(1);
            if (var4.contains("material_refund_percent") && var4.contains("xp_refund_percent")) {
               return;
            }

            RunicUnravelRuntime.Config var5 = parseConfigSection(var4);
            String var6 = "\"unravel\": {\n    \"enabled\": "
               + var5.enabled
               + ",\n    \"material_refund_percent\": "
               + var5.materials
               + ",\n    \"xp_refund_percent\": "
               + var5.xp
               + "\n  }";
            var3 = var2.replaceFirst(Matcher.quoteReplacement(var6));
         } else {
            int var8 = var1.lastIndexOf(125);
            if (var8 < 0) {
               return;
            }

            String var9 = var1.substring(0, var8).stripTrailing();
            var3 = var9
               + (var9.endsWith("{") ? "\n" : ",\n")
               + "  \"unravel\": {\n    \"enabled\": true,\n    \"material_refund_percent\": 50,\n    \"xp_refund_percent\": 50\n  }\n"
               + var1.substring(var8);
         }

         Files.writeString(var0, var3, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
      } catch (Throwable var7) {
         warn("updating configuration", var7);
      }
   }

   private static Object id(String var0) throws Exception {
      return mc("class_2960").getConstructor(String.class).newInstance(var0);
   }

   private static Object lookupRegistry(String var0, String var1) throws Exception {
      Object var2 = accessField(mc("class_7923"), var1);
      return ((Optional)invoke(var2, "method_17966", id(var0))).orElse(null);
   }

   private static Map<Object, Integer> enchantments(Object var0) throws Exception {
      return (Map<Object, Integer>)invoke(mc("class_1890"), "method_8222", var0);
   }

   public static boolean canUnravel(Object var0, Object var1) {
      if (var0 != null && var1 != null) {
         try {
            Object var2 = invoke(var0, "getEquipmentStack");
            if (var2 != null && !Boolean.TRUE.equals(invoke(var2, "method_7960"))) {
               Object var3 = lookupRegistry(String.valueOf(var1), "field_41176");
               if (var3 == null) {
                  return false;
               }

               int var4 = number(enchantments(var2).get(var3));
               if (var4 >= 1 && var4 <= 256) {
                  Object var5 = invoke(var0, "getDefinition", var1);
                  if (var5 == null) {
                     return false;
                  }

                  for (int var6 = 1; var6 <= var4; var6++) {
                     Optional var7 = (Optional)invoke(var5, "level", var6);
                     if (var7.isEmpty()) {
                        return false;
                     }
                  }

                  return true;
               } else {
                  return false;
               }
            } else {
               return false;
            }
         } catch (Throwable var8) {
            return false;
         }
      } else {
         return false;
      }
   }

   static int refund(int var0, int var1) {
      return (int)((long)Math.max(0, var0) * Math.max(0, Math.min(100, var1)) / 100L);
   }

   private static RunicUnravelRuntime.Quote quote(Object var0, int var1, RunicUnravelRuntime.Config var2) throws Exception {
      if (var1 >= 1 && var1 <= 256) {
         Class var3 = Class.forName("dev.runicattunement.data.RunicDefinitionManager");
         Object var4 = invoke(var3, "get", var0);
         if (var4 == null) {
            return null;
         }

         Object var5 = invoke(Class.forName("dev.runicattunement.config.RunicConfig"), "experienceCosts");
         Class var6 = Class.forName("dev.runicattunement.util.RunicExperience");
         LinkedHashMap<String, Long> var7 = new LinkedHashMap<>();
         long var8 = 0L;

         for (int var10 = 1; var10 <= var1; var10++) {
            Optional var11 = (Optional)invoke(var4, "level", var10);
            if (var11.isEmpty()) {
               return null;
            }

            int var12 = number(invoke(var6, "attunementCost", var4, var10, var5));
            if (var12 < 0) {
               return null;
            }

            var8 += var12;
            if (var8 > 2147483647L) {
               return null;
            }

            List var13 = (List)invoke(var3, "combinedCosts", var4, var10);
            if (var13 == null) {
               return null;
            }

            for (Object var15 : var13) {
               String var16 = String.valueOf(invoke(var15, "itemId"));
               int var17 = number(invoke(var15, "count"));
               if (var17 < 0) {
                  return null;
               }

               long var18 = var7.getOrDefault(var16, 0L) + var17;
               if (var18 > 65536L) {
                  return null;
               }

               var7.put(var16, var18);
               if (var7.size() > 128) {
                  return null;
               }
            }
         }

         LinkedHashMap var20 = new LinkedHashMap();

         for (Entry var22 : var7.entrySet()) {
            int var23 = refund(((Long)var22.getValue()).intValue(), var2.materials);
            if (var23 > 0) {
               if (lookupRegistry((String)var22.getKey(), "field_41178") == null) {
                  return null;
               }

               var20.put((String)var22.getKey(), var23);
            }
         }

         return new RunicUnravelRuntime.Quote(var20, refund((int)var8, var2.xp));
      } else {
         return null;
      }
   }

   private static void refundItem(Object var0, String var1, int var2) throws Exception {
      if (var2 > 0) {
         Object var3 = lookupRegistry(var1, "field_41178");
         if (var3 == null) {
            throw new IllegalStateException("Removed refund item: " + var1);
         }

         Object var4 = invoke(var0, "method_31548");
         Class var5 = mc("class_1799");
         Constructor var6 = null;

         for (Constructor var10 : var5.getConstructors()) {
            Class[] var11 = var10.getParameterTypes();
            if (var11.length == 2 && var11[1] == int.class && var11[0].isInstance(var3)) {
               var6 = var10;
               break;
            }
         }

         if (var6 == null) {
            throw new IllegalStateException("ItemStack constructor not found");
         }

         int var12 = Math.max(1, Math.min(64, number(invoke(var3, "method_7882"))));
         int var13 = var2;

         while (var13 > 0) {
            int var14 = Math.min(var13, var12);
            Object var15 = var6.newInstance(var3, var14);
            if (!Boolean.TRUE.equals(invoke(var4, "method_7394", var15))) {
               invoke(var0, "method_7328", var15, false);
            }

            var13 -= var14;
         }
      }
   }

   private static void clearOldLedger(Object var0, String var1) {
      try {
         Object var2 = invoke(var0, "method_7969");
         if (var2 == null) {
            return;
         }

         if (!Boolean.TRUE.equals(invoke(var2, "method_10545", "runic_attunement_investment_v1"))) {
            return;
         }

         Object var3 = invoke(var2, "method_10562", "runic_attunement_investment_v1");
         invoke(var3, "method_10551", var1);
         if (((Collection)invoke(var3, "method_10541")).isEmpty()) {
            invoke(var2, "method_10551", "runic_attunement_investment_v1");
         }
      } catch (Throwable var4) {
      }
   }

   private static int deny(String var0) {
      if (REPORTED_DENIALS.add(var0)) {
         System.err.println("[Runic Unravel] Action refused: " + var0);
      }

      return 0;
   }

   public static boolean handleMpAction(Object var0, int var1, int var2) {
      if (var1 == 0 && var2 >= 6000 && var2 < 7000) {
         try {
            Class var3 = Class.forName("pota.runicmpfix.RunicMpFix");
            Method var4 = var3.getDeclaredMethod("findRunicHandler", mc("class_3222"));
            var4.setAccessible(true);
            Object var5 = var4.invoke(null, var0);
            if (var5 == null) {
               deny("no Runic Attunement screen open");
               feedback(var0, "UNRAVEL rejected: reopen the Attunement Table.");
               return true;
            }

            int var6 = handleButton(var5, var0, var2);
            if (var6 != 1) {
               feedback(var0, "UNRAVEL rejected. See latest.log for [Runic Unravel].");
            }
         } catch (Throwable var7) {
            System.err.println("[Runic Unravel] Multiplayer action failed: " + var7);
            var7.printStackTrace(System.err);
            feedback(var0, "UNRAVEL failed internally. Check latest.log.");
         }

         return true;
      } else {
         return false;
      }
   }

   private static void feedback(Object var0, String var1) {
      if (var0 != null) {
         try {
            Object var2 = invoke(mc("class_2561"), "method_43470", var1);
            invoke(var0, "method_7353", var2, true);
         } catch (Throwable var3) {
         }
      }
   }

   public static int handleButton(Object var0, Object var1, int var2) {
      if (var2 >= 6000 && var2 < 7000) {
         try {
            if (!mc("class_3222").isInstance(var1)) {
               return deny("not a server player");
            }

            if (Boolean.TRUE.equals(invoke(var0, "isTransmutationMode"))) {
               return deny("table in Transmutation mode");
            }

            if (!Boolean.TRUE.equals(invoke(var0, "method_7597", var1))) {
               return deny("workstation out of reach");
            }

            RunicUnravelRuntime.Config var3 = config();
            if (!var3.enabled) {
               return deny("disabled by configuration");
            }

            List var4 = (List)invoke(var0, "getAttunedEnchantments");
            int var5 = var2 - 6000;
            if (var5 >= 0 && var5 < var4.size()) {
               Object var6 = var4.get(var5);
               Object var7 = invoke(var0, "getEquipmentStack");
               if (var7 != null && !Boolean.TRUE.equals(invoke(var7, "method_7960"))) {
                  Object var8 = lookupRegistry(String.valueOf(var6), "field_41176");
                  if (var8 == null) {
                     return deny("unregistered enchantment");
                  }

                  LinkedHashMap var9 = new LinkedHashMap<>(enchantments(var7));
                  int var10 = number(var9.get(var8));
                  if (var10 >= 1 && var10 <= 256) {
                     Object var11 = invoke(Class.forName("dev.runicattunement.data.RunicDefinitionManager"), "get", var6);
                     if (var11 != null && var11.equals(invoke(var0, "getDefinition", var6))) {
                        RunicUnravelRuntime.Quote var12 = quote(var6, var10, var3);
                        if (var12 == null) {
                           return deny("no valid refund quote for enchantment level");
                        }

                        var9.remove(var8);
                        invoke(mc("class_1890"), "method_8214", var9, var7);
                        clearOldLedger(var7, String.valueOf(var6));

                        for (Entry var14 : var12.materials.entrySet()) {
                           refundItem(var1, (String)var14.getKey(), (Integer)var14.getValue());
                        }

                        if (var12.xp > 0) {
                           invoke(var1, "method_7255", var12.xp);
                        }

                        Object var16 = accessField(var0, "workInventory");
                        if (var16 != null) {
                           invoke(var16, "method_5431");
                        }

                        invoke(var0, "method_7623");
                        RunicEffects.unravelSuccess((class_3222)var1);
                        return 1;
                     } else {
                        return deny("cost definitions stale - reopen table");
                     }
                  } else {
                     return deny("selected enchantment no longer on item");
                  }
               } else {
                  return deny("missing equipment");
               }
            } else {
               return deny("invalid enchantment list index");
            }
         } catch (Throwable var15) {
            System.err.println("[Runic Unravel] Server action failed: " + var15);
            var15.printStackTrace(System.err);
            return 0;
         }
      } else {
         return -1;
      }
   }

   private static final class Config {
      boolean enabled = true;
      int materials = 50;
      int xp = 50;
   }

   private static final class Quote {
      final Map<String, Integer> materials;
      final int xp;

      Quote(Map<String, Integer> var1, int var2) {
         this.materials = var1;
         this.xp = var2;
      }
   }
}
