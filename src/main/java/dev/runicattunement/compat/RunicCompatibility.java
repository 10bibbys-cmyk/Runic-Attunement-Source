/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
 *  net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
 *  net.fabricmc.fabric.api.resource.ResourceManagerHelper
 *  net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
 *  net.minecraft.class_1799
 *  net.minecraft.class_1887
 *  net.minecraft.class_2540
 *  net.minecraft.class_2960
 *  net.minecraft.class_3264
 *  net.minecraft.class_3298
 *  net.minecraft.class_3300
 *  net.minecraft.class_5321
 *  net.minecraft.class_6862
 *  net.minecraft.class_7923
 *  net.minecraft.class_7924
 */
package dev.runicattunement.compat;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.runicattunement.RunicAttunement;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.class_1799;
import net.minecraft.class_1887;
import net.minecraft.class_2540;
import net.minecraft.class_2960;
import net.minecraft.class_3264;
import net.minecraft.class_3298;
import net.minecraft.class_3300;
import net.minecraft.class_5321;
import net.minecraft.class_6862;
import net.minecraft.class_7923;
import net.minecraft.class_7924;

public final class RunicCompatibility {
    private static final String PREFIX = "runic_attunement/compatibility";
    private static final int MAX_SYNC_BYTES = 131072;
    private static volatile Map<String, Rule> serverRules = Map.of();
    private static volatile Map<String, Rule> clientRules = Map.of();
    private static volatile boolean serverInitialized = false;

    private RunicCompatibility() {
    }

    public static void initialize() {
        ResourceManagerHelper.get((class_3264)class_3264.field_14190).registerReloadListener((IdentifiableResourceReloadListener)new SimpleSynchronousResourceReloadListener(){

            public class_2960 getFabricId() {
                return RunicAttunement.id("enchantment_compatibility");
            }

            public void method_14491(class_3300 class_33002) {
                RunicCompatibility.reload(class_33002);
            }
        });
        ServerLifecycleEvents.SERVER_STOPPED.register(class_31762 -> {
            serverRules = Map.of();
            serverInitialized = false;
        });
    }

    private static synchronized void reload(class_3300 class_33002) {
        LinkedHashMap<String, Rule> linkedHashMap = new LinkedHashMap<String, Rule>();
        int n = 0;
        for (Map.Entry entry : class_33002.method_14488(PREFIX, class_29602 -> class_29602.method_12832().endsWith(".json")).entrySet()) {
            try (InputStreamReader inputStreamReader = new InputStreamReader(((class_3298)entry.getValue()).method_14482(), StandardCharsets.UTF_8);){
                JsonObject jsonObject = JsonParser.parseReader((Reader)inputStreamReader).getAsJsonObject();
                Rule rule = RunicCompatibility.parse(jsonObject);
                if (linkedHashMap.putIfAbsent(rule.enchantment(), rule) == null) continue;
                RunicAttunement.LOGGER.warn("Runic compatibility: duplicate rule for {} at {}; keeping first", new Object[]{rule.enchantment(), entry.getKey()});
                ++n;
            }
            catch (Exception exception) {
                ++n;
                RunicAttunement.LOGGER.warn("Runic compatibility: invalid rule {}: {}", new Object[]{entry.getKey(), exception.toString()});
            }
        }
        serverRules = Map.copyOf(linkedHashMap);
        serverInitialized = true;
        RunicAttunement.LOGGER.info("Runic compatibility loaded {} override(s), {} rejected; native rules remain the fallback", new Object[]{linkedHashMap.size(), n});
    }

    private static Rule parse(JsonObject jsonObject) {
        String string;
        if (jsonObject == null || !jsonObject.has("enchantment")) {
            throw new IllegalArgumentException("Missing enchantment ID");
        }
        String string2 = jsonObject.get("enchantment").getAsString();
        RunicCompatibility.validateId(string2);
        String string3 = string = jsonObject.has("mode") ? jsonObject.get("mode").getAsString().toLowerCase(Locale.ROOT) : "extend";
        if (!string.equals("extend") && !string.equals("replace")) {
            throw new IllegalArgumentException("mode must be extend or replace");
        }
        List<String> list = RunicCompatibility.parseList(jsonObject, "allow");
        List<String> list2 = RunicCompatibility.parseList(jsonObject, "deny");
        return new Rule(string2, string.equals("replace"), RunicCompatibility.selectors(list), RunicCompatibility.selectors(list2), List.copyOf(list), List.copyOf(list2));
    }

    private static List<String> parseList(JsonObject jsonObject, String string) {
        ArrayList<String> arrayList = new ArrayList<String>();
        if (!jsonObject.has(string)) {
            return arrayList;
        }
        JsonElement jsonElement = jsonObject.get(string);
        if (!jsonElement.isJsonArray()) {
            throw new IllegalArgumentException(string + " must be a JSON array");
        }
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        if (jsonArray.size() > 1024) {
            throw new IllegalArgumentException(string + " exceeds 1024 entries");
        }
        for (JsonElement jsonElement2 : jsonArray) {
            String string2 = jsonElement2.getAsString();
            RunicCompatibility.validateId(string2.startsWith("#") ? string2.substring(1) : string2);
            arrayList.add(string2);
        }
        return arrayList;
    }

