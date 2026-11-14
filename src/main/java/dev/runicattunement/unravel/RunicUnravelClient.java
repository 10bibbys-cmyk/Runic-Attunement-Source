/*
 * Decompiled with CFR 0.152.
 */
package dev.runicattunement.unravel;

import dev.runicattunement.unravel.RunicUnravelRuntime;

public final class RunicUnravelClient {
    private RunicUnravelClient() {
    }

    private static int asInt(Object object, int n) {
        return object instanceof Number ? ((Number)object).intValue() : n;
    }

    private static int rgb(Object object, String string, int n) {
        try {
            return 0xFF000000 | RunicUnravelClient.asInt(RunicUnravelRuntime.invoke(object, string, new Object[0]), n);
        }
        catch (Throwable throwable) {
            return 0xFF000000 | n;
        }
    }

    private static boolean inside(double d, double d2, int n, int n2, int n3, int n4) {
        return d >= (double)n && d < (double)(n + n3) && d2 >= (double)n2 && d2 < (double)(n2 + n4);
    }

    private static Object handler(Object object) {
        return RunicUnravelRuntime.accessField(object, "field_2797");
    }

    private static Object selected(Object object) {
        return RunicUnravelRuntime.accessField(object, "selectedEnchantment");
    }

    private static int left(Object object) {
        return RunicUnravelClient.asInt(RunicUnravelRuntime.accessField(object, "field_2776"), 0);
    }

    private static int top(Object object) {
        return RunicUnravelClient.asInt(RunicUnravelRuntime.accessField(object, "field_2800"), 0);
    }

    private static boolean isAttunement(Object object) {
        return !Boolean.TRUE.equals(RunicUnravelRuntime.invoke(RunicUnravelClient.handler(object), "isTransmutationMode", new Object[0]));
    }

    private static boolean allowedAttune(Object object) {
        Object object2 = RunicUnravelRuntime.invoke(object, "eligibility", new Object[0]);
        return Boolean.TRUE.equals(RunicUnravelRuntime.invoke(object2, "allowed", new Object[0]));
    }

    private static boolean allowedUnravel(Object object) {
        Object object2 = RunicUnravelClient.selected(object);
        if (object2 == null || RunicUnravelClient.handler(object) == null) {
            return false;
        }
        try {
            Object object3 = RunicUnravelRuntime.invoke(RunicUnravelClient.handler(object), "getEquipmentStack", new Object[0]);
            if (object3 == null || Boolean.TRUE.equals(RunicUnravelRuntime.invoke(object3, "method_7960", new Object[0]))) {
                return false;
            }
            int n = RunicUnravelClient.asInt(RunicUnravelRuntime.invoke(RunicUnravelClient.handler(object), "indexOf", object2), -1);
            return n >= 0 && n < 1000;
        }
        catch (Throwable throwable) {
            return false;
        }
    }

    private static int xpCost(Object object) {
        try {
            return RunicUnravelClient.asInt(RunicUnravelRuntime.invoke(object, "currentAttunementExperienceCost", new Object[0]), 0);
        }
        catch (Throwable throwable) {
            return 0;
        }
    }

    private static void fill(Object object, int n, int n2, int n3, int n4, int n5) {
        RunicUnravelRuntime.invoke(object, "method_25294", n, n2, n3, n4, n5);
    }

    private static void frame(Object object, int n, int n2, int n3, int n4, int n5) {
        RunicUnravelRuntime.invoke(object, "method_49601", n, n2, n3, n4, n5);
    }

    private static void text(Object object, Object object2, String string, int n, int n2, int n3, int n4) {
        int n5 = RunicUnravelClient.asInt(RunicUnravelRuntime.invoke(object2, "method_1727", string), string.length() * 6);
        RunicUnravelRuntime.invoke(object, "method_51433", object2, string, n + (n4 - n5) / 2, n2, n3, Boolean.FALSE);
    }

    private static void button(Object object, Object object2, Object object3, int n, int n2, int n3, boolean bl, boolean bl2, String string, String string2) {
        int n4 = RunicUnravelClient.rgb(object3, bl ? (bl2 ? "buttonHover" : "button") : "buttonDisabled", 2698288);
        int n5 = RunicUnravelClient.rgb(object3, bl ? (bl2 ? "accentBright" : "accent") : "border", 5857642);
        int n6 = RunicUnravelClient.rgb(object3, bl ? "title" : "muted", 8357255);
        RunicUnravelClient.fill(object, n, n2, n + n3, n2 + 23, n4);
        RunicUnravelClient.frame(object, n, n2, n3, 23, n5);
        RunicUnravelClient.text(object, object2, string, n, n2 + 2, n6, n3);
        if (string2 != null && !string2.isBlank()) {
            RunicUnravelClient.text(object, object2, string2, n, n2 + 13, n6, n3);
        }
    }

    public static void draw(Object object, Object object2, int n, int n2) {
        try {
            if (!RunicUnravelClient.isAttunement(object)) {
                return;
            }
            int n3 = RunicUnravelClient.left(object) + 104;
            int n4 = RunicUnravelClient.top(object) + 147;
            Object object3 = RunicUnravelRuntime.accessField(object, "theme");
            Object object4 = RunicUnravelRuntime.accessField(object, "field_22793");
            if (object3 == null || object4 == null) {
                return;
            }
            RunicUnravelClient.fill(object2, n3, n4, n3 + 100, n4 + 23, RunicUnravelClient.rgb(object3, "panel", 2369839));
            boolean bl = RunicUnravelClient.allowedUnravel(object);
            boolean bl2 = RunicUnravelClient.allowedAttune(object);
            int n5 = RunicUnravelClient.xpCost(object);
            Object object5 = n5 > 9999 ? "XP" : n5 + " XP";
            RunicUnravelClient.button(object2, object4, object3, n3, n4, 48, bl, RunicUnravelClient.inside(n, n2, n3, n4, 48, 23), "UNRAVEL", "REFUND");
            RunicUnravelClient.button(object2, object4, object3, n3 + 52, n4, 48, bl2, RunicUnravelClient.inside(n, n2, n3 + 52, n4, 48, 23), "ATTUNE", (String)object5);
        }
        catch (Throwable throwable) {
            System.err.println("[Runic Unravel] GUI overlay: " + String.valueOf(throwable));
        }
    }

    public static int buttonId(Object object, double d, double d2, int n) {
        if (n != 0) {
            return -1;
        }
        try {
            int n2;
            if (!RunicUnravelClient.isAttunement(object)) {
                return -1;
            }
            int n3 = RunicUnravelClient.left(object) + 104;
            if (!RunicUnravelClient.inside(d, d2, n3, n2 = RunicUnravelClient.top(object) + 147, 50, 23)) {
                return -1;
            }
            if (d >= (double)(n3 + 48)) {
                return -2;
            }
            Object object2 = RunicUnravelClient.selected(object);
            if (object2 == null) {
                return -2;
            }
            int n4 = RunicUnravelClient.asInt(RunicUnravelRuntime.invoke(RunicUnravelClient.handler(object), "indexOf", object2), -1);
            if (n4 < 0 || n4 >= 1000) {
                return -2;
            }
            return 6000 + n4;
        }
        catch (Throwable throwable) {
            System.err.println("[Runic Unravel] GUI button routing failed: " + String.valueOf(throwable));
            return -2;
        }
    }
}

