package dev.runicattunement.grimoire;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import net.minecraft.class_1109;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_3414;
import net.minecraft.class_7923;

public final class RunicRefinedClassicUi {
   private static final Map<Object, RunicRefinedClassicUi.State> STATE = Collections.synchronizedMap(new WeakHashMap<>());
   private static final Map<String, Method> METHODS = new HashMap<>();
   private static final class_2960 BG = new class_2960("runic_attunement", "textures/gui/classic_refined_bg.png");
   private static final class_2960 BG_TRANS = new class_2960("runic_attunement", "textures/gui/classic_refined_transmutation_bg.png");
   private static volatile Object[] runeSigils;
   private static final class_2960 RUNE_OFF = new class_2960("runic_attunement", "textures/gui/classic_rune_dormant.png");
   private static final class_2960 RUNE_ON = new class_2960("runic_attunement", "textures/gui/classic_rune_active.png");
   private static final int WHITE = -2565411;
   private static final int GOLD = -3563153;
   private static final int GRAY = -7695202;
   private static final int VIOLET = -3892498;
   private static final int MUTED = -8945783;
   private static final int RED = -2912890;
   private static final int GREEN = -7748193;
   private static final int INK = -15591652;
   private static final int HOVER = -12300711;
   private static final int DIM = -14341327;
   private static final int SELECT = -11649438;
   private static final int ART_X = -16;
   private static final int ART_Y = -12;
   private static final int RUNE_Y = 153;
   private static final int LEFT_CX = 124;
   private static final int RIGHT_CX = 547;
   private static final int INV_X = 255;
   private static final int INV_Y = 270;
   private static final int BOOK_X = 451;
   private static final int BOOK_Y = 149;
   private static final int ROW_H = 17;
   private static final int ROWS = 11;

   private RunicRefinedClassicUi() {
   }

   private static RunicRefinedClassicUi.State st(Object var0) {
      return STATE.computeIfAbsent(var0, var0x -> new RunicRefinedClassicUi.State());
   }

   private static Method lookup(String var0, int var1) {
      String var2 = var0 + "/" + var1;
      synchronized (METHODS) {
         Method var4 = METHODS.get(var2);
         if (var4 != null) {
            return var4;
         }

         for (Method var8 : RunicGrimoireUi.class.getDeclaredMethods()) {
            if (var8.getName().equals(var0) && var8.getParameterCount() == var1) {
               var8.setAccessible(true);
               METHODS.put(var2, var8);
               return var8;
            }
         }
      }

      throw new IllegalStateException("Missing Runic UI method " + var2);
   }

   private static <T> T call(String var0, Object... var1) {
      try {
         return (T)lookup(var0, var1.length).invoke(null, var1);
      } catch (InvocationTargetException var3) {
         throw new IllegalStateException("Runic UI:" + var0, var3.getCause());
      } catch (ReflectiveOperationException var4) {
         throw new IllegalStateException("Runic UI:" + var0, var4);
      }
   }

   private static Field fld(Object var0, String var1) throws NoSuchFieldException {
      for (Class var2 = var0.getClass(); var2 != null; var2 = var2.getSuperclass()) {
         try {
            Field var3 = var2.getDeclaredField(var1);
            var3.setAccessible(true);
            return var3;
         } catch (NoSuchFieldException var4) {
         }
      }

      throw new NoSuchFieldException(var1);
   }

   private static Object get(Object var0, String var1) {
      try {
         return var0 == null ? null : fld(var0, var1).get(var0);
      } catch (Exception var3) {
         return null;
      }
   }

   private static void set(Object var0, String var1, Object var2) {
      try {
         fld(var0, var1).set(var0, var2);
      } catch (Exception var4) {
         throw new IllegalStateException("Field " + var1, var4);
      }
   }

   private static int num(Object var0) {
      return var0 instanceof Number var1 ? var1.intValue() : 0;
   }

   private static long lng(Object var0) {
      return var0 instanceof Number var1 ? var1.longValue() : 0L;
   }

   private static int x(Object var0) {
      return call("x", var0);
   }

   private static int y(Object var0) {
      return call("y", var0);
   }

   private static int ax(Object var0) {
      return x(var0) + -16;
   }

   private static int ay(Object var0) {
      return y(var0) + -12;
   }

   private static boolean trans(Object var0) {
      return Boolean.TRUE.equals(call("trans", var0));
   }

   private static boolean empty(Object var0) {
      return Boolean.TRUE.equals(call("empty", var0));
   }

   private static Object source(Object var0) {
      return call("source", var0);
   }

   private static Object target(Object var0) {
      return call("target", var0);
   }

   private static Object equipment(Object var0) {
      return call("equipment", var0);
   }

   private static Object selected(Object var0) {
      return call("current", var0);
   }

   private static class_327 font(Object var0) {
      return call("font", var0);
   }

   private static List<?> all(Object var0) {
      return call("filtered", var0);
   }

   private static List<?> transferIds(Object var0) {
      return call("transferIds", var0);
   }

   private static Set<?> selectedTransfer(Object var0) {
      return call("transfers", var0);
   }

   private static Set<?> selectedCurses(Object var0) {
      return call("curses", var0);
   }

   private static Map<?, Integer> enchants(Object var0) {
      return call("enchants", var0);
   }

   private static List<?> costs(Object var0, boolean var1) {
      List var2 = call("costs", var0, var1);
      if (!var2.isEmpty()) {
         return var2;
      }

      try {
         if (!var1) {
            Object var11 = selected(var0);
            Object var13 = equipment(var0);
            if (var11 != null && !empty(var13)) {
               Object var15 = call("handler", var0);
               Object var16 = call("call", var15, "getDefinition", new Object[]{var11});
               if (var16 == null) {
                  return var2;
               }

               int var7 = num(enchants(var13).get(var11)) + 1;
               if (call("call", var15, "getClientCosts", new Object[]{var16, var7}) instanceof List var9 && !var9.isEmpty()) {
                  return var9;
               }

               return var2;
            }

            return var2;
         }

         if (selectedTransfer(var0).isEmpty() && selectedCurses(var0).isEmpty()) {
            return var2;
         }

         Object var3 = call("handler", var0);
         Class var12 = Class.forName("dev.runicattunement.transmutation.TransmutationRules");
         if (call(
               "call",
               var12,
               "calculateCosts",
               new Object[]{
                  source(var0),
                  target(var0),
                  call("call", var3, "getDefinitions", new Object[0]),
                  selectedTransfer(var0),
                  selectedCurses(var0),
                  call("call", var3, "getTransmutationCosts", new Object[0]),
                  call("call", var3, "getTransmutationSettings", new Object[0])
               }
            ) instanceof List var6
            && !var6.isEmpty()) {
            return var6;
         }
      } catch (Throwable var10) {
         RunicRefinedClassicUi.State var4 = st(var0);
         String var5 = var1 ? "direct-transmutation-cost-quote" : "direct-attunement-cost-quote";
         if (var4.failedSections.add(var5)) {
            System.err.println("[Runic Classic Refined] Could not read server-synchronised material quote: " + var10);
            var10.printStackTrace();
         }
      }

      return var2;
   }

