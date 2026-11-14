package dev.runicattunement.grimoire;

import net.minecraft.class_332;

public final class RunicGuiSparkles {
   private static final int GOLD = -931967;
   private static final int VIOLET = -2644737;
   private static final int LAVENDER = -5799207;
   private static final int CYAN = -6561815;

   private RunicGuiSparkles() {
   }

   private static void dot(class_332 var0, int var1, int var2, int var3, int var4, float var5) {
      int var6 = Math.min(255, Math.max(0, (int)(255.0F * var5)));
      if (var6 > 0) {
         var0.method_25294(var1, var2, var1 + var3, var2 + var3, var6 << 24 | var4 & 16777215);
      }
   }

   private static double fract(double var0) {
      return var0 - Math.floor(var0);
   }

   private static double hash(int var0) {
      return fract(Math.sin(var0 * 78.233 + 9.783) * 43758.5453);
   }

   private static double ease(double var0) {
      var0 = Math.min(1.0, Math.max(0.0, var0));
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   public static void draw(Object var0, class_332 var1, int var2, int var3, long var4, String var6, boolean var7) {
      if (var4 > 0L && var6 != null) {
         double var8 = (System.nanoTime() - var4) * 1.0E-9;
         if (!(var8 < 0.0) && !(var8 > 1.38)) {
            double var10 = Math.max(0.0, 1.0 - var8 / 1.38);
            boolean var12 = var7 && "TRANSMUTE".equals(var6);
            if (var12) {
               int var13 = var2 + 107;
               int var14 = var2 + 533;
               int var15 = var3 + 129;

               for (int var16 = 0; var16 < 45; var16++) {
                  double var17 = var16 * 0.012;
                  double var19 = (var8 - var17) / 1.05;
                  if (!(var19 < 0.0) && !(var19 > 1.0)) {
                     double var21 = ease(var19);
                     double var23 = (hash(var16 + 11) - 0.5) * 27.0;
                     double var25 = -32.0 * Math.sin(var21 * Math.PI);
                     int var27 = (int)Math.round(var13 + (var14 - var13) * var21 + (hash(var16 + 29) - 0.5) * 9.0);
                     int var28 = (int)Math.round(var15 + var25 + var23 * (0.45 + Math.sin(var21 * Math.PI) * 0.55));
                     int var29 = var16 % 7 == 0 ? -6561815 : ((var16 & 1) == 0 ? -2644737 : -931967);
                     float var30 = (float)((0.36 + 0.64 * Math.sin(var21 * Math.PI)) * (0.5 + 0.5 * var10));
                     dot(var1, var27, var28, var16 % 9 == 0 ? 3 : 2, var29, var30);
                     dot(var1, var27 - 4, var28 + 1, 1, -5799207, var30 * 0.38F);
                  }
               }

               double var34 = (var8 - 0.7) / 0.52;
               if (var34 > 0.0 && var34 < 1.0) {
                  for (int var18 = 0; var18 < 18; var18++) {
                     double var37 = var18 * 2 * Math.PI / 18.0 + var8 * 3.0;
                     double var40 = 18.0 + var34 * 35.0;
                     dot(
                        var1,
                        (int)(var14 + Math.cos(var37) * var40),
                        (int)(var15 + Math.sin(var37) * var40),
                        2,
                        (var18 & 1) == 0 ? -931967 : -2644737,
                        (float)((1.0 - var34) * 0.75)
                     );
                  }
               }
            } else {
               int var31 = var2 + 107;
               int var32 = var3 + 129;
               boolean var33 = "UNRAVEL".equals(var6);

               for (int var35 = 0; var35 < 26; var35++) {
                  double var36 = var35 * (Math.PI / 13) + 0.2;
                  double var38 = var33 ? 52.0 + var8 * 30.0 : 60.0 - var8 * 20.0;
                  var38 += Math.sin(var35 * 2.3 + var8 * 12.0) * 5.0;
                  int var41 = (int)(var31 + Math.cos(var36) * var38);
                  int var22 = (int)(var32 + Math.sin(var36) * var38);
                  int var42 = var33 ? ((var35 & 1) == 0 ? -6561815 : -2644737) : ((var35 & 1) == 0 ? -931967 : -2644737);
                  dot(var1, var41, var22, var35 % 6 == 0 ? 3 : 2, var42, (float)(var10 * (0.4 + 0.4 * hash(var35 + 57))));
               }
            }
         }
      }
   }
}
