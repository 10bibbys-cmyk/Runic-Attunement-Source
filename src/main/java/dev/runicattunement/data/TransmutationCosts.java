/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2540
 */
package dev.runicattunement.data;

import dev.runicattunement.data.IngredientCost;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_2540;

public record TransmutationCosts(List<IngredientCost> unitMaterials, List<IngredientCost> curseRetentionCosts) {
    public TransmutationCosts {
        unitMaterials = List.copyOf(unitMaterials);
        curseRetentionCosts = List.copyOf(curseRetentionCosts);
    }

    public boolean available() {
        return !this.unitMaterials.isEmpty();
    }

    public void write(class_2540 buf) {
        TransmutationCosts.writeCosts(buf, this.unitMaterials);
        TransmutationCosts.writeCosts(buf, this.curseRetentionCosts);
    }

    public static TransmutationCosts read(class_2540 buf) {
        return new TransmutationCosts(TransmutationCosts.readCosts(buf), TransmutationCosts.readCosts(buf));
    }

    private static void writeCosts(class_2540 buf, List<IngredientCost> costs) {
        buf.method_10804(costs.size());
        for (IngredientCost cost : costs) {
            cost.write(buf);
        }
    }

    private static List<IngredientCost> readCosts(class_2540 buf) {
        int size = buf.method_10816();
        ArrayList<IngredientCost> costs = new ArrayList<IngredientCost>(size);
        for (int i = 0; i < size; ++i) {
            costs.add(IngredientCost.read(buf));
        }
        return List.copyOf(costs);
    }

    public static TransmutationCosts unavailable() {
        return new TransmutationCosts(List.of(), List.of());
    }
}

