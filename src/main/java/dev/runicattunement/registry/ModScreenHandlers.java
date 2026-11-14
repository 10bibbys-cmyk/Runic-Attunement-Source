/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType
 *  net.minecraft.class_2378
 *  net.minecraft.class_2960
 *  net.minecraft.class_3917
 *  net.minecraft.class_7923
 */
package dev.runicattunement.registry;

import dev.runicattunement.RunicAttunement;
import dev.runicattunement.screen.RunicInscriptionScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.class_2378;
import net.minecraft.class_2960;
import net.minecraft.class_3917;
import net.minecraft.class_7923;

public final class ModScreenHandlers {
    public static final class_3917<RunicInscriptionScreenHandler> RUNIC_INSCRIPTION = (class_3917)class_2378.method_10230((class_2378)class_7923.field_41187, (class_2960)RunicAttunement.id("runic_inscription"), (Object)new ExtendedScreenHandlerType(RunicInscriptionScreenHandler::new));

    private ModScreenHandlers() {
    }

    public static void initialize() {
    }
}

