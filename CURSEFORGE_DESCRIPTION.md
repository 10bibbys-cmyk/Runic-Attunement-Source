# Runic Attunement

**Runic Attunement** is a deterministic, exploration-driven enchanting system for Minecraft **Fabric 1.20.1**. Instead of relying on random enchantment rolls, players discover reusable Runic Tablets and use them to choose exactly which enchantments to apply or upgrade.

Place your Runic Tablets inside nearby Chiseled Bookshelves, then open the Runic Attunement Table (the vanilla Enchanting Table) to browse the enchantments you've discovered. Each Attunement upgrades an enchantment one level at a time, with configurable Lapis Lazuli, thematic material and **raw XP point** costs.

## Features

- **Runic Tablets:** Explore structures to find enchantment knowledge in three potency tiers: Latent, Resonant and Pristine. Tablets remain reusable after Attunement.
- **Controlled Attunement:** Apply or improve eligible enchantments without random rolls. The Table shows available enchantments, levels, costs and information in a refined two-tab interface.
- **Runic Dust and Reforging:** Grind unwanted Tablets into potency-specific Dust and use it at an Anvil to improve Tablet potency, with configurable success chances.
- **Transmutation:** Transfer selected enchantments between compatible equipment using configured materials and XP costs.
- **Unravel:** Remove enchantments or curses and receive configurable refunds of their resource and XP costs (50% by default).
- **Exploration and advancements:** Includes 37 built-in enchantment definitions, 18 vanilla structure loot profiles and its own advancement progression.
- **Equipment compatibility:** Datapack-based enchantment eligibility for vanilla and modded equipment, using per-enchantment allow, deny, extend and replace rules.
- **Optional integrations:** Enchanted ToolTips provides enchantment descriptions on Tablets and information buttons in the interface. Jade shows inscribed Tablet enchantments in individual Chiseled Bookshelf slots.

## Configuration and datapacks

The main configuration is generated at `config/runic_attunement.json`. It supports Tablet loot and potency distribution, XP costs, Reforging, Transmutation, Unravel refunds and controls for alternative enchantment acquisition (including enchanted books, trades and vanilla Anvil enchanting).

Datapack authors can customize the system without modifying Java code:

- `data/<namespace>/runic_attunement/enchantments/` — enchantment definitions and costs
- `data/<namespace>/runic_attunement/loot_profiles/` — structure Tablet drops
- `data/<namespace>/runic_attunement/compatibility/` — equipment eligibility and restrictions

## Requirements

- Minecraft **1.20.1**
- **Fabric Loader 0.16.14+** and **Fabric API**
- **Java 17+**
- Install on **both client and server** for multiplayer.
- Optional: **Enchanted ToolTips** and **Jade** (neither is required).

Runic Attunement was originally developed for **Path of the Ascended**, but can be configured for other Fabric 1.20.1 modpacks.

**Public Source Code:** https://github.com/10bibbys-cmyk/Runic-Attunement-Source

**License:** All Rights Reserved.

**AI-Assisted Development Disclosure:** This mod was written with the assistance of AI. All code has been reviewed, tested, and refined by a human developer to ensure functionality, compatibility, and performance.
