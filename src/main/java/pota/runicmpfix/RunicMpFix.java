package pota.runicmpfix;

import dev.runicattunement.unravel.RunicUnravelRuntime;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_3222;

public final class RunicMpFix implements ModInitializer {
   public static final class_2960 ACTION = new class_2960("pota_runic_mp_fix", "ui_action");
   public static final int ACTION_ATTUNE = 0;
   public static final int ACTION_TRANSMUTE = 1;
   private static final int MAX_IDS = 64;

   public void onInitialize() {
      ServerPlayNetworking.registerGlobalReceiver(ACTION, (var0, var1, var2, var3, var4) -> {
         int var5 = RunicMpFix.PacketCodec.readInt(var3);
         int var6;
         String[] var7;
         String[] var8;
         if (var5 == 0) {
            var6 = RunicMpFix.PacketCodec.readInt(var3);
            var7 = null;
            var8 = null;
         } else {
            if (var5 != 1) {
               return;
            }

            var6 = -1;
            int var9 = boundedCount(RunicMpFix.PacketCodec.readInt(var3));
            if (var9 < 0) {
               return;
            }

            var7 = new String[var9];

            for (int var10 = 0; var10 < var9; var10++) {
               var7[var10] = RunicMpFix.PacketCodec.readString(var3);
            }

            int var12 = boundedCount(RunicMpFix.PacketCodec.readInt(var3));
            if (var12 < 0) {
               return;
            }

            var8 = new String[var12];

            for (int var11 = 0; var11 < var12; var11++) {
               var8[var11] = RunicMpFix.PacketCodec.readString(var3);
            }
         }

         var0.execute(() -> process(var1, var5, var6, var7, var8));
      });
      System.out.println("[Runic Attunement] 1.3.8 initialised (multiplayer action packets enabled)");
   }

   private static int boundedCount(int var0) {
      return var0 >= 0 && var0 <= 64 ? var0 : -1;
   }

   private static void process(class_3222 var0, int var1, int var2, String[] var3, String[] var4) {
      if (!RunicUnravelRuntime.handleMpAction(var0, var1, var2)) {
         try {
            Object var5 = findRunicHandler(var0);
            if (var5 == null) {
               System.err.println("[Runic Attunement] Rejected action: player does not have a Runic Attunement screen open.");
               feedback(var0, "Runic action rejected: the server no longer has this Runic screen open.");
               return;
            }

            if (var1 == 0) {
               Method var13 = var5.getClass().getMethod("getAttunedEnchantments");
               List var14 = (List)var13.invoke(var5);
               if (var2 >= 0 && var2 < var14.size()) {
                  Object var17 = var14.get(var2);
                  Method var20 = var5.getClass().getDeclaredMethod("tryInscribe", class_3222.class, class_2960.class);
                  var20.setAccessible(true);
                  boolean var22 = Boolean.TRUE.equals(var20.invoke(var5, var0, var17));
                  if (!var22) {
                     System.err
                        .println(
                           "[Runic Attunement] Server rejected Attunement for "
                              + var17
                              + ". Check materials, raw XP, compatibility, shelf state and table range."
                        );
                     feedback(var0, "Runic Attunement rejected by server. Check materials, raw XP, compatibility and shelf/table state.");
                  }

                  return;
               }

               System.err.println("[Runic Attunement] Rejected Attunement: stale/invalid enchantment index " + var2 + ".");
               feedback(var0, "Runic Attunement rejected: stale enchantment selection. Reopen the table.");
               return;
            }

            Set var6 = getSetField(var5, "selectedNormal");
            Set var7 = getSetField(var5, "removeCurses");
            var6.clear();
            var7.clear();

            for (String var11 : var3) {
               var6.add(parseIdentifier(var11));
            }

            for (String var23 : var4) {
               var7.add(parseIdentifier(var23));
            }

            Method var16 = var5.getClass().getDeclaredMethod("tryTransmute", class_3222.class);
            var16.setAccessible(true);
            boolean var19 = Boolean.TRUE.equals(var16.invoke(var5, var0));
            if (!var19) {
               System.err
                  .println(
                     "[Runic Attunement] Server rejected Transmutation. Selected="
                        + var6.size()
                        + ", removeCurses="
                        + var7.size()
                        + ". Check source/target compatibility, materials, raw XP and screen/table state."
                  );
               feedback(var0, "Runic Transmutation rejected by server. Check source/target compatibility, materials and raw XP.");
            }
         } catch (Throwable var12) {
            System.err.println("[Runic Attunement] Action processing failed: " + var12);
            var12.printStackTrace();
            feedback(var0, "Runic multiplayer action failed internally. Check latest.log for [Runic Attunement].");
         }
      }
   }

   private static void feedback(class_3222 var0, String var1) {
      try {
         var0.method_7353(class_2561.method_43470(var1), true);
      } catch (Throwable var3) {
      }
   }

   private static Object findRunicHandler(class_3222 var0) throws IllegalAccessException {
      for (Class var1 = var0.getClass(); var1 != null; var1 = var1.getSuperclass()) {
         for (Field var5 : var1.getDeclaredFields()) {
            var5.setAccessible(true);

            Object var6;
            try {
               var6 = var5.get(var0);
            } catch (Throwable var8) {
               continue;
            }

            if (var6 != null && var6.getClass().getName().equals("dev.runicattunement.screen.RunicInscriptionScreenHandler")) {
               return var6;
            }
         }
      }

      return null;
   }

   private static Set<Object> getSetField(Object var0, String var1) throws Exception {
      Field var2 = var0.getClass().getDeclaredField(var1);
      var2.setAccessible(true);
      return (Set<Object>)var2.get(var0);
   }

   private static class_2960 parseIdentifier(String var0) {
      int var1 = var0.indexOf(58);
      return var1 > 0 && var1 < var0.length() - 1 ? new class_2960(var0.substring(0, var1), var0.substring(var1 + 1)) : new class_2960("minecraft", var0);
   }

   static final class PacketCodec {
      private PacketCodec() {
      }

      static int readInt(Object var0) {
         try {
            return ((Number)var0.getClass().getMethod("readInt").invoke(var0)).intValue();
         } catch (ReflectiveOperationException var2) {
            throw new IllegalStateException("Could not read Runic MP packet int", var2);
         }
      }

      static String readString(Object var0) {
         int var1 = readInt(var0);
         if (var1 >= 0 && var1 <= 32767) {
            byte[] var2 = new byte[var1];

            try {
               var0.getClass().getMethod("readBytes", byte[].class).invoke(var0, var2);
            } catch (ReflectiveOperationException var4) {
               throw new IllegalStateException("Could not read Runic MP packet bytes", var4);
            }

            return new String(var2, StandardCharsets.UTF_8);
         } else {
            throw new IllegalArgumentException("Invalid Runic MP packet string length: " + var1);
         }
      }
   }
}
