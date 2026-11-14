/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1109
 *  net.minecraft.class_1113
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_1935
 *  net.minecraft.class_2561
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_327
 *  net.minecraft.class_332
 *  net.minecraft.class_3414
 *  net.minecraft.class_7923
 */
package dev.runicattunement.grimoire;

import dev.runicattunement.grimoire.RunicGrimoireUi;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;
import net.minecraft.class_1109;
import net.minecraft.class_1113;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1935;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_3414;
import net.minecraft.class_7923;

public final class RunicRefinedClassicUi {
    private static final Map<Object, State> STATE = Collections.synchronizedMap(new WeakHashMap());
    private static final Map<String, Method> METHODS = new HashMap<String, Method>();
    private static final class_2960 BG = new class_2960("runic_attunement", "textures/gui/classic_refined_bg.png");
    private static final class_2960 BG_TRANS = new class_2960("runic_attunement", "textures/gui/classic_refined_transmutation_bg.png");
    private static volatile Object[] runeSigils;
    private static final class_2960 RUNE_OFF;
    private static final class_2960 RUNE_ON;
    private static final int WHITE = -2565411;
    private static final int GOLD = -3563153;
    private static final int GRAY = -7695202;
    private static final int VIOLET = -3892498;
    private static final int MUTED = -8945783;
    private static final int RED = -2912890;
    private static final int GREEN = -7748193;
    private static final int INK = -15591652;
    private static final int HOVER = -12300711;
    private static final int DIM = -14341327;
    private static final int SELECT = -11649438;
    private static final int ART_X = -16;
    private static final int ART_Y = -12;
    private static final int RUNE_Y = 153;
    private static final int LEFT_CX = 124;
    private static final int RIGHT_CX = 547;
    private static final int INV_X = 255;
    private static final int INV_Y = 270;
    private static final int BOOK_X = 451;
    private static final int BOOK_Y = 149;
    private static final int ROW_H = 17;
    private static final int ROWS = 11;

    private RunicRefinedClassicUi() {
    }

