/*
 * Decompiled with CFR 0.152.
 */
package dev.runicattunement.data;

public enum CostKind {
    UNIVERSAL,
    THEMATIC,
    DIMENSIONAL;


    public static CostKind fromString(String value) {
        return switch (value.toLowerCase()) {
            case "universal", "lapis" -> UNIVERSAL;
            case "thematic", "material" -> THEMATIC;
            case "dimensional", "reagent" -> DIMENSIONAL;
            default -> throw new IllegalArgumentException("Unknown Runic Attunement cost kind: " + value);
        };
    }
}