   private static int xp(Object var0, boolean var1) {
      return call("xp", var0, var1);
   }

   private static boolean allowed(Object var0, boolean var1) {
      return Boolean.TRUE.equals(call("allowed", var0, var1));
   }

   private static String nice(Object var0) {
      if (var0 == null) {
         return "—";
      }

      try {
         String var1 = call("naturalName", var0, 0);
         return var1 == null ? String.valueOf(var0) : var1;
      } catch (Throwable var2) {
         return String.valueOf(var0);
      }
   }

   private static void fill(class_332 var0, int var1, int var2, int var3, int var4, int var5) {
      if (var3 > 0 && var4 > 0) {
         var0.method_25294(var1, var2, var1 + var3, var2 + var4, var5);
      }
   }

   private static void border(class_332 var0, int var1, int var2, int var3, int var4, int var5) {
      fill(var0, var1, var2, var3, 1, var5);
      fill(var0, var1, var2 + var4 - 1, var3, 1, var5);
      fill(var0, var1, var2, 1, var4, var5);
      fill(var0, var1 + var3 - 1, var2, 1, var4, var5);
   }

   private static void text(class_332 var0, class_327 var1, String var2, int var3, int var4, int var5) {
      var0.method_51433(var1, var2, var3, var4, var5, false);
   }

   private static String fit(String var0, int var1) {
      return var0 == null ? "" : (var0.length() > var1 ? var0.substring(0, Math.max(1, var1 - 1)) + "…" : var0);
   }

   private static int width(class_327 var0, String var1) {
      return var0.method_1727(var1);
   }

   private static void centered(class_332 var0, class_327 var1, String var2, int var3, int var4, int var5) {
      text(var0, var1, var2, var3 - width(var1, var2) / 2, var4, var5);
   }

   private static void centerBox(class_332 var0, class_327 var1, String var2, int var3, int var4, int var5, int var6, int var7) {
      text(var0, var1, var2, var3 + (var5 - width(var1, var2)) / 2, var4 + (var6 - 9) / 2, var7);
   }

   private static String fitPixels(class_327 var0, String var1, int var2) {
      if (var1 == null) {
         return "";
      }

      if (width(var0, var1) <= var2) {
         return var1;
      }

      int var3 = Math.max(0, var2 - width(var0, "…"));
      int var4 = var1.length();

      while (var4 > 0 && width(var0, var1.substring(0, var4)) > var3) {
         var4--;
      }

      return var1.substring(0, var4) + "…";
   }

   private static boolean inside(double var0, double var2, int var4, int var5, int var6, int var7) {
      return var0 >= var4 && var2 >= var5 && var0 < var4 + var6 && var2 < var5 + var7;
   }

   private static boolean hover(Object var0, int var1, int var2, int var3, int var4) {
      RunicRefinedClassicUi.State var5 = st(var0);
      return inside(var5.mouseX, var5.mouseY, ax(var0) + var1, ay(var0) + var2, var3, var4);
   }

   private static void button(class_332 var0, class_327 var1, String var2, int var3, int var4, int var5, int var6, boolean var7, boolean var8, int var9) {
      fill(var0, var3, var4, var5, var6, -15526116);
      fill(var0, var3 + 1, var4 + 1, var5 - 2, var6 - 2, var7 ? (var8 ? -11188384 : -12567753) : -14013134);
      border(var0, var3, var4, var5, var6, var7 ? (var8 ? -1983106 : -7572401) : -11512740);
      fill(var0, var3 + 2, var4 + 1, var5 - 4, 1, var7 ? var9 : -12499378);
      centerBox(var0, var1, var2, var3, var4 + 1, var5, var6, var7 ? (var8 ? -666456 : -2565411) : -9670022);
   }

   private static void tabButton(class_332 var0, class_327 var1, String var2, int var3, int var4, int var5, int var6, boolean var7, boolean var8) {
      int var9 = var7 ? -11452321 : (var8 ? -10530501 : -13354946);
      int var10 = var7 ? -4220196 : (var8 ? -1720965 : -9472640);
      int var11 = var7 ? -8004 : (var8 ? -7505 : -4802113);
      fill(var0, var3, var4, var5, var6, -15657959);
      fill(var0, var3 + 1, var4 + 1, var5 - 2, var6 - 2, var9);
      border(var0, var3, var4, var5, var6, var10);
      fill(var0, var3 + 2, var4 + 2, var5 - 4, 1, var7 ? -4814117 : (var8 ? -997748 : -10327949));
      centerBox(var0, var1, var2, var3, var4 + 1, var5, var6, var11);
   }

   private static void switchSound() {
      try {
         class_3414 var0 = class_3414.method_47908(new class_2960("minecraft", "ui.button.click"));
         class_1109 var1 = class_1109.method_4758(var0, 1.06F);
         class_310.method_1551().method_1483().method_4873(var1);
      } catch (Throwable var2) {
      }
   }

   private static void switchFx(Object var0) {
      st(var0).tabFxStart = System.nanoTime();
      switchSound();
   }

   private static void drawSwitchFx(Object var0, class_332 var1, int var2, int var3) {
      long var4 = System.nanoTime() - st(var0).tabFxStart;
      if (var4 >= 0L && var4 <= 420000000L && st(var0).tabFxStart != 0L) {
         float var6 = (float)var4 / 4.2E8F;
         int var7 = var2 + (trans(var0) ? 515 : 378);
         int var8 = var3 + 25;
         int var9 = trans(var0) ? 130 : 129;
         byte var10 = 28;
         int var11 = var6 < 0.35F ? -1787398 : -4812331;

         for (int var12 = 0; var12 < 6; var12++) {
            float var13 = (var12 / 6.0F + var6 * 0.7F) % 1.0F;
            int var14 = var7 + 3 + (int)((var9 - 6) * var13);
            int var15 = var12 % 2 == 0 ? var8 + 2 : var8 + var10 - 4;
            fill(var1, var14, var15, 2, 2, var12 % 3 == 0 ? -1195647 : var11);
         }
      }
   }

