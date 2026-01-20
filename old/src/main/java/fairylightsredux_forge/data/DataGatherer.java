package org.hedgetech.fairylightsredux.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public final class DataGatherer {
    public static void onGatherData(final GatherDataEvent event) {
        final DataGenerator gen = event.getGenerator();
        final PackOutput packOutput = gen.getPackOutput();
        final CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        gen.addProvider(event.includeServer(), new ForgeFLRRecipeProvider.Runner(packOutput, lookupProvider));
        gen.addProvider(event.includeServer(), new ForgeFLRLootTableProvider(packOutput, lookupProvider));
    }
}
