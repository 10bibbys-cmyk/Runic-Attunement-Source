/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1799
 *  net.minecraft.class_2561
 *  net.minecraft.class_2583
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_327
 *  net.minecraft.class_332
 *  net.minecraft.class_4587
 *  net.minecraft.class_4597
 *  net.minecraft.class_5250
 *  net.minecraft.class_811
 *  net.minecraft.class_918
 */
package dev.runicattunement.grimoire;

import dev.runicattunement.grimoire.GrimoireEmiGuard;
import dev.runicattunement.grimoire.RunicRefinedClassicUi;
import dev.runicattunement.unravel.RunicUnravelRuntime;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.class_1799;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_5250;
import net.minecraft.class_811;
import net.minecraft.class_918;

public final class RunicGrimoireUi {
    public static final int W = 640;
    public static final int H = 366;
    public static final int MIN_W = 678;
    public static final int MIN_H = 398;
    private static final int LEATHER = -266525907;
    private static final int PAGE = -181718725;
    private static final int PAGE_DEEP = -182376141;
    private static final int EDGE = -9150399;
    private static final int GOLD = -1721994;
    private static final int SOFT_GOLD = -4021647;
    private static final int TEXT = -924215;
    private static final int MUTED = -5986902;
    private static final int VIOLET = -5727764;
    private static final int SILVER = -3683367;
    private static final class_2960 RUNIC_FONT = new class_2960("minecraft", "alt");
    private static final class_2960 STONE_ATTUNEMENT = new class_2960("runic_attunement", "textures/gui/stone_attunement.png");
    private static final class_2960 STONE_TRANSMUTATION = new class_2960("runic_attunement", "textures/gui/stone_transmutation.png");
    private static final class_2960 SEAL_DORMANT = new class_2960("runic_attunement", "textures/gui/stone_sigil_dormant.png");
    private static final class_2960 SEAL_ACTIVE = new class_2960("runic_attunement", "textures/gui/stone_sigil_active.png");
    private static final String RUNIC_SIGILS = "abcdefghiklmnopqrstuvwxyz";
    private static final class_5250[] CACHED_SIGILS = RunicGrimoireUi.makeRunicSigils();
    private static final int CYAN = -8073253;
    private static final int GREEN = -7222118;
    private static final int RED = -2518133;
    private static final int ENCHANT_ROWS = 11;
    private static final int TRANS_ROWS = 6;
    private static final int INSTALLED_ROWS = 9;
    private static boolean warned = false;
    private static volatile long lastRefundConfigCheck = 0L;
    private static volatile int cachedRefundXpPercent = 50;
    private static final Map<Object, State> STATES = Collections.synchronizedMap(new WeakHashMap());
    private static volatile Boolean etAvailable = null;

    private static class_5250[] makeRunicSigils() {
        class_5250[] class_5250Array = new class_5250[RUNIC_SIGILS.length()];
        class_2583 class_25832 = class_2583.field_24360.method_27704(RUNIC_FONT);
        for (int i = 0; i < class_5250Array.length; ++i) {
            class_5250Array[i] = class_2561.method_43470((String)String.valueOf(RUNIC_SIGILS.charAt(i))).method_10862(class_25832);
        }
        return class_5250Array;
    }

    private RunicGrimoireUi() {
    }

    private static void warn(Throwable throwable) {
        if (!warned) {
            warned = true;
            System.err.println("[Runic Grimoire] Render/interaction error: " + String.valueOf(throwable));
            throwable.printStackTrace(System.err);
        }
    }

    private static Object get(Object object, String string) {
        return RunicUnravelRuntime.accessField(object, string);
    }

    private static Object call(Object object, String string, Object ... objectArray) {
        return RunicUnravelRuntime.invoke(object, string, objectArray);
    }

    private static int num(Object object) {
        return object instanceof Number ? ((Number)object).intValue() : 0;
    }

    private static boolean yes(Object object) {
        return Boolean.TRUE.equals(object);
    }

    private static State state(Object object2) {
        return STATES.computeIfAbsent(object2, object -> new State());
    }

    private static Object handler(Object object) {
        return RunicGrimoireUi.get(object, "field_2797");
    }

    private static int screenW(Object object) {
        return RunicGrimoireUi.num(RunicGrimoireUi.get(object, "field_22789"));
    }

    private static int screenH(Object object) {
        return RunicGrimoireUi.num(RunicGrimoireUi.get(object, "field_22790"));
    }

    private static int x(Object object) {
        return (RunicGrimoireUi.screenW(object) - 640) / 2;
    }

    private static int y(Object object) {
        return (RunicGrimoireUi.screenH(object) - 366) / 2;
    }

    private static boolean trans(Object object) {
        return RunicGrimoireUi.yes(RunicGrimoireUi.call(RunicGrimoireUi.handler(object), "isTransmutationMode", new Object[0]));
    }

    public static boolean active(Object object) {
        return object != null && RunicGrimoireUi.screenW(object) >= 678 && RunicGrimoireUi.screenH(object) >= 398 && !RunicGrimoireUi.state((Object)object).classic;
    }

    private static void set(Object object, String string, Object object2) {
        if (object == null) {
            return;
        }
        for (Class<?> clazz = object.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            try {
                Field field = clazz.getDeclaredField(string);
                field.setAccessible(true);
                field.set(object, object2);
                return;
            }
            catch (NoSuchFieldException noSuchFieldException) {
                continue;
            }
            catch (Throwable throwable) {
                throw new IllegalStateException("Writing " + string, throwable);
            }
        }
    }

    private static void setI(Object object, String string, int n) {
        RunicGrimoireUi.set(object, string, n);
    }

    private static boolean in(double d, double d2, int n, int n2, int n3, int n4) {
        return d >= (double)n && d < (double)(n + n3) && d2 >= (double)n2 && d2 < (double)(n2 + n4);
    }

    private static List<?> slots(Object object) {
        Object object2 = RunicGrimoireUi.get(RunicGrimoireUi.handler(object), "field_7761");
        return object2 instanceof List ? (List)object2 : Collections.emptyList();
    }

    public static void restore(Object object) {
        Object object2;
        GrimoireEmiGuard.exit();
        State state = RunicGrimoireUi.state(object);
        if (state.relaid && state.originalSlots != null) {
            object2 = RunicGrimoireUi.slots(object);
            for (int i = 0; i < Math.min(object2.size(), state.originalSlots.size()); ++i) {
                int[] nArray = state.originalSlots.get(i);
                RunicGrimoireUi.setI(object2.get(i), "field_7873", nArray[0]);
                RunicGrimoireUi.setI(object2.get(i), "field_7872", nArray[1]);
            }
            state.relaid = false;
            state.prevX = Integer.MIN_VALUE;
        }
        if (state.search != null) {
            object2 = RunicGrimoireUi.get(object, "searchField");
            if (object2 == state.search) {
                RunicGrimoireUi.call(object2, "method_46421", state.searchX);
                RunicGrimoireUi.call(object2, "method_46419", state.searchY);
                RunicGrimoireUi.setI(object2, "field_22758", state.searchW);
            }
            state.search = null;
        }
    }

