/*
 * Decompiled with CFR 0.152.
 */
package dev.runicattunement.compat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

public final class EnchantedTooltipsBridge {
    private static final String ET = "net.kyrptonaught.enchantedtooltips.EnchantToolTipHelper";
    private static final String ET_MOD = "net.kyrptonaught.enchantedtooltips.EnchantedToolTipMod";
    private static final Map<Object, Popup> POPUPS = Collections.synchronizedMap(new WeakHashMap());
    private static volatile Boolean present;
    private static volatile boolean errorLogged;
    private static Method descriptionMethod;
    private static Method configMethod;
    private static Field showDescField;
    private static Field showAlwaysField;
    private static Field showPromptField;
    private static Field enableForBooksField;

    private EnchantedTooltipsBridge() {
    }

    private static boolean available() {
        Boolean bl = present;
        if (bl != null) {
            return bl;
        }
        try {
            Class.forName(ET_MOD, false, EnchantedTooltipsBridge.class.getClassLoader());
            Class<?> clazz = Class.forName(ET, false, EnchantedTooltipsBridge.class.getClassLoader());
            descriptionMethod = clazz.getDeclaredMethod("getEnchantDesc", String.class);
            descriptionMethod.setAccessible(true);
            Class<?> clazz2 = Class.forName(ET_MOD, false, EnchantedTooltipsBridge.class.getClassLoader());
            configMethod = clazz2.getMethod("getConfig", new Class[0]);
            Class<?> clazz3 = configMethod.getReturnType();
            showDescField = clazz3.getField("displayDescription");
            showAlwaysField = clazz3.getField("alwaysShowEnchantInfo");
            showPromptField = clazz3.getField("displayPressForInfo");
            enableForBooksField = clazz3.getField("enableForBooks");
            present = true;
            return true;
        }
        catch (LinkageError | ReflectiveOperationException throwable) {
            present = false;
            return false;
        }
    }

    private static boolean setting(Field field) throws ReflectiveOperationException {
        return field.getBoolean(configMethod.invoke(null, new Object[0]));
    }

    private static String descriptionKey(String string) {
        return "enchantment." + string.replace(':', '.') + ".desc";
    }

    private static Object translatedDescription(String string) throws ReflectiveOperationException {
        return descriptionMethod.invoke(null, EnchantedTooltipsBridge.descriptionKey(string));
    }

    private static String textString(Object object) throws ReflectiveOperationException {
        return (String)object.getClass().getMethod("getString", new Class[0]).invoke(object, new Object[0]);
    }

    private static String usableDescription(Object object, String string) throws ReflectiveOperationException {
        String string2 = EnchantedTooltipsBridge.textString(object);
        return string2.isBlank() || string2.equals(EnchantedTooltipsBridge.descriptionKey(string)) ? null : string2;
    }

    private static Method findMethod(Class<?> clazz, String string, int n) throws NoSuchMethodException {
        for (Class<?> clazz2 = clazz; clazz2 != null; clazz2 = clazz2.getSuperclass()) {
            for (Method method : clazz2.getDeclaredMethods()) {
                if (!method.getName().equals(string) || method.getParameterCount() != n) continue;
                method.setAccessible(true);
                return method;
            }
        }
        throw new NoSuchMethodException(String.valueOf(clazz) + "." + string);
    }

    private static Field findField(Class<?> clazz, String string) throws NoSuchFieldException {
        for (Class<?> clazz2 = clazz; clazz2 != null; clazz2 = clazz2.getSuperclass()) {
            try {
                Field field = clazz2.getDeclaredField(string);
                field.setAccessible(true);
                return field;
            }
            catch (NoSuchFieldException noSuchFieldException) {
                continue;
            }
        }
        throw new NoSuchFieldException(String.valueOf(clazz) + "." + string);
    }

    private static void removeOriginalShiftHint(List list) throws ReflectiveOperationException {
        if (list.isEmpty()) {
            return;
        }
        Class<?> clazz = Class.forName("net.minecraft.class_2561");
        Object object = clazz.getMethod("method_43471", String.class).invoke(null, "tooltip.runic_attunement.hold_shift");
        String string = EnchantedTooltipsBridge.textString(object);
        Object e = list.get(list.size() - 1);
        if (e != null && string.equals(EnchantedTooltipsBridge.textString(e))) {
            list.remove(list.size() - 1);
        }
    }

