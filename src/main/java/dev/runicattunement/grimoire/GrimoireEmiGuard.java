package dev.runicattunement.grimoire;

import java.lang.reflect.Field;

public final class GrimoireEmiGuard {
   private static boolean active;
   private static boolean missing;
   private static boolean warned;
   private static Field enabled;
   private static Field configButton;
   private static Field treeButton;
   private static Object initialConfigButton;
   private static Object initialTreeButton;
   private static boolean initialEnabled;
   private static Object searchWidget;
   private static Field searchVisible;
   private static boolean initialSearchVisible;
   private static Object hiddenVisibility;

   private GrimoireEmiGuard() {
   }

   private static Field field(Class<?> var0, String var1) throws ReflectiveOperationException {
      Field var2 = var0.getField(var1);
      var2.setAccessible(true);
      return var2;
   }

   private static Field visibleField(Object var0) throws ReflectiveOperationException {
      for (Class var1 = var0.getClass(); var1 != null; var1 = var1.getSuperclass()) {
         try {
            Field var2 = var1.getDeclaredField("field_22764");
            var2.setAccessible(true);
            return var2;
         } catch (NoSuchFieldException var3) {
         }
      }

      throw new NoSuchFieldException("EMI widget visible field");
   }

   private static void searchHidden() {
      try {
         Class var0 = Class.forName("dev.emi.emi.screen.EmiScreenManager");
         Object var1 = field(var0, "search").get(null);
         if (var1 == null) {
            return;
         }

         if (var1 != searchWidget) {
            searchWidget = var1;
            searchVisible = visibleField(var1);
            initialSearchVisible = searchVisible.getBoolean(var1);
         }

         searchVisible.setBoolean(var1, false);
      } catch (Throwable var2) {
      }
   }

   public static synchronized void enter() {
      if (!missing) {
         try {
            if (!active) {
               Class var0 = Class.forName("dev.emi.emi.config.EmiConfig");
               enabled = field(var0, "enabled");
               configButton = field(var0, "emiConfigButtonVisibility");
               treeButton = field(var0, "recipeTreeButtonVisibility");
               initialEnabled = enabled.getBoolean(null);
               initialConfigButton = configButton.get(null);
               initialTreeButton = treeButton.get(null);
               Class var1 = configButton.getType().asSubclass(Enum.class);
               hiddenVisibility = Enum.valueOf(var1, "HIDDEN");
               active = true;
               System.out.println("[Runic Grimoire] EMI screen visibility temporarily disabled (runtime guard active).");
            }

            enabled.setBoolean(null, false);
            configButton.set(null, hiddenVisibility);
            treeButton.set(null, hiddenVisibility);
            searchHidden();
         } catch (ClassNotFoundException var2) {
            missing = true;
         } catch (Throwable var3) {
            if (!warned) {
               warned = true;
               System.err.println("[Runic Grimoire] EMI runtime suppression failed: " + var3);
            }
         }
      }
   }

   public static synchronized void exit() {
      if (active) {
         try {
            enabled.setBoolean(null, initialEnabled);
            configButton.set(null, initialConfigButton);
            treeButton.set(null, initialTreeButton);
            if (searchWidget != null && searchVisible != null) {
               searchVisible.setBoolean(searchWidget, initialSearchVisible);
            }

            System.out.println("[Runic Grimoire] EMI screen visibility restored.");
         } catch (Throwable var4) {
            if (!warned) {
               warned = true;
               System.err.println("[Runic Grimoire] EMI visibility restore failed: " + var4);
            }
         } finally {
            active = false;
            searchWidget = null;
            searchVisible = null;
         }
      }
   }
}
