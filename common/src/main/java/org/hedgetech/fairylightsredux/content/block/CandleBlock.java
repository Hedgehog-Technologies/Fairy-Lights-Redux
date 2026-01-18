package org.hedgetech.fairylightsredux.content.block;

import net.minecraft.world.level.block.Block;

public final class CandleBlock extends Block {
    private final int lightLevel;

    public CandleBlock(Properties props, int lightLevel) {
        super(props.lightLevel(state -> lightLevel));
        this.lightLevel = lightLevel;
    }
}
