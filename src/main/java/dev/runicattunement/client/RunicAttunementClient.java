/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.ClientModInitializer
 *  net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback
 *  net.minecraft.class_124
 *  net.minecraft.class_1792
 *  net.minecraft.class_2561
 *  net.minecraft.class_3929
 *  net.minecraft.class_437
 */
package dev.runicattunement.client;

import dev.runicattunement.client.RunicInscriptionScreen;
import dev.runicattunement.item.RunicTabletItem;
import dev.runicattunement.registry.ModScreenHandlers;
import dev.runicattunement.util.RunicPotency;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.class_124;
import net.minecraft.class_1792;
import net.minecraft.class_2561;
import net.minecraft.class_3929;
import net.minecraft.class_437;

public final class RunicAttunementClient
implements ClientModInitializer {
    public void onInitializeClient() {
        class_3929.method_17542(ModScreenHandlers.RUNIC_INSCRIPTION, RunicInscriptionScreen::new);
        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            RunicTabletItem tablet;
            block3: {
                block2: {
                    class_1792 patt813$temp = stack.method_7909();
                    if (!(patt813$temp instanceof RunicTabletItem)) break block2;
                    tablet = (RunicTabletItem)patt813$temp;
                    if (RunicTabletItem.isConfiguredTablet(stack) && class_437.method_25442()) break block3;
                }
                return;
            }
            lines.add(class_2561.method_43473());
            lines.add(class_2561.method_43471((String)"tooltip.runic_attunement.extended.shelf").method_27692(class_124.field_1080));
            lines.add(class_2561.method_43469((String)"tooltip.runic_attunement.extended.grind", (Object[])new Object[]{RunicPotency.dustName(tablet.tier())}).method_27692(class_124.field_1080));
            lines.add(class_2561.method_43471((String)"tooltip.runic_attunement.extended.reforge").method_27692(class_124.field_1080));
        });
    }
}

