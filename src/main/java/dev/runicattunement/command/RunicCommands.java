/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.arguments.IntegerArgumentType
 *  com.mojang.brigadier.arguments.StringArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.builder.RequiredArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
 *  net.minecraft.class_1661
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_1935
 *  net.minecraft.class_2168
 *  net.minecraft.class_2170
 *  net.minecraft.class_2170$class_5364
 *  net.minecraft.class_2186
 *  net.minecraft.class_2232
 *  net.minecraft.class_2246
 *  net.minecraft.class_2338
 *  net.minecraft.class_2561
 *  net.minecraft.class_2960
 *  net.minecraft.class_3222
 *  net.minecraft.class_3965
 *  net.minecraft.class_7157
 *  net.minecraft.class_7923
 */
package dev.runicattunement.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
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
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.List;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.class_1661;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1935;
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

public final class RunicCommands {
    private RunicCommands() {
    }

    public static void initialize() {
        CommandRegistrationCallback.EVENT.register(RunicCommands::register);
    }

    private static void register(CommandDispatcher<class_2168> dispatcher, class_7157 registryAccess, class_2170.class_5364 environment) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)class_2170.method_9247((String)"runicattunement").then(((LiteralArgumentBuilder)class_2170.method_9247((String)"inspect").then(class_2170.method_9247((String)"held").executes(ctx -> RunicCommands.inspectHeld((class_2168)ctx.getSource())))).then(class_2170.method_9247((String)"table").executes(ctx -> RunicCommands.inspectTable((class_2168)ctx.getSource()))))).then(((LiteralArgumentBuilder)class_2170.method_9247((String)"validate").requires(source -> source.method_9259(2))).executes(ctx -> RunicCommands.validate((class_2168)ctx.getSource())))).then(((LiteralArgumentBuilder)class_2170.method_9247((String)"reload").requires(source -> source.method_9259(2))).executes(ctx -> RunicCommands.reload((class_2168)ctx.getSource())))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)class_2170.method_9247((String)"debug").requires(source -> source.method_9259(2))).then(class_2170.method_9247((String)"on").executes(ctx -> RunicCommands.setDebug((class_2168)ctx.getSource(), true)))).then(class_2170.method_9247((String)"off").executes(ctx -> RunicCommands.setDebug((class_2168)ctx.getSource(), false))))).then(((LiteralArgumentBuilder)class_2170.method_9247((String)"dump").requires(source -> source.method_9259(2))).executes(ctx -> RunicCommands.dump((class_2168)ctx.getSource())))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)class_2170.method_9247((String)"give").requires(source -> source.method_9259(2))).then(class_2170.method_9247((String)"tablet").then(class_2170.method_9244((String)"player", (ArgumentType)class_2186.method_9305()).then(class_2170.method_9244((String)"enchantment", (ArgumentType)class_2232.method_9441()).then(class_2170.method_9244((String)"potency", (ArgumentType)StringArgumentType.word()).executes(ctx -> RunicCommands.giveTablet((class_2168)ctx.getSource(), class_2186.method_9315((CommandContext)ctx, (String)"player"), class_2232.method_9443((CommandContext)ctx, (String)"enchantment"), StringArgumentType.getString((CommandContext)ctx, (String)"potency")))))))).then(class_2170.method_9247((String)"dust").then(class_2170.method_9244((String)"player", (ArgumentType)class_2186.method_9305()).then(((RequiredArgumentBuilder)class_2170.method_9244((String)"potency", (ArgumentType)StringArgumentType.word()).executes(ctx -> RunicCommands.giveDust((class_2168)ctx.getSource(), class_2186.method_9315((CommandContext)ctx, (String)"player"), StringArgumentType.getString((CommandContext)ctx, (String)"potency"), 1))).then(class_2170.method_9244((String)"count", (ArgumentType)IntegerArgumentType.integer((int)1, (int)64)).executes(ctx -> RunicCommands.giveDust((class_2168)ctx.getSource(), class_2186.method_9315((CommandContext)ctx, (String)"player"), StringArgumentType.getString((CommandContext)ctx, (String)"potency"), IntegerArgumentType.getInteger((CommandContext)ctx, (String)"count")))))))));
    }

    private static int inspectHeld(class_2168 source) throws CommandSyntaxException {
        class_3222 player = source.method_9207();
        class_1799 stack = player.method_6047();
        if (stack.method_7960()) {
            source.method_9226(() -> class_2561.method_43470((String)"Runic Attunement: main hand is empty."), false);
            return 0;
        }
        class_2960 itemId = class_7923.field_41178.method_10221((Object)stack.method_7909());
        source.method_9226(() -> class_2561.method_43470((String)("Item: " + String.valueOf(itemId))), false);
        class_1792 class_17922 = stack.method_7909();
        if (class_17922 instanceof RunicTabletItem) {
            RunicTabletItem tablet = (RunicTabletItem)class_17922;
            String enchantment = RunicTabletItem.getEnchantmentId(stack).map(class_2960::toString).orElse("<missing/invalid>");
            source.method_9226(() -> class_2561.method_43470((String)("Tablet potency: " + RunicPotency.key(tablet.tier()) + " (internal tier " + tablet.tier() + ")")), false);
            source.method_9226(() -> class_2561.method_43470((String)("Attunement: " + enchantment)), false);
            source.method_9226(() -> class_2561.method_43470((String)("Configured/valid: " + RunicTabletItem.isConfiguredTablet(stack))), false);
        } else {
            class_17922 = stack.method_7909();
            if (class_17922 instanceof RunicDustItem) {
                RunicDustItem dust = (RunicDustItem)class_17922;
                source.method_9226(() -> class_2561.method_43470((String)("Dust potency: " + RunicPotency.key(dust.tier()) + " (internal tier " + dust.tier() + ")")), false);
            }
        }
        return 1;
    }

    private static int inspectTable(class_2168 source) throws CommandSyntaxException {
        class_3222 player = source.method_9207();
        class_3965 hit = (class_3965)player.method_5745(6.0, 0.0f, false);
        class_2338 pos = hit.method_17777();
        if (!player.method_37908().method_8320(pos).method_27852(class_2246.field_10485)) {
            source.method_9213((class_2561)class_2561.method_43470((String)"Look directly at a Runic Attunement Table within 6 blocks."));
            return 0;
        }
        List<AttunementScanner.TabletSource> tablets = AttunementScanner.scanTabletSources(player.method_37908(), pos);
        int[] counts = new int[4];
        tablets.forEach(t -> {
            if (t.tier() >= 1 && t.tier() <= 3) {
                int n = t.tier();
                counts[n] = counts[n] + 1;
            }
        });
        int unique = AttunementScanner.scan(player.method_37908(), pos).size();
        source.method_9226(() -> class_2561.method_43470((String)("Runic table at " + pos.method_23854())), false);
        source.method_9226(() -> class_2561.method_43470((String)("Detected tablets: " + tablets.size() + " (Latent " + counts[1] + ", Resonant " + counts[2] + ", Pristine " + counts[3] + ")")), false);
        source.method_9226(() -> class_2561.method_43470((String)("Unique attunements available: " + unique)), false);
        return 1;
    }

    private static int validate(class_2168 source) {
        int definitions = RunicDefinitionManager.snapshot().size();
        int lootProfiles = LootProfileManager.snapshot().size();
        boolean transmutation = RunicDefinitionManager.transmutationCostsSnapshot().available();
        source.method_9226(() -> class_2561.method_43470((String)("Runic validation: " + definitions + " definitions; pools " + RunicDefinitionManager.tierPool(1).size() + "/" + RunicDefinitionManager.tierPool(2).size() + "/" + RunicDefinitionManager.tierPool(3).size() + "; " + lootProfiles + " loot profiles; transmutation costs " + (transmutation ? "available" : "INVALID/UNAVAILABLE") + ".")), false);
        return transmutation && definitions > 0 ? 1 : 0;
    }

    private static int reload(class_2168 source) {
        source.method_9211().method_3734().method_44252(source, "reload");
        return 1;
    }

    private static int setDebug(class_2168 source, boolean enabled) {
        if (!RunicConfig.setDebugLogging(enabled)) {
            source.method_9213((class_2561)class_2561.method_43470((String)"Failed to update Runic Attunement debug logging."));
            return 0;
        }
        source.method_9226(() -> class_2561.method_43470((String)("Runic Attunement debug logging " + (enabled ? "enabled" : "disabled") + ".")), true);
        return 1;
    }

    private static int giveTablet(class_2168 source, class_3222 player, class_2960 enchantmentId, String potency) {
        int tier = RunicCommands.parsePotency(potency);
        if (tier == 0) {
            source.method_9213((class_2561)class_2561.method_43470((String)"Potency must be latent, resonant, or pristine."));
            return 0;
        }
        if (!class_7923.field_41176.method_10250(enchantmentId) || RunicDefinitionManager.get(enchantmentId) == null) {
            source.method_9213((class_2561)class_2561.method_43470((String)("Unknown or unsupported Runic enchantment: " + String.valueOf(enchantmentId))));
            return 0;
        }
        if (RunicDefinitionManager.get(enchantmentId).tabletTier() != tier) {
            source.method_9213((class_2561)class_2561.method_43470((String)("That enchantment belongs to " + RunicPotency.key(RunicDefinitionManager.get(enchantmentId).tabletTier()) + " potency.")));
            return 0;
        }
        class_1799 stack = new class_1799((class_1935)ModItems.byTier(tier));
        RunicTabletItem.setEnchantment(stack, enchantmentId);
        RunicCommands.give(player, stack);
        source.method_9226(() -> class_2561.method_43470((String)("Gave " + player.method_5477().getString() + " a " + RunicPotency.key(tier) + " tablet attuned to " + String.valueOf(enchantmentId) + ".")), true);
        return 1;
    }

    private static int giveDust(class_2168 source, class_3222 player, String potency, int count) {
        int tier = RunicCommands.parsePotency(potency);
        if (tier == 0) {
            source.method_9213((class_2561)class_2561.method_43470((String)"Potency must be latent, resonant, or pristine."));
            return 0;
        }
        class_1799 stack = new class_1799((class_1935)ModItems.dustByTier(tier), count);
        RunicCommands.give(player, stack);
        source.method_9226(() -> class_2561.method_43470((String)("Gave " + player.method_5477().getString() + " " + count + " " + RunicPotency.key(tier) + " dust.")), true);
        return 1;
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
            for (int tier = 1; tier <= 3; ++tier) {
                out.append(RunicPotency.key(tier)).append(" pool: ").append(RunicDefinitionManager.tierPool(tier)).append('\n');
            }
            out.append("Loot profiles: ").append(LootProfileManager.snapshot()).append('\n');
            out.append("Blacklisted: ").append(RunicConfig.blacklistedEnchantments()).append('\n');
            out.append("Transmutation costs available: ").append(RunicDefinitionManager.transmutationCostsSnapshot().available()).append('\n');
            out.append("Config: ").append(RunicConfig.path()).append('\n');
            Path path = RunicConfig.path().resolveSibling("runic_attunement_dump.txt");
            Files.writeString(path, (CharSequence)out.toString(), StandardCharsets.UTF_8, new OpenOption[0]);
            RunicAttunement.LOGGER.info("{}", (Object)out);
            source.method_9226(() -> class_2561.method_43470((String)("Wrote Runic Attunement diagnostics to " + String.valueOf(path))), false);
            return 1;
        }
        catch (Exception exception) {
            RunicAttunement.LOGGER.error("Failed to write Runic Attunement diagnostic dump", (Throwable)exception);
            source.method_9213((class_2561)class_2561.method_43470((String)"Failed to write diagnostic dump; see server log."));
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