   private static void filterButton(class_332 var0, class_327 var1, String var2, int var3, int var4, int var5, int var6, boolean var7, boolean var8) {
      fill(var0, var3, var4, var5, var6, -15526116);
      fill(var0, var3 + 1, var4 + 1, var5 - 2, var6 - 2, var7 ? -11058325 : (var8 ? -11845565 : -12567753));
      border(var0, var3, var4, var5, var6, var7 ? -5733944 : -7506354);
      centerBox(var0, var1, var2, var3, var4 + 1, var5, var6, var7 ? -665423 : -7695202);
   }

   private static void tex(class_332 var0, class_2960 var1, int var2, int var3, int var4, int var5) {
      var0.method_25290(var1, var2, var3, 0.0F, 0.0F, var4, var5, var4, var5);
   }

   private static void searchPos(Object var0, int var1, int var2, int var3) {
      if (var0 != null) {
         for (String[] var7 : new String[][]{{"method_46421", String.valueOf(var1)}, {"method_46419", String.valueOf(var2)}}) {
            boolean var8 = false;

            for (Method var12 : var0.getClass().getMethods()) {
               if (var12.getName().equals(var7[0]) && var12.getParameterCount() == 1) {
                  try {
                     var12.invoke(var0, Integer.parseInt(var7[1]));
                     var8 = true;
                     break;
                  } catch (Exception var14) {
                  }
               }
            }

            if (!var8) {
               throw new IllegalStateException("Search field move: " + var7[0]);
            }
         }

         set(var0, "field_22758", var3);
      }
   }

   public static void relayout(Object var0) {
      if (RunicGrimoireUi.active(var0)) {
         try {
            List var1 = call("slots", var0);
            int var2 = ax(var0);
            int var3 = ay(var0);
            int var4 = num(get(var0, "field_2776"));
            int var5 = num(get(var0, "field_2800"));
            boolean var6 = trans(var0);

            for (int var7 = 0; var7 < var1.size(); var7++) {
               int var8 = -5000;
               int var9 = -5000;
               if (var7 == 0) {
                  var8 = var2 + 124 - 8;
                  var9 = var3 + 153 - 8;
               } else if (var7 == 1 && var6) {
                  var8 = var2 + 547 - 8;
                  var9 = var3 + 153 - 8;
               } else if (var7 >= 2 && var7 < 38) {
                  int var10 = var7 - 2;
                  int var11 = var10 < 27 ? var10 / 9 : 3;
                  int var12 = var10 < 27 ? var10 % 9 : var10 - 27;
                  var8 = var2 + 255 + 18 * var12;
                  var9 = var3 + 270 + 18 * var11 + (var11 == 3 ? 8 : 0);
               }

               set(var1.get(var7), "field_7873", var8 - var4);
               set(var1.get(var7), "field_7872", var9 - var5);
            }

            Object var14 = get(var0, "searchField");
            if (var14 != null) {
               searchPos(var14, var2 + (var6 ? -5000 : 458), var3 + (var6 ? -5000 : 100), 177);
            }
         } catch (Throwable var13) {
            error(var0, "relayout", var13);
         }
      }
   }

   private static void error(Object var0, String var1, Throwable var2) {
      if (!st(var0).warned) {
         st(var0).warned = true;
         System.err.println("[Runic Classic Refined] " + var1 + ": " + var2);
         var2.printStackTrace();
      }
   }

   public static boolean background(Object var0, class_332 var1, float var2, int var3, int var4) {
      if (!RunicGrimoireUi.active(var0)) {
         return false;
      }

      try {
         RunicGrimoireUi.prepare(var0);
         st(var0).mouseX = var3;
         st(var0).mouseY = var4;
         Object var5 = call("state", var0);
         set(var5, "mouseX", var3);
         set(var5, "mouseY", var4);
         int var6 = ax(var0);
         int var7 = ay(var0);
         tex(var1, trans(var0) ? BG_TRANS : BG, var6, var7, 672, 390);
         boolean var8 = trans(var0);
         Object var9 = var8 ? source(var0) : equipment(var0);
         seal(var1, var6 + 124, var7 + 153, !empty(var9), 0, var0);
         if (var8) {
            seal(var1, var6 + 547, var7 + 153, !empty(target(var0)), 1, var0);
         }

         for (int var10 = 0; var10 < 36; var10++) {
            int var11 = var10 < 27 ? var10 / 9 : 3;
            int var12 = var10 < 27 ? var10 % 9 : var10 - 27;
            int var13 = var6 + 255 + var12 * 18;
            int var14 = var7 + 270 + var11 * 18 + (var11 == 3 ? 8 : 0);
            fill(var1, var13 - 1, var14 - 1, 18, 18, -11576987);
            fill(var1, var13, var14, 16, 16, -14341325);
         }

         relayout(var0);
         return true;
      } catch (Throwable var15) {
         error(var0, "background", var15);
         return false;
      }
   }

   private static Object[] glyphs() {
      Object[] var0 = runeSigils;
      if (var0 != null) {
         return var0;
      }

      try {
         Field var1 = RunicGrimoireUi.class.getDeclaredField("CACHED_SIGILS");
         var1.setAccessible(true);
         runeSigils = (Object[])var1.get(null);
      } catch (Throwable var2) {
         runeSigils = new Object[0];
      }

      return runeSigils;
   }

   private static void seal(class_332 var0, int var1, int var2, boolean var3, int var4, Object var5) {
      tex(var0, var3 ? RUNE_ON : RUNE_OFF, var1 - 56, var2 - 56, 112, 112);
      Object[] var6 = glyphs();
      if (var6.length != 0) {
         double var7 = System.nanoTime() / 1.0E9;
         double var9 = var7 * (var3 ? 0.28 : 0.065) * (var4 == 0 ? 1 : -1);

         for (int var11 = 0; var11 < 13; var11++) {
            double var12 = (Math.PI * 2) * var11 / 13.0 + var9 + var4 * 0.19;
            int var14 = var1 + (int)Math.round(56.0 * Math.cos(var12)) - 3;
            int var15 = var2 + (int)Math.round(56.0 * Math.sin(var12)) - 4;
            int var16 = Math.floorMod(var4 * 7 + var11 * 11 + var11 * var11 * 3, var6.length);
            if (var6[var16] instanceof class_2561 var18) {
               int var19 = var3 ? (var11 % 3 == 0 ? -1063545 : -4219931) : (var11 % 3 == 0 ? -8951684 : -10331279);
               var0.method_27534(font(var5), var18, var14, var15, var19);
            }
         }
      }
   }

