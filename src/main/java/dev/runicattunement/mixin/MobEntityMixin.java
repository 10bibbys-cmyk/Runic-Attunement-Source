/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1266
 *  net.minecraft.class_1308
 *  net.minecraft.class_5819
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package dev.runicattunement.mixin;

import dev.runicattunement.config.RunicConfig;
import net.minecraft.class_1266;
import net.minecraft.class_1308;
import net.minecraft.class_5819;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_1308.class})
public abstract class MobEntityMixin {
    @Inject(method={"updateEnchantments"}, at={@At(value="HEAD")}, cancellable=true)
    private void runicAttunement$disableSpawnEnchantments(class_5819 random, class_1266 difficulty, CallbackInfo ci) {
        if (RunicConfig.enchantmentBypassControl().disableMobSpawnEnchantedGear()) {
            ci.cancel();
        }
    }
}

