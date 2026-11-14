package dev.runicattunement.effect;

import dev.runicattunement.attunement.AttunementScanner;
import dev.runicattunement.util.RunicPotency;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.class_2338;
import net.minecraft.class_2390;
import net.minecraft.class_2398;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import org.joml.Vector3f;

public final class RunicEffects {
   private RunicEffects() {
   }

   public static void attunementSuccess(class_3222 player, class_2338 tablePos, int resultLevel, int maxLevel, boolean maximum) {
      class_3218 world = player.method_51469();
      float progress = maxLevel <= 1 ? 1.0F : Math.min(1.0F, Math.max(0.0F, (resultLevel - 1.0F) / (maxLevel - 1.0F)));
      float pitch = 0.84F + 0.28F * progress;
      world.method_8396(null, tablePos, class_3417.field_15119, class_3419.field_15245, 0.9F, pitch);
      if (maximum) {
         world.method_8396(null, tablePos, class_3417.field_26980, class_3419.field_15245, 0.65F, 1.18F);
      }

      List<AttunementScanner.TabletSource> sources = AttunementScanner.scanTabletSources(world, tablePos);
      Vector3f colour = weightedColour(sources);
      class_2390 dust = new class_2390(colour, maximum ? 1.15F : 0.9F);
      double tx = tablePos.method_10263() + 0.5;
      double ty = tablePos.method_10264() + 1.25;
      double tz = tablePos.method_10260() + 0.5;
      Set<class_2338> visualSources = new LinkedHashSet<>();

      for (AttunementScanner.TabletSource source : sources) {
         visualSources.add(source.pos());
      }

      for (class_2338 sourcePos : visualSources) {
         double sx = sourcePos.method_10263() + 0.5;
         double sy = sourcePos.method_10264() + 0.65;
         double sz = sourcePos.method_10260() + 0.5;
         double mx = (sx + tx) * 0.5;
         double my = (sy + ty) * 0.5;
         double mz = (sz + tz) * 0.5;
         world.method_14199(class_2398.field_11215, mx, my, mz, 2, 0.16, 0.1, 0.16, 0.02);
         world.method_14199(dust, mx, my, mz, 1, 0.05, 0.05, 0.05, 0.0);
      }

      world.method_14199(class_2398.field_11215, tx, ty, tz, maximum ? 24 : 14, 0.55, 0.35, 0.55, 0.08);
      world.method_14199(dust, tx, ty, tz, maximum ? 20 : 10, 0.38, 0.24, 0.38, maximum ? 0.05 : 0.025);
   }

   public static void transmutationSuccess(class_3222 player, class_2338 tablePos) {
      class_3218 world = player.method_51469();
      world.method_8396(null, tablePos, class_3417.field_14858, class_3419.field_15245, 0.72F, 1.02F);
      double x = tablePos.method_10263() + 0.5;
      double y = tablePos.method_10264() + 1.18;
      double z = tablePos.method_10260() + 0.5;
      class_2390 purple = new class_2390(new Vector3f(0.62F, 0.3F, 0.86F), 0.95F);
      world.method_14199(class_2398.field_11215, x, y, z, 18, 0.55, 0.32, 0.55, 0.08);
      world.method_14199(class_2398.field_23190, x, y, z, 24, 0.34, 0.22, 0.34, 0.08);
      world.method_14199(purple, x, y, z, 16, 0.32, 0.2, 0.32, 0.04);
   }

   private static Vector3f weightedColour(List<AttunementScanner.TabletSource> sources) {
      if (sources.isEmpty()) {
         return rgb(RunicPotency.rgb(2));
      }

      float r = 0.0F;
      float g = 0.0F;
      float b = 0.0F;
      float total = 0.0F;

      for (AttunementScanner.TabletSource source : sources) {
         float weight = source.tier();
         Vector3f c = rgb(RunicPotency.rgb(source.tier()));
         r += c.x * weight;
         g += c.y * weight;
         b += c.z * weight;
         total += weight;
      }

      return total <= 0.0F ? rgb(RunicPotency.rgb(2)) : new Vector3f(r / total, g / total, b / total);
   }

   private static Vector3f rgb(int rgb) {
      return new Vector3f((rgb >> 16 & 0xFF) / 255.0F, (rgb >> 8 & 0xFF) / 255.0F, (rgb & 0xFF) / 255.0F);
   }

   public static void unravelSuccess(class_3222 var0) {
      class_3218 var1 = var0.method_51469();
      class_2338 var2 = var0.method_24515();
      var1.method_8396(null, var2, class_3417.field_15119, class_3419.field_15245, 0.62F, 0.58F);
      var1.method_8396(null, var2, class_3417.field_26980, class_3419.field_15245, 0.58F, 0.74F);
   }
}
