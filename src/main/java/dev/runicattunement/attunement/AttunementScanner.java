package dev.runicattunement.attunement;

import dev.runicattunement.data.RunicDefinitionManager;
import dev.runicattunement.item.RunicTabletItem;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.class_1799;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2331;
import net.minecraft.class_2338;
import net.minecraft.class_2960;
import net.minecraft.class_7716;

public final class AttunementScanner {
   private AttunementScanner() {
   }

   public static List<AttunementScanner.TabletSource> scanTabletSources(class_1937 world, class_2338 tablePos) {
      List<AttunementScanner.TabletSource> result = new ArrayList<>();

      for (class_2338 offset : class_2331.field_36535) {
         class_2338 shelfPos = tablePos.method_10081(offset);
         if (world.method_8320(shelfPos).method_27852(class_2246.field_40276)) {
            class_2338 gapPos = tablePos.method_10069(offset.method_10263() / 2, offset.method_10264(), offset.method_10260() / 2);
            if (world.method_8320(gapPos).method_26215() && world.method_8321(shelfPos) instanceof class_7716 shelf) {
               for (int slot = 0; slot < shelf.method_5439(); slot++) {
                  class_1799 stack = shelf.method_5438(slot);
                  if (RunicTabletItem.isConfiguredTablet(stack) && stack.method_7909() instanceof RunicTabletItem tablet) {
                     result.add(new AttunementScanner.TabletSource(shelfPos.method_10062(), tablet.tier()));
                  }
               }
            }
         }
      }

      return List.copyOf(result);
   }

   public static List<class_2960> scan(class_1937 world, class_2338 tablePos) {
      Set<class_2960> found = new LinkedHashSet<>();

      for (class_2338 offset : class_2331.field_36535) {
         class_2338 shelfPos = tablePos.method_10081(offset);
         if (world.method_8320(shelfPos).method_27852(class_2246.field_40276)) {
            class_2338 gapPos = tablePos.method_10069(offset.method_10263() / 2, offset.method_10264(), offset.method_10260() / 2);
            if (world.method_8320(gapPos).method_26215() && world.method_8321(shelfPos) instanceof class_7716 shelf) {
               for (int slot = 0; slot < shelf.method_5439(); slot++) {
                  class_1799 stack = shelf.method_5438(slot);
                  if (RunicTabletItem.isConfiguredTablet(stack)) {
                     RunicTabletItem.getEnchantmentId(stack).filter(RunicDefinitionManager::isEnabled).ifPresent(found::add);
                  }
               }
            }
         }
      }

      List<class_2960> result = new ArrayList<>(found);
      result.sort(Comparator.comparing(class_2960::toString));
      return List.copyOf(result);
   }

   public record TabletSource(class_2338 pos, int tier) {
   }
}