   public static boolean foreground(Object var0, class_332 var1) {
      if (!RunicGrimoireUi.active(var0)) {
         return false;
      }

      try {
         class_327 var2 = font(var0);
         int var3 = num(get(var0, "field_2776"));
         int var4 = num(get(var0, "field_2800"));
         int var5 = ax(var0) - var3;
         int var6 = ay(var0) - var4;
         boolean var7 = trans(var0);
         centerBox(var1, var2, "RUNIC ATTUNEMENT TABLE", var5 + 28, var6 + 25, 341, 28, -3563153);
         tabButton(var1, var2, "ATTUNEMENT", var5 + 378, var6 + 25, 129, 28, !var7, hover(var0, 378, 25, 129, 28));
         tabButton(var1, var2, "TRANSMUTATION", var5 + 515, var6 + 25, 130, 28, var7, hover(var0, 515, 25, 130, 28));
         centerBox(var1, var2, var7 ? "SOURCE EQUIPMENT" : "EQUIPMENT", var5 + 34, var6 + 72, 179, 20, -3563153);
         centerBox(var1, var2, var7 ? "TRANSMUTATION" : "ATTUNEMENT", var5 + 247, var6 + 72, 176, 20, -3563153);
         centerBox(var1, var2, var7 ? "TARGET EQUIPMENT" : "ATTUNEMENT TOME", var5 + 457, var6 + 72, 181, 20, -3563153);
         if (var7) {
            drawTransmutation(var0, var1, var2, var5, var6);
         } else {
            drawAttunement(var0, var1, var2, var5, var6);
         }

         centerBox(var1, var2, "INVENTORY", var5 + 246, var6 + 253, 177, 15, -3563153);
         drawFX(var0, var1, var5, var6);
         drawSwitchFx(var0, var1, var5, var6);
         drawInfoPopup(var0, var1, var2, var5, var6);
         return true;
      } catch (Throwable var8) {
         error(var0, "foreground", var8);
         return true;
      }
   }

   private static void safely(Object var0, String var1, Runnable var2) {
      try {
         var2.run();
      } catch (Throwable var5) {
         RunicRefinedClassicUi.State var4 = st(var0);
         if (var4.failedSections.add(var1)) {
            System.err.println("[Runic Classic Refined] Foreground section '" + var1 + "' failed; other panels retained: " + var5);
            var5.printStackTrace();
         }
      }
   }

   private static void drawAttunement(Object var0, class_332 var1, class_327 var2, int var3, int var4) {
      safely(var0, "attunement-refresh", () -> call("call", var0, "syncAttunementCostState", new Object[0]));
      safely(var0, "attunement-inscribed", () -> drawAttunementInscribed(var0, var1, var2, var3, var4));
      safely(var0, "attunement-costs-and-actions", () -> drawAttunementCenter(var0, var1, var2, var3, var4));
      safely(var0, "attunement-tome", () -> drawAttunementCatalogue(var0, var1, var2, var3, var4));
   }

   private static void drawAttunementInscribed(Object var0, class_332 var1, class_327 var2, int var3, int var4) {
      Object var5 = equipment(var0);
      Object var6 = selected(var0);
      boolean var7 = !empty(var5);
      centerBox(var1, var2, "INSCRIBED RUNES", var3 + 35, var4 + 220, 178, 18, -3563153);
      Map<?, Integer> var8 = var7 ? enchants(var5) : Map.of();
      int var9 = 0;
      int var10 = Math.max(0, num(get(call("state", var0), "installedScroll")));
      if (var8.isEmpty()) {
         centerBox(var1, var2, var7 ? "No enchantments" : "Insert equipment", var3 + 38, var4 + 247, 168, 20, -8945783);
      }

      for (Entry var12 : var8.entrySet()) {
         if (var9++ >= var10) {
            int var13 = var9 - 1 - var10;
            if (var13 >= 6) {
               break;
            }

            int var14 = var4 + 244 + var13 * 17;
            boolean var15 = Objects.equals(var6, var12.getKey());
            if (var15) {
               fill(var1, var3 + 40, var14 - 1, 165, 15, -11649438);
            }

            text(var1, var2, fitPixels(var2, nice(var12.getKey()), 129), var3 + 43, var14 + 2, var15 ? -2565411 : -7695202);
            text(var1, var2, roman(num(var12.getValue())), var3 + 198 - width(var2, roman(num(var12.getValue()))), var14 + 2, var15 ? -3892498 : -3563153);
         }
      }

      if (var8.size() > 6) {
         text(var1, var2, "▾", var3 + 197, var4 + 337, -3892498);
      }
   }

   private static void drawAttunementCenter(Object var0, class_332 var1, class_327 var2, int var3, int var4) {
      Object var5 = equipment(var0);
      Object var6 = selected(var0);
      boolean var7 = !empty(var5);
      boolean var8 = var7 && var6 != null;
      int var9 = var8 ? num(call("level", var0)) : 0;
      centerBox(
         var1,
         var2,
         var8 ? "LEVEL  " + (var9 > 0 ? roman(var9) : "—") + "  →  " + roman(var9 + 1) : "LEVEL  —  →  —",
         var3 + 249,
         var4 + 105,
         172,
         14,
         var8 ? -3563153 : -8945783
      );
      centerBox(var1, var2, "MATERIAL COSTS", var3 + 249, var4 + 121, 172, 13, -3563153);
      safely(var0, "attunement-material-items", () -> drawCosts(var0, var1, var2, var3 + 262, var4 + 139, false));
      centerBox(var1, var2, var8 ? "XP: " + xp(var0, false) : "XP: —", var3 + 249, var4 + 195, 172, 11, var8 ? -3563153 : -8945783);
      boolean var10 = var8 && allowed(var0, false);
      boolean var11 = var8 && var9 > 0;
      button(var1, var2, "UNRAVEL", var3 + 248, var4 + 221, 83, 20, var11, hover(var0, 248, 221, 83, 20), -5995835);
      button(var1, var2, "ATTUNE", var3 + 336, var4 + 221, 83, 20, var10, hover(var0, 336, 221, 83, 20), -3562899);
   }

