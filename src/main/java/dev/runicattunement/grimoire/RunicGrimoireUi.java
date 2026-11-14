package dev.runicattunement.grimoire;

import dev.runicattunement.unravel.RunicUnravelRuntime;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.class_1799;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_5250;
import net.minecraft.class_811;
import net.minecraft.class_918;

public final class RunicGrimoireUi {
   public static final int W = 640;
   public static final int H = 366;
   public static final int MIN_W = 678;
   public static final int MIN_H = 398;
   private static final int LEATHER = -266525907;
   private static final int PAGE = -181718725;
   private static final int PAGE_DEEP = -182376141;
   private static final int EDGE = -9150399;
   private static final int GOLD = -1721994;
   private static final int SOFT_GOLD = -4021647;
   private static final int TEXT = -924215;
   private static final int MUTED = -5986902;
   private static final int VIOLET = -5727764;
   private static final int SILVER = -3683367;
   private static final class_2960 RUNIC_FONT = new class_2960("minecraft", "alt");
   private static final class_2960 STONE_ATTUNEMENT = new class_2960("runic_attunement", "textures/gui/stone_attunement.png");
   private static final class_2960 STONE_TRANSMUTATION = new class_2960("runic_attunement", "textures/gui/stone_transmutation.png");
   private static final class_2960 SEAL_DORMANT = new class_2960("runic_attunement", "textures/gui/stone_sigil_dormant.png");
   private static final class_2960 SEAL_ACTIVE = new class_2960("runic_attunement", "textures/gui/stone_sigil_active.png");
   private static final String RUNIC_SIGILS = "abcdefghiklmnopqrstuvwxyz";
   private static final class_5250[] CACHED_SIGILS = makeRunicSigils();
   private static final int CYAN = -8073253;
   private static final int GREEN = -7222118;
   private static final int RED = -2518133;
   private static final int ENCHANT_ROWS = 11;
   private static final int TRANS_ROWS = 6;
   private static final int INSTALLED_ROWS = 9;
   private static boolean warned = false;
   private static volatile long lastRefundConfigCheck = 0L;
   private static volatile int cachedRefundXpPercent = 50;
   private static final Map<Object, RunicGrimoireUi.State> STATES = Collections.synchronizedMap(new WeakHashMap<>());
   private static volatile Boolean etAvailable = null;

   private static class_5250[] makeRunicSigils() {
      class_5250[] var0 = new class_5250["abcdefghiklmnopqrstuvwxyz".length()];
      class_2583 var1 = class_2583.field_24360.method_27704(RUNIC_FONT);

      for (int var2 = 0; var2 < var0.length; var2++) {
         var0[var2] = class_2561.method_43470(String.valueOf("abcdefghiklmnopqrstuvwxyz".charAt(var2))).method_10862(var1);
      }

      return var0;
   }

   private RunicGrimoireUi() {
   }

   private static void warn(Throwable var0) {
      if (!warned) {
         warned = true;
         System.err.println("[Runic Grimoire] Render/interaction error: " + var0);
         var0.printStackTrace(System.err);
      }
   }

   private static Object get(Object var0, String var1) {
      return RunicUnravelRuntime.accessField(var0, var1);
   }

   private static Object call(Object var0, String var1, Object... var2) {
      return RunicUnravelRuntime.invoke(var0, var1, var2);
   }

   private static int num(Object var0) {
      return var0 instanceof Number ? ((Number)var0).intValue() : 0;
   }

   private static boolean yes(Object var0) {
      return Boolean.TRUE.equals(var0);
   }

   private static RunicGrimoireUi.State state(Object var0) {
      return STATES.computeIfAbsent(var0, var0x -> new RunicGrimoireUi.State());
   }

   private static Object handler(Object var0) {
      return get(var0, "field_2797");
   }

   private static int screenW(Object var0) {
      return num(get(var0, "field_22789"));
   }

   private static int screenH(Object var0) {
      return num(get(var0, "field_22790"));
   }

   private static int x(Object var0) {
      return (screenW(var0) - 640) / 2;
   }

   private static int y(Object var0) {
      return (screenH(var0) - 366) / 2;
   }

   private static boolean trans(Object var0) {
      return yes(call(handler(var0), "isTransmutationMode"));
   }

   public static boolean active(Object var0) {
      return var0 != null && screenW(var0) >= 678 && screenH(var0) >= 398 && !state(var0).classic;
   }

   private static void set(Object var0, String var1, Object var2) {
      if (var0 != null) {
         Class var3 = var0.getClass();

         while (var3 != null) {
            try {
               Field var4 = var3.getDeclaredField(var1);
               var4.setAccessible(true);
               var4.set(var0, var2);
               return;
            } catch (NoSuchFieldException var5) {
               var3 = var3.getSuperclass();
            } catch (Throwable var6) {
               throw new IllegalStateException("Writing " + var1, var6);
            }
         }
      }
   }

   private static void setI(Object var0, String var1, int var2) {
      set(var0, var1, var2);
   }

   private static boolean in(double var0, double var2, int var4, int var5, int var6, int var7) {
      return var0 >= var4 && var0 < var4 + var6 && var2 >= var5 && var2 < var5 + var7;
   }

   private static List<?> slots(Object var0) {
      Object var1 = get(handler(var0), "field_7761");
      return var1 instanceof List ? (List)var1 : Collections.emptyList();
   }

   public static void restore(Object var0) {
      GrimoireEmiGuard.exit();
      RunicGrimoireUi.State var1 = state(var0);
      if (var1.relaid && var1.originalSlots != null) {
         List var2 = slots(var0);

         for (int var3 = 0; var3 < Math.min(var2.size(), var1.originalSlots.size()); var3++) {
            int[] var4 = var1.originalSlots.get(var3);
            setI(var2.get(var3), "field_7873", var4[0]);
            setI(var2.get(var3), "field_7872", var4[1]);
         }

         var1.relaid = false;
         var1.prevX = Integer.MIN_VALUE;
      }

      if (var1.search != null) {
         Object var5 = get(var0, "searchField");
         if (var5 == var1.search) {
            call(var5, "method_46421", var1.searchX);
            call(var5, "method_46419", var1.searchY);
            setI(var5, "field_22758", var1.searchW);
         }

         var1.search = null;
      }
   }