    private static State st(Object object2) {
        return STATE.computeIfAbsent(object2, object -> new State());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static Method lookup(String string, int n) {
        String string2 = string + "/" + n;
        Map<String, Method> map = METHODS;
        synchronized (map) {
            Method method = METHODS.get(string2);
            if (method != null) {
                return method;
            }
            for (Method method2 : RunicGrimoireUi.class.getDeclaredMethods()) {
                if (!method2.getName().equals(string) || method2.getParameterCount() != n) continue;
                method2.setAccessible(true);
                METHODS.put(string2, method2);
                return method2;
            }
        }
        throw new IllegalStateException("Missing Runic UI method " + string2);
    }

    private static <T> T call(String string, Object ... objectArray) {
        try {
            return (T)RunicRefinedClassicUi.lookup(string, objectArray.length).invoke(null, objectArray);
        }
        catch (InvocationTargetException invocationTargetException) {
            throw new IllegalStateException("Runic UI:" + string, invocationTargetException.getCause());
        }
        catch (ReflectiveOperationException reflectiveOperationException) {
            throw new IllegalStateException("Runic UI:" + string, reflectiveOperationException);
        }
    }

    private static Field fld(Object object, String string) throws NoSuchFieldException {
        for (Class<?> clazz = object.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            try {
                Field field = clazz.getDeclaredField(string);
                field.setAccessible(true);
                return field;
            }
            catch (NoSuchFieldException noSuchFieldException) {
                continue;
            }
        }
        throw new NoSuchFieldException(string);
    }

    private static Object get(Object object, String string) {
        try {
            return object == null ? null : RunicRefinedClassicUi.fld(object, string).get(object);
        }
        catch (Exception exception) {
            return null;
        }
    }

    private static void set(Object object, String string, Object object2) {
        try {
            RunicRefinedClassicUi.fld(object, string).set(object, object2);
        }
        catch (Exception exception) {
            throw new IllegalStateException("Field " + string, exception);
        }
    }

    private static int num(Object object) {
        int n;
        if (object instanceof Number) {
            Number number = (Number)object;
            n = number.intValue();
        } else {
            n = 0;
        }
        return n;
    }

    private static long lng(Object object) {
        long l;
        if (object instanceof Number) {
            Number number = (Number)object;
            l = number.longValue();
        } else {
            l = 0L;
        }
        return l;
    }

    private static int x(Object object) {
        return (Integer)RunicRefinedClassicUi.call("x", object);
    }

    private static int y(Object object) {
        return (Integer)RunicRefinedClassicUi.call("y", object);
    }

    private static int ax(Object object) {
        return RunicRefinedClassicUi.x(object) + -16;
    }

    private static int ay(Object object) {
        return RunicRefinedClassicUi.y(object) + -12;
    }

    private static boolean trans(Object object) {
        return Boolean.TRUE.equals(RunicRefinedClassicUi.call("trans", object));
    }

    private static boolean empty(Object object) {
        return Boolean.TRUE.equals(RunicRefinedClassicUi.call("empty", object));
    }

    private static Object source(Object object) {
        return RunicRefinedClassicUi.call("source", object);
    }

    private static Object target(Object object) {
        return RunicRefinedClassicUi.call("target", object);
    }

    private static Object equipment(Object object) {
        return RunicRefinedClassicUi.call("equipment", object);
    }

    private static Object selected(Object object) {
        return RunicRefinedClassicUi.call("current", object);
    }

    private static class_327 font(Object object) {
        return (class_327)RunicRefinedClassicUi.call("font", object);
    }

    private static List<?> all(Object object) {
        return (List)RunicRefinedClassicUi.call("filtered", object);
    }

    private static List<?> transferIds(Object object) {
        return (List)RunicRefinedClassicUi.call("transferIds", object);
    }

    private static Set<?> selectedTransfer(Object object) {
        return (Set)RunicRefinedClassicUi.call("transfers", object);
    }

    private static Set<?> selectedCurses(Object object) {
        return (Set)RunicRefinedClassicUi.call("curses", object);
    }

    private static Map<?, Integer> enchants(Object object) {
        return (Map)RunicRefinedClassicUi.call("enchants", object);
    }

    private static List<?> costs(Object object, boolean bl) {
        List list;
        block9: {
            list = (List)RunicRefinedClassicUi.call("costs", object, bl);
            if (!list.isEmpty()) {
                return list;
            }
            try {
                List list2;
                if (!bl) {
                    List list3;
                    Object object2 = RunicRefinedClassicUi.selected(object);
                    Object object3 = RunicRefinedClassicUi.equipment(object);
                    if (object2 == null || RunicRefinedClassicUi.empty(object3)) {
                        return list;
                    }
                    Object t = RunicRefinedClassicUi.call("handler", object);
                    Object t2 = RunicRefinedClassicUi.call("call", t, "getDefinition", new Object[]{object2});
                    if (t2 == null) {
                        return list;
                    }
                    int n = RunicRefinedClassicUi.num(RunicRefinedClassicUi.enchants(object3).get(object2)) + 1;
                    Object t3 = RunicRefinedClassicUi.call("call", t, "getClientCosts", new Object[]{t2, n});
                    if (t3 instanceof List && !(list3 = (List)t3).isEmpty()) {
                        return list3;
                    }
                    return list;
                }
                if (RunicRefinedClassicUi.selectedTransfer(object).isEmpty() && RunicRefinedClassicUi.selectedCurses(object).isEmpty()) {
                    return list;
                }
                Object t = RunicRefinedClassicUi.call("handler", object);
                Class<?> clazz = Class.forName("dev.runicattunement.transmutation.TransmutationRules");
                Object t4 = RunicRefinedClassicUi.call("call", clazz, "calculateCosts", new Object[]{RunicRefinedClassicUi.source(object), RunicRefinedClassicUi.target(object), RunicRefinedClassicUi.call("call", t, "getDefinitions", new Object[0]), RunicRefinedClassicUi.selectedTransfer(object), RunicRefinedClassicUi.selectedCurses(object), RunicRefinedClassicUi.call("call", t, "getTransmutationCosts", new Object[0]), RunicRefinedClassicUi.call("call", t, "getTransmutationSettings", new Object[0])});
                if (t4 instanceof List && !(list2 = (List)t4).isEmpty()) {
                    return list2;
                }
            }
            catch (Throwable throwable) {
                String string;
                State state = RunicRefinedClassicUi.st(object);
                String string2 = string = bl ? "direct-transmutation-cost-quote" : "direct-attunement-cost-quote";
                if (!state.failedSections.add(string)) break block9;
                System.err.println("[Runic Classic Refined] Could not read server-synchronised material quote: " + String.valueOf(throwable));
                throwable.printStackTrace();
            }
        }
        return list;
    }

    private static int xp(Object object, boolean bl) {
        return (Integer)RunicRefinedClassicUi.call("xp", object, bl);
    }

    private static boolean allowed(Object object, boolean bl) {
        return Boolean.TRUE.equals(RunicRefinedClassicUi.call("allowed", object, bl));
    }

    private static String nice(Object object) {
        if (object == null) {
            return "\u2014";
        }
        try {
            String string = (String)RunicRefinedClassicUi.call("naturalName", object, 0);
            return string == null ? String.valueOf(object) : string;
        }
        catch (Throwable throwable) {
            return String.valueOf(object);
        }
    }

    private static void fill(class_332 class_3322, int n, int n2, int n3, int n4, int n5) {
        if (n3 > 0 && n4 > 0) {
            class_3322.method_25294(n, n2, n + n3, n2 + n4, n5);
        }
    }

    private static void border(class_332 class_3322, int n, int n2, int n3, int n4, int n5) {
        RunicRefinedClassicUi.fill(class_3322, n, n2, n3, 1, n5);
        RunicRefinedClassicUi.fill(class_3322, n, n2 + n4 - 1, n3, 1, n5);
        RunicRefinedClassicUi.fill(class_3322, n, n2, 1, n4, n5);
        RunicRefinedClassicUi.fill(class_3322, n + n3 - 1, n2, 1, n4, n5);
    }

    private static void text(class_332 class_3322, class_327 class_3272, String string, int n, int n2, int n3) {
        class_3322.method_51433(class_3272, string, n, n2, n3, false);
    }

    private static String fit(String string, int n) {
        return string == null ? "" : (string.length() > n ? string.substring(0, Math.max(1, n - 1)) + "\u2026" : string);
    }

    private static int width(class_327 class_3272, String string) {
        return class_3272.method_1727(string);
    }

    private static void centered(class_332 class_3322, class_327 class_3272, String string, int n, int n2, int n3) {
        RunicRefinedClassicUi.text(class_3322, class_3272, string, n - RunicRefinedClassicUi.width(class_3272, string) / 2, n2, n3);
    }

    private static void centerBox(class_332 class_3322, class_327 class_3272, String string, int n, int n2, int n3, int n4, int n5) {
        RunicRefinedClassicUi.text(class_3322, class_3272, string, n + (n3 - RunicRefinedClassicUi.width(class_3272, string)) / 2, n2 + (n4 - 9) / 2, n5);
    }

    private static String fitPixels(class_327 class_3272, String string, int n) {
        int n2;
        if (string == null) {
            return "";
        }
        if (RunicRefinedClassicUi.width(class_3272, string) <= n) {
            return string;
        }
        int n3 = Math.max(0, n - RunicRefinedClassicUi.width(class_3272, "\u2026"));
        for (n2 = string.length(); n2 > 0 && RunicRefinedClassicUi.width(class_3272, string.substring(0, n2)) > n3; --n2) {
        }
        return string.substring(0, n2) + "\u2026";
    }

    private static boolean inside(double d, double d2, int n, int n2, int n3, int n4) {
        return d >= (double)n && d2 >= (double)n2 && d < (double)(n + n3) && d2 < (double)(n2 + n4);
    }

    private static boolean hover(Object object, int n, int n2, int n3, int n4) {
        State state = RunicRefinedClassicUi.st(object);
        return RunicRefinedClassicUi.inside(state.mouseX, state.mouseY, RunicRefinedClassicUi.ax(object) + n, RunicRefinedClassicUi.ay(object) + n2, n3, n4);
    }

    private static void button(class_332 class_3322, class_327 class_3272, String string, int n, int n2, int n3, int n4, boolean bl, boolean bl2, int n5) {
        RunicRefinedClassicUi.fill(class_3322, n, n2, n3, n4, -15526116);
        RunicRefinedClassicUi.fill(class_3322, n + 1, n2 + 1, n3 - 2, n4 - 2, bl ? (bl2 ? -11188384 : -12567753) : -14013134);
        RunicRefinedClassicUi.border(class_3322, n, n2, n3, n4, bl ? (bl2 ? -1983106 : -7572401) : -11512740);
        RunicRefinedClassicUi.fill(class_3322, n + 2, n2 + 1, n3 - 4, 1, bl ? n5 : -12499378);
        RunicRefinedClassicUi.centerBox(class_3322, class_3272, string, n, n2 + 1, n3, n4, bl ? (bl2 ? -666456 : -2565411) : -9670022);
    }

    private static void tabButton(class_332 class_3322, class_327 class_3272, String string, int n, int n2, int n3, int n4, boolean bl, boolean bl2) {
        int n5;
        int n6;
        int n7 = bl ? -11452321 : (n6 = bl2 ? -10530501 : -13354946);
        int n8 = bl ? -4220196 : (n5 = bl2 ? -1720965 : -9472640);
        int n9 = bl ? -8004 : (bl2 ? -7505 : -4802113);
        RunicRefinedClassicUi.fill(class_3322, n, n2, n3, n4, -15657959);
        RunicRefinedClassicUi.fill(class_3322, n + 1, n2 + 1, n3 - 2, n4 - 2, n6);
        RunicRefinedClassicUi.border(class_3322, n, n2, n3, n4, n5);
        RunicRefinedClassicUi.fill(class_3322, n + 2, n2 + 2, n3 - 4, 1, bl ? -4814117 : (bl2 ? -997748 : -10327949));
        RunicRefinedClassicUi.centerBox(class_3322, class_3272, string, n, n2 + 1, n3, n4, n9);
    }

    private static void switchSound() {
        try {
            class_3414 class_34142 = class_3414.method_47908((class_2960)new class_2960("minecraft", "ui.button.click"));
            class_1109 class_11092 = class_1109.method_4758((class_3414)class_34142, (float)1.06f);
            class_310.method_1551().method_1483().method_4873((class_1113)class_11092);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static void switchFx(Object object) {
        RunicRefinedClassicUi.st((Object)object).tabFxStart = System.nanoTime();
        RunicRefinedClassicUi.switchSound();
    }

    private static void drawSwitchFx(Object object, class_332 class_3322, int n, int n2) {
        long l = System.nanoTime() - RunicRefinedClassicUi.st((Object)object).tabFxStart;
        if (l < 0L || l > 420000000L || RunicRefinedClassicUi.st((Object)object).tabFxStart == 0L) {
            return;
        }
        float f = (float)l / 4.2E8f;
        int n3 = n + (RunicRefinedClassicUi.trans(object) ? 515 : 378);
        int n4 = n2 + 25;
        int n5 = RunicRefinedClassicUi.trans(object) ? 130 : 129;
        int n6 = 28;
        int n7 = f < 0.35f ? -1787398 : -4812331;
        for (int i = 0; i < 6; ++i) {
            float f2 = ((float)i / 6.0f + f * 0.7f) % 1.0f;
            int n8 = n3 + 3 + (int)((float)(n5 - 6) * f2);
            int n9 = i % 2 == 0 ? n4 + 2 : n4 + n6 - 4;
            RunicRefinedClassicUi.fill(class_3322, n8, n9, 2, 2, i % 3 == 0 ? -1195647 : n7);
        }
    }

    private static void filterButton(class_332 class_3322, class_327 class_3272, String string, int n, int n2, int n3, int n4, boolean bl, boolean bl2) {
        RunicRefinedClassicUi.fill(class_3322, n, n2, n3, n4, -15526116);
        RunicRefinedClassicUi.fill(class_3322, n + 1, n2 + 1, n3 - 2, n4 - 2, bl ? -11058325 : (bl2 ? -11845565 : -12567753));
        RunicRefinedClassicUi.border(class_3322, n, n2, n3, n4, bl ? -5733944 : -7506354);
        RunicRefinedClassicUi.centerBox(class_3322, class_3272, string, n, n2 + 1, n3, n4, bl ? -665423 : -7695202);
    }

    private static void tex(class_332 class_3322, class_2960 class_29602, int n, int n2, int n3, int n4) {
        class_3322.method_25290(class_29602, n, n2, 0.0f, 0.0f, n3, n4, n3, n4);
    }

    private static void searchPos(Object object, int n, int n2, int n3) {
        if (object == null) {
            return;
        }
        for (String[] stringArray : new String[][]{{"method_46421", String.valueOf(n)}, {"method_46419", String.valueOf(n2)}}) {
            boolean bl = false;
            for (Method method : object.getClass().getMethods()) {
                if (!method.getName().equals(stringArray[0]) || method.getParameterCount() != 1) continue;
                try {
                    method.invoke(object, Integer.parseInt(stringArray[1]));
                    bl = true;
                    break;
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
            if (bl) continue;
            throw new IllegalStateException("Search field move: " + stringArray[0]);
        }
        RunicRefinedClassicUi.set(object, "field_22758", n3);
    }

    public static void relayout(Object object) {
        if (!RunicGrimoireUi.active(object)) {
            return;
        }
        try {
            List list = (List)RunicRefinedClassicUi.call("slots", object);
            int n = RunicRefinedClassicUi.ax(object);
            int n2 = RunicRefinedClassicUi.ay(object);
            int n3 = RunicRefinedClassicUi.num(RunicRefinedClassicUi.get(object, "field_2776"));
            int n4 = RunicRefinedClassicUi.num(RunicRefinedClassicUi.get(object, "field_2800"));
            boolean bl = RunicRefinedClassicUi.trans(object);
            for (int i = 0; i < list.size(); ++i) {
                int n5 = -5000;
                int n6 = -5000;
                if (i == 0) {
                    n5 = n + 124 - 8;
                    n6 = n2 + 153 - 8;
                } else if (i == 1 && bl) {
                    n5 = n + 547 - 8;
                    n6 = n2 + 153 - 8;
                } else if (i >= 2 && i < 38) {
                    int n7 = i - 2;
                    int n8 = n7 < 27 ? n7 / 9 : 3;
                    int n9 = n7 < 27 ? n7 % 9 : n7 - 27;
                    n5 = n + 255 + 18 * n9;
                    n6 = n2 + 270 + 18 * n8 + (n8 == 3 ? 8 : 0);
                }
                RunicRefinedClassicUi.set(list.get(i), "field_7873", n5 - n3);
                RunicRefinedClassicUi.set(list.get(i), "field_7872", n6 - n4);
            }
            Object object2 = RunicRefinedClassicUi.get(object, "searchField");
            if (object2 != null) {
                RunicRefinedClassicUi.searchPos(object2, n + (bl ? -5000 : 458), n2 + (bl ? -5000 : 100), 177);
            }
        }
        catch (Throwable throwable) {
            RunicRefinedClassicUi.error(object, "relayout", throwable);
        }
    }

    private static void error(Object object, String string, Throwable throwable) {
        if (!RunicRefinedClassicUi.st((Object)object).warned) {
            RunicRefinedClassicUi.st((Object)object).warned = true;
            System.err.println("[Runic Classic Refined] " + string + ": " + String.valueOf(throwable));
            throwable.printStackTrace();
        }
    }

    public static boolean background(Object object, class_332 class_3322, float f, int n, int n2) {
        if (!RunicGrimoireUi.active(object)) {
            return false;
        }
        try {
            RunicGrimoireUi.prepare(object);
            RunicRefinedClassicUi.st((Object)object).mouseX = n;
            RunicRefinedClassicUi.st((Object)object).mouseY = n2;
            Object t = RunicRefinedClassicUi.call("state", object);
            RunicRefinedClassicUi.set(t, "mouseX", n);
            RunicRefinedClassicUi.set(t, "mouseY", n2);
            int n3 = RunicRefinedClassicUi.ax(object);
            int n4 = RunicRefinedClassicUi.ay(object);
            RunicRefinedClassicUi.tex(class_3322, RunicRefinedClassicUi.trans(object) ? BG_TRANS : BG, n3, n4, 672, 390);
            boolean bl = RunicRefinedClassicUi.trans(object);
            Object object2 = bl ? RunicRefinedClassicUi.source(object) : RunicRefinedClassicUi.equipment(object);
            RunicRefinedClassicUi.seal(class_3322, n3 + 124, n4 + 153, !RunicRefinedClassicUi.empty(object2), 0, object);
            if (bl) {
                RunicRefinedClassicUi.seal(class_3322, n3 + 547, n4 + 153, !RunicRefinedClassicUi.empty(RunicRefinedClassicUi.target(object)), 1, object);
            }
            for (int i = 0; i < 36; ++i) {
                int n5 = i < 27 ? i / 9 : 3;
                int n6 = i < 27 ? i % 9 : i - 27;
                int n7 = n3 + 255 + n6 * 18;
                int n8 = n4 + 270 + n5 * 18 + (n5 == 3 ? 8 : 0);
                RunicRefinedClassicUi.fill(class_3322, n7 - 1, n8 - 1, 18, 18, -11576987);
                RunicRefinedClassicUi.fill(class_3322, n7, n8, 16, 16, -14341325);
            }
            RunicRefinedClassicUi.relayout(object);
            return true;
        }
        catch (Throwable throwable) {
            RunicRefinedClassicUi.error(object, "background", throwable);
            return false;
        }
    }

    private static Object[] glyphs() {
        Object[] objectArray = runeSigils;
        if (objectArray != null) {
            return objectArray;
        }
        try {
            Field field = RunicGrimoireUi.class.getDeclaredField("CACHED_SIGILS");
            field.setAccessible(true);
            runeSigils = (Object[])field.get(null);
        }
        catch (Throwable throwable) {
            runeSigils = new Object[0];
        }
        return runeSigils;
    }

    private static void seal(class_332 class_3322, int n, int n2, boolean bl, int n3, Object object) {
        RunicRefinedClassicUi.tex(class_3322, bl ? RUNE_ON : RUNE_OFF, n - 56, n2 - 56, 112, 112);
        Object[] objectArray = RunicRefinedClassicUi.glyphs();
        if (objectArray.length == 0) {
            return;
        }
        double d = (double)System.nanoTime() / 1.0E9;
        double d2 = d * (bl ? 0.28 : 0.065) * (double)(n3 == 0 ? 1 : -1);
        for (int i = 0; i < 13; ++i) {
            double d3 = Math.PI * 2 * (double)i / 13.0 + d2 + (double)n3 * 0.19;
            int n4 = n + (int)Math.round(56.0 * Math.cos(d3)) - 3;
            int n5 = n2 + (int)Math.round(56.0 * Math.sin(d3)) - 4;
            int n6 = Math.floorMod(n3 * 7 + i * 11 + i * i * 3, objectArray.length);
            Object object2 = objectArray[n6];
            if (!(object2 instanceof class_2561)) continue;
            class_2561 class_25612 = (class_2561)object2;
            int n7 = bl ? (i % 3 == 0 ? -1063545 : -4219931) : (i % 3 == 0 ? -8951684 : -10331279);
            class_3322.method_27534(RunicRefinedClassicUi.font(object), class_25612, n4, n5, n7);
        }
    }

    public static boolean foreground(Object object, class_332 class_3322) {
        if (!RunicGrimoireUi.active(object)) {
            return false;
        }
        try {
            class_327 class_3272 = RunicRefinedClassicUi.font(object);
            int n = RunicRefinedClassicUi.num(RunicRefinedClassicUi.get(object, "field_2776"));
            int n2 = RunicRefinedClassicUi.num(RunicRefinedClassicUi.get(object, "field_2800"));
            int n3 = RunicRefinedClassicUi.ax(object) - n;
            int n4 = RunicRefinedClassicUi.ay(object) - n2;
            boolean bl = RunicRefinedClassicUi.trans(object);
            RunicRefinedClassicUi.centerBox(class_3322, class_3272, "RUNIC ATTUNEMENT TABLE", n3 + 28, n4 + 25, 341, 28, -3563153);
            RunicRefinedClassicUi.tabButton(class_3322, class_3272, "ATTUNEMENT", n3 + 378, n4 + 25, 129, 28, !bl, RunicRefinedClassicUi.hover(object, 378, 25, 129, 28));
            RunicRefinedClassicUi.tabButton(class_3322, class_3272, "TRANSMUTATION", n3 + 515, n4 + 25, 130, 28, bl, RunicRefinedClassicUi.hover(object, 515, 25, 130, 28));
            RunicRefinedClassicUi.centerBox(class_3322, class_3272, bl ? "SOURCE EQUIPMENT" : "EQUIPMENT", n3 + 34, n4 + 72, 179, 20, -3563153);
            RunicRefinedClassicUi.centerBox(class_3322, class_3272, bl ? "TRANSMUTATION" : "ATTUNEMENT", n3 + 247, n4 + 72, 176, 20, -3563153);
            RunicRefinedClassicUi.centerBox(class_3322, class_3272, bl ? "TARGET EQUIPMENT" : "ATTUNEMENT TOME", n3 + 457, n4 + 72, 181, 20, -3563153);
            if (bl) {
                RunicRefinedClassicUi.drawTransmutation(object, class_3322, class_3272, n3, n4);
            } else {
                RunicRefinedClassicUi.drawAttunement(object, class_3322, class_3272, n3, n4);
            }
            RunicRefinedClassicUi.centerBox(class_3322, class_3272, "INVENTORY", n3 + 246, n4 + 253, 177, 15, -3563153);
            RunicRefinedClassicUi.drawFX(object, class_3322, n3, n4);
            RunicRefinedClassicUi.drawSwitchFx(object, class_3322, n3, n4);
            RunicRefinedClassicUi.drawInfoPopup(object, class_3322, class_3272, n3, n4);
            return true;
        }
        catch (Throwable throwable) {
            RunicRefinedClassicUi.error(object, "foreground", throwable);
            return true;
        }
    }

    private static void safely(Object object, String string, Runnable runnable) {
        block2: {
            try {
                runnable.run();
            }
            catch (Throwable throwable) {
                State state = RunicRefinedClassicUi.st(object);
                if (!state.failedSections.add(string)) break block2;
                System.err.println("[Runic Classic Refined] Foreground section '" + string + "' failed; other panels retained: " + String.valueOf(throwable));
                throwable.printStackTrace();
            }
        }
    }

    private static void drawAttunement(Object object, class_332 class_3322, class_327 class_3272, int n, int n2) {
        RunicRefinedClassicUi.safely(object, "attunement-refresh", () -> RunicRefinedClassicUi.call("call", object, "syncAttunementCostState", new Object[0]));
        RunicRefinedClassicUi.safely(object, "attunement-inscribed", () -> RunicRefinedClassicUi.drawAttunementInscribed(object, class_3322, class_3272, n, n2));
        RunicRefinedClassicUi.safely(object, "attunement-costs-and-actions", () -> RunicRefinedClassicUi.drawAttunementCenter(object, class_3322, class_3272, n, n2));
        RunicRefinedClassicUi.safely(object, "attunement-tome", () -> RunicRefinedClassicUi.drawAttunementCatalogue(object, class_3322, class_3272, n, n2));
    }

    private static void drawAttunementInscribed(Object object, class_332 class_3322, class_327 class_3272, int n, int n2) {
        Object object2 = RunicRefinedClassicUi.equipment(object);
        Object object3 = RunicRefinedClassicUi.selected(object);
        boolean bl = !RunicRefinedClassicUi.empty(object2);
        RunicRefinedClassicUi.centerBox(class_3322, class_3272, "INSCRIBED RUNES", n + 35, n2 + 220, 178, 18, -3563153);
        Map<Object, Object> map = bl ? RunicRefinedClassicUi.enchants(object2) : Map.of();
        int n3 = 0;
        int n4 = Math.max(0, RunicRefinedClassicUi.num(RunicRefinedClassicUi.get(RunicRefinedClassicUi.call("state", object), "installedScroll")));
        if (map.isEmpty()) {
            RunicRefinedClassicUi.centerBox(class_3322, class_3272, bl ? "No enchantments" : "Insert equipment", n + 38, n2 + 247, 168, 20, -8945783);
        }
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            if (n3++ < n4) continue;
            int n5 = n3 - 1 - n4;
            if (n5 >= 6) break;
            int n6 = n2 + 244 + n5 * 17;
            boolean bl2 = Objects.equals(object3, entry.getKey());
            if (bl2) {
                RunicRefinedClassicUi.fill(class_3322, n + 40, n6 - 1, 165, 15, -11649438);
            }
            RunicRefinedClassicUi.text(class_3322, class_3272, RunicRefinedClassicUi.fitPixels(class_3272, RunicRefinedClassicUi.nice(entry.getKey()), 129), n + 43, n6 + 2, bl2 ? -2565411 : -7695202);
            RunicRefinedClassicUi.text(class_3322, class_3272, RunicRefinedClassicUi.roman(RunicRefinedClassicUi.num(entry.getValue())), n + 198 - RunicRefinedClassicUi.width(class_3272, RunicRefinedClassicUi.roman(RunicRefinedClassicUi.num(entry.getValue()))), n6 + 2, bl2 ? -3892498 : -3563153);
        }
        if (map.size() > 6) {
            RunicRefinedClassicUi.text(class_3322, class_3272, "\u25be", n + 197, n2 + 337, -3892498);
        }
    }

    private static void drawAttunementCenter(Object object, class_332 class_3322, class_327 class_3272, int n, int n2) {
        int n3;
        Object object2 = RunicRefinedClassicUi.equipment(object);
        Object object3 = RunicRefinedClassicUi.selected(object);
        boolean bl = !RunicRefinedClassicUi.empty(object2);
        boolean bl2 = bl && object3 != null;
        int n4 = n3 = bl2 ? RunicRefinedClassicUi.num(RunicRefinedClassicUi.call("level", object)) : 0;
        RunicRefinedClassicUi.centerBox(class_3322, class_3272, (String)(bl2 ? "LEVEL  " + (n3 > 0 ? RunicRefinedClassicUi.roman(n3) : "\u2014") + "  \u2192  " + RunicRefinedClassicUi.roman(n3 + 1) : "LEVEL  \u2014  \u2192  \u2014"), n + 249, n2 + 105, 172, 14, bl2 ? -3563153 : -8945783);
        RunicRefinedClassicUi.centerBox(class_3322, class_3272, "MATERIAL COSTS", n + 249, n2 + 121, 172, 13, -3563153);
        RunicRefinedClassicUi.safely(object, "attunement-material-items", () -> RunicRefinedClassicUi.drawCosts(object, class_3322, class_3272, n + 262, n2 + 139, false));
        RunicRefinedClassicUi.centerBox(class_3322, class_3272, (String)(bl2 ? "XP: " + RunicRefinedClassicUi.xp(object, false) : "XP: \u2014"), n + 249, n2 + 195, 172, 11, bl2 ? -3563153 : -8945783);
        boolean bl3 = bl2 && RunicRefinedClassicUi.allowed(object, false);
        boolean bl4 = bl2 && n3 > 0;
        RunicRefinedClassicUi.button(class_3322, class_3272, "UNRAVEL", n + 248, n2 + 221, 83, 20, bl4, RunicRefinedClassicUi.hover(object, 248, 221, 83, 20), -5995835);
        RunicRefinedClassicUi.button(class_3322, class_3272, "ATTUNE", n + 336, n2 + 221, 83, 20, bl3, RunicRefinedClassicUi.hover(object, 336, 221, 83, 20), -3562899);
    }

    private static void drawAttunementCatalogue(Object object, class_332 class_3322, class_327 class_3272, int n, int n2) {
        int n3;
        int n4;
        Object object2 = RunicRefinedClassicUi.selected(object);
        for (int i = 0; i < 4; ++i) {
            n4 = n + 453 + i * 47;
            n3 = RunicRefinedClassicUi.num(RunicRefinedClassicUi.get(object, "tierFilter"));
            RunicRefinedClassicUi.filterButton(class_3322, class_3272, (new String[]{"ALL", "I", "II", "III"})[i], n4, n2 + 125, 43, 16, n3 == i, RunicRefinedClassicUi.hover(object, 453 + i * 47, 125, 43, 16));
        }
        List<?> list = RunicRefinedClassicUi.all(object);
        n4 = Math.max(0, list.size() - 11);
        n3 = Math.max(0, Math.min(n4, RunicRefinedClassicUi.num(RunicRefinedClassicUi.get(object, "scrollOffset"))));
        for (int i = 0; i < 11 && n3 + i < list.size(); ++i) {
            Object obj = list.get(n3 + i);
            int n5 = n2 + 149 + i * 17;
            boolean bl = Objects.equals(object2, obj);
            boolean bl2 = RunicRefinedClassicUi.hover(object, 455, 149 + i * 17, 177, 17);
            if (bl) {
                RunicRefinedClassicUi.fill(class_3322, n + 455, n5, 177, 16, -11649438);
            } else if (bl2) {
                RunicRefinedClassicUi.fill(class_3322, n + 455, n5, 177, 16, -12300711);
            }
            RunicRefinedClassicUi.text(class_3322, class_3272, RunicRefinedClassicUi.fitPixels(class_3272, RunicRefinedClassicUi.nice(obj), 131), n + 459, n5 + 4, bl ? -929372 : -7695202);
            RunicRefinedClassicUi.badge(class_3322, class_3272, "i", n + 594, n5 + 1, 15, 15, -7155500);
            RunicRefinedClassicUi.badge(class_3322, class_3272, RunicRefinedClassicUi.roman(RunicRefinedClassicUi.tier(object, obj)), n + 613, n5 + 1, 19, 15, RunicRefinedClassicUi.tier(object, obj) == 3 ? -1918856 : -4021526);
            if (!bl) continue;
            RunicRefinedClassicUi.fill(class_3322, n + 455, n5, 2, 16, -5600296);
        }
        RunicRefinedClassicUi.scroll(class_3322, n + 636, n2 + 150, 4, 185, n3, n4);
    }

    private static void drawTransmutation(Object object, class_332 class_3322, class_327 class_3272, int n, int n2) {
        RunicRefinedClassicUi.safely(object, "transmutation-refresh", () -> RunicRefinedClassicUi.call("call", object, "syncTransmutationState", new Object[0]));
        RunicRefinedClassicUi.safely(object, "transmutation-source-list", () -> RunicRefinedClassicUi.drawTransmutationSource(object, class_3322, class_3272, n, n2));
        RunicRefinedClassicUi.safely(object, "transmutation-result-preview", () -> RunicRefinedClassicUi.drawTransmutationResult(object, class_3322, class_3272, n, n2));
        RunicRefinedClassicUi.safely(object, "transmutation-costs-and-actions", () -> RunicRefinedClassicUi.drawTransmutationCenter(object, class_3322, class_3272, n, n2));
    }

    private static void drawTransmutationSource(Object object, class_332 class_3322, class_327 class_3272, int n, int n2) {
        Object object2 = RunicRefinedClassicUi.source(object);
        Object object3 = RunicRefinedClassicUi.target(object);
        List<?> list = RunicRefinedClassicUi.transferIds(object);
        RunicRefinedClassicUi.centerBox(class_3322, class_3272, "SOURCE ENCHANTMENTS", n + 35, n2 + 220, 178, 18, -3563153);
        Object t = RunicRefinedClassicUi.call("state", object);
        int n3 = Math.max(0, RunicRefinedClassicUi.num(RunicRefinedClassicUi.get(t, "sourceScroll")));
        Set<?> set = RunicRefinedClassicUi.selectedTransfer(object);
        Set<?> set2 = RunicRefinedClassicUi.selectedCurses(object);
        for (int i = 0; i < 6 && i + n3 < list.size(); ++i) {
            Object obj = list.get(i + n3);
            int n4 = n2 + 244 + i * 17;
            boolean bl = set.contains(obj) || set2.contains(obj);
            RunicRefinedClassicUi.fill(class_3322, n + 41, n4 + 1, 11, 11, bl ? -9086820 : -14341583);
            RunicRefinedClassicUi.border(class_3322, n + 41, n4 + 1, 11, 11, bl ? -3892498 : -7695202);
            if (bl) {
                RunicRefinedClassicUi.fill(class_3322, n + 45, n4 + 5, 3, 3, -3563153);
            }
            RunicRefinedClassicUi.text(class_3322, class_3272, RunicRefinedClassicUi.fitPixels(class_3272, RunicRefinedClassicUi.nice(obj), 101), n + 59, n4 + 3, bl ? -2565411 : -7695202);
            RunicRefinedClassicUi.badge(class_3322, class_3272, "i", n + 166, n4, 15, 15, -7155500);
            RunicRefinedClassicUi.badge(class_3322, class_3272, RunicRefinedClassicUi.roman(RunicRefinedClassicUi.tier(object, obj)), n + 185, n4, 18, 15, -3563153);
        }
        if (list.isEmpty()) {
            RunicRefinedClassicUi.centerBox(class_3322, class_3272, RunicRefinedClassicUi.empty(object2) ? "Insert source item" : "No enchantments", n + 38, n2 + 248, 165, 20, -8945783);
        }
        if (list.size() > 6) {
            RunicRefinedClassicUi.scroll(class_3322, n + 206, n2 + 244, 3, 100, n3, Math.max(0, list.size() - 6));
        }
    }

    private static void drawTransmutationResult(Object object, class_332 class_3322, class_327 class_3272, int n, int n2) {
        Object object2 = RunicRefinedClassicUi.source(object);
        Object object3 = RunicRefinedClassicUi.target(object);
        Set<?> set = RunicRefinedClassicUi.selectedTransfer(object);
        Set<?> set2 = RunicRefinedClassicUi.selectedCurses(object);
        RunicRefinedClassicUi.centerBox(class_3322, class_3272, "RESULT PREVIEW", n + 453, n2 + 220, 185, 18, -3563153);
        LinkedHashMap<Object, Integer> linkedHashMap = new LinkedHashMap<Object, Integer>();
        linkedHashMap.putAll(RunicRefinedClassicUi.enchants(object3));
        Map<?, Integer> map = RunicRefinedClassicUi.enchants(object2);
        for (Object object4 : set) {
            if (map.get(object4) == null) continue;
            linkedHashMap.merge(object4, map.get(object4), Math::max);
        }
        for (Object object4 : set2) {
            if (map.get(object4) == null) continue;
            linkedHashMap.merge(object4, map.get(object4), Math::max);
        }
        int n3 = 0;
        for (Map.Entry entry : linkedHashMap.entrySet()) {
            if (n3 >= 6) break;
            RunicRefinedClassicUi.text(class_3322, class_3272, RunicRefinedClassicUi.fitPixels(class_3272, RunicRefinedClassicUi.nice(entry.getKey()), 135), n + 464, n2 + 245 + n3 * 17, -7695202);
            RunicRefinedClassicUi.text(class_3322, class_3272, RunicRefinedClassicUi.roman((Integer)entry.getValue()), n + 615, n2 + 245 + n3 * 17, -3563153);
            ++n3;
        }
        if (n3 == 0) {
            RunicRefinedClassicUi.centerBox(class_3322, class_3272, RunicRefinedClassicUi.empty(object3) ? "Insert target item" : "No resulting runes", n + 453, n2 + 248, 185, 20, -8945783);
        }
    }

    private static void drawTransmutationCenter(Object object, class_332 class_3322, class_327 class_3272, int n, int n2) {
        List<?> list = RunicRefinedClassicUi.transferIds(object);
        RunicRefinedClassicUi.centerBox(class_3322, class_3272, "TRANSFER COST", n + 249, n2 + 105, 172, 14, -3563153);
        RunicRefinedClassicUi.centerBox(class_3322, class_3272, "MATERIALS", n + 249, n2 + 121, 172, 13, -3563153);
        RunicRefinedClassicUi.safely(object, "transmutation-material-items", () -> RunicRefinedClassicUi.drawCosts(object, class_3322, class_3272, n + 262, n2 + 139, true));
        RunicRefinedClassicUi.centerBox(class_3322, class_3272, "XP: " + RunicRefinedClassicUi.xp(object, true), n + 249, n2 + 195, 172, 11, -3563153);
        RunicRefinedClassicUi.button(class_3322, class_3272, "SELECT ALL", n + 248, n2 + 216, 83, 15, !list.isEmpty(), RunicRefinedClassicUi.hover(object, 248, 216, 83, 15), -4879390);
        RunicRefinedClassicUi.button(class_3322, class_3272, "CLEAR", n + 336, n2 + 216, 83, 15, !list.isEmpty(), RunicRefinedClassicUi.hover(object, 336, 216, 83, 15), -5668275);
        RunicRefinedClassicUi.button(class_3322, class_3272, "TRANSMUTE", n + 248, n2 + 234, 171, 15, RunicRefinedClassicUi.allowed(object, true), RunicRefinedClassicUi.hover(object, 248, 234, 171, 15), -4879390);
    }

    private static Object itemFor(Object object) {
        if (!(object instanceof class_2960)) {
            return null;
        }
        class_2960 class_29602 = (class_2960)object;
        return class_7923.field_41178.method_17966(class_29602).orElse(null);
    }

    private static void badge(class_332 class_3322, class_327 class_3272, String string, int n, int n2, int n3, int n4, int n5) {
        RunicRefinedClassicUi.fill(class_3322, n, n2, n3, n4, -15394012);
        RunicRefinedClassicUi.border(class_3322, n, n2, n3, n4, -9537657);
        RunicRefinedClassicUi.centerBox(class_3322, class_3272, string, n, n2 + 1, n3, n4, n5);
    }

    private static int tier(Object object, Object object2) {
        try {
            Object t = RunicRefinedClassicUi.call("handler", object);
            Object t2 = RunicRefinedClassicUi.call("call", t, "getDefinition", new Object[]{object2});
            return Math.max(1, Math.min(3, RunicRefinedClassicUi.num(RunicRefinedClassicUi.invokeMember(t2, "tabletTier"))));
        }
        catch (Exception exception) {
            return 1;
        }
    }

    private static void drawCosts(Object object, class_332 class_3322, class_327 class_3272, int n, int n2, boolean bl) {
        List<?> list = RunicRefinedClassicUi.costs(object, bl);
        if (list.isEmpty()) {
            Object t;
            Object t2;
            Object t3;
            boolean bl2;
            boolean bl3 = bl && (!RunicRefinedClassicUi.selectedTransfer(object).isEmpty() || !RunicRefinedClassicUi.selectedCurses(object).isEmpty());
            boolean bl4 = bl2 = !RunicRefinedClassicUi.empty(bl ? RunicRefinedClassicUi.source(object) : RunicRefinedClassicUi.equipment(object));
            String string = bl && !bl3 ? "Select enchantments to transfer" : (!bl && (!bl2 || RunicRefinedClassicUi.selected(object) == null) ? "Select an enchantment" : "No material cost required");
            RunicRefinedClassicUi.centerBox(class_3322, class_3272, string, n - 14, n2 + 12, 176, 18, -8945783);
            if (bl3 && RunicRefinedClassicUi.xp(object, true) > 0 && RunicRefinedClassicUi.st((Object)object).failedSections.add("empty-selected-transfer-quote")) {
                try {
                    t3 = RunicRefinedClassicUi.call("handler", object);
                    t2 = RunicRefinedClassicUi.call("call", t3, "getTransmutationCosts", new Object[0]);
                    t = RunicRefinedClassicUi.call("call", t2, "unitMaterials", new Object[0]);
                    System.err.println("[Runic Classic Refined] Server-synced transfer quote is empty for " + RunicRefinedClassicUi.selectedTransfer(object).size() + " selected runes; unitMaterials=" + String.valueOf(t) + ". No material costs will be fabricated.");
                }
                catch (Throwable throwable) {
                    System.err.println("[Runic Classic Refined] Transfer quote diagnostics: " + String.valueOf(throwable));
                }
            }
            if (!bl && bl2 && RunicRefinedClassicUi.selected(object) != null && RunicRefinedClassicUi.xp(object, false) > 0 && RunicRefinedClassicUi.st((Object)object).failedSections.add("empty-attunement-quote-" + String.valueOf(RunicRefinedClassicUi.selected(object)) + "-" + RunicRefinedClassicUi.num(RunicRefinedClassicUi.enchants(RunicRefinedClassicUi.equipment(object)).get(RunicRefinedClassicUi.selected(object))))) {
                try {
                    t3 = RunicRefinedClassicUi.call("handler", object);
                    t2 = RunicRefinedClassicUi.call("call", t3, "data", new Object[0]);
                    t = RunicRefinedClassicUi.call("call", t2, "universalCosts", new Object[0]);
                    System.err.println("[Runic Classic Refined] Attunement quote empty for " + String.valueOf(RunicRefinedClassicUi.selected(object)) + "; synced universalCosts=" + String.valueOf(t) + ". No material costs will be fabricated.");
                }
                catch (Throwable throwable) {
                    System.err.println("[Runic Classic Refined] Attunement quote diagnostics: " + String.valueOf(throwable));
                }
            }
            return;
        }
        Object t = RunicRefinedClassicUi.call("state", object);
        int n3 = 8;
        int n4 = Math.max(0, list.size() - n3);
        int n5 = Math.max(0, Math.min(n4, RunicRefinedClassicUi.num(RunicRefinedClassicUi.get(t, "costScroll"))));
        for (int i = 0; i < n3 && i + n5 < list.size(); ++i) {
            Object object2;
            int n6;
            int n7;
            Object obj;
            block12: {
                obj = list.get(i + n5);
                Object object3 = RunicRefinedClassicUi.itemFor(RunicRefinedClassicUi.invokeMember(obj, "itemId"));
                n7 = n + i % 4 * 37 + RunicRefinedClassicUi.costRowInset(list, n5, i);
                n6 = n2 + 3 + i / 4 * 29;
                RunicRefinedClassicUi.fill(class_3322, n7 - 1, n6 - 1, 18, 18, -12498862);
                RunicRefinedClassicUi.fill(class_3322, n7, n6, 16, 16, -14341068);
                if (object3 instanceof class_1792) {
                    object2 = (class_1792)object3;
                    try {
                        class_3322.method_51427(new class_1799((class_1935)object2), n7, n6);
                    }
                    catch (Throwable throwable) {
                        if (!RunicRefinedClassicUi.st((Object)object).failedSections.add("material-icon")) break block12;
                        System.err.println("[Runic Classic Refined] Could not render a material item icon: " + String.valueOf(throwable));
                        throwable.printStackTrace();
                    }
                }
            }
            object2 = "\u00d7" + RunicRefinedClassicUi.num(RunicRefinedClassicUi.invokeMember(obj, "count"));
            RunicRefinedClassicUi.text(class_3322, class_3272, RunicRefinedClassicUi.fitPixels(class_3272, (String)object2, 22), n7 + 17, n6 + 4, -2565411);
        }
        if (list.size() > n3) {
            RunicRefinedClassicUi.scroll(class_3322, n + 153, n2 + 1, 3, 49, n5, n4);
        }
    }

    private static Object invokeMember(Object object, String string) {
        try {
            return object.getClass().getMethod(string, new Class[0]).invoke(object, new Object[0]);
        }
        catch (Exception exception) {
            return null;
        }
    }

    private static String roman(int n) {
        if (n <= 0) {
            return "\u2014";
        }
        if (n > 10) {
            return String.valueOf(n);
        }
        return (new String[]{"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"})[n];
    }

    private static void scroll(class_332 class_3322, int n, int n2, int n3, int n4, int n5, int n6) {
        RunicRefinedClassicUi.fill(class_3322, n, n2, n3, n4, -15525858);
        RunicRefinedClassicUi.fill(class_3322, n + 1, n2 + 1, n3 - 2, n4 - 2, -13354171);
        int n7 = n6 == 0 ? n4 - 2 : Math.max(14, n4 / (n6 + 1));
        int n8 = n6 == 0 ? 0 : (n4 - n7 - 2) * n5 / n6;
        RunicRefinedClassicUi.fill(class_3322, n + 1, n2 + 1 + n8, n3 - 2, n7, n6 == 0 ? -10656658 : -6059838);
    }

    private static void emitRing(class_332 class_3322, int n, int n2, float f, int n3, int n4) {
        if (f < 0.0f || f >= 1.0f) {
            return;
        }
        double d = 14.0f + 44.0f * f;
        for (int i = 0; i < 20; ++i) {
            double d2 = (double)i * 0.31415926535 + (double)f * 5.7 * (double)n3;
            int n5 = n + (int)Math.round(Math.cos(d2) * (d + (double)(i % 4)));
            int n6 = n2 + (int)Math.round(Math.sin(d2) * (d + (double)(i % 4)));
            RunicRefinedClassicUi.fill(class_3322, n5, n6, 2, 2, i % 5 == 0 ? -992853 : n4);
        }
    }

    private static void drawFX(Object object, class_332 class_3322, int n, int n2) {
        Object t = RunicRefinedClassicUi.call("state", object);
        State state = RunicRefinedClassicUi.st(object);
        long l = RunicRefinedClassicUi.lng(RunicRefinedClassicUi.get(t, "pulseStart"));
        long l2 = System.nanoTime();
        if (l > 0L && l != state.lastPulseStart) {
            state.lastPulseStart = l;
            state.pulses.addLast(new Pulse(l, String.valueOf(RunicRefinedClassicUi.get(t, "pulseKind"))));
            while (state.pulses.size() > 32) {
                state.pulses.removeFirst();
            }
        }
        while (!state.pulses.isEmpty() && l2 - state.pulses.peekFirst().start > 1350000000L) {
            state.pulses.removeFirst();
        }
        for (Pulse pulse : state.pulses) {
            if (pulse.start > l2) continue;
            float f = Math.max(0.0f, Math.min(1.0f, (float)(l2 - pulse.start) / 1.35E9f));
            String string = pulse.kind.toLowerCase(Locale.ROOT);
            int n3 = string.contains("unravel") ? -2244987 : -4813598;
            int n4 = n + 124;
            int n5 = n + 547;
            int n6 = n2 + 153;
            if (string.contains("trans")) {
                RunicRefinedClassicUi.emitRing(class_3322, n4, n6, f / 0.33f, 1, n3);
                float f2 = (f - 0.18f) / 0.58f;
                if (f2 >= 0.0f && f2 <= 1.14f) {
                    for (int i = 0; i < 20; ++i) {
                        float f3 = f2 - (float)i * 0.017f;
                        if (f3 < 0.0f || f3 > 1.0f) continue;
                        int n7 = n4 + (int)(423.0f * f3);
                        int n8 = (int)(7.0 * Math.sin((double)f3 * 11.5 + (double)i * 0.48));
                        RunicRefinedClassicUi.fill(class_3322, n7, n6 + n8, 2, 2, i % 4 == 0 ? -665692 : n3);
                    }
                }
                RunicRefinedClassicUi.emitRing(class_3322, n5, n6, (f - 0.64f) / 0.36f, -1, -2049808);
                continue;
            }
            RunicRefinedClassicUi.emitRing(class_3322, n4, n6, f, string.contains("unravel") ? -1 : 1, n3);
        }
    }

    private static void drawInfoPopup(Object object, class_332 class_3322, class_327 class_3272, int n, int n2) {
        Object t = RunicRefinedClassicUi.call("state", object);
        String string = String.valueOf(RunicRefinedClassicUi.get(t, "infoEnchant"));
        if (string == null || string.equals("null") || string.isEmpty()) {
            return;
        }
        String string2 = String.valueOf(RunicRefinedClassicUi.get(t, "infoTitle"));
        String string3 = String.valueOf(RunicRefinedClassicUi.get(t, "infoDescription"));
        if (string3.equals("null") || string3.isBlank()) {
            string3 = "No description is available for this enchantment.";
        }
        int n3 = n + 243;
        int n4 = n2 + 98;
        int n5 = 394;
        int n6 = 167;
        RunicRefinedClassicUi.fill(class_3322, n3 + 3, n4 + 3, n5, n6, -1157627904);
        RunicRefinedClassicUi.fill(class_3322, n3, n4, n5, n6, -15525600);
        RunicRefinedClassicUi.border(class_3322, n3, n4, n5, n6, -6519718);
        RunicRefinedClassicUi.fill(class_3322, n3 + 4, n4 + 4, n5 - 8, 23, -14341581);
        RunicRefinedClassicUi.border(class_3322, n3 + 4, n4 + 4, n5 - 8, 23, -9937811);
        RunicRefinedClassicUi.text(class_3322, class_3272, RunicRefinedClassicUi.fitPixels(class_3272, string2, n5 - 58), n3 + 13, n4 + 12, -3563153);
        RunicRefinedClassicUi.button(class_3322, class_3272, "X", n3 + n5 - 26, n4 + 8, 18, 15, true, RunicRefinedClassicUi.hover(object, 243 + n5 - 26, 106, 18, 15), -5664921);
        int n7 = n4 + 41;
        int n8 = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for (String string4 : string3.replace('\n', ' ').split(" +")) {
            String string5;
            if (string4.isBlank()) continue;
            String string6 = string5 = stringBuilder.length() == 0 ? string4 : String.valueOf(stringBuilder) + " " + string4;
            if (RunicRefinedClassicUi.width(class_3272, string5) > n5 - 30 && stringBuilder.length() > 0) {
                RunicRefinedClassicUi.text(class_3322, class_3272, stringBuilder.toString(), n3 + 14, n7, -2565411);
                n7 += 15;
                ++n8;
                stringBuilder.setLength(0);
            }
            if (RunicRefinedClassicUi.width(class_3272, string4) > n5 - 30) {
                string4 = RunicRefinedClassicUi.fitPixels(class_3272, string4, n5 - 30);
            }
            if (stringBuilder.length() > 0) {
                stringBuilder.append(' ');
            }
            stringBuilder.append(string4);
            if (n8 >= 7) break;
        }
        if (n8 < 8 && stringBuilder.length() > 0) {
            RunicRefinedClassicUi.text(class_3322, class_3272, stringBuilder.toString(), n3 + 14, n7, -2565411);
        }
    }

    private static int oldClick(Object object, double d, double d2, int n) {
        return (Integer)RunicRefinedClassicUi.call("classicLegacyClick", object, (double)RunicRefinedClassicUi.x(object) + d, (double)RunicRefinedClassicUi.y(object) + d2, n);
    }

    public static int click(Object object, double d, double d2, int n) {
        int n2;
        double d3;
        if (!RunicGrimoireUi.active(object)) {
            return -1000;
        }
        int n3 = RunicRefinedClassicUi.ax(object);
        double d4 = d - (double)n3;
        if (!RunicRefinedClassicUi.inside(d4, d3 = d2 - (double)(n2 = RunicRefinedClassicUi.ay(object)), 0, 0, 672, 390)) {
            return -1000;
        }
        Object t = RunicRefinedClassicUi.call("state", object);
        Object object2 = RunicRefinedClassicUi.get(t, "infoEnchant");
        if (object2 != null && !String.valueOf(object2).isEmpty()) {
            RunicRefinedClassicUi.set(t, "infoEnchant", "");
            return -20;
        }
        if (RunicRefinedClassicUi.inside(d4, d3, 254, 269, 164, 86)) {
            return -10;
        }
        if (RunicRefinedClassicUi.inside(d4, d3, 115, 144, 18, 18) || RunicRefinedClassicUi.trans(object) && RunicRefinedClassicUi.inside(d4, d3, 538, 144, 18, 18)) {
            return -10;
        }
        if (!RunicRefinedClassicUi.trans(object) && RunicRefinedClassicUi.inside(d4, d3, 457, 99, 179, 22)) {
            return -10;
        }
        if (n != 0) {
            return -20;
        }
        try {
            if (RunicRefinedClassicUi.inside(d4, d3, 378, 25, 129, 28)) {
                if (!RunicRefinedClassicUi.trans(object)) {
                    return -20;
                }
                int n4 = RunicRefinedClassicUi.oldClick(object, 360.0, 15.0, n);
                RunicRefinedClassicUi.switchFx(object);
                return n4;
            }
            if (RunicRefinedClassicUi.inside(d4, d3, 515, 25, 130, 28)) {
                if (RunicRefinedClassicUi.trans(object)) {
                    return -20;
                }
                int n5 = RunicRefinedClassicUi.oldClick(object, 500.0, 15.0, n);
                RunicRefinedClassicUi.switchFx(object);
                return n5;
            }
            if (RunicRefinedClassicUi.trans(object)) {
                int n6;
                int n7;
                List<?> list = RunicRefinedClassicUi.transferIds(object);
                Object t2 = RunicRefinedClassicUi.call("state", object);
                if (RunicRefinedClassicUi.inside(d4, d3, 248, 216, 83, 15)) {
                    return RunicRefinedClassicUi.oldClick(object, 260.0, 80.0, n);
                }
                if (RunicRefinedClassicUi.inside(d4, d3, 336, 216, 83, 15)) {
                    return RunicRefinedClassicUi.oldClick(object, 340.0, 80.0, n);
                }
                if (RunicRefinedClassicUi.inside(d4, d3, 248, 234, 171, 15)) {
                    return RunicRefinedClassicUi.oldClick(object, 280.0, 225.0, n);
                }
                if (RunicRefinedClassicUi.inside(d4, d3, 40, 243, 160, 104)) {
                    n7 = (int)((d3 - 244.0) / 17.0);
                    n6 = RunicRefinedClassicUi.num(RunicRefinedClassicUi.get(t2, "sourceScroll"));
                    int n8 = n6 + n7;
                    if (n7 >= 0 && n8 < list.size()) {
                        Object obj = list.get(n8);
                        if (d4 >= 166.0 && d4 < 182.0) {
                            RunicRefinedClassicUi.call("openInfo", object, obj);
                            return -20;
                        }
                        Set<?> set = RunicRefinedClassicUi.selectedTransfer(object);
                        Set<?> set2 = RunicRefinedClassicUi.selectedCurses(object);
                        return RunicRefinedClassicUi.oldClick(object, 25.0, 244 + n7 * 15 + 3, n);
                    }
                }
                if (RunicRefinedClassicUi.inside(d4, d3, 204, 244, 6, 100)) {
                    n7 = Math.max(0, list.size() - 6);
                    n6 = Math.min(n7, Math.max(0, (int)((d3 - 244.0) * (double)n7 / 100.0)));
                    RunicRefinedClassicUi.set(t2, "sourceScroll", n6);
                    return -20;
                }
                return -20;
            }
            if (RunicRefinedClassicUi.inside(d4, d3, 248, 221, 83, 20)) {
                return RunicRefinedClassicUi.oldClick(object, 60.0, 329.0, n);
            }
            if (RunicRefinedClassicUi.inside(d4, d3, 336, 221, 83, 20)) {
                return RunicRefinedClassicUi.oldClick(object, 152.0, 329.0, n);
            }
            if (RunicRefinedClassicUi.inside(d4, d3, 453, 125, 187, 16)) {
                int n9 = Math.min(3, (int)((d4 - 453.0) / 47.0));
                RunicRefinedClassicUi.set(object, "tierFilter", n9);
                RunicRefinedClassicUi.set(object, "scrollOffset", 0);
                RunicRefinedClassicUi.call("call", object, "keepSelectionValid", new Object[0]);
                return -20;
            }
            List<?> list = RunicRefinedClassicUi.all(object);
            int n10 = Math.max(0, list.size() - 11);
            int n11 = Math.max(0, Math.min(n10, RunicRefinedClassicUi.num(RunicRefinedClassicUi.get(object, "scrollOffset"))));
            if (RunicRefinedClassicUi.inside(d4, d3, 636, 150, 6, 185)) {
                int n12 = n10 == 0 ? 0 : Math.min(n10, Math.max(0, (int)((d3 - 150.0) * (double)n10 / 185.0)));
                RunicRefinedClassicUi.set(object, "scrollOffset", n12);
                return -20;
            }
            if (RunicRefinedClassicUi.inside(d4, d3, 454, 148, 182, 187)) {
                int n13 = (int)((d3 - 149.0) / 17.0);
                int n14 = n11 + n13;
                if (n14 >= 0 && n14 < list.size()) {
                    Object obj = list.get(n14);
                    if (d4 >= 594.0 && d4 < 611.0) {
                        RunicRefinedClassicUi.call("openInfo", object, obj);
                        return -20;
                    }
                    RunicRefinedClassicUi.set(object, "selectedEnchantment", obj);
                    RunicRefinedClassicUi.set(RunicRefinedClassicUi.call("state", object), "costScroll", 0);
                }
                return -20;
            }
            if (RunicRefinedClassicUi.inside(d4, d3, 40, 244, 166, 103)) {
                Map<?, Integer> map = RunicRefinedClassicUi.enchants(RunicRefinedClassicUi.equipment(object));
                int n15 = RunicRefinedClassicUi.num(RunicRefinedClassicUi.get(RunicRefinedClassicUi.call("state", object), "installedScroll"));
                int n16 = (int)((d3 - 244.0) / 17.0) + n15;
                int n17 = 0;
                for (Object obj : map.keySet()) {
                    if (n17++ != n16) continue;
                    RunicRefinedClassicUi.set(object, "selectedEnchantment", obj);
                    return -20;
                }
            }
            return -20;
        }
        catch (Throwable throwable) {
            RunicRefinedClassicUi.error(object, "click", throwable);
            return -20;
        }
    }

    public static boolean mouseScroll(Object object, double d, double d2, double d3) {
        int n;
        if (!RunicGrimoireUi.active(object)) {
            return false;
        }
        int n2 = RunicRefinedClassicUi.ax(object);
        int n3 = RunicRefinedClassicUi.ay(object);
        double d4 = d - (double)n2;
        double d5 = d2 - (double)n3;
        if (d3 == 0.0) {
            return RunicRefinedClassicUi.inside(d4, d5, 0, 0, 672, 390);
        }
        int n4 = n = d3 < 0.0 ? 1 : -1;
        if (RunicRefinedClassicUi.trans(object) && RunicRefinedClassicUi.inside(d4, d5, 35, 230, 188, 128)) {
            Object t = RunicRefinedClassicUi.call("state", object);
            int n5 = Math.max(0, RunicRefinedClassicUi.transferIds(object).size() - 6);
            RunicRefinedClassicUi.set(t, "sourceScroll", Math.min(n5, Math.max(0, RunicRefinedClassicUi.num(RunicRefinedClassicUi.get(t, "sourceScroll")) + n)));
            return true;
        }
        if (!RunicRefinedClassicUi.trans(object) && RunicRefinedClassicUi.inside(d4, d5, 35, 230, 188, 128)) {
            Object t = RunicRefinedClassicUi.call("state", object);
            int n6 = Math.max(0, RunicRefinedClassicUi.enchants(RunicRefinedClassicUi.equipment(object)).size() - 6);
            RunicRefinedClassicUi.set(t, "installedScroll", Math.min(n6, Math.max(0, RunicRefinedClassicUi.num(RunicRefinedClassicUi.get(t, "installedScroll")) + n)));
            return true;
        }
        if (RunicRefinedClassicUi.inside(d4, d5, 249, 135, 178, 62)) {
            Object t = RunicRefinedClassicUi.call("state", object);
            int n7 = RunicRefinedClassicUi.costs(object, RunicRefinedClassicUi.trans(object)).size();
            int n8 = Math.max(0, n7 - 8);
            RunicRefinedClassicUi.set(t, "costScroll", Math.min(n8, Math.max(0, RunicRefinedClassicUi.num(RunicRefinedClassicUi.get(t, "costScroll")) + n)));
            return true;
        }
        if (!RunicRefinedClassicUi.trans(object) && RunicRefinedClassicUi.inside(d4, d5, 450, 142, 198, 210)) {
            int n9 = Math.max(0, RunicRefinedClassicUi.all(object).size() - 11);
            RunicRefinedClassicUi.set(object, "scrollOffset", Math.min(n9, Math.max(0, RunicRefinedClassicUi.num(RunicRefinedClassicUi.get(object, "scrollOffset")) + n * 2)));
            return true;
        }
        return RunicRefinedClassicUi.inside(d4, d5, 0, 0, 672, 390);
    }

    public static boolean insideScreen(Object object, double d, double d2) {
        return RunicGrimoireUi.active(object) && RunicRefinedClassicUi.inside(d, d2, RunicRefinedClassicUi.ax(object), RunicRefinedClassicUi.ay(object), 672, 390);
    }

    static {
        RUNE_OFF = new class_2960("runic_attunement", "textures/gui/classic_rune_dormant.png");
        RUNE_ON = new class_2960("runic_attunement", "textures/gui/classic_rune_active.png");
    }

    private static int costRowInset(List list, int n, int n2) {
        return (4 - Math.min(4, Math.max(0, list.size() - n - n2 / 4 * 4))) * 37 / 2;
    }

    private static final class State {
        int mouseX;
        int mouseY;
        int prevX = -9999;
        int prevY = -9999;
        long opened;
        long lastPulseStart;
        long tabFxStart;
        boolean warned;
        final ArrayDeque<Pulse> pulses = new ArrayDeque();
        final Set<String> failedSections = new HashSet<String>();

        private State() {
        }
    }

    private static final class Pulse {
        final long start;
        final String kind;

        Pulse(long l, String string) {
            this.start = l;
            this.kind = string;
        }
    }
}

