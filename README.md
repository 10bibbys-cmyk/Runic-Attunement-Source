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

## Current release — 1.3.8.2

**Runic Attunement 1.3.8.2** fixes the Refined Classic enchantment information panel opened with `[i]`.

- Material-cost icons and the search bar no longer overlap the popup.
- The information panel renders above the GUI and is centered in the Runic Attunement interface.
- The close button's hover area is aligned, and the hidden material icons return when the popup closes.
- Enchanting costs, eligible items, progression, and the other Runic systems remain unchanged.

The maintainer confirmed the final popup placement and readability in-game in Path of the Ascended: Next Gen. [Read the 1.3.8.2 release notes](RELEASE_NOTES_1.3.8.2.md).

**Build provenance:** 1.3.8.2 is compiled from this recovered source and is not claimed to be byte-identical to the previous 1.3.8.1 minimally patched production JAR. Dedicated-server and wider multiplayer regression checks remain recommended.

## Previous hotfix — 1.3.8.1

**Current maintenance hotfix:** `Runic-Attunement-1.3.8.1-Fabric-1.20.1.jar` (Fabric 1.20.1). It fixes a `SERVER_STOPPED` callback descriptor mismatch that caused `AbstractMethodError` when exiting worlds. The maintainer confirmed the patch across repeated world logins and shutdowns in Path of the Ascended: Next Gen.

**Tested JAR SHA-256:** `731e377e6e1340c3246bd0872683a3d570c0f6a076abc69463f633eef6dd26d8`.

This specific field-tested JAR is a surgical bytecode patch of the verified production 1.3.8 JAR, not a full rebuild from this recovered source snapshot. The two original changed members are `fabric.mod.json` and `RunicCompatibility.class`; one new `RunicStoppedLifecycle.class` is added. All other compiled classes and resources are byte-identical to the previous release. A fresh Gradle build of the maintained recovered source is not necessarily byte-identical to this field-tested binary.

See [1.3.8.1 release notes](RELEASE_NOTES_1.3.8.1.md) and [changelog](CHANGELOG.md). Gameplay mechanics, prices, datapacks, UI and multiplayer packet protocols are unchanged by this hotfix.

## Source provenance and build status

This repository is the publicly viewable **1.3.8.2 maintained source-recovery snapshot**, reconstructed from the SHA-256-verified CurseForge production JAR using **Vineflower 1.12.0**. The earlier CFR reconstruction did not compile and has been replaced. The historical private GitHub repository did not contain the original 1.3.8 hand-written Java source.

**Verified production release:** `Runic-Attunement-1.3.8-Fabric-1.20.1.jar`, CurseForge project `1737183`, file `9595336`, published **13 November 2026**.

**Verified JAR SHA-256:** `0661dd74284e329389faaf20613f649d7ce06556da5bdf0d60def60e055e0f12`

All release assets, data, mixin configurations and recovered Java files are included. The Java uses intermediary Minecraft class/method names, with decompiler-generated type and bridge errors corrected. **The recovered source passes a clean Java 17 Gradle build.** The rebuilt JAR retains all 86 original compiled class paths and the original Mixin reference map. It is **not byte-for-byte identical** to the published CurseForge binary. The project maintainer has now confirmed the rebuilt 1.3.8 JAR works in the PotA test instance, including the previously identified regression checks. The published CurseForge release remains unchanged.

The `build.gradle` is a recovery toolchain based on Java 17, Gradle 8.8, Fabric Loom 1.7.4 and public dependency repositories. Both private and public GitHub Actions builds pass. A dedicated Fabric **development-server smoke test passed** on 14 November 2026: Runic Attunement initialized, registered multiplayer packets, loaded 37 enchantment definitions and 18 loot profiles, and reached the server-ready state. [View the smoke test](https://github.com/10bibbys-cmyk/Runic-Attunement/actions/runs/43637857538).

**Maintainer regression confirmation (14 November 2026):** The rebuilt 1.3.8 JAR was tested in the Path of the Ascended instance and its primary gameplay systems were reported working, including the Runic Table, Attunement, Reforging, Transmutation, Unravel, and mod integrations. This is an in-game functional confirmation, **not a claim of byte-for-byte equivalence** or proof that every configuration and external mod combination has been exercised. No gameplay changes were intentionally introduced during source recovery.

## License

**Copyright © 2026 Path of the Ascended. All Rights Reserved.**

The source is shared for viewing and inspection, not under an open-source licence. See [LICENSE](LICENSE).

## AI-Assisted Development Disclosure

This mod was written with the assistance of AI. All code has been reviewed, tested, and refined by a human developer to ensure functionality, compatibility, and performance.
