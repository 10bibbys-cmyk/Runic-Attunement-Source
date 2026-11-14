package dev.runicattunement.client;

import dev.runicattunement.item.RunicTabletItem;
import dev.runicattunement.registry.ModScreenHandlers;
import dev.runicattunement.util.RunicPotency;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_3929;
import net.minecraft.class_437;

public final class RunicAttunementClient implements ClientModInitializer {
   public void onInitializeClient() {
      class_3929.method_17542(ModScreenHandlers.RUNIC_INSCRIPTION, RunicInscriptionScreen::new);
      ItemTooltipCallback.EVENT
         .register(
            (ItemTooltipCallback)(stack, context, lines) -> {
               if (stack.method_7909() instanceof RunicTabletItem tablet && RunicTabletItem.isConfiguredTablet(stack) && class_437.method_25442()) {
                  lines.add(class_2561.method_43473());
                  lines.add(class_2561.method_43471("tooltip.runic_attunement.extended.shelf").method_27692(class_124.field_1080));
                  lines.add(
                     class_2561.method_43469("tooltip.runic_attunement.extended.grind", new Object[]{RunicPotency.dustName(tablet.tier())})
                        .method_27692(class_124.field_1080)
                  );
                  lines.add(class_2561.method_43471("tooltip.runic_attunement.extended.reforge").method_27692(class_124.field_1080));
               }
            }
         );
   }
}
