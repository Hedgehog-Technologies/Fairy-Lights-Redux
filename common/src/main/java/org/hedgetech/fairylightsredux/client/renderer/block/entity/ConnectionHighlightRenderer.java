package org.hedgetech.fairylightsredux.client.renderer.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.client.ConnectionHitHandler;
import org.hedgetech.fairylightsredux.server.connection.Connection;
import org.hedgetech.fairylightsredux.server.feature.FeatureType;
import org.hedgetech.fairylightsredux.util.ICurve;
import org.joml.Vector3f;

import java.util.function.BiConsumer;

public final class ConnectionHighlightRenderer {
    private static final int HIGHLIGHT_COLOR = ARGB.color(102, 0xFF000000);
    private static final int HIGH_CONTRAST_COLOR = 0xFF57FFE1;
    private static final int HIGH_CONTRAST_BACKING_COLOR = 0xFF000000;

    public static void render(final ConnectionHitHandler.Hit hit, final Vec3 origin, final PoseStack matrix, final MultiBufferSource source) {
        final AABB bb = hit.intersection().hitBox().move(origin.reverse()).inflate(0.002D);

        forEachOutline(source, (buf, color) -> {
            if (hit.intersection().featureType() == FeatureType.CORD) {
                renderCord(hit.connection(), matrix.last(), buf, color);
            } else {
                ShapeRenderer.renderLineBox(matrix, buf, bb, ARGB.redFloat(color), ARGB.greenFloat(color), ARGB.blueFloat(color), ARGB.alphaFloat(color));
            }
        });
    }

    public static void renderBox(final PoseStack matrix, final MultiBufferSource source, final AABB bb) {
        forEachOutline(source, (buf, color) ->  ShapeRenderer.renderLineBox(matrix, buf, bb, ARGB.redFloat(color), ARGB.greenFloat(color), ARGB.blueFloat(color), ARGB.alphaFloat(color)));
    }

    private static void forEachOutline(final MultiBufferSource source, final BiConsumer<VertexConsumer, Integer> outline) {
        final boolean highContrast = Minecraft.getInstance().options.highContrastBlockOutline().get();
        if (highContrast) {
            outline.accept(source.getBuffer(RenderType.secondaryBlockOutline()), HIGH_CONTRAST_BACKING_COLOR);
        }
        outline.accept(source.getBuffer(RenderType.lines()), highContrast ? HIGH_CONTRAST_COLOR : HIGHLIGHT_COLOR);
    }

    private static void renderCord(final Connection connection, final PoseStack.Pose pose, final VertexConsumer buf, final int color) {
        final ICurve cat = connection.getCatenary();
        if (cat == null) return;

        final Vector3f p = new Vector3f();
        final Vector3f v1 = new Vector3f();
        final Vector3f v2 = new Vector3f();
        final LineBuilder builder = new LineBuilder(pose, buf, color);
        final float r = connection.getRadius() + 0.01F;

        for (int edge = 0; edge < 4; edge++) {
            p.set(cat.getX(0), cat.getY(0), cat.getZ(0));
            v1.set(cat.getDx(0), cat.getDy(0), cat.getDz(0));
            v1.normalize();
            v2.set(-v1.x(), -v1.y(), -v1.z());

            for (int n = 0; edge == 0 && n < 8; n++) {
                builder.accept(corner((n + 1) / 2 % 4, p, v1, v2, r));
            }

            builder.accept(corner(edge, p, v1, v2, r));

            for (int i = 1; i < cat.getCount() - 1; i++) {
                p.set(cat.getX(i), cat.getY(i), cat.getZ(i));
                v2.set(-cat.getDx(i), -cat.getDy(i), -cat.getDz(i));
                v2.normalize();
                builder.accept(corner(edge, p, v1, v2, r));
                builder.accept(corner(edge, p, v1, v2, r));
                v1.set(-v2.x(), -v2.y(), -v2.z());
            }

            p.set(cat.getX(), cat.getY(), cat.getZ());
            v2.set(-v1.x(), -v1.y(), -v1.z());
            builder.accept(corner(edge, p, v1, v2, r));

            for (int n = 0; edge == 0 && n < 8; n++) {
                builder.accept(corner((n + 1) / 2 % 4, p, v1, v2, r));
            }
        }
    }

    private static Vector3f corner(final int edge, final Vector3f p, final Vector3f v1, final Vector3f v2, final float r) {
        final Vector3f up = new Vector3f();
        final Vector3f side = new Vector3f();

        if (v1.dot(v2) < -(1.0F - 1.0e-2F)) {
            final float h = Mth.sqrt(v1.x() * v1.x() + v1.z() * v1.z());

            if (h < 1.0e-2F) {
                up.set(-1.0F, 0.0F, 0.0F);
            } else {
                up.set(-v1.x() / h * -v1.y(), -h, -v1.z() / h * -v1.y());
            }
        } else {
            up.set(v2.x(), v2.y(), v2.z());
            up.lerp(v1, 0.5F);
        }

        up.normalize();
        side.set(v1.x(), v1.y(), v1.z());
        side.cross(up);
        side.normalize();
        side.mul(edge == 0 || edge == 3 ? -r : r);
        up.mul(edge < 2 ? -r : r);
        up.add(side);
        up.add(p);

        return up;
    }

    private static final class LineBuilder {
        private final PoseStack.Pose pose;
        private final VertexConsumer buf;
        private final int color;
        private Vector3f last;

        private LineBuilder(final PoseStack.Pose pose, final VertexConsumer buf, final int color) {
            this.pose = pose;
            this.buf = buf;
            this.color = color;
        }

        private void accept(final Vector3f pos) {
            if (this.last == null) {
                this.last = pos;
                return;
            }

            final Vector3f n = new Vector3f(pos).sub(this.last).normalize();
            this.buf.addVertex(this.pose, this.last.x(), this.last.y(), this.last.z())
                    .setColor(this.color)
                    .setNormal(this.pose, n.x(), n.y(), n.z());
            this.buf.addVertex(this.pose, pos.x(), pos.y(), pos.z())
                    .setColor(this.color)
                    .setNormal(this.pose, n.x(), n.y(), n.z());
            this.last = null;
        }
    }

    private ConnectionHighlightRenderer() {}
}
