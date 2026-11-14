package dev.runicattunement.compat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

public final class EnchantedTooltipsBridge {
   private static final String ET = "net.kyrptonaught.enchantedtooltips.EnchantToolTipHelper";
   private static final String ET_MOD = "net.kyrptonaught.enchantedtooltips.EnchantedToolTipMod";
   private static final Map<Object, EnchantedTooltipsBridge.Popup> POPUPS = Collections.synchronizedMap(new WeakHashMap<>());
   private static volatile Boolean present;
   private static volatile boolean errorLogged;
   private static Method descriptionMethod;
   private static Method configMethod;
   private static Field showDescField;
   private static Field showAlwaysField;
   private static Field showPromptField;
   private static Field enableForBooksField;

   private EnchantedTooltipsBridge() {
   }

   private static boolean available() {
      Boolean var0 = present;
      if (var0 != null) {
         return var0;
      }

      try {
         Class.forName("net.kyrptonaught.enchantedtooltips.EnchantedToolTipMod", false, EnchantedTooltipsBridge.class.getClassLoader());
         Class var1 = Class.forName("net.kyrptonaught.enchantedtooltips.EnchantToolTipHelper", false, EnchantedTooltipsBridge.class.getClassLoader());
         descriptionMethod = var1.getDeclaredMethod("getEnchantDesc", String.class);
         descriptionMethod.setAccessible(true);
         Class var2 = Class.forName("net.kyrptonaught.enchantedtooltips.EnchantedToolTipMod", false, EnchantedTooltipsBridge.class.getClassLoader());
         configMethod = var2.getMethod("getConfig");
         Class var3 = configMethod.getReturnType();
         showDescField = var3.getField("displayDescription");
         showAlwaysField = var3.getField("alwaysShowEnchantInfo");
         showPromptField = var3.getField("displayPressForInfo");
         enableForBooksField = var3.getField("enableForBooks");
         present = true;
         return true;
      } catch (ReflectiveOperationException | LinkageError var4) {
         present = false;
         return false;
      }
   }

   private static boolean setting(Field var0) throws ReflectiveOperationException {
      return var0.getBoolean(configMethod.invoke(null));
   }

   private static String descriptionKey(String var0) {
      return "enchantment." + var0.replace(':', '.') + ".desc";
   }

   private static Object translatedDescription(String var0) throws ReflectiveOperationException {
      return descriptionMethod.invoke(null, descriptionKey(var0));
   }

   private static String textString(Object var0) throws ReflectiveOperationException {
      return (String)var0.getClass().getMethod("getString").invoke(var0);
   }

   private static String usableDescription(Object var0, String var1) throws ReflectiveOperationException {
      String var2 = textString(var0);
      return !var2.isBlank() && !var2.equals(descriptionKey(var1)) ? var2 : null;
   }

   private static Method findMethod(Class<?> var0, String var1, int var2) throws NoSuchMethodException {
      for (Class var3 = var0; var3 != null; var3 = var3.getSuperclass()) {
         for (Method var7 : var3.getDeclaredMethods()) {
            if (var7.getName().equals(var1) && var7.getParameterCount() == var2) {
               var7.setAccessible(true);
               return var7;
            }
         }
      }

      throw new NoSuchMethodException(var0 + "." + var1);
   }

   private static Field findField(Class<?> var0, String var1) throws NoSuchFieldException {
      for (Class var2 = var0; var2 != null; var2 = var2.getSuperclass()) {
         try {
            Field var3 = var2.getDeclaredField(var1);
            var3.setAccessible(true);
            return var3;
         } catch (NoSuchFieldException var4) {
         }
      }

      throw new NoSuchFieldException(var0 + "." + var1);
   }

   private static void removeOriginalShiftHint(List var0) throws ReflectiveOperationException {
      if (!var0.isEmpty()) {
         Class var1 = Class.forName("net.minecraft.class_2561");
         Object var2 = var1.getMethod("method_43471", String.class).invoke(null, "tooltip.runic_attunement.hold_shift");
         String var3 = textString(var2);
         Object var4 = var0.get(var0.size() - 1);
         if (var4 != null && var3.equals(textString(var4))) {
            var0.remove(var0.size() - 1);
         }
      }
   }

