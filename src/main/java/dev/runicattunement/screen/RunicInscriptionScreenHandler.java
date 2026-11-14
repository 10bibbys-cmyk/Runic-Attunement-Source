package dev.runicattunement.screen;

import dev.runicattunement.advancement.RunicAdvancements;
import dev.runicattunement.attunement.AttunementScanner;
import dev.runicattunement.compat.RunicCompatibility;
import dev.runicattunement.config.RunicConfig;
import dev.runicattunement.data.CostKind;
import dev.runicattunement.data.EnchantmentDefinition;
import dev.runicattunement.data.IngredientCost;
import dev.runicattunement.data.RunicDefinitionManager;
import dev.runicattunement.data.TransmutationCosts;
import dev.runicattunement.effect.RunicEffects;
import dev.runicattunement.item.RunicTabletItem;
import dev.runicattunement.registry.ModScreenHandlers;
import dev.runicattunement.transmutation.TransmutationRules;
import dev.runicattunement.unravel.RunicUnravelRuntime;
import dev.runicattunement.util.RunicExperience;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.class_1263;
import net.minecraft.class_1277;
import net.minecraft.class_1657;
import net.minecraft.class_1661;
import net.minecraft.class_1703;
import net.minecraft.class_1735;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1887;
import net.minecraft.class_1890;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2540;
import net.minecraft.class_2960;
import net.minecraft.class_3222;
import net.minecraft.class_3914;
import net.minecraft.class_3915;
import net.minecraft.class_7923;

public final class RunicInscriptionScreenHandler extends class_1703 {
   public static final int PRIMARY_SLOT_INDEX = 0;
   public static final int TRANSMUTATION_TARGET_SLOT_INDEX = 1;
   public static final int MODE_ATTUNEMENT = 0;
   public static final int MODE_TRANSMUTATION = 1;
   public static final int BUTTON_MODE_ATTUNEMENT = 10;
   public static final int BUTTON_MODE_TRANSMUTATION = 11;
   public static final int BUTTON_INSCRIBE_BASE = 1000;
   public static final int BUTTON_TOGGLE_TRANSFER_BASE = 2000;
   public static final int BUTTON_TOGGLE_CURSE_BASE = 3000;
   public static final int BUTTON_SELECT_ALL_COMPATIBLE = 4000;
   public static final int BUTTON_CLEAR_OPTIONAL = 4001;
   public static final int BUTTON_TRANSMUTE = 5000;
   private static final int PLAYER_INVENTORY_START = 2;
   private static final int PLAYER_INVENTORY_END = 38;
   private final class_1263 workInventory;
   private final RunicScreenData data;
   private final class_3914 context;
   private final class_3915 mode = class_3915.method_17403();
   private final Set<class_2960> selectedNormal = new LinkedHashSet<>();
   private final Set<class_2960> removeCurses = new LinkedHashSet<>();

   public RunicInscriptionScreenHandler(int syncId, class_1661 playerInventory, class_2540 buf) {
      this(syncId, playerInventory, RunicScreenData.read(buf), null);
   }

