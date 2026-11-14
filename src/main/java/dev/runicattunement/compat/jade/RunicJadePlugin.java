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
public final class RunicJadePlugin implements IWailaPlugin {
   private static final class_2960 ID = new class_2960("runic_attunement", "tablet_inscription");

   public void registerClient(IWailaClientRegistration var1) {
      var1.registerBlockComponent(RunicJadePlugin.TabletComponent.INSTANCE, class_7714.class);
   }

   private enum TabletComponent implements IBlockComponentProvider {
      INSTANCE;

      private static final Method JADE_HIT_BOOK = resolveHitBookMethod();

      private static Method resolveHitBookMethod() {
         try {
            Class var0 = Class.forName("snownee.jade.addon.vanilla.ChiseledBookshelfProvider");
            Method var1 = var0.getDeclaredMethod("getHitBook", BlockAccessor.class);
            var1.setAccessible(true);
            return var1;
         } catch (ReflectiveOperationException | SecurityException | LinkageError var2) {
            return null;
         }
      }

      public class_2960 getUid() {
         return RunicJadePlugin.ID;
      }

      public void appendTooltip(ITooltip var1, BlockAccessor var2, IPluginConfig var3) {
         if (!var2.showDetails() && JADE_HIT_BOOK != null) {
            class_1799 var4;
            try {
               var4 = (class_1799)JADE_HIT_BOOK.invoke(null, var2);
            } catch (ReflectiveOperationException | RuntimeException var9) {
               return;
            }

            if (var4 != null && var4.method_7909() instanceof RunicTabletItem) {
               Optional var5 = RunicTabletItem.getEnchantmentId(var4);
               if (!var5.isEmpty()) {
                  Optional<class_1887> var6 = RunicTabletItem.getEnchantment(var4);
                  String var7 = var6.<String>map(class_1887::method_8184)
                     .orElseGet(() -> "enchantment." + ((class_2960)var5.get()).toString().replace(':', '.'));
                  class_5250 var8 = class_2561.method_43471(var7);
                  var1.add(class_2561.method_43469("jade.runic_attunement.tablet_inscription", new Object[]{var8}));
               }
            }
         }
      }
   }
}
