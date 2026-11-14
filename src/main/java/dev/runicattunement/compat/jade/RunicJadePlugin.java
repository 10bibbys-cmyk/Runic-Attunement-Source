/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1799
 *  net.minecraft.class_1887
 *  net.minecraft.class_2561
 *  net.minecraft.class_2960
 *  net.minecraft.class_5250
 *  net.minecraft.class_7714
 *  snownee.jade.api.BlockAccessor
 *  snownee.jade.api.IBlockComponentProvider
 *  snownee.jade.api.ITooltip
 *  snownee.jade.api.IWailaClientRegistration
 *  snownee.jade.api.IWailaPlugin
 *  snownee.jade.api.WailaPlugin
 *  snownee.jade.api.config.IPluginConfig
 */
package dev.runicattunement.compat.jade;

import dev.runicattunement.item.RunicTabletItem;
import java.lang.reflect.Method;
import java.util.Optional;
import net.minecraft.class_1799;
import net.minecraft.class_1887;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_5250;
import net.minecraft.class_7714;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.config.IPluginConfig;

@WailaPlugin
public final class RunicJadePlugin
implements IWailaPlugin {
    private static final class_2960 ID = new class_2960("runic_attunement", "tablet_inscription");

    public void registerClient(IWailaClientRegistration iWailaClientRegistration) {
        iWailaClientRegistration.registerBlockComponent((IBlockComponentProvider)TabletComponent.INSTANCE, class_7714.class);
    }

    private static enum TabletComponent implements IBlockComponentProvider
    {
        INSTANCE;

        private static final Method JADE_HIT_BOOK;

        private static Method resolveHitBookMethod() {
            try {
                Class<?> clazz = Class.forName("snownee.jade.addon.vanilla.ChiseledBookshelfProvider");
                Method method = clazz.getDeclaredMethod("getHitBook", BlockAccessor.class);
                method.setAccessible(true);
                return method;
            }
            catch (LinkageError | ReflectiveOperationException | SecurityException throwable) {
                return null;
            }
        }

        public class_2960 getUid() {
            return ID;
        }

        public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
            class_1799 class_17992;
            if (blockAccessor.showDetails() || JADE_HIT_BOOK == null) {
                return;
            }
            try {
                class_17992 = (class_1799)JADE_HIT_BOOK.invoke(null, blockAccessor);
            }
            catch (ReflectiveOperationException | RuntimeException exception) {
                return;
            }
            if (class_17992 == null || !(class_17992.method_7909() instanceof RunicTabletItem)) {
                return;
            }
            Optional<class_2960> optional = RunicTabletItem.getEnchantmentId(class_17992);
            if (optional.isEmpty()) {
                return;
            }
            Optional<class_1887> optional2 = RunicTabletItem.getEnchantment(class_17992);
            String string = optional2.map(class_1887::method_8184).orElseGet(() -> "enchantment." + ((class_2960)optional.get()).toString().replace(':', '.'));
            class_5250 class_52502 = class_2561.method_43471((String)string);
            iTooltip.add((class_2561)class_2561.method_43469((String)"jade.runic_attunement.tablet_inscription", (Object[])new Object[]{class_52502}));
        }

        static {
            JADE_HIT_BOOK = TabletComponent.resolveHitBookMethod();
        }
    }
}