   public static void prepare(Object var0) {
      if (!active(var0)) {
         restore(var0);
         RunicRefinedClassicUi.relayout(var0);
      } else {
         GrimoireEmiGuard.enter();
         RunicGrimoireUi.State var1 = state(var0);
         int var2 = x(var0);
         int var3 = y(var0);
         int var4 = num(get(var0, "field_2776"));
         int var5 = num(get(var0, "field_2800"));
         List var6 = slots(var0);
         boolean var7 = trans(var0);
         if (!var1.relaid) {
            var1.originalSlots = new ArrayList<>(var6.size());

            for (Object var9 : var6) {
               var1.originalSlots.add(new int[]{num(get(var9, "field_7873")), num(get(var9, "field_7872"))});
            }

            var1.relaid = true;
         }

         if (var1.prevX != var2 || var1.prevY != var3 || var1.prevOx != var4 || var1.prevOy != var5 || !var1.lastItem.equals(var7 ? "trans" : "attune")) {
            for (int var12 = 0; var12 < var6.size(); var12++) {
               int var10;
               int var14;
               if (var12 == 0) {
                  var14 = var2 + 98;
                  var10 = var3 + 196;
               } else if (var12 == 1) {
                  var14 = var7 ? var2 + 524 : -4000;
                  var10 = var7 ? var3 + 196 : -4000;
               } else if (var12 < 29) {
                  int var11 = var12 - 2;
                  var14 = var2 + 233 + var11 % 9 * 18;
                  var10 = var3 + 271 + var11 / 9 * 18;
               } else {
                  int var15 = var12 - 29;
                  var14 = var2 + 233 + var15 * 18;
                  var10 = var3 + 329;
               }

               setI(var6.get(var12), "field_7873", var14 - var4);
               setI(var6.get(var12), "field_7872", var10 - var5);
            }

            var1.lastItem = var7 ? "trans" : "attune";
            var1.prevX = var2;
            var1.prevY = var3;
            var1.prevOx = var4;
            var1.prevOy = var5;
         }

         Object var13 = get(var0, "searchField");
         if (var13 != null) {
            if (var1.search != var13) {
               var1.search = var13;
               var1.searchX = num(call(var13, "method_46426"));
               var1.searchY = num(call(var13, "method_46427"));
               var1.searchW = num(get(var13, "field_22758"));
            }

            call(var13, "method_46421", var2 + 445);
            call(var13, "method_46419", var3 + 91);
            setI(var13, "field_22758", 176);
         }

         RunicRefinedClassicUi.relayout(var0);
      }
   }

   private static void rect(class_332 var0, int var1, int var2, int var3, int var4, int var5) {
      var0.method_25294(var1, var2, var1 + var3, var2 + var4, var5);
   }

   private static void frame(class_332 var0, int var1, int var2, int var3, int var4, int var5) {
      var0.method_49601(var1, var2, var3, var4, var5);
   }

   private static class_327 font(Object var0) {
      return (class_327)get(var0, "field_22793");
   }

   private static void text(class_332 var0, class_327 var1, String var2, int var3, int var4, int var5) {
      var0.method_51433(var1, var2, var3, var4, var5, false);
   }

   private static String truncate(String var0, int var1) {
      return var0.length() > var1 ? var0.substring(0, Math.max(0, var1 - 2)) + ".." : var0;
   }

   private static String title(Object var0) {
      if (var0 == null) {
         return "Choose an inscription";
      }

      String var1 = String.valueOf(var0);
      var1 = var1.contains(":") ? var1.substring(var1.indexOf(58) + 1) : var1;
      StringBuilder var2 = new StringBuilder();
      boolean var3 = true;

      for (char var7 : var1.toCharArray()) {
         if (var7 != '_' && var7 != '-' && var7 != '/') {
            var2.append(var3 ? Character.toUpperCase(var7) : var7);
            var3 = false;
         } else {
            var2.append(' ');
            var3 = true;
         }
      }

      return var2.toString();
   }

   private static String roman(int var0) {
      if (var0 < 1) {
         return "-";
      }

      if (var0 > 20) {
         return var0 + "";
      }

      StringBuilder var1 = new StringBuilder();
      int[] var2 = new int[]{10, 9, 5, 4, 1};
      String[] var3 = new String[]{"X", "IX", "V", "IV", "I"};

      for (int var4 = 0; var4 < 5; var4++) {
         while (var0 >= var2[var4]) {
            var0 -= var2[var4];
            var1.append(var3[var4]);
         }
      }

      return var1.toString();
   }

   private static Object current(Object var0) {
      return get(var0, "selectedEnchantment");
   }

   private static Object equipment(Object var0) {
      return call(handler(var0), "getEquipmentStack");
   }

   private static Object source(Object var0) {
      return call(handler(var0), "getSourceStack");
   }

   private static Object target(Object var0) {
      return call(handler(var0), "getTargetStack");
   }

   private static boolean empty(Object var0) {
      return var0 == null || yes(call(var0, "method_7960"));
   }

   private static List<?> filtered(Object var0) {
      Object var1 = call(var0, "filteredEnchantments");
      return var1 instanceof List ? (List)var1 : List.of();
   }

   private static LinkedHashMap<Object, Integer> enchants(Object var0) {
      LinkedHashMap var1 = new LinkedHashMap();
      if (empty(var0)) {
         return var1;
      }

      try {
         Object var2 = call(Class.forName("net.minecraft.class_1890"), "method_8222", var0);
         Object var3 = get(Class.forName("net.minecraft.class_7923"), "field_41176");
         if (var2 instanceof Map) {
            for (Entry var5 : ((Map<?, ?>)var2).entrySet()) {
               Object var6 = call(var3, "method_10221", var5.getKey());
               if (var6 != null) {
                  var1.put(var6, num(var5.getValue()));
               }
            }
         }
      } catch (Throwable var7) {
         warn(var7);
      }

      return var1;
   }

   private static LinkedHashMap<Object, Integer> installed(Object var0) {
      return enchants(equipment(var0));
   }

   private static int level(Object var0) {
      return installed(var0).getOrDefault(current(var0), 0);
   }

   private static List<?> costs(Object var0, boolean var1) {
      Object var2 = call(var0, var1 ? "currentTransmutationCosts" : "currentAttunementCosts");
      return var2 instanceof List ? (List)var2 : List.of();
   }

   private static int xp(Object var0, boolean var1) {
      return num(call(var0, var1 ? "currentTransmutationExperienceCost" : "currentAttunementExperienceCost"));
   }

   private static boolean allowed(Object var0, boolean var1) {
      return yes(call(call(var0, var1 ? "transmutationEligibility" : "eligibility"), "allowed"));
   }

   private static List<?> transferIds(Object var0) {
      Object var1 = call(RunicGrimoireUi.ClassForTransmutation.INSTANCE, "sourceEnchantments", source(var0));
      return var1 instanceof List ? (List)var1 : List.of();
   }

   private static Set<Object> transfers(Object var0) {
      Object var1 = get(var0, "selectedTransfers");
      return var1 instanceof Set ? (Set)var1 : Collections.emptySet();
   }

   private static Set<Object> curses(Object var0) {
      Object var1 = get(var0, "removeCurses");
      return var1 instanceof Set ? (Set)var1 : Collections.emptySet();
   }

   private static boolean isCurse(Object var0) {
      try {
         Object var1 = get(Class.forName("net.minecraft.class_7923"), "field_41176");
         Object var2 = call(var1, "method_17966", var0);
         Object var3 = var2 instanceof Optional ? ((Optional)var2).orElse(null) : null;
         return var3 != null && yes(call(var3, "method_8195"));
      } catch (Throwable var4) {
         return false;
      }
   }

