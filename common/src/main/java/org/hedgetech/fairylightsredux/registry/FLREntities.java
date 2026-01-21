package org.hedgetech.fairylightsredux.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.hedgetech.fairylightsredux.content.entity.EntityDef;
import org.hedgetech.fairylightsredux.content.entity.EntityDefs;

import java.util.HashMap;
import java.util.Map;

public final class FLREntities {
    public static final ResourceKey<EntityType<?>> FASTENER_KEY = ResourceKey.create(
            Registries.ENTITY_TYPE,
            EntityDefs.FENCE_FASTENER_RL
    );

    private static final Map<String, EntityType<?>> TYPES = new HashMap<>();

    public static void registerAll(RegistryBridge bridge) {
        for (EntityDef<?> def : EntityDefs.TYPES) {
            EntityType<?> type = bridge.registerEntityType(
                    def.id(),
                    def.build(FASTENER_KEY)
            );
            TYPES.put(def.id(), type);
        }
    }

    public static EntityType<?> get(String id) {
        return TYPES.get(id);
    }

    public static <E extends Entity> EntityType<E> get(String id, Class<E> clazz) {
        EntityType<?> type = get(id);
        if (type == null) return null;
        if (!type.getBaseClass().isInstance(clazz)) return null;
        @SuppressWarnings("unchecked")
        EntityType<E> cast = (EntityType<E>) type;
        return cast;
    }

    private FLREntities() {}
}
