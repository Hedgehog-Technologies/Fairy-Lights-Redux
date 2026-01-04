package org.hedgetech.fairylightsredux.server.block;

import org.hedgetech.fairylightsredux.Constants;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public final class FLRBlocks {
    public static final DeferredRegister<Block> REG = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MOD_ID);


    public static final RegistryObject<FastenerBlock> FASTENER = REG.register("fastener", () -> new FastenerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).forceSolidOn().strength(3.5F).sound(SoundType.LANTERN)));

    public static final RegistryObject<LightBlock> FAIRY_LIGHT = REG.register("fairy_light", FLRBlocks.createLight(SimpleLightVariant.FAIRY_LIGHT));

    public static final RegistryObject<LightBlock> PAPER_LANTERN = REG.register("paper_lantern", FLRBlocks.createLight(SimpleLightVariant.PAPER_LANTERN));

    public static final RegistryObject<LightBlock> ORB_LANTERN = REG.register("orb_lantern", FLRBlocks.createLight(SimpleLightVariant.ORB_LANTERN));

    public static final RegistryObject<LightBlock> FLOWER_LIGHT = REG.register("flower_light", FLRBlocks.createLight(SimpleLightVariant.FLOWER_LIGHT));

    public static final RegistryObject<LightBlock> CANDLE_LANTERN_LIGHT = REG.register("candle_lantern_light", FLRBlocks.createLight(SimpleLightVariant.CANDLE_LANTERN_LIGHT));

    public static final RegistryObject<LightBlock> OIL_LANTERN_LIGHT = REG.register("oil_lantern_light", FLRBlocks.createLight(SimpleLightVariant.OIL_LANTERN_LIGHT));

    public static final RegistryObject<LightBlock> JACK_O_LANTERN = REG.register("jack_o_lantern", FLRBlocks.createLight(SimpleLightVariant.JACK_O_LANTERN));

    public static final RegistryObject<LightBlock> SKULL_LIGHT = REG.register("skull_light", FLRBlocks.createLight(SimpleLightVariant.SKULL_LIGHT));

    public static final RegistryObject<LightBlock> GHOST_LIGHT = REG.register("ghost_light", FLRBlocks.createLight(SimpleLightVariant.GHOST_LIGHT));

    public static final RegistryObject<LightBlock> SPIDER_LIGHT = REG.register("spider_light", FLRBlocks.createLight(SimpleLightVariant.SPIDER_LIGHT));

    public static final RegistryObject<LightBlock> WITCH_LIGHT = REG.register("witch_light", FLRBlocks.createLight(SimpleLightVariant.WITCH_LIGHT));

    public static final RegistryObject<LightBlock> SNOWFLAKE_LIGHT = REG.register("snowflake_light", FLRBlocks.createLight(SimpleLightVariant.SNOWFLAKE_LIGHT));

    public static final RegistryObject<LightBlock> HEART_LIGHT = REG.register("heart_light", FLRBlocks.createLight(SimpleLightVariant.HEART_LIGHT));

    public static final RegistryObject<LightBlock> MOON_LIGHT = REG.register("moon_light", FLRBlocks.createLight(SimpleLightVariant.MOON_LIGHT));

    public static final RegistryObject<LightBlock> STAR_LIGHT = REG.register("star_light", FLRBlocks.createLight(SimpleLightVariant.STAR_LIGHT));

    public static final RegistryObject<LightBlock> ICICLE_LIGHTS = REG.register("icicle_lights", FLRBlocks.createLight(SimpleLightVariant.ICICLE_LIGHTS));

    public static final RegistryObject<LightBlock> METEOR_LIGHT = REG.register("meteor_light", FLRBlocks.createLight(SimpleLightVariant.METEOR_LIGHT));

    public static final RegistryObject<LightBlock> OIL_LANTERN = REG.register("oil_lantern", FLRBlocks.createLight(SimpleLightVariant.OIL_LANTERN));

    public static final RegistryObject<LightBlock> CANDLE_LANTERN = REG.register("candle_lantern", FLRBlocks.createLight(SimpleLightVariant.CANDLE_LANTERN));

    public static final RegistryObject<LightBlock> INCANDESCENT_LIGHT = REG.register("incandescent_light", FLRBlocks.createLight(SimpleLightVariant.INCANDESCENT_LIGHT));


    private static Supplier<LightBlock> createLight(final LightVariant<?> variant, final BiFunction<BlockBehaviour.Properties, LightVariant<?>, LightBlock> factory) {
        return () -> factory.apply(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).forceSolidOn().strength(3.5F).sound(SoundType.LANTERN).lightLevel(state -> state.getValue(LightBlock.LIT) ? 15 : 0).noCollission(), variant);
    }

    private static Supplier<LightBlock> createLight(final LightVariant<?> variant) {
        return createLight(variant, LightBlock::new);
    }

    private FLRBlocks() {}
}
