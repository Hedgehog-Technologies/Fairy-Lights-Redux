package org.hedgetech.fairylightsredux.server.block;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.server.item.LightVariant;

import java.util.function.Supplier;

public final class FLRBlocks {
    public static final DeferredRegister<Block> REG = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MOD_ID);

    public static final RegistryObject<FastenerBlock>

    private static Supplier<LightBlock> createLight(final LightVariant<?> variant) {
        return createLight(variant, LightBlock::new);
    }

    private FLRBlocks() {}
}
