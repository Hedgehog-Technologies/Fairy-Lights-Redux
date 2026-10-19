package org.hedgetech.fairylightsredux.server.jingle;

import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.hedgetech.fairylightsredux.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class JingleManager extends SimpleJsonResourceReloadListener<Jingle> {
    public static final ResourceLocation ID = Constants.id("jingles");
    public static final String CHRISTMAS = "christmas";
    public static final String HALLOWEEN = "halloween";
    public static final String RANDOM = "";
    public static final JingleManager INSTANCE = new JingleManager();

    private Map<String, JingleLibrary> libraries = Map.of();

    private JingleManager() {
        super(Jingle.CODEC, FileToIdConverter.json("jingles"));
    }

    public JingleLibrary get(final String library) {
        return this.libraries.getOrDefault(library, JingleLibrary.EMPTY);
    }

    @Override
    protected void apply(final Map<ResourceLocation, Jingle> jingles, final ResourceManager manager, final ProfilerFiller profiler) {
        final Map<String, List<Jingle>> grouped = new HashMap<>();

        jingles.forEach((id, jingle) -> {
            final int slash = id.getPath().indexOf('/');
            final String library = slash <0 ? RANDOM : id.getPath().substring(0, slash);
            grouped.computeIfAbsent(library, l -> new ArrayList<>()).add(jingle);
        });

        final Map<String, JingleLibrary> libraries = new HashMap<>();
        grouped.forEach((library, list) -> libraries.put(library, new JingleLibrary(list)));
        this.libraries = Map.copyOf(libraries);
    }
}
