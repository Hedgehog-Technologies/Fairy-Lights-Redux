package org.hedgetech.fairylightsredux.content.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.entity.FenceFastenerEntity;

import java.util.List;

public final class EntityDefs {
    public static final String FENCE_FASTENER_ID = "fastener";
    public static final ResourceLocation FENCE_FASTENER_RL = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, FENCE_FASTENER_ID);

    public static final List<EntityDef<?>> TYPES = List.of(
            new EntityDef<FenceFastenerEntity>(
                    FENCE_FASTENER_ID,
                    MobCategory.MISC,
                    FenceFastenerEntity::new,
                    1.15F, 2.8F,
                    1.0F,
                    10,
                    Integer.MAX_VALUE
            )
    );


    private EntityDefs() {}
}
