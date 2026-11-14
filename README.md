# Runic Attunement

An exploration-driven, deterministic enchanting system for **Minecraft Fabric 1.20.1**, originally developed for **Path of the Ascended**.

Runic Attunement replaces random enchantment rolls with reusable **Runic Tablets**. Discover Tablets in structures, store them in Chiseled Bookshelves around the Runic Attunement Table, and choose which enchantments to apply or upgrade on compatible equipment.

## Gameplay

- **Runic Tablets:** Latent, Resonant and Pristine potency tiers. Tablets are reusable after an Attunement.
- **Attunement:** Choose a known enchantment and pay configured Lapis Lazuli, thematic ingredients and raw XP points. Upgrade one level at a time.
- **Runic Dust and Reforging:** Grind duplicate Tablets into tiered Dust and use it at an Anvil to improve Tablet potency.
- **Transmutation:** Transfer selected enchantments between compatible items with configurable material and XP costs.
- **Unravel:** Remove enchantments or curses and recover configurable portions of their material and XP costs (50% by default).
- **Exploration and advancements:** Includes 37 built-in enchantment definitions, 18 vanilla structure loot profiles and custom advancements.
- **Optional integrations:** Enchanted ToolTips descriptions and Jade Chiseled Bookshelf Tablet information.

## Configuration and datapacks

The main configuration is generated at `config/runic_attunement.json`. It controls Tablet loot, Tablet potency weights, Reforging odds, XP costs, Transmutation, Unravel refunds, enchantment blacklists, and optional restrictions on traditional enchantment acquisition.

Datapacks can override or extend the default systems:

- `data/<namespace>/runic_attunement/enchantments/` – enchantment definitions, required materials, Tablet potency and XP.
- `data/<namespace>/runic_attunement/loot_profiles/` – Tablet injection by loot table.
- `data/<namespace>/runic_attunement/compatibility/` – equipment eligibility per enchantment, using `extend`, `replace`, `allow` and `deny` selectors.

The mod does **not** ship with active item-compatibility overrides. A rule may allow item IDs or item tags, and deny rules take priority. Datapacks can be reloaded with `/reload`; close and reopen the table afterward to refresh client rules.

See the supplied JSON files in `src/main/resources/data/runic_attunement/` for working examples of definitions and loot profiles.

## Requirements

- **Minecraft:** 1.20.1
- **Loader:** Fabric
- **Java:** 17+
- **Required:** Fabric API
- **Optional:** Jade, Enchanted ToolTips
- **Multiplayer:** Install on both client and server

## Source provenance and build status

This repository is the publicly viewable **1.3.8 source-recovery snapshot**, reconstructed from the SHA-256-verified CurseForge production JAR using **Vineflower 1.12.0**. The earlier CFR reconstruction did not compile and has been replaced. The historical private GitHub repository did not contain the original 1.3.8 hand-written Java source.

**Verified production release:** `Runic-Attunement-1.3.8-Fabric-1.20.1.jar`, CurseForge project `1737183`, file `9595336`, published **13 November 2026**.

**Verified JAR SHA-256:** `0661dd74284e329389faaf20613f649d7ce06556da5bdf0d60def60e055e0f12`

All release assets, data, mixin configurations and recovered Java files are included. The Java uses intermediary Minecraft class/method names, with decompiler-generated type and bridge errors corrected. **The recovered source now passes a clean Java 17 Gradle build.** The rebuilt JAR has all 86 original compiled class paths and preserves the production Mixin reference map, but it is **not byte-for-byte identical or proven in-game equivalent**. Continue using the original CurseForge binary for normal gameplay until the experimental rebuild passes regression testing.

The `build.gradle` is a recovery toolchain based on Java 17, Gradle 8.8, Fabric Loom 1.7.4 and public dependency repositories. The build workflow checks compilation and packaging. Runtime tests must separately cover table opening, reforge, transmutation, unravel, enchantment eligibility, client/server packets, and Jade integration. Rebuilds are experimental and are not official 1.3.8 updates.

## License

**Copyright © 2026 Path of the Ascended. All Rights Reserved.**

The source is shared for viewing and inspection, not under an open-source licence. See [LICENSE](LICENSE).

## AI-Assisted Development Disclosure

This mod was written with the assistance of AI. All code has been reviewed, tested, and refined by a human developer to ensure functionality, compatibility, and performance.
