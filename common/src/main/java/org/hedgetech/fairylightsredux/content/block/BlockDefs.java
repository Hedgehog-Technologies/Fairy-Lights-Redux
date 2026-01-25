package org.hedgetech.fairylightsredux.content.block;

import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.List;

public final class BlockDefs {
    public static final String FASTENER = "fastener";
    public static final String LIGHT = "light";

    public static final List<BlockDef> BLOCKS = List.of(
//            new BlockDef(
//                    "blue_candle",
//                    RecipeCategory.DECORATIONS,
//                    () -> defaultProperties().strength(0.1F).sound(SoundType.CANDLE),
//                    (props, def) -> {
//                        CandleData d = BlockData.requireData(def, CandleData.class);
//                        return new CandleBlock(props, d.lightLevel());
//                    },
//                    (block, props, def) -> new BlockItem(block, props),
//                    new CandleData(7)
//            )
    );

    private static BlockBehaviour.Properties defaultProperties() {
        return BlockBehaviour.Properties.of();
    }

    private BlockDefs() {}
}
