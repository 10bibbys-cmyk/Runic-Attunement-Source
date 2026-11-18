# Runic Attunement 1.3.8.2 — Information popup fix

**Minecraft 1.20.1 | Fabric | Java 17+**

## Fixed

- Fixed enchantment information panels opened with **[i]** displaying material-cost icons over the description text.
- Information panels now appear above the rest of the Refined Classic GUI, including its search bar and item layers.
- Centered information panels over the Runic Attunement interface and corrected the close button hover region.
- Material-cost icons are hidden only while a description is open and return immediately when it closes.

## Unchanged

No changes to enchantment rules, XP and item costs, Runic Tablets, Attunement, Transmutation, Reforging, Unravel, configurations or player progression.

Includes the server-lifecycle shutdown compatibility fix from 1.3.8.1.

## Installation

Replace the previous Runic Attunement JAR with **1.3.8.2**. Do not run multiple versions simultaneously. Install the mod on both client and server where required by multiplayer.

## Testing and release provenance

The centered popup and its corrected layering were confirmed in-game in Path of the Ascended: Next Gen before this version bump. GitHub Actions compiles and checks the final packaged JAR.

**Source provenance:** The current development tree was recovered from the verified 1.3.8 production binary. The 1.3.8.1 public hotfix was made by patching that binary. Version 1.3.8.2 is a fresh build from the recovered source and is *not claimed to be byte-for-byte identical to the 1.3.8.1 production binary apart from the popup changes*. Dedicated-server, broader multiplayer and full pack regression checks remain recommended.
