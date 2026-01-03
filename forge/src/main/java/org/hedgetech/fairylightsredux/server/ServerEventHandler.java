package org.hedgetech.fairylightsredux.server;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBlockEventPacket;
import net.minecraft.server.players.PlayerList;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import org.hedgetech.fairylightsredux.ForgeFairyLightsRedux;
import org.hedgetech.fairylightsredux.components.FastenerComponent;
import org.hedgetech.fairylightsredux.components.Fasteners;
import org.hedgetech.fairylightsredux.server.item.ConnectionItem;
import org.hedgetech.fairylightsredux.server.block.FLBlocks;
import org.hedgetech.fairylightsredux.server.block.FastenerBlock;
import org.hedgetech.fairylightsredux.server.entity.FenceFastenerEntity;
import org.hedgetech.fairylightsredux.server.feature.light.Light;
import org.hedgetech.fairylightsredux.server.jingle.Jingle;
import org.hedgetech.fairylightsredux.server.jingle.JingleLibrary;
import org.hedgetech.fairylightsredux.server.jingle.JingleManager;
import org.hedgetech.fairylightsredux.server.net.clientbound.JingleMessage;
import org.hedgetech.fairylightsredux.server.net.clientbound.UpdateEntityFastenerMessage;
import org.hedgetech.fairylightsredux.server.sound.FLSounds;

public final class ServerEventHandler {

    @SubscribeEvent
    public void onEntityJoinWorld(final EntityJoinLevelEvent event) {
        final Entity entity = event.getEntity();
        if (entity instanceof Player || entity instanceof FenceFastenerEntity) {
            final FastenerComponent comp = Fasteners.getOrCreate(entity);
            if (comp != null) comp.migrateIfNeeded(entity);
        }
    }

    @SubscribeEvent
    public void onTick(final TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            final Player player = event.player;
            final FastenerComponent comp = Fasteners.getOrCreate(player);
            if (comp != null) {
                // The old fastener.update -> here we just migrate and always serialize if non-empty
                // This is a minimal compatibility behavior until FastenerComponent provides update semantics
                if (!player.level().isClientSide()) {
                    // send the component data to players watching the entity
                    ForgeFairyLightsRedux.sendToPlayersWatchingEntity(new UpdateEntityFastenerMessage(player, comp.save()), player);
                }
            }
        }
    }

    // Ported note-block and right-click logic from the original ServerEventHandler
    @SubscribeEvent
    public void onNoteBlockPlay(final net.minecraftforge.event.level.NoteBlockEvent.Play event) {
        final Level world = (Level) event.getLevel();
        final BlockPos pos = event.getPos();
        final Block noteBlock = world.getBlockState(pos).getBlock();
        final BlockState below = world.getBlockState(pos.below());
        if (below.getBlock() == FLBlocks.FASTENER.get() && below.getValue(FastenerBlock.FACING) == Direction.DOWN) {
            final int note = event.getVanillaNoteId();
            final float pitch = (float) Math.pow(2, (note - 12) / 12D);
            world.playSound(null, pos, FLSounds.JINGLE_BELL.get(), SoundSource.RECORDS, 3, pitch);
            world.addParticle(ParticleTypes.NOTE, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, note / 24D, 0, 0);
            if (!world.isClientSide()) {
                final Packet<?> pkt = new ClientboundBlockEventPacket(pos, noteBlock, event.getInstrument().ordinal(), note);
                final PlayerList players = world.getServer().getPlayerList();
                players.broadcast(null, pos.getX(), pos.getY(), pos.getZ(), 64, world.dimension(), pkt);
            }
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onRightClickBlock(final net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock event) {
        final Level world = event.getLevel();
        final BlockPos pos = event.getPos();
        if (!(world.getBlockState(pos).getBlock() instanceof FenceBlock)) {
            return;
        }
        final ItemStack stack = event.getItemStack();
        boolean checkHanging = stack.getItem() == Items.LEAD;
        final Player player = event.getEntity();
        if (event.getHand() == InteractionHand.MAIN_HAND) {
            final ItemStack offhandStack = player.getOffhandItem();
            if (offhandStack.getItem() instanceof ConnectionItem) {
                if (checkHanging) {
                    event.setCanceled(true);
                    return;
                } else {
                    event.setUseBlock(net.minecraftforge.eventbus.api.Event.Result.DENY);
                }
            }
        }
        if (!checkHanging && !world.isClientSide()) {
            final double range = 7;
            final int x = pos.getX();
            final int y = pos.getY();
            final int z = pos.getZ();
            final AABB area = new AABB(x - range, y - range, z - range, x + range, y + range, z + range);
            for (final Mob entity : world.getEntitiesOfClass(Mob.class, area)) {
                if (entity.isLeashed() && entity.getLeashHolder() == player) {
                    checkHanging = true;
                    break;
                }
            }
        }
        if (checkHanging) {
            final HangingEntity entity = FenceFastenerEntity.findHanging(world, pos);
            if (entity != null && !(entity instanceof LeashFenceKnotEntity)) {
                event.setCanceled(true);
            }
        }
    }

    public static boolean tryJingle(final Level world, final org.hedgetech.fairylightsredux.server.connection.HangingLightsConnection hangingLights) {
        String lib;
        if (ForgeFairyLightsRedux.CHRISTMAS.isOccurringNow()) {
            lib = JingleLibrary.CHRISTMAS;
        } else if (ForgeFairyLightsRedux.HALLOWEEN.isOccurringNow()) {
            lib = JingleLibrary.HALLOWEEN;
        } else {
            lib = JingleLibrary.RANDOM;
        }
        return tryJingle(world, hangingLights, lib);
    }

    public static boolean tryJingle(final Level world, final org.hedgetech.fairylightsredux.server.connection.HangingLightsConnection hangingLights, final String lib) {
        if (world.isClientSide()) return false;
        final Light<?>[] lights = hangingLights.getFeatures();
        final Jingle jingle = JingleManager.INSTANCE.get(lib).getRandom(world.random, lights.length);
        if (jingle != null) {
            final int lightOffset = lights.length / 2 - jingle.getRange() / 2;
            hangingLights.play(jingle, lightOffset);
            ForgeFairyLightsRedux.sendToPlayersWatchingChunk(new JingleMessage(hangingLights, lightOffset, jingle), world, hangingLights.getFastener().getPos());
            return true;
        }
        return false;
    }
}
