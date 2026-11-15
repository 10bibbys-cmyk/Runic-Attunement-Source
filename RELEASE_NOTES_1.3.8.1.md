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
