package org.hedgetech.fairylightsredux.content.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;

import java.util.concurrent.CompletableFuture;

public abstract class BaseTagProvider<T> extends TagsProvider<T> {
    protected BaseTagProvider(PackOutput output, ResourceKey<? extends Registry<T>> registry, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, registry, lookup);
    }

    protected TagAppender<T> tag(TagKey<T> key) {
        return new TagAppender<>(getOrCreateRawBuilder(key));
    }

    protected static final class TagAppender<T> {
        private final TagBuilder builder;

        TagAppender(TagBuilder builder) {
            this.builder = builder;
        }

        public TagAppender<T> add(ResourceLocation id) {
            builder.add(TagEntry.element(id));
            return this;
        }

        public TagAppender<T> add(ResourceKey<T> key) {
            builder.add(TagEntry.element(key.location()));
            return this;
        }

        public TagAppender<T> addTag(TagKey<T> tag) {
            builder.add(TagEntry.tag(tag.location()));
            return this;
        }
    }
}
