package org.hedgetech.fairylightsredux.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.client.gui.EditLetteredConnectionScreen;
import org.hedgetech.fairylightsredux.network.PickConnectionPayload;
import org.hedgetech.fairylightsredux.platform.Services;
import org.hedgetech.fairylightsredux.server.collision.Intersection;
import org.hedgetech.fairylightsredux.server.connection.Connection;
import org.hedgetech.fairylightsredux.server.connection.ILettered;
import org.hedgetech.fairylightsredux.server.connection.PlayerAction;
import org.hedgetech.fairylightsredux.server.entity.FenceFastenerEntity;
import org.hedgetech.fairylightsredux.server.fastener.Fastener;
import org.hedgetech.fairylightsredux.server.fastener.FastenerType;
import org.hedgetech.fairylightsredux.server.fastener.IFastenerHolder;

import java.util.ArrayList;
import java.util.List;

public final class ConnectionHitHandler {
    private static Hit hit;

    public static Hit getHit() {
        return hit;
    }

    public static void update(final float partialTick) {
        hit = null;
        final Minecraft mc = Minecraft.getInstance();
        final Entity viewer = mc.getCameraEntity();
        if (mc.hitResult == null || mc.level == null || mc.player == null || viewer == null) return;

        final Vec3 origin = viewer.getEyePosition(partialTick);
        final Vec3 look = viewer.getViewVector(partialTick);
        final Vec3 end = origin.add(look.scale(mc.player.blockInteractionRange()));
        final Hit result = find(mc.level, origin, end);

        if (result != null && result.intersection().result().distanceTo(origin) < mc.hitResult.getLocation().distanceTo(origin)) {
            hit = result;
            final Vec3 location = result.intersection().result();
            mc.hitResult = BlockHitResult.miss(location, Direction.getApproximateNearest(look.x, look.y, look.z), BlockPos.containing(location));
            mc.crosshairPickEntity = null;
        }
    }

    public static boolean attack(final Player player) {
        return processAction(player, PlayerAction.ATTACK);
    }

    public static boolean use(final Player player) {
        return processAction(player, PlayerAction.INTERACT);
    }

    public static boolean pick() {
        if (hit == null) return false;
        Services.PLATFORM.sendToServer(new PickConnectionPayload(hit.connection().saveFastener(), hit.connection().getUUID(), hit.intersection().result()));
        return true;
    }

    private static boolean processAction(final Player player, final PlayerAction action) {
        if (hit == null) return false;

        if (action == PlayerAction.INTERACT && player.isSecondaryUseActive() && hit.connection() instanceof ILettered) {
            EditLetteredConnectionScreen.open(hit.connection());
            return true;
        }

        hit.connection().processClientAction(player, action, hit.intersection());
        return true;
    }

    private static Hit find(final Level world, final Vec3 origin, final Vec3 end) {
        final AABB bounds = new AABB(BlockPos.containing(origin)).inflate(Connection.MAX_LENGTH + 1.0D);
        Hit found = null;
        double distance = Double.MAX_VALUE;

        for (final Fastener<?> fastener : collectFasteners(world, bounds)) {
            for (final Connection connection : fastener.getOwnConnections()) {
                if (connection.getDestination().getType() == FastenerType.PLAYER) continue;

                final Intersection intersection = connection.getCollision().intersect(origin, end);
                if (intersection != null) {
                    final double dist = intersection.result().distanceTo(origin);

                    if (dist < distance) {
                        distance = dist;
                        found = new Hit(connection, intersection);
                    }
                }
            }
        }

        return found;
    }

    private static List<Fastener<?>> collectFasteners(final Level world, final AABB bounds) {
        final List<Fastener<?>> fasteners = new ArrayList<>();
        for (final FenceFastenerEntity entity : world.getEntitiesOfClass(FenceFastenerEntity.class, bounds)) {
            fasteners.add(entity.getFastener());
        }

        final int minX = Mth.floor(bounds.minX / 16.0D);
        final int maxX = Mth.ceil(bounds.maxX / 16.0D);
        final int minZ = Mth.floor(bounds.minZ / 16.0D);
        final int maxZ = Mth.ceil(bounds.maxZ / 16.0D);

        for (int x = minX; x < maxX; x++) {
            for (int z = minZ; z < maxZ; z++) {
                final LevelChunk chunk = world.getChunkSource().getChunkNow(x, z);
                if (chunk == null) continue;

                for (final BlockEntity entity : chunk.getBlockEntities().values()) {
                    if (entity instanceof final IFastenerHolder holder && bounds.contains(holder.getFastener().getConnectionPoint())) {
                        fasteners.add(holder.getFastener());
                    }
                }
            }
        }

        return fasteners;
    }

    public record Hit(Connection connection, Intersection intersection) {}

    private ConnectionHitHandler() {}
}
