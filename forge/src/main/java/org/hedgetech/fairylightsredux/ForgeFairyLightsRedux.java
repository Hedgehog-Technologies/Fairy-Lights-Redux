package org.hedgetech.fairylightsredux;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.common.MinecraftForge;
import org.hedgetech.fairylightsredux.migration.LegacyMigration;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;

@Mod(Constants.MOD_ID)
public class ForgeFairyLightsRedux {

    public ForgeFairyLightsRedux() {

        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.

        // Use Forge to bootstrap the Common mod.
        Constants.LOG.info("Hello Forge world!");
        CommonClass.init();

        // Register runtime listeners such as legacy migration handlers on the Forge event bus
        MinecraftForge.EVENT_BUS.register(new LegacyMigration());

    }

    // Minimal no-op helper used by ServerEventHandler while migration is in progress.
    public static void sendToPlayersWatchingEntity(final Object message, final Entity entity) {
        // no-op placeholder for incremental migration
    }

    public static void sendToPlayersWatchingChunk(final Object message, final Level world, final BlockPos pos) {
        // no-op placeholder for incremental migration
    }
}