package org.hedgetech.fairylightsredux.client.model.light;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.AABB;
import org.hedgetech.fairylightsredux.feature.light.Light;
import org.hedgetech.fairylightsredux.feature.light.LightBehavior;
import org.hedgetech.fairylightsredux.util.ColorUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class LightModel<T extends LightBehavior> extends Model {
    protected final ModelPart lit;
    protected final ModelPart litTint;
    protected final ModelPart litTintGlow;
    protected final ModelPart unlit;
    protected float brightness = 1.0F;
    protected float red = 1.0F;
    protected float green = 1.0F;
    protected float blue = 1.0F;

    @Nullable private AABB bounds;
    private double floorOffset = Double.NaN;
    private boolean powered;

    public LightModel(ModelPart root) {
        super(root, RenderType::entityTranslucent);
        this.lit = root.getChild("lit");
        this.litTint = root.getChild("lit_tint");
        this.litTintGlow = root.getChild("lit_tint_glow");
        this.unlit = root.getChild("unlit");
    }

    public record LightMeshHelper(EasyMeshBuilder lit, EasyMeshBuilder litTint, EasyMeshBuilder litTintGlow, EasyMeshBuilder unlit, List<EasyMeshBuilder> extra) {
        public BulbBuilder createBulb() {
            return new BulbBuilder(this.litTint(), this.litTintGlow());
        }

        public LayerDefinition build() {
            MeshDefinition def = new MeshDefinition();
            this.lit().build(def.getRoot());
            this.litTint().build(def.getRoot());
            this.litTintGlow().build(def.getRoot());
            this.unlit().build(def.getRoot());
            for (EasyMeshBuilder builder : this.extra()) {
                builder.build(def.getRoot());
            }
            return LayerDefinition.create(def, 128, 128);
        }

        public EasyMeshBuilder parented(final String name) {
            EasyMeshBuilder result = new EasyMeshBuilder(name);
            result.addChild(this.lit());
            result.addChild(this.litTint());
            result.addChild(this.litTintGlow());
            result.addChild(this.unlit());
            for (EasyMeshBuilder builder : this.extra()) {
                result.addChild(builder);
            }
            return result;
        }

        public static LightMeshHelper create() {
            EasyMeshBuilder lit = new EasyMeshBuilder("lit");
            EasyMeshBuilder litTint = new EasyMeshBuilder("lit_tint");
            EasyMeshBuilder litTintGlow = new EasyMeshBuilder("lit_tint_glow");
            EasyMeshBuilder unlit = new EasyMeshBuilder("unlit");
            return new LightMeshHelper(lit, litTint, litTintGlow, unlit, new ArrayList<>());
        }
    }

    public AABB getBounds() {
        if (this.bounds == null) {
            final PoseStack matrix = new PoseStack();
            final AABBVertexBuilder builder = new AABBVertextBuilder();
            this.renderToBuffer(matrix, builder, 0, 0, 1.0F, 1.0F, 1.0F, 1.0F);
            this.renderTranslucent(matrix, builder, 0, 0, 1.0F, 1.0F, 1.0F, 1.0F);
            this.bounds = builder.build();
        }
        return this.bounds;
    }

    public double getFloorOffset() {
        if (Double.isNaN(this.floorOffset)) {
            final AABBVertexBuilder builder = new AABBVertexBuilder();
            this.renderToBuffer(new PoseStack(), builder, 0, 0, 1.0F, 1.0F, 1.0F, 1.0F);
            this.floorOffset = builder.build().minY - this.getBounds().minY;
        }
        return this.floorOffset;
    }

    public void animate(final Light<?> light, final T behavior, final float delta) {
        this.powered = light.isPowered();
    }

//    @Override
    public void renderToBuffer(final PoseStack matrix, final VertexConsumer builder, final int light, final int overlay, final float r, final float g, final float b, final float a) {
        final int color = ColorUtil.packColor(r, g, b, a);
        this.unlit.render(matrix, builder, light, overlay, color);
        final int emissiveLight = this.getLight(light);
        this.lit.render(matrix, builder, emissiveLight, overlay, color);
        this.litTint.render(matrix, builder, emissiveLight, overlay, ColorUtil.packColor(r * this.red, g * this.green, b * this.blue, a));
    }

    public void renderTranslucent(final PoseStack matrix, final VertexConsumer builder, final int light, final int overlay, final float r, final float g, final float b, final float a) {
        final float v = this.brightness;
        this.litTintGlow.render(matrix, builder, this.getLight(light), overlay, ColorUtil.packColor(r * this.red * v + (1.0F - v), g * this.green * v + (1.0F - v), b * this.blue * v + (1.0F - v), v * 0.15F + 0.2F));
    }

    protected int getLight(final int packedLight) {
        return (int) Math.max((this.brightness * 15.0F * 16.0F), this.powered ? 0 : packedLight & 255) | packedLight & (255 << 16);
    }
}
