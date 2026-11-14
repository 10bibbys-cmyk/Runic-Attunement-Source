/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_1657
 *  net.minecraft.class_1661
 *  net.minecraft.class_1703
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_1887
 *  net.minecraft.class_1890
 *  net.minecraft.class_1935
 *  net.minecraft.class_2561
 *  net.minecraft.class_2583
 *  net.minecraft.class_2960
 *  net.minecraft.class_332
 *  net.minecraft.class_342
 *  net.minecraft.class_364
 *  net.minecraft.class_465
 *  net.minecraft.class_5250
 *  net.minecraft.class_5348
 *  net.minecraft.class_7923
 */
package dev.runicattunement.client;

import dev.runicattunement.client.RunicUiTheme;
import dev.runicattunement.compat.EnchantedTooltipsBridge;
import dev.runicattunement.compat.RunicCompatibility;
import dev.runicattunement.data.EnchantmentDefinition;
import dev.runicattunement.data.IngredientCost;
import dev.runicattunement.grimoire.RunicGrimoireUi;
import dev.runicattunement.screen.RunicInscriptionScreenHandler;
import dev.runicattunement.transmutation.TransmutationRules;
import dev.runicattunement.unravel.RunicUnravelClient;
import dev.runicattunement.util.RunicExperience;
import dev.runicattunement.util.RunicPotency;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.minecraft.class_1657;
import net.minecraft.class_1661;
import net.minecraft.class_1703;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1887;
import net.minecraft.class_1890;
import net.minecraft.class_1935;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_342;
import net.minecraft.class_364;
import net.minecraft.class_465;
import net.minecraft.class_5250;
import net.minecraft.class_5348;
import net.minecraft.class_7923;

