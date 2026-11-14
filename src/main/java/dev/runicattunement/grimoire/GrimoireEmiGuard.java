/*
 * Decompiled with CFR 0.152.
 */
package dev.runicattunement.grimoire;

import java.lang.reflect.Field;

public final class GrimoireEmiGuard {
    private static boolean active;
    private static boolean missing;
    private static boolean warned;
    private static Field enabled;
    private static Field configButton;
    private static Field treeButton;
    private static Object initialConfigButton;
    private static Object initialTreeButton;
    private static boolean initialEnabled;
    private static Object searchWidget;
    private static Field searchVisible;
    private static boolean initialSearchVisible;
    private static Object hiddenVisibility;

    private GrimoireEmiGuard() {
    }

    private static Field field(Class<?> clazz, String string) throws ReflectiveOperationException {
        Field field = clazz.getField(string);
        field.setAccessible(true);
        return field;
    }

    private static Field visibleField(Object object) throws ReflectiveOperationException {
        for (Class<?> clazz = object.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            try {
                Field field = clazz.getDeclaredField("field_22764");
                field.setAccessible(true);
                return field;
            }
            catch (NoSuchFieldException noSuchFieldException) {
                continue;
            }
        }
        throw new NoSuchFieldException("EMI widget visible field");
    }

    private static void searchHidden() {
        try {
            Class<?> clazz = Class.forName("dev.emi.emi.screen.EmiScreenManager");
            Object object = GrimoireEmiGuard.field(clazz, "search").get(null);
            if (object == null) {
                return;
            }
            if (object != searchWidget) {
                searchWidget = object;
                searchVisible = GrimoireEmiGuard.visibleField(object);
                initialSearchVisible = searchVisible.getBoolean(object);
            }
            searchVisible.setBoolean(object, false);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static synchronized void enter() {
        block5: {
            if (missing) {
                return;
            }
            try {
                if (!active) {
                    Class<?> clazz = Class.forName("dev.emi.emi.config.EmiConfig");
                    enabled = GrimoireEmiGuard.field(clazz, "enabled");
                    configButton = GrimoireEmiGuard.field(clazz, "emiConfigButtonVisibility");
                    treeButton = GrimoireEmiGuard.field(clazz, "recipeTreeButtonVisibility");
                    initialEnabled = enabled.getBoolean(null);
                    initialConfigButton = configButton.get(null);
                    initialTreeButton = treeButton.get(null);
                    Class<Enum> clazz2 = configButton.getType().asSubclass(Enum.class);
                    hiddenVisibility = Enum.valueOf(clazz2, "HIDDEN");
                    active = true;
                    System.out.println("[Runic Grimoire] EMI screen visibility temporarily disabled (runtime guard active).");
                }
                enabled.setBoolean(null, false);
                configButton.set(null, hiddenVisibility);
                treeButton.set(null, hiddenVisibility);
                GrimoireEmiGuard.searchHidden();
            }
            catch (ClassNotFoundException classNotFoundException) {
                missing = true;
            }
            catch (Throwable throwable) {
                if (warned) break block5;
                warned = true;
                System.err.println("[Runic Grimoire] EMI runtime suppression failed: " + String.valueOf(throwable));
            }
        }
    }

    public static synchronized void exit() {
        if (!active) {
            return;
        }
        try {
            enabled.setBoolean(null, initialEnabled);
            configButton.set(null, initialConfigButton);
            treeButton.set(null, initialTreeButton);
            if (searchWidget != null && searchVisible != null) {
                searchVisible.setBoolean(searchWidget, initialSearchVisible);
            }
            System.out.println("[Runic Grimoire] EMI screen visibility restored.");
        }
        catch (Throwable throwable) {
            if (!warned) {
                warned = true;
                System.err.println("[Runic Grimoire] EMI visibility restore failed: " + String.valueOf(throwable));
            }
        }
        finally {
            active = false;
            searchWidget = null;
            searchVisible = null;
        }
    }
}