   private static void drawAttunementCatalogue(Object var0, class_332 var1, class_327 var2, int var3, int var4) {
      Object var5 = selected(var0);

      for (int var6 = 0; var6 < 4; var6++) {
         int var7 = var3 + 453 + var6 * 47;
         int var8 = num(get(var0, "tierFilter"));
         filterButton(
            var1, var2, new String[]{"ALL", "I", "II", "III"}[var6], var7, var4 + 125, 43, 16, var8 == var6, hover(var0, 453 + var6 * 47, 125, 43, 16)
         );
      }

      List var14 = all(var0);
      int var15 = Math.max(0, var14.size() - 11);
      int var16 = Math.max(0, Math.min(var15, num(get(var0, "scrollOffset"))));

      for (int var9 = 0; var9 < 11 && var16 + var9 < var14.size(); var9++) {
         Object var10 = var14.get(var16 + var9);
         int var11 = var4 + 149 + var9 * 17;
         boolean var12 = Objects.equals(var5, var10);
         boolean var13 = hover(var0, 455, 149 + var9 * 17, 177, 17);
         if (var12) {
            fill(var1, var3 + 455, var11, 177, 16, -11649438);
         } else if (var13) {
            fill(var1, var3 + 455, var11, 177, 16, -12300711);
         }

         text(var1, var2, fitPixels(var2, nice(var10), 131), var3 + 459, var11 + 4, var12 ? -929372 : -7695202);
         badge(var1, var2, "i", var3 + 594, var11 + 1, 15, 15, -7155500);
         badge(var1, var2, roman(tier(var0, var10)), var3 + 613, var11 + 1, 19, 15, tier(var0, var10) == 3 ? -1918856 : -4021526);
         if (var12) {
            fill(var1, var3 + 455, var11, 2, 16, -5600296);
         }
      }

      scroll(var1, var3 + 636, var4 + 150, 4, 185, var16, var15);
   }

   private static void drawTransmutation(Object var0, class_332 var1, class_327 var2, int var3, int var4) {
      safely(var0, "transmutation-refresh", () -> call("call", var0, "syncTransmutationState", new Object[0]));
      safely(var0, "transmutation-source-list", () -> drawTransmutationSource(var0, var1, var2, var3, var4));
      safely(var0, "transmutation-result-preview", () -> drawTransmutationResult(var0, var1, var2, var3, var4));
      safely(var0, "transmutation-costs-and-actions", () -> drawTransmutationCenter(var0, var1, var2, var3, var4));
   }

   private static void drawTransmutationSource(Object var0, class_332 var1, class_327 var2, int var3, int var4) {
      Object var5 = source(var0);
      Object var6 = target(var0);
      List var7 = transferIds(var0);
      centerBox(var1, var2, "SOURCE ENCHANTMENTS", var3 + 35, var4 + 220, 178, 18, -3563153);
      Object var8 = call("state", var0);
      int var9 = Math.max(0, num(get(var8, "sourceScroll")));
      Set var10 = selectedTransfer(var0);
      Set var11 = selectedCurses(var0);

      for (int var12 = 0; var12 < 6 && var12 + var9 < var7.size(); var12++) {
         Object var13 = var7.get(var12 + var9);
         int var14 = var4 + 244 + var12 * 17;
         boolean var15 = var10.contains(var13) || var11.contains(var13);
         fill(var1, var3 + 41, var14 + 1, 11, 11, var15 ? -9086820 : -14341583);
         border(var1, var3 + 41, var14 + 1, 11, 11, var15 ? -3892498 : -7695202);
         if (var15) {
            fill(var1, var3 + 45, var14 + 5, 3, 3, -3563153);
         }

         text(var1, var2, fitPixels(var2, nice(var13), 101), var3 + 59, var14 + 3, var15 ? -2565411 : -7695202);
         badge(var1, var2, "i", var3 + 166, var14, 15, 15, -7155500);
         badge(var1, var2, roman(tier(var0, var13)), var3 + 185, var14, 18, 15, -3563153);
      }

      if (var7.isEmpty()) {
         centerBox(var1, var2, empty(var5) ? "Insert source item" : "No enchantments", var3 + 38, var4 + 248, 165, 20, -8945783);
      }

      if (var7.size() > 6) {
         scroll(var1, var3 + 206, var4 + 244, 3, 100, var9, Math.max(0, var7.size() - 6));
      }
   }

   private static void drawTransmutationResult(Object var0, class_332 var1, class_327 var2, int var3, int var4) {
      Object var5 = source(var0);
      Object var6 = target(var0);
      Set var7 = selectedTransfer(var0);
      Set var8 = selectedCurses(var0);
      centerBox(var1, var2, "RESULT PREVIEW", var3 + 453, var4 + 220, 185, 18, -3563153);
      LinkedHashMap<Object, Integer> var9 = new LinkedHashMap<>();
      var9.putAll(enchants(var6));
      Map<?, Integer> var10 = enchants(var5);

      for (Object var12 : var7) {
         if (var10.get(var12) != null) {
            var9.merge(var12, (Integer)var10.get(var12), Math::max);
         }
      }

      for (Object var16 : var8) {
         if (var10.get(var16) != null) {
            var9.merge(var16, (Integer)var10.get(var16), Math::max);
         }
      }

      int var15 = 0;

      for (Entry var13 : var9.entrySet()) {
         if (var15 >= 6) {
            break;
         }

         text(var1, var2, fitPixels(var2, nice(var13.getKey()), 135), var3 + 464, var4 + 245 + var15 * 17, -7695202);
         text(var1, var2, roman((Integer)var13.getValue()), var3 + 615, var4 + 245 + var15 * 17, -3563153);
         var15++;
      }

      if (var15 == 0) {
         centerBox(var1, var2, empty(var6) ? "Insert target item" : "No resulting runes", var3 + 453, var4 + 248, 185, 20, -8945783);
      }
   }

   private static void drawTransmutationCenter(Object var0, class_332 var1, class_327 var2, int var3, int var4) {
      List var5 = transferIds(var0);
      centerBox(var1, var2, "TRANSFER COST", var3 + 249, var4 + 105, 172, 14, -3563153);
      centerBox(var1, var2, "MATERIALS", var3 + 249, var4 + 121, 172, 13, -3563153);
      safely(var0, "transmutation-material-items", () -> drawCosts(var0, var1, var2, var3 + 262, var4 + 139, true));
      centerBox(var1, var2, "XP: " + xp(var0, true), var3 + 249, var4 + 195, 172, 11, -3563153);
      button(var1, var2, "SELECT ALL", var3 + 248, var4 + 216, 83, 15, !var5.isEmpty(), hover(var0, 248, 216, 83, 15), -4879390);
      button(var1, var2, "CLEAR", var3 + 336, var4 + 216, 83, 15, !var5.isEmpty(), hover(var0, 336, 216, 83, 15), -5668275);
      button(var1, var2, "TRANSMUTE", var3 + 248, var4 + 234, 171, 15, allowed(var0, true), hover(var0, 248, 234, 171, 15), -4879390);
   }