    public static void prepare(Object object) {
        Object object2;
        if (!RunicGrimoireUi.active(object)) {
            RunicGrimoireUi.restore(object);
            RunicRefinedClassicUi.relayout(object);
            return;
        }
        GrimoireEmiGuard.enter();
        State state = RunicGrimoireUi.state(object);
        int n = RunicGrimoireUi.x(object);
        int n2 = RunicGrimoireUi.y(object);
        int n3 = RunicGrimoireUi.num(RunicGrimoireUi.get(object, "field_2776"));
        int n4 = RunicGrimoireUi.num(RunicGrimoireUi.get(object, "field_2800"));
        List<?> list = RunicGrimoireUi.slots(object);
        boolean bl = RunicGrimoireUi.trans(object);
        if (!state.relaid) {
            state.originalSlots = new ArrayList<int[]>(list.size());
            object2 = list.iterator();
            while (object2.hasNext()) {
                Object obj = object2.next();
                state.originalSlots.add(new int[]{RunicGrimoireUi.num(RunicGrimoireUi.get(obj, "field_7873")), RunicGrimoireUi.num(RunicGrimoireUi.get(obj, "field_7872"))});
            }
            state.relaid = true;
        }
        if (state.prevX != n || state.prevY != n2 || state.prevOx != n3 || state.prevOy != n4 || !state.lastItem.equals(bl ? "trans" : "attune")) {
            for (int i = 0; i < list.size(); ++i) {
                int n5;
                int n6;
                if (i == 0) {
                    n6 = n + 98;
                    n5 = n2 + 196;
                } else if (i == 1) {
                    n6 = bl ? n + 524 : -4000;
                    n5 = bl ? n2 + 196 : -4000;
                } else if (i < 29) {
                    var11_13 = i - 2;
                    n6 = n + 233 + var11_13 % 9 * 18;
                    n5 = n2 + 271 + var11_13 / 9 * 18;
                } else {
                    var11_13 = i - 29;
                    n6 = n + 233 + var11_13 * 18;
                    n5 = n2 + 329;
                }
                RunicGrimoireUi.setI(list.get(i), "field_7873", n6 - n3);
                RunicGrimoireUi.setI(list.get(i), "field_7872", n5 - n4);
            }
            state.lastItem = bl ? "trans" : "attune";
            state.prevX = n;
            state.prevY = n2;
            state.prevOx = n3;
            state.prevOy = n4;
        }
        if ((object2 = RunicGrimoireUi.get(object, "searchField")) != null) {
            if (state.search != object2) {
                state.search = object2;
                state.searchX = RunicGrimoireUi.num(RunicGrimoireUi.call(object2, "method_46426", new Object[0]));
                state.searchY = RunicGrimoireUi.num(RunicGrimoireUi.call(object2, "method_46427", new Object[0]));
                state.searchW = RunicGrimoireUi.num(RunicGrimoireUi.get(object2, "field_22758"));
            }
            RunicGrimoireUi.call(object2, "method_46421", n + 445);
            RunicGrimoireUi.call(object2, "method_46419", n2 + 91);
            RunicGrimoireUi.setI(object2, "field_22758", 176);
        }
        RunicRefinedClassicUi.relayout(object);
    }

    private static void rect(class_332 class_3322, int n, int n2, int n3, int n4, int n5) {
        class_3322.method_25294(n, n2, n + n3, n2 + n4, n5);
    }

    private static void frame(class_332 class_3322, int n, int n2, int n3, int n4, int n5) {
        class_3322.method_49601(n, n2, n3, n4, n5);
    }

    private static class_327 font(Object object) {
        return (class_327)RunicGrimoireUi.get(object, "field_22793");
    }

    private static void text(class_332 class_3322, class_327 class_3272, String string, int n, int n2, int n3) {
        class_3322.method_51433(class_3272, string, n, n2, n3, false);
    }

    private static String truncate(String string, int n) {
        return string.length() > n ? string.substring(0, Math.max(0, n - 2)) + ".." : string;
    }

    private static String title(Object object) {
        if (object == null) {
            return "Choose an inscription";
        }
        String string = String.valueOf(object);
        string = string.contains(":") ? string.substring(string.indexOf(58) + 1) : string;
        StringBuilder stringBuilder = new StringBuilder();
        boolean bl = true;
        for (char c : string.toCharArray()) {
            if (c == '_' || c == '-' || c == '/') {
                stringBuilder.append(' ');
                bl = true;
                continue;
            }
            stringBuilder.append(bl ? Character.toUpperCase(c) : c);
            bl = false;
        }
        return stringBuilder.toString();
    }

    private static String roman(int n) {
        if (n < 1) {
            return "-";
        }
        if (n > 20) {
            return "" + n;
        }
        StringBuilder stringBuilder = new StringBuilder();
        int[] nArray = new int[]{10, 9, 5, 4, 1};
        String[] stringArray = new String[]{"X", "IX", "V", "IV", "I"};
        for (int i = 0; i < 5; ++i) {
            while (n >= nArray[i]) {
                n -= nArray[i];
                stringBuilder.append(stringArray[i]);
            }
        }
        return stringBuilder.toString();
    }

    private static Object current(Object object) {
        return RunicGrimoireUi.get(object, "selectedEnchantment");
    }

    private static Object equipment(Object object) {
        return RunicGrimoireUi.call(RunicGrimoireUi.handler(object), "getEquipmentStack", new Object[0]);
    }

    private static Object source(Object object) {
        return RunicGrimoireUi.call(RunicGrimoireUi.handler(object), "getSourceStack", new Object[0]);
    }

    private static Object target(Object object) {
        return RunicGrimoireUi.call(RunicGrimoireUi.handler(object), "getTargetStack", new Object[0]);
    }

    private static boolean empty(Object object) {
        return object == null || RunicGrimoireUi.yes(RunicGrimoireUi.call(object, "method_7960", new Object[0]));
    }

    private static List<?> filtered(Object object) {
        Object object2 = RunicGrimoireUi.call(object, "filteredEnchantments", new Object[0]);
        return object2 instanceof List ? (List)object2 : List.of();
    }

    private static LinkedHashMap<Object, Integer> enchants(Object object) {
        LinkedHashMap<Object, Integer> linkedHashMap = new LinkedHashMap<Object, Integer>();
        if (RunicGrimoireUi.empty(object)) {
            return linkedHashMap;
        }
        try {
            Object object2 = RunicGrimoireUi.call(Class.forName("net.minecraft.class_1890"), "method_8222", object);
            Object object3 = RunicGrimoireUi.get(Class.forName("net.minecraft.class_7923"), "field_41176");
            if (object2 instanceof Map) {
                for (Map.Entry entry : ((Map)object2).entrySet()) {
                    Object object4 = RunicGrimoireUi.call(object3, "method_10221", entry.getKey());
                    if (object4 == null) continue;
                    linkedHashMap.put(object4, RunicGrimoireUi.num(entry.getValue()));
                }
            }
        }
        catch (Throwable throwable) {
            RunicGrimoireUi.warn(throwable);
        }
        return linkedHashMap;
    }

    private static LinkedHashMap<Object, Integer> installed(Object object) {
        return RunicGrimoireUi.enchants(RunicGrimoireUi.equipment(object));
    }

    private static int level(Object object) {
        return RunicGrimoireUi.installed(object).getOrDefault(RunicGrimoireUi.current(object), 0);
    }

    private static List<?> costs(Object object, boolean bl) {
        Object object2 = RunicGrimoireUi.call(object, bl ? "currentTransmutationCosts" : "currentAttunementCosts", new Object[0]);
        return object2 instanceof List ? (List)object2 : List.of();
    }

    private static int xp(Object object, boolean bl) {
        return RunicGrimoireUi.num(RunicGrimoireUi.call(object, bl ? "currentTransmutationExperienceCost" : "currentAttunementExperienceCost", new Object[0]));
    }

    private static boolean allowed(Object object, boolean bl) {
        return RunicGrimoireUi.yes(RunicGrimoireUi.call(RunicGrimoireUi.call(object, bl ? "transmutationEligibility" : "eligibility", new Object[0]), "allowed", new Object[0]));
    }

    private static List<?> transferIds(Object object) {
        Object object2 = RunicGrimoireUi.call(ClassForTransmutation.INSTANCE, "sourceEnchantments", RunicGrimoireUi.source(object));
        return object2 instanceof List ? (List)object2 : List.of();
    }

    private static Set<Object> transfers(Object object) {
        Object object2 = RunicGrimoireUi.get(object, "selectedTransfers");
        return object2 instanceof Set ? (Set)object2 : Collections.emptySet();
    }

    private static Set<Object> curses(Object object) {
        Object object2 = RunicGrimoireUi.get(object, "removeCurses");
        return object2 instanceof Set ? (Set)object2 : Collections.emptySet();
    }

    private static boolean isCurse(Object object) {
        try {
            Object object2 = RunicGrimoireUi.get(Class.forName("net.minecraft.class_7923"), "field_41176");
            Object object3 = RunicGrimoireUi.call(object2, "method_17966", object);
            Object var3_4 = object3 instanceof Optional ? ((Optional)object3).orElse(null) : null;
            return var3_4 != null && RunicGrimoireUi.yes(RunicGrimoireUi.call(var3_4, "method_8195", new Object[0]));
        }
        catch (Throwable throwable) {
            return false;
        }
    }

    private static boolean transferable(Object object, Object object2) {
        try {
            if (RunicGrimoireUi.isCurse(object2)) {
                return RunicGrimoireUi.yes(RunicGrimoireUi.call(ClassForTransmutation.INSTANCE, "curseCanTransfer", object2, RunicGrimoireUi.source(object), RunicGrimoireUi.target(object), RunicGrimoireUi.transfers(object)));
            }
            Object object3 = RunicGrimoireUi.call(ClassForTransmutation.INSTANCE, "normalStatus", object2, RunicGrimoireUi.source(object), RunicGrimoireUi.target(object), RunicGrimoireUi.call(RunicGrimoireUi.handler(object), "getDefinitions", new Object[0]), RunicGrimoireUi.transfers(object), RunicGrimoireUi.curses(object));
            return object3 != null && String.valueOf(object3).equals("TRANSFERABLE");
        }
        catch (Throwable throwable) {
            return false;
        }
    }

