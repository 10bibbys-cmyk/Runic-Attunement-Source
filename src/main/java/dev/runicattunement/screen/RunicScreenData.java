/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2338
 *  net.minecraft.class_2540
 *  net.minecraft.class_2960
 */
package dev.runicattunement.screen;

import dev.runicattunement.compat.RunicCompatibility;
import dev.runicattunement.config.RunicConfig;
import dev.runicattunement.data.EnchantmentDefinition;
import dev.runicattunement.data.IngredientCost;
import dev.runicattunement.data.TransmutationCosts;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.class_2338;
import net.minecraft.class_2540;
import net.minecraft.class_2960;

public record RunicScreenData(class_2338 tablePos, List<class_2960> attunedEnchantments, Map<class_2960, EnchantmentDefinition> definitions, Map<Integer, List<IngredientCost>> universalCosts, TransmutationCosts transmutationCosts, RunicConfig.ExperienceCosts experienceSettings, RunicConfig.Transmutation transmutationSettings) {
    public RunicScreenData {
        attunedEnchantments = List.copyOf(attunedEnchantments);
        definitions = Map.copyOf(definitions);
        universalCosts = Map.copyOf(universalCosts);
    }

    public void write(class_2540 buf) {
        buf.method_10807(this.tablePos);
        buf.method_10804(this.attunedEnchantments.size());
        for (class_2960 class_29602 : this.attunedEnchantments) {
            buf.method_10812(class_29602);
        }
        buf.method_10804(this.definitions.size());
        for (EnchantmentDefinition enchantmentDefinition : this.definitions.values()) {
            enchantmentDefinition.write(buf);
        }
        buf.method_10804(this.universalCosts.size());
        for (Map.Entry entry : this.universalCosts.entrySet()) {
            buf.method_10804(((Integer)entry.getKey()).intValue());
            buf.method_10804(((List)entry.getValue()).size());
            for (IngredientCost cost : (List)entry.getValue()) {
                cost.write(buf);
            }
        }
        this.transmutationCosts.write(buf);
        buf.writeBoolean(this.experienceSettings.enabled());
        buf.writeDouble(this.experienceSettings.globalMultiplier());
        buf.writeDouble(this.experienceSettings.transmutationMultiplier());
        buf.writeBoolean(this.transmutationSettings.enabled());
        buf.writeDouble(this.transmutationSettings.sourceItemDestructionChance());
        buf.writeDouble(this.transmutationSettings.baseEnchantUnits());
        buf.writeDouble(this.transmutationSettings.tierUnitsPerTier());
        buf.writeDouble(this.transmutationSettings.extraLevelUnits());
        buf.writeDouble(this.transmutationSettings.curseRetentionCostMultiplier());
        RunicCompatibility.writeSync(buf);
    }

    public static RunicScreenData read(class_2540 buf) {
        class_2338 tablePos = buf.method_10811();
        int attunedCount = buf.method_10816();
        ArrayList<class_2960> attuned = new ArrayList<class_2960>(attunedCount);
        for (int i = 0; i < attunedCount; ++i) {
            attuned.add(buf.method_10810());
        }
        int definitionCount = buf.method_10816();
        LinkedHashMap<class_2960, EnchantmentDefinition> definitions = new LinkedHashMap<class_2960, EnchantmentDefinition>();
        for (int i = 0; i < definitionCount; ++i) {
            EnchantmentDefinition definition = EnchantmentDefinition.read(buf);
            definitions.put(definition.enchantmentId(), definition);
        }
        int universalLevelCount = buf.method_10816();
        LinkedHashMap<Integer, List<IngredientCost>> universal = new LinkedHashMap<Integer, List<IngredientCost>>();
        for (int i = 0; i < universalLevelCount; ++i) {
            int level = buf.method_10816();
            int costCount = buf.method_10816();
            ArrayList<IngredientCost> costs = new ArrayList<IngredientCost>(costCount);
            for (int j = 0; j < costCount; ++j) {
                costs.add(IngredientCost.read(buf));
            }
            universal.put(level, List.copyOf(costs));
        }
        TransmutationCosts transmutationCosts = TransmutationCosts.read(buf);
        RunicConfig.ExperienceCosts experienceSettings = new RunicConfig.ExperienceCosts(buf.readBoolean(), buf.readDouble(), buf.readDouble());
        RunicConfig.Transmutation settings = new RunicConfig.Transmutation(buf.readBoolean(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble());
        RunicScreenData runicScreenData = new RunicScreenData(tablePos, attuned, definitions, universal, transmutationCosts, experienceSettings, settings);
        RunicCompatibility.readSync(buf);
        return runicScreenData;
    }
}

