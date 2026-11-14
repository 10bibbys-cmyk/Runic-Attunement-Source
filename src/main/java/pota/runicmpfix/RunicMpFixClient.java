/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
 *  net.fabricmc.fabric.api.networking.v1.PacketByteBufs
 *  net.minecraft.class_2540
 *  net.minecraft.class_2960
 */
package pota.runicmpfix;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.class_2540;
import net.minecraft.class_2960;
import pota.runicmpfix.RunicMpFix;

public final class RunicMpFixClient {
    private static final int BUTTON_INSCRIBE_BASE = 1000;
    private static final int BUTTON_TOGGLE_TRANSFER_BASE = 2000;
    private static final int BUTTON_TRANSMUTE = 5000;

    private RunicMpFixClient() {
    }

    public static boolean interceptFinalAction(Object object, int n) {
        if (n >= 6000 && n < 7000) {
            class_2540 class_25402 = PacketByteBufs.create();
            RunicMpFixClient.writeInt(class_25402, 0);
            RunicMpFixClient.writeInt(class_25402, n);
            ClientPlayNetworking.send((class_2960)RunicMpFix.ACTION, (class_2540)class_25402);
            return true;
        }
        if (n >= 1000 && n < 2000) {
            class_2540 class_25403 = PacketByteBufs.create();
            RunicMpFixClient.writeInt(class_25403, 0);
            RunicMpFixClient.writeInt(class_25403, n - 1000);
            ClientPlayNetworking.send((class_2960)RunicMpFix.ACTION, (class_2540)class_25403);
            return true;
        }
        if (n == 5000) {
            try {
                Set<?> set = RunicMpFixClient.getSetField(object, "selectedTransfers");
                Set<?> set2 = RunicMpFixClient.getSetField(object, "removeCurses");
                class_2540 class_25404 = PacketByteBufs.create();
                RunicMpFixClient.writeInt(class_25404, 1);
                RunicMpFixClient.writeInt(class_25404, Math.min(set.size(), 64));
                int n2 = 0;
                for (Object obj : set) {
                    if (n2++ >= 64) break;
                    RunicMpFixClient.writeString(class_25404, String.valueOf(obj));
                }
                RunicMpFixClient.writeInt(class_25404, Math.min(set2.size(), 64));
                n2 = 0;
                for (Object obj : set2) {
                    if (n2++ >= 64) break;
                    RunicMpFixClient.writeString(class_25404, String.valueOf(obj));
                }
                ClientPlayNetworking.send((class_2960)RunicMpFix.ACTION, (class_2540)class_25404);
                return true;
            }
            catch (Throwable throwable) {
                System.err.println("[Runic Attunement] Could not serialize Transmutation selection; falling back to Runic legacy button packet: " + String.valueOf(throwable));
                return false;
            }
        }
        return false;
    }

    private static void writeInt(Object object, int n) {
        try {
            object.getClass().getMethod("writeInt", Integer.TYPE).invoke(object, n);
        }
        catch (ReflectiveOperationException reflectiveOperationException) {
            throw new IllegalStateException("Could not write Runic MP packet int", reflectiveOperationException);
        }
    }

    private static void writeString(Object object, String string) {
        byte[] byArray = string.getBytes(StandardCharsets.UTF_8);
        if (byArray.length > Short.MAX_VALUE) {
            throw new IllegalArgumentException("Runic MP packet string is too long");
        }
        RunicMpFixClient.writeInt(object, byArray.length);
        try {
            object.getClass().getMethod("writeBytes", byte[].class).invoke(object, new Object[]{byArray});
        }
        catch (ReflectiveOperationException reflectiveOperationException) {
            throw new IllegalStateException("Could not write Runic MP packet bytes", reflectiveOperationException);
        }
    }

    private static Set<?> getSetField(Object object, String string) throws Exception {
        Field field = object.getClass().getDeclaredField(string);
        field.setAccessible(true);
        return (Set)field.get(object);
    }
}

