/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_332
 */
package dev.runicattunement.grimoire;

import net.minecraft.class_332;

public final class RunicGuiSparkles {
    private static final int GOLD = -931967;
    private static final int VIOLET = -2644737;
    private static final int LAVENDER = -5799207;
    private static final int CYAN = -6561815;

    private RunicGuiSparkles() {
    }

    private static void dot(class_332 class_3322, int n, int n2, int n3, int n4, float f) {
        int n5 = Math.min(255, Math.max(0, (int)(255.0f * f)));
        if (n5 <= 0) {
            return;
        }
        class_3322.method_25294(n, n2, n + n3, n2 + n3, n5 << 24 | n4 & 0xFFFFFF);
    }

    private static double fract(double d) {
        return d - Math.floor(d);
    }

    private static double hash(int n) {
        return RunicGuiSparkles.fract(Math.sin((double)n * 78.233 + 9.783) * 43758.5453);
    }

    private static double ease(double d) {
        d = Math.min(1.0, Math.max(0.0, d));
        return d * d * (3.0 - 2.0 * d);
    }

    public static void draw(Object object, class_332 class_3322, int n, int n2, long l, String string, boolean bl) {
        block6: {
            double d;
            double d2;
            block5: {
                double d3;
                double d4;
                boolean bl2;
                if (l <= 0L || string == null) {
                    return;
                }
                d2 = (double)(System.nanoTime() - l) * 1.0E-9;
                if (d2 < 0.0 || d2 > 1.38) {
                    return;
                }
                d = Math.max(0.0, 1.0 - d2 / 1.38);
                boolean bl3 = bl2 = bl && "TRANSMUTE".equals(string);
                if (!bl2) break block5;
                int n3 = n + 107;
                int n4 = n + 533;
                int n5 = n2 + 129;
                for (int i = 0; i < 45; ++i) {
                    double d5 = (double)i * 0.012;
                    d4 = (d2 - d5) / 1.05;
                    if (d4 < 0.0 || d4 > 1.0) continue;
                    d3 = RunicGuiSparkles.ease(d4);
                    double d6 = (RunicGuiSparkles.hash(i + 11) - 0.5) * 27.0;
                    double d7 = -32.0 * Math.sin(d3 * Math.PI);
                    int n6 = (int)Math.round((double)n3 + (double)(n4 - n3) * d3 + (RunicGuiSparkles.hash(i + 29) - 0.5) * 9.0);
                    int n7 = (int)Math.round((double)n5 + d7 + d6 * (0.45 + Math.sin(d3 * Math.PI) * 0.55));
                    int n8 = i % 7 == 0 ? -6561815 : ((i & 1) == 0 ? -2644737 : -931967);
                    float f = (float)((0.36 + 0.64 * Math.sin(d3 * Math.PI)) * (0.5 + 0.5 * d));
                    RunicGuiSparkles.dot(class_3322, n6, n7, i % 9 == 0 ? 3 : 2, n8, f);
                    RunicGuiSparkles.dot(class_3322, n6 - 4, n7 + 1, 1, -5799207, f * 0.38f);
                }
                double d8 = (d2 - 0.7) / 0.52;
                if (!(d8 > 0.0) || !(d8 < 1.0)) break block6;
                for (int i = 0; i < 18; ++i) {
                    d4 = (double)(i * 2) * Math.PI / 18.0 + d2 * 3.0;
                    d3 = 18.0 + d8 * 35.0;
                    RunicGuiSparkles.dot(class_3322, (int)((double)n4 + Math.cos(d4) * d3), (int)((double)n5 + Math.sin(d4) * d3), 2, (i & 1) == 0 ? -931967 : -2644737, (float)((1.0 - d8) * 0.75));
                }
                break block6;
            }
            int n9 = n + 107;
            int n10 = n2 + 129;
            boolean bl4 = "UNRAVEL".equals(string);
            for (int i = 0; i < 26; ++i) {
                double d9 = (double)i * 0.241660973353061 + 0.2;
                double d10 = bl4 ? 52.0 + d2 * 30.0 : 60.0 - d2 * 20.0;
                int n11 = (int)((double)n9 + Math.cos(d9) * (d10 += Math.sin((double)i * 2.3 + d2 * 12.0) * 5.0));
                int n12 = (int)((double)n10 + Math.sin(d9) * d10);
                int n13 = bl4 ? ((i & 1) == 0 ? -6561815 : -2644737) : ((i & 1) == 0 ? -931967 : -2644737);
                RunicGuiSparkles.dot(class_3322, n11, n12, i % 6 == 0 ? 3 : 2, n13, (float)(d * (0.4 + 0.4 * RunicGuiSparkles.hash(i + 57))));
            }
        }
    }
}

