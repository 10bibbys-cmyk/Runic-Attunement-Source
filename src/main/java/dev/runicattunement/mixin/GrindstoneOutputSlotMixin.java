/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1657
 *  net.minecraft.class_1799
 *  net.minecraft.class_3222
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package dev.runicattunement.mixin;

import dev.runicattunement.advancement.RunicAdvancements;
import dev.runicattunement.item.RunicDustItem;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_3222;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets={"net.minecraft.screen.GrindstoneScreenHandler$4"})
public abstract class GrindstoneOutputSlotMixin {
    @Inject(method={"onTakeItem"}, at={@At(value="HEAD")})
    private void runicAttunement$grindAdvancement(class_1657 player, class_1799 stack, CallbackInfo ci) {
        if (!player.method_37908().field_9236 && player instanceof class_3222) {
            class_3222 serverPlayer = (class_3222)player;
            if (stack.method_7909() instanceof RunicDustItem) {
                RunicAdvancements.triggerGrind(serverPlayer);
            }
        }
    }
}