public final class RunicInscriptionScreen
extends class_465<RunicInscriptionScreenHandler> {
    private static final int WIDTH = 320;
    private static final int HEIGHT = 275;
    private static final int LEFT_X = 8;
    private static final int LEFT_WIDTH = 76;
    private static final int MID_X = 88;
    private static final int MID_WIDTH = 132;
    private static final int RIGHT_X = 224;
    private static final int RIGHT_WIDTH = 88;
    private static final int TAB_Y = 21;
    private static final int ATTUNEMENT_TAB_X = 79;
    private static final int ATTUNEMENT_TAB_WIDTH = 77;
    private static final int TRANSMUTATION_TAB_X = 158;
    private static final int TRANSMUTATION_TAB_WIDTH = 84;
    private static final int TAB_HEIGHT = 15;
    private static final int PRIMARY_SLOT_X = 20;
    private static final int PRIMARY_SLOT_Y = 66;
    private static final int TARGET_SLOT_X = 59;
    private static final int TARGET_SLOT_Y = 66;
    private static final int SEARCH_X = 228;
    private static final int SEARCH_Y = 62;
    private static final int SEARCH_WIDTH = 78;
    private static final int SEARCH_HEIGHT = 16;
    private static final int FILTER_Y = 80;
    private static final int FILTER_HEIGHT = 12;
    private static final int FILTER_ALL_X = 228;
    private static final int FILTER_ALL_WIDTH = 24;
    private static final int FILTER_T1_X = 254;
    private static final int FILTER_T2_X = 271;
    private static final int FILTER_T3_X = 288;
    private static final int FILTER_TIER_WIDTH = 15;
    private static final int ATT_LIST_X = 228;
    private static final int ATT_LIST_Y = 94;
    private static final int ATT_LIST_WIDTH = 75;
    private static final int ATT_ROW_HEIGHT = 13;
    private static final int ATT_VISIBLE_ROWS = 5;
    private static final int ATT_SCROLL_X = 305;
    private static final int ATT_SCROLL_Y = 94;
    private static final int ATT_SCROLL_WIDTH = 4;
    private static final int ATT_SCROLL_HEIGHT = 65;
    private static final int TRANS_LIST_X = 92;
    private static final int TRANS_LIST_Y = 62;
    private static final int TRANS_LIST_WIDTH = 120;
    private static final int TRANS_ROW_HEIGHT = 18;
    private static final int TRANS_VISIBLE_ROWS = 4;
    private static final int TRANS_SCROLL_X = 214;
    private static final int TRANS_SCROLL_Y = 62;
    private static final int TRANS_SCROLL_WIDTH = 4;
    private static final int TRANS_SCROLL_HEIGHT = 72;
    private static final int TRANS_SELECT_ALL_X = 94;
    private static final int TRANS_CLEAR_X = 156;
    private static final int TRANS_SMALL_BUTTON_Y = 136;
    private static final int TRANS_SMALL_BUTTON_WIDTH = 58;
    private static final int TRANS_SMALL_BUTTON_HEIGHT = 9;
    private static final int ACTION_X = 104;
    private static final int ACTION_Y_ATTUNE = 147;
    private static final int ACTION_Y_TRANSMUTE = 147;
    private static final int ACTION_WIDTH = 100;
    private static final int ACTION_HEIGHT = 23;
    private static final int ATT_COST_X = 95;
    private static final int ATT_COST_Y = 106;
    private static final int ATT_COST_WIDTH = 116;
    private static final int ATT_COST_COLUMNS = 2;
    private static final int ATT_COST_VISIBLE_ROWS = 2;
    private static final int ATT_COST_ROW_HEIGHT = 18;
    private static final int ATT_COST_SCROLL_X = 216;
    private static final int ATT_COST_SCROLL_Y = 106;
    private static final int ATT_COST_SCROLL_WIDTH = 3;
    private static final int ATT_COST_SCROLL_HEIGHT = 36;
    private static final class_2960 ALT_FONT = new class_2960("minecraft", "alt");
    private static final class_2960 EXPERIENCE_ORB_TEXTURE = new class_2960("runic_attunement", "textures/gui/xp_orb_icon.png");
    private RunicUiTheme theme = RunicUiTheme.load();
    private class_342 searchField;
    private class_2960 selectedEnchantment;
    private int scrollOffset;
    private int tierFilter;
    private final Set<class_2960> selectedTransfers = new LinkedHashSet<class_2960>();
    private final Set<class_2960> removeCurses = new LinkedHashSet<class_2960>();
    private int transmutationScroll;
    private String transmutationFingerprint = "";
    private boolean draggingAttunementScrollbar;
    private boolean draggingTransmutationScrollbar;
    private boolean draggingAttunementCostScrollbar;
    private int attunementCostScrollRow;
    private String attunementCostFingerprint = "";

    public RunicInscriptionScreen(RunicInscriptionScreenHandler handler, class_1661 inventory, class_2561 title) {
        super((class_1703)handler, inventory, title);
        this.field_2792 = 320;
        this.field_2779 = 275;
        this.field_25269 = 79;
        this.field_25270 = 184;
        this.field_25267 = 0;
        this.field_25268 = 0;
    }

    protected void method_25426() {
        this.theme = RunicUiTheme.load();
        super.method_25426();
        this.searchField = new class_342(this.field_22793, this.field_2776 + 228, this.field_2800 + 62, 78, 16, (class_2561)class_2561.method_43471((String)"screen.runic_attunement.search"));
        this.searchField.method_1880(40);
        this.searchField.method_47404((class_2561)class_2561.method_43471((String)"screen.runic_attunement.search"));
        this.searchField.method_1863(ignored -> {
            this.scrollOffset = 0;
            this.keepSelectionValid();
        });
        this.method_37063((class_364)this.searchField);
        this.keepSelectionValid();
        this.updateModeWidgets();
    }

    public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
        this.syncTransmutationState();
        this.syncAttunementCostState();
        this.updateModeWidgets();
        this.method_25420(context);
        super.method_25394(context, mouseX, mouseY, delta);
        this.method_2380(context, mouseX, mouseY);
        this.drawFullRowNameTooltip(context, mouseX, mouseY);
        RunicGrimoireUi.maybeRenderInfo((Object)this, context, mouseX, mouseY);
        RunicGrimoireUi.classicButton((Object)this, context);
    }

    private void updateModeWidgets() {
        if (this.searchField != null) {
            this.searchField.field_22764 = !((RunicInscriptionScreenHandler)this.field_2797).isTransmutationMode();
            this.searchField.field_22763 = !((RunicInscriptionScreenHandler)this.field_2797).isTransmutationMode();
        }
    }

    /*
     * WARNING - void declaration
     */
    protected void method_2389(class_332 class_3322, float f, int n, int n2) {
        void mouseY;
        void mouseX;
        void context;
        if (RunicGrimoireUi.drawBackground((Object)this, class_3322, f, n, n2)) {
            return;
        }
        context.method_25291(this.theme.backgroundTexture(), this.field_2776, this.field_2800, 0, 0.0f, 0.0f, this.field_2792, this.field_2779, 320, 275);
        this.drawTabs((class_332)context, (int)mouseX, (int)mouseY);
        if (((RunicInscriptionScreenHandler)this.field_2797).isTransmutationMode()) {
            this.drawSlotFrame((class_332)context, 20, 66, true);
            this.drawSlotFrame((class_332)context, 59, 66, true);
            this.drawTransferArrow((class_332)context);
            this.drawTransmutationList((class_332)context, (int)mouseX, (int)mouseY);
            this.drawScrollbar((class_332)context, 214, 62, 4, 72, TransmutationRules.sourceEnchantments(((RunicInscriptionScreenHandler)this.field_2797).getSourceStack()).size(), 4, this.transmutationScroll, this.draggingTransmutationScrollbar);
            this.drawSmallTransmutationButtons((class_332)context, (int)mouseX, (int)mouseY);
            this.drawTransmuteButton((class_332)context, (int)mouseX, (int)mouseY);
        } else {
            this.drawSlotFrame((class_332)context, 20, 66, true);
            this.drawTierFilters((class_332)context, (int)mouseX, (int)mouseY);
            this.drawEnchantmentList((class_332)context, (int)mouseX, (int)mouseY);
            this.drawScrollbar((class_332)context, 305, 94, 4, 65, this.filteredEnchantments().size(), 5, this.scrollOffset, this.draggingAttunementScrollbar);
            this.drawInscribeButton((class_332)context, (int)mouseX, (int)mouseY);
        }
    }

    /*
     * WARNING - void declaration
     */
    protected void method_2388(class_332 class_3322, int n, int n2) {
        void context;
        if (RunicGrimoireUi.drawForeground((Object)this, class_3322)) {
            return;
        }
        context.method_27534(this.field_22793, (class_2561)class_2561.method_43471((String)"screen.runic_attunement.title"), this.field_2792 / 2, 7, this.theme.title());
        class_5250 leftRunes = class_2561.method_43470((String)"bind").method_10862(class_2583.field_24360.method_27704(ALT_FONT));
        class_5250 rightRunes = class_2561.method_43470((String)"rune").method_10862(class_2583.field_24360.method_27704(ALT_FONT));
        context.method_27534(this.field_22793, (class_2561)leftRunes, 48, 8, this.theme.accent());
        context.method_27534(this.field_22793, (class_2561)rightRunes, 272, 8, this.theme.accent());
        context.method_27534(this.field_22793, this.field_29347, this.field_2792 / 2, this.field_25270, this.theme.text());
        if (((RunicInscriptionScreenHandler)this.field_2797).isTransmutationMode()) {
            this.drawTransmutationForeground((class_332)context);
        } else {
            this.drawAttunementForeground((class_332)context);
        }
    }

    private void drawAttunementForeground(class_332 context) {
        context.method_27534(this.field_22793, (class_2561)class_2561.method_43471((String)"screen.runic_attunement.equipment"), 46, 48, this.theme.text());
        context.method_27534(this.field_22793, (class_2561)class_2561.method_43471((String)"screen.runic_attunement.selected_attunement"), 154, 48, this.theme.text());
        context.method_27534(this.field_22793, (class_2561)class_2561.method_43471((String)"screen.runic_attunement.attunement"), 268, 48, this.theme.text());
        this.drawEquipmentDetails(context);
        this.drawSelectedDetails(context);
        String count = class_2561.method_43469((String)"screen.runic_attunement.available_short", (Object[])new Object[]{this.filteredEnchantments().size()}).getString();
        context.method_25300(this.field_22793, count, 268, 162, this.theme.muted());
    }

    private void drawTabs(class_332 context, int mouseX, int mouseY) {
        this.drawTab(context, 79, 77, (class_2561)class_2561.method_43471((String)"screen.runic_attunement.tab_attunement"), !((RunicInscriptionScreenHandler)this.field_2797).isTransmutationMode(), true, RunicInscriptionScreen.isWithin(mouseX, mouseY, this.field_2776 + 79, this.field_2800 + 21, 77, 15));
        this.drawTab(context, 158, 84, (class_2561)class_2561.method_43471((String)"screen.runic_attunement.tab_transmutation"), ((RunicInscriptionScreenHandler)this.field_2797).isTransmutationMode(), ((RunicInscriptionScreenHandler)this.field_2797).getTransmutationSettings().enabled(), RunicInscriptionScreen.isWithin(mouseX, mouseY, this.field_2776 + 158, this.field_2800 + 21, 84, 15));
    }

    private void drawTab(class_332 context, int localX, int width, class_2561 label, boolean selected, boolean enabled, boolean hovered) {
        int base = !enabled ? this.theme.buttonDisabled() : (selected ? this.theme.panelSelected() : (hovered ? this.theme.panelHover() : this.theme.panel()));
        int border = selected ? this.theme.accentBright() : this.theme.border();
        context.method_25294(this.field_2776 + localX, this.field_2800 + 21, this.field_2776 + localX + width, this.field_2800 + 21 + 15, RunicInscriptionScreen.argb(base));
        context.method_49601(this.field_2776 + localX, this.field_2800 + 21, width, 15, RunicInscriptionScreen.argb(border));
        if (selected) {
            context.method_25294(this.field_2776 + localX + 2, this.field_2800 + 21 + 15 - 2, this.field_2776 + localX + width - 2, this.field_2800 + 21 + 15 - 1, RunicInscriptionScreen.argb(this.theme.accentBright()));
        }
        int colour = !enabled ? this.theme.muted() : (selected ? this.theme.title() : this.theme.text());
        String trimmed = this.field_22793.method_27523(label.getString(), width - 8);
        context.method_25300(this.field_22793, trimmed, this.field_2776 + localX + width / 2, this.field_2800 + 21 + 3, colour);
    }

    private String fitName(String value, int maxWidth) {
        if (this.field_22793.method_1727(value) <= maxWidth) {
            return value;
        }
        return this.field_22793.method_27523(value, Math.max(0, maxWidth - this.field_22793.method_1727("\u2026"))) + "\u2026";
    }

    private void drawFullRowNameTooltip(class_332 class_3322, int n, int n2) {
        if (RunicGrimoireUi.active((Object)this)) {
            return;
        }
        if (((RunicInscriptionScreenHandler)this.field_2797).isTransmutationMode()) {
            String name;
            if (!RunicInscriptionScreen.isWithin((double)mouseX, (double)mouseY, this.field_2776 + 92, this.field_2800 + 62, 120, 72)) {
                return;
            }
            int index = this.transmutationScroll + (mouseY - (this.field_2800 + 62)) / 18;
            List<class_2960> entries = TransmutationRules.sourceEnchantments(((RunicInscriptionScreenHandler)this.field_2797).getSourceStack());
            if (index < 0 || index >= entries.size()) {
                return;
            }
            class_1887 enchantment = class_7923.field_41176.method_17966(entries.get(index)).orElse(null);
            if (enchantment != null && this.field_22793.method_1727(name = enchantment.method_8179(class_1890.method_8225((class_1887)enchantment, (class_1799)((RunicInscriptionScreenHandler)this.field_2797).getSourceStack())).getString()) > 94) {
                context.method_51438(this.field_22793, (class_2561)class_2561.method_43470((String)name), (int)mouseX, (int)mouseY);
            }
        } else {
            String name;
            if (!RunicInscriptionScreen.isWithin((double)mouseX, (double)mouseY, this.field_2776 + 228, this.field_2800 + 94, 75, 65)) {
                return;
            }
            int index = this.scrollOffset + (mouseY - (this.field_2800 + 94)) / 13;
            List<class_2960> entries = this.filteredEnchantments();
            if (index < 0 || index >= entries.size()) {
                return;
            }
            class_1887 enchantment = class_7923.field_41176.method_17966(entries.get(index)).orElse(null);
            if (enchantment != null && this.field_22793.method_1727(name = class_2561.method_43471((String)enchantment.method_8184()).getString()) > 56) {
                context.method_51438(this.field_22793, (class_2561)class_2561.method_43470((String)name), (int)mouseX, (int)mouseY);
            }
        }
    }

    private void drawTierFilters(class_332 context, int mouseX, int mouseY) {
        this.drawFilterButton(context, 228, 24, 0, (class_2561)class_2561.method_43471((String)"screen.runic_attunement.filter_all"), mouseX, mouseY);
        this.drawFilterButton(context, 254, 15, 1, (class_2561)class_2561.method_43470((String)"I"), mouseX, mouseY);
        this.drawFilterButton(context, 271, 15, 2, (class_2561)class_2561.method_43470((String)"II"), mouseX, mouseY);
        this.drawFilterButton(context, 288, 15, 3, (class_2561)class_2561.method_43470((String)"III"), mouseX, mouseY);
    }

    private void drawFilterButton(class_332 context, int localX, int width, int filter, class_2561 label, int mouseX, int mouseY) {
        int fill;
        boolean selected = this.tierFilter == filter;
        boolean hovered = RunicInscriptionScreen.isWithin(mouseX, mouseY, this.field_2776 + localX, this.field_2800 + 80, width, 12);
        int n = selected ? this.theme.panelSelected() : (fill = hovered ? this.theme.panelHover() : this.theme.panel());
        int border = selected ? (filter == 0 ? this.theme.accentBright() : this.theme.tier(filter)) : this.theme.border();
        context.method_25294(this.field_2776 + localX, this.field_2800 + 80, this.field_2776 + localX + width, this.field_2800 + 80 + 12, RunicInscriptionScreen.argb(fill));
        context.method_49601(this.field_2776 + localX, this.field_2800 + 80, width, 12, RunicInscriptionScreen.argb(border));
        context.method_27534(this.field_22793, label, this.field_2776 + localX + width / 2, this.field_2800 + 80 + 2, selected && filter > 0 ? this.theme.tier(filter) : this.theme.text());
    }

    private void drawSlotFrame(class_332 context, int localX, int localY, boolean enabled) {
        int outer = enabled ? this.theme.border() : this.theme.buttonDisabled();
        context.method_25294(this.field_2776 + localX - 2, this.field_2800 + localY - 2, this.field_2776 + localX + 18, this.field_2800 + localY + 18, -15657959);
        context.method_49601(this.field_2776 + localX - 2, this.field_2800 + localY - 2, 20, 20, RunicInscriptionScreen.argb(outer));
        context.method_25294(this.field_2776 + localX - 1, this.field_2800 + localY - 1, this.field_2776 + localX + 17, this.field_2800 + localY + 17, -14670549);
        context.method_25294(this.field_2776 + localX, this.field_2800 + localY, this.field_2776 + localX + 16, this.field_2800 + localY + 16, -15263202);
        context.method_25294(this.field_2776 + localX - 1, this.field_2800 + localY - 1, this.field_2776 + localX + 17, this.field_2800 + localY, RunicInscriptionScreen.argb(this.theme.accent()));
        context.method_25294(this.field_2776 + localX - 1, this.field_2800 + localY - 1, this.field_2776 + localX, this.field_2800 + localY + 17, RunicInscriptionScreen.argb(this.theme.accent()));
    }

    private void drawTransferArrow(class_332 context) {
        int ox = this.field_2776 + 40;
        int oy = this.field_2800 + 70;
        int c = RunicInscriptionScreen.argb(this.theme.accentBright());
        context.method_25294(ox, oy + 3, ox + 9, oy + 6, c);
        context.method_25294(ox + 8, oy, ox + 10, oy + 9, c);
        context.method_25294(ox + 10, oy + 1, ox + 12, oy + 8, c);
        context.method_25294(ox + 12, oy + 2, ox + 14, oy + 7, c);
        context.method_25294(ox + 14, oy + 3, ox + 15, oy + 6, c);
    }

    private void drawEquipmentDetails(class_332 context) {
        class_1799 target = ((RunicInscriptionScreenHandler)this.field_2797).getEquipmentStack();
        if (target.method_7960()) {
            context.method_51440(this.field_22793, (class_5348)class_2561.method_43471((String)"screen.runic_attunement.insert_equipment"), 12, 93, 68, this.theme.muted());
            return;
        }
        String name = this.field_22793.method_27523(target.method_7964().getString(), 69);
        context.method_51433(this.field_22793, name, 12, 91, this.theme.text(), false);
        Map enchants = class_1890.method_8222((class_1799)target);
        if (enchants.isEmpty()) {
            context.method_51439(this.field_22793, (class_2561)class_2561.method_43471((String)"screen.runic_attunement.no_enchantments"), 12, 105, this.theme.muted(), false);
            return;
        }
        ArrayList sorted = new ArrayList(enchants.entrySet());
        sorted.sort(Comparator.comparing(entry -> ((class_1887)entry.getKey()).method_8184()));
        int row = 0;
        for (Map.Entry entry2 : sorted) {
            if (row >= 5) {
                context.method_51433(this.field_22793, "\u2026", 12, 105 + row * 10, this.theme.muted(), false);
                break;
            }
            String level = ((class_1887)entry2.getKey()).method_8179(((Integer)entry2.getValue()).intValue()).getString();
            context.method_51433(this.field_22793, this.field_22793.method_27523(level, 68), 12, 105 + row * 10, this.theme.muted(), false);
            ++row;
        }
    }

    private void drawSelectedDetails(class_332 context) {
        this.keepSelectionValid();
        if (this.selectedEnchantment == null) {
            context.method_51440(this.field_22793, (class_5348)class_2561.method_43471((String)"screen.runic_attunement.no_attuned"), 95, 65, 118, this.theme.muted());
            return;
        }
        class_1887 enchantment = class_7923.field_41176.method_17966(this.selectedEnchantment).orElse(null);
        EnchantmentDefinition definition = ((RunicInscriptionScreenHandler)this.field_2797).getDefinition(this.selectedEnchantment);
        if (enchantment == null || definition == null) {
            return;
        }
        String name = class_2561.method_43471((String)enchantment.method_8184()).getString();
        context.method_51433(this.field_22793, this.field_22793.method_27523(name, 100), 95, 65, this.theme.tier(definition.tabletTier()), false);
        this.drawTierBadge(context, 199, 63, definition.tabletTier());
        class_1799 target = ((RunicInscriptionScreenHandler)this.field_2797).getEquipmentStack();
        int current = target.method_7960() ? 0 : class_1890.method_8225((class_1887)enchantment, (class_1799)target);
        int next = current + 1;
        boolean maxed = current >= enchantment.method_8183() || definition.level(next).isEmpty();
        String levelText = maxed ? class_2561.method_43471((String)"screen.runic_attunement.maximum").getString() : RunicInscriptionScreen.roman(current) + "  \u2192  " + RunicInscriptionScreen.roman(next);
        context.method_51433(this.field_22793, levelText, 95, 79, maxed ? this.theme.maximum() : this.theme.title(), false);
        if (RunicInscriptionScreen.hasConflict(enchantment, target)) {
            context.method_51439(this.field_22793, (class_2561)class_2561.method_43471((String)"screen.runic_attunement.conflict"), 95, 94, this.theme.incompatible(), false);
            return;
        }
        if (!maxed) {
            List<IngredientCost> costs = ((RunicInscriptionScreenHandler)this.field_2797).getClientCosts(definition, next);
            context.method_51439(this.field_22793, (class_2561)class_2561.method_43471((String)"screen.runic_attunement.cost"), 95, 94, this.theme.muted(), false);
            this.drawScrollableCostGrid(context, costs);
        }
    }

    private void drawTierBadge(class_332 context, int localX, int localY, int tier) {
        int colour = this.theme.tier(tier);
        context.method_25294(localX, localY, localX + 14, localY + 11, -15263202);
        context.method_49601(localX, localY, 14, 11, RunicInscriptionScreen.argb(colour));
        context.method_27534(this.field_22793, (class_2561)class_2561.method_43470((String)RunicInscriptionScreen.roman(tier)), localX + 7, localY + 2, colour);
    }

    private void drawScrollableCostGrid(class_332 context, List<IngredientCost> costs) {
        int totalRows = (costs.size() + 2 - 1) / 2;
        int maxRowOffset = Math.max(0, totalRows - 2);
        this.attunementCostScrollRow = Math.min(this.attunementCostScrollRow, maxRowOffset);
        int cellWidth = 58;
        int startIndex = this.attunementCostScrollRow * 2;
        int capacity = 4;
        int shown = Math.min(capacity, Math.max(0, costs.size() - startIndex));
        for (int visible = 0; visible < shown; ++visible) {
            int index = startIndex + visible;
            IngredientCost cost = costs.get(index);
            class_1792 item = class_7923.field_41178.method_17966(cost.itemId()).orElse(null);
            if (item == null) continue;
            int col = visible % 2;
            int row = visible / 2;
            int cx = 95 + col * cellWidth;
            int cy = 106 + row * 18;
            class_1799 icon = new class_1799((class_1935)item);
            int have = this.field_22787 == null || this.field_22787.field_1724 == null ? 0 : RunicInscriptionScreen.countItem(this.field_22787.field_1724.method_31548(), item);
            boolean enough = this.field_22787 != null && this.field_22787.field_1724 != null && (this.field_22787.field_1724.method_31549().field_7477 || have >= cost.count());
            context.method_51427(icon, cx, cy);
            String qty = have + "/" + cost.count();
            context.method_51433(this.field_22793, this.field_22793.method_27523(qty, cellWidth - 19), cx + 18, cy + 5, enough ? this.theme.compatible() : this.theme.incompatible(), false);
        }
        if (totalRows > 2) {
            this.drawLocalScrollbar(context, 216, 106, 3, 36, totalRows, 2, this.attunementCostScrollRow, this.draggingAttunementCostScrollbar);
        }
    }

    private void drawEnchantmentList(class_332 context, int mouseX, int mouseY) {
        int index;
        List<class_2960> filtered = this.filteredEnchantments();
        int maxOffset = Math.max(0, filtered.size() - 5);
        this.scrollOffset = Math.min(this.scrollOffset, maxOffset);
        for (int visible = 0; visible < 5 && (index = visible + this.scrollOffset) < filtered.size(); ++visible) {
            class_2960 id = filtered.get(index);
            class_1887 enchantment = class_7923.field_41176.method_17966(id).orElse(null);
            EnchantmentDefinition definition = ((RunicInscriptionScreenHandler)this.field_2797).getDefinition(id);
            if (enchantment == null || definition == null) continue;
            int localX = 228;
            int localY = 94 + visible * 13;
            boolean hovered = RunicInscriptionScreen.isWithin(mouseX, mouseY, this.field_2776 + localX, this.field_2800 + localY, 75, 12);
            boolean selected = id.equals((Object)this.selectedEnchantment);
            class_1799 target = ((RunicInscriptionScreenHandler)this.field_2797).getEquipmentStack();
            boolean conflict = RunicInscriptionScreen.hasConflict(enchantment, target);
            boolean maximum = this.isAtMaximum(id, enchantment, target);
            int fill = selected ? this.theme.panelSelected() : (hovered ? this.theme.panelHover() : this.theme.panel());
            context.method_25294(this.field_2776 + localX, this.field_2800 + localY, this.field_2776 + localX + 75, this.field_2800 + localY + 13 - 1, RunicInscriptionScreen.argb(fill, selected ? 230 : 205));
            if (selected) {
                context.method_49601(this.field_2776 + localX, this.field_2800 + localY, 75, 12, RunicInscriptionScreen.argb(this.theme.accentBright()));
            }
            int statusReserve = conflict || maximum ? 8 : 0;
            int labelWidth = 43 - statusReserve;
            String label = this.fitName(class_2561.method_43471((String)enchantment.method_8184()).getString(), labelWidth);
            int labelColor = maximum ? this.theme.maximum() : (conflict ? this.theme.incompatible() : this.theme.text());
            context.method_51433(this.field_22793, label, this.field_2776 + localX + 3, this.field_2800 + localY + 2, labelColor, false);
            if (conflict) {
                this.drawCrossIcon(context, localX + 75 - 18, localY + 3, this.theme.incompatible());
            } else if (maximum) {
                this.drawDiamondIcon(context, localX + 75 - 18, localY + 3, this.theme.maximum());
            }
            this.drawTinyTierBadge(context, localX + 75 - 10, localY + 1, definition.tabletTier());
        }
    }

    private void drawTinyTierBadge(class_332 context, int localX, int localY, int tier) {
        int c = this.theme.tier(tier);
        context.method_25294(this.field_2776 + localX, this.field_2800 + localY, this.field_2776 + localX + 9, this.field_2800 + localY + 10, -15394788);
        context.method_49601(this.field_2776 + localX, this.field_2800 + localY, 9, 10, RunicInscriptionScreen.argb(c));
        context.method_27534(this.field_22793, (class_2561)class_2561.method_43470((String)Integer.toString(tier)), this.field_2776 + localX + 4, this.field_2800 + localY + 1, c);
    }

    private void drawCrossIcon(class_332 context, int localX, int localY, int colour) {
        int c = RunicInscriptionScreen.argb(colour);
        context.method_25294(this.field_2776 + localX, this.field_2800 + localY, this.field_2776 + localX + 2, this.field_2800 + localY + 2, c);
        context.method_25294(this.field_2776 + localX + 4, this.field_2800 + localY, this.field_2776 + localX + 6, this.field_2800 + localY + 2, c);
        context.method_25294(this.field_2776 + localX + 2, this.field_2800 + localY + 2, this.field_2776 + localX + 4, this.field_2800 + localY + 4, c);
        context.method_25294(this.field_2776 + localX, this.field_2800 + localY + 4, this.field_2776 + localX + 2, this.field_2800 + localY + 6, c);
        context.method_25294(this.field_2776 + localX + 4, this.field_2800 + localY + 4, this.field_2776 + localX + 6, this.field_2800 + localY + 6, c);
    }

    private void drawDiamondIcon(class_332 context, int localX, int localY, int colour) {
        int c = RunicInscriptionScreen.argb(colour);
        context.method_25294(this.field_2776 + localX + 2, this.field_2800 + localY, this.field_2776 + localX + 4, this.field_2800 + localY + 1, c);
        context.method_25294(this.field_2776 + localX + 1, this.field_2800 + localY + 1, this.field_2776 + localX + 5, this.field_2800 + localY + 2, c);
        context.method_25294(this.field_2776 + localX, this.field_2800 + localY + 2, this.field_2776 + localX + 6, this.field_2800 + localY + 4, c);
        context.method_25294(this.field_2776 + localX + 1, this.field_2800 + localY + 4, this.field_2776 + localX + 5, this.field_2800 + localY + 5, c);
        context.method_25294(this.field_2776 + localX + 2, this.field_2800 + localY + 5, this.field_2776 + localX + 4, this.field_2800 + localY + 6, c);
    }

    private void drawInscribeButton(class_332 class_3322, int n, int n2) {
        RunicUnravelClient.draw((Object)this, class_3322, n, n2);
    }

    private void drawActionButton(class_332 context, int localY, Eligibility eligibility, boolean hovered, class_2561 actionLabel, int experienceCost) {
        int fill;
        int n = !eligibility.allowed() ? this.theme.buttonDisabled() : (fill = hovered ? this.theme.buttonHover() : this.theme.button());
        int border = eligibility.allowed() ? (hovered ? this.theme.accentBright() : this.theme.accent()) : this.theme.border();
        context.method_25294(this.field_2776 + 104, this.field_2800 + localY, this.field_2776 + 104 + 100, this.field_2800 + localY + 23, RunicInscriptionScreen.argb(fill));
        context.method_49601(this.field_2776 + 104, this.field_2800 + localY, 100, 23, RunicInscriptionScreen.argb(border));
        context.method_25294(this.field_2776 + 104 + 1, this.field_2800 + localY + 1, this.field_2776 + 104 + 100 - 1, this.field_2800 + localY + 2, RunicInscriptionScreen.argb(eligibility.allowed() ? this.theme.accent() : this.theme.border(), 180));
        String trimmed = this.field_22793.method_27523(actionLabel.getString(), 94);
        boolean showExperience = ((RunicInscriptionScreenHandler)this.field_2797).getExperienceSettings().enabled();
        int titleY = showExperience ? localY + 3 : localY + 7;
        context.method_25300(this.field_22793, trimmed, this.field_2776 + 104 + 50, this.field_2800 + titleY, eligibility.allowed() ? this.theme.title() : this.theme.muted());
        if (showExperience) {
            int points = Math.max(0, experienceCost);
            boolean enough = this.field_22787 != null && this.field_22787.field_1724 != null && (this.field_22787.field_1724.method_31549().field_7477 || RunicExperience.availablePoints((class_1657)this.field_22787.field_1724) >= points);
            String xpText = class_2561.method_43469((String)"screen.runic_attunement.xp_points", (Object[])new Object[]{points}).getString();
            int textWidth = this.field_22793.method_1727(xpText);
            int groupWidth = 10 + textWidth;
            int groupX = this.field_2776 + 104 + (100 - groupWidth) / 2;
            this.drawExperienceOrb(context, groupX, this.field_2800 + localY + 13);
            context.method_51433(this.field_22793, xpText, groupX + 10, this.field_2800 + localY + 13, enough ? this.theme.compatible() : this.theme.incompatible(), false);
        }
    }

    private void drawExperienceOrb(class_332 context, int drawX, int drawY) {
        context.method_25291(EXPERIENCE_ORB_TEXTURE, drawX, drawY, 0, 0.0f, 0.0f, 8, 8, 8, 8);
    }

    private void drawTransmutationForeground(class_332 context) {
        context.method_27534(this.field_22793, (class_2561)class_2561.method_43471((String)"screen.runic_attunement.source_target"), 46, 48, this.theme.text());
        context.method_27534(this.field_22793, (class_2561)class_2561.method_43471((String)"screen.runic_attunement.transfers"), 154, 48, this.theme.text());
        context.method_27534(this.field_22793, (class_2561)class_2561.method_43471((String)"screen.runic_attunement.cost"), 268, 48, this.theme.text());
        context.method_51439(this.field_22793, (class_2561)class_2561.method_43471((String)"screen.runic_attunement.source_short"), 12, 89, this.theme.muted(), false);
        context.method_51439(this.field_22793, (class_2561)class_2561.method_43471((String)"screen.runic_attunement.target_short"), 53, 89, this.theme.muted(), false);
        class_1799 source = ((RunicInscriptionScreenHandler)this.field_2797).getSourceStack();
        class_1799 target = ((RunicInscriptionScreenHandler)this.field_2797).getTargetStack();
        if (source.method_7960() && target.method_7960()) {
            context.method_51440(this.field_22793, (class_5348)class_2561.method_43471((String)"screen.runic_attunement.insert_source_target"), 12, 107, 67, this.theme.muted());
        } else {
            if (!source.method_7960()) {
                context.method_51433(this.field_22793, this.field_22793.method_27523(source.method_7964().getString(), 67), 12, 107, this.theme.text(), false);
            }
            if (!target.method_7960()) {
                context.method_51433(this.field_22793, this.field_22793.method_27523(target.method_7964().getString(), 67), 12, 121, this.theme.text(), false);
            }
        }
        this.drawTransmutationCosts(context);
    }

    private void drawTransmutationList(class_332 context, int mouseX, int mouseY) {
        int index;
        List<class_2960> entries = TransmutationRules.sourceEnchantments(((RunicInscriptionScreenHandler)this.field_2797).getSourceStack());
        int maxOffset = Math.max(0, entries.size() - 4);
        this.transmutationScroll = Math.min(this.transmutationScroll, maxOffset);
        if (entries.isEmpty()) {
            context.method_51440(this.field_22793, (class_5348)class_2561.method_43471((String)"screen.runic_attunement.no_source_enchantments"), this.field_2776 + 92 + 4, this.field_2800 + 62 + 5, 112, this.theme.muted());
            return;
        }
        for (int visible = 0; visible < 4 && (index = this.transmutationScroll + visible) < entries.size(); ++visible) {
            int rowY;
            int rowX;
            class_2960 id = entries.get(index);
            class_1887 enchantment = class_7923.field_41176.method_17966(id).orElse(null);
            if (enchantment == null) continue;
            boolean hovered = RunicInscriptionScreen.isWithin(mouseX, mouseY, rowX = this.field_2776 + 92, rowY = this.field_2800 + 62 + visible * 18, 120, 17);
            context.method_25294(rowX, rowY, rowX + 120, rowY + 18 - 1, RunicInscriptionScreen.argb(hovered ? this.theme.panelHover() : this.theme.panel(), 215));
            int level = class_1890.method_8225((class_1887)enchantment, (class_1799)((RunicInscriptionScreenHandler)this.field_2797).getSourceStack());
            String name = enchantment.method_8179(level).getString();
            context.method_51433(this.field_22793, this.fitName(name, 82), rowX + 17, rowY + 2, this.transmutationRowColour(id, enchantment), false);
            if (enchantment.method_8195()) {
                boolean autoRetained = !TransmutationRules.curseCanTransfer(id, ((RunicInscriptionScreenHandler)this.field_2797).getSourceStack(), ((RunicInscriptionScreenHandler)this.field_2797).getTargetStack(), this.selectedTransfers);
                boolean checked = autoRetained || this.removeCurses.contains(id);
                this.drawCheckbox(context, rowX + 4, rowY + 4, checked, autoRetained ? this.theme.incompatible() : this.theme.text());
                String label = class_2561.method_43471((String)"screen.runic_attunement.remove_curse").getString();
                String suffix = autoRetained ? " \u2022 " + class_2561.method_43471((String)"screen.runic_attunement.required").getString() : "";
                context.method_51433(this.field_22793, this.field_22793.method_27523(label + suffix, 98), rowX + 17, rowY + 10, autoRetained ? this.theme.incompatible() : this.theme.muted(), false);
                continue;
            }
            boolean selected = this.selectedTransfers.contains(id);
            TransmutationRules.Status status = TransmutationRules.normalStatus(id, ((RunicInscriptionScreenHandler)this.field_2797).getSourceStack(), ((RunicInscriptionScreenHandler)this.field_2797).getTargetStack(), ((RunicInscriptionScreenHandler)this.field_2797).getDefinitions(), this.selectedTransfers, this.removeCurses);
            this.drawCheckbox(context, rowX + 4, rowY + 4, selected, status == TransmutationRules.Status.TRANSFERABLE ? this.theme.compatible() : this.theme.incompatible());
            class_2561 statusText = this.statusText(status, ((RunicInscriptionScreenHandler)this.field_2797).getDefinition(id));
            context.method_51433(this.field_22793, this.field_22793.method_27523(statusText.getString(), 98), rowX + 17, rowY + 10, status == TransmutationRules.Status.TRANSFERABLE ? this.theme.muted() : this.theme.incompatible(), false);
        }
    }

    private int transmutationRowColour(class_2960 id, class_1887 enchantment) {
        if (enchantment.method_8195()) {
            return this.theme.incompatible();
        }
        TransmutationRules.Status status = TransmutationRules.normalStatus(id, ((RunicInscriptionScreenHandler)this.field_2797).getSourceStack(), ((RunicInscriptionScreenHandler)this.field_2797).getTargetStack(), ((RunicInscriptionScreenHandler)this.field_2797).getDefinitions(), this.selectedTransfers, this.removeCurses);
        if (status != TransmutationRules.Status.TRANSFERABLE) {
            return this.theme.incompatible();
        }
        EnchantmentDefinition definition = ((RunicInscriptionScreenHandler)this.field_2797).getDefinition(id);
        return definition == null ? this.theme.text() : this.theme.tier(definition.tabletTier());
    }

    private class_2561 statusText(TransmutationRules.Status status, EnchantmentDefinition definition) {
        return switch (status) {
            default -> throw new IncompatibleClassChangeError();
            case TransmutationRules.Status.TRANSFERABLE -> {
                if (definition == null) {
                    yield class_2561.method_43471((String)"screen.runic_attunement.compatible");
                }
                yield class_2561.method_43469((String)"screen.runic_attunement.tier_compatible", (Object[])new Object[]{RunicPotency.name(definition.tabletTier())});
            }
            case TransmutationRules.Status.UNDEFINED -> class_2561.method_43471((String)"screen.runic_attunement.undefined");
            case TransmutationRules.Status.LEVEL_UNSUPPORTED -> class_2561.method_43471((String)"screen.runic_attunement.level_unsupported");
            case TransmutationRules.Status.TARGET_EQUAL_OR_HIGHER -> class_2561.method_43471((String)"screen.runic_attunement.target_higher");
            case TransmutationRules.Status.CONFLICT -> class_2561.method_43471((String)"screen.runic_attunement.conflict");
            case TransmutationRules.Status.INCOMPATIBLE -> class_2561.method_43471((String)"screen.runic_attunement.incompatible");
            case TransmutationRules.Status.CURSE -> class_2561.method_43471((String)"screen.runic_attunement.curse");
        };
    }

    private void drawCheckbox(class_332 context, int boxX, int boxY, boolean checked, int colour) {
        context.method_25294(boxX, boxY, boxX + 9, boxY + 9, -15460581);
        context.method_49601(boxX, boxY, 9, 9, RunicInscriptionScreen.argb(colour));
        if (checked) {
            int c = RunicInscriptionScreen.argb(colour);
            context.method_25294(boxX + 2, boxY + 4, boxX + 4, boxY + 6, c);
            context.method_25294(boxX + 3, boxY + 5, boxX + 5, boxY + 7, c);
            context.method_25294(boxX + 4, boxY + 3, boxX + 6, boxY + 6, c);
            context.method_25294(boxX + 5, boxY + 2, boxX + 7, boxY + 4, c);
        }
    }

    private void drawSmallTransmutationButtons(class_332 context, int mouseX, int mouseY) {
        this.drawSmallButton(context, 94, (class_2561)class_2561.method_43471((String)"screen.runic_attunement.select_all"), RunicInscriptionScreen.isWithin(mouseX, mouseY, this.field_2776 + 94, this.field_2800 + 136, 58, 9));
        this.drawSmallButton(context, 156, (class_2561)class_2561.method_43471((String)"screen.runic_attunement.clear"), RunicInscriptionScreen.isWithin(mouseX, mouseY, this.field_2776 + 156, this.field_2800 + 136, 58, 9));
    }

    private void drawSmallButton(class_332 context, int localX, class_2561 label, boolean hovered) {
        int fill = hovered ? this.theme.buttonHover() : this.theme.button();
        context.method_25294(this.field_2776 + localX, this.field_2800 + 136, this.field_2776 + localX + 58, this.field_2800 + 136 + 9, RunicInscriptionScreen.argb(fill));
        context.method_49601(this.field_2776 + localX, this.field_2800 + 136, 58, 9, RunicInscriptionScreen.argb(hovered ? this.theme.accentBright() : this.theme.accent()));
        context.method_25300(this.field_22793, this.field_22793.method_27523(label.getString(), 54), this.field_2776 + localX + 29, this.field_2800 + 136 + 1, this.theme.text());
    }

    private void drawTransmuteButton(class_332 context, int mouseX, int mouseY) {
        Eligibility eligibility = this.transmutationEligibility();
        boolean hovered = RunicInscriptionScreen.isWithin(mouseX, mouseY, this.field_2776 + 104, this.field_2800 + 147, 100, 23);
        this.drawActionButton(context, 147, eligibility, hovered, (class_2561)class_2561.method_43471((String)"screen.runic_attunement.transmute"), this.currentTransmutationExperienceCost());
    }

    private void drawTransmutationCosts(class_332 context) {
        int retained;
        List<IngredientCost> costs = this.currentTransmutationCosts();
        if (((RunicInscriptionScreenHandler)this.field_2797).getSourceStack().method_7960() || ((RunicInscriptionScreenHandler)this.field_2797).getTargetStack().method_7960()) {
            context.method_51440(this.field_22793, (class_5348)class_2561.method_43471((String)"screen.runic_attunement.insert_source_target"), 228, 66, 78, this.theme.muted());
            return;
        }
        if (costs.isEmpty()) {
            context.method_51440(this.field_22793, (class_5348)class_2561.method_43471((String)"screen.runic_attunement.select_transfer"), 228, 66, 78, this.theme.muted());
            return;
        }
        int capacity = 5;
        int shown = Math.min(costs.size(), capacity);
        for (int i = 0; i < shown; ++i) {
            IngredientCost cost = costs.get(i);
            class_1792 item = class_7923.field_41178.method_17966(cost.itemId()).orElse(null);
            if (item == null) continue;
            int rowY = 67 + i * 17;
            class_1799 icon = new class_1799((class_1935)item);
            int have = this.field_22787 == null || this.field_22787.field_1724 == null ? 0 : RunicInscriptionScreen.countItem(this.field_22787.field_1724.method_31548(), item);
            boolean enough = this.field_22787 != null && this.field_22787.field_1724 != null && (this.field_22787.field_1724.method_31549().field_7477 || have >= cost.count());
            context.method_51427(icon, 228, rowY - 2);
            String quantity = have + "/" + cost.count();
            context.method_51433(this.field_22793, this.field_22793.method_27523(quantity, 58), 247, rowY + 3, enough ? this.theme.compatible() : this.theme.incompatible(), false);
        }
        if (costs.size() > capacity) {
            context.method_51439(this.field_22793, (class_2561)class_2561.method_43469((String)"screen.runic_attunement.more_costs", (Object[])new Object[]{costs.size() - capacity}), 228, 156, this.theme.muted(), false);
        }
        if ((retained = TransmutationRules.retainedCurses(((RunicInscriptionScreenHandler)this.field_2797).getSourceStack(), ((RunicInscriptionScreenHandler)this.field_2797).getTargetStack(), this.selectedTransfers, this.removeCurses).size()) > 0) {
            context.method_51433(this.field_22793, this.field_22793.method_27523(class_2561.method_43469((String)"screen.runic_attunement.curses_retained", (Object[])new Object[]{retained}).getString(), 78), 228, 156, this.theme.incompatible(), false);
        }
    }

    private void drawLocalScrollbar(class_332 context, int localX, int localY, int width, int height, int totalEntries, int visibleEntries, int offset, boolean dragging) {
        context.method_25294(localX, localY, localX + width, localY + height, -15394788);
        if (totalEntries <= visibleEntries || totalEntries <= 0) {
            context.method_25294(localX + 1, localY + 1, localX + width - 1, localY + height - 1, RunicInscriptionScreen.argb(this.theme.border(), 120));
            return;
        }
        int maxOffset = Math.max(1, totalEntries - visibleEntries);
        int thumbHeight = Math.max(8, height * visibleEntries / totalEntries);
        int travel = Math.max(1, height - thumbHeight);
        int thumbY = localY + Math.round((float)offset / (float)maxOffset * (float)travel);
        context.method_25294(localX, localY, localX + width, localY + height, -15657959);
        context.method_25294(localX, thumbY, localX + width, thumbY + thumbHeight, RunicInscriptionScreen.argb(dragging ? this.theme.accentBright() : this.theme.accent()));
    }

    private void drawScrollbar(class_332 context, int localX, int localY, int width, int height, int totalEntries, int visibleEntries, int offset, boolean dragging) {
        context.method_25294(this.field_2776 + localX, this.field_2800 + localY, this.field_2776 + localX + width, this.field_2800 + localY + height, -15394788);
        if (totalEntries <= visibleEntries || totalEntries <= 0) {
            context.method_25294(this.field_2776 + localX + 1, this.field_2800 + localY + 1, this.field_2776 + localX + width - 1, this.field_2800 + localY + height - 1, RunicInscriptionScreen.argb(this.theme.border(), 120));
            return;
        }
        int thumb = Math.max(9, height * visibleEntries / totalEntries);
        int maxOffset = totalEntries - visibleEntries;
        int travel = height - thumb;
        int thumbY = localY + (maxOffset == 0 ? 0 : Math.round((float)offset / (float)maxOffset * (float)travel));
        int colour = dragging ? this.theme.accentBright() : this.theme.accent();
        context.method_25294(this.field_2776 + localX, this.field_2800 + thumbY, this.field_2776 + localX + width, this.field_2800 + thumbY + thumb, RunicInscriptionScreen.argb(colour));
    }

    private /* synthetic */ boolean runic$originalClick(double mouseX, double mouseY, int button) {
        if (button == 0) {
            if (RunicInscriptionScreen.isWithin(mouseX, mouseY, this.field_2776 + 79, this.field_2800 + 21, 77, 15)) {
                this.clickServerButton(10);
                return true;
            }
            if (RunicInscriptionScreen.isWithin(mouseX, mouseY, this.field_2776 + 158, this.field_2800 + 21, 84, 15)) {
                if (((RunicInscriptionScreenHandler)this.field_2797).getTransmutationSettings().enabled()) {
                    this.clickServerButton(11);
                }
                return true;
            }
            if (((RunicInscriptionScreenHandler)this.field_2797).isTransmutationMode()) {
                List<class_2960> entries = TransmutationRules.sourceEnchantments(((RunicInscriptionScreenHandler)this.field_2797).getSourceStack());
                if (entries.size() > 4 && RunicInscriptionScreen.isWithin(mouseX, mouseY, this.field_2776 + 214, this.field_2800 + 62, 4, 72)) {
                    this.draggingTransmutationScrollbar = true;
                    this.transmutationScroll = RunicInscriptionScreen.scrollFromMouse(mouseY, this.field_2800 + 62, 72, entries.size(), 4);
                    return true;
                }
                if (this.handleTransmutationClick(mouseX, mouseY)) {
                    return true;
                }
            } else {
                int row;
                int index;
                if (this.handleTierFilterClick(mouseX, mouseY)) {
                    return true;
                }
                List<class_2960> filtered = this.filteredEnchantments();
                if (filtered.size() > 5 && RunicInscriptionScreen.isWithin(mouseX, mouseY, this.field_2776 + 305, this.field_2800 + 94, 4, 65)) {
                    this.draggingAttunementScrollbar = true;
                    this.scrollOffset = RunicInscriptionScreen.scrollFromMouse(mouseY, this.field_2800 + 94, 65, filtered.size(), 5);
                    return true;
                }
                List<IngredientCost> costs = this.currentAttunementCosts();
                int costRows = (costs.size() + 2 - 1) / 2;
                if (costRows > 2 && RunicInscriptionScreen.isWithin(mouseX, mouseY, this.field_2776 + 216, this.field_2800 + 106, 3, 36)) {
                    this.draggingAttunementCostScrollbar = true;
                    this.attunementCostScrollRow = RunicInscriptionScreen.scrollFromMouse(mouseY, this.field_2800 + 106, 36, costRows, 2);
                    return true;
                }
                if (RunicInscriptionScreen.isWithin(mouseX, mouseY, this.field_2776 + 228, this.field_2800 + 94, 75, 65) && (index = this.scrollOffset + (row = (int)((mouseY - (double)(this.field_2800 + 94)) / 13.0))) >= 0 && index < filtered.size()) {
                    this.selectedEnchantment = filtered.get(index);
                    this.attunementCostScrollRow = 0;
                    this.attunementCostFingerprint = "";
                    return true;
                }
                if (RunicInscriptionScreen.isWithin(mouseX, mouseY, this.field_2776 + 104, this.field_2800 + 147, 100, 23)) {
                    Eligibility eligibility = this.eligibility();
                    if (eligibility.allowed() && this.selectedEnchantment != null && (index = ((RunicInscriptionScreenHandler)this.field_2797).indexOf(this.selectedEnchantment)) >= 0) {
                        this.clickServerButton(1000 + index);
                    }
                    return true;
                }
            }
        }
        return super.method_25402(mouseX, mouseY, button);
    }

    private boolean handleTierFilterClick(double mouseX, double mouseY) {
        int filter = -1;
        if (RunicInscriptionScreen.isWithin(mouseX, mouseY, this.field_2776 + 228, this.field_2800 + 80, 24, 12)) {
            filter = 0;
        } else if (RunicInscriptionScreen.isWithin(mouseX, mouseY, this.field_2776 + 254, this.field_2800 + 80, 15, 12)) {
            filter = 1;
        } else if (RunicInscriptionScreen.isWithin(mouseX, mouseY, this.field_2776 + 271, this.field_2800 + 80, 15, 12)) {
            filter = 2;
        } else if (RunicInscriptionScreen.isWithin(mouseX, mouseY, this.field_2776 + 288, this.field_2800 + 80, 15, 12)) {
            filter = 3;
        }
        if (filter < 0) {
            return false;
        }
        this.tierFilter = filter;
        this.scrollOffset = 0;
        this.keepSelectionValid();
        return true;
    }

    private boolean handleTransmutationClick(double mouseX, double mouseY) {
        int row;
        int index;
        List<class_2960> entries = TransmutationRules.sourceEnchantments(((RunicInscriptionScreenHandler)this.field_2797).getSourceStack());
        if (RunicInscriptionScreen.isWithin(mouseX, mouseY, this.field_2776 + 92, this.field_2800 + 62, 120, 72) && (index = this.transmutationScroll + (row = (int)((mouseY - (double)(this.field_2800 + 62)) / 18.0))) >= 0 && index < entries.size()) {
            class_2960 id = entries.get(index);
            class_1887 enchantment = class_7923.field_41176.method_17966(id).orElse(null);
            if (enchantment == null) {
                return true;
            }
            if (enchantment.method_8195()) {
                boolean autoRetained;
                boolean bl = autoRetained = !TransmutationRules.curseCanTransfer(id, ((RunicInscriptionScreenHandler)this.field_2797).getSourceStack(), ((RunicInscriptionScreenHandler)this.field_2797).getTargetStack(), this.selectedTransfers);
                if (!autoRetained) {
                    if (!this.removeCurses.remove(id)) {
                        this.removeCurses.add(id);
                    }
                    this.clickServerButton(3000 + index);
                }
                return true;
            }
            if (this.selectedTransfers.contains(id)) {
                this.selectedTransfers.remove(id);
                this.clickServerButton(2000 + index);
            } else if (TransmutationRules.normalStatus(id, ((RunicInscriptionScreenHandler)this.field_2797).getSourceStack(), ((RunicInscriptionScreenHandler)this.field_2797).getTargetStack(), ((RunicInscriptionScreenHandler)this.field_2797).getDefinitions(), this.selectedTransfers, this.removeCurses) == TransmutationRules.Status.TRANSFERABLE) {
                this.selectedTransfers.add(id);
                this.clickServerButton(2000 + index);
            }
            return true;
        }
        if (RunicInscriptionScreen.isWithin(mouseX, mouseY, this.field_2776 + 94, this.field_2800 + 136, 58, 9)) {
            this.selectedTransfers.clear();
            for (class_2960 id : entries) {
                class_1887 enchantment = class_7923.field_41176.method_17966(id).orElse(null);
                if (enchantment == null || enchantment.method_8195() || TransmutationRules.normalStatus(id, ((RunicInscriptionScreenHandler)this.field_2797).getSourceStack(), ((RunicInscriptionScreenHandler)this.field_2797).getTargetStack(), ((RunicInscriptionScreenHandler)this.field_2797).getDefinitions(), this.selectedTransfers, this.removeCurses) != TransmutationRules.Status.TRANSFERABLE) continue;
                this.selectedTransfers.add(id);
            }
            this.clickServerButton(4000);
            return true;
        }
        if (RunicInscriptionScreen.isWithin(mouseX, mouseY, this.field_2776 + 156, this.field_2800 + 136, 58, 9)) {
            this.selectedTransfers.clear();
            this.clickServerButton(4001);
            return true;
        }
        if (RunicInscriptionScreen.isWithin(mouseX, mouseY, this.field_2776 + 104, this.field_2800 + 147, 100, 23)) {
            if (this.transmutationEligibility().allowed()) {
                this.clickServerButton(5000);
            }
            return true;
        }
        return false;
    }

    /*
     * WARNING - void declaration
     */
    public boolean method_25403(double d, double d2, int n, double d3, double d4) {
        void deltaY;
        void deltaX;
        void mouseX;
        void mouseY;
        void button;
        if (RunicGrimoireUi.active((Object)this)) {
            return super.method_25403(d, d2, n, d3, d4);
        }
        if (button == false) {
            if (this.draggingAttunementScrollbar && !((RunicInscriptionScreenHandler)this.field_2797).isTransmutationMode()) {
                List<class_2960> filtered = this.filteredEnchantments();
                this.scrollOffset = RunicInscriptionScreen.scrollFromMouse((double)mouseY, this.field_2800 + 94, 65, filtered.size(), 5);
                return true;
            }
            if (this.draggingAttunementCostScrollbar && !((RunicInscriptionScreenHandler)this.field_2797).isTransmutationMode()) {
                int rows = (this.currentAttunementCosts().size() + 2 - 1) / 2;
                this.attunementCostScrollRow = RunicInscriptionScreen.scrollFromMouse((double)mouseY, this.field_2800 + 106, 36, rows, 2);
                return true;
            }
            if (this.draggingTransmutationScrollbar && ((RunicInscriptionScreenHandler)this.field_2797).isTransmutationMode()) {
                List<class_2960> entries = TransmutationRules.sourceEnchantments(((RunicInscriptionScreenHandler)this.field_2797).getSourceStack());
                this.transmutationScroll = RunicInscriptionScreen.scrollFromMouse((double)mouseY, this.field_2800 + 62, 72, entries.size(), 4);
                return true;
            }
        }
        return super.method_25403((double)mouseX, (double)mouseY, (int)button, (double)deltaX, (double)deltaY);
    }

    public boolean method_25406(double mouseX, double mouseY, int button) {
        if (button == 0) {
            this.draggingAttunementScrollbar = false;
            this.draggingTransmutationScrollbar = false;
            this.draggingAttunementCostScrollbar = false;
        }
        return super.method_25406(mouseX, mouseY, button);
    }

    private static int scrollFromMouse(double mouseY, int trackY, int trackHeight, int total, int visible) {
        int maxOffset = Math.max(0, total - visible);
        if (maxOffset == 0) {
            return 0;
        }
        int thumb = Math.max(9, trackHeight * visible / total);
        int travel = Math.max(1, trackHeight - thumb);
        double relative = mouseY - (double)trackY - (double)thumb / 2.0;
        double fraction = Math.max(0.0, Math.min(1.0, relative / (double)travel));
        return (int)Math.round(fraction * (double)maxOffset);
    }

    private void clickServerButton(int id) {
        if (this.field_22787 != null && this.field_22787.field_1761 != null) {
            this.field_22787.field_1761.method_2900(((RunicInscriptionScreenHandler)this.field_2797).field_7763, id);
        }
    }

    /*
     * WARNING - void declaration
     */
    public boolean method_25401(double d, double d2, double d3) {
        void amount;
        void mouseY;
        void mouseX;
        if (RunicGrimoireUi.scroll((Object)this, d, d2, d3)) {
            return true;
        }
        if (((RunicInscriptionScreenHandler)this.field_2797).isTransmutationMode()) {
            if (RunicInscriptionScreen.isWithin((double)mouseX, (double)mouseY, this.field_2776 + 92, this.field_2800 + 62, 128, 72)) {
                List<class_2960> entries = TransmutationRules.sourceEnchantments(((RunicInscriptionScreenHandler)this.field_2797).getSourceStack());
                int maxOffset = Math.max(0, entries.size() - 4);
                if (amount < 0.0) {
                    this.transmutationScroll = Math.min(maxOffset, this.transmutationScroll + 1);
                } else if (amount > 0.0) {
                    this.transmutationScroll = Math.max(0, this.transmutationScroll - 1);
                }
                return true;
            }
        } else {
            int maxOffset;
            if (RunicInscriptionScreen.isWithin((double)mouseX, (double)mouseY, this.field_2776 + 95, this.field_2800 + 106, 124, 36)) {
                int rows = (this.currentAttunementCosts().size() + 2 - 1) / 2;
                maxOffset = Math.max(0, rows - 2);
                if (amount < 0.0) {
                    this.attunementCostScrollRow = Math.min(maxOffset, this.attunementCostScrollRow + 1);
                } else if (amount > 0.0) {
                    this.attunementCostScrollRow = Math.max(0, this.attunementCostScrollRow - 1);
                }
                if (maxOffset > 0) {
                    return true;
                }
            }
            if (RunicInscriptionScreen.isWithin((double)mouseX, (double)mouseY, this.field_2776 + 228, this.field_2800 + 94, 83, 65)) {
                List<class_2960> filtered = this.filteredEnchantments();
                maxOffset = Math.max(0, filtered.size() - 5);
                if (amount < 0.0) {
                    this.scrollOffset = Math.min(maxOffset, this.scrollOffset + 1);
                } else if (amount > 0.0) {
                    this.scrollOffset = Math.max(0, this.scrollOffset - 1);
                }
                return true;
            }
        }
        return super.method_25401((double)mouseX, (double)mouseY, (double)amount);
    }

    private List<IngredientCost> currentAttunementCosts() {
        if (this.selectedEnchantment == null) {
            return List.of();
        }
        class_1887 enchantment = class_7923.field_41176.method_17966(this.selectedEnchantment).orElse(null);
        EnchantmentDefinition definition = ((RunicInscriptionScreenHandler)this.field_2797).getDefinition(this.selectedEnchantment);
        class_1799 target = ((RunicInscriptionScreenHandler)this.field_2797).getEquipmentStack();
        if (enchantment == null || definition == null || target.method_7960()) {
            return List.of();
        }
        int next = class_1890.method_8225((class_1887)enchantment, (class_1799)target) + 1;
        if (next > enchantment.method_8183() || definition.level(next).isEmpty()) {
            return List.of();
        }
        return ((RunicInscriptionScreenHandler)this.field_2797).getClientCosts(definition, next);
    }

    private void syncAttunementCostState() {
        String fingerprint = String.valueOf(this.selectedEnchantment) + "||" + RunicInscriptionScreen.stackFingerprint(((RunicInscriptionScreenHandler)this.field_2797).getEquipmentStack());
        if (!fingerprint.equals(this.attunementCostFingerprint)) {
            this.attunementCostFingerprint = fingerprint;
            this.attunementCostScrollRow = 0;
        }
    }

    private void syncTransmutationState() {
        String fingerprint = RunicInscriptionScreen.stackFingerprint(((RunicInscriptionScreenHandler)this.field_2797).getSourceStack()) + "||" + RunicInscriptionScreen.stackFingerprint(((RunicInscriptionScreenHandler)this.field_2797).getTargetStack());
        if (!fingerprint.equals(this.transmutationFingerprint)) {
            this.transmutationFingerprint = fingerprint;
            this.selectedTransfers.clear();
            this.removeCurses.clear();
            this.transmutationScroll = 0;
        }
    }

    private static String stackFingerprint(class_1799 stack) {
        if (stack.method_7960()) {
            return "empty";
        }
        return String.valueOf(class_7923.field_41178.method_10221((Object)stack.method_7909())) + "|" + stack.method_7947() + "|" + String.valueOf(stack.method_7969());
    }

    private List<IngredientCost> currentTransmutationCosts() {
        return TransmutationRules.calculateCosts(((RunicInscriptionScreenHandler)this.field_2797).getSourceStack(), ((RunicInscriptionScreenHandler)this.field_2797).getTargetStack(), ((RunicInscriptionScreenHandler)this.field_2797).getDefinitions(), this.selectedTransfers, this.removeCurses, ((RunicInscriptionScreenHandler)this.field_2797).getTransmutationCosts(), ((RunicInscriptionScreenHandler)this.field_2797).getTransmutationSettings());
    }

    private int currentAttunementExperienceCost() {
        if (!((RunicInscriptionScreenHandler)this.field_2797).getExperienceSettings().enabled() || this.selectedEnchantment == null) {
            return 0;
        }
        class_1887 enchantment = class_7923.field_41176.method_17966(this.selectedEnchantment).orElse(null);
        EnchantmentDefinition definition = ((RunicInscriptionScreenHandler)this.field_2797).getDefinition(this.selectedEnchantment);
        class_1799 target = ((RunicInscriptionScreenHandler)this.field_2797).getEquipmentStack();
        if (enchantment == null || definition == null || target.method_7960()) {
            return 0;
        }
        int next = class_1890.method_8225((class_1887)enchantment, (class_1799)target) + 1;
        return ((RunicInscriptionScreenHandler)this.field_2797).getAttunementExperienceCost(definition, next);
    }

    private int currentTransmutationExperienceCost() {
        return ((RunicInscriptionScreenHandler)this.field_2797).getTransmutationExperienceCost(this.selectedTransfers, this.removeCurses);
    }

    private Eligibility transmutationEligibility() {
        if (!((RunicInscriptionScreenHandler)this.field_2797).getTransmutationSettings().enabled() || !((RunicInscriptionScreenHandler)this.field_2797).getTransmutationCosts().available()) {
            return new Eligibility(false, (class_2561)class_2561.method_43471((String)"screen.runic_attunement.transmutation_disabled"));
        }
        if (((RunicInscriptionScreenHandler)this.field_2797).getSourceStack().method_7960() || ((RunicInscriptionScreenHandler)this.field_2797).getTargetStack().method_7960()) {
            return new Eligibility(false, (class_2561)class_2561.method_43471((String)"screen.runic_attunement.insert_items"));
        }
        if (!TransmutationRules.hasValidNormalTransfer(((RunicInscriptionScreenHandler)this.field_2797).getSourceStack(), ((RunicInscriptionScreenHandler)this.field_2797).getTargetStack(), ((RunicInscriptionScreenHandler)this.field_2797).getDefinitions(), this.selectedTransfers, this.removeCurses)) {
            return new Eligibility(false, (class_2561)class_2561.method_43471((String)"screen.runic_attunement.select_transfer"));
        }
        if (this.field_22787 == null || this.field_22787.field_1724 == null) {
            return new Eligibility(false, (class_2561)class_2561.method_43473());
        }
        if (!this.field_22787.field_1724.method_31549().field_7477) {
            for (IngredientCost cost : this.currentTransmutationCosts()) {
                class_1792 item = class_7923.field_41178.method_17966(cost.itemId()).orElse(null);
                if (item != null && RunicInscriptionScreen.countItem(this.field_22787.field_1724.method_31548(), item) >= cost.count()) continue;
                return new Eligibility(false, (class_2561)class_2561.method_43471((String)"screen.runic_attunement.missing_materials"));
            }
            if (((RunicInscriptionScreenHandler)this.field_2797).getExperienceSettings().enabled() && RunicExperience.availablePoints((class_1657)this.field_22787.field_1724) < this.currentTransmutationExperienceCost()) {
                return new Eligibility(false, (class_2561)class_2561.method_43471((String)"screen.runic_attunement.missing_experience"));
            }
        }
        return new Eligibility(true, (class_2561)class_2561.method_43473());
    }

    private List<class_2960> filteredEnchantments() {
        String query = this.searchField == null ? "" : this.searchField.method_1882().trim().toLowerCase(Locale.ROOT);
        class_1799 target = ((RunicInscriptionScreenHandler)this.field_2797).getEquipmentStack();
        ArrayList<class_2960> result = new ArrayList<class_2960>();
        for (class_2960 id2 : ((RunicInscriptionScreenHandler)this.field_2797).getAttunedEnchantments()) {
            class_1887 enchantment = class_7923.field_41176.method_17966(id2).orElse(null);
            EnchantmentDefinition definition = ((RunicInscriptionScreenHandler)this.field_2797).getDefinition(id2);
            if (enchantment == null || definition == null || this.tierFilter > 0 && definition.tabletTier() != this.tierFilter || !target.method_7960() && !RunicCompatibility.accepts(enchantment, target)) continue;
            String name = class_2561.method_43471((String)enchantment.method_8184()).getString().toLowerCase(Locale.ROOT);
            if (!query.isEmpty() && !name.contains(query) && !id2.toString().contains(query)) continue;
            result.add(id2);
        }
        result.sort(Comparator.comparing(id -> {
            class_1887 enchantment = (class_1887)class_7923.field_41176.method_10223(id);
            return class_2561.method_43471((String)enchantment.method_8184()).getString().toLowerCase(Locale.ROOT);
        }));
        return result;
    }

    private boolean isAtMaximum(class_2960 id, class_1887 enchantment, class_1799 target) {
        if (target.method_7960()) {
            return false;
        }
        int current = class_1890.method_8225((class_1887)enchantment, (class_1799)target);
        if (current <= 0) {
            return false;
        }
        EnchantmentDefinition definition = ((RunicInscriptionScreenHandler)this.field_2797).getDefinition(id);
        return current >= enchantment.method_8183() || definition != null && definition.level(current + 1).isEmpty();
    }

    private void keepSelectionValid() {
        List<class_2960> filtered = this.filteredEnchantments();
        if (this.selectedEnchantment == null || !filtered.contains(this.selectedEnchantment)) {
            class_2960 next;
            class_2960 class_29602 = next = filtered.isEmpty() ? null : filtered.get(0);
            if (!Objects.equals(this.selectedEnchantment, next)) {
                this.selectedEnchantment = next;
                this.attunementCostScrollRow = 0;
                this.attunementCostFingerprint = "";
            }
        }
    }

    private Eligibility eligibility() {
        if (this.selectedEnchantment == null) {
            return new Eligibility(false, (class_2561)class_2561.method_43471((String)"screen.runic_attunement.no_selection"));
        }
        class_1799 target = ((RunicInscriptionScreenHandler)this.field_2797).getEquipmentStack();
        if (target.method_7960()) {
            return new Eligibility(false, (class_2561)class_2561.method_43471((String)"screen.runic_attunement.insert_item_short"));
        }
        class_1887 enchantment = class_7923.field_41176.method_17966(this.selectedEnchantment).orElse(null);
        EnchantmentDefinition definition = ((RunicInscriptionScreenHandler)this.field_2797).getDefinition(this.selectedEnchantment);
        if (enchantment == null || definition == null || !RunicCompatibility.accepts(enchantment, target)) {
            return new Eligibility(false, (class_2561)class_2561.method_43471((String)"screen.runic_attunement.incompatible"));
        }
        int current = class_1890.method_8225((class_1887)enchantment, (class_1799)target);
        int targetLevel = current + 1;
        if (targetLevel > enchantment.method_8183() || definition.level(targetLevel).isEmpty()) {
            return new Eligibility(false, (class_2561)class_2561.method_43471((String)"screen.runic_attunement.maximum"));
        }
        if (RunicInscriptionScreen.hasConflict(enchantment, target)) {
            return new Eligibility(false, (class_2561)class_2561.method_43471((String)"screen.runic_attunement.conflict"));
        }
        if (this.field_22787 == null || this.field_22787.field_1724 == null) {
            return new Eligibility(false, (class_2561)class_2561.method_43473());
        }
        if (!this.field_22787.field_1724.method_31549().field_7477) {
            for (IngredientCost cost : ((RunicInscriptionScreenHandler)this.field_2797).getClientCosts(definition, targetLevel)) {
                class_1792 item = class_7923.field_41178.method_17966(cost.itemId()).orElse(null);
                if (item != null && RunicInscriptionScreen.countItem(this.field_22787.field_1724.method_31548(), item) >= cost.count()) continue;
                return new Eligibility(false, (class_2561)class_2561.method_43471((String)"screen.runic_attunement.missing_materials"));
            }
            int experienceCost = ((RunicInscriptionScreenHandler)this.field_2797).getAttunementExperienceCost(definition, targetLevel);
            if (((RunicInscriptionScreenHandler)this.field_2797).getExperienceSettings().enabled() && RunicExperience.availablePoints((class_1657)this.field_22787.field_1724) < experienceCost) {
                return new Eligibility(false, (class_2561)class_2561.method_43471((String)"screen.runic_attunement.missing_experience"));
            }
        }
        return new Eligibility(true, (class_2561)class_2561.method_43473());
    }

    private static boolean hasConflict(class_1887 enchantment, class_1799 target) {
        if (target.method_7960()) {
            return false;
        }
        for (class_1887 other : class_1890.method_8222((class_1799)target).keySet()) {
            if (other == enchantment || enchantment.method_8188(other)) continue;
            return true;
        }
        return false;
    }

    private static int countItem(class_1661 inventory, class_1792 item) {
        int count = 0;
        for (int slot = 0; slot < 36; ++slot) {
            class_1799 stack = inventory.method_5438(slot);
            if (!stack.method_31574(item)) continue;
            count += stack.method_7947();
        }
        return count;
    }

    private static boolean isWithin(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= (double)x && mouseX < (double)(x + width) && mouseY >= (double)y && mouseY < (double)(y + height);
    }

    private static int argb(int rgb) {
        return 0xFF000000 | rgb & 0xFFFFFF;
    }

    private static int argb(int rgb, int alpha) {
        return (alpha & 0xFF) << 24 | rgb & 0xFFFFFF;
    }

    private static String roman(int level) {
        return switch (level) {
            case 0 -> "\u2014";
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            case 6 -> "VI";
            case 7 -> "VII";
            case 8 -> "VIII";
            case 9 -> "IX";
            case 10 -> "X";
            default -> Integer.toString(level);
        };
    }

    public boolean method_25402(double d, double d2, int n) {
        int n2 = RunicGrimoireUi.click((Object)this, d, d2, n);
        if (n2 != -1000) {
            if (n2 != -10) {
                if (n2 >= 0) {
                    this.clickServerButton(n2);
                }
                return true;
            }
            return super.method_25402(d, d2, n);
        }
        int n3 = RunicUnravelClient.buttonId((Object)this, d, d2, n);
        if (n3 != -1) {
            if (n3 >= 0) {
                this.clickServerButton(n3);
                return true;
            }
            return true;
        }
        if (EnchantedTooltipsBridge.clickInfo((Object)this, d, d2, n)) {
            return true;
        }
        return this.runic$originalClick(d, d2, n);
    }

    public void method_25419() {
        RunicGrimoireUi.close((Object)this);
        super.method_25419();
    }

    protected boolean method_2381(double d, double d2, int n, int n2, int n3) {
        if (RunicGrimoireUi.insideGrimoire((Object)this, d, d2)) {
            return false;
        }
        return super.method_2381(d, d2, n, n2, n3);
    }

    private record Eligibility(boolean allowed, class_2561 reason) {
    }
}

