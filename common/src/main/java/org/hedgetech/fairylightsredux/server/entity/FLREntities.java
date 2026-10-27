package org.hedgetech.fairylightsredux.server.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.FLRNames;

import java.util.function.BiConsumer;

public final class FLREntities {
    public static EntityType<FenceFastenerEntity> FASTENER;

    public static void register(final BiConsumer<ResourceLocation, EntityType<?>> registry) {
        final ResourceLocation id = Constants.id(FLRNames.FASTENER);
        FASTENER = EntityType.Builder.<FenceFastenerEntity>of(FenceFastenerEntity::new, MobCategory.MISC)
                .noLootTable()
                .sized(1.15F, 2.8F)
                .clientTrackingRange(10)
                .updateInterval(Integer.MAX_VALUE)
                .build(ResourceKey.create(Registries.ENTITY_TYPE, id));
        registry.accept(id, FASTENER);
    }

    private FLREntities() {}
}
