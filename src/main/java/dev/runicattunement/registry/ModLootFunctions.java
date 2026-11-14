/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2378
 *  net.minecraft.class_2960
 *  net.minecraft.class_5335
 *  net.minecraft.class_5339
 *  net.minecraft.class_7923
 */
package dev.runicattunement.registry;

import dev.runicattunement.RunicAttunement;
import dev.runicattunement.loot.GenerateRunicTabletLootFunction;
import net.minecraft.class_2378;
import net.minecraft.class_2960;
import net.minecraft.class_5335;
import net.minecraft.class_5339;
import net.minecraft.class_7923;

public final class ModLootFunctions {
    public static final class_5339 GENERATE_RUNIC_TABLET = (class_5339)class_2378.method_10230((class_2378)class_7923.field_41134, (class_2960)RunicAttunement.id("generate_runic_tablet"), (Object)new class_5339((class_5335)new GenerateRunicTabletLootFunction.Serializer()));

    private ModLootFunctions() {
    }

    public static void initialize() {
    }
}

