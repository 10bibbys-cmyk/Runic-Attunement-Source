/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_3222
 */
package dev.runicattunement.unravel;

import dev.runicattunement.effect.RunicEffects;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.class_3222;

public final class RunicUnravelRuntime {
    private static final int BUTTON_BASE = 6000;
    private static final String LEGACY_LEDGER = "runic_attunement_investment_v1";
    private static final Pattern UNRAVEL = Pattern.compile("\\\"unravel\\\"\\s*:\\s*\\{([^}]*)\\}", 32);
    private static final String DEFAULT_SECTION = "  \"unravel\": {\n    \"enabled\": true,\n    \"material_refund_percent\": 50,\n    \"xp_refund_percent\": 50\n  }\n";
    private static final int MAX_ENCHANTMENT_LEVEL = 256;
    private static final int MAX_MATERIALS_PER_ID = 65536;
    private static final int MAX_MATERIAL_TYPES = 128;
    private static boolean warned;
    private static final Set<String> REPORTED_DENIALS;

    private RunicUnravelRuntime() {
    }

    public static Object accessField(Object object, String string) {
        if (object == null) {
            return null;
        }
        try {
            Class<?> clazz;
            Class<?> clazz2 = clazz = object instanceof Class ? (Class<?>)object : object.getClass();
            while (clazz != null) {
                try {
                    Field field = clazz.getDeclaredField(string);
                    field.setAccessible(true);
                    return field.get(object instanceof Class ? null : object);
                }
                catch (NoSuchFieldException noSuchFieldException) {
                    clazz = clazz.getSuperclass();
                }
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return null;
    }

    private static boolean fits(Class<?> clazz, Object object) {
        if (object == null) {
            return !clazz.isPrimitive();
        }
        if (clazz.isPrimitive()) {
            if (clazz == Integer.TYPE) {
                return object instanceof Integer;
            }
            if (clazz == Boolean.TYPE) {
                return object instanceof Boolean;
            }
            if (clazz == Double.TYPE) {
                return object instanceof Double;
            }
            if (clazz == Long.TYPE) {
                return object instanceof Long;
            }
            if (clazz == Float.TYPE) {
                return object instanceof Float;
            }
            if (clazz == Short.TYPE) {
                return object instanceof Short;
            }
            if (clazz == Byte.TYPE) {
                return object instanceof Byte;
            }
            return false;
        }
        return clazz.isAssignableFrom(object.getClass());
    }

    /*
     * WARNING - void declaration
     */
    public static Object invoke(Object object, String string, Object ... objectArray) {
        int n;
        Class<?>[] classArray;
        void var4_5;
        Class<?> methodArray;
        if (object == null) {
            throw new IllegalArgumentException("Null receiver for " + string);
        }
        Class<?> object2 = methodArray = object instanceof Class ? (Class<?>)object : object.getClass();
        while (var4_5 != null) {
            Method[] methodArray2 = var4_5.getDeclaredMethods();
            int n2 = methodArray2.length;
            for (int i = 0; i < n2; ++i) {
                classArray = methodArray2[i];
                if (!classArray.getName().equals(string) || classArray.getParameterCount() != objectArray.length) continue;
                Class<?>[] classArray2 = classArray.getParameterTypes();
                n = 1;
                for (int j = 0; j < objectArray.length; ++j) {
                    if (RunicUnravelRuntime.fits(classArray2[j], objectArray[j])) continue;
                    n = 0;
                    break;
                }
                if (n == 0) continue;
                try {
                    classArray.setAccessible(true);
                    return classArray.invoke(object instanceof Class ? null : object, objectArray);
                }
                catch (InvocationTargetException invocationTargetException) {
                    throw new IllegalStateException("Invoking " + string, invocationTargetException.getCause());
                }
                catch (ReflectiveOperationException reflectiveOperationException) {
                    throw new IllegalStateException("Invoking " + string, reflectiveOperationException);
                }
            }
            Class clazz = var4_5.getSuperclass();
        }
        for (Method method : methodArray.getMethods()) {
            if (!method.getName().equals(string) || method.getParameterCount() != objectArray.length) continue;
            classArray = method.getParameterTypes();
            boolean bl = true;
            for (n = 0; n < objectArray.length; ++n) {
                if (RunicUnravelRuntime.fits(classArray[n], objectArray[n])) continue;
                bl = false;
                break;
            }
            if (!bl) continue;
            try {
                return method.invoke(object instanceof Class ? null : object, objectArray);
            }
            catch (InvocationTargetException invocationTargetException) {
                throw new IllegalStateException("Invoking interface " + string, invocationTargetException.getCause());
            }
            catch (ReflectiveOperationException reflectiveOperationException) {
                throw new IllegalStateException("Invoking interface " + string, reflectiveOperationException);
            }
        }
        throw new IllegalStateException("Missing " + methodArray.getName() + "." + string + "/" + objectArray.length);
    }

    private static Class<?> mc(String string) throws ClassNotFoundException {
        return Class.forName("net.minecraft." + string);
    }

    private static int number(Object object) {
        return object instanceof Number ? ((Number)object).intValue() : 0;
    }

    private static void warn(String string, Throwable throwable) {
        if (!warned) {
            warned = true;
            System.err.println("[Runic Unravel] " + string + " failed safely: " + String.valueOf(throwable));
        }
    }

    private static double configNumber(String string, String string2, double d) {
        Matcher matcher = Pattern.compile("\\\"" + Pattern.quote(string2) + "\\\"\\s*:\\s*(-?[0-9]+(?:\\.[0-9]+)?)").matcher(string);
        if (!matcher.find()) {
            return d;
        }
        try {
            return Double.parseDouble(matcher.group(1));
        }
        catch (NumberFormatException numberFormatException) {
            return d;
        }
    }

    private static int percent(String string, String string2, String string3) {
        double d = RunicUnravelRuntime.configNumber(string, string2, Double.NaN);
        if (!Double.isFinite(d)) {
            d = 100.0 * RunicUnravelRuntime.configNumber(string, string3, 0.5);
        }
        if (!Double.isFinite(d)) {
            d = 50.0;
        }
        return (int)Math.round(Math.max(0.0, Math.min(100.0, d)));
    }

    private static Config parseConfigSection(String string) {
        Config config = new Config();
        config.materials = RunicUnravelRuntime.percent(string, "material_refund_percent", "material_refund_multiplier");
        config.xp = RunicUnravelRuntime.percent(string, "xp_refund_percent", "xp_refund_multiplier");
        Matcher matcher = Pattern.compile("\\\"enabled\\\"\\s*:\\s*(true|false)", 2).matcher(string);
        if (matcher.find()) {
            config.enabled = Boolean.parseBoolean(matcher.group(1));
        }
        return config;
    }

    private static Config config() {
        Config config = new Config();
        try {
            Matcher matcher;
            Path path = (Path)RunicUnravelRuntime.invoke(Class.forName("dev.runicattunement.config.RunicConfig"), "path", new Object[0]);
            if (Files.isRegularFile(path, new LinkOption[0]) && (matcher = UNRAVEL.matcher(Files.readString(path, StandardCharsets.UTF_8))).find()) {
                config = RunicUnravelRuntime.parseConfigSection(matcher.group(1));
            }
        }
        catch (Throwable throwable) {
            RunicUnravelRuntime.warn("reading configuration", throwable);
        }
        return config;
    }

    public static void ensureConfig() {
        try {
            Object object;
            Path path = (Path)RunicUnravelRuntime.invoke(Class.forName("dev.runicattunement.config.RunicConfig"), "path", new Object[0]);
            if (!Files.isRegularFile(path, new LinkOption[0])) {
                return;
            }
            String string = Files.readString(path, StandardCharsets.UTF_8);
            Matcher matcher = UNRAVEL.matcher(string);
            if (matcher.find()) {
                String string2 = matcher.group(1);
                if (string2.contains("material_refund_percent") && string2.contains("xp_refund_percent")) {
                    return;
                }
                Config config = RunicUnravelRuntime.parseConfigSection(string2);
                String string3 = "\"unravel\": {\n    \"enabled\": " + config.enabled + ",\n    \"material_refund_percent\": " + config.materials + ",\n    \"xp_refund_percent\": " + config.xp + "\n  }";
                object = matcher.replaceFirst(Matcher.quoteReplacement(string3));
            } else {
                String string4;
                int n = string.lastIndexOf(125);
                if (n < 0) {
                    return;
                }
                object = string4 + ((string4 = string.substring(0, n).stripTrailing()).endsWith("{") ? "\n" : ",\n") + DEFAULT_SECTION + string.substring(n);
            }
            Files.writeString(path, (CharSequence)object, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
        }
        catch (Throwable throwable) {
            RunicUnravelRuntime.warn("updating configuration", throwable);
        }
    }

    private static Object id(String string) throws Exception {
        return RunicUnravelRuntime.mc("class_2960").getConstructor(String.class).newInstance(string);
    }

    private static Object lookupRegistry(String string, String string2) throws Exception {
        Object object = RunicUnravelRuntime.accessField(RunicUnravelRuntime.mc("class_7923"), string2);
        return ((Optional)RunicUnravelRuntime.invoke(object, "method_17966", RunicUnravelRuntime.id(string))).orElse(null);
    }

    private static Map<Object, Integer> enchantments(Object object) throws Exception {
        return (Map)RunicUnravelRuntime.invoke(RunicUnravelRuntime.mc("class_1890"), "method_8222", object);
    }

    public static boolean canUnravel(Object object, Object object2) {
        if (object == null || object2 == null) {
            return false;
        }
        try {
            Object object3 = RunicUnravelRuntime.invoke(object, "getEquipmentStack", new Object[0]);
            if (object3 == null || Boolean.TRUE.equals(RunicUnravelRuntime.invoke(object3, "method_7960", new Object[0]))) {
                return false;
            }
            Object object4 = RunicUnravelRuntime.lookupRegistry(String.valueOf(object2), "field_41176");
            if (object4 == null) {
                return false;
            }
            int n = RunicUnravelRuntime.number(RunicUnravelRuntime.enchantments(object3).get(object4));
            if (n < 1 || n > 256) {
                return false;
            }
            Object object5 = RunicUnravelRuntime.invoke(object, "getDefinition", object2);
            if (object5 == null) {
                return false;
            }
            for (int i = 1; i <= n; ++i) {
                Optional optional = (Optional)RunicUnravelRuntime.invoke(object5, "level", i);
                if (!optional.isEmpty()) continue;
                return false;
            }
            return true;
        }
        catch (Throwable throwable) {
            return false;
        }
    }

    static int refund(int n, int n2) {
        return (int)((long)Math.max(0, n) * (long)Math.max(0, Math.min(100, n2)) / 100L);
    }

    private static Quote quote(Object object, int n, Config config) throws Exception {
        if (n < 1 || n > 256) {
            return null;
        }
        Class<?> clazz = Class.forName("dev.runicattunement.data.RunicDefinitionManager");
        Object object2 = RunicUnravelRuntime.invoke(clazz, "get", object);
        if (object2 == null) {
            return null;
        }
        Object object3 = RunicUnravelRuntime.invoke(Class.forName("dev.runicattunement.config.RunicConfig"), "experienceCosts", new Object[0]);
        Class<?> clazz2 = Class.forName("dev.runicattunement.util.RunicExperience");
        LinkedHashMap<String, Long> linkedHashMap = new LinkedHashMap<String, Long>();
        long l = 0L;
        for (int i = 1; i <= n; ++i) {
            Optional optional = (Optional)RunicUnravelRuntime.invoke(object2, "level", i);
            if (optional.isEmpty()) {
                return null;
            }
            int n2 = RunicUnravelRuntime.number(RunicUnravelRuntime.invoke(clazz2, "attunementCost", object2, i, object3));
            if (n2 < 0) {
                return null;
            }
            if ((l += (long)n2) > Integer.MAX_VALUE) {
                return null;
            }
            List list = (List)RunicUnravelRuntime.invoke(clazz, "combinedCosts", object2, i);
            if (list == null) {
                return null;
            }
            for (Object e : list) {
                String string = String.valueOf(RunicUnravelRuntime.invoke(e, "itemId", new Object[0]));
                int n3 = RunicUnravelRuntime.number(RunicUnravelRuntime.invoke(e, "count", new Object[0]));
                if (n3 < 0) {
                    return null;
                }
                long l2 = linkedHashMap.getOrDefault(string, 0L) + (long)n3;
                if (l2 > 65536L) {
                    return null;
                }
                linkedHashMap.put(string, l2);
                if (linkedHashMap.size() <= 128) continue;
                return null;
            }
        }
        LinkedHashMap<String, Integer> linkedHashMap2 = new LinkedHashMap<String, Integer>();
        for (Map.Entry entry : linkedHashMap.entrySet()) {
            int n4 = RunicUnravelRuntime.refund(((Long)entry.getValue()).intValue(), config.materials);
            if (n4 <= 0) continue;
            if (RunicUnravelRuntime.lookupRegistry((String)entry.getKey(), "field_41178") == null) {
                return null;
            }
            linkedHashMap2.put((String)entry.getKey(), n4);
        }
        return new Quote(linkedHashMap2, RunicUnravelRuntime.refund((int)l, config.xp));
    }

    private static void refundItem(Object object, String string, int n) throws Exception {
        Constructor<?> constructor;
        int n2;
        if (n <= 0) {
            return;
        }
        Object object2 = RunicUnravelRuntime.lookupRegistry(string, "field_41178");
        if (object2 == null) {
            throw new IllegalStateException("Removed refund item: " + string);
        }
        Object object3 = RunicUnravelRuntime.invoke(object, "method_31548", new Object[0]);
        Class<?> clazz = RunicUnravelRuntime.mc("class_1799");
        Constructor<?> constructor2 = null;
        Constructor<?>[] constructorArray = clazz.getConstructors();
        int n3 = constructorArray.length;
        for (n2 = 0; n2 < n3; ++n2) {
            constructor = constructorArray[n2];
            Class<?>[] classArray = constructor.getParameterTypes();
            if (classArray.length != 2 || classArray[1] != Integer.TYPE || !classArray[0].isInstance(object2)) continue;
            constructor2 = constructor;
            break;
        }
        if (constructor2 == null) {
            throw new IllegalStateException("ItemStack constructor not found");
        }
        int n4 = Math.max(1, Math.min(64, RunicUnravelRuntime.number(RunicUnravelRuntime.invoke(object2, "method_7882", new Object[0]))));
        for (n3 = n; n3 > 0; n3 -= n2) {
            n2 = Math.min(n3, n4);
            constructor = constructor2.newInstance(object2, n2);
            if (Boolean.TRUE.equals(RunicUnravelRuntime.invoke(object3, "method_7394", constructor))) continue;
            RunicUnravelRuntime.invoke(object, "method_7328", constructor, false);
        }
    }

    private static void clearOldLedger(Object object, String string) {
        try {
            Object object2 = RunicUnravelRuntime.invoke(object, "method_7969", new Object[0]);
            if (object2 == null) {
                return;
            }
            if (!Boolean.TRUE.equals(RunicUnravelRuntime.invoke(object2, "method_10545", LEGACY_LEDGER))) {
                return;
            }
            Object object3 = RunicUnravelRuntime.invoke(object2, "method_10562", LEGACY_LEDGER);
            RunicUnravelRuntime.invoke(object3, "method_10551", string);
            if (((Collection)RunicUnravelRuntime.invoke(object3, "method_10541", new Object[0])).isEmpty()) {
                RunicUnravelRuntime.invoke(object2, "method_10551", LEGACY_LEDGER);
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static int deny(String string) {
        if (REPORTED_DENIALS.add(string)) {
            System.err.println("[Runic Unravel] Action refused: " + string);
        }
        return 0;
    }

    public static boolean handleMpAction(Object object, int n, int n2) {
        if (n != 0 || n2 < 6000 || n2 >= 7000) {
            return false;
        }
        try {
            Class<?> clazz = Class.forName("pota.runicmpfix.RunicMpFix");
            Method method = clazz.getDeclaredMethod("findRunicHandler", RunicUnravelRuntime.mc("class_3222"));
            method.setAccessible(true);
            Object object2 = method.invoke(null, object);
            if (object2 == null) {
                RunicUnravelRuntime.deny("no Runic Attunement screen open");
                RunicUnravelRuntime.feedback(object, "UNRAVEL rejected: reopen the Attunement Table.");
                return true;
            }
            int n3 = RunicUnravelRuntime.handleButton(object2, object, n2);
            if (n3 != 1) {
                RunicUnravelRuntime.feedback(object, "UNRAVEL rejected. See latest.log for [Runic Unravel].");
            }
        }
        catch (Throwable throwable) {
            System.err.println("[Runic Unravel] Multiplayer action failed: " + String.valueOf(throwable));
            throwable.printStackTrace(System.err);
            RunicUnravelRuntime.feedback(object, "UNRAVEL failed internally. Check latest.log.");
        }
        return true;
    }

    private static void feedback(Object object, String string) {
        if (object == null) {
            return;
        }
        try {
            Object object2 = RunicUnravelRuntime.invoke(RunicUnravelRuntime.mc("class_2561"), "method_43470", string);
            RunicUnravelRuntime.invoke(object, "method_7353", object2, true);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static int handleButton(Object object, Object object2, int n) {
        if (n < 6000 || n >= 7000) {
            return -1;
        }
        try {
            if (!RunicUnravelRuntime.mc("class_3222").isInstance(object2)) {
                return RunicUnravelRuntime.deny("not a server player");
            }
            if (Boolean.TRUE.equals(RunicUnravelRuntime.invoke(object, "isTransmutationMode", new Object[0]))) {
                return RunicUnravelRuntime.deny("table in Transmutation mode");
            }
            if (!Boolean.TRUE.equals(RunicUnravelRuntime.invoke(object, "method_7597", object2))) {
                return RunicUnravelRuntime.deny("workstation out of reach");
            }
            Config config = RunicUnravelRuntime.config();
            if (!config.enabled) {
                return RunicUnravelRuntime.deny("disabled by configuration");
            }
            List list = (List)RunicUnravelRuntime.invoke(object, "getAttunedEnchantments", new Object[0]);
            int n2 = n - 6000;
            if (n2 < 0 || n2 >= list.size()) {
                return RunicUnravelRuntime.deny("invalid enchantment list index");
            }
            Object e = list.get(n2);
            Object object3 = RunicUnravelRuntime.invoke(object, "getEquipmentStack", new Object[0]);
            if (object3 == null || Boolean.TRUE.equals(RunicUnravelRuntime.invoke(object3, "method_7960", new Object[0]))) {
                return RunicUnravelRuntime.deny("missing equipment");
            }
            Object object4 = RunicUnravelRuntime.lookupRegistry(String.valueOf(e), "field_41176");
            if (object4 == null) {
                return RunicUnravelRuntime.deny("unregistered enchantment");
            }
            LinkedHashMap<Object, Integer> linkedHashMap = new LinkedHashMap<Object, Integer>(RunicUnravelRuntime.enchantments(object3));
            int n3 = RunicUnravelRuntime.number(linkedHashMap.get(object4));
            if (n3 < 1 || n3 > 256) {
                return RunicUnravelRuntime.deny("selected enchantment no longer on item");
            }
            Object object5 = RunicUnravelRuntime.invoke(Class.forName("dev.runicattunement.data.RunicDefinitionManager"), "get", e);
            if (object5 == null || !object5.equals(RunicUnravelRuntime.invoke(object, "getDefinition", e))) {
                return RunicUnravelRuntime.deny("cost definitions stale - reopen table");
            }
            Quote quote = RunicUnravelRuntime.quote(e, n3, config);
            if (quote == null) {
                return RunicUnravelRuntime.deny("no valid refund quote for enchantment level");
            }
            linkedHashMap.remove(object4);
            RunicUnravelRuntime.invoke(RunicUnravelRuntime.mc("class_1890"), "method_8214", linkedHashMap, object3);
            RunicUnravelRuntime.clearOldLedger(object3, String.valueOf(e));
            Object object6 = quote.materials.entrySet().iterator();
            while (object6.hasNext()) {
                Map.Entry<String, Integer> entry = object6.next();
                RunicUnravelRuntime.refundItem(object2, entry.getKey(), entry.getValue());
            }
            if (quote.xp > 0) {
                RunicUnravelRuntime.invoke(object2, "method_7255", quote.xp);
            }
            if ((object6 = RunicUnravelRuntime.accessField(object, "workInventory")) != null) {
                RunicUnravelRuntime.invoke(object6, "method_5431", new Object[0]);
            }
            RunicUnravelRuntime.invoke(object, "method_7623", new Object[0]);
            RunicEffects.unravelSuccess((class_3222)object2);
            return 1;
        }
        catch (Throwable throwable) {
            System.err.println("[Runic Unravel] Server action failed: " + String.valueOf(throwable));
            throwable.printStackTrace(System.err);
            return 0;
        }
    }

    static {
        REPORTED_DENIALS = Collections.synchronizedSet(new HashSet());
    }

    private static final class Config {
        boolean enabled = true;
        int materials = 50;
        int xp = 50;

        private Config() {
        }
    }

    private static final class Quote {
        final Map<String, Integer> materials;
        final int xp;

        Quote(Map<String, Integer> map, int n) {
            this.materials = map;
            this.xp = n;
        }
    }
}

