package org.hedgetech.fairylightsredux.server.connection;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.server.fastener.Fastener;
import org.hedgetech.fairylightsredux.server.feature.FeatureType;
import org.hedgetech.fairylightsredux.server.feature.light.Light;
import org.hedgetech.fairylightsredux.server.jingle.Jingle;
import org.hedgetech.fairylightsredux.server.jingle.JinglePlayer;
import org.hedgetech.fairylightsredux.server.string.StringType;
import org.hedgetech.fairylightsredux.server.string.StringTypes;

import javax.annotation.Nullable;
import java.util.*;

public final class HangingLightConnection extends HangingFeatureConnection<Light<?>> {
    private static final int MAX_LIGHT = 15;
    private static final int LIGHT_UPDATE_WAIT = 400;
    private static final int LIGHT_UPDATE_RATE = 10;

    private StringType string;
    private List<ItemStack> pattern;
    private JinglePlayer jinglePlayer = new JinglePlayer();
    private boolean wasPlaying = false;
    private boolean isOn = true;
    private int getLightUpdateTime = (int) (Math.random() * LIGHT_UPDATE_WAIT / 2);
    private int lightUpdateIndex;

    private final Set<BlockPos> litBlocks = new HashSet<>();
    private final Set<BlockPos> oldLitBlocks = new HashSet<>();

    public HangingLightConnection(final ConnectionType<? extends HangingLightConnection> type, final Level world, final Fastener<?> fastenerOrigin, final UUID uuid) {
        super(type, world, fastenerOrigin, uuid);
        this.string = StringTypes.BLACK_STRING.get();
        this.pattern = new ArrayList<>();
    }

    public StringType getString() {
        return this.string;
    }

    @Nullable
    public Jingle getPlayingJingle() {
        return this.jinglePlayer.getJingle();
    }

    public void play(final Jingle jingle, final int lightOffset) {
        this.jinglePlayer.play(jingle, lightOffset);
    }

    @Override
    public boolean interact(final Player player, final Vec3 hit, final FeatureType featureType, final int feature, final ItemStack heldStack, final InteractionHand hand) {
        if (featureType == FEATURE && heldStack.is())
    }
}