   public RunicInscriptionScreenHandler(int syncId, class_1661 playerInventory, RunicScreenData data, class_1937 world) {
      super(ModScreenHandlers.RUNIC_INSCRIPTION, syncId);
      this.data = data;
      this.context = world == null ? class_3914.field_17304 : class_3914.method_17392(world, data.tablePos());
      this.workInventory = new class_1277(2) {
         public void method_5431() {
            super.method_5431();
            RunicInscriptionScreenHandler.this.method_7609(this);
         }
      };
      this.method_17362(this.mode);
      this.mode.method_17404(0);
      this.method_7621(new class_1735(this.workInventory, 0, 20, 66) {
         public boolean method_7680(class_1799 stack) {
            return !stack.method_7960() && !(stack.method_7909() instanceof RunicTabletItem);
         }

         public int method_7675() {
            return 1;
         }

         public int method_7676(class_1799 stack) {
            return 1;
         }
      });
      this.method_7621(new class_1735(this.workInventory, 1, 59, 66) {
         public boolean method_7680(class_1799 stack) {
            return RunicInscriptionScreenHandler.this.isTransmutationMode() && !stack.method_7960() && !(stack.method_7909() instanceof RunicTabletItem);
         }

         public boolean method_7682() {
            return RunicInscriptionScreenHandler.this.isTransmutationMode();
         }

         public int method_7675() {
            return 1;
         }

         public int method_7676(class_1799 stack) {
            return 1;
         }
      });

      for (int row = 0; row < 3; row++) {
         for (int column = 0; column < 9; column++) {
            this.method_7621(new class_1735(playerInventory, column + row * 9 + 9, 79 + column * 18, 195 + row * 18));
         }
      }

      for (int column = 0; column < 9; column++) {
         this.method_7621(new class_1735(playerInventory, column, 79 + column * 18, 251));
      }
   }

   public RunicScreenData data() {
      return this.data;
   }

   public int getMode() {
      return this.mode.method_17407();
   }

   public boolean isTransmutationMode() {
      return this.mode.method_17407() == 1;
   }

   public class_1799 getEquipmentStack() {
      return this.workInventory.method_5438(0);
   }

   public class_1799 getSourceStack() {
      return this.workInventory.method_5438(0);
   }

   public class_1799 getTargetStack() {
      return this.workInventory.method_5438(1);
   }

   public List<class_2960> getAttunedEnchantments() {
      return this.data.attunedEnchantments();
   }

   public EnchantmentDefinition getDefinition(class_2960 id) {
      return this.data.definitions().get(id);
   }

   public Map<class_2960, EnchantmentDefinition> getDefinitions() {
      return this.data.definitions();
   }

   public TransmutationCosts getTransmutationCosts() {
      return this.data.transmutationCosts();
   }

   public RunicConfig.ExperienceCosts getExperienceSettings() {
      return this.data.experienceSettings();
   }

   public RunicConfig.Transmutation getTransmutationSettings() {
      return this.data.transmutationSettings();
   }

   public int getAttunementExperienceCost(EnchantmentDefinition definition, int targetLevel) {
      return RunicExperience.attunementCost(definition, targetLevel, this.data.experienceSettings());
   }

   public int getTransmutationExperienceCost(Set<class_2960> selectedNormal, Set<class_2960> removeCurses) {
      Set<class_2960> transferredCurses = TransmutationRules.transferredCurses(this.getSourceStack(), this.getTargetStack(), selectedNormal, removeCurses);
      return RunicExperience.transmutationCost(
         this.getSourceStack(), this.data.definitions(), selectedNormal, transferredCurses, this.data.experienceSettings()
      );
   }

   public List<IngredientCost> getClientCosts(EnchantmentDefinition definition, int targetLevel) {
      List<IngredientCost> costs = new ArrayList<>();
      costs.addAll(this.data.universalCosts().getOrDefault(targetLevel, List.of()));
      definition.level(targetLevel).ifPresent(level -> costs.addAll(level.ingredients()));
      return mergeCosts(costs);
   }

   public int indexOf(class_2960 enchantmentId) {
      return this.data.attunedEnchantments().indexOf(enchantmentId);
   }

   public boolean method_7597(class_1657 player) {
      return method_17695(this.context, player, class_2246.field_10485);
   }

   public void method_7609(class_1263 inventory) {
      this.selectedNormal.clear();
      this.removeCurses.clear();
      super.method_7609(inventory);
   }

   public void method_7595(class_1657 player) {
      super.method_7595(player);
      this.method_7607(player, this.workInventory);
   }

