package org.hedgetech.fairylightsredux.content.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class BlockTagDef extends TagDef<Block, BlockTagDef> {
    public BlockTagDef(ResourceLocation id) {
        super(TagKey.create(Registries.BLOCK, id));
    }

    public BlockTagDef add(Block block) {
        return add(() -> block);
    }
}
