/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1657
 *  net.minecraft.class_1706
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_3222
 *  net.minecraft.class_3915
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package dev.runicattunement.mixin;

import dev.runicattunement.advancement.RunicAdvancements;
import dev.runicattunement.bypass.RunicEnchantmentBypass;
import dev.runicattunement.config.RunicConfig;
import dev.runicattunement.item.RunicTabletItem;
import dev.runicattunement.reforging.RunicReforging;
import net.minecraft.class_1657;
import net.minecraft.class_1706;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_3222;
import net.minecraft.class_3915;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={class_1706.class})
public abstract class AnvilScreenHandlerMixin {
    @Shadow
    @Final
    private class_3915 field_7770;
    @Shadow
    private int field_7776;

    @Inject(method={"updateResult"}, at={@At(value="HEAD")}, cancellable=true)
    private void runicAttunement$buildReforgePreview(CallbackInfo ci) {
        class_1799 dust;
        class_1706 self = (class_1706)this;
        class_1799 tablet = self.method_7611(0).method_7677();
        if (!RunicReforging.isRunicReforge(tablet, dust = self.method_7611(1).method_7677())) {
            return;
        }
        class_1799 preview = RunicReforging.createPreview(tablet, dust);
        self.method_7611(2).method_48931(preview);
        this.field_7776 = 1;
        this.field_7770.method_17404(0);
        self.method_7623();
        ci.cancel();
    }

    @Inject(method={"canTakeOutput"}, at={@At(value="HEAD")}, cancellable=true)
    private void runicAttunement$allowFreeReforge(class_1657 player, boolean present, CallbackInfoReturnable<Boolean> cir) {
        if (!present) {
            return;
        }
        class_1706 self = (class_1706)this;
        if (RunicReforging.isRunicReforge(self.method_7611(0).method_7677(), self.method_7611(1).method_7677())) {
            cir.setReturnValue((Object)true);
        }
    }

    @Inject(method={"onTakeOutput"}, at={@At(value="HEAD")})
    private void runicAttunement$reforgeAdvancement(class_1657 player, class_1799 stack, CallbackInfo ci) {
        RunicTabletItem result;
        RunicTabletItem start;
        class_1799 inputTablet;
        class_1706 self;
        class_3222 serverPlayer;
        block6: {
            block5: {
                if (player.method_37908().field_9236 || !(player instanceof class_3222)) {
                    return;
                }
                serverPlayer = (class_3222)player;
                self = (class_1706)this;
                inputTablet = self.method_7611(0).method_7677();
                class_1792 class_17922 = inputTablet.method_7909();
                if (!(class_17922 instanceof RunicTabletItem)) break block5;
                start = (RunicTabletItem)class_17922;
                class_17922 = stack.method_7909();
                if (!(class_17922 instanceof RunicTabletItem)) break block5;
                result = (RunicTabletItem)class_17922;
                if (!RunicTabletItem.isReforgePreview(stack)) break block6;
            }
            return;
        }
        if (RunicReforging.isRunicReforge(inputTablet, self.method_7611(1).method_7677())) {
            RunicAdvancements.triggerReforge(serverPlayer, result.tier() > start.tier());
        }
    }

    @Inject(method={"updateResult"}, at={@At(value="RETURN")})
    private void runicAttunement$blockAnvilEnchanting(CallbackInfo ci) {
        class_1799 result;
        if (!RunicConfig.enchantmentBypassControl().disableAnvilEnchanting()) {
            return;
        }
        class_1706 self = (class_1706)this;
        class_1799 base = self.method_7611(0).method_7677();
        if (!RunicEnchantmentBypass.wouldAddOrIncreaseEnchantments(base, result = self.method_7611(2).method_7677())) {
            return;
        }
        self.method_7611(2).method_48931(class_1799.field_8037);
        this.field_7770.method_17404(0);
        self.method_7623();
    }
}