   private static Object itemFor(Object var0) {
      return var0 instanceof class_2960 var1 ? class_7923.field_41178.method_17966(var1).orElse(null) : null;
   }

   private static void badge(class_332 var0, class_327 var1, String var2, int var3, int var4, int var5, int var6, int var7) {
      fill(var0, var3, var4, var5, var6, -15394012);
      border(var0, var3, var4, var5, var6, -9537657);
      centerBox(var0, var1, var2, var3, var4 + 1, var5, var6, var7);
   }

   private static int tier(Object var0, Object var1) {
      try {
         Object var2 = call("handler", var0);
         Object var3 = call("call", var2, "getDefinition", new Object[]{var1});
         return Math.max(1, Math.min(3, num(invokeMember(var3, "tabletTier"))));
      } catch (Exception var4) {
         return 1;
      }
   }

   private static void drawCosts(Object var0, class_332 var1, class_327 var2, int var3, int var4, boolean var5) {
      List var6 = costs(var0, var5);
      if (var6.isEmpty()) {
         boolean var21 = var5 && (!selectedTransfer(var0).isEmpty() || !selectedCurses(var0).isEmpty());
         boolean var22 = !empty(var5 ? source(var0) : equipment(var0));
         String var23;
         if (var5 && !var21) {
            var23 = "Select enchantments to transfer";
         } else if (var5 || var22 && selected(var0) != null) {
            var23 = "No material cost required";
         } else {
            var23 = "Select an enchantment";
         }

         centerBox(var1, var2, var23, var3 - 14, var4 + 12, 176, 18, -8945783);
         if (var21 && xp(var0, true) > 0 && st(var0).failedSections.add("empty-selected-transfer-quote")) {
            try {
               Object var24 = call("handler", var0);
               Object var26 = call("call", var24, "getTransmutationCosts", new Object[0]);
               Object var28 = call("call", var26, "unitMaterials", new Object[0]);
               System.err
                  .println(
                     "[Runic Classic Refined] Server-synced transfer quote is empty for "
                        + selectedTransfer(var0).size()
                        + " selected runes; unitMaterials="
                        + var28
                        + ". No material costs will be fabricated."
                  );
            } catch (Throwable var19) {
               System.err.println("[Runic Classic Refined] Transfer quote diagnostics: " + var19);
            }
         }

         if (!var5
            && var22
            && selected(var0) != null
            && xp(var0, false) > 0
            && st(var0).failedSections.add("empty-attunement-quote-" + selected(var0) + "-" + num(enchants(equipment(var0)).get(selected(var0))))) {
            try {
               Object var25 = call("handler", var0);
               Object var27 = call("call", var25, "data", new Object[0]);
               Object var29 = call("call", var27, "universalCosts", new Object[0]);
               System.err
                  .println(
                     "[Runic Classic Refined] Attunement quote empty for "
                        + selected(var0)
                        + "; synced universalCosts="
                        + var29
                        + ". No material costs will be fabricated."
                  );
            } catch (Throwable var18) {
               System.err.println("[Runic Classic Refined] Attunement quote diagnostics: " + var18);
            }
         }
      } else {
         Object var7 = call("state", var0);
         byte var8 = 8;
         int var9 = Math.max(0, var6.size() - var8);
         int var10 = Math.max(0, Math.min(var9, num(get(var7, "costScroll"))));

         for (int var11 = 0; var11 < var8 && var11 + var10 < var6.size(); var11++) {
            Object var12 = var6.get(var11 + var10);
            Object var13 = itemFor(invokeMember(var12, "itemId"));
            int var14 = var3 + var11 % 4 * 37 + costRowInset(var6, var10, var11);
            int var15 = var4 + 3 + var11 / 4 * 29;
            fill(var1, var14 - 1, var15 - 1, 18, 18, -12498862);
            fill(var1, var14, var15, 16, 16, -14341068);
            if (var13 instanceof class_1792 var16) {
               try {
                  var1.method_51427(new class_1799(var16), var14, var15);
               } catch (Throwable var20) {
                  if (st(var0).failedSections.add("material-icon")) {
                     System.err.println("[Runic Classic Refined] Could not render a material item icon: " + var20);
                     var20.printStackTrace();
                  }
               }
            }

            String var30 = "×" + num(invokeMember(var12, "count"));
            text(var1, var2, fitPixels(var2, var30, 22), var14 + 17, var15 + 4, -2565411);
         }

         if (var6.size() > var8) {
            scroll(var1, var3 + 153, var4 + 1, 3, 49, var10, var9);
         }
      }
   }

   private static Object invokeMember(Object var0, String var1) {
      try {
         return var0.getClass().getMethod(var1).invoke(var0);
      } catch (Exception var3) {
         return null;
      }
   }

   private static String roman(int var0) {
      if (var0 <= 0) {
         return "—";
      } else {
         return var0 > 10 ? String.valueOf(var0) : new String[]{"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"}[var0];
      }
   }

   private static void scroll(class_332 var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      fill(var0, var1, var2, var3, var4, -15525858);
      fill(var0, var1 + 1, var2 + 1, var3 - 2, var4 - 2, -13354171);
      int var7 = var6 == 0 ? var4 - 2 : Math.max(14, var4 / (var6 + 1));
      int var8 = var6 == 0 ? 0 : (var4 - var7 - 2) * var5 / var6;
      fill(var0, var1 + 1, var2 + 1 + var8, var3 - 2, var7, var6 == 0 ? -10656658 : -6059838);
   }

   private static void emitRing(class_332 var0, int var1, int var2, float var3, int var4, int var5) {
      if (!(var3 < 0.0F) && !(var3 >= 1.0F)) {
         double var6 = 14.0F + 44.0F * var3;

         for (int var8 = 0; var8 < 20; var8++) {
            double var9 = var8 * 0.31415926535 + var3 * 5.7 * var4;
            int var11 = var1 + (int)Math.round(Math.cos(var9) * (var6 + var8 % 4));
            int var12 = var2 + (int)Math.round(Math.sin(var9) * (var6 + var8 % 4));
            fill(var0, var11, var12, 2, 2, var8 % 5 == 0 ? -992853 : var5);
         }
      }
   }