   private static boolean transferable(Object var0, Object var1) {
      try {
         if (isCurse(var1)) {
            return yes(call(RunicGrimoireUi.ClassForTransmutation.INSTANCE, "curseCanTransfer", var1, source(var0), target(var0), transfers(var0)));
         }

         Object var2 = call(
            RunicGrimoireUi.ClassForTransmutation.INSTANCE,
            "normalStatus",
            var1,
            source(var0),
            target(var0),
            call(handler(var0), "getDefinitions"),
            transfers(var0),
            curses(var0)
         );
         return var2 != null && String.valueOf(var2).equals("TRANSFERABLE");
      } catch (Throwable var3) {
         return false;
      }
   }

   private static int index(Object var0, Object var1) {
      return num(call(handler(var0), "indexOf", var1));
   }

   private static void startCharge(Object var0, String var1) {
   }

   private static boolean charging(Object var0) {
      return false;
   }

   private static float progress(Object var0) {
      RunicGrimoireUi.State var1 = state(var0);
      long var2 = var1.chargeEnd - var1.chargeStart;
      return var2 > 0L && charging(var0) ? Math.min(1.0F, Math.max(0.0F, (float)(System.nanoTime() - var1.chargeStart) / (float)var2)) : 0.0F;
   }

   private static void panel(class_332 var0, int var1, int var2, int var3, int var4, int var5) {
   }

   private static void line(class_332 var0, int var1, int var2, int var3) {
      rect(var0, var1, var2, var3, 1, -1636868538);
      rect(var0, var1 + 5, var2 + 2, Math.max(0, var3 - 10), 1, 1181703750);
   }

   private static void bigText(class_332 var0, class_327 var1, String var2, int var3, int var4, int var5, float var6) {
      class_4587 var7 = var0.method_51448();
      var7.method_22903();

      try {
         int var8 = num(call(var1, "method_1727", var2));
         var7.method_22904(var3 - var8 * var6 / 2.0, var4, 0.0);
         var7.method_22905(var6, var6, 1.0F);
         text(var0, var1, var2, 0, 0, var5);
      } finally {
         var7.method_22909();
      }
   }

   private static void plaque(class_332 var0, class_327 var1, String var2, int var3, int var4, int var5) {
      bigText(var0, var1, var2, var3 + var5 / 2, var4 + 6, -4021647, 1.12F);
   }

   private static void button(class_332 var0, class_327 var1, String var2, int var3, int var4, int var5, int var6, boolean var7, boolean var8, int var9) {
      int var10 = !var7 ? -13750215 : (var8 ? -11185828 : -13092027);
      rect(var0, var3 + 2, var4 + 2, var5, var6, -1341782259);
      rect(var0, var3, var4, var5, var6, var10);
      frame(var0, var3, var4, var5, var6, var7 ? var9 : -9145990);
      rect(var0, var3 + 2, var4 + 2, var5 - 4, 1, var7 ? -6979998 : -11251110);
      rect(var0, var3 + 2, var4 + var6 - 3, var5 - 4, 1, -15065819);
      if (var7) {
         rect(var0, var3 + 5, var4 + 4, 1, 1, var9);
         rect(var0, var3 + var5 - 6, var4 + var6 - 5, 1, 1, var9);
      }

      int var11 = num(call(var1, "method_1727", var2));
      text(var0, var1, var2, var3 + Math.max(3, (var5 - var11) / 2), var4 + Math.max(3, (var6 - 8) / 2), var7 ? -924215 : -5986902);
   }

   private static void attuneButton(class_332 var0, class_327 var1, int var2, int var3, int var4, int var5, boolean var6, boolean var7, int var8) {
      int var9 = !var6 ? -13750215 : (var7 ? -11185828 : -13092027);
      rect(var0, var2 + 1, var3 + 2, var4, var5, 2013855755);
      rect(var0, var2, var3, var4, var5, var9);
      frame(var0, var2, var3, var4, var5, var6 ? -1721994 : -10066325);
      if (var6) {
         rect(var0, var2 + 3, var3 + 2, var4 - 6, 1, -1864712032);
      }

      String var10 = "ATTUNE";
      String var11 = var8 + " XP";
      int var12 = num(call(var1, "method_1727", var10));
      int var13 = num(call(var1, "method_1727", var11));
      text(var0, var1, var10, var2 + (var4 - var12) / 2, var3 + 4, var6 ? -924215 : -5986902);
      text(var0, var1, var11, var2 + (var4 - var13) / 2, var3 + 16, var6 ? -4021647 : -5986902);
   }

   private static void runeCircle(Object var0, class_332 var1, class_327 var2, int var3, int var4, int var5, boolean var6, int var7, boolean var8) {
      RunicGrimoireUi.State var9 = state(var0);
      var1.method_25290(var6 ? SEAL_ACTIVE : SEAL_DORMANT, var3 - 56, var4 - 56, 0.0F, 0.0F, 112, 112, 112, 112);
      double var10 = System.nanoTime() / 1.0E9;
      double var12 = (System.nanoTime() - var9.pulseStart) / 1.0E9;
      boolean var14 = var6 && var12 >= 0.0 && var12 < 1.3;
      double var15 = var6 ? var10 * (var14 ? 1.1 : 0.25) : var10 * 0.035;

      for (int var17 = 0; var17 < 48; var17++) {
         double var18 = (Math.PI * 2) * var17 / 48.0;
         int var20 = var3 + (int)Math.round(Math.cos(var18) * (var5 - 2));
         int var21 = var4 + (int)Math.round(Math.sin(var18) * (var5 - 2));
         if (var17 % 3 == 0) {
            rect(var1, var20, var21, 1, 1, var6 ? 1969771667 : 896297081);
         }
      }

      for (int var25 = 0; var25 < 13; var25++) {
         int var27 = Math.floorMod(var7 * 13 + var25 * 11 + var25 * var25 * 3, "abcdefghiklmnopqrstuvwxyz".length());
         class_5250 var19 = CACHED_SIGILS[var27];
         double var30 = (Math.PI * 2) * var25 / 13.0 + var15 + (var7 & 15) * 0.035;
         int var22 = var3 + (int)Math.round(Math.cos(var30) * var5);
         int var23 = var4 + (int)Math.round(Math.sin(var30) * var5);
         int var24 = var14
            ? (var25 % 3 == 0 ? -1721994 : (var25 % 3 == 1 ? -5727764 : -3683367))
            : (
               var8
                  ? (var25 % 4 == 0 ? -5727764 : -1721994)
                  : (var6 ? (var25 % 3 == 0 ? -1721994 : (var25 % 3 == 1 ? -3683367 : -5727764)) : (var25 % 3 == 0 ? -7435878 : -9937040))
            );
         var1.method_27534(var2, var19, var22, var23 - 4, var24);
      }

      if (var14) {
         int var26 = (int)(var12 * 20.0);
         int var28 = var5 + 5 + (int)(var12 * 8.0);
         int var29 = "UNRAVEL".equals(var9.pulseKind) ? -5727764 : ("TRANSMUTE".equals(var9.pulseKind) ? -3683367 : -1721994);

         for (int var31 = 0; var31 < 14; var31++) {
            double var32 = (Math.PI * 2) * var31 / 14.0 + var10 * 0.85;
            int var33 = var28 + var31 % 3 * 2;
            rect(var1, var3 + (int)(var33 * Math.cos(var32)), var4 + (int)(var33 * Math.sin(var32)), (var31 + var26) % 4 == 0 ? 2 : 1, 2, var29);
         }
      }
   }

