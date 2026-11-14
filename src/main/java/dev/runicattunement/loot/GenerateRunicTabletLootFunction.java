/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonDeserializationContext
 *  com.google.gson.JsonObject
 *  net.minecraft.class_117$class_118
 *  net.minecraft.class_120
 *  net.minecraft.class_120$class_123
 *  net.minecraft.class_156
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_2960
 *  net.minecraft.class_47
 *  net.minecraft.class_5339
 *  net.minecraft.class_5341
 *  net.minecraft.class_5819
 */
package dev.runicattunement.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import dev.runicattunement.data.RunicDefinitionManager;
import dev.runicattunement.item.RunicTabletItem;
import dev.runicattunement.registry.ModLootFunctions;
import java.util.List;
import net.minecraft.class_117;
import net.minecraft.class_120;
import net.minecraft.class_156;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_2960;
import net.minecraft.class_47;
import net.minecraft.class_5339;
import net.minecraft.class_5341;
import net.minecraft.class_5819;

public final class GenerateRunicTabletLootFunction
extends class_120 {
    private GenerateRunicTabletLootFunction(class_5341[] conditions) {
        super(conditions);
    }

    public static class_117.class_118 builder() {
        return class_120.method_520(GenerateRunicTabletLootFunction::new);
    }

    public class_5339 method_29321() {
        return ModLootFunctions.GENERATE_RUNIC_TABLET;
    }

    protected class_1799 method_522(class_1799 stack, class_47 context) {
        class_1792 class_17922 = stack.method_7909();
        if (!(class_17922 instanceof RunicTabletItem)) {
            return stack;
        }
        RunicTabletItem tablet = (RunicTabletItem)class_17922;
        List<class_2960> pool = RunicDefinitionManager.tierPool(tablet.tier());
        if (pool.isEmpty()) {
            return class_1799.field_8037;
        }
        class_2960 enchantmentId = (class_2960)class_156.method_32309(pool, (class_5819)context.method_294());
        RunicTabletItem.setEnchantment(stack, enchantmentId);
        return stack;
    }

    public static final class Serializer
    extends class_120.class_123<GenerateRunicTabletLootFunction> {
        public GenerateRunicTabletLootFunction fromJson(JsonObject json, JsonDeserializationContext context, class_5341[] conditions) {
            return new GenerateRunicTabletLootFunction(conditions);
        }
    }
}

