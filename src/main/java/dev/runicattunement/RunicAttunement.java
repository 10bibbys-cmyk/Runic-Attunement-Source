/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.ModInitializer
 *  net.fabricmc.fabric.api.event.player.UseBlockCallback
 *  net.minecraft.class_1269
 *  net.minecraft.class_2246
 *  net.minecraft.class_2960
 *  net.minecraft.class_3222
 *  net.minecraft.class_3908
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package dev.runicattunement;

import dev.runicattunement.advancement.RunicAdvancements;
import dev.runicattunement.attunement.AttunementScanner;
import dev.runicattunement.command.RunicCommands;
import dev.runicattunement.compat.RunicCompatibility;
import dev.runicattunement.config.RunicConfig;
import dev.runicattunement.data.RunicDefinitionManager;
import dev.runicattunement.loot.RunicLootInjector;
import dev.runicattunement.registry.ModItems;
import dev.runicattunement.registry.ModLootFunctions;
import dev.runicattunement.registry.ModScreenHandlers;
import dev.runicattunement.screen.RunicScreenData;
import dev.runicattunement.screen.RunicScreenFactory;
import java.util.List;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.class_1269;
import net.minecraft.class_2246;
import net.minecraft.class_2960;
import net.minecraft.class_3222;
import net.minecraft.class_3908;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RunicAttunement
implements ModInitializer {
    public static final String MOD_ID = "runic_attunement";
    public static final Logger LOGGER = LoggerFactory.getLogger((String)"Runic Attunement");

    public static class_2960 id(String path) {
        return new class_2960(MOD_ID, path);
    }

    public void onInitialize() {
        ModItems.initialize();
        RunicAdvancements.initialize();
        RunicCommands.initialize();
        ModLootFunctions.initialize();
        ModScreenHandlers.initialize();
        RunicConfig.initialize();
        RunicLootInjector.initialize();
        RunicDefinitionManager.registerReloadListener();
        RunicCompatibility.initialize();
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (!world.method_8320(hitResult.method_17777()).method_27852(class_2246.field_10485)) {
                return class_1269.field_5811;
            }
            if (player.method_7325()) {
                return class_1269.field_5811;
            }
            if (world.field_9236) {
                return class_1269.field_5812;
            }
            if (player instanceof class_3222) {
                class_3222 serverPlayer = (class_3222)player;
                RunicAdvancements.triggerUseTable(serverPlayer);
                if (!AttunementScanner.scanTabletSources(world, hitResult.method_17777()).isEmpty()) {
                    RunicAdvancements.triggerShelveTablet(serverPlayer);
                }
                List<class_2960> attuned = AttunementScanner.scan(world, hitResult.method_17777());
                RunicScreenData data = new RunicScreenData(hitResult.method_17777().method_10062(), attuned, RunicDefinitionManager.snapshot(), RunicDefinitionManager.universalSnapshot(), RunicDefinitionManager.transmutationCostsSnapshot(), RunicConfig.experienceCosts(), RunicConfig.transmutation());
                serverPlayer.method_17355((class_3908)new RunicScreenFactory(data));
            }
            return class_1269.field_5812;
        });
        LOGGER.info("Runic Attunement V1.3.8 initialised");
    }
}