   public static boolean insideGrimoire(Object var0, double var1, double var3) {
      return RunicRefinedClassicUi.insideScreen(var0, var1, var3);
   }

   private static void scrollBar(class_332 var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      if (var4 > var5) {
         int var7 = Math.max(1, var4 - var5);
         int var8 = Math.max(11, Math.min(var3, Math.round((float)var5 * var3 / var4)));
         int var9 = var2 + (int)Math.round((double)(var3 - var8) * Math.min(var7, Math.max(0, var6)) / var7);
         rect(var0, var1, var2, 5, var3, -15328992);
         rect(var0, var1 + 1, var2 + 1, 3, var3 - 2, -13355195);
         rect(var0, var1, var9, 5, var8, -9018550);
         rect(var0, var1 + 1, var9 + 1, 3, Math.max(1, var8 - 2), -4021647);
         rect(var0, var1 + 2, var9 + Math.max(1, var8 / 2), 1, 1, -731492);
      }
   }

   private static int scrollTo(double var0, int var2, int var3, int var4, int var5) {
      if (var4 <= var5) {
         return 0;
      }

      int var6 = Math.max(11, Math.min(var3, Math.round((float)var5 * var3 / var4)));
      double var7 = (var0 - var2 - var6 / 2.0) / Math.max(1, var3 - var6);
      return Math.max(0, Math.min(var4 - var5, (int)Math.round(var7 * (var4 - var5))));
   }

   private static void label(class_332 var0, class_327 var1, String var2, int var3, int var4) {
      text(var0, var1, var2, var3, var4, -4021647);
   }

   private static void model(Object var0, class_332 var1, Object var2, int var3, int var4, int var5) {
      if (!empty(var2)) {
         class_1799 var6 = (class_1799)var2;
         class_4587 var7 = var1.method_51448();
         var7.method_22903();

         try {
            double var8 = Math.sin(System.nanoTime() / 8.5E8) * 2.0;
            var7.method_22904(var3, var4 + var8, 240.0);
            boolean var10 = false;
            var7.method_22903();

            try {
               class_310 var11 = class_310.method_1551();
               var7.method_22905(var5, var5, var5);
               class_918 var12 = var11.method_1480();
               var12.method_23178(var6, class_811.field_4322, 15728880, 0, var7, var1.method_51450(), var11.field_1687, 0);
               var1.method_51452();
               var10 = true;
            } catch (Throwable var29) {
            } finally {
               var7.method_22909();
            }

            if (!var10) {
               var7.method_22903();

               try {
                  var7.method_22905(3.1F, 3.1F, 3.1F);
                  var1.method_51427(var6, -8, -8);
               } finally {
                  var7.method_22909();
               }
            }
         } finally {
            var7.method_22909();
         }
      }
   }

   private static boolean isSelectedMaxLevel(Object var0) {
      Object var1 = current(var0);
      if (var1 == null) {
         return false;
      }

      int var2 = level(var0);
      if (var2 <= 0) {
         return false;
      }

      Object var3 = call(handler(var0), "getDefinition", var1);
      int var4 = var3 == null ? 0 : num(call(var3, "highestConfiguredLevel"));
      return var4 > 0 && var2 >= var4;
   }

   private static void backgroundCommon(Object var0, class_332 var1) {
      int var2 = x(var0);
      int var3 = y(var0);
      var1.method_25290(trans(var0) ? STONE_TRANSMUTATION : STONE_ATTUNEMENT, var2 - 16, var3 - 12, 0.0F, 0.0F, 672, 390, 672, 390);
   }

   public static boolean drawBackground(Object var0, class_332 var1, float var2, int var3, int var4) {
      return RunicRefinedClassicUi.background(var0, var1, var2, var3, var4);
   }

   private static String naturalName(Object var0, int var1) {
      if (var0 == null) {
         return "No enchantment";
      }

      try {
         Object var2 = get(Class.forName("net.minecraft.class_7923"), "field_41176");
         Object var3 = call(var2, "method_17966", var0);
         Object var4 = var3 instanceof Optional ? ((Optional)var3).orElse(null) : null;
         if (var4 != null) {
            Object var5 = call(var4, "method_8179", Math.max(1, var1));
            if (var5 != null) {
               Object var6 = call(var5, "method_10851");
               if (var6 instanceof String && !((String)var6).isBlank()) {
                  return (String)var6;
               }
            }
         }
      } catch (Throwable var7) {
      }

      return title(var0) + (var1 > 0 ? " " + roman(var1) : "");
   }

   private static void header(Object var0, class_332 var1, class_327 var2) {
      int var3 = x(var0);
      int var4 = y(var0);
      boolean var5 = trans(var0);
      RunicGrimoireUi.State var6 = state(var0);
      plaque(var1, var2, "RUNIC ATTUNEMENT TABLE", var3 + 10, var4 + 8, 333);
      button(var1, var2, "ATTUNEMENT", var3 + 355, var4 + 8, 130, 20, !var5, in(var6.mouseX, var6.mouseY, var3 + 355, var4 + 8, 130, 20), -1721994);
      button(var1, var2, "TRANSMUTATION", var3 + 492, var4 + 8, 136, 20, var5, in(var6.mouseX, var6.mouseY, var3 + 492, var4 + 8, 136, 20), -5727764);
   }

   private static String costName(Object var0) {
      Object var1 = call(var0, "itemId");
      return title(var1);
   }

   private static void costGrid(Object var0, class_332 var1, class_327 var2, int var3, int var4, int var5, boolean var6) {
      List var7 = costs(var0, var6);
      RunicGrimoireUi.State var8 = state(var0);
      int var9 = Math.min(var8.costScroll, Math.max(0, var7.size() - var5));

      for (int var10 = var9; var10 < Math.min(var7.size(), var9 + var5); var10++) {
         Object var11 = var7.get(var10);
         int var12 = var10 - var9;
         text(var1, var2, truncate(costName(var11), 19), var3, var4 + var12 * 14, -924215);
         String var13 = "x" + num(call(var11, "count"));
         text(var1, var2, var13, var3 + 141, var4 + var12 * 14, -4021647);
      }

      if (var7.isEmpty()) {
         text(var1, var2, "No additional materials", var3, var4, -5986902);
      }

      scrollBar(var1, var3 + 165, var4, var5 * 14, var7.size(), var5, var9);
   }

