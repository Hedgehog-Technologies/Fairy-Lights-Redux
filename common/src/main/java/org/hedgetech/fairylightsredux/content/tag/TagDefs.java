package org.hedgetech.fairylightsredux.content.tag;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public final class TagDefs {
    private static final List<TagDef<?, ?>> ALL = new ArrayList<>();

    private static <T extends TagDef<?, ?>> T register(final T def) {
        ALL.add(def);
        return def;
    }

    public static List<TagDef<?, ?>> all() {
        return ALL;
    }

    public static ItemTagDef item(String id) {
        return register(new ItemTagDef(ResourceLocation.withDefaultNamespace(id)));
    }

    public static BlockTagDef block(String id) {
        return register(new BlockTagDef(ResourceLocation.withDefaultNamespace(id)));
    }

    public static final ItemTagDef DYES_WHITE      = item("c:dyes/white").add(Items.WHITE_DYE);
    public static final ItemTagDef DYES_ORANGE     = item("c:dyes/orange").add(Items.ORANGE_DYE);
    public static final ItemTagDef DYES_MAGENTA    = item("c:dyes/magenta").add(Items.MAGENTA_DYE);
    public static final ItemTagDef DYES_LIGHT_BLUE = item("c:dyes/light_blue").add(Items.LIGHT_BLUE_DYE);
    public static final ItemTagDef DYES_YELLOW     = item("c:dyes/yellow").add(Items.YELLOW_DYE);
    public static final ItemTagDef DYES_LIME       = item("c:dyes/lime").add(Items.LIME_DYE);
    public static final ItemTagDef DYES_PINK       = item("c:dyes/pink").add(Items.PINK_DYE);
    public static final ItemTagDef DYES_GRAY       = item("c:dyes/gray").add(Items.GRAY_DYE);
    public static final ItemTagDef DYES_LIGHT_GRAY = item("c:dyes/light_gray").add(Items.LIGHT_GRAY_DYE);
    public static final ItemTagDef DYES_CYAN       = item("c:dyes/cyan").add(Items.CYAN_DYE);
    public static final ItemTagDef DYES_PURPLE     = item("c:dyes/purple").add(Items.PURPLE_DYE);
    public static final ItemTagDef DYES_BLUE       = item("c:dyes/blue").add(Items.BLUE_DYE);
    public static final ItemTagDef DYES_BROWN      = item("c:dyes/brown").add(Items.BROWN_DYE);
    public static final ItemTagDef DYES_GREEN      = item("c:dyes/green").add(Items.GREEN_DYE);
    public static final ItemTagDef DYES_RED        = item("c:dyes/red").add(Items.RED_DYE);
    public static final ItemTagDef DYES_BLACK      = item("c:dyes/black").add(Items.BLACK_DYE);

    public static final ItemTagDef DYES_ALL = item("c:dyes")
            .addTag(DYES_WHITE.key())
            .addTag(DYES_ORANGE.key())
            .addTag(DYES_MAGENTA.key())
            .addTag(DYES_LIGHT_BLUE.key())
            .addTag(DYES_YELLOW.key())
            .addTag(DYES_LIME.key())
            .addTag(DYES_PINK.key())
            .addTag(DYES_GRAY.key())
            .addTag(DYES_LIGHT_GRAY.key())
            .addTag(DYES_CYAN.key())
            .addTag(DYES_PURPLE.key())
            .addTag(DYES_BLUE.key())
            .addTag(DYES_BROWN.key())
            .addTag(DYES_GREEN.key())
            .addTag(DYES_RED.key())
            .addTag(DYES_BLACK.key());

}
