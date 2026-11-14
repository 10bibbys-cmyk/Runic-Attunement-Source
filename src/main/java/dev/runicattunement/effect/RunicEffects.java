/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1937
 *  net.minecraft.class_2338
 *  net.minecraft.class_2390
 *  net.minecraft.class_2394
 *  net.minecraft.class_2398
 *  net.minecraft.class_3218
 *  net.minecraft.class_3222
 *  net.minecraft.class_3417
 *  net.minecraft.class_3419
 *  org.joml.Vector3f
 */
package dev.runicattunement.effect;

import dev.runicattunement.attunement.AttunementScanner;
import dev.runicattunement.util.RunicPotency;
import java.util.LinkedHashSet;
import java.util.List;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_2390;
import net.minecraft.class_2394;
import net.minecraft.class_2398;
import net.minecraft.class_3218;
import net.minecraft.class_3222;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import org.joml.Vector3f;

public final class RunicEffects {
    private RunicEffects() {
    }

    public static void attunementSuccess(class_3222 player, class_2338 tablePos, int resultLevel, int maxLevel, boolean maximum) {
        class_3218 world = player.method_51469();
        float progress = maxLevel <= 1 ? 1.0f : Math.min(1.0f, Math.max(0.0f, ((float)resultLevel - 1.0f) / ((float)maxLevel - 1.0f)));
        float pitch = 0.84f + 0.28f * progress;
        world.method_8396(null, tablePos, class_3417.field_15119, class_3419.field_15245, 0.9f, pitch);
        if (maximum) {
            world.method_8396(null, tablePos, class_3417.field_26980, class_3419.field_15245, 0.65f, 1.18f);
        }
        List<AttunementScanner.TabletSource> sources = AttunementScanner.scanTabletSources((class_1937)world, tablePos);
        Vector3f colour = RunicEffects.weightedColour(sources);
        class_2390 dust = new class_2390(colour, maximum ? 1.15f : 0.9f);
        double tx = (double)tablePos.method_10263() + 0.5;
        double ty = (double)tablePos.method_10264() + 1.25;
        double tz = (double)tablePos.method_10260() + 0.5;
        LinkedHashSet<class_2338> visualSources = new LinkedHashSet<class_2338>();
        for (AttunementScanner.TabletSource source : sources) {
            visualSources.add(source.pos());
        }
        for (class_2338 sourcePos : visualSources) {
            double sx = (double)sourcePos.method_10263() + 0.5;
            double sy = (double)sourcePos.method_10264() + 0.65;
            double sz = (double)sourcePos.method_10260() + 0.5;
            double mx = (sx + tx) * 0.5;
            double my = (sy + ty) * 0.5;
            double mz = (sz + tz) * 0.5;
            world.method_14199((class_2394)class_2398.field_11215, mx, my, mz, 2, 0.16, 0.1, 0.16, 0.02);
            world.method_14199((class_2394)dust, mx, my, mz, 1, 0.05, 0.05, 0.05, 0.0);
        }
        world.method_14199((class_2394)class_2398.field_11215, tx, ty, tz, maximum ? 24 : 14, 0.55, 0.35, 0.55, 0.08);
        world.method_14199((class_2394)dust, tx, ty, tz, maximum ? 20 : 10, 0.38, 0.24, 0.38, maximum ? 0.05 : 0.025);
    }

    public static void transmutationSuccess(class_3222 player, class_2338 tablePos) {
        class_3218 world = player.method_51469();
        world.method_8396(null, tablePos, class_3417.field_14858, class_3419.field_15245, 0.72f, 1.02f);
        double x = (double)tablePos.method_10263() + 0.5;
        double y = (double)tablePos.method_10264() + 1.18;
        double z = (double)tablePos.method_10260() + 0.5;
        class_2390 purple = new class_2390(new Vector3f(0.62f, 0.3f, 0.86f), 0.95f);
        world.method_14199((class_2394)class_2398.field_11215, x, y, z, 18, 0.55, 0.32, 0.55, 0.08);
        world.method_14199((class_2394)class_2398.field_23190, x, y, z, 24, 0.34, 0.22, 0.34, 0.08);
        world.method_14199((class_2394)purple, x, y, z, 16, 0.32, 0.2, 0.32, 0.04);
    }

    private static Vector3f weightedColour(List<AttunementScanner.TabletSource> sources) {
        if (sources.isEmpty()) {
            return RunicEffects.rgb(RunicPotency.rgb(2));
        }
        float r = 0.0f;
        float g = 0.0f;
        float b = 0.0f;
        float total = 0.0f;
        for (AttunementScanner.TabletSource source : sources) {
            float weight = source.tier();
            Vector3f c = RunicEffects.rgb(RunicPotency.rgb(source.tier()));
            r += c.x * weight;
            g += c.y * weight;
            b += c.z * weight;
            total += weight;
        }
        return total <= 0.0f ? RunicEffects.rgb(RunicPotency.rgb(2)) : new Vector3f(r / total, g / total, b / total);
    }

    private static Vector3f rgb(int rgb) {
        return new Vector3f((float)(rgb >> 16 & 0xFF) / 255.0f, (float)(rgb >> 8 & 0xFF) / 255.0f, (float)(rgb & 0xFF) / 255.0f);
    }

    public static void unravelSuccess(class_3222 class_32222) {
        class_3218 class_32182 = class_32222.method_51469();
        class_2338 class_23382 = class_32222.method_24515();
        class_32182.method_8396(null, class_23382, class_3417.field_15119, class_3419.field_15245, 0.62f, 0.58f);
        class_32182.method_8396(null, class_23382, class_3417.field_26980, class_3419.field_15245, 0.58f, 0.74f);
    }
}