    private static void validateId(String string) {
        if (string.length() > 256 || !string.matches("[a-z0-9_.-]+:[a-z0-9_./-]+")) {
            throw new IllegalArgumentException("Invalid namespaced identifier: " + string);
        }
    }

    private static List<Selector> selectors(List<String> list) {
        ArrayList<Selector> arrayList = new ArrayList<Selector>();
        for (String string : list) {
            if (string.startsWith("#")) {
                class_6862 class_68622 = class_6862.method_40092((class_5321)class_7924.field_41197, (class_2960)new class_2960(string.substring(1)));
                arrayList.add(new Selector(string, null, class_68622));
                continue;
            }
            arrayList.add(new Selector(string, string, null));
        }
        return List.copyOf(arrayList);
    }

    public static boolean accepts(class_1887 class_18872, class_1799 class_17992) {
        boolean bl = class_18872.method_8192(class_17992);
        class_2960 class_29602 = class_7923.field_41176.method_10221((Object)class_18872);
        if (class_29602 == null) {
            return bl;
        }
        Rule rule = (serverInitialized ? serverRules : clientRules).get(class_29602.toString());
        if (rule == null) {
            return bl;
        }
        class_2960 class_29603 = class_7923.field_41178.method_10221((Object)class_17992.method_7909());
        return rule.accepts(bl, class_17992, class_29603 == null ? "" : class_29603.toString());
    }

    public static void writeSync(class_2540 class_25402) {
        JsonArray jsonArray = new JsonArray();
        for (Rule rule : serverRules.values()) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("enchantment", rule.enchantment());
            jsonObject.addProperty("mode", rule.replace() ? "replace" : "extend");
            JsonArray jsonArray2 = new JsonArray();
            for (String string : rule.rawAllow()) {
                jsonArray2.add(string);
            }
            JsonArray jsonArray3 = new JsonArray();
            for (String string : rule.rawDeny()) {
                jsonArray3.add(string);
            }
            jsonObject.add("allow", (JsonElement)jsonArray2);
            jsonObject.add("deny", (JsonElement)jsonArray3);
            jsonArray.add((JsonElement)jsonObject);
        }
        Object object = jsonArray.toString().getBytes(StandardCharsets.UTF_8);
        if (((Object)object).length > 131072) {
            RunicAttunement.LOGGER.warn("Runic compatibility sync is too large ({} bytes); sending no overrides", new Object[]{((Object)object).length});
            object = "[]".getBytes(StandardCharsets.UTF_8);
        }
        class_25402.method_10804(((Object)object).length);
        class_25402.writeBytes((byte[])object);
    }

    public static void readSync(class_2540 class_25402) {
        int n = class_25402.method_10816();
        if (n < 0 || n > 131072) {
            throw new IllegalArgumentException("Runic compatibility sync exceeds maximum size");
        }
        byte[] byArray = new byte[n];
        class_25402.readBytes(byArray);
        LinkedHashMap<String, Rule> linkedHashMap = new LinkedHashMap<String, Rule>();
        JsonElement jsonElement = JsonParser.parseString((String)new String(byArray, StandardCharsets.UTF_8));
        if (!jsonElement.isJsonArray()) {
            throw new IllegalArgumentException("Invalid compatibility sync JSON");
        }
        for (JsonElement jsonElement2 : jsonElement.getAsJsonArray()) {
            Rule rule = RunicCompatibility.parse(jsonElement2.getAsJsonObject());
            if (linkedHashMap.putIfAbsent(rule.enchantment(), rule) == null) continue;
            throw new IllegalArgumentException("Duplicate synchronized enchantment ID");
        }
        clientRules = Map.copyOf(linkedHashMap);
    }

    private record Rule(String enchantment, boolean replace, List<Selector> allow, List<Selector> deny, List<String> rawAllow, List<String> rawDeny) {
        boolean matches(List<Selector> list, class_1799 class_17992, String string) {
            for (Selector selector : list) {
                if (!selector.matches(class_17992, string)) continue;
                return true;
            }
            return false;
        }

        boolean accepts(boolean bl, class_1799 class_17992, String string) {
            if (this.matches(this.deny, class_17992, string)) {
                return false;
            }
            if (this.replace) {
                return this.matches(this.allow, class_17992, string);
            }
            return bl || this.matches(this.allow, class_17992, string);
        }
    }

    private record Selector(String source, String item, class_6862 tag) {
        boolean matches(class_1799 class_17992, String string) {
            return this.tag != null ? class_17992.method_31573(this.tag) : this.item.equals(string);
        }
    }
}

