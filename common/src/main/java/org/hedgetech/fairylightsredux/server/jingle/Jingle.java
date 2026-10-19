package org.hedgetech.fairylightsredux.server.jingle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

import java.util.List;

public final class Jingle {
    private static final int MAX_DURATION = 127;

    public static final int NOTE_COUNT = 25;

    public static final Codec<Jingle> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("title").forGetter(Jingle::getTitle),
            Codec.STRING.fieldOf("artist").forGetter(Jingle::getArtist),
            PlayTick.CODEC.listOf().fieldOf("ticks").forGetter(Jingle::getPlayTicks)
    ).apply(instance, Jingle::new));

    public static final StreamCodec<ByteBuf, Jingle> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, Jingle::getTitle,
            ByteBufCodecs.STRING_UTF8, Jingle::getArtist,
            PlayTick.STREAM_CODEC.apply(ByteBufCodecs.list()),Jingle::getPlayTicks,
            Jingle::new
    );

    private final String title;
    private final String artist;
    private final List<PlayTick> ticks;
    private final int length;
    private final int lowestNote;
    private final int range;

    public Jingle(final String title, final String artist, final List<PlayTick> ticks) {
        this.title = title;
        this.artist = artist;
        this.ticks = List.copyOf(ticks);

        int length = 0;
        int min = NOTE_COUNT - 1;
        int max = 0;

        for (final PlayTick tick : this.ticks) {
            length += tick.duration();

            for (final int note : tick.notes()) {
                min = Math.min(min, note);
                max = Math.max(max, note);
            }
        }

        this.length = length;
        this.lowestNote = max < min ? 0 : min;
        this.range = max < min ? 1 : max - min + 1;
    }

    public String getTitle() {
        return this.title;
    }

    public String getArtist() {
        return this.artist;
    }

    public List<PlayTick> getPlayTicks() {
        return this.ticks;
    }

    public int getLength() {
        return this.length;
    }

    public int getLowestNote() {
        return this.lowestNote;
    }

    public int getRange() {
        return this.range;
    }

    public record PlayTick(int duration, List<Integer> notes) {
        // Durations and notes are bounded so a tick packs into one int on the network
        public static final Codec<PlayTick> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ExtraCodecs.intRange(1, MAX_DURATION).fieldOf("duration").forGetter(PlayTick::duration),
                ExtraCodecs.intRange(0, NOTE_COUNT -1).listOf().fieldOf("notes").forGetter(PlayTick::notes)
        ).apply(instance, PlayTick::new));

        public static final StreamCodec<ByteBuf, PlayTick> STREAM_CODEC = ByteBufCodecs.INT.map(PlayTick::unpack, PlayTick::pack);

        public PlayTick {
            notes = List.copyOf(notes);
        }

        private int pack() {
            int bits = 0;

            for (final int note : this.notes) {
                bits |= 1 << note;
            }

            return this.duration << NOTE_COUNT | bits;
        }

        private static PlayTick unpack(final int packed) {
            final int bits = packed & (1 << NOTE_COUNT) - 1;
            final Integer[] notes = new Integer[Integer.bitCount(bits)];

            for (int note = 0, i = 0; note < NOTE_COUNT; note++) {
                if ((bits & 1 << note) != 0) {
                    notes[i++] = note;
                }
            }

            return new PlayTick(packed >>> NOTE_COUNT & MAX_DURATION, List.of(notes));
        }
    }
}
