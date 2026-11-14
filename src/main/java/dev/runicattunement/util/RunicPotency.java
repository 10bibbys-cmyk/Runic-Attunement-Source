/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_124
 *  net.minecraft.class_2561
 */
package dev.runicattunement.util;

import net.minecraft.class_124;
import net.minecraft.class_2561;

public final class RunicPotency {
    private RunicPotency() {
    }

    public static String key(int tier) {
        return switch (tier) {
            case 1 -> "latent";
            case 2 -> "resonant";
            case 3 -> "pristine";
            default -> "unknown";
        };
    }

    public static class_2561 name(int tier) {
        return class_2561.method_43471((String)("potency.runic_attunement." + RunicPotency.key(tier)));
    }

    public static class_2561 tabletName(int tier) {
        return class_2561.method_43471((String)("item.runic_attunement.runic_tablet_tier_" + tier));
    }

    public static class_2561 dustName(int tier) {
        return class_2561.method_43471((String)("item.runic_attunement.runic_dust_tier_" + tier));
    }

    public static class_124 formatting(int tier) {
        return switch (tier) {
            case 1 -> class_124.field_1060;
            case 2 -> class_124.field_1065;
            case 3 -> class_124.field_1076;
            default -> class_124.field_1080;
        };
    }

    public static int rgb(int tier) {
        return switch (tier) {
            case 1 -> 6473325;
            case 2 -> 14129992;
            case 3 -> 12088296;
            default -> 0xA8A8A8;
        };
    }
}