   public class_1799 method_7601(class_1657 player, int slotIndex) {
      class_1735 slot = (class_1735)this.field_7761.get(slotIndex);
      if (!slot.method_7681()) {
         return class_1799.field_8037;
      }

      class_1799 stack = slot.method_7677();
      class_1799 original = stack.method_7972();
      if (slotIndex != 0 && slotIndex != 1) {
         if (this.isTransmutationMode()) {
            class_1735 sourceSlot = (class_1735)this.field_7761.get(0);
            class_1735 targetSlot = (class_1735)this.field_7761.get(1);
            if (!sourceSlot.method_7681() && sourceSlot.method_7680(stack)) {
               if (!this.method_7616(stack, 0, 1, false)) {
                  return class_1799.field_8037;
               }
            } else if (!targetSlot.method_7681() && targetSlot.method_7680(stack)) {
               if (!this.method_7616(stack, 1, 2, false)) {
                  return class_1799.field_8037;
               }
            } else if (!this.moveWithinPlayerInventory(stack, slotIndex)) {
               return class_1799.field_8037;
            }
         } else {
            class_1735 equipmentSlot = (class_1735)this.field_7761.get(0);
            if (!equipmentSlot.method_7681() && equipmentSlot.method_7680(stack)) {
               if (!this.method_7616(stack, 0, 1, false)) {
                  return class_1799.field_8037;
               }
            } else if (!this.moveWithinPlayerInventory(stack, slotIndex)) {
               return class_1799.field_8037;
            }
         }
      } else if (!this.method_7616(stack, 2, 38, true)) {
         return class_1799.field_8037;
      }

      if (stack.method_7960()) {
         slot.method_48931(class_1799.field_8037);
      } else {
         slot.method_7668();
      }

      return original;
   }

   private boolean moveWithinPlayerInventory(class_1799 stack, int slotIndex) {
      int relative = slotIndex - 2;
      return relative < 27 ? this.method_7616(stack, 29, 38, false) : this.method_7616(stack, 2, 29, false);
   }

   public boolean method_7604(class_1657 player, int id) {
      if (RunicUnravelRuntime.handleButton(this, player, id) != -1) {
         return true;
      }

      if (id == 10) {
         if (player instanceof class_3222 serverPlayer) {
            this.switchToAttunement(serverPlayer);
         }

         return true;
      } else if (id == 11) {
         if (player instanceof class_3222 && this.data.transmutationSettings().enabled()) {
            this.mode.method_17404(1);
            this.selectedNormal.clear();
            this.removeCurses.clear();
            this.method_7623();
         }

         return this.data.transmutationSettings().enabled();
      } else if (id >= 1000 && id < 1000 + this.data.attunedEnchantments().size()) {
         int index = id - 1000;
         if (player instanceof class_3222 serverPlayer) {
            return this.isTransmutationMode() ? false : this.tryInscribe(serverPlayer, this.data.attunedEnchantments().get(index));
         } else {
            return true;
         }
      } else {
         if (!this.isTransmutationMode()) {
            return false;
         }

         List<class_2960> sourceEnchantments = TransmutationRules.sourceEnchantments(this.getSourceStack());
         if (id >= 2000 && id < 2000 + sourceEnchantments.size()) {
            if (!(player instanceof class_3222)) {
               return true;
            }

            class_2960 enchantmentId = sourceEnchantments.get(id - 2000);
            return this.toggleNormal(enchantmentId);
         } else if (id >= 3000 && id < 3000 + sourceEnchantments.size()) {
            if (!(player instanceof class_3222)) {
               return true;
            }

            class_2960 enchantmentId = sourceEnchantments.get(id - 3000);
            return this.toggleCurse(enchantmentId);
         } else if (id == 4000) {
            if (player instanceof class_3222) {
               this.selectAllCompatible(sourceEnchantments);
            }

            return true;
         } else if (id == 4001) {
            if (player instanceof class_3222) {
               this.selectedNormal.clear();
            }

            return true;
         } else {
            return id != 5000 ? false : player instanceof class_3222 serverPlayer && this.tryTransmute(serverPlayer);
         }
      }
   }

