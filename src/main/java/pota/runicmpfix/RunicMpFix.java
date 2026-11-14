/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.ModInitializer
 *  net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
 *  net.minecraft.class_2561
 *  net.minecraft.class_2960
 *  net.minecraft.class_3222
 */
package pota.runicmpfix;

import dev.runicattunement.unravel.RunicUnravelRuntime;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_3222;

public final class RunicMpFix
implements ModInitializer {
    public static final class_2960 ACTION = new class_2960("pota_runic_mp_fix", "ui_action");
    public static final int ACTION_ATTUNE = 0;
    public static final int ACTION_TRANSMUTE = 1;
    private static final int MAX_IDS = 64;

    public void onInitialize() {
        ServerPlayNetworking.registerGlobalReceiver((class_2960)ACTION, (minecraftServer, class_32222, class_32442, class_25402, packetSender) -> {
            String[] stringArray;
            String[] stringArray2;
            int n;
            int n2 = PacketCodec.readInt(class_25402);
            if (n2 == 0) {
                n = PacketCodec.readInt(class_25402);
                stringArray2 = null;
                stringArray = null;
            } else if (n2 == 1) {
                int n3;
                n = -1;
                int n4 = RunicMpFix.boundedCount(PacketCodec.readInt(class_25402));
                if (n4 < 0) {
                    return;
                }
                stringArray2 = new String[n4];
                for (n3 = 0; n3 < n4; ++n3) {
                    stringArray2[n3] = PacketCodec.readString(class_25402);
                }
                n3 = RunicMpFix.boundedCount(PacketCodec.readInt(class_25402));
                if (n3 < 0) {
                    return;
                }
                stringArray = new String[n3];
                for (int i = 0; i < n3; ++i) {
                    stringArray[i] = PacketCodec.readString(class_25402);
                }
            } else {
                return;
            }
            minecraftServer.execute(() -> RunicMpFix.process(class_32222, n2, n, stringArray2, stringArray));
        });
        System.out.println("[Runic Attunement] 1.3.8 initialised (multiplayer action packets enabled)");
    }

    private static int boundedCount(int n) {
        return n >= 0 && n <= 64 ? n : -1;
    }

    private static void process(class_3222 class_32222, int n, int n2, String[] stringArray, String[] stringArray2) {
        if (RunicUnravelRuntime.handleMpAction(class_32222, n, n2)) {
            return;
        }
        try {
            Object object = RunicMpFix.findRunicHandler(class_32222);
            if (object == null) {
                System.err.println("[Runic Attunement] Rejected action: player does not have a Runic Attunement screen open.");
                RunicMpFix.feedback(class_32222, "Runic action rejected: the server no longer has this Runic screen open.");
                return;
            }
            if (n == 0) {
                Method method = object.getClass().getMethod("getAttunedEnchantments", new Class[0]);
                List list = (List)method.invoke(object, new Object[0]);
                if (n2 < 0 || n2 >= list.size()) {
                    System.err.println("[Runic Attunement] Rejected Attunement: stale/invalid enchantment index " + n2 + ".");
                    RunicMpFix.feedback(class_32222, "Runic Attunement rejected: stale enchantment selection. Reopen the table.");
                    return;
                }
                Object e = list.get(n2);
                Method method2 = object.getClass().getDeclaredMethod("tryInscribe", class_3222.class, class_2960.class);
                method2.setAccessible(true);
                boolean bl = Boolean.TRUE.equals(method2.invoke(object, class_32222, e));
                if (!bl) {
                    System.err.println("[Runic Attunement] Server rejected Attunement for " + String.valueOf(e) + ". Check materials, raw XP, compatibility, shelf state and table range.");
                    RunicMpFix.feedback(class_32222, "Runic Attunement rejected by server. Check materials, raw XP, compatibility and shelf/table state.");
                }
                return;
            }
            Set<Object> set = RunicMpFix.getSetField(object, "selectedNormal");
            Set<Object> set2 = RunicMpFix.getSetField(object, "removeCurses");
            set.clear();
            set2.clear();
            for (String string : stringArray) {
                set.add(RunicMpFix.parseIdentifier(string));
            }
            for (String string : stringArray2) {
                set2.add(RunicMpFix.parseIdentifier(string));
            }
            Method method = object.getClass().getDeclaredMethod("tryTransmute", class_3222.class);
            method.setAccessible(true);
            int n3 = Boolean.TRUE.equals(method.invoke(object, class_32222)) ? 1 : 0;
            if (n3 == 0) {
                System.err.println("[Runic Attunement] Server rejected Transmutation. Selected=" + set.size() + ", removeCurses=" + set2.size() + ". Check source/target compatibility, materials, raw XP and screen/table state.");
                RunicMpFix.feedback(class_32222, "Runic Transmutation rejected by server. Check source/target compatibility, materials and raw XP.");
            }
        }
        catch (Throwable throwable) {
            System.err.println("[Runic Attunement] Action processing failed: " + String.valueOf(throwable));
            throwable.printStackTrace();
            RunicMpFix.feedback(class_32222, "Runic multiplayer action failed internally. Check latest.log for [Runic Attunement].");
        }
    }

    private static void feedback(class_3222 class_32222, String string) {
        try {
            class_32222.method_7353((class_2561)class_2561.method_43470((String)string), true);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static Object findRunicHandler(class_3222 class_32222) throws IllegalAccessException {
        for (Class<?> clazz = class_32222.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            for (Field field : clazz.getDeclaredFields()) {
                Object object;
                field.setAccessible(true);
                try {
                    object = field.get(class_32222);
                }
                catch (Throwable throwable) {
                    continue;
                }
                if (object == null || !object.getClass().getName().equals("dev.runicattunement.screen.RunicInscriptionScreenHandler")) continue;
                return object;
            }
        }
        return null;
    }

    private static Set<Object> getSetField(Object object, String string) throws Exception {
        Field field = object.getClass().getDeclaredField(string);
        field.setAccessible(true);
        return (Set)field.get(object);
    }

    private static class_2960 parseIdentifier(String string) {
        int n = string.indexOf(58);
        if (n > 0 && n < string.length() - 1) {
            return new class_2960(string.substring(0, n), string.substring(n + 1));
        }
        return new class_2960("minecraft", string);
    }

    static final class PacketCodec {
        private PacketCodec() {
        }

        static int readInt(Object object) {
            try {
                return ((Number)object.getClass().getMethod("readInt", new Class[0]).invoke(object, new Object[0])).intValue();
            }
            catch (ReflectiveOperationException reflectiveOperationException) {
                throw new IllegalStateException("Could not read Runic MP packet int", reflectiveOperationException);
            }
        }

        static String readString(Object object) {
            int n = PacketCodec.readInt(object);
            if (n < 0 || n > Short.MAX_VALUE) {
                throw new IllegalArgumentException("Invalid Runic MP packet string length: " + n);
            }
            byte[] byArray = new byte[n];
            try {
                object.getClass().getMethod("readBytes", byte[].class).invoke(object, new Object[]{byArray});
            }
            catch (ReflectiveOperationException reflectiveOperationException) {
                throw new IllegalStateException("Could not read Runic MP packet bytes", reflectiveOperationException);
            }
            return new String(byArray, StandardCharsets.UTF_8);
        }
    }
}