   private static void drawFX(Object var0, class_332 var1, int var2, int var3) {
      Object var4 = call("state", var0);
      RunicRefinedClassicUi.State var5 = st(var0);
      long var6 = lng(get(var4, "pulseStart"));
      long var8 = System.nanoTime();
      if (var6 > 0L && var6 != var5.lastPulseStart) {
         var5.lastPulseStart = var6;
         var5.pulses.addLast(new RunicRefinedClassicUi.Pulse(var6, String.valueOf(get(var4, "pulseKind"))));

         while (var5.pulses.size() > 32) {
            var5.pulses.removeFirst();
         }
      }

      while (!var5.pulses.isEmpty() && var8 - var5.pulses.peekFirst().start > 1350000000L) {
         var5.pulses.removeFirst();
      }

      for (RunicRefinedClassicUi.Pulse var11 : var5.pulses) {
         if (var11.start <= var8) {
            float var12 = Math.max(0.0F, Math.min(1.0F, (float)(var8 - var11.start) / 1.35E9F));
            String var13 = var11.kind.toLowerCase(Locale.ROOT);
            int var14 = var13.contains("unravel") ? -2244987 : -4813598;
            int var15 = var2 + 124;
            int var16 = var2 + 547;
            int var17 = var3 + 153;
            if (var13.contains("trans")) {
               emitRing(var1, var15, var17, var12 / 0.33F, 1, var14);
               float var18 = (var12 - 0.18F) / 0.58F;
               if (var18 >= 0.0F && var18 <= 1.14F) {
                  for (int var19 = 0; var19 < 20; var19++) {
                     float var20 = var18 - var19 * 0.017F;
                     if (!(var20 < 0.0F) && !(var20 > 1.0F)) {
                        int var21 = var15 + (int)(423.0F * var20);
                        int var22 = (int)(7.0 * Math.sin(var20 * 11.5 + var19 * 0.48));
                        fill(var1, var21, var17 + var22, 2, 2, var19 % 4 == 0 ? -665692 : var14);
                     }
                  }
               }

               emitRing(var1, var16, var17, (var12 - 0.64F) / 0.36F, -1, -2049808);
            } else {
               emitRing(var1, var15, var17, var12, var13.contains("unravel") ? -1 : 1, var14);
            }
         }
      }
   }

   private static void drawInfoPopup(Object var0, class_332 var1, class_327 var2, int var3, int var4) {
      Object var5 = call("state", var0);
      String var6 = String.valueOf(get(var5, "infoEnchant"));
      if (var6 != null && !var6.equals("null") && !var6.isEmpty()) {
         String var7 = String.valueOf(get(var5, "infoTitle"));
         String var8 = String.valueOf(get(var5, "infoDescription"));
         if (var8.equals("null") || var8.isBlank()) {
            var8 = "No description is available for this enchantment.";
         }

         int var9 = var3 + 243;
         int var10 = var4 + 98;
         short var11 = 394;
         short var12 = 167;
         fill(var1, var9 + 3, var10 + 3, var11, var12, -1157627904);
         fill(var1, var9, var10, var11, var12, -15525600);
         border(var1, var9, var10, var11, var12, -6519718);
         fill(var1, var9 + 4, var10 + 4, var11 - 8, 23, -14341581);
         border(var1, var9 + 4, var10 + 4, var11 - 8, 23, -9937811);
         text(var1, var2, fitPixels(var2, var7, var11 - 58), var9 + 13, var10 + 12, -3563153);
         button(var1, var2, "X", var9 + var11 - 26, var10 + 8, 18, 15, true, hover(var0, 243 + var11 - 26, 106, 18, 15), -5664921);
         int var13 = var10 + 41;
         int var14 = 0;
         StringBuilder var15 = new StringBuilder();

         for (String var19 : var8.replace('\n', ' ').split(" +")) {
            if (!var19.isBlank()) {
               String var20 = var15.length() == 0 ? var19 : var15 + " " + var19;
               if (width(var2, var20) > var11 - 30 && var15.length() > 0) {
                  text(var1, var2, var15.toString(), var9 + 14, var13, -2565411);
                  var13 += 15;
                  var14++;
                  var15.setLength(0);
               }

               if (width(var2, var19) > var11 - 30) {
                  var19 = fitPixels(var2, var19, var11 - 30);
               }

               if (var15.length() > 0) {
                  var15.append(' ');
               }

               var15.append(var19);
               if (var14 >= 7) {
                  break;
               }
            }
         }

         if (var14 < 8 && var15.length() > 0) {
            text(var1, var2, var15.toString(), var9 + 14, var13, -2565411);
         }
      }
   }

   private static int oldClick(Object var0, double var1, double var3, int var5) {
      return call("classicLegacyClick", var0, x(var0) + var1, y(var0) + var3, var5);
   }

