package org.hedgetech.fairylightsredux.content.tag;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class TagDef<T, SELF extends TagDef<T, SELF>> {
    private final ResourceLocation id;
    private final TagKey<T> key;
    private final List<TagKey<T>> nested = new ArrayList<>();
    private final List<Supplier<T>> entries = new ArrayList<>();

    protected TagDef(ResourceKey<Registry<T>> registry, ResourceLocation id) {
        this.id = id;
        this.key = TagKey.create(registry, id);
    }

    @SuppressWarnings("unchecked")
    protected SELF self() {
        return (SELF) this;
    }

    public ResourceLocation id() {
        return this.id;
    }

    public TagKey<T> key() {
        return this.key;
    }

    public SELF add(Supplier<T> entry) {
        this.entries.add(entry);
        return self();
    }

    public SELF addTag(TagKey<T> tag) {
        this.nested.add(tag);
        return self();
    }

    public List<Supplier<T>> entries() {
        return this.entries;
    }

    public List<TagKey<T>> nested() {
        return this.nested;
    }
}
