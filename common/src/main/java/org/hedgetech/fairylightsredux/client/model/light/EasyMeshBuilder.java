package org.hedgetech.fairylightsredux.client.model.light;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EasyMeshBuilder {
    private final String name;
    private final CubeListBuilder cubes;
    private final List<EasyMeshBuilder> children;

    public float x, y, z;
    public float xRot, yRot, zRot;

    public EasyMeshBuilder(final String name) {
        this.name = name;
        this.cubes = new CubeListBuilder();
        this.children = new ArrayList<>();
    }

    public EasyMeshBuilder(final String name, final int u, final int v) {
        this(name);
        this.setTextureOffset(u, v);
    }

    public EasyMeshBuilder setTextureOffset(int u, int v) {
        this.cubes.texOffs(u, v);
        return this;
    }

    public EasyMeshBuilder addBox(float x, float y, float z, float width, float height, float depth) {
        this.cubes.addBox(x, y, z, width, height, depth);
        return this;
    }

    public EasyMeshBuilder addBox(float x, float y, float z, float width, float height, float depth, float expand) {
        this.cubes.addBox(x, y, z, width, height, depth, new CubeDeformation(expand));
        return this;
    }

    public EasyMeshBuilder addBox(float x, float y, float z, float width, float height, float depth, Set<Direction> visibleFaces) {
        this.cubes.addBox(x, y, z, width, height, depth, visibleFaces);
        return this;
    }

    public void addChild(EasyMeshBuilder child) {
        this.children.add(child);
    }

    public EasyMeshBuilder setRotationPoint(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public EasyMeshBuilder setRotationAngles(float x, float y) {
        this.xRot = x;
        this.yRot = y;
        return this;
    }

    public void setRotationAngles(float x, float y, float z) {
        this.xRot = x;
        this.yRot = y;
        this.zRot = z;
    }

    public void build(final PartDefinition parent) {
        PartDefinition part = parent.addOrReplaceChild(this.name,
                this.cubes,
                PartPose.offsetAndRotation(this.x, this.y, this.z, this.xRot, this.yRot, this.zRot));

        for (EasyMeshBuilder child : this.children) {
            child.build(part);
        }
    }
}