    public static void appendTabletTooltip(Object object, List list) {
        if (!EnchantedTooltipsBridge.available() || object == null || list == null) {
            return;
        }
        try {
            Optional optional;
            if (!EnchantedTooltipsBridge.setting(enableForBooksField) || !EnchantedTooltipsBridge.setting(showDescField)) {
                return;
            }
            Class<?> clazz = Class.forName("dev.runicattunement.item.RunicTabletItem");
            Object object2 = EnchantedTooltipsBridge.findMethod(clazz, "getEnchantmentId", 1).invoke(null, object);
            if (!(object2 instanceof Optional) || (optional = (Optional)object2).isEmpty()) {
                return;
            }
            String string = optional.get().toString();
            Object object3 = EnchantedTooltipsBridge.translatedDescription(string);
            if (EnchantedTooltipsBridge.usableDescription(object3, string) == null) {
                return;
            }
            boolean bl = EnchantedTooltipsBridge.setting(showAlwaysField);
            if (!bl) {
                Class<?> clazz2 = Class.forName("net.minecraft.class_437");
                bl = Boolean.TRUE.equals(clazz2.getMethod("method_25442", new Class[0]).invoke(null, new Object[0]));
            }
            if (bl) {
                EnchantedTooltipsBridge.removeOriginalShiftHint(list);
                list.add(object3);
            }
        }
        catch (LinkageError | ReflectiveOperationException | RuntimeException throwable) {
            EnchantedTooltipsBridge.warnOnce("tablet", throwable);
        }
    }

    private static void warnOnce(String string, Throwable throwable) {
        if (!errorLogged) {
            errorLogged = true;
            System.err.println("[Runic Attunement/Enchanted ToolTips] Optional " + string + " integration disabled: " + String.valueOf(throwable));
        }
    }

    private static boolean transmutation(Object object) throws ReflectiveOperationException {
        Object object2 = EnchantedTooltipsBridge.findField(object.getClass(), "field_2797").get(object);
        return (Boolean)EnchantedTooltipsBridge.findMethod(object2.getClass(), "isTransmutationMode", 0).invoke(object2, new Object[0]);
    }

    private static List<Object> entries(Object object, boolean bl) throws ReflectiveOperationException {
        if (!bl) {
            return (List)EnchantedTooltipsBridge.findMethod(object.getClass(), "filteredEnchantments", 0).invoke(object, new Object[0]);
        }
        Object object2 = EnchantedTooltipsBridge.findField(object.getClass(), "field_2797").get(object);
        Object object3 = EnchantedTooltipsBridge.findMethod(object2.getClass(), "getSourceStack", 0).invoke(object2, new Object[0]);
        Class<?> clazz = Class.forName("dev.runicattunement.transmutation.TransmutationRules");
        return (List)EnchantedTooltipsBridge.findMethod(clazz, "sourceEnchantments", 1).invoke(null, object3);
    }

    private static int numberField(Object object, String string) throws ReflectiveOperationException {
        return EnchantedTooltipsBridge.findField(object.getClass(), string).getInt(object);
    }

    private static Object infoEntry(Object object, boolean bl, int n) throws ReflectiveOperationException {
        List<Object> list = EnchantedTooltipsBridge.entries(object, bl);
        int n2 = EnchantedTooltipsBridge.numberField(object, bl ? "transmutationScroll" : "scrollOffset");
        int n3 = n2 + n;
        return n3 >= 0 && n3 < list.size() ? list.get(n3) : null;
    }

    private static String title(String string) {
        Object object;
        try {
            object = Class.forName("net.minecraft.class_2561");
            int n = string.indexOf(58);
            String string2 = "enchantment." + string.substring(0, n) + "." + string.substring(n + 1);
            Object object2 = ((Class)object).getMethod("method_43471", String.class).invoke(null, string2);
            String string3 = EnchantedTooltipsBridge.textString(object2);
            if (!string3.equals(string2)) {
                return string3;
            }
        }
        catch (ReflectiveOperationException | RuntimeException exception) {
            // empty catch block
        }
        object = string.substring(string.indexOf(58) + 1).replace('_', ' ');
        if (((String)object).isEmpty()) {
            return string;
        }
        return ((String)object).substring(0, 1).toUpperCase(Locale.ROOT) + ((String)object).substring(1);
    }

