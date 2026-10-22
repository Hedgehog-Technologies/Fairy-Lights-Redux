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

        LIGHT = new BlockEntityType<>(LightBlockEntity::new, Set.of(
                FLRBlocks.FAIRY_LIGHT,
                FLRBlocks.PAPER_LANTERN,
                FLRBlocks.ORB_LANTERN,
                FLRBlocks.FLOWER_LIGHT,
                FLRBlocks.CANDLE_LANTERN_LIGHT,
                FLRBlocks.OIL_LANTERN_LIGHT,
                FLRBlocks.JACK_O_LANTERN,
                FLRBlocks.SKULL_LIGHT,
                FLRBlocks.GHOST_LIGHT,
                FLRBlocks.SPIDER_LIGHT,
                FLRBlocks.WITCH_LIGHT,
                FLRBlocks.SNOWFLAKE_LIGHT,
                FLRBlocks.HEART_LIGHT,
                FLRBlocks.MOON_LIGHT,
                FLRBlocks.STAR_LIGHT,
                FLRBlocks.ICICLE_LIGHTS,
                FLRBlocks.METEOR_LIGHT,
                FLRBlocks.OIL_LANTERN,
                FLRBlocks.CANDLE_LANTERN,
                FLRBlocks.INCANDESCENT_LIGHT
        ));
        registry.accept(Constants.id("light"), LIGHT);
    }

    private FLRBlockEntities() {}
}
