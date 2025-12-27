package org.hedgetech.fairylightsredux.server.block.entity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.server.block.FLRBlocks;

public final class FLRBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> REG = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Constants.MOD_ID);

    public static final RegistryObject<BlockEntityType<FastenerBlockEntity>> FASTENER = REG.register("fastener", () -> new BlockEntityType(FastenerBlockEntity::new, FLRBlocks.FASTENER.get()));

    public static final RegistryObject<BlockEntityType<LightBlockEntity>> LIGHT = REG.register("light", () -> new BlockEntityType(LightBlockEntity::new,
            ))

    private FLRBlockEntities() {}
}
