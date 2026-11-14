/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_124
 *  net.minecraft.class_1792
 *  net.minecraft.class_1792$class_1793
 *  net.minecraft.class_1799
 *  net.minecraft.class_1836
 *  net.minecraft.class_1937
 *  net.minecraft.class_2561
 *  org.jetbrains.annotations.Nullable
 */
package dev.runicattunement.item;

import dev.runicattunement.util.RunicPotency;
import java.util.List;
import net.minecraft.class_124;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1836;
import net.minecraft.class_1937;
import net.minecraft.class_2561;
import org.jetbrains.annotations.Nullable;

public final class RunicDustItem
extends class_1792 {
    private final int tier;

    public RunicDustItem(int tier, class_1792.class_1793 settings) {
        super(settings);
        if (tier < 1 || tier > 3) {
            throw new IllegalArgumentException("Runic Dust tier must be 1-3");
        }
        this.tier = tier;
    }

    public int tier() {
        return this.tier;
    }

    public boolean method_7886(class_1799 stack) {
        return true;
    }

    public void method_7851(class_1799 stack, @Nullable class_1937 world, List<class_2561> tooltip, class_1836 context) {
        tooltip.add((class_2561)class_2561.method_43469((String)"tooltip.runic_attunement.runic_dust", (Object[])new Object[]{RunicPotency.name(this.tier)}).method_27692(this.tierFormatting()));
    }

    private class_124 tierFormatting() {
        return RunicPotency.formatting(this.tier);
    }
}

