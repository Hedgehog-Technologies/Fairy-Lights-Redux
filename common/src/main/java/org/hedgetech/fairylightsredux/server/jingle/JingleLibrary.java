package org.hedgetech.fairylightsredux.server.jingle;

import net.minecraft.util.RandomSource;

import java.util.List;

public final class JingleLibrary {
    public static final JingleLibrary EMPTY = new JingleLibrary(List.of());

    private final List<Jingle> jingles;

    public JingleLibrary(final List<Jingle> jingles) {
        this.jingles = List.copyOf(jingles);
    }

    public Jingle getRandom(final RandomSource rng, final int lightCount) {
        final int range = Math.min(lightCount, Jingle.NOTE_COUNT);
        int total = 0;

        for (final Jingle jingle : this.jingles) {
            if (jingle.getRange() <= range) {
                total += jingle.getRange();
            }
        }

        float choice = rng.nextFloat() * total;

        for (final Jingle jingle : this.jingles) {
            if (jingle.getRange() <= range) {
                choice -= jingle.getRange();

                if (choice <= 0.0F) {
                    return jingle;
                }
            }
        }

        return null;
    }
}