   public static void appendTabletTooltip(Object var0, List var1) {
      if (available() && var0 != null && var1 != null) {
         try {
            if (!setting(enableForBooksField) || !setting(showDescField)) {
               return;
            }

            Class var2 = Class.forName("dev.runicattunement.item.RunicTabletItem");
            if (!(findMethod(var2, "getEnchantmentId", 1).invoke(null, var0) instanceof Optional var4 && !var4.isEmpty())) {
               return;
            }

            String var5 = var4.get().toString();
            Object var6 = translatedDescription(var5);
            if (usableDescription(var6, var5) == null) {
               return;
            }

            boolean var7 = setting(showAlwaysField);
            if (!var7) {
               Class var8 = Class.forName("net.minecraft.class_437");
               var7 = Boolean.TRUE.equals(var8.getMethod("method_25442").invoke(null));
            }

            if (var7) {
               removeOriginalShiftHint(var1);
               var1.add(var6);
            }
         } catch (ReflectiveOperationException | LinkageError | RuntimeException var9) {
            warnOnce("tablet", var9);
         }
      }
   }

   private static void warnOnce(String var0, Throwable var1) {
      if (!errorLogged) {
         errorLogged = true;
         System.err.println("[Runic Attunement/Enchanted ToolTips] Optional " + var0 + " integration disabled: " + var1);
      }
   }

   private static boolean transmutation(Object var0) throws ReflectiveOperationException {
      Object var1 = findField(var0.getClass(), "field_2797").get(var0);
      return (Boolean)findMethod(var1.getClass(), "isTransmutationMode", 0).invoke(var1);
   }

   private static List<Object> entries(Object var0, boolean var1) throws ReflectiveOperationException {
      if (!var1) {
         return (List<Object>)findMethod(var0.getClass(), "filteredEnchantments", 0).invoke(var0);
      }

      Object var2 = findField(var0.getClass(), "field_2797").get(var0);
      Object var3 = findMethod(var2.getClass(), "getSourceStack", 0).invoke(var2);
      Class var4 = Class.forName("dev.runicattunement.transmutation.TransmutationRules");
      return (List<Object>)findMethod(var4, "sourceEnchantments", 1).invoke(null, var3);
   }

   private static int numberField(Object var0, String var1) throws ReflectiveOperationException {
      return findField(var0.getClass(), var1).getInt(var0);
   }

   private static Object infoEntry(Object var0, boolean var1, int var2) throws ReflectiveOperationException {
      List var3 = entries(var0, var1);
      int var4 = numberField(var0, var1 ? "transmutationScroll" : "scrollOffset");
      int var5 = var4 + var2;
      return var5 >= 0 && var5 < var3.size() ? var3.get(var5) : null;
   }

   private static String title(String var0) {
      try {
         Class var1 = Class.forName("net.minecraft.class_2561");
         int var2 = var0.indexOf(58);
         String var3 = "enchantment." + var0.substring(0, var2) + "." + var0.substring(var2 + 1);
         Object var4 = var1.getMethod("method_43471", String.class).invoke(null, var3);
         String var5 = textString(var4);
         if (!var5.equals(var3)) {
            return var5;
         }
      } catch (ReflectiveOperationException | RuntimeException var6) {
      }

      String var7 = var0.substring(var0.indexOf(58) + 1).replace('_', ' ');
      return var7.isEmpty() ? var0 : var7.substring(0, 1).toUpperCase(Locale.ROOT) + var7.substring(1);
   }

   public static boolean clickInfo(Object var0, double var1, double var3, int var5) {
      if (available() && var5 == 0) {
         try {
            boolean var6 = transmutation(var0);
            int var7 = numberField(var0, "field_2776");
            int var8 = numberField(var0, "field_2800");
            int var9 = var7 + (var6 ? 192 : 275);
            int var10 = var8 + (var6 ? 62 : 94);
            int var11 = var6 ? 4 : 5;
            int var12 = var6 ? 18 : 13;
            int var13 = var6 ? 17 : 12;

            for (int var14 = 0; var14 < var11; var14++) {
               int var15 = var10 + var14 * var12;
               if (var1 >= var9 && var1 < var9 + 10 && var3 >= var15 && var3 < var15 + var13) {
                  Object var16 = infoEntry(var0, var6, var14);
                  if (var16 == null) {
                     return false;
                  }

                  String var17 = var16.toString();
                  EnchantedTooltipsBridge.Popup var18 = POPUPS.get(var0);
                  if (var18 != null && var18.id().equals(var17)) {
                     POPUPS.remove(var0);
                     return true;
                  }

                  Object var19 = translatedDescription(var17);
                  String var20 = usableDescription(var19, var17);
                  if (var20 == null) {
                     var20 = "No Enchanted ToolTips description available.";
                  }

                  POPUPS.put(var0, new EnchantedTooltipsBridge.Popup(var17, title(var17), var20, (int)var1, (int)var3));
                  return true;
               }
            }

            POPUPS.remove(var0);
         } catch (ReflectiveOperationException | RuntimeException | LinkageError var21) {
            warnOnce("table click", var21);
         }

         return false;
      } else {
         return false;
      }
   }

