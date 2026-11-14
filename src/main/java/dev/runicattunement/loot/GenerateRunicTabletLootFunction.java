package dev.runicattunement.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import dev.runicattunement.data.RunicDefinitionManager;
import dev.runicattunement.item.RunicTabletItem;
import dev.runicattunement.registry.ModLootFunctions;
import java.util.List;
import net.minecraft.class_120;
import net.minecraft.class_156;
import net.minecraft.class_1799;
import net.minecraft.class_2960;
import net.minecraft.class_47;
import net.minecraft.class_5339;
import net.minecraft.class_5341;
import net.minecraft.class_117.class_118;
import net.minecraft.class_120.class_123;

public final class GenerateRunicTabletLootFunction extends class_120 {
   private GenerateRunicTabletLootFunction(class_5341[] conditions) {
      super(conditions);
   }

   public static class_118 builder() {
      return class_120.method_520(GenerateRunicTabletLootFunction::new);
   }

   @Override
   public class_1799 apply(class_1799 stack, class_47 context) {
      return this.method_522(stack, context);
   }

   public class_5339 method_29321() {
      return ModLootFunctions.GENERATE_RUNIC_TABLET;
   }

   protected class_1799 method_522(class_1799 stack, class_47 context) {
      if (stack.method_7909() instanceof RunicTabletItem tablet) {
         List<class_2960> pool = RunicDefinitionManager.tierPool(tablet.tier());
         if (pool.isEmpty()) {
            return class_1799.field_8037;
         }

         class_2960 enchantmentId = (class_2960)class_156.method_32309(pool, context.method_294());
         RunicTabletItem.setEnchantment(stack, enchantmentId);
         return stack;
      } else {
         return stack;
      }
   }

   public static final class Serializer extends class_123<GenerateRunicTabletLootFunction> {
      @Override
      public GenerateRunicTabletLootFunction method_530(JsonObject json, JsonDeserializationContext context, class_5341[] conditions) {
         return this.fromJson(json, context, conditions);
      }

      @Override
      public void method_516(JsonObject json, GenerateRunicTabletLootFunction function, JsonSerializationContext context) {
         super.method_529(json, function, context);
      }

      @Override
      public GenerateRunicTabletLootFunction method_517(JsonObject json, JsonDeserializationContext context) {
         return (GenerateRunicTabletLootFunction)super.method_528(json, context);
      }

      public GenerateRunicTabletLootFunction fromJson(JsonObject json, JsonDeserializationContext context, class_5341[] conditions) {
         return new GenerateRunicTabletLootFunction(conditions);
      }
   }
}
