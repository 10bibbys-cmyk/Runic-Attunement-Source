/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
 *  net.minecraft.class_1657
 *  net.minecraft.class_1661
 *  net.minecraft.class_1703
 *  net.minecraft.class_2540
 *  net.minecraft.class_2561
 *  net.minecraft.class_3222
 */
package dev.runicattunement.screen;

import dev.runicattunement.screen.RunicInscriptionScreenHandler;
import dev.runicattunement.screen.RunicScreenData;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.class_1657;
import net.minecraft.class_1661;
import net.minecraft.class_1703;
import net.minecraft.class_2540;
import net.minecraft.class_2561;
import net.minecraft.class_3222;

public final class RunicScreenFactory
implements ExtendedScreenHandlerFactory {
    private final RunicScreenData data;

    public RunicScreenFactory(RunicScreenData data) {
        this.data = data;
    }

    public void writeScreenOpeningData(class_3222 player, class_2540 buf) {
        this.data.write(buf);
    }

    public class_2561 method_5476() {
        return class_2561.method_43471((String)"screen.runic_attunement.title");
    }

    public class_1703 createMenu(int syncId, class_1661 playerInventory, class_1657 player) {
        return new RunicInscriptionScreenHandler(syncId, playerInventory, this.data, player.method_37908());
    }
}