    private static int index(Object object, Object object2) {
        return RunicGrimoireUi.num(RunicGrimoireUi.call(RunicGrimoireUi.handler(object), "indexOf", object2));
    }

    private static void startCharge(Object object, String string) {
    }

    private static boolean charging(Object object) {
        return false;
    }

    private static float progress(Object object) {
        State state = RunicGrimoireUi.state(object);
        long l = state.chargeEnd - state.chargeStart;
        if (l <= 0L || !RunicGrimoireUi.charging(object)) {
            return 0.0f;
        }
        return Math.min(1.0f, Math.max(0.0f, (float)(System.nanoTime() - state.chargeStart) / (float)l));
    }

    private static void panel(class_332 class_3322, int n, int n2, int n3, int n4, int n5) {
    }

    private static void line(class_332 class_3322, int n, int n2, int n3) {
        RunicGrimoireUi.rect(class_3322, n, n2, n3, 1, -1636868538);
        RunicGrimoireUi.rect(class_3322, n + 5, n2 + 2, Math.max(0, n3 - 10), 1, 1181703750);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void bigText(class_332 class_3322, class_327 class_3272, String string, int n, int n2, int n3, float f) {
        class_4587 class_45872 = class_3322.method_51448();
        class_45872.method_22903();
        try {
            int n4 = RunicGrimoireUi.num(RunicGrimoireUi.call(class_3272, "method_1727", string));
            class_45872.method_22904((double)n - (double)((float)n4 * f) / 2.0, (double)n2, 0.0);
            class_45872.method_22905(f, f, 1.0f);
            RunicGrimoireUi.text(class_3322, class_3272, string, 0, 0, n3);
        }
        finally {
            class_45872.method_22909();
        }
    }

    private static void plaque(class_332 class_3322, class_327 class_3272, String string, int n, int n2, int n3) {
        RunicGrimoireUi.bigText(class_3322, class_3272, string, n + n3 / 2, n2 + 6, -4021647, 1.12f);
    }

    private static void button(class_332 class_3322, class_327 class_3272, String string, int n, int n2, int n3, int n4, boolean bl, boolean bl2, int n5) {
        int n6 = !bl ? -13750215 : (bl2 ? -11185828 : -13092027);
        RunicGrimoireUi.rect(class_3322, n + 2, n2 + 2, n3, n4, -1341782259);
        RunicGrimoireUi.rect(class_3322, n, n2, n3, n4, n6);
        RunicGrimoireUi.frame(class_3322, n, n2, n3, n4, bl ? n5 : -9145990);
        RunicGrimoireUi.rect(class_3322, n + 2, n2 + 2, n3 - 4, 1, bl ? -6979998 : -11251110);
        RunicGrimoireUi.rect(class_3322, n + 2, n2 + n4 - 3, n3 - 4, 1, -15065819);
        if (bl) {
            RunicGrimoireUi.rect(class_3322, n + 5, n2 + 4, 1, 1, n5);
            RunicGrimoireUi.rect(class_3322, n + n3 - 6, n2 + n4 - 5, 1, 1, n5);
        }
        int n7 = RunicGrimoireUi.num(RunicGrimoireUi.call(class_3272, "method_1727", string));
        RunicGrimoireUi.text(class_3322, class_3272, string, n + Math.max(3, (n3 - n7) / 2), n2 + Math.max(3, (n4 - 8) / 2), bl ? -924215 : -5986902);
    }

    private static void attuneButton(class_332 class_3322, class_327 class_3272, int n, int n2, int n3, int n4, boolean bl, boolean bl2, int n5) {
        int n6 = !bl ? -13750215 : (bl2 ? -11185828 : -13092027);
        RunicGrimoireUi.rect(class_3322, n + 1, n2 + 2, n3, n4, 2013855755);
        RunicGrimoireUi.rect(class_3322, n, n2, n3, n4, n6);
        RunicGrimoireUi.frame(class_3322, n, n2, n3, n4, bl ? -1721994 : -10066325);
        if (bl) {
            RunicGrimoireUi.rect(class_3322, n + 3, n2 + 2, n3 - 6, 1, -1864712032);
        }
        String string = "ATTUNE";
        String string2 = n5 + " XP";
        int n7 = RunicGrimoireUi.num(RunicGrimoireUi.call(class_3272, "method_1727", string));
        int n8 = RunicGrimoireUi.num(RunicGrimoireUi.call(class_3272, "method_1727", string2));
        RunicGrimoireUi.text(class_3322, class_3272, string, n + (n3 - n7) / 2, n2 + 4, bl ? -924215 : -5986902);
        RunicGrimoireUi.text(class_3322, class_3272, string2, n + (n3 - n8) / 2, n2 + 16, bl ? -4021647 : -5986902);
    }

    private static void runeCircle(Object object, class_332 class_3322, class_327 class_3272, int n, int n2, int n3, boolean bl, int n4, boolean bl2) {
        int n5;
        int n6;
        boolean bl3;
        State state = RunicGrimoireUi.state(object);
        class_3322.method_25290(bl ? SEAL_ACTIVE : SEAL_DORMANT, n - 56, n2 - 56, 0.0f, 0.0f, 112, 112, 112, 112);
        double d = (double)System.nanoTime() / 1.0E9;
        double d2 = (double)(System.nanoTime() - state.pulseStart) / 1.0E9;
        boolean bl4 = bl3 = bl && d2 >= 0.0 && d2 < 1.3;
        double d3 = bl ? d * (bl3 ? 1.1 : 0.25) : d * 0.035;
        for (n6 = 0; n6 < 48; ++n6) {
            double d4 = Math.PI * 2 * (double)n6 / 48.0;
            int n7 = n + (int)Math.round(Math.cos(d4) * (double)(n3 - 2));
            int n8 = n2 + (int)Math.round(Math.sin(d4) * (double)(n3 - 2));
            if (n6 % 3 != 0) continue;
            RunicGrimoireUi.rect(class_3322, n7, n8, 1, 1, bl ? 1969771667 : 896297081);
        }
        for (n6 = 0; n6 < 13; ++n6) {
            int n9 = Math.floorMod(n4 * 13 + n6 * 11 + n6 * n6 * 3, RUNIC_SIGILS.length());
            class_5250 class_52502 = CACHED_SIGILS[n9];
            double d5 = Math.PI * 2 * (double)n6 / 13.0 + d3 + (double)(n4 & 0xF) * 0.035;
            int n10 = n + (int)Math.round(Math.cos(d5) * (double)n3);
            n5 = n2 + (int)Math.round(Math.sin(d5) * (double)n3);
            int n11 = bl3 ? (n6 % 3 == 0 ? -1721994 : (n6 % 3 == 1 ? -5727764 : -3683367)) : (bl2 ? (n6 % 4 == 0 ? -5727764 : -1721994) : (bl ? (n6 % 3 == 0 ? -1721994 : (n6 % 3 == 1 ? -3683367 : -5727764)) : (n6 % 3 == 0 ? -7435878 : -9937040)));
            class_3322.method_27534(class_3272, (class_2561)class_52502, n10, n5 - 4, n11);
        }
        if (bl3) {
            n6 = (int)(d2 * 20.0);
            int n12 = n3 + 5 + (int)(d2 * 8.0);
            int n13 = "UNRAVEL".equals(state.pulseKind) ? -5727764 : ("TRANSMUTE".equals(state.pulseKind) ? -3683367 : -1721994);
            for (int i = 0; i < 14; ++i) {
                double d6 = Math.PI * 2 * (double)i / 14.0 + d * 0.85;
                n5 = n12 + i % 3 * 2;
                RunicGrimoireUi.rect(class_3322, n + (int)((double)n5 * Math.cos(d6)), n2 + (int)((double)n5 * Math.sin(d6)), (i + n6) % 4 == 0 ? 2 : 1, 2, n13);
            }
        }
    }

    public static boolean insideGrimoire(Object object, double d, double d2) {
        return RunicRefinedClassicUi.insideScreen(object, d, d2);
    }

    private static void scrollBar(class_332 class_3322, int n, int n2, int n3, int n4, int n5, int n6) {
        if (n4 <= n5) {
            return;
        }
        int n7 = Math.max(1, n4 - n5);
        int n8 = Math.max(11, Math.min(n3, Math.round((float)n5 * (float)n3 / (float)n4)));
        int n9 = n2 + (int)Math.round((double)(n3 - n8) * (double)Math.min(n7, Math.max(0, n6)) / (double)n7);
        RunicGrimoireUi.rect(class_3322, n, n2, 5, n3, -15328992);
        RunicGrimoireUi.rect(class_3322, n + 1, n2 + 1, 3, n3 - 2, -13355195);
        RunicGrimoireUi.rect(class_3322, n, n9, 5, n8, -9018550);
        RunicGrimoireUi.rect(class_3322, n + 1, n9 + 1, 3, Math.max(1, n8 - 2), -4021647);
        RunicGrimoireUi.rect(class_3322, n + 2, n9 + Math.max(1, n8 / 2), 1, 1, -731492);
    }

    private static int scrollTo(double d, int n, int n2, int n3, int n4) {
        if (n3 <= n4) {
            return 0;
        }
        int n5 = Math.max(11, Math.min(n2, Math.round((float)n4 * (float)n2 / (float)n3)));
        double d2 = (d - (double)n - (double)n5 / 2.0) / (double)Math.max(1, n2 - n5);
        return Math.max(0, Math.min(n3 - n4, (int)Math.round(d2 * (double)(n3 - n4))));
    }

    private static void label(class_332 class_3322, class_327 class_3272, String string, int n, int n2) {
        RunicGrimoireUi.text(class_3322, class_3272, string, n, n2, -4021647);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void model(Object object, class_332 class_3322, Object object2, int n, int n2, int n3) {
        block13: {
            if (RunicGrimoireUi.empty(object2)) {
                return;
            }
            class_1799 class_17992 = (class_1799)object2;
            class_4587 class_45872 = class_3322.method_51448();
            class_45872.method_22903();
            try {
                double d = Math.sin((double)System.nanoTime() / 8.5E8) * 2.0;
                class_45872.method_22904((double)n, (double)n2 + d, 240.0);
                boolean bl = false;
                class_45872.method_22903();
                try {
                    class_310 class_3102 = class_310.method_1551();
                    class_45872.method_22905((float)n3, (float)n3, (float)n3);
                    class_918 class_9182 = class_3102.method_1480();
                    class_9182.method_23178(class_17992, class_811.field_4322, 0xF000F0, 0, class_45872, (class_4597)class_3322.method_51450(), class_3102.field_1687, 0);
                    class_3322.method_51452();
                    bl = true;
                }
                catch (Throwable throwable) {
                }
                finally {
                    class_45872.method_22909();
                }
                if (bl) break block13;
                class_45872.method_22903();
                try {
                    class_45872.method_22905(3.1f, 3.1f, 3.1f);
                    class_3322.method_51427(class_17992, -8, -8);
                }
                finally {
                    class_45872.method_22909();
                }
            }
            finally {
                class_45872.method_22909();
            }
        }
    }

    private static boolean isSelectedMaxLevel(Object object) {
        Object object2 = RunicGrimoireUi.current(object);
        if (object2 == null) {
            return false;
        }
        int n = RunicGrimoireUi.level(object);
        if (n <= 0) {
            return false;
        }
        Object object3 = RunicGrimoireUi.call(RunicGrimoireUi.handler(object), "getDefinition", object2);
        int n2 = object3 == null ? 0 : RunicGrimoireUi.num(RunicGrimoireUi.call(object3, "highestConfiguredLevel", new Object[0]));
        return n2 > 0 && n >= n2;
    }

    private static void backgroundCommon(Object object, class_332 class_3322) {
        int n = RunicGrimoireUi.x(object);
        int n2 = RunicGrimoireUi.y(object);
        class_3322.method_25290(RunicGrimoireUi.trans(object) ? STONE_TRANSMUTATION : STONE_ATTUNEMENT, n - 16, n2 - 12, 0.0f, 0.0f, 672, 390, 672, 390);
    }

    public static boolean drawBackground(Object object, class_332 class_3322, float f, int n, int n2) {
        return RunicRefinedClassicUi.background(object, class_3322, f, n, n2);
    }

    private static String naturalName(Object object, int n) {
        if (object == null) {
            return "No enchantment";
        }
        try {
            Object object2;
            Object object3;
            Object var4_5;
            Object object4 = RunicGrimoireUi.get(Class.forName("net.minecraft.class_7923"), "field_41176");
            Object object5 = RunicGrimoireUi.call(object4, "method_17966", object);
            Object v0 = var4_5 = object5 instanceof Optional ? ((Optional)object5).orElse(null) : null;
            if (var4_5 != null && (object3 = RunicGrimoireUi.call(var4_5, "method_8179", Math.max(1, n))) != null && (object2 = RunicGrimoireUi.call(object3, "method_10851", new Object[0])) instanceof String && !((String)object2).isBlank()) {
                return (String)object2;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return RunicGrimoireUi.title(object) + (String)(n > 0 ? " " + RunicGrimoireUi.roman(n) : "");
    }

    private static void header(Object object, class_332 class_3322, class_327 class_3272) {
        int n = RunicGrimoireUi.x(object);
        int n2 = RunicGrimoireUi.y(object);
        boolean bl = RunicGrimoireUi.trans(object);
        State state = RunicGrimoireUi.state(object);
        RunicGrimoireUi.plaque(class_3322, class_3272, "RUNIC ATTUNEMENT TABLE", n + 10, n2 + 8, 333);
        RunicGrimoireUi.button(class_3322, class_3272, "ATTUNEMENT", n + 355, n2 + 8, 130, 20, !bl, RunicGrimoireUi.in(state.mouseX, state.mouseY, n + 355, n2 + 8, 130, 20), -1721994);
        RunicGrimoireUi.button(class_3322, class_3272, "TRANSMUTATION", n + 492, n2 + 8, 136, 20, bl, RunicGrimoireUi.in(state.mouseX, state.mouseY, n + 492, n2 + 8, 136, 20), -5727764);
    }

    private static String costName(Object object) {
        Object object2 = RunicGrimoireUi.call(object, "itemId", new Object[0]);
        return RunicGrimoireUi.title(object2);
    }

    private static void costGrid(Object object, class_332 class_3322, class_327 class_3272, int n, int n2, int n3, boolean bl) {
        int n4;
        List<?> list = RunicGrimoireUi.costs(object, bl);
        State state = RunicGrimoireUi.state(object);
        for (int i = n4 = Math.min(state.costScroll, Math.max(0, list.size() - n3)); i < Math.min(list.size(), n4 + n3); ++i) {
            Object obj = list.get(i);
            int n5 = i - n4;
            RunicGrimoireUi.text(class_3322, class_3272, RunicGrimoireUi.truncate(RunicGrimoireUi.costName(obj), 19), n, n2 + n5 * 14, -924215);
            String string = "x" + RunicGrimoireUi.num(RunicGrimoireUi.call(obj, "count", new Object[0]));
            RunicGrimoireUi.text(class_3322, class_3272, string, n + 141, n2 + n5 * 14, -4021647);
        }
        if (list.isEmpty()) {
            RunicGrimoireUi.text(class_3322, class_3272, "No additional materials", n, n2, -5986902);
        }
        RunicGrimoireUi.scrollBar(class_3322, n + 165, n2, n3 * 14, list.size(), n3, n4);
    }

    private static void attune(Object object, class_332 class_3322, class_327 class_3272) {
        int n;
        int n2;
        Object object2;
        int n3;
        int n4;
        int n5 = RunicGrimoireUi.x(object);
        int n6 = RunicGrimoireUi.y(object);
        State state = RunicGrimoireUi.state(object);
        Object object3 = RunicGrimoireUi.current(object);
        int n7 = RunicGrimoireUi.level(object);
        boolean bl = !RunicGrimoireUi.empty(RunicGrimoireUi.equipment(object));
        LinkedHashMap<Object, Integer> linkedHashMap = RunicGrimoireUi.installed(object);
        RunicGrimoireUi.plaque(class_3322, class_3272, "EQUIPMENT", n5 + 16, n6 + 49, 182);
        if (bl) {
            RunicGrimoireUi.model(object, class_3322, RunicGrimoireUi.equipment(object), n5 + 107, n6 + 129, 33);
        }
        RunicGrimoireUi.bigText(class_3322, class_3272, RunicGrimoireUi.truncate(RunicGrimoireUi.naturalName(object3, 0), 27), n5 + 107, n6 + 229, object3 == null ? -5986902 : -1721994, 1.1f);
        RunicGrimoireUi.bigText(class_3322, class_3272, "MATERIALS", n5 + 107, n6 + 248, -4021647, 0.98f);
        RunicGrimoireUi.costGrid(object, class_3322, class_3272, n5 + 22, n6 + 266, 2, false);
        boolean bl2 = bl && object3 != null && n7 > 0 && RunicUnravelRuntime.canUnravel(RunicGrimoireUi.handler(object), object3);
        String string = bl2 ? RunicGrimoireUi.estimateRefund(object, n7) : "Not available";
        RunicGrimoireUi.text(class_3322, class_3272, "Refund: " + RunicGrimoireUi.truncate(string, 22), n5 + 22, n6 + 305, bl2 ? -7222118 : -5986902);
        RunicGrimoireUi.button(class_3322, class_3272, "UNRAVEL", n5 + 22, n6 + 319, 83, 25, bl2 && !RunicGrimoireUi.charging(object), RunicGrimoireUi.in(state.mouseX, state.mouseY, n5 + 22, n6 + 319, 83, 25), -5727764);
        boolean bl3 = bl && object3 != null && RunicGrimoireUi.allowed(object, false) && !RunicGrimoireUi.charging(object);
        RunicGrimoireUi.attuneButton(class_3322, class_3272, n5 + 114, n6 + 319, 83, 25, bl3, RunicGrimoireUi.in(state.mouseX, state.mouseY, n5 + 114, n6 + 319, 83, 25), RunicGrimoireUi.xp(object, false));
        RunicGrimoireUi.plaque(class_3322, class_3272, "INSCRIBED RUNES", n5 + 222, n6 + 49, 192);
        ArrayList<Map.Entry<Object, Integer>> arrayList = new ArrayList<Map.Entry<Object, Integer>>(linkedHashMap.entrySet());
        for (n4 = n3 = Math.min(state.installedScroll, Math.max(0, arrayList.size() - 9)); n4 < Math.min(arrayList.size(), n3 + 9); ++n4) {
            object2 = (Map.Entry)arrayList.get(n4);
            int n8 = n6 + 80 + (n4 - n3) * 17;
            n2 = Objects.equals(object2.getKey(), object3);
            RunicGrimoireUi.rect(class_3322, n5 + 229, n8, 176, 16, n2 != 0 ? -11910312 : (RunicGrimoireUi.in(state.mouseX, state.mouseY, n5 + 229, n8, 176, 16) ? -13288635 : (n4 % 2 == 0 ? -14078922 : -14210252)));
            if (n2 != 0) {
                RunicGrimoireUi.frame(class_3322, n5 + 229, n8, 176, 16, -5727764);
            }
            RunicGrimoireUi.text(class_3322, class_3272, RunicGrimoireUi.truncate(RunicGrimoireUi.naturalName(object2.getKey(), (Integer)object2.getValue()), 27), n5 + 235, n8 + 4, n2 != 0 ? -1721994 : -924215);
        }
        if (arrayList.isEmpty()) {
            RunicGrimoireUi.text(class_3322, class_3272, "No inscriptions yet", n5 + 233, n6 + 91, -5986902);
        }
        RunicGrimoireUi.scrollBar(class_3322, n5 + 408, n6 + 80, 153, arrayList.size(), 9, n3);
        RunicGrimoireUi.inventoryLabel(object, class_3322, class_3272);
        RunicGrimoireUi.plaque(class_3322, class_3272, "ATTUNEMENT TOME", n5 + 442, n6 + 49, 182);
        RunicGrimoireUi.bigText(class_3322, class_3272, "Search & Filter", n5 + 533, n6 + 73, -5986902, 0.9f);
        n4 = RunicGrimoireUi.num(RunicGrimoireUi.get(object, "tierFilter"));
        object2 = new int[]{n5 + 445, n5 + 493, n5 + 539, n5 + 585};
        String[] stringArray = new String[]{"ALL", "I", "II", "III"};
        for (n2 = 0; n2 < 4; ++n2) {
            RunicGrimoireUi.button(class_3322, class_3272, stringArray[n2], (int)object2[n2], n6 + 114, n2 == 0 ? 43 : 42, 18, n4 == n2, RunicGrimoireUi.in(state.mouseX, state.mouseY, (int)object2[n2], n6 + 114, n2 == 0 ? 43 : 42, 18), -1721994);
        }
        List<?> list = RunicGrimoireUi.filtered(object);
        for (int i = n = Math.min(Math.max(0, RunicGrimoireUi.num(RunicGrimoireUi.get(object, "scrollOffset"))), Math.max(0, list.size() - 11)); i < Math.min(n + 11, list.size()); ++i) {
            int n9;
            Object obj = list.get(i);
            int n10 = i - n;
            int n11 = n6 + 139 + n10 * 17;
            int n12 = linkedHashMap.getOrDefault(obj, 0);
            boolean bl4 = Objects.equals(obj, object3);
            Object object4 = RunicGrimoireUi.call(RunicGrimoireUi.handler(object), "getDefinition", obj);
            int n13 = object4 == null ? 0 : RunicGrimoireUi.num(RunicGrimoireUi.call(object4, "tabletTier", new Object[0]));
            boolean bl5 = n12 > 0 && object4 != null && n12 >= RunicGrimoireUi.num(RunicGrimoireUi.call(object4, "highestConfiguredLevel", new Object[0]));
            boolean bl6 = RunicGrimoireUi.in(state.mouseX, state.mouseY, n5 + 442, n11, 183, 16);
            RunicGrimoireUi.rect(class_3322, n5 + 442, n11, 181, 16, bl4 ? -11319965 : (bl6 ? -12369069 : ((n10 & 1) == 0 ? -13815494 : -14275788)));
            if (bl4) {
                RunicGrimoireUi.frame(class_3322, n5 + 442, n11, 183, 16, -5727764);
            }
            if (n12 > 0) {
                RunicGrimoireUi.rect(class_3322, n5 + 444, n11 + 3, 2, 10, bl5 ? -1721994 : -8073253);
            }
            RunicGrimoireUi.text(class_3322, class_3272, RunicGrimoireUi.truncate(RunicGrimoireUi.naturalName(obj, 0), 20), n5 + 450, n11 + 4, bl5 ? -1721994 : (n12 > 0 ? -8073253 : (bl4 ? -924215 : -5986902)));
            if (RunicGrimoireUi.infoAvailable()) {
                n9 = RunicGrimoireUi.in(state.mouseX, state.mouseY, n5 + 580, n11 + 1, 14, 14) ? -1721994 : -8073253;
                RunicGrimoireUi.rect(class_3322, n5 + 579, n11 + 2, 14, 12, -14208193);
                RunicGrimoireUi.frame(class_3322, n5 + 579, n11 + 2, 14, 12, -10587537);
                RunicGrimoireUi.text(class_3322, class_3272, "i", n5 + 584, n11 + 4, n9);
            }
            n9 = n13 == 3 ? -1721994 : (n13 == 2 ? -5727764 : -3683367);
            RunicGrimoireUi.rect(class_3322, n5 + 597, n11 + 2, 23, 12, -14143426);
            RunicGrimoireUi.frame(class_3322, n5 + 597, n11 + 2, 23, 12, n9);
            String string2 = n13 > 0 ? RunicGrimoireUi.roman(n13) : "-";
            int n14 = RunicGrimoireUi.num(RunicGrimoireUi.call(class_3272, "method_1727", string2));
            RunicGrimoireUi.text(class_3322, class_3272, string2, n5 + 608 - n14 / 2, n11 + 4, n9);
        }
        if (list.isEmpty()) {
            RunicGrimoireUi.text(class_3322, class_3272, "No compatible inscriptions", n5 + 446, n6 + 149, -5986902);
        }
        RunicGrimoireUi.scrollBar(class_3322, n5 + 624, n6 + 139, 187, list.size(), 11, n);
    }

    private static int refundXpPercent() {
        long l = System.nanoTime();
        if (l - lastRefundConfigCheck < 5000000000L) {
            return cachedRefundXpPercent;
        }
        try {
            Path path = Path.of("config", "runic_attunement.json");
            if (Files.exists(path, new LinkOption[0])) {
                Matcher matcher;
                String string = Files.readString(path);
                Matcher matcher2 = Pattern.compile("\\\"unravel\\\"\\s*:\\s*\\{([^}]*)\\}").matcher(string);
                if (matcher2.find() && (matcher = Pattern.compile("\\\"xp_refund_percent\\\"\\s*:\\s*(\\d+)").matcher(matcher2.group(1))).find()) {
                    cachedRefundXpPercent = Math.max(0, Math.min(100, Integer.parseInt(matcher.group(1))));
                }
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        lastRefundConfigCheck = l;
        return cachedRefundXpPercent;
    }

    private static String estimateRefund(Object object, int n) {
        try {
            Object object2 = RunicGrimoireUi.call(RunicGrimoireUi.handler(object), "getDefinition", RunicGrimoireUi.current(object));
            long l = 0L;
            for (int i = 1; i <= n; ++i) {
                l += (long)RunicGrimoireUi.num(RunicGrimoireUi.call(RunicGrimoireUi.handler(object), "getAttunementExperienceCost", object2, i));
            }
            return l * (long)RunicGrimoireUi.refundXpPercent() / 100L + " XP + materials";
        }
        catch (Throwable throwable) {
            return "Calculated refund";
        }
    }

    private static void inventoryLabel(Object object, class_332 class_3322, class_327 class_3272) {
        int n = RunicGrimoireUi.x(object);
        int n2 = RunicGrimoireUi.y(object);
        RunicGrimoireUi.bigText(class_3322, class_3272, "INVENTORY", n + 322, n2 + 256, -4021647, 0.95f);
        for (int i = 0; i < 36; ++i) {
            int n3 = i < 27 ? i / 9 : 3;
            int n4 = i % 9;
            int n5 = n + 233 + n4 * 18;
            int n6 = n2 + (n3 < 3 ? 271 + n3 * 18 : 329);
            RunicGrimoireUi.rect(class_3322, n5, n6, 18, 18, 1179933798);
            RunicGrimoireUi.frame(class_3322, n5, n6, 18, 18, 1431660409);
        }
    }

    private static void transmutation(Object object, class_332 class_3322, class_327 class_3272) {
        int n;
        int n2;
        int n3;
        int n4;
        Object object2;
        int n5;
        int n6 = RunicGrimoireUi.x(object);
        int n7 = RunicGrimoireUi.y(object);
        State state = RunicGrimoireUi.state(object);
        RunicGrimoireUi.plaque(class_3322, class_3272, "SOURCE EQUIPMENT", n6 + 16, n7 + 49, 182);
        if (!RunicGrimoireUi.empty(RunicGrimoireUi.source(object))) {
            RunicGrimoireUi.model(object, class_3322, RunicGrimoireUi.source(object), n6 + 107, n7 + 129, 30);
        }
        RunicGrimoireUi.label(class_3322, class_3272, "SOURCE ENCHANTMENTS", n6 + 20, n7 + 227);
        List<?> list = RunicGrimoireUi.transferIds(object);
        for (int i = n5 = Math.min(state.sourceScroll, Math.max(0, list.size() - 6)); i < Math.min(list.size(), n5 + 6); ++i) {
            boolean bl;
            object2 = list.get(i);
            n4 = n7 + 244 + (i - n5) * 15;
            n3 = RunicGrimoireUi.transfers(object).contains(object2);
            boolean bl2 = RunicGrimoireUi.curses(object).contains(object2);
            RunicGrimoireUi.rect(class_3322, n6 + 17, n4, 174, 14, n3 != 0 ? -13084580 : (bl2 ? -10205358 : (RunicGrimoireUi.in(state.mouseX, state.mouseY, n6 + 17, n4, 174, 14) ? -12171946 : -13354684)));
            boolean bl3 = bl = n3 != 0 || bl2 || RunicGrimoireUi.transferable(object, object2);
            if (n3 != 0 || bl2) {
                RunicGrimoireUi.frame(class_3322, n6 + 17, n4, 174, 14, n3 != 0 ? -7222118 : -5727764);
            }
            n2 = n6 + 21;
            n = n4 + 2;
            RunicGrimoireUi.rect(class_3322, n2, n, 11, 11, n3 != 0 ? -13214120 : (bl2 ? -9877680 : -14669259));
            RunicGrimoireUi.frame(class_3322, n2, n, 11, 11, n3 != 0 ? -7222118 : (bl2 ? -2518133 : -7761505));
            if (n3 != 0) {
                RunicGrimoireUi.rect(class_3322, n2 + 2, n + 5, 2, 2, -7222118);
                RunicGrimoireUi.rect(class_3322, n2 + 4, n + 7, 2, 2, -7222118);
                RunicGrimoireUi.rect(class_3322, n2 + 6, n + 5, 2, 2, -7222118);
                RunicGrimoireUi.rect(class_3322, n2 + 8, n + 3, 2, 2, -7222118);
            } else if (bl2) {
                RunicGrimoireUi.text(class_3322, class_3272, "X", n2 + 2, n + 1, -2518133);
            }
            RunicGrimoireUi.text(class_3322, class_3272, RunicGrimoireUi.truncate(RunicGrimoireUi.naturalName(object2, 0), 23), n6 + 38, n4 + 3, n3 != 0 ? -7222118 : (bl2 ? -2518133 : (bl ? -924215 : -5986902)));
        }
        if (list.isEmpty()) {
            RunicGrimoireUi.text(class_3322, class_3272, "No source enchantments", n6 + 22, n7 + 249, -5986902);
        }
        RunicGrimoireUi.scrollBar(class_3322, n6 + 200, n7 + 244, 90, list.size(), 6, n5);
        RunicGrimoireUi.plaque(class_3322, class_3272, "TRANSMUTATION", n6 + 222, n7 + 49, 192);
        RunicGrimoireUi.label(class_3322, class_3272, "TRANSFER COST", n6 + 236, n7 + 137);
        RunicGrimoireUi.costGrid(object, class_3322, class_3272, n6 + 237, n7 + 154, 3, true);
        RunicGrimoireUi.text(class_3322, class_3272, "XP: " + RunicGrimoireUi.xp(object, true), n6 + 237, n7 + 199, -1721994);
        RunicGrimoireUi.button(class_3322, class_3272, "TRANSMUTE", n6 + 242, n7 + 217, 160, 26, RunicGrimoireUi.allowed(object, true) && !RunicGrimoireUi.charging(object), RunicGrimoireUi.in(state.mouseX, state.mouseY, n6 + 242, n7 + 217, 160, 26), -5727764);
        RunicGrimoireUi.button(class_3322, class_3272, "SELECT ALL", n6 + 242, n7 + 73, 76, 20, !list.isEmpty(), RunicGrimoireUi.in(state.mouseX, state.mouseY, n6 + 242, n7 + 73, 76, 20), -4021647);
        RunicGrimoireUi.button(class_3322, class_3272, "CLEAR", n6 + 326, n7 + 73, 76, 20, !list.isEmpty(), RunicGrimoireUi.in(state.mouseX, state.mouseY, n6 + 326, n7 + 73, 76, 20), -4021647);
        RunicGrimoireUi.plaque(class_3322, class_3272, "TARGET EQUIPMENT", n6 + 442, n7 + 49, 182);
        if (!RunicGrimoireUi.empty(RunicGrimoireUi.target(object))) {
            RunicGrimoireUi.model(object, class_3322, RunicGrimoireUi.target(object), n6 + 533, n7 + 129, 30);
        }
        RunicGrimoireUi.label(class_3322, class_3272, "RESULT PREVIEW", n6 + 446, n7 + 227);
        LinkedHashMap<Object, Integer> linkedHashMap = RunicGrimoireUi.enchants(RunicGrimoireUi.target(object));
        object2 = new LinkedHashSet<Object>(linkedHashMap.keySet());
        object2.addAll(RunicGrimoireUi.transfers(object));
        object2.addAll(RunicGrimoireUi.curses(object));
        n4 = Math.min(state.targetScroll, Math.max(0, object2.size() - 6));
        n3 = 0;
        for (Object e : new ArrayList(object2).subList(n4, object2.size())) {
            if (n3 >= 6) break;
            n2 = linkedHashMap.getOrDefault(e, 0);
            n = RunicGrimoireUi.transfers(object).contains(e) ? 1 : 0;
            boolean bl = RunicGrimoireUi.curses(object).contains(e);
            int n8 = n7 + 244 + n3 * 15;
            RunicGrimoireUi.rect(class_3322, n6 + 442, n8, 183, 14, n3 % 2 == 0 ? -13354684 : -13880772);
            RunicGrimoireUi.text(class_3322, class_3272, (bl ? "- " : (n != 0 ? "+ " : "  ")) + RunicGrimoireUi.truncate(RunicGrimoireUi.naturalName(e, n2), 25), n6 + 449, n8 + 3, bl ? -2518133 : (n != 0 ? -7222118 : -924215));
            ++n3;
        }
        if (object2.isEmpty()) {
            RunicGrimoireUi.text(class_3322, class_3272, "Target enchantments appear here", n6 + 442, n7 + 249, -5986902);
        }
        RunicGrimoireUi.scrollBar(class_3322, n6 + 626, n7 + 244, 90, object2.size(), 6, n4);
        RunicGrimoireUi.inventoryLabel(object, class_3322, class_3272);
    }

    private static boolean infoAvailable() {
        if (etAvailable != null) {
            return etAvailable;
        }
        boolean bl = false;
        try {
            Class<?> clazz = Class.forName("dev.runicattunement.compat.EnchantedTooltipsBridge");
            Method method = clazz.getDeclaredMethod("available", new Class[0]);
            method.setAccessible(true);
            bl = Boolean.TRUE.equals(method.invoke(null, new Object[0]));
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        etAvailable = bl;
        return bl;
    }

    private static void openInfo(Object object, Object object2) {
        State state = RunicGrimoireUi.state(object);
        String string = String.valueOf(object2);
        if (string.equals(state.infoEnchant)) {
            state.infoEnchant = "";
            return;
        }
        state.infoEnchant = string;
        state.infoTitle = RunicGrimoireUi.naturalName(object2, 0);
        state.infoDescription = "No Enchanted ToolTips description available.";
        try {
            Class<?> clazz = Class.forName("dev.runicattunement.compat.EnchantedTooltipsBridge");
            Method method = clazz.getDeclaredMethod("translatedDescription", String.class);
            Method method2 = clazz.getDeclaredMethod("usableDescription", Object.class, String.class);
            method.setAccessible(true);
            method2.setAccessible(true);
            Object object3 = method.invoke(null, string);
            Object object4 = method2.invoke(null, object3, string);
            if (object4 instanceof String && !((String)object4).isBlank()) {
                state.infoDescription = (String)object4;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static void infoPopup(Object object, class_332 class_3322, class_327 class_3272) {
        State state = RunicGrimoireUi.state(object);
        if (state.infoEnchant.isEmpty() || RunicGrimoireUi.trans(object)) {
            return;
        }
        int n = RunicGrimoireUi.x(object);
        int n2 = RunicGrimoireUi.y(object);
        int n3 = n + 174;
        int n4 = n2 + 96;
        int n5 = 292;
        int n6 = 171;
        RunicGrimoireUi.rect(class_3322, n3 + 3, n4 + 4, n5, n6, -1426063360);
        RunicGrimoireUi.rect(class_3322, n3, n4, n5, n6, -14472650);
        RunicGrimoireUi.frame(class_3322, n3, n4, n5, n6, -1721994);
        RunicGrimoireUi.rect(class_3322, n3 + 4, n4 + 4, n5 - 8, 25, -13090736);
        RunicGrimoireUi.frame(class_3322, n3 + 4, n4 + 4, n5 - 8, 25, -9150399);
        RunicGrimoireUi.bigText(class_3322, class_3272, RunicGrimoireUi.truncate(state.infoTitle, 39), n3 + n5 / 2, n4 + 12, -1721994, 1.07f);
        RunicGrimoireUi.button(class_3322, class_3272, "X", n3 + n5 - 24, n4 + 7, 16, 17, true, RunicGrimoireUi.in(state.mouseX, state.mouseY, n3 + n5 - 24, n4 + 7, 16, 17), -5727764);
        String string = state.infoDescription.replace('\n', ' ');
        String[] stringArray = string.trim().split("\\s+");
        StringBuilder stringBuilder = new StringBuilder();
        int n7 = n4 + 41;
        for (String string2 : stringArray) {
            if (n7 > n4 + n6 - 28) break;
            if (stringBuilder.length() > 0 && RunicGrimoireUi.num(RunicGrimoireUi.call(class_3272, "method_1727", String.valueOf(stringBuilder) + " " + string2)) > n5 - 28) {
                RunicGrimoireUi.text(class_3322, class_3272, stringBuilder.toString(), n3 + 15, n7, -924215);
                stringBuilder.setLength(0);
                n7 += 13;
            }
            if (stringBuilder.length() > 0) {
                stringBuilder.append(' ');
            }
            stringBuilder.append(string2);
        }
        if (stringBuilder.length() > 0 && n7 <= n4 + n6 - 20) {
            RunicGrimoireUi.text(class_3322, class_3272, RunicGrimoireUi.truncate(stringBuilder.toString(), 51), n3 + 15, n7, -924215);
        }
        RunicGrimoireUi.text(class_3322, class_3272, "Enchanted ToolTips  |  click outside to close", n3 + 15, n4 + n6 - 16, -5986902);
    }

    private static void chargeOverlay(Object object, class_332 class_3322, class_327 class_3272) {
        if (!RunicGrimoireUi.charging(object)) {
            return;
        }
        State state = RunicGrimoireUi.state(object);
        int n = RunicGrimoireUi.x(object);
        int n2 = RunicGrimoireUi.y(object);
        float f = RunicGrimoireUi.progress(object);
        RunicGrimoireUi.rect(class_3322, n + 226, n2 + 42, 187, 4, -14343378);
        RunicGrimoireUi.rect(class_3322, n + 226, n2 + 42, (int)(187.0f * f), 4, state.chargeKind.equals("UNRAVEL") ? -5727764 : -1721994);
        RunicGrimoireUi.text(class_3322, class_3272, state.chargeKind + "  " + (int)(f * 100.0f) + "%", n + 275, n2 + 56, -1721994);
        int n3 = n + 320;
        int n4 = n2 + 127;
        for (int i = 0; i < 8; ++i) {
            double d = (double)i * Math.PI / 4.0 + (double)f * Math.PI * 2.0;
            int n5 = 52 - (int)(f * 18.0f);
            RunicGrimoireUi.rect(class_3322, n3 + (int)((double)n5 * Math.cos(d)), n4 + (int)((double)n5 * Math.sin(d)), 2, 2, state.chargeKind.equals("UNRAVEL") ? -5727764 : -1721994);
        }
    }

    public static boolean drawForeground(Object object, class_332 class_3322) {
        return RunicRefinedClassicUi.foreground(object, class_3322);
    }

    public static void maybeRenderInfo(Object object, Object object2, int n, int n2) {
        if (RunicGrimoireUi.active(object)) {
            return;
        }
        try {
            RunicGrimoireUi.call(Class.forName("dev.runicattunement.compat.EnchantedTooltipsBridge"), "renderInfo", object, object2, n, n2);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static void classicButton(Object object, class_332 class_3322) {
    }

    private static void pulse(Object object, String string) {
        State state = RunicGrimoireUi.state(object);
        state.pulseStart = System.nanoTime();
        state.pulseKind = string;
    }

    private static int classicLegacyClick(Object object, double d, double d2, int n) {
        try {
            State state = RunicGrimoireUi.state(object);
            int n2 = RunicGrimoireUi.x(object);
            int n3 = RunicGrimoireUi.y(object);
            if (state.classic || !RunicGrimoireUi.active(object)) {
                if (n == 0 && RunicGrimoireUi.in(d, d2, RunicGrimoireUi.num(RunicGrimoireUi.get(object, "field_2776")) + 246, RunicGrimoireUi.num(RunicGrimoireUi.get(object, "field_2800")) + 8, 70, 18)) {
                    state.classic = false;
                    RunicGrimoireUi.prepare(object);
                    return -20;
                }
                return -1000;
            }
            RunicGrimoireUi.prepare(object);
            boolean bl = RunicGrimoireUi.trans(object);
            if (!state.infoEnchant.isEmpty()) {
                state.infoEnchant = "";
                return -20;
            }
            if (n != 0) {
                return RunicGrimoireUi.in(d, d2, n2, n3, 640, 366) ? -10 : -1000;
            }
            if (bl) {
                if (RunicGrimoireUi.in(d, d2, n2 + 199, n3 + 244, 8, 90)) {
                    state.sourceScroll = RunicGrimoireUi.scrollTo(d2, n3 + 244, 90, RunicGrimoireUi.transferIds(object).size(), 6);
                    return -20;
                }
                if (RunicGrimoireUi.in(d, d2, n2 + 625, n3 + 244, 7, 90)) {
                    LinkedHashSet<Object> linkedHashSet = new LinkedHashSet<Object>(RunicGrimoireUi.enchants(RunicGrimoireUi.target(object)).keySet());
                    linkedHashSet.addAll(RunicGrimoireUi.transfers(object));
                    linkedHashSet.addAll(RunicGrimoireUi.curses(object));
                    int n4 = linkedHashSet.size();
                    state.targetScroll = RunicGrimoireUi.scrollTo(d2, n3 + 244, 90, n4, 6);
                    return -20;
                }
            } else {
                if (RunicGrimoireUi.in(d, d2, n2 + 407, n3 + 80, 8, 153)) {
                    state.installedScroll = RunicGrimoireUi.scrollTo(d2, n3 + 80, 153, RunicGrimoireUi.installed(object).size(), 9);
                    return -20;
                }
                if (RunicGrimoireUi.in(d, d2, n2 + 623, n3 + 139, 9, 187)) {
                    RunicGrimoireUi.setI(object, "scrollOffset", RunicGrimoireUi.scrollTo(d2, n3 + 139, 187, RunicGrimoireUi.filtered(object).size(), 11));
                    return -20;
                }
            }
            if (RunicGrimoireUi.in(d, d2, n2 + 355, n3 + 8, 130, 20)) {
                return bl ? 10 : -20;
            }
            if (RunicGrimoireUi.in(d, d2, n2 + 492, n3 + 8, 136, 20)) {
                return bl ? -20 : 11;
            }
            if (RunicGrimoireUi.in(d, d2, n2 + 232, n3 + 270, 164, 78)) {
                return -10;
            }
            if (bl) {
                if (RunicGrimoireUi.in(d, d2, n2 + 95, n3 + 193, 25, 26) || RunicGrimoireUi.in(d, d2, n2 + 521, n3 + 193, 25, 26)) {
                    return -10;
                }
                if (RunicGrimoireUi.in(d, d2, n2 + 242, n3 + 73, 76, 20)) {
                    for (Object obj : RunicGrimoireUi.transferIds(object)) {
                        if (RunicGrimoireUi.isCurse(obj) || !RunicGrimoireUi.transferable(object, obj)) continue;
                        RunicGrimoireUi.transfers(object).add(obj);
                    }
                    return 4000;
                }
                if (RunicGrimoireUi.in(d, d2, n2 + 326, n3 + 73, 76, 20)) {
                    RunicGrimoireUi.transfers(object).clear();
                    return 4001;
                }
                if (RunicGrimoireUi.in(d, d2, n2 + 242, n3 + 217, 160, 26)) {
                    if (!RunicGrimoireUi.charging(object) && RunicGrimoireUi.allowed(object, true)) {
                        RunicGrimoireUi.startCharge(object, "TRANSMUTE");
                        RunicGrimoireUi.pulse(object, "TRANSMUTE");
                        return 5000;
                    }
                    return -20;
                }
                if (RunicGrimoireUi.in(d, d2, n2 + 17, n3 + 244, 192, 90)) {
                    int n5 = (int)(d2 - (double)n3 - 244.0) / 15 + state.sourceScroll;
                    List<?> list = RunicGrimoireUi.transferIds(object);
                    if (n5 >= 0 && n5 < list.size()) {
                        Object obj = list.get(n5);
                        if (RunicGrimoireUi.isCurse(obj)) {
                            if (RunicGrimoireUi.curses(object).contains(obj)) {
                                RunicGrimoireUi.curses(object).remove(obj);
                                return 3000 + n5;
                            }
                            if (RunicGrimoireUi.transferable(object, obj)) {
                                RunicGrimoireUi.curses(object).add(obj);
                                return 3000 + n5;
                            }
                            return -20;
                        }
                        if (RunicGrimoireUi.transfers(object).contains(obj)) {
                            RunicGrimoireUi.transfers(object).remove(obj);
                            return 2000 + n5;
                        }
                        if (RunicGrimoireUi.transferable(object, obj)) {
                            RunicGrimoireUi.transfers(object).add(obj);
                            return 2000 + n5;
                        }
                        return -20;
                    }
                    return -20;
                }
            } else {
                if (RunicGrimoireUi.in(d, d2, n2 + 95, n3 + 192, 25, 26) || RunicGrimoireUi.in(d, d2, n2 + 445, n3 + 91, 176, 19)) {
                    return -10;
                }
                if (RunicGrimoireUi.in(d, d2, n2 + 22, n3 + 319, 83, 25)) {
                    Object object2 = RunicGrimoireUi.current(object);
                    int n6 = RunicGrimoireUi.index(object, object2);
                    if (n6 >= 0 && !RunicGrimoireUi.charging(object) && RunicGrimoireUi.level(object) > 0 && RunicUnravelRuntime.canUnravel(RunicGrimoireUi.handler(object), object2)) {
                        RunicGrimoireUi.startCharge(object, "UNRAVEL");
                        RunicGrimoireUi.pulse(object, "UNRAVEL");
                        return 6000 + n6;
                    }
                    return -20;
                }
                if (RunicGrimoireUi.in(d, d2, n2 + 114, n3 + 319, 83, 25)) {
                    int n7;
                    Object object3 = RunicGrimoireUi.current(object);
                    Object object4 = RunicGrimoireUi.equipment(object);
                    int n8 = n7 = object3 == null ? -1 : RunicGrimoireUi.index(object, object3);
                    if (n7 >= 0 && !RunicGrimoireUi.empty(object4) && !RunicGrimoireUi.charging(object) && RunicGrimoireUi.allowed(object, false)) {
                        RunicGrimoireUi.startCharge(object, "ATTUNE");
                        RunicGrimoireUi.pulse(object, "ATTUNE");
                        return 1000 + n7;
                    }
                    return -20;
                }
                if (RunicGrimoireUi.in(d, d2, n2 + 445, n3 + 114, 183, 18)) {
                    int n9 = d < (double)(n2 + 493) ? 0 : (d < (double)(n2 + 539) ? 1 : (d < (double)(n2 + 585) ? 2 : 3));
                    RunicGrimoireUi.setI(object, "tierFilter", n9);
                    RunicGrimoireUi.setI(object, "scrollOffset", 0);
                    RunicGrimoireUi.call(object, "keepSelectionValid", new Object[0]);
                    return -20;
                }
                if (RunicGrimoireUi.in(d, d2, n2 + 442, n3 + 139, 183, 187)) {
                    int n10 = (int)(d2 - (double)n3 - 139.0) / 17 + RunicGrimoireUi.num(RunicGrimoireUi.get(object, "scrollOffset"));
                    List<?> list = RunicGrimoireUi.filtered(object);
                    if (n10 >= 0 && n10 < list.size()) {
                        Object obj = list.get(n10);
                        if (RunicGrimoireUi.in(d, d2, n2 + 579, n3 + 139 + (n10 - RunicGrimoireUi.num(RunicGrimoireUi.get(object, "scrollOffset"))) * 17 + 1, 15, 15) && RunicGrimoireUi.infoAvailable()) {
                            RunicGrimoireUi.openInfo(object, obj);
                            return -20;
                        }
                        state.infoEnchant = "";
                        RunicGrimoireUi.set(object, "selectedEnchantment", obj);
                        state.costScroll = 0;
                    }
                    return -20;
                }
                if (RunicGrimoireUi.in(d, d2, n2 + 229, n3 + 80, 181, 153)) {
                    int n11 = (int)(d2 - (double)n3 - 80.0) / 17 + state.installedScroll;
                    ArrayList<Object> arrayList = new ArrayList<Object>(RunicGrimoireUi.installed(object).keySet());
                    if (n11 >= 0 && n11 < arrayList.size()) {
                        RunicGrimoireUi.set(object, "selectedEnchantment", arrayList.get(n11));
                        state.costScroll = 0;
                    }
                    return -20;
                }
            }
            return RunicGrimoireUi.in(d, d2, n2, n3, 640, 366) ? -20 : -10;
        }
        catch (Throwable throwable) {
            RunicGrimoireUi.warn(throwable);
            return -20;
        }
    }

    public static boolean scroll(Object object, double d, double d2, double d3) {
        return RunicRefinedClassicUi.mouseScroll(object, d, d2, d3);
    }

    public static void close(Object object) {
        try {
            RunicGrimoireUi.restore(object);
            STATES.remove(object);
        }
        catch (Throwable throwable) {
            RunicGrimoireUi.warn(throwable);
        }
        finally {
            GrimoireEmiGuard.exit();
        }
    }

    public static int click(Object object, double d, double d2, int n) {
        return RunicRefinedClassicUi.click(object, d, d2, n);
    }

    private static final class State {
        boolean classic = false;
        boolean relaid = false;
        List<int[]> originalSlots;
        Object search;
        int searchX;
        int searchY;
        int searchW;
        int prevX = Integer.MIN_VALUE;
        int prevY = Integer.MIN_VALUE;
        int prevOx = Integer.MIN_VALUE;
        int prevOy = Integer.MIN_VALUE;
        int installedScroll = 0;
        int costScroll = 0;
        int sourceScroll = 0;
        int targetScroll = 0;
        long chargeEnd = 0L;
        long chargeStart = 0L;
        String chargeKind = "";
        String lastItem = "";
        long pulseStart = 0L;
        String pulseKind = "";
        String enchantFingerprint = "";
        String infoEnchant = "";
        String infoTitle = "";
        String infoDescription = "";
        int mouseX = -9999;
        int mouseY = -9999;

        private State() {
        }
    }

    private static class ClassForTransmutation {
        private static Class<?> INSTANCE;

        private ClassForTransmutation() {
        }

        static {
            try {
                INSTANCE = Class.forName("dev.runicattunement.transmutation.TransmutationRules");
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
    }
}

