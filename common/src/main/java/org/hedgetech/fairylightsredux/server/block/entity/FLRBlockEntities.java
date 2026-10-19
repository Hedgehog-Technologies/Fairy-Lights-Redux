package org.hedgetech.fairylightsredux.server.block.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.server.block.FLRBlocks;

import java.util.Set;
import java.util.function.BiConsumer;

public final class FLRBlockEntities {
    public static BlockEntityType<FastenerBlockEntity> FASTENER;
    public static BlockEntityType<LightBlockEntity> LIGHT;

    public static void register(final BiConsumer<ResourceLocation, BlockEntityType<?>> registry) {
        FASTENER = new BlockEntityType<>(FastenerBlockEntity::new, Set.of(FLRBlocks.FASTENER));
        registry.accept(Constants.id("fastener"), FASTENER);

        LIGHT = new BlockEntityType<>(LightBlockEntity::new, Set.of());
        registry.accept(Constants.id("light"), LIGHT);
    }

    private FLRBlockEntities() {}
}
