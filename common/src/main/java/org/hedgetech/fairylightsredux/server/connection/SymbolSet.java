package org.hedgetech.fairylightsredux.server.connection;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

public final class SymbolSet {
    private static final float SCALE = 1.0F / 16.0F;
    private final int height;
    private final String description;
    private final Int2IntMap widths = new Int2IntOpenHashMap();

    public SymbolSet(final int height, final String description) {
        this.height = height;
        this.description = description;
    }

    public SymbolSet add(final String codepoints, final int width) {
        codepoints.chars().forEach(codepoint -> this.widths.put(codepoint, width));
        return this;
    }

    public float getHeight() {
        return this.height * SCALE;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean contains(final int  chr) {
        return this.widths.containsKey(chr);
    }

    public float getWidth(final char chr) {
        return this.widths.getOrDefault(chr, 0) * SCALE;
    }
}
