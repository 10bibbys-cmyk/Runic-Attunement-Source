/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_1937
 *  net.minecraft.class_2246
 *  net.minecraft.class_2331
 *  net.minecraft.class_2338
 *  net.minecraft.class_2382
 *  net.minecraft.class_2586
 *  net.minecraft.class_2960
 *  net.minecraft.class_7716
 */
package dev.runicattunement.attunement;

import dev.runicattunement.data.RunicDefinitionManager;
import dev.runicattunement.item.RunicTabletItem;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2331;
import net.minecraft.class_2338;
import net.minecraft.class_2382;
import net.minecraft.class_2586;
import net.minecraft.class_2960;
import net.minecraft.class_7716;

public final class AttunementScanner {
    private AttunementScanner() {
    }

    public static List<TabletSource> scanTabletSources(class_1937 world, class_2338 tablePos) {
        ArrayList<TabletSource> result = new ArrayList<TabletSource>();
        for (class_2338 offset : class_2331.field_36535) {
            class_2586 blockEntity;
            class_2338 gapPos;
            class_2338 shelfPos = tablePos.method_10081((class_2382)offset);
            if (!world.method_8320(shelfPos).method_27852(class_2246.field_40276) || !world.method_8320(gapPos = tablePos.method_10069(offset.method_10263() / 2, offset.method_10264(), offset.method_10260() / 2)).method_26215() || !((blockEntity = world.method_8321(shelfPos)) instanceof class_7716)) continue;
            class_7716 shelf = (class_7716)blockEntity;
            for (int slot = 0; slot < shelf.method_5439(); ++slot) {
                class_1792 class_17922;
                class_1799 stack = shelf.method_5438(slot);
                if (!RunicTabletItem.isConfiguredTablet(stack) || !((class_17922 = stack.method_7909()) instanceof RunicTabletItem)) continue;
                RunicTabletItem tablet = (RunicTabletItem)class_17922;
                result.add(new TabletSource(shelfPos.method_10062(), tablet.tier()));
            }
        }
        return List.copyOf(result);
    }

    public static List<class_2960> scan(class_1937 world, class_2338 tablePos) {
        LinkedHashSet found = new LinkedHashSet();
        for (class_2338 offset : class_2331.field_36535) {
            class_2586 blockEntity;
            class_2338 gapPos;
            class_2338 shelfPos = tablePos.method_10081((class_2382)offset);
            if (!world.method_8320(shelfPos).method_27852(class_2246.field_40276) || !world.method_8320(gapPos = tablePos.method_10069(offset.method_10263() / 2, offset.method_10264(), offset.method_10260() / 2)).method_26215() || !((blockEntity = world.method_8321(shelfPos)) instanceof class_7716)) continue;
            class_7716 shelf = (class_7716)blockEntity;
            for (int slot = 0; slot < shelf.method_5439(); ++slot) {
                class_1799 stack = shelf.method_5438(slot);
                if (!RunicTabletItem.isConfiguredTablet(stack)) continue;
                RunicTabletItem.getEnchantmentId(stack).filter(RunicDefinitionManager::isEnabled).ifPresent(found::add);
            }
        }
        ArrayList<class_2960> result = new ArrayList<class_2960>(found);
        result.sort(Comparator.comparing(class_2960::toString));
        return List.copyOf(result);
    }

    public record TabletSource(class_2338 pos, int tier) {
    }
}