   private static void attune(Object var0, class_332 var1, class_327 var2) {
      int var3 = x(var0);
      int var4 = y(var0);
      RunicGrimoireUi.State var5 = state(var0);
      Object var6 = current(var0);
      int var7 = level(var0);
      boolean var8 = !empty(equipment(var0));
      LinkedHashMap<Object, Integer> var9 = installed(var0);
      plaque(var1, var2, "EQUIPMENT", var3 + 16, var4 + 49, 182);
      if (var8) {
         model(var0, var1, equipment(var0), var3 + 107, var4 + 129, 33);
      }

      bigText(var1, var2, truncate(naturalName(var6, 0), 27), var3 + 107, var4 + 229, var6 == null ? -5986902 : -1721994, 1.1F);
      bigText(var1, var2, "MATERIALS", var3 + 107, var4 + 248, -4021647, 0.98F);
      costGrid(var0, var1, var2, var3 + 22, var4 + 266, 2, false);
      boolean var10 = var8 && var6 != null && var7 > 0 && RunicUnravelRuntime.canUnravel(handler(var0), var6);
      String var11 = var10 ? estimateRefund(var0, var7) : "Not available";
      text(var1, var2, "Refund: " + truncate(var11, 22), var3 + 22, var4 + 305, var10 ? -7222118 : -5986902);
      button(
         var1, var2, "UNRAVEL", var3 + 22, var4 + 319, 83, 25, var10 && !charging(var0), in(var5.mouseX, var5.mouseY, var3 + 22, var4 + 319, 83, 25), -5727764
      );
      boolean var12 = var8 && var6 != null && allowed(var0, false) && !charging(var0);
      attuneButton(var1, var2, var3 + 114, var4 + 319, 83, 25, var12, in(var5.mouseX, var5.mouseY, var3 + 114, var4 + 319, 83, 25), xp(var0, false));
      plaque(var1, var2, "INSCRIBED RUNES", var3 + 222, var4 + 49, 192);
      ArrayList var13 = new ArrayList(var9.entrySet());
      int var14 = Math.min(var5.installedScroll, Math.max(0, var13.size() - 9));

      for (int var15 = var14; var15 < Math.min(var13.size(), var14 + 9); var15++) {
         Entry var16 = (Entry)var13.get(var15);
         int var17 = var4 + 80 + (var15 - var14) * 17;
         boolean var18 = Objects.equals(var16.getKey(), var6);
         rect(
            var1,
            var3 + 229,
            var17,
            176,
            16,
            var18 ? -11910312 : (in(var5.mouseX, var5.mouseY, var3 + 229, var17, 176, 16) ? -13288635 : (var15 % 2 == 0 ? -14078922 : -14210252))
         );
         if (var18) {
            frame(var1, var3 + 229, var17, 176, 16, -5727764);
         }

         text(var1, var2, truncate(naturalName(var16.getKey(), (Integer)var16.getValue()), 27), var3 + 235, var17 + 4, var18 ? -1721994 : -924215);
      }

      if (var13.isEmpty()) {
         text(var1, var2, "No inscriptions yet", var3 + 233, var4 + 91, -5986902);
      }

      scrollBar(var1, var3 + 408, var4 + 80, 153, var13.size(), 9, var14);
      inventoryLabel(var0, var1, var2);
      plaque(var1, var2, "ATTUNEMENT TOME", var3 + 442, var4 + 49, 182);
      bigText(var1, var2, "Search & Filter", var3 + 533, var4 + 73, -5986902, 0.9F);
      int var33 = num(get(var0, "tierFilter"));
      int[] var34 = new int[]{var3 + 445, var3 + 493, var3 + 539, var3 + 585};
      String[] var35 = new String[]{"ALL", "I", "II", "III"};

      for (int var36 = 0; var36 < 4; var36++) {
         button(
            var1,
            var2,
            var35[var36],
            var34[var36],
            var4 + 114,
            var36 == 0 ? 43 : 42,
            18,
            var33 == var36,
            in(var5.mouseX, var5.mouseY, var34[var36], var4 + 114, var36 == 0 ? 43 : 42, 18),
            -1721994
         );
      }

      List var37 = filtered(var0);
      int var19 = Math.min(Math.max(0, num(get(var0, "scrollOffset"))), Math.max(0, var37.size() - 11));

      for (int var20 = var19; var20 < Math.min(var19 + 11, var37.size()); var20++) {
         Object var21 = var37.get(var20);
         int var22 = var20 - var19;
         int var23 = var4 + 139 + var22 * 17;
         int var24 = var9.getOrDefault(var21, 0);
         boolean var25 = Objects.equals(var21, var6);
         Object var26 = call(handler(var0), "getDefinition", var21);
         int var27 = var26 == null ? 0 : num(call(var26, "tabletTier"));
         boolean var28 = var24 > 0 && var26 != null && var24 >= num(call(var26, "highestConfiguredLevel"));
         boolean var29 = in(var5.mouseX, var5.mouseY, var3 + 442, var23, 183, 16);
         rect(var1, var3 + 442, var23, 181, 16, var25 ? -11319965 : (var29 ? -12369069 : ((var22 & 1) == 0 ? -13815494 : -14275788)));
         if (var25) {
            frame(var1, var3 + 442, var23, 183, 16, -5727764);
         }

         if (var24 > 0) {
            rect(var1, var3 + 444, var23 + 3, 2, 10, var28 ? -1721994 : -8073253);
         }

         text(var1, var2, truncate(naturalName(var21, 0), 20), var3 + 450, var23 + 4, var28 ? -1721994 : (var24 > 0 ? -8073253 : (var25 ? -924215 : -5986902)));
         if (infoAvailable()) {
            int var30 = in(var5.mouseX, var5.mouseY, var3 + 580, var23 + 1, 14, 14) ? -1721994 : -8073253;
            rect(var1, var3 + 579, var23 + 2, 14, 12, -14208193);
            frame(var1, var3 + 579, var23 + 2, 14, 12, -10587537);
            text(var1, var2, "i", var3 + 584, var23 + 4, var30);
         }

         int var38 = var27 == 3 ? -1721994 : (var27 == 2 ? -5727764 : -3683367);
         rect(var1, var3 + 597, var23 + 2, 23, 12, -14143426);
         frame(var1, var3 + 597, var23 + 2, 23, 12, var38);
         String var31 = var27 > 0 ? roman(var27) : "-";
         int var32 = num(call(var2, "method_1727", var31));
         text(var1, var2, var31, var3 + 608 - var32 / 2, var23 + 4, var38);
      }

      if (var37.isEmpty()) {
         text(var1, var2, "No compatible inscriptions", var3 + 446, var4 + 149, -5986902);
      }

      scrollBar(var1, var3 + 624, var4 + 139, 187, var37.size(), 11, var19);
   }

   private static int refundXpPercent() {
      long var0 = System.nanoTime();
      if (var0 - lastRefundConfigCheck < 5000000000L) {
         return cachedRefundXpPercent;
      }

      try {
         Path var2 = Path.of("config", "runic_attunement.json");
         if (Files.exists(var2)) {
            String var3 = Files.readString(var2);
            Matcher var4 = Pattern.compile("\\\"unravel\\\"\\s*:\\s*\\{([^}]*)\\}").matcher(var3);
            if (var4.find()) {
               Matcher var5 = Pattern.compile("\\\"xp_refund_percent\\\"\\s*:\\s*(\\d+)").matcher(var4.group(1));
               if (var5.find()) {
                  cachedRefundXpPercent = Math.max(0, Math.min(100, Integer.parseInt(var5.group(1))));
               }
            }
         }
      } catch (Throwable var6) {
      }

      lastRefundConfigCheck = var0;
      return cachedRefundXpPercent;
   }

