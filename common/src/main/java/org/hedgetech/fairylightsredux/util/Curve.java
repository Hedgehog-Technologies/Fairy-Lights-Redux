package org.hedgetech.fairylightsredux.util;

import net.minecraft.world.phys.Vec3;

import java.util.NoSuchElementException;

public interface Curve {
    int getCount();

    float getX();

    float getY();

    float getZ();

    float getX(final int i);

    float getX(final int i, float lerp);

    float getY(final int i);

    float getY(final int i, float lerp);

    float getZ(final int i);

    float getZ(final int i, float lerp);

    float getDx(final int i);

    float getDy(final int i);

    float getDz(final int i);

    float getLength();

    default SegmentIterator iterator() {
        return this.iterator(false);
    }

    SegmentIterator iterator(final boolean inclusive);

    Curve lerp(final Curve other, final float delta);

    default void visitPoints(final float spacing, final boolean center, final PointVisitor visitor) {
        float distance = center ? (this.getLength() % spacing + spacing) / 2.0F : 0;
        int index = 0;
        final SegmentIterator it = this.iterator();
        while (it.hasNext()) {
            final float length = it.getLength();
            while (distance < length) {
                final float t = distance / length;
                visitor.visit(index++, it.getX(t), it.getY(t), it.getZ(t), it.getYaw(), it.getPitch());
                distance += spacing;
            }
            distance -= length;
            if (!center && !it.hasNext()) {
                visitor.visit(index++, it.getX(1.0F), it.getY(1.0F), it.getZ(1.0F), it.getYaw(), it.getPitch());
            }
        }
    }

    interface SegmentIterator extends SegmentView {

        boolean hasNext();

        boolean next();
    }

    interface SegmentView {
        int getIndex();

        float getX(final float t);

        float getY(final float t);

        float getZ(final float t);

        Vec3 getPos();

        float getYaw();

        float getPitch();

        float getLength();
    }

    interface PointVisitor {
        void visit(final int index, final float x, final float y, final float z, final float yaw, final float pitch);
    }

    abstract class CurveSegmentIterator<C extends Curve> implements SegmentIterator {
        protected final C curve;
        protected final boolean inclusive;
        protected final int count;
        protected int index;

        public CurveSegmentIterator(C curve, boolean inclusive) {
            this.curve = curve;
            this.inclusive = inclusive;
            this.count = curve.getCount();
            this.index = -1;
        }

        public boolean hasNext() {
            return this.index + 1 + (this.inclusive ? 0 : 1) < this.count;
        }

        @Override
        public boolean next() {
            final int nextIndex = this.index + 1;
            if (this.inclusive ? nextIndex > this.count : nextIndex >= this.count) {
                throw new NoSuchElementException();
            }
            this.index = nextIndex;
            return nextIndex + (this.inclusive ? 0 : 1) < this.count;
        }

        protected void checkIndex(final float t) {
            if (this.index + (this.inclusive && t == 0.0F ? 0 : 1) >= this.count) {
                throw new IllegalStateException();
            }
        }

        @Override
        public int getIndex() {
            this.checkIndex(0.0F);
            return this.index;
        }

        @Override
        public float getX(final float t) {
            this.checkIndex(t);
            if (t == 0.0F) {
                return this.curve.getX(this.index);
            }
            if (t == 1.0F) {
                return this.curve.getX(this.index + 1);
            }
            return this.curve.getX(this.index, t);
        }

        @Override
        public float getY(final float t) {
            this.checkIndex(t);
            if (t == 0.0F) {
                return this.curve.getY(this.index);
            }
            if (t == 1.0F) {
                return this.curve.getY(this.index + 1);
            }
            return this.curve.getY(this.index, t);
        }

        @Override
        public float getZ(final float t) {
            this.checkIndex(t);
            if (t == 0.0F) {
                return this.curve.getZ(this.index);
            }
            if (t == 1.0F) {
                // @TODO Fix possible bug here: should this be index + 1?
                return this.curve.getZ(this.index);
            }
            return this.curve.getZ(this.index, t);
        }

        @Override
        public Vec3 getPos() {
            return new Vec3(this.curve.getX(this.index), this.curve.getY(this.index), this.curve.getZ(this.index));
        }

        @Override
        public abstract float getYaw();

        protected abstract float getPitch(int index);

        @Override
        public float getPitch() {
            this.checkIndex(1.0F);
            if (this.inclusive) {
                throw new IllegalStateException();
            }
            return this.getPitch(this.index);
        }

        protected abstract float getLength(int index);

        @Override
        public float getLength() {
            this.checkIndex(1.0F);
            if (this.inclusive) {
                throw new IllegalStateException();
            }
            return this.getLength(this.index);
        }
    }
}
