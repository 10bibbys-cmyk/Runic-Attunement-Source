/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
 *  net.minecraft.class_1792
 *  net.minecraft.class_1792$class_1793
 *  net.minecraft.class_1935
 *  net.minecraft.class_2378
 *  net.minecraft.class_2960
 *  net.minecraft.class_5321
 *  net.minecraft.class_7706
 *  net.minecraft.class_7923
 */
package dev.runicattunement.registry;

import dev.runicattunement.RunicAttunement;
import dev.runicattunement.item.RunicDustItem;
import dev.runicattunement.item.RunicTabletItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.class_1792;
import net.minecraft.class_1935;
import net.minecraft.class_2378;
import net.minecraft.class_2960;
import net.minecraft.class_5321;
import net.minecraft.class_7706;
import net.minecraft.class_7923;

public final class ModItems {
    public static final RunicTabletItem RUNIC_TABLET_TIER_1 = ModItems.register("runic_tablet_tier_1", new RunicTabletItem(1, new class_1792.class_1793()));
    public static final RunicTabletItem RUNIC_TABLET_TIER_2 = ModItems.register("runic_tablet_tier_2", new RunicTabletItem(2, new class_1792.class_1793()));
    public static final RunicTabletItem RUNIC_TABLET_TIER_3 = ModItems.register("runic_tablet_tier_3", new RunicTabletItem(3, new class_1792.class_1793()));
    public static final RunicDustItem RUNIC_DUST_TIER_1 = ModItems.register("runic_dust_tier_1", new RunicDustItem(1, new class_1792.class_1793()));
    public static final RunicDustItem RUNIC_DUST_TIER_2 = ModItems.register("runic_dust_tier_2", new RunicDustItem(2, new class_1792.class_1793()));
    public static final RunicDustItem RUNIC_DUST_TIER_3 = ModItems.register("runic_dust_tier_3", new RunicDustItem(3, new class_1792.class_1793()));

    private ModItems() {
    }

    private static <T extends class_1792> T register(String path, T item) {
        return (T)((class_1792)class_2378.method_10230((class_2378)class_7923.field_41178, (class_2960)RunicAttunement.id(path), item));
    }

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent((class_5321)class_7706.field_41062).register(entries -> {
            entries.method_45421((class_1935)RUNIC_TABLET_TIER_1);
            entries.method_45421((class_1935)RUNIC_TABLET_TIER_2);
            entries.method_45421((class_1935)RUNIC_TABLET_TIER_3);
            entries.method_45421((class_1935)RUNIC_DUST_TIER_1);
            entries.method_45421((class_1935)RUNIC_DUST_TIER_2);
            entries.method_45421((class_1935)RUNIC_DUST_TIER_3);
        });
    }

    public static RunicTabletItem byTier(int tier) {
        return switch (tier) {
            case 1 -> RUNIC_TABLET_TIER_1;
            case 2 -> RUNIC_TABLET_TIER_2;
            case 3 -> RUNIC_TABLET_TIER_3;
            default -> throw new IllegalArgumentException("Tablet tier must be 1-3");
        };
    }

    public static RunicDustItem dustByTier(int tier) {
        return switch (tier) {
            case 1 -> RUNIC_DUST_TIER_1;
            case 2 -> RUNIC_DUST_TIER_2;
            case 3 -> RUNIC_DUST_TIER_3;
            default -> throw new IllegalArgumentException("Dust tier must be 1-3");
        };
    }
}

