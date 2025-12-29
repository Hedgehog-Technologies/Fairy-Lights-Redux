package org.hedgetech.fairylightsredux.server.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.hedgetech.fairylightsredux.Constants;

public final class FLREntities {
    public static final DeferredRegister<EntityType<?>> REG = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Constants.MOD_ID);

    public static final ResourceKey<EntityType<?>> FASTENER_KEY = ResourceKey.create(
            Registries.ENTITY_TYPE,
            Constants.FASTENER
    );

    public static final RegistryObject<EntityType<FenceFastenerEntity>> FASTENER = REG.register("fastener", () ->
            EntityType.Builder.<FenceFastenerEntity>of(FenceFastenerEntity::new, MobCategory.MISC)
                    .sized(1.15F, 2.8F)
                    .setTrackingRange(10)
                    .setUpdateInterval(Integer.MAX_VALUE)
                    .setShouldReceiveVelocityUpdates(false)
                    .setCustomClientFactory((message, world) -> new FenceFastenerEntity(world))
                    .build(FASTENER_KEY)
    );

    private FLREntities() {}
}
