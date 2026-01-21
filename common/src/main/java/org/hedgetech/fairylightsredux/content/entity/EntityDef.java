package org.hedgetech.fairylightsredux.content.entity;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.jetbrains.annotations.Nullable;

public record EntityDef<T extends Entity>(
        String id,
        MobCategory category,
        EntityType.EntityFactory<T> factory,
        float width,
        float height,
        @Nullable Float eyeHeight,
        @Nullable Integer clientTrackingRange,
        @Nullable Integer updateInterval
) {
    public EntityType<T> build(ResourceKey<EntityType<?>> key) {
        EntityType.Builder<T> bob = EntityType.Builder.of(this.factory, this.category)
                .sized(this.width, this.height);

        if (this.eyeHeight != null) bob.eyeHeight(this.eyeHeight);
        if (this.clientTrackingRange != null) bob.clientTrackingRange(this.clientTrackingRange);
        if (this.updateInterval != null) bob.updateInterval(this.updateInterval);

        return bob.build(key);
    }
}
