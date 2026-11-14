/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1657
 *  net.minecraft.class_1703
 *  net.minecraft.class_1706
 *  net.minecraft.class_1713
 *  net.minecraft.class_1799
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package dev.runicattunement.mixin;

import dev.runicattunement.reforging.RunicReforging;
import net.minecraft.class_1657;
import net.minecraft.class_1703;
import net.minecraft.class_1706;
import net.minecraft.class_1713;
import net.minecraft.class_1799;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={class_1703.class})
public abstract class ScreenHandlerMixin {
    @Unique
    private boolean runicAttunement$substitutedReforgeResult;

    @Inject(method={"onSlotClick"}, at={@At(value="HEAD")})
    private void runicAttunement$rollReforgeOnTake(int slotIndex, int button, class_1713 actionType, class_1657 player, CallbackInfo ci) {
        ScreenHandlerMixin screenHandlerMixin;
        if (player.method_37908().field_9236 || slotIndex != 2 || !((screenHandlerMixin = this) instanceof class_1706)) {
            return;
        }
        class_1706 anvil = (class_1706)screenHandlerMixin;
        class_1799 tablet = anvil.method_7611(0).method_7677();
        class_1799 dust = anvil.method_7611(1).method_7677();
        if (!RunicReforging.isRunicReforge(tablet, dust)) {
            return;
        }
        class_1799 actual = RunicReforging.createRandomResult(tablet, dust, player.method_6051());
        anvil.method_7611(2).method_48931(actual);
        this.runicAttunement$substitutedReforgeResult = true;
    }

    @Inject(method={"onSlotClick"}, at={@At(value="RETURN")})
    private void runicAttunement$restoreReforgePreview(int slotIndex, int button, class_1713 actionType, class_1657 player, CallbackInfo ci) {
        if (!this.runicAttunement$substitutedReforgeResult) {
            return;
        }
        this.runicAttunement$substitutedReforgeResult = false;
        ScreenHandlerMixin screenHandlerMixin = this;
        if (!(screenHandlerMixin instanceof class_1706)) {
            return;
        }
        class_1706 anvil = (class_1706)screenHandlerMixin;
        class_1799 tablet = anvil.method_7611(0).method_7677();
        class_1799 dust = anvil.method_7611(1).method_7677();
        if (RunicReforging.isRunicReforge(tablet, dust)) {
            anvil.method_7611(2).method_48931(RunicReforging.createPreview(tablet, dust));
            anvil.method_7623();
        }
    }
}

