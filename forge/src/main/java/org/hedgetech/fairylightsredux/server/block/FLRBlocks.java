package org.hedgetech.fairylightsredux.server.block;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.hedgetech.fairylightsredux.Constants;

import java.util.function.Supplier;

public final class FLRBlocks {
    public static final DeferredRegister<Block> REG = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MOD_ID);

    private static Supplier<LightBlock> createLight(final LightVariant<?> variant) {
        return createLight(variant, LightBlock::new);
    }

    private FLRBlocks() {}
}
