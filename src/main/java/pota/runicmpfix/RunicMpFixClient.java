package pota.runicmpfix;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.class_2540;

public final class RunicMpFixClient {
   private static final int BUTTON_INSCRIBE_BASE = 1000;
   private static final int BUTTON_TOGGLE_TRANSFER_BASE = 2000;
   private static final int BUTTON_TRANSMUTE = 5000;

   private RunicMpFixClient() {
   }

   public static boolean interceptFinalAction(Object var0, int var1) {
      if (var1 >= 6000 && var1 < 7000) {
         class_2540 var10 = PacketByteBufs.create();
         writeInt(var10, 0);
         writeInt(var10, var1);
         ClientPlayNetworking.send(RunicMpFix.ACTION, var10);
         return true;
      }

      if (var1 >= 1000 && var1 < 2000) {
         class_2540 var9 = PacketByteBufs.create();
         writeInt(var9, 0);
         writeInt(var9, var1 - 1000);
         ClientPlayNetworking.send(RunicMpFix.ACTION, var9);
         return true;
      }

      if (var1 == 5000) {
         try {
            Set var2 = getSetField(var0, "selectedTransfers");
            Set var3 = getSetField(var0, "removeCurses");
            class_2540 var4 = PacketByteBufs.create();
            writeInt(var4, 1);
            writeInt(var4, Math.min(var2.size(), 64));
            int var5 = 0;

            for (Object var7 : var2) {
               if (var5++ >= 64) {
                  break;
               }

               writeString(var4, String.valueOf(var7));
            }

            writeInt(var4, Math.min(var3.size(), 64));
            var5 = 0;

            for (Object var13 : var3) {
               if (var5++ >= 64) {
                  break;
               }

               writeString(var4, String.valueOf(var13));
            }

            ClientPlayNetworking.send(RunicMpFix.ACTION, var4);
            return true;
         } catch (Throwable var8) {
            System.err.println("[Runic Attunement] Could not serialize Transmutation selection; falling back to Runic legacy button packet: " + var8);
            return false;
         }
      } else {
         return false;
      }
   }

   private static void writeInt(Object var0, int var1) {
      try {
         var0.getClass().getMethod("writeInt", int.class).invoke(var0, var1);
      } catch (ReflectiveOperationException var3) {
         throw new IllegalStateException("Could not write Runic MP packet int", var3);
      }
   }

   private static void writeString(Object var0, String var1) {
      byte[] var2 = var1.getBytes(StandardCharsets.UTF_8);
      if (var2.length > 32767) {
         throw new IllegalArgumentException("Runic MP packet string is too long");
      }

      writeInt(var0, var2.length);

      try {
         var0.getClass().getMethod("writeBytes", byte[].class).invoke(var0, var2);
      } catch (ReflectiveOperationException var4) {
         throw new IllegalStateException("Could not write Runic MP packet bytes", var4);
      }
   }

   private static Set<?> getSetField(Object var0, String var1) throws Exception {
      Field var2 = var0.getClass().getDeclaredField(var1);
      var2.setAccessible(true);
      return (Set<?>)var2.get(var0);
   }
}
