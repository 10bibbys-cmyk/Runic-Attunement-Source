/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1916
 *  net.minecraft.class_3988
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package dev.runicattunement.mixin;

import dev.runicattunement.bypass.RunicEnchantmentBypass;
import net.minecraft.class_1916;
import net.minecraft.class_3988;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_3988.class})
public abstract class MerchantEntityMixin {
    @Inject(method={"getOffers"}, at={@At(value="RETURN")})
    private void runicAttunement$sanitizeOffers(CallbackInfoReturnable<class_1916> cir) {
        class_3988 self = (class_3988)this;
        if (self.method_37908().field_9236) {
            return;
        }
        RunicEnchantmentBypass.sanitizeTradeOffers((class_1916)cir.getReturnValue());
    }
}

