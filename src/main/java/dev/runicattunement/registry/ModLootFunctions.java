package dev.runicattunement.registry;

import dev.runicattunement.RunicAttunement;
import dev.runicattunement.loot.GenerateRunicTabletLootFunction;
import net.minecraft.class_2378;
import net.minecraft.class_5339;
import net.minecraft.class_7923;

public final class ModLootFunctions {
   public static final class_5339 GENERATE_RUNIC_TABLET = (class_5339)class_2378.method_10230(
      class_7923.field_41134, RunicAttunement.id("generate_runic_tablet"), new class_5339(new GenerateRunicTabletLootFunction.Serializer())
   );

   private ModLootFunctions() {
   }

   public static void initialize() {
   }
}