   public static int click(Object var0, double var1, double var3, int var5) {
      if (!RunicGrimoireUi.active(var0)) {
         return -1000;
      }

      int var6 = ax(var0);
      int var7 = ay(var0);
      double var8 = var1 - var6;
      double var10 = var3 - var7;
      if (!inside(var8, var10, 0, 0, 672, 390)) {
         return -1000;
      }

      Object var12 = call("state", var0);
      Object var13 = get(var12, "infoEnchant");
      if (var13 != null && !String.valueOf(var13).isEmpty()) {
         set(var12, "infoEnchant", "");
         return -20;
      }

      if (inside(var8, var10, 254, 269, 164, 86)) {
         return -10;
      }

      if (!inside(var8, var10, 115, 144, 18, 18) && (!trans(var0) || !inside(var8, var10, 538, 144, 18, 18))) {
         if (!trans(var0) && inside(var8, var10, 457, 99, 179, 22)) {
            return -10;
         }

         if (var5 != 0) {
            return -20;
         }

         try {
            if (inside(var8, var10, 378, 25, 129, 28)) {
               if (!trans(var0)) {
                  return -20;
               }

               int var27 = oldClick(var0, 360.0, 15.0, var5);
               switchFx(var0);
               return var27;
            } else if (inside(var8, var10, 515, 25, 130, 28)) {
               if (trans(var0)) {
                  return -20;
               }

               int var26 = oldClick(var0, 500.0, 15.0, var5);
               switchFx(var0);
               return var26;
            } else if (trans(var0)) {
               List var25 = transferIds(var0);
               Object var28 = call("state", var0);
               if (inside(var8, var10, 248, 216, 83, 15)) {
                  return oldClick(var0, 260.0, 80.0, var5);
               }

               if (inside(var8, var10, 336, 216, 83, 15)) {
                  return oldClick(var0, 340.0, 80.0, var5);
               }

               if (inside(var8, var10, 248, 234, 171, 15)) {
                  return oldClick(var0, 280.0, 225.0, var5);
               }

               if (inside(var8, var10, 40, 243, 160, 104)) {
                  int var29 = (int)((var10 - 244.0) / 17.0);
                  int var33 = num(get(var28, "sourceScroll"));
                  int var36 = var33 + var29;
                  if (var29 >= 0 && var36 < var25.size()) {
                     Object var38 = var25.get(var36);
                     if (var8 >= 166.0 && var8 < 182.0) {
                        call("openInfo", var0, var38);
                        return -20;
                     }

                     Set var39 = selectedTransfer(var0);
                     Set var40 = selectedCurses(var0);
                     return oldClick(var0, 25.0, 244 + var29 * 15 + 3, var5);
                  }
               }

               if (inside(var8, var10, 204, 244, 6, 100)) {
                  int var30 = Math.max(0, var25.size() - 6);
                  int var34 = Math.min(var30, Math.max(0, (int)((var10 - 244.0) * var30 / 100.0)));
                  set(var28, "sourceScroll", var34);
                  return -20;
               } else {
                  return -20;
               }
            } else {
               if (inside(var8, var10, 248, 221, 83, 20)) {
                  return oldClick(var0, 60.0, 329.0, var5);
               }

               if (inside(var8, var10, 336, 221, 83, 20)) {
                  return oldClick(var0, 152.0, 329.0, var5);
               }

               if (inside(var8, var10, 453, 125, 187, 16)) {
                  int var24 = Math.min(3, (int)((var8 - 453.0) / 47.0));
                  set(var0, "tierFilter", var24);
                  set(var0, "scrollOffset", 0);
                  call("call", var0, "keepSelectionValid", new Object[0]);
                  return -20;
               }

               List var14 = all(var0);
               int var15 = Math.max(0, var14.size() - 11);
               int var16 = Math.max(0, Math.min(var15, num(get(var0, "scrollOffset"))));
               if (inside(var8, var10, 636, 150, 6, 185)) {
                  int var32 = var15 == 0 ? 0 : Math.min(var15, Math.max(0, (int)((var10 - 150.0) * var15 / 185.0)));
                  set(var0, "scrollOffset", var32);
                  return -20;
               }

               if (inside(var8, var10, 454, 148, 182, 187)) {
                  int var31 = (int)((var10 - 149.0) / 17.0);
                  int var35 = var16 + var31;
                  if (var35 >= 0 && var35 < var14.size()) {
                     Object var37 = var14.get(var35);
                     if (var8 >= 594.0 && var8 < 611.0) {
                        call("openInfo", var0, var37);
                        return -20;
                     }

                     set(var0, "selectedEnchantment", var37);
                     set(call("state", var0), "costScroll", 0);
                  }

                  return -20;
               } else {
                  if (inside(var8, var10, 40, 244, 166, 103)) {
                     Map var17 = enchants(equipment(var0));
                     int var18 = num(get(call("state", var0), "installedScroll"));
                     int var19 = (int)((var10 - 244.0) / 17.0) + var18;
                     int var20 = 0;

                     for (Object var22 : var17.keySet()) {
                        if (var20++ == var19) {
                           set(var0, "selectedEnchantment", var22);
                           return -20;
                        }
                     }
                  }

                  return -20;
               }
            }
         } catch (Throwable var23) {
            error(var0, "click", var23);
            return -20;
         }
      } else {
         return -10;
      }
   }

   public static boolean mouseScroll(Object var0, double var1, double var3, double var5) {
      if (!RunicGrimoireUi.active(var0)) {
         return false;
      } else {
         int var7 = ax(var0);
         int var8 = ay(var0);
         double var9 = var1 - var7;
         double var11 = var3 - var8;
         if (var5 == 0.0) {
            return inside(var9, var11, 0, 0, 672, 390);
         } else {
            int var13 = var5 < 0.0 ? 1 : -1;
            if (trans(var0) && inside(var9, var11, 35, 230, 188, 128)) {
               Object var19 = call("state", var0);
               int var21 = Math.max(0, transferIds(var0).size() - 6);
               set(var19, "sourceScroll", Math.min(var21, Math.max(0, num(get(var19, "sourceScroll")) + var13)));
               return true;
            } else if (!trans(var0) && inside(var9, var11, 35, 230, 188, 128)) {
               Object var18 = call("state", var0);
               int var20 = Math.max(0, enchants(equipment(var0)).size() - 6);
               set(var18, "installedScroll", Math.min(var20, Math.max(0, num(get(var18, "installedScroll")) + var13)));
               return true;
            } else if (inside(var9, var11, 249, 135, 178, 62)) {
               Object var17 = call("state", var0);
               int var15 = costs(var0, trans(var0)).size();
               int var16 = Math.max(0, var15 - 8);
               set(var17, "costScroll", Math.min(var16, Math.max(0, num(get(var17, "costScroll")) + var13)));
               return true;
            } else if (!trans(var0) && inside(var9, var11, 450, 142, 198, 210)) {
               int var14 = Math.max(0, all(var0).size() - 11);
               set(var0, "scrollOffset", Math.min(var14, Math.max(0, num(get(var0, "scrollOffset")) + var13 * 2)));
               return true;
            } else {
               return inside(var9, var11, 0, 0, 672, 390);
            }
         }
      }
   }

   public static boolean insideScreen(Object var0, double var1, double var3) {
      return RunicGrimoireUi.active(var0) && inside(var1, var3, ax(var0), ay(var0), 672, 390);
   }

   private static int costRowInset(List var0, int var1, int var2) {
      return (4 - Math.min(4, Math.max(0, var0.size() - var1 - var2 / 4 * 4))) * 37 / 2;
   }

   private static final class Pulse {
      final long start;
      final String kind;

      Pulse(long var1, String var3) {
         this.start = var1;
         this.kind = var3;
      }
   }

   private static final class State {
      int mouseX;
      int mouseY;
      int prevX = -9999;
      int prevY = -9999;
      long opened;
      long lastPulseStart;
      long tabFxStart;
      boolean warned;
      final ArrayDeque<RunicRefinedClassicUi.Pulse> pulses = new ArrayDeque<>();
      final Set<String> failedSections = new HashSet<>();
   }
}
