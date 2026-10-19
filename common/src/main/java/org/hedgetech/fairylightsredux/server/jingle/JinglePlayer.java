package org.hedgetech.fairylightsredux.server.jingle;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.server.feature.light.Light;

public final class JinglePlayer {
    private Jingle jingle;
    private ParticleOptions[] particles = new ParticleOptions[0];
    private int lightOffset;
    private int index;
    private int rest;
    private int time;

    public Jingle getJingle() {
        return this.jingle;
    }

    public boolean isPlaying() {
        return this.jingle != null;
    }

    public float getProgress() {
        return this.jingle == null || this.jingle.getLength() == 0
                ? 0.0F
                : this.time / (float) this.jingle.getLength();
    }

    public void play(final Jingle jingle, final int lightOffset) {
        this.play(jingle, lightOffset, 0, 0, 0);
    }

    private void play(final Jingle jingle, final int lightOffset, final int index, final int rest, final int time) {
        this.jingle = jingle;
        this.particles = getParticles(jingle);
        this.lightOffset = lightOffset;
        this.index = index;
        this.rest = rest;
        this.time = time;
    }

    public void tick(final Level world, final Vec3 origin, final Light<?>[] lights) {
        if (this.jingle == null) return;

        this.time++;
        if (this.rest > 0) {
            this.rest--;
            return;
        }

        if (this.index >= this.jingle.getPlayTicks().size()) {
            this.jingle = null;
            return;
        }

        final Jingle.PlayTick tick = this.jingle.getPlayTicks().get(this.index++);
        this.rest = tick.duration() - 1;

        if (world.isClientSide()) {
            for (final int note : tick.notes()) {
                final int idx = note - this.jingle.getLowestNote() + this.lightOffset;

                if (idx >= 0 && idx < lights.length) {
                    lights[idx].jingle(world, origin, note, this.particles);
                }
            }
        }
    }

    public void save(final ValueOutput output) {
        if (this.jingle != null) {
            output.store("jingle", Jingle.CODEC, this.jingle);
            output.putInt("lightOffset", this.lightOffset);
            output.putInt("index", this.index);
            output.putInt("rest", this.rest);
            output.putInt("time", this.time);
        }
    }

    public void load(final ValueInput input) {
        input.read("jingle", Jingle.CODEC).ifPresentOrElse(
                jingle -> this.play(jingle, input.getIntOr("lightOffset", 0), input.getIntOr("index", 0), input.getIntOr("rest", 0), input.getIntOr("time", 0)),
                () -> this.jingle = null
        );
    }

    private static ParticleOptions[] getParticles(final Jingle jingle) {
        return switch (jingle.getTitle().hashCode()) {
            case 0xF2B587DE -> new ParticleOptions[]{ParticleTypes.NOTE, ParticleTypes.LAVA };
            case 0xC3FA68BD, 0x70F77BF4 -> new ParticleOptions[]{ ParticleTypes.NOTE, ParticleTypes.HEART };
            default -> new ParticleOptions[]{ ParticleTypes.NOTE };
        };
    }
}
