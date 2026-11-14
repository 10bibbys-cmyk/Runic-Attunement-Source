/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1799
 *  net.minecraft.class_52
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.ModifyVariable
 */
package dev.runicattunement.mixin;

import dev.runicattunement.bypass.RunicBypassContext;
import dev.runicattunement.bypass.RunicEnchantmentBypass;
import dev.runicattunement.config.RunicConfig;
import java.util.function.Consumer;
import net.minecraft.class_1799;
import net.minecraft.class_52;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value={class_52.class})
public abstract class LootTableMixin {
    @ModifyVariable(method={"generateLoot(Lnet/minecraft/loot/context/LootContext;Ljava/util/function/Consumer;)V"}, at=@At(value="HEAD"), argsOnly=true)
    private Consumer<class_1799> runicAttunement$sanitizeGeneratedLoot(Consumer<class_1799> original) {
        RunicConfig.EnchantmentBypassControl control = RunicConfig.enchantmentBypassControl();
        if (!(control.disableNaturalEnchantedBooks() || control.disableNaturalEnchantedGear() || control.disableFishingEnchantedBooks())) {
            return original;
        }
        boolean fishing = RunicBypassContext.isFishing();
        return stack -> original.accept(RunicEnchantmentBypass.sanitizeGeneratedLoot(stack, fishing));
    }
}