   private static String estimateRefund(Object var0, int var1) {
      try {
         Object var2 = call(handler(var0), "getDefinition", current(var0));
         long var3 = 0L;

         for (int var5 = 1; var5 <= var1; var5++) {
            var3 += num(call(handler(var0), "getAttunementExperienceCost", var2, var5));
         }

         return var3 * refundXpPercent() / 100L + " XP + materials";
      } catch (Throwable var6) {
         return "Calculated refund";
      }
   }

   private static void inventoryLabel(Object var0, class_332 var1, class_327 var2) {
      int var3 = x(var0);
      int var4 = y(var0);
      bigText(var1, var2, "INVENTORY", var3 + 322, var4 + 256, -4021647, 0.95F);

      for (int var5 = 0; var5 < 36; var5++) {
         int var6 = var5 < 27 ? var5 / 9 : 3;
         int var7 = var5 % 9;
         int var8 = var3 + 233 + var7 * 18;
         int var9 = var4 + (var6 < 3 ? 271 + var6 * 18 : 329);
         rect(var1, var8, var9, 18, 18, 1179933798);
         frame(var1, var8, var9, 18, 18, 1431660409);
      }
   }

   private static void transmutation(Object var0, class_332 var1, class_327 var2) {
      int var3 = x(var0);
      int var4 = y(var0);
      RunicGrimoireUi.State var5 = state(var0);
      plaque(var1, var2, "SOURCE EQUIPMENT", var3 + 16, var4 + 49, 182);
      if (!empty(source(var0))) {
         model(var0, var1, source(var0), var3 + 107, var4 + 129, 30);
      }

      label(var1, var2, "SOURCE ENCHANTMENTS", var3 + 20, var4 + 227);
      List var6 = transferIds(var0);
      int var7 = Math.min(var5.sourceScroll, Math.max(0, var6.size() - 6));

      for (int var8 = var7; var8 < Math.min(var6.size(), var7 + 6); var8++) {
         Object var9 = var6.get(var8);
         int var10 = var4 + 244 + (var8 - var7) * 15;
         boolean var11 = transfers(var0).contains(var9);
         boolean var12 = curses(var0).contains(var9);
         rect(
            var1,
            var3 + 17,
            var10,
            174,
            14,
            var11 ? -13084580 : (var12 ? -10205358 : (in(var5.mouseX, var5.mouseY, var3 + 17, var10, 174, 14) ? -12171946 : -13354684))
         );
         boolean var13 = var11 || var12 || transferable(var0, var9);
         if (var11 || var12) {
            frame(var1, var3 + 17, var10, 174, 14, var11 ? -7222118 : -5727764);
         }

         int var14 = var3 + 21;
         int var15 = var10 + 2;
         rect(var1, var14, var15, 11, 11, var11 ? -13214120 : (var12 ? -9877680 : -14669259));
         frame(var1, var14, var15, 11, 11, var11 ? -7222118 : (var12 ? -2518133 : -7761505));
         if (var11) {
            rect(var1, var14 + 2, var15 + 5, 2, 2, -7222118);
            rect(var1, var14 + 4, var15 + 7, 2, 2, -7222118);
            rect(var1, var14 + 6, var15 + 5, 2, 2, -7222118);
            rect(var1, var14 + 8, var15 + 3, 2, 2, -7222118);
         } else if (var12) {
            text(var1, var2, "X", var14 + 2, var15 + 1, -2518133);
         }

         text(var1, var2, truncate(naturalName(var9, 0), 23), var3 + 38, var10 + 3, var11 ? -7222118 : (var12 ? -2518133 : (var13 ? -924215 : -5986902)));
      }

      if (var6.isEmpty()) {
         text(var1, var2, "No source enchantments", var3 + 22, var4 + 249, -5986902);
      }

      scrollBar(var1, var3 + 200, var4 + 244, 90, var6.size(), 6, var7);
      plaque(var1, var2, "TRANSMUTATION", var3 + 222, var4 + 49, 192);
      label(var1, var2, "TRANSFER COST", var3 + 236, var4 + 137);
      costGrid(var0, var1, var2, var3 + 237, var4 + 154, 3, true);
      text(var1, var2, "XP: " + xp(var0, true), var3 + 237, var4 + 199, -1721994);
      button(
         var1,
         var2,
         "TRANSMUTE",
         var3 + 242,
         var4 + 217,
         160,
         26,
         allowed(var0, true) && !charging(var0),
         in(var5.mouseX, var5.mouseY, var3 + 242, var4 + 217, 160, 26),
         -5727764
      );
      button(var1, var2, "SELECT ALL", var3 + 242, var4 + 73, 76, 20, !var6.isEmpty(), in(var5.mouseX, var5.mouseY, var3 + 242, var4 + 73, 76, 20), -4021647);
      button(var1, var2, "CLEAR", var3 + 326, var4 + 73, 76, 20, !var6.isEmpty(), in(var5.mouseX, var5.mouseY, var3 + 326, var4 + 73, 76, 20), -4021647);
      plaque(var1, var2, "TARGET EQUIPMENT", var3 + 442, var4 + 49, 182);
      if (!empty(target(var0))) {
         model(var0, var1, target(var0), var3 + 533, var4 + 129, 30);
      }

      label(var1, var2, "RESULT PREVIEW", var3 + 446, var4 + 227);
      LinkedHashMap<Object, Integer> var18 = enchants(target(var0));
      LinkedHashSet var19 = new LinkedHashSet(var18.keySet());
      var19.addAll(transfers(var0));
      var19.addAll(curses(var0));
      int var20 = Math.min(var5.targetScroll, Math.max(0, var19.size() - 6));
      int var21 = 0;

      for (Object var23 : new ArrayList(var19).subList(var20, var19.size())) {
         if (var21 >= 6) {
            break;
         }

         int var24 = var18.getOrDefault(var23, 0);
         boolean var25 = transfers(var0).contains(var23);
         boolean var16 = curses(var0).contains(var23);
         int var17 = var4 + 244 + var21 * 15;
         rect(var1, var3 + 442, var17, 183, 14, var21 % 2 == 0 ? -13354684 : -13880772);
         text(
            var1,
            var2,
            (var16 ? "- " : (var25 ? "+ " : "  ")) + truncate(naturalName(var23, var24), 25),
            var3 + 449,
            var17 + 3,
            var16 ? -2518133 : (var25 ? -7222118 : -924215)
         );
         var21++;
      }

      if (var19.isEmpty()) {
         text(var1, var2, "Target enchantments appear here", var3 + 442, var4 + 249, -5986902);
      }

      scrollBar(var1, var3 + 626, var4 + 244, 90, var19.size(), 6, var20);
      inventoryLabel(var0, var1, var2);
   }

