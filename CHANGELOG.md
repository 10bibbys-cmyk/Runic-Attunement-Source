# Changelog

## 1.3.8.2 — Enchantment information popup fix

**Minecraft 1.20.1 · Fabric · Java 17+**

### Fixed

- Corrected the **[i] enchantment information popup** in the Refined Classic Attunement and Transmutation interface. Material-cost icons and the search field no longer draw over enchantment descriptions.
- Rendered the information panel after the other interface layers, keeping its heading and description readable.
- Centered the popup over the Runic Attunement interface and aligned the close button's hover area.
- Temporarily hides the underlying material-cost icons while an information popup is open. The costs return as soon as the popup closes.

### Unchanged

- Runic Tablet progression, enchantment eligibility, material and XP costs, Attunement, Transmutation, Reforging and Unravel mechanics are unchanged.
- The 1.3.8.1 server-shutdown lifecycle compatibility fix is retained.

### Installation and verification

- Replace **1.3.8.1** with **1.3.8.2**; do not install both versions.
- The corrected popup positioning and readable description were confirmed in-game by the PotA maintainer.
- This release is compiled from the recovered 1.3.8.x source tree, not a byte-identical continuation of the original 1.3.8.1 minimally patched production binary. Wider dedicated-server and multiplayer regression testing remains a separate verification step.

# Runic Attunement 1.3.8.1 — Shutdown hotfix

**Minecraft 1.20.1 · Fabric · Java 17+**

## Fixed

- Fixed a Fabric server-lifecycle callback compatibility error that could produce `AbstractMethodError` when leaving a world or shutting down an integrated server.
- The shutdown listener now uses the runtime Fabric API's callback interface, so enchantment-compatibility state is cleared correctly between server sessions.
- Prevents the lifecycle exception from appearing during repeated world shutdowns and reconnects.

## Unchanged

- No changes to Runic Tablet progression, Attunement, Transmutation, Reforging, Unravel refunds or enchanting costs.
- No changes to enchantment eligibility rules, multiplayer packet handling, GUI layout, loot tables, datapacks or configuration defaults.

## Installation

Replace Runic Attunement **1.3.8** with **1.3.8.1**. Do not install both JARs at once. The mod requires Fabric API and must be installed on both client and dedicated server for multiplayer.

## Verification and provenance

The maintainer confirmed the hotfix works across repeated world logins and shutdowns in Path of the Ascended: Next Gen. The shipped binary is a minimal patch of the verified 1.3.8 production JAR, **not** a complete source-derived Gradle rebuild. It preserves all original class/resource entries apart from the shutdown handler and `fabric.mod.json`, and adds one small lifecycle helper class.

Tested hotfix JAR: `Runic-Attunement-1.3.8.1-Fabric-1.20.1.jar`

SHA-256: `731e377e6e1340c3246bd0872683a3d570c0f6a076abc69463f633eef6dd26d8`

The original production 1.3.8 JAR remains unchanged. The current GitHub source repositories are recovered sources for maintenance and are not byte-for-byte identical to the shipped hotfix.


## 1.3.8 — Source recovery maintenance (14 November 2026)

- Replaced the initial non-compiling CFR source snapshot with a Vineflower 1.12.0 reconstruction of the verified published 1.3.8 JAR.
- Repaired decompiler-generated type errors, compiler bridge methods, Mixin selector declarations, and the original Runic Table mouse-click handler.
- Added a reproducible Java 17 / Fabric Loom 1.7.4 build that passes Gradle compilation and packaging.
- Preserved all original compiled class paths, assets, datapack content, and the Runic Mixin reference map.
- **This is source and build maintenance, not a new gameplay release.** The existing 1.3.8 CurseForge JAR has not been changed.
- On 14 November 2026, the project maintainer confirmed the rebuilt 1.3.8 JAR worked in-game in the PotA test instance. Clean builds and a dedicated-server smoke test also passed. The CurseForge binary was not replaced.

Runic Attunement 1.3.8 includes Tablet-based Attunement, Reforging, Transmutation, Unravel refunds, datapack-driven equipment compatibility, optional Jade and Enchanted ToolTips support, and a refined Runic Table interface.
