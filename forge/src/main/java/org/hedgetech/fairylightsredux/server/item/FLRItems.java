package org.hedgetech.fairylightsredux.server.item;

import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.server.block.FLRBlocks;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.BiFunction;

public final class FLRItems {
    public static final DeferredRegister<Item> REG = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);


    public static final RegistryObject<ConnectionItem> HANGING_LIGHTS = REG.register("hanging_lights", () -> new HangingLightsConnectionItem(defaultProperties()));

    public static final RegistryObject<ConnectionItem> PENNANT_BUNTING = REG.register("pennant_bunting", () -> new PennantBuntingConnectionItem(defaultProperties()));

    public static final RegistryObject<ConnectionItem> TINSEL = REG.register("tinsel", () -> new TinselConnectionItem(defaultProperties()));

    public static final RegistryObject<ConnectionItem> LETTER_BUNTING = REG.register("letter_bunting", () -> new LetterBuntingConnectionItem(defaultProperties()));

    public static final RegistryObject<ConnectionItem> GARLAND = REG.register("garland", () -> new GarlandConnectionItem(defaultProperties()));

    public static final RegistryObject<LightItem> FAIRY_LIGHT = REG.register("fairy_light", FLRItems.createColorLight(FLRBlocks.FAIRY_LIGHT));

    public static final RegistryObject<LightItem> PAPER_LANTERN = REG.register("paper_lantern", FLRItems.createColorLight(FLRBlocks.PAPER_LANTERN));

    public static final RegistryObject<LightItem> ORB_LANTERN = REG.register("orb_lantern", FLRItems.createColorLight(FLRBlocks.ORB_LANTERN));

    public static final RegistryObject<LightItem> FLOWER_LIGHT = REG.register("flower_light", FLRItems.createColorLight(FLRBlocks.FLOWER_LIGHT));

    public static final RegistryObject<LightItem> CANDLE_LANTERN_LIGHT = REG.register("candle_lantern_light", FLRItems.createColorLight(FLRBlocks.CANDLE_LANTERN_LIGHT));

    public static final RegistryObject<LightItem> OIL_LANTERN_LIGHT = REG.register("oil_lantern_light", FLRItems.createColorLight(FLRBlocks.OIL_LANTERN_LIGHT));

    public static final RegistryObject<LightItem> JACK_O_LANTERN = REG.register("jack_o_lantern", FLRItems.createColorLight(FLRBlocks.JACK_O_LANTERN));

    public static final RegistryObject<LightItem> SKULL_LIGHT = REG.register("skull_light", FLRItems.createColorLight(FLRBlocks.SKULL_LIGHT));

    public static final RegistryObject<LightItem> GHOST_LIGHT = REG.register("ghost_light", FLRItems.createColorLight(FLRBlocks.GHOST_LIGHT));

    public static final RegistryObject<LightItem> SPIDER_LIGHT = REG.register("spider_light", FLRItems.createColorLight(FLRBlocks.SPIDER_LIGHT));

    public static final RegistryObject<LightItem> WITCH_LIGHT = REG.register("witch_light", FLRItems.createColorLight(FLRBlocks.WITCH_LIGHT));

    public static final RegistryObject<LightItem> SNOWFLAKE_LIGHT = REG.register("snowflake_light", FLRItems.createColorLight(FLRBlocks.SNOWFLAKE_LIGHT));

    public static final RegistryObject<LightItem> HEART_LIGHT = REG.register("heart_light", FLRItems.createColorLight(FLRBlocks.HEART_LIGHT));

    public static final RegistryObject<LightItem> MOON_LIGHT = REG.register("moon_light", FLRItems.createColorLight(FLRBlocks.MOON_LIGHT));

    public static final RegistryObject<LightItem> STAR_LIGHT = REG.register("star_light", FLRItems.createColorLight(FLRBlocks.STAR_LIGHT));

    public static final RegistryObject<LightItem> ICICLE_LIGHTS = REG.register("icicle_lights", FLRItems.createColorLight(FLRBlocks.ICICLE_LIGHTS));

    public static final RegistryObject<LightItem> METEOR_LIGHT = REG.register("meteor_light", FLRItems.createColorLight(FLRBlocks.METEOR_LIGHT));

    public static final RegistryObject<LightItem> OIL_LANTERN = REG.register("oil_lantern", FLRItems.createLight(FLRBlocks.OIL_LANTERN, LightItem::new));

    public static final RegistryObject<LightItem> CANDLE_LANTERN = REG.register("candle_lantern", FLRItems.createLight(FLRBlocks.CANDLE_LANTERN, LightItem::new));

    public static final RegistryObject<LightItem> INCANDESCENT_LIGHT = REG.register("incandescent_light", FLRItems.createLight(FLRBlocks.INCANDESCENT_LIGHT, LightItem::new));

    public static final RegistryObject<Item> TRIANGLE_PENNANT = REG.register("triangle_pennant", () -> new PennantItem(defaultProperties()));

    public static final RegistryObject<Item> SPEARHEAD_PENNANT = REG.register("spearhead_pennant", () -> new PennantItem(defaultProperties()));

    public static final RegistryObject<Item> SWALLOWTAIL_PENNANT = REG.register("swallowtail_pennant", () -> new PennantItem(defaultProperties()));

    public static final RegistryObject<Item> SQUARE_PENNANT = REG.register("square_pennant", () -> new PennantItem(defaultProperties()));


    private static Item.Properties defaultProperties() {
        return new Item.Properties();
    }

    private static Supplier<LightItem> createLight(final RegistryObject<LightBlock> block, final BiFunction<LightBlock, Item.Properties, LightItem>, factory) {
        return () -> factory.apply(block.get(), defaultProperties().stacksTo(16));
    }

    private static Supplier<LightItem> createColorLight(final RegistryObject<LightBlock> block) {
        return createLight(block, ColorLightItem::new);
    }

    private FLRItems() {}
}
