package dev.runicattunement.data;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_2540;

public record LevelCost(int level, int experiencePoints, List<IngredientCost> ingredients) {
   public LevelCost {
      if (experiencePoints < 0) {
         throw new IllegalArgumentException("Experience point cost cannot be negative");
      }

      ingredients = List.copyOf(ingredients);
   }

   public void write(class_2540 buf) {
      buf.method_10804(this.level);
      buf.method_10804(this.experiencePoints);
      buf.method_10804(this.ingredients.size());

      for (IngredientCost ingredient : this.ingredients) {
         ingredient.write(buf);
      }
   }

   public static LevelCost read(class_2540 buf) {
      int level = buf.method_10816();
      int experiencePoints = buf.method_10816();
      int count = buf.method_10816();
      List<IngredientCost> ingredients = new ArrayList<>(count);

      for (int i = 0; i < count; i++) {
         ingredients.add(IngredientCost.read(buf));
      }

      return new LevelCost(level, experiencePoints, ingredients);
   }
}
