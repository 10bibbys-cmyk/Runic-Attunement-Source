package dev.runicattunement.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.runicattunement.RunicAttunement;
import dev.runicattunement.attunement.AttunementScanner;
import dev.runicattunement.config.RunicConfig;
import dev.runicattunement.data.RunicDefinitionManager;
import dev.runicattunement.item.RunicDustItem;
import dev.runicattunement.item.RunicTabletItem;
import dev.runicattunement.loot.LootProfileManager;
import dev.runicattunement.registry.ModItems;
import dev.runicattunement.util.RunicPotency;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.class_1661;
import net.minecraft.class_1799;
import net.minecraft.class_2168;
import net.minecraft.class_2170;
import net.minecraft.class_2186;
import net.minecraft.class_2232;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_3222;
import net.minecraft.class_3965;
import net.minecraft.class_7157;
import net.minecraft.class_7923;
import net.minecraft.class_2170.class_5364;

public final class RunicCommands {
   private RunicCommands() {
   }

   public static void initialize() {
      CommandRegistrationCallback.EVENT.register(RunicCommands::register);
   }

   private static void register(CommandDispatcher<class_2168> dispatcher, class_7157 registryAccess, class_5364 environment) {
      dispatcher.register(
         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)class_2170.method_9247(
                              "runicattunement"
                           )
                           .then(
                              ((LiteralArgumentBuilder)class_2170.method_9247("inspect")
                                    .then(class_2170.method_9247("held").executes(ctx -> inspectHeld((class_2168)ctx.getSource()))))
                                 .then(class_2170.method_9247("table").executes(ctx -> inspectTable((class_2168)ctx.getSource())))
                           ))
                        .then(
                           ((LiteralArgumentBuilder)class_2170.method_9247("validate").requires(source -> source.method_9259(2)))
                              .executes(ctx -> validate((class_2168)ctx.getSource()))
                        ))
                     .then(
                        ((LiteralArgumentBuilder)class_2170.method_9247("reload").requires(source -> source.method_9259(2)))
                           .executes(ctx -> reload((class_2168)ctx.getSource()))
                     ))
                  .then(
                     ((LiteralArgumentBuilder)((LiteralArgumentBuilder)class_2170.method_9247("debug").requires(source -> source.method_9259(2)))
                           .then(class_2170.method_9247("on").executes(ctx -> setDebug((class_2168)ctx.getSource(), true))))
                        .then(class_2170.method_9247("off").executes(ctx -> setDebug((class_2168)ctx.getSource(), false)))
                  ))
               .then(
                  ((LiteralArgumentBuilder)class_2170.method_9247("dump").requires(source -> source.method_9259(2)))
                     .executes(ctx -> dump((class_2168)ctx.getSource()))
               ))
            .then(
               ((LiteralArgumentBuilder)((LiteralArgumentBuilder)class_2170.method_9247("give").requires(source -> source.method_9259(2)))
                     .then(
                        class_2170.method_9247("tablet")
                           .then(
                              class_2170.method_9244("player", class_2186.method_9305())
                                 .then(
                                    class_2170.method_9244("enchantment", class_2232.method_9441())
                                       .then(
                                          class_2170.method_9244("potency", StringArgumentType.word())
                                             .executes(
                                                ctx -> giveTablet(
                                                   (class_2168)ctx.getSource(),
                                                   class_2186.method_9315(ctx, "player"),
                                                   class_2232.method_9443(ctx, "enchantment"),
                                                   StringArgumentType.getString(ctx, "potency")
                                                )
                                             )
                                       )
                                 )
                           )
                     ))
                  .then(
                     class_2170.method_9247("dust")
                        .then(
                           class_2170.method_9244("player", class_2186.method_9305())
                              .then(
                                 ((RequiredArgumentBuilder)class_2170.method_9244("potency", StringArgumentType.word())
                                       .executes(
                                          ctx -> giveDust(
                                             (class_2168)ctx.getSource(),
                                             class_2186.method_9315(ctx, "player"),
                                             StringArgumentType.getString(ctx, "potency"),
                                             1
                                          )
                                       ))
                                    .then(
                                       class_2170.method_9244("count", IntegerArgumentType.integer(1, 64))
                                          .executes(
                                             ctx -> giveDust(
                                                (class_2168)ctx.getSource(),
                                                class_2186.method_9315(ctx, "player"),
                                                StringArgumentType.getString(ctx, "potency"),
                                                IntegerArgumentType.getInteger(ctx, "count")
                                             )
                                          )
                                    )
                              )
                        )
                  )
            )
      );
   }

   private static int inspectHeld(class_2168 source) throws CommandSyntaxException {
      class_3222 player = source.method_9207();
      class_1799 stack = player.method_6047();
      if (stack.method_7960()) {
         source.method_9226(() -> class_2561.method_43470("Runic Attunement: main hand is empty."), false);
         return 0;
      }

      class_2960 itemId = class_7923.field_41178.method_10221(stack.method_7909());
      source.method_9226(() -> class_2561.method_43470("Item: " + itemId), false);
      if (stack.method_7909() instanceof RunicTabletItem tablet) {
         String enchantment = RunicTabletItem.getEnchantmentId(stack).<String>map(class_2960::toString).orElse("<missing/invalid>");
         source.method_9226(
            () -> class_2561.method_43470("Tablet potency: " + RunicPotency.key(tablet.tier()) + " (internal tier " + tablet.tier() + ")"), false
         );
         source.method_9226(() -> class_2561.method_43470("Attunement: " + enchantment), false);
         source.method_9226(() -> class_2561.method_43470("Configured/valid: " + RunicTabletItem.isConfiguredTablet(stack)), false);
      } else if (stack.method_7909() instanceof RunicDustItem dust) {
         source.method_9226(() -> class_2561.method_43470("Dust potency: " + RunicPotency.key(dust.tier()) + " (internal tier " + dust.tier() + ")"), false);
      }

      return 1;
   }

   private static int inspectTable(class_2168 source) throws CommandSyntaxException {
      class_3222 player = source.method_9207();
      class_3965 hit = (class_3965)player.method_5745(6.0, 0.0F, false);
      class_2338 pos = hit.method_17777();
      if (!player.method_37908().method_8320(pos).method_27852(class_2246.field_10485)) {
         source.method_9213(class_2561.method_43470("Look directly at a Runic Attunement Table within 6 blocks."));
         return 0;
      } else {
         List<AttunementScanner.TabletSource> tablets = AttunementScanner.scanTabletSources(player.method_37908(), pos);
         int[] counts = new int[4];
         tablets.forEach(t -> {
            if (t.tier() >= 1 && t.tier() <= 3) {
               counts[t.tier()]++;
            }
         });
         int unique = AttunementScanner.scan(player.method_37908(), pos).size();
         source.method_9226(() -> class_2561.method_43470("Runic table at " + pos.method_23854()), false);
         source.method_9226(
            () -> class_2561.method_43470(
               "Detected tablets: " + tablets.size() + " (Latent " + counts[1] + ", Resonant " + counts[2] + ", Pristine " + counts[3] + ")"
            ),
            false
         );
         source.method_9226(() -> class_2561.method_43470("Unique attunements available: " + unique), false);
         return 1;
      }
   }

   private static int validate(class_2168 source) {
      int definitions = RunicDefinitionManager.snapshot().size();
      int lootProfiles = LootProfileManager.snapshot().size();
      boolean transmutation = RunicDefinitionManager.transmutationCostsSnapshot().available();
      source.method_9226(
         () -> class_2561.method_43470(
            "Runic validation: "
               + definitions
               + " definitions; pools "
               + RunicDefinitionManager.tierPool(1).size()
               + "/"
               + RunicDefinitionManager.tierPool(2).size()
               + "/"
               + RunicDefinitionManager.tierPool(3).size()
               + "; "
               + lootProfiles
               + " loot profiles; transmutation costs "
               + (transmutation ? "available" : "INVALID/UNAVAILABLE")
               + "."
         ),
         false
      );
      return transmutation && definitions > 0 ? 1 : 0;
   }

   private static int reload(class_2168 source) {
      source.method_9211().method_3734().method_44252(source, "reload");
      return 1;
   }

   private static int setDebug(class_2168 source, boolean enabled) {
      if (!RunicConfig.setDebugLogging(enabled)) {
         source.method_9213(class_2561.method_43470("Failed to update Runic Attunement debug logging."));
         return 0;
      } else {
         source.method_9226(() -> class_2561.method_43470("Runic Attunement debug logging " + (enabled ? "enabled" : "disabled") + "."), true);
         return 1;
      }
   }

   private static int giveTablet(class_2168 source, class_3222 player, class_2960 enchantmentId, String potency) {
      int tier = parsePotency(potency);
      if (tier == 0) {
         source.method_9213(class_2561.method_43470("Potency must be latent, resonant, or pristine."));
         return 0;
      }

      if (class_7923.field_41176.method_10250(enchantmentId) && RunicDefinitionManager.get(enchantmentId) != null) {
         if (RunicDefinitionManager.get(enchantmentId).tabletTier() != tier) {
            source.method_9213(
               class_2561.method_43470("That enchantment belongs to " + RunicPotency.key(RunicDefinitionManager.get(enchantmentId).tabletTier()) + " potency.")
            );
            return 0;
         } else {
            class_1799 stack = new class_1799(ModItems.byTier(tier));
            RunicTabletItem.setEnchantment(stack, enchantmentId);
            give(player, stack);
            source.method_9226(
               () -> class_2561.method_43470(
                  "Gave " + player.method_5477().getString() + " a " + RunicPotency.key(tier) + " tablet attuned to " + enchantmentId + "."
               ),
               true
            );
            return 1;
         }
      } else {
         source.method_9213(class_2561.method_43470("Unknown or unsupported Runic enchantment: " + enchantmentId));
         return 0;
      }
   }

   private static int giveDust(class_2168 source, class_3222 player, String potency, int count) {
      int tier = parsePotency(potency);
      if (tier == 0) {
         source.method_9213(class_2561.method_43470("Potency must be latent, resonant, or pristine."));
         return 0;
      } else {
         class_1799 stack = new class_1799(ModItems.dustByTier(tier), count);
         give(player, stack);
         source.method_9226(
            () -> class_2561.method_43470("Gave " + player.method_5477().getString() + " " + count + " " + RunicPotency.key(tier) + " dust."), true
         );
         return 1;
      }
   }

   private static void give(class_3222 player, class_1799 stack) {
      class_1661 inventory = player.method_31548();
      if (!inventory.method_7394(stack)) {
         player.method_7328(stack, false);
      }
   }

   private static int dump(class_2168 source) {
      try {
         StringBuilder out = new StringBuilder();
         out.append("Runic Attunement diagnostic dump\n");
         out.append("Definitions: ").append(RunicDefinitionManager.snapshot().size()).append('\n');

         for (int tier = 1; tier <= 3; tier++) {
            out.append(RunicPotency.key(tier)).append(" pool: ").append(RunicDefinitionManager.tierPool(tier)).append('\n');
         }

         out.append("Loot profiles: ").append(LootProfileManager.snapshot()).append('\n');
         out.append("Blacklisted: ").append(RunicConfig.blacklistedEnchantments()).append('\n');
         out.append("Transmutation costs available: ").append(RunicDefinitionManager.transmutationCostsSnapshot().available()).append('\n');
         out.append("Config: ").append(RunicConfig.path()).append('\n');
         Path path = RunicConfig.path().resolveSibling("runic_attunement_dump.txt");
         Files.writeString(path, out.toString(), StandardCharsets.UTF_8);
         RunicAttunement.LOGGER.info("{}", out);
         source.method_9226(() -> class_2561.method_43470("Wrote Runic Attunement diagnostics to " + path), false);
         return 1;
      } catch (Exception exception) {
         RunicAttunement.LOGGER.error("Failed to write Runic Attunement diagnostic dump", exception);
         source.method_9213(class_2561.method_43470("Failed to write diagnostic dump; see server log."));
         return 0;
      }
   }

   private static int parsePotency(String value) {
      return switch (value.toLowerCase()) {
         case "latent", "1", "tier1", "tier_1" -> 1;
         case "resonant", "2", "tier2", "tier_2" -> 2;
         case "pristine", "3", "tier3", "tier_3" -> 3;
         default -> 0;
      };
   }
}
