package dev.runicattunement.advancement;

import dev.runicattunement.RunicAttunement;
import net.minecraft.class_174;
import net.minecraft.class_3222;

public final class RunicAdvancements {
   public static final RunicCriterion USE_TABLE = register("use_table");
   public static final RunicCriterion SHELVE_TABLET = register("shelve_tablet");
   public static final RunicCriterion FIRST_ATTUNEMENT = register("first_attunement");
   public static final RunicCriterion MAX_ATTUNEMENT = register("max_attunement");
   public static final RunicCriterion FIRST_TRANSMUTATION = register("first_transmutation");
   public static final RunicCriterion GRIND_TABLET = register("grind_tablet");
   public static final RunicCriterion REFORGE_TABLET = register("reforge_tablet");
   public static final RunicCriterion UPGRADE_TABLET = register("upgrade_tablet");

   private RunicAdvancements() {
   }

   private static RunicCriterion register(String path) {
      return (RunicCriterion)class_174.method_767(new RunicCriterion(RunicAttunement.id(path)));
   }

   public static void initialize() {
   }

   public static void triggerUseTable(class_3222 player) {
      USE_TABLE.trigger(player);
   }

   public static void triggerShelveTablet(class_3222 player) {
      SHELVE_TABLET.trigger(player);
   }

   public static void triggerAttunement(class_3222 player, boolean maximum) {
      FIRST_ATTUNEMENT.trigger(player);
      if (maximum) {
         MAX_ATTUNEMENT.trigger(player);
      }
   }

   public static void triggerTransmutation(class_3222 player) {
      FIRST_TRANSMUTATION.trigger(player);
   }

   public static void triggerGrind(class_3222 player) {
      GRIND_TABLET.trigger(player);
   }

   public static void triggerReforge(class_3222 player, boolean upgraded) {
      REFORGE_TABLET.trigger(player);
      if (upgraded) {
         UPGRADE_TABLET.trigger(player);
      }
   }
}
