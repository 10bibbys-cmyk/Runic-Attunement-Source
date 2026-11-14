/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2540
 *  net.minecraft.class_2960
 */
package dev.runicattunement.data;

import dev.runicattunement.data.CostKind;
import net.minecraft.class_2540;
import net.minecraft.class_2960;

public record IngredientCost(class_2960 itemId, int count, CostKind kind) {
    public IngredientCost {
        if (count <= 0) {
            throw new IllegalArgumentException("Ingredient count must be positive");
        }
    }

    public void write(class_2540 buf) {
        buf.method_10812(this.itemId);
        buf.method_10804(this.count);
        buf.method_10817((Enum)this.kind);
    }

    public static IngredientCost read(class_2540 buf) {
        return new IngredientCost(buf.method_10810(), buf.method_10816(), (CostKind)buf.method_10818(CostKind.class));
    }
}