    public static boolean clickInfo(Object object, double d, double d2, int n) {
        if (!EnchantedTooltipsBridge.available() || n != 0) {
            return false;
        }
        try {
            boolean bl = EnchantedTooltipsBridge.transmutation(object);
            int n2 = EnchantedTooltipsBridge.numberField(object, "field_2776");
            int n3 = EnchantedTooltipsBridge.numberField(object, "field_2800");
            int n4 = n2 + (bl ? 192 : 275);
            int n5 = n3 + (bl ? 62 : 94);
            int n6 = bl ? 4 : 5;
            int n7 = bl ? 18 : 13;
            int n8 = bl ? 17 : 12;
            for (int i = 0; i < n6; ++i) {
                int n9 = n5 + i * n7;
                if (!(d >= (double)n4) || !(d < (double)(n4 + 10)) || !(d2 >= (double)n9) || !(d2 < (double)(n9 + n8))) continue;
                Object object2 = EnchantedTooltipsBridge.infoEntry(object, bl, i);
                if (object2 == null) {
                    return false;
                }
                String string = object2.toString();
                Popup popup = POPUPS.get(object);
                if (popup != null && popup.id().equals(string)) {
                    POPUPS.remove(object);
                    return true;
                }
                Object object3 = EnchantedTooltipsBridge.translatedDescription(string);
                String string2 = EnchantedTooltipsBridge.usableDescription(object3, string);
                if (string2 == null) {
                    string2 = "No Enchanted ToolTips description available.";
                }
                POPUPS.put(object, new Popup(string, EnchantedTooltipsBridge.title(string), string2, (int)d, (int)d2));
                return true;
            }
            POPUPS.remove(object);
        }
        catch (LinkageError | ReflectiveOperationException | RuntimeException throwable) {
            EnchantedTooltipsBridge.warnOnce("table click", throwable);
        }
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void renderInfo(Object object, Object object2, int n, int n2) {
        if (!EnchantedTooltipsBridge.available()) {
            return;
        }
        try {
            Object object3;
            boolean bl = EnchantedTooltipsBridge.transmutation(object);
            int n3 = EnchantedTooltipsBridge.numberField(object, "field_2776");
            int n4 = EnchantedTooltipsBridge.numberField(object, "field_2800");
            Object object4 = EnchantedTooltipsBridge.findField(object.getClass(), "field_22793").get(object);
            Class<?> clazz = object2.getClass();
            Method method = EnchantedTooltipsBridge.findMethod(clazz, "method_25294", 5);
            Method method2 = EnchantedTooltipsBridge.findMethod(clazz, "method_51433", 6);
            int n5 = n3 + (bl ? 192 : 275);
            int n6 = n4 + (bl ? 62 : 94);
            int n7 = bl ? 4 : 5;
            int n8 = bl ? 18 : 13;
            int n9 = bl ? 17 : 12;
            for (int i = 0; i < n7 && (object3 = EnchantedTooltipsBridge.infoEntry(object, bl, i)) != null; ++i) {
                int n10 = n6 + i * n8;
                boolean bl2 = n >= n5 && n < n5 + 10 && n2 >= n10 && n2 < n10 + n9;
                method.invoke(object2, n5, n10 + 1, n5 + 9, n10 + 11, bl2 ? -14385273 : -14205878);
                method2.invoke(object2, object4, "i", n5 + 3, n10 + 2, bl2 ? -8831 : -8858145, true);
            }
            Popup popup = POPUPS.get(object);
            if (popup == null) {
                return;
            }
            object3 = EnchantedTooltipsBridge.findMethod(object4.getClass(), "method_1727", 1);
            ArrayList<String> arrayList = new ArrayList<String>();
            arrayList.add(popup.title());
            StringBuilder stringBuilder = new StringBuilder();
            for (String string : popup.description().split("\\s+")) {
                String string2;
                String string3 = string2 = stringBuilder.isEmpty() ? string : String.valueOf(stringBuilder) + " " + string;
                if (!stringBuilder.isEmpty() && (Integer)((Method)object3).invoke(object4, string2) > 192) {
                    arrayList.add(stringBuilder.toString());
                    stringBuilder = new StringBuilder(string);
                    continue;
                }
                stringBuilder = new StringBuilder(string2);
            }
            if (!stringBuilder.isEmpty()) {
                arrayList.add(stringBuilder.toString());
            }
            int n11 = 0;
            for (String string : arrayList) {
                n11 = Math.max(n11, (Integer)((Method)object3).invoke(object4, string));
            }
            int n12 = arrayList.size() * 11 + 9;
            int n13 = EnchantedTooltipsBridge.numberField(object, "field_22789");
            int n14 = EnchantedTooltipsBridge.numberField(object, "field_22790");
            int n15 = n3 + (bl ? 232 : 318);
            int n16 = Math.max(popup.x() + 30, n15);
            int n17 = n16 + (n11 += 12) + 5 <= n13 ? n16 : (n3 - n11 - 12 >= 4 ? n3 - n11 - 12 : Math.max(4, n13 - n11 - 5));
            int n18 = Math.min(popup.y() + 12, Math.max(4, n14 - n12 - 5));
            Object object5 = EnchantedTooltipsBridge.findMethod(clazz, "method_51448", 0).invoke(object2, new Object[0]);
            Method method3 = EnchantedTooltipsBridge.findMethod(object5.getClass(), "method_22903", 0);
            Method method4 = EnchantedTooltipsBridge.findMethod(object5.getClass(), "method_22904", 3);
            Method method5 = EnchantedTooltipsBridge.findMethod(object5.getClass(), "method_22909", 0);
            method3.invoke(object5, new Object[0]);
            try {
                method4.invoke(object5, 0.0, 0.0, 500.0);
                method.invoke(object2, n17 - 1, n18 - 1, n17 + n11 + 1, n18 + n12 + 1, -12208443);
                method.invoke(object2, n17, n18, n17 + n11, n18 + n12, -283827923);
                for (int i = 0; i < arrayList.size(); ++i) {
                    method2.invoke(object2, object4, arrayList.get(i), n17 + 6, n18 + 5 + i * 11, i == 0 ? -11654 : -1773070, true);
                }
            }
            finally {
                method5.invoke(object5, new Object[0]);
            }
        }
        catch (LinkageError | ReflectiveOperationException | RuntimeException throwable) {
            EnchantedTooltipsBridge.warnOnce("table rendering", throwable);
        }
    }

    private record Popup(String id, String title, String description, int x, int y) {
    }
}