   private void switchToAttunement(class_3222 player) {
      if (this.mode.method_17407() != 0) {
         class_1799 target = this.workInventory.method_5441(1);
         if (!target.method_7960() && !player.method_31548().method_7394(target)) {
            player.method_7328(target, false);
         }

         this.mode.method_17404(0);
         this.selectedNormal.clear();
         this.removeCurses.clear();
         this.method_7623();
      }
   }

   private boolean toggleNormal(class_2960 enchantmentId) {
      class_1887 enchantment = (class_1887)class_7923.field_41176.method_17966(enchantmentId).orElse(null);
      if (enchantment == null || enchantment.method_8195()) {
         return false;
      }

      if (this.selectedNormal.remove(enchantmentId)) {
         return true;
      }

      if (TransmutationRules.normalStatus(
            enchantmentId, this.getSourceStack(), this.getTargetStack(), this.data.definitions(), this.selectedNormal, this.removeCurses
         )
         != TransmutationRules.Status.TRANSFERABLE) {
         return false;
      }

      this.selectedNormal.add(enchantmentId);
      return true;
   }

   private boolean toggleCurse(class_2960 enchantmentId) {
      class_1887 enchantment = (class_1887)class_7923.field_41176.method_17966(enchantmentId).orElse(null);
      if (enchantment == null || !enchantment.method_8195()) {
         return false;
      }

      if (this.removeCurses.remove(enchantmentId)) {
         if (!TransmutationRules.curseCanTransfer(enchantmentId, this.getSourceStack(), this.getTargetStack(), this.selectedNormal)) {
            this.removeCurses.add(enchantmentId);
            return false;
         } else {
            return true;
         }
      } else {
         if (!TransmutationRules.curseCanTransfer(enchantmentId, this.getSourceStack(), this.getTargetStack(), this.selectedNormal)) {
            return false;
         }

         this.removeCurses.add(enchantmentId);
         return true;
      }
   }

   private void selectAllCompatible(List<class_2960> sourceEnchantments) {
      this.selectedNormal.clear();

      for (class_2960 id : sourceEnchantments) {
         class_1887 enchantment = (class_1887)class_7923.field_41176.method_17966(id).orElse(null);
         if (enchantment != null
            && !enchantment.method_8195()
            && TransmutationRules.normalStatus(
                  id, this.getSourceStack(), this.getTargetStack(), this.data.definitions(), this.selectedNormal, this.removeCurses
               )
               == TransmutationRules.Status.TRANSFERABLE) {
            this.selectedNormal.add(id);
         }
      }
   }

