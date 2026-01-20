package org.hedgetech.fairylightsredux.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.packs.VanillaLootTableProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class ForgeFLRLootTableProvider extends LootTableProvider {
    public ForgeFLRLootTableProvider(PackOutput pack, CompletableFuture<HolderLookup.Provider> lookup) {
        super(pack, Set.of(), VanillaLootTableProvider.create(pack, lookup).getTables(), lookup);
    }
}
