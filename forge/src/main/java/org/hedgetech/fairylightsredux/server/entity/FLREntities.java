package org.hedgetech.fairylightsredux.server.entity;

import org.hedgetech.fairylightsredux.Constants;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class FLREntities {
    public static final DeferredRegister<EntityType<?>> REG = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Constants.MOD_ID);

    public static final RegistryObject<EntityType<FenceFastenerEntity>> FASTENER = REG.register("fastener", () ->
            EntityType.Builder.of(FenceFastenerEntity::new, MobCategory.MISC)
                    .sized(1.15F, 2.8F)
                    .setTrackingRange(10)
                    .setUpdateInterval(Integer.MAX_VALUE)
                    .setShouldReceiveVelocityUpdates(false)
                    .setCustomClientFactory((message, world) -> new FenceFastenerEntity(world))
                    .build(Constants.MOD_ID + ":fastener")
    );

    private FLREntities() {}
}