   private static boolean infoAvailable() {
      if (etAvailable != null) {
         return etAvailable;
      }

      boolean var0 = false;

      try {
         Class var1 = Class.forName("dev.runicattunement.compat.EnchantedTooltipsBridge");
         Method var2 = var1.getDeclaredMethod("available");
         var2.setAccessible(true);
         var0 = Boolean.TRUE.equals(var2.invoke(null));
      } catch (Throwable var3) {
      }

      etAvailable = var0;
      return var0;
   }

   private static void openInfo(Object var0, Object var1) {
      RunicGrimoireUi.State var2 = state(var0);
      String var3 = String.valueOf(var1);
      if (var3.equals(var2.infoEnchant)) {
         var2.infoEnchant = "";
      } else {
         var2.infoEnchant = var3;
         var2.infoTitle = naturalName(var1, 0);
         var2.infoDescription = "No Enchanted ToolTips description available.";

         try {
            Class var4 = Class.forName("dev.runicattunement.compat.EnchantedTooltipsBridge");
            Method var5 = var4.getDeclaredMethod("translatedDescription", String.class);
            Method var6 = var4.getDeclaredMethod("usableDescription", Object.class, String.class);
            var5.setAccessible(true);
            var6.setAccessible(true);
            Object var7 = var5.invoke(null, var3);
            Object var8 = var6.invoke(null, var7, var3);
            if (var8 instanceof String && !((String)var8).isBlank()) {
               var2.infoDescription = (String)var8;
            }
         } catch (Throwable var9) {
         }
      }
   }

   private static void infoPopup(Object var0, class_332 var1, class_327 var2) {
      RunicGrimoireUi.State var3 = state(var0);
      if (!var3.infoEnchant.isEmpty() && !trans(var0)) {
         int var4 = x(var0);
         int var5 = y(var0);
         int var6 = var4 + 174;
         int var7 = var5 + 96;
         short var8 = 292;
         short var9 = 171;
         rect(var1, var6 + 3, var7 + 4, var8, var9, -1426063360);
         rect(var1, var6, var7, var8, var9, -14472650);
         frame(var1, var6, var7, var8, var9, -1721994);
         rect(var1, var6 + 4, var7 + 4, var8 - 8, 25, -13090736);
         frame(var1, var6 + 4, var7 + 4, var8 - 8, 25, -9150399);
         bigText(var1, var2, truncate(var3.infoTitle, 39), var6 + var8 / 2, var7 + 12, -1721994, 1.07F);
         button(var1, var2, "X", var6 + var8 - 24, var7 + 7, 16, 17, true, in(var3.mouseX, var3.mouseY, var6 + var8 - 24, var7 + 7, 16, 17), -5727764);
         String var10 = var3.infoDescription.replace('\n', ' ');
         String[] var11 = var10.trim().split("\\s+");
         StringBuilder var12 = new StringBuilder();
         int var13 = var7 + 41;

         for (String var17 : var11) {
            if (var13 > var7 + var9 - 28) {
               break;
            }

            if (var12.length() > 0 && num(call(var2, "method_1727", var12 + " " + var17)) > var8 - 28) {
               text(var1, var2, var12.toString(), var6 + 15, var13, -924215);
               var12.setLength(0);
               var13 += 13;
            }

            if (var12.length() > 0) {
               var12.append(' ');
            }

            var12.append(var17);
         }

         if (var12.length() > 0 && var13 <= var7 + var9 - 20) {
            text(var1, var2, truncate(var12.toString(), 51), var6 + 15, var13, -924215);
         }

         text(var1, var2, "Enchanted ToolTips  |  click outside to close", var6 + 15, var7 + var9 - 16, -5986902);
      }
   }

   private static void chargeOverlay(Object var0, class_332 var1, class_327 var2) {
      if (charging(var0)) {
         RunicGrimoireUi.State var3 = state(var0);
         int var4 = x(var0);
         int var5 = y(var0);
         float var6 = progress(var0);
         rect(var1, var4 + 226, var5 + 42, 187, 4, -14343378);
         rect(var1, var4 + 226, var5 + 42, (int)(187.0F * var6), 4, var3.chargeKind.equals("UNRAVEL") ? -5727764 : -1721994);
         text(var1, var2, var3.chargeKind + "  " + (int)(var6 * 100.0F) + "%", var4 + 275, var5 + 56, -1721994);
         int var7 = var4 + 320;
         int var8 = var5 + 127;

         for (int var9 = 0; var9 < 8; var9++) {
            double var10 = var9 * Math.PI / 4.0 + var6 * Math.PI * 2.0;
            int var12 = 52 - (int)(var6 * 18.0F);
            rect(
               var1,
               var7 + (int)(var12 * Math.cos(var10)),
               var8 + (int)(var12 * Math.sin(var10)),
               2,
               2,
               var3.chargeKind.equals("UNRAVEL") ? -5727764 : -1721994
            );
         }
      }
   }

   public static boolean drawForeground(Object var0, class_332 var1) {
      return RunicRefinedClassicUi.foreground(var0, var1);
   }

   public static void maybeRenderInfo(Object var0, Object var1, int var2, int var3) {
      if (!active(var0)) {
         try {
            call(Class.forName("dev.runicattunement.compat.EnchantedTooltipsBridge"), "renderInfo", var0, var1, var2, var3);
         } catch (Throwable var5) {
         }
      }
   }

   public static void classicButton(Object var0, class_332 var1) {
   }

   private static void pulse(Object var0, String var1) {
      RunicGrimoireUi.State var2 = state(var0);
      var2.pulseStart = System.nanoTime();
      var2.pulseKind = var1;
   }