   public static void renderInfo(Object var0, Object var1, int var2, int var3) {
      if (available()) {
         try {
            boolean var4 = transmutation(var0);
            int var5 = numberField(var0, "field_2776");
            int var6 = numberField(var0, "field_2800");
            Object var7 = findField(var0.getClass(), "field_22793").get(var0);
            Class var8 = var1.getClass();
            Method var9 = findMethod(var8, "method_25294", 5);
            Method var10 = findMethod(var8, "method_51433", 6);
            int var11 = var5 + (var4 ? 192 : 275);
            int var12 = var6 + (var4 ? 62 : 94);
            int var13 = var4 ? 4 : 5;
            int var14 = var4 ? 18 : 13;
            int var15 = var4 ? 17 : 12;

            for (int var16 = 0; var16 < var13; var16++) {
               Object var17 = infoEntry(var0, var4, var16);
               if (var17 == null) {
                  break;
               }

               int var18 = var12 + var16 * var14;
               boolean var19 = var2 >= var11 && var2 < var11 + 10 && var3 >= var18 && var3 < var18 + var15;
               var9.invoke(var1, var11, var18 + 1, var11 + 9, var18 + 11, var19 ? -14385273 : -14205878);
               var10.invoke(var1, var7, "i", var11 + 3, var18 + 2, var19 ? -8831 : -8858145, true);
            }

            EnchantedTooltipsBridge.Popup var38 = POPUPS.get(var0);
            if (var38 == null) {
               return;
            }

            Method var39 = findMethod(var7.getClass(), "method_1727", 1);
            ArrayList<String> var40 = new ArrayList<>();
            var40.add(var38.title());
            StringBuilder var41 = new StringBuilder();

            for (String var23 : var38.description().split("\\s+")) {
               String var24 = var41.isEmpty() ? var23 : var41 + " " + var23;
               if (!var41.isEmpty() && (Integer)var39.invoke(var7, var24) > 192) {
                  var40.add(var41.toString());
                  var41 = new StringBuilder(var23);
               } else {
                  var41 = new StringBuilder(var24);
               }
            }

            if (!var41.isEmpty()) {
               var40.add(var41.toString());
            }

            int var42 = 0;

            for (String var46 : var40) {
               var42 = Math.max(var42, (Integer)var39.invoke(var7, var46));
            }

            var42 += 12;
            int var45 = var40.size() * 11 + 9;
            int var47 = numberField(var0, "field_22789");
            int var48 = numberField(var0, "field_22790");
            int var49 = var5 + (var4 ? 232 : 318);
            int var25 = Math.max(var38.x() + 30, var49);
            int var26;
            if (var25 + var42 + 5 <= var47) {
               var26 = var25;
            } else if (var5 - var42 - 12 >= 4) {
               var26 = var5 - var42 - 12;
            } else {
               var26 = Math.max(4, var47 - var42 - 5);
            }

            int var27 = Math.min(var38.y() + 12, Math.max(4, var48 - var45 - 5));
            Object var28 = findMethod(var8, "method_51448", 0).invoke(var1);
            Method var29 = findMethod(var28.getClass(), "method_22903", 0);
            Method var30 = findMethod(var28.getClass(), "method_22904", 3);
            Method var31 = findMethod(var28.getClass(), "method_22909", 0);
            var29.invoke(var28);

            try {
               var30.invoke(var28, 0.0, 0.0, 500.0);
               var9.invoke(var1, var26 - 1, var27 - 1, var26 + var42 + 1, var27 + var45 + 1, -12208443);
               var9.invoke(var1, var26, var27, var26 + var42, var27 + var45, -283827923);

               for (int var32 = 0; var32 < var40.size(); var32++) {
                  var10.invoke(var1, var7, var40.get(var32), var26 + 6, var27 + 5 + var32 * 11, var32 == 0 ? -11654 : -1773070, true);
               }
            } finally {
               var31.invoke(var28);
            }
         } catch (ReflectiveOperationException | RuntimeException | LinkageError var37) {
            warnOnce("table rendering", var37);
         }
      }
   }

   private record Popup(String id, String title, String description, int x, int y) {
   }
}