   private boolean tryInscribe(class_3222 player, class_2960 enchantmentId) {
      if (!this.method_7597(player)) {
         return false;
      }

      if (!AttunementScanner.scan(player.method_37908(), this.data.tablePos()).contains(enchantmentId)) {
         return false;
      }

      EnchantmentDefinition definition = RunicDefinitionManager.get(enchantmentId);
      class_1887 enchantment = (class_1887)class_7923.field_41176.method_17966(enchantmentId).orElse(null);
      if (definition != null && enchantment != null) {
         EnchantmentDefinition openedDefinition = this.data.definitions().get(enchantmentId);
         if (definition.equals(openedDefinition) && RunicDefinitionManager.universalSnapshot().equals(this.data.universalCosts())) {
            class_1799 target = this.workInventory.method_5438(0);
            if (!target.method_7960() && RunicCompatibility.accepts(enchantment, target)) {
               int currentLevel = class_1890.method_8225(enchantment, target);
               int targetLevel = currentLevel + 1;
               if (targetLevel <= enchantment.method_8183() && !definition.level(targetLevel).isEmpty()) {
                  Map<class_1887, Integer> existing = class_1890.method_8222(target);

                  for (class_1887 other : existing.keySet()) {
                     if (other != enchantment && !enchantment.method_8188(other)) {
                        return false;
                     }
                  }

                  List<IngredientCost> costs = mergeCosts(RunicDefinitionManager.combinedCosts(definition, targetLevel));
                  RunicConfig.ExperienceCosts experienceSettings = RunicConfig.experienceCosts();
                  if (!experienceSettings.equals(this.data.experienceSettings())) {
                     return false;
                  }

                  int experienceCost = RunicExperience.attunementCost(definition, targetLevel, experienceSettings);
                  if (!player.method_31549().field_7477) {
                     if (!hasAllCosts(player.method_31548(), costs) || RunicExperience.availablePoints(player) < experienceCost) {
                        return false;
                     }

                     consumeCosts(player.method_31548(), costs);
                     if (experienceCost > 0) {
                        player.method_7255(-experienceCost);
                     }
                  }

                  Map<class_1887, Integer> updated = new LinkedHashMap<>(existing);
                  updated.put(enchantment, targetLevel);
                  class_1890.method_8214(updated, target);
                  this.workInventory.method_5431();
                  this.method_7623();
                  boolean maximum = targetLevel >= enchantment.method_8183() || definition.level(targetLevel + 1).isEmpty();
                  RunicAdvancements.triggerAttunement(player, maximum);
                  RunicEffects.attunementSuccess(
                     player, this.data.tablePos(), targetLevel, Math.min(enchantment.method_8183(), definition.highestConfiguredLevel()), maximum
                  );
                  return true;
               } else {
                  return false;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private boolean tryTransmute(class_3222 player) {
      if (this.method_7597(player) && this.isTransmutationMode()) {
         RunicConfig.Transmutation settings = RunicConfig.transmutation();
         RunicConfig.ExperienceCosts experienceSettings = RunicConfig.experienceCosts();
         TransmutationCosts authoritativeCosts = RunicDefinitionManager.transmutationCostsSnapshot();
         if (settings.enabled() && authoritativeCosts.available()) {
            if (RunicDefinitionManager.snapshot().equals(this.data.definitions())
               && authoritativeCosts.equals(this.data.transmutationCosts())
               && experienceSettings.equals(this.data.experienceSettings())
               && settings.equals(this.data.transmutationSettings())) {
               class_1799 source = this.getSourceStack();
               class_1799 target = this.getTargetStack();
               if (source.method_7960() || target.method_7960()) {
                  return false;
               }

               if (this.selectedNormal.isEmpty()) {
                  return false;
               }

               for (class_2960 id : this.selectedNormal) {
                  if (TransmutationRules.normalStatus(id, source, target, RunicDefinitionManager.snapshot(), this.selectedNormal, this.removeCurses)
                     != TransmutationRules.Status.TRANSFERABLE) {
                     return false;
                  }
               }

               if (!TransmutationRules.hasValidNormalTransfer(source, target, RunicDefinitionManager.snapshot(), this.selectedNormal, this.removeCurses)) {
                  return false;
               }

               Set<class_2960> transferredCurses = TransmutationRules.transferredCurses(source, target, this.selectedNormal, this.removeCurses);
               List<IngredientCost> costs = TransmutationRules.calculateCosts(
                  source, target, RunicDefinitionManager.snapshot(), this.selectedNormal, this.removeCurses, authoritativeCosts, settings
               );
               int experienceCost = RunicExperience.transmutationCost(
                  source, RunicDefinitionManager.snapshot(), this.selectedNormal, transferredCurses, experienceSettings
               );
               if (player.method_31549().field_7477 || hasAllCosts(player.method_31548(), costs) && RunicExperience.availablePoints(player) >= experienceCost) {
                  class_1799 sourceResult = source.method_7972();
                  class_1799 targetResult = target.method_7972();
                  Map<class_1887, Integer> sourceEnchantments = new LinkedHashMap<>(class_1890.method_8222(sourceResult));
                  Map<class_1887, Integer> targetEnchantments = new LinkedHashMap<>(class_1890.method_8222(targetResult));

                  for (class_2960 id : this.selectedNormal) {
                     class_1887 enchantment = (class_1887)class_7923.field_41176.method_17966(id).orElse(null);
                     if (enchantment == null) {
                        return false;
                     }

                     int level = sourceEnchantments.getOrDefault(enchantment, 0);
                     if (level <= 0) {
                        return false;
                     }

                     targetEnchantments.put(enchantment, Math.max(level, targetEnchantments.getOrDefault(enchantment, 0)));
                     sourceEnchantments.remove(enchantment);
                  }

                  for (class_2960 id : transferredCurses) {
                     class_1887 curse = (class_1887)class_7923.field_41176.method_17966(id).orElse(null);
                     if (curse == null) {
                        return false;
                     }

                     int level = sourceEnchantments.getOrDefault(curse, 0);
                     if (level <= 0) {
                        return false;
                     }

                     targetEnchantments.put(curse, Math.max(level, targetEnchantments.getOrDefault(curse, 0)));
                     sourceEnchantments.remove(curse);
                  }

                  class_1890.method_8214(sourceEnchantments, sourceResult);
                  class_1890.method_8214(targetEnchantments, targetResult);
                  if (!player.method_31549().field_7477) {
                     consumeCosts(player.method_31548(), costs);
                     if (experienceCost > 0) {
                        player.method_7255(-experienceCost);
                     }
                  }

                  boolean destroySource = settings.sourceItemDestructionChance() > 0.0
                     && player.method_6051().method_43058() < settings.sourceItemDestructionChance();
                  this.workInventory.method_5447(0, destroySource ? class_1799.field_8037 : sourceResult);
                  this.workInventory.method_5447(1, targetResult);
                  this.selectedNormal.clear();
                  this.removeCurses.clear();
                  this.method_7623();
                  RunicAdvancements.triggerTransmutation(player);
                  RunicEffects.transmutationSuccess(player, this.data.tablePos());
                  return true;
               } else {
                  return false;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private static List<IngredientCost> mergeCosts(List<IngredientCost> costs) {
      record Aggregate(int count, CostKind kind) {
      }

      Map<class_2960, Aggregate> merged = new LinkedHashMap<>();

      for (IngredientCost cost : costs) {
         Aggregate previous = merged.get(cost.itemId());
         if (previous == null) {
            merged.put(cost.itemId(), new Aggregate(cost.count(), cost.kind()));
         } else {
            long total = (long)previous.count() + cost.count();
            merged.put(cost.itemId(), new Aggregate((int)Math.min(2147483647L, total), previous.kind()));
         }
      }

      List<IngredientCost> result = new ArrayList<>(merged.size());

      for (Entry<class_2960, Aggregate> entry : merged.entrySet()) {
         result.add(new IngredientCost(entry.getKey(), entry.getValue().count(), entry.getValue().kind()));
      }

      return List.copyOf(result);
   }

   private static boolean hasAllCosts(class_1661 inventory, List<IngredientCost> costs) {
      for (IngredientCost cost : costs) {
         class_1792 item = (class_1792)class_7923.field_41178.method_17966(cost.itemId()).orElse(null);
         if (item == null || countItem(inventory, item) < cost.count()) {
            return false;
         }
      }

      return true;
   }

   private static int countItem(class_1661 inventory, class_1792 item) {
      int count = 0;

      for (int slot = 0; slot < 36; slot++) {
         class_1799 stack = inventory.method_5438(slot);
         if (stack.method_31574(item)) {
            count += stack.method_7947();
         }
      }

      return count;
   }

   private static void consumeCosts(class_1661 inventory, List<IngredientCost> costs) {
      for (IngredientCost cost : costs) {
         class_1792 item = (class_1792)class_7923.field_41178.method_17966(cost.itemId()).orElse(null);
         if (item != null) {
            int remaining = cost.count();

            for (int slot = 0; slot < 36 && remaining > 0; slot++) {
               class_1799 stack = inventory.method_5438(slot);
               if (stack.method_31574(item)) {
                  int take = Math.min(remaining, stack.method_7947());
                  stack.method_7934(take);
                  remaining -= take;
               }
            }
         }
      }

      inventory.method_5431();
   }
}
