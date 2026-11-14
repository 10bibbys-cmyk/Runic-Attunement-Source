/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  net.minecraft.class_195
 *  net.minecraft.class_2960
 *  net.minecraft.class_3222
 *  net.minecraft.class_4558
 *  net.minecraft.class_5257
 *  net.minecraft.class_5258
 */
package dev.runicattunement.advancement;

import com.google.gson.JsonObject;
import net.minecraft.class_195;
import net.minecraft.class_2960;
import net.minecraft.class_3222;
import net.minecraft.class_4558;
import net.minecraft.class_5257;
import net.minecraft.class_5258;

public final class RunicCriterion
extends class_4558<Conditions> {
    private final class_2960 id;

    public RunicCriterion(class_2960 id) {
        this.id = id;
    }

    public class_2960 method_794() {
        return this.id;
    }

    protected Conditions conditionsFromJson(JsonObject obj, class_5258 playerPredicate, class_5257 predicateDeserializer) {
        return new Conditions(this.id, playerPredicate);
    }

    public void trigger(class_3222 player) {
        this.method_22510(player, conditions -> true);
    }

    public static final class Conditions
    extends class_195 {
        public Conditions(class_2960 id, class_5258 playerPredicate) {
            super(id, playerPredicate);
        }
    }
}

