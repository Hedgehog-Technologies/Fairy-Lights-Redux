package org.hedgetech.fairylightsredux.content.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class FLRItemTagProvider extends BaseTagProvider<Item> {
    public FLRItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, Registries.ITEM, lookup);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider lookup) {
        for (TagDef<?, ?> raw : TagDefs.all()) {
            if (!(raw instanceof ItemTagDef def)) continue;

            BaseTagProvider.TagAppender<Item> builder = tag(def.key());

            for (Supplier<Item> entry : def.entries()) {
                ResourceLocation id = BuiltInRegistries.ITEM.getKey(entry.get());
                builder.add(id);
            }

            for (TagKey<Item> nested : def.nested()) {
                builder.addTag(nested);
            }
        }
    }
}