   private static int classicLegacyClick(Object var0, double var1, double var3, int var5) {
      try {
         RunicGrimoireUi.State var6 = state(var0);
         int var7 = x(var0);
         int var8 = y(var0);
         if (!var6.classic && active(var0)) {
            prepare(var0);
            boolean var9 = trans(var0);
            if (!var6.infoEnchant.isEmpty()) {
               var6.infoEnchant = "";
               return -20;
            }

            if (var5 != 0) {
               return in(var1, var3, var7, var8, 640, 366) ? -10 : -1000;
            }

            if (var9) {
               if (in(var1, var3, var7 + 199, var8 + 244, 8, 90)) {
                  var6.sourceScroll = scrollTo(var3, var8 + 244, 90, transferIds(var0).size(), 6);
                  return -20;
               }

               if (in(var1, var3, var7 + 625, var8 + 244, 7, 90)) {
                  LinkedHashSet var10 = new LinkedHashSet<>(enchants(target(var0)).keySet());
                  var10.addAll(transfers(var0));
                  var10.addAll(curses(var0));
                  int var11 = var10.size();
                  var6.targetScroll = scrollTo(var3, var8 + 244, 90, var11, 6);
                  return -20;
               }
            } else {
               if (in(var1, var3, var7 + 407, var8 + 80, 8, 153)) {
                  var6.installedScroll = scrollTo(var3, var8 + 80, 153, installed(var0).size(), 9);
                  return -20;
               }

               if (in(var1, var3, var7 + 623, var8 + 139, 9, 187)) {
                  setI(var0, "scrollOffset", scrollTo(var3, var8 + 139, 187, filtered(var0).size(), 11));
                  return -20;
               }
            }

            if (in(var1, var3, var7 + 355, var8 + 8, 130, 20)) {
               return var9 ? 10 : -20;
            }

            if (in(var1, var3, var7 + 492, var8 + 8, 136, 20)) {
               return var9 ? -20 : 11;
            }

            if (in(var1, var3, var7 + 232, var8 + 270, 164, 78)) {
               return -10;
            }

            if (var9) {
               if (in(var1, var3, var7 + 95, var8 + 193, 25, 26) || in(var1, var3, var7 + 521, var8 + 193, 25, 26)) {
                  return -10;
               }

               if (in(var1, var3, var7 + 242, var8 + 73, 76, 20)) {
                  for (Object var22 : transferIds(var0)) {
                     if (!isCurse(var22) && transferable(var0, var22)) {
                        transfers(var0).add(var22);
                     }
                  }

                  return 4000;
               }

               if (in(var1, var3, var7 + 326, var8 + 73, 76, 20)) {
                  transfers(var0).clear();
                  return 4001;
               }

               if (in(var1, var3, var7 + 242, var8 + 217, 160, 26)) {
                  if (!charging(var0) && allowed(var0, true)) {
                     startCharge(var0, "TRANSMUTE");
                     pulse(var0, "TRANSMUTE");
                     return 5000;
                  }

                  return -20;
               }

               if (in(var1, var3, var7 + 17, var8 + 244, 192, 90)) {
                  int var14 = (int)(var3 - var8 - 244.0) / 15 + var6.sourceScroll;
                  List var21 = transferIds(var0);
                  if (var14 >= 0 && var14 < var21.size()) {
                     Object var12 = var21.get(var14);
                     if (isCurse(var12)) {
                        if (curses(var0).contains(var12)) {
                           curses(var0).remove(var12);
                           return 3000 + var14;
                        }

                        if (transferable(var0, var12)) {
                           curses(var0).add(var12);
                           return 3000 + var14;
                        }

                        return -20;
                     }

                     if (transfers(var0).contains(var12)) {
                        transfers(var0).remove(var12);
                        return 2000 + var14;
                     }

                     if (transferable(var0, var12)) {
                        transfers(var0).add(var12);
                        return 2000 + var14;
                     }

                     return -20;
                  }

                  return -20;
               }
            } else {
               if (in(var1, var3, var7 + 95, var8 + 192, 25, 26) || in(var1, var3, var7 + 445, var8 + 91, 176, 19)) {
                  return -10;
               }

               if (in(var1, var3, var7 + 22, var8 + 319, 83, 25)) {
                  Object var20 = current(var0);
                  int var26 = index(var0, var20);
                  if (var26 >= 0 && !charging(var0) && level(var0) > 0 && RunicUnravelRuntime.canUnravel(handler(var0), var20)) {
                     startCharge(var0, "UNRAVEL");
                     pulse(var0, "UNRAVEL");
                     return 6000 + var26;
                  }

                  return -20;
               }

               if (in(var1, var3, var7 + 114, var8 + 319, 83, 25)) {
                  Object var19 = current(var0);
                  Object var25 = equipment(var0);
                  int var28 = var19 == null ? -1 : index(var0, var19);
                  if (var28 >= 0 && !empty(var25) && !charging(var0) && allowed(var0, false)) {
                     startCharge(var0, "ATTUNE");
                     pulse(var0, "ATTUNE");
                     return 1000 + var28;
                  }

                  return -20;
               }

               if (in(var1, var3, var7 + 445, var8 + 114, 183, 18)) {
                  int var18 = var1 < var7 + 493 ? 0 : (var1 < var7 + 539 ? 1 : (var1 < var7 + 585 ? 2 : 3));
                  setI(var0, "tierFilter", var18);
                  setI(var0, "scrollOffset", 0);
                  call(var0, "keepSelectionValid");
                  return -20;
               }

               if (in(var1, var3, var7 + 442, var8 + 139, 183, 187)) {
                  int var17 = (int)(var3 - var8 - 139.0) / 17 + num(get(var0, "scrollOffset"));
                  List var24 = filtered(var0);
                  if (var17 >= 0 && var17 < var24.size()) {
                     Object var27 = var24.get(var17);
                     if (in(var1, var3, var7 + 579, var8 + 139 + (var17 - num(get(var0, "scrollOffset"))) * 17 + 1, 15, 15) && infoAvailable()) {
                        openInfo(var0, var27);
                        return -20;
                     }

                     var6.infoEnchant = "";
                     set(var0, "selectedEnchantment", var27);
                     var6.costScroll = 0;
                  }

                  return -20;
               }

               if (in(var1, var3, var7 + 229, var8 + 80, 181, 153)) {
                  int var16 = (int)(var3 - var8 - 80.0) / 17 + var6.installedScroll;
                  ArrayList var23 = new ArrayList<>(installed(var0).keySet());
                  if (var16 >= 0 && var16 < var23.size()) {
                     set(var0, "selectedEnchantment", var23.get(var16));
                     var6.costScroll = 0;
                  }

                  return -20;
               }
            }

            return in(var1, var3, var7, var8, 640, 366) ? -20 : -10;
         } else if (var5 == 0 && in(var1, var3, num(get(var0, "field_2776")) + 246, num(get(var0, "field_2800")) + 8, 70, 18)) {
            var6.classic = false;
            prepare(var0);
            return -20;
         } else {
            return -1000;
         }
      } catch (Throwable var13) {
         warn(var13);
         return -20;
      }
   }

   public static boolean scroll(Object var0, double var1, double var3, double var5) {
      return RunicRefinedClassicUi.mouseScroll(var0, var1, var3, var5);
   }

   public static void close(Object var0) {
      try {
         restore(var0);
         STATES.remove(var0);
      } catch (Throwable var5) {
         warn(var5);
      } finally {
         GrimoireEmiGuard.exit();
      }
   }

   public static int click(Object var0, double var1, double var3, int var5) {
      return RunicRefinedClassicUi.click(var0, var1, var3, var5);
   }

   private static class ClassForTransmutation {
      private static Class<?> INSTANCE;

      static {
         try {
            INSTANCE = Class.forName("dev.runicattunement.transmutation.TransmutationRules");
         } catch (Throwable var1) {
         }
      }
   }

   private static final class State {
      boolean classic = false;
      boolean relaid = false;
      List<int[]> originalSlots;
      Object search;
      int searchX;
      int searchY;
      int searchW;
      int prevX = Integer.MIN_VALUE;
      int prevY = Integer.MIN_VALUE;
      int prevOx = Integer.MIN_VALUE;
      int prevOy = Integer.MIN_VALUE;
      int installedScroll = 0;
      int costScroll = 0;
      int sourceScroll = 0;
      int targetScroll = 0;
      long chargeEnd = 0L;
      long chargeStart = 0L;
      String chargeKind = "";
      String lastItem = "";
      long pulseStart = 0L;
      String pulseKind = "";
      String enchantFingerprint = "";
      String infoEnchant = "";
      String infoTitle = "";
      String infoDescription = "";
      int mouseX = -9999;
      int mouseY = -9999;
   }
}
