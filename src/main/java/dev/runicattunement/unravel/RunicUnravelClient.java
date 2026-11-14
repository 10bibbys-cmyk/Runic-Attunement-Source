package dev.runicattunement.unravel;

public final class RunicUnravelClient {
   private RunicUnravelClient() {
   }

   private static int asInt(Object var0, int var1) {
      return var0 instanceof Number ? ((Number)var0).intValue() : var1;
   }

   private static int rgb(Object var0, String var1, int var2) {
      try {
         return 0xFF000000 | asInt(RunicUnravelRuntime.invoke(var0, var1), var2);
      } catch (Throwable var4) {
         return 0xFF000000 | var2;
      }
   }

   private static boolean inside(double var0, double var2, int var4, int var5, int var6, int var7) {
      return var0 >= var4 && var0 < var4 + var6 && var2 >= var5 && var2 < var5 + var7;
   }

   private static Object handler(Object var0) {
      return RunicUnravelRuntime.accessField(var0, "field_2797");
   }

   private static Object selected(Object var0) {
      return RunicUnravelRuntime.accessField(var0, "selectedEnchantment");
   }

   private static int left(Object var0) {
      return asInt(RunicUnravelRuntime.accessField(var0, "field_2776"), 0);
   }

   private static int top(Object var0) {
      return asInt(RunicUnravelRuntime.accessField(var0, "field_2800"), 0);
   }

   private static boolean isAttunement(Object var0) {
      return !Boolean.TRUE.equals(RunicUnravelRuntime.invoke(handler(var0), "isTransmutationMode"));
   }

   private static boolean allowedAttune(Object var0) {
      Object var1 = RunicUnravelRuntime.invoke(var0, "eligibility");
      return Boolean.TRUE.equals(RunicUnravelRuntime.invoke(var1, "allowed"));
   }

   private static boolean allowedUnravel(Object var0) {
      Object var1 = selected(var0);
      if (var1 != null && handler(var0) != null) {
         try {
            Object var2 = RunicUnravelRuntime.invoke(handler(var0), "getEquipmentStack");
            if (var2 != null && !Boolean.TRUE.equals(RunicUnravelRuntime.invoke(var2, "method_7960"))) {
               int var3 = asInt(RunicUnravelRuntime.invoke(handler(var0), "indexOf", var1), -1);
               return var3 >= 0 && var3 < 1000;
            } else {
               return false;
            }
         } catch (Throwable var4) {
            return false;
         }
      } else {
         return false;
      }
   }

   private static int xpCost(Object var0) {
      try {
         return asInt(RunicUnravelRuntime.invoke(var0, "currentAttunementExperienceCost"), 0);
      } catch (Throwable var2) {
         return 0;
      }
   }

   private static void fill(Object var0, int var1, int var2, int var3, int var4, int var5) {
      RunicUnravelRuntime.invoke(var0, "method_25294", var1, var2, var3, var4, var5);
   }

   private static void frame(Object var0, int var1, int var2, int var3, int var4, int var5) {
      RunicUnravelRuntime.invoke(var0, "method_49601", var1, var2, var3, var4, var5);
   }

   private static void text(Object var0, Object var1, String var2, int var3, int var4, int var5, int var6) {
      int var7 = asInt(RunicUnravelRuntime.invoke(var1, "method_1727", var2), var2.length() * 6);
      RunicUnravelRuntime.invoke(var0, "method_51433", var1, var2, var3 + (var6 - var7) / 2, var4, var5, Boolean.FALSE);
   }

   private static void button(Object var0, Object var1, Object var2, int var3, int var4, int var5, boolean var6, boolean var7, String var8, String var9) {
      int var10 = rgb(var2, var6 ? (var7 ? "buttonHover" : "button") : "buttonDisabled", 2698288);
      int var11 = rgb(var2, var6 ? (var7 ? "accentBright" : "accent") : "border", 5857642);
      int var12 = rgb(var2, var6 ? "title" : "muted", 8357255);
      fill(var0, var3, var4, var3 + var5, var4 + 23, var10);
      frame(var0, var3, var4, var5, 23, var11);
      text(var0, var1, var8, var3, var4 + 2, var12, var5);
      if (var9 != null && !var9.isBlank()) {
         text(var0, var1, var9, var3, var4 + 13, var12, var5);
      }
   }

   public static void draw(Object var0, Object var1, int var2, int var3) {
      try {
         if (!isAttunement(var0)) {
            return;
         }

         int var4 = left(var0) + 104;
         int var5 = top(var0) + 147;
         Object var6 = RunicUnravelRuntime.accessField(var0, "theme");
         Object var7 = RunicUnravelRuntime.accessField(var0, "field_22793");
         if (var6 == null || var7 == null) {
            return;
         }

         fill(var1, var4, var5, var4 + 100, var5 + 23, rgb(var6, "panel", 2369839));
         boolean var8 = allowedUnravel(var0);
         boolean var9 = allowedAttune(var0);
         int var10 = xpCost(var0);
         String var11 = var10 > 9999 ? "XP" : var10 + " XP";
         button(var1, var7, var6, var4, var5, 48, var8, inside(var2, var3, var4, var5, 48, 23), "UNRAVEL", "REFUND");
         button(var1, var7, var6, var4 + 52, var5, 48, var9, inside(var2, var3, var4 + 52, var5, 48, 23), "ATTUNE", var11);
      } catch (Throwable var12) {
         System.err.println("[Runic Unravel] GUI overlay: " + var12);
      }
   }

   public static int buttonId(Object var0, double var1, double var3, int var5) {
      if (var5 != 0) {
         return -1;
      }

      try {
         if (!isAttunement(var0)) {
            return -1;
         }

         int var6 = left(var0) + 104;
         int var7 = top(var0) + 147;
         if (!inside(var1, var3, var6, var7, 50, 23)) {
            return -1;
         }

         if (var1 >= var6 + 48) {
            return -2;
         }

         Object var8 = selected(var0);
         if (var8 == null) {
            return -2;
         }

         int var9 = asInt(RunicUnravelRuntime.invoke(handler(var0), "indexOf", var8), -1);
         return var9 >= 0 && var9 < 1000 ? 6000 + var9 : -2;
      } catch (Throwable var10) {
         System.err.println("[Runic Unravel] GUI button routing failed: " + var10);
         return -2;
      }
   }
}
