package org.hedgetech.fairylightsredux.server.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.server.item.LightVariant;

import java.util.function.BiConsumer;

public final class FLRBlocks {
    public static FastenerBlock FASTENER;
    public static LightBlock FAIRY_LIGHT;
    public static LightBlock PAPER_LANTERN;
    public static LightBlock ORB_LANTERN;
    public static LightBlock FLOWER_LIGHT;
    public static LightBlock CANDLE_LANTERN_LIGHT;
    public static LightBlock OIL_LANTERN_LIGHT;
    public static LightBlock JACK_O_LANTERN;
    public static LightBlock SKULL_LIGHT;
    public static LightBlock GHOST_LIGHT;
    public static LightBlock SPIDER_LIGHT;
    public static LightBlock WITCH_LIGHT;
    public static LightBlock SNOWFLAKE_LIGHT;
    public static LightBlock HEART_LIGHT;
    public static LightBlock MOON_LIGHT;
    public static LightBlock STAR_LIGHT;
    public static LightBlock ICICLE_LIGHTS;
    public static LightBlock METEOR_LIGHT;
    public static LightBlock OIL_LANTERN;
    public static LightBlock CANDLE_LANTERN;
    public static LightBlock INCANDESCENT_LIGHT;

    public static void register(final BiConsumer<ResourceLocation, Block> registry) {
        FASTENER = createFastener(registry, "fastener");
        FAIRY_LIGHT = createLight(registry, "fairy_light", LightVariant.FAIRY_LIGHT);
        PAPER_LANTERN = createLight(registry, "paper_lantern", LightVariant.PAPER_LANTERN);
        ORB_LANTERN = createLight(registry, "orb_lantern", LightVariant.ORB_LANTERN);
        FLOWER_LIGHT = createLight(registry, "flower_light", LightVariant.FLOWER_LIGHT);
        CANDLE_LANTERN_LIGHT = createLight(registry, "candle_lantern_light", LightVariant.CANDLE_LANTERN_LIGHT);
        OIL_LANTERN_LIGHT = createLight(registry, "oil_lantern_light", LightVariant.OIL_LANTERN_LIGHT);
        JACK_O_LANTERN = createLight(registry, "jack_o_lantern", LightVariant.JACK_O_LANTERN);
        SKULL_LIGHT = createLight(registry, "skull_light", LightVariant.SKULL_LIGHT);
        GHOST_LIGHT = createLight(registry, "ghost_light", LightVariant.GHOST_LIGHT);
        SPIDER_LIGHT = createLight(registry, "spider_light", LightVariant.SPIDER_LIGHT);
        WITCH_LIGHT = createLight(registry, "witch_light", LightVariant.WITCH_LIGHT);
        SNOWFLAKE_LIGHT = createLight(registry, "snowflake_light", LightVariant.SNOWFLAKE_LIGHT);
        HEART_LIGHT = createLight(registry, "heart_light", LightVariant.HEART_LIGHT);
        MOON_LIGHT = createLight(registry, "moon_light", LightVariant.MOON_LIGHT);
        STAR_LIGHT = createLight(registry, "star_light", LightVariant.STAR_LIGHT);
        ICICLE_LIGHTS = createLight(registry, "icicle_lights", LightVariant.ICICLE_LIGHTS);
        METEOR_LIGHT = createLight(registry, "meteor_lights", LightVariant.METEOR_LIGHT);
        OIL_LANTERN = createLight(registry, "oil_lantern", LightVariant.OIL_LANTERN);
        CANDLE_LANTERN = createLight(registry, "candle_lantern", LightVariant.CANDLE_LANTERN);
        INCANDESCENT_LIGHT = createLight(registry, "incandescent_light", LightVariant.INCANDESCENT_LIGHT);
    }

    private static FastenerBlock createFastener(final BiConsumer<ResourceLocation, Block> registry, final String name) {
        final ResourceLocation id = Constants.id(name);
        final FastenerBlock block = new FastenerBlock(BlockBehaviour.Properties.of()
                .setId(ResourceKey.create(Registries.BLOCK, id))
                .mapColor(MapColor.METAL)
                .forceSolidOn()
                .strength(3.5F)
                .sound(SoundType.LANTERN)
                .noLootTable()
        );

        registry.accept(id, block);
        return block;
    }

    private static LightBlock createLight(final BiConsumer<ResourceLocation, Block> registry, final String name, final LightVariant<?> variant) {
        final ResourceLocation id = Constants.id(name);
        final LightBlock block = new LightBlock(BlockBehaviour.Properties.of()
                .setId(ResourceKey.create(Registries.BLOCK, id))
                .mapColor(MapColor.METAL)
                .forceSolidOn()
                .strength(3.5F)
                .sound(SoundType.LANTERN)
                .lightLevel(state -> state.getValue(LightBlock.LIT) ? 15 : 0)
                .noCollission(),
            variant
        );

        registry.accept(id, block);
        return block;
    }

    private FLRBlocks() {}
}
