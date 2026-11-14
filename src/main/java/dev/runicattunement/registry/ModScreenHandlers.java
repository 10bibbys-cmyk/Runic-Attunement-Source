package dev.runicattunement.registry;

import dev.runicattunement.RunicAttunement;
import dev.runicattunement.screen.RunicInscriptionScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.class_2378;
import net.minecraft.class_3917;
import net.minecraft.class_7923;

public final class ModScreenHandlers {
   public static final class_3917<RunicInscriptionScreenHandler> RUNIC_INSCRIPTION = (class_3917<RunicInscriptionScreenHandler>)class_2378.method_10230(
      class_7923.field_41187, RunicAttunement.id("runic_inscription"), new ExtendedScreenHandlerType(RunicInscriptionScreenHandler::new)
   );

   private ModScreenHandlers() {
   }

   public static void initialize() {
   }
}
