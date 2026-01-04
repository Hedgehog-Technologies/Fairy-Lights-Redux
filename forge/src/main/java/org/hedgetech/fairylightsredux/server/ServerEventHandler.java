package org.hedgetech.fairylightsredux.server;

import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.server.capability.CapabilityHandler;
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
import net.minecraft.world.entity.decoration.BlockAttachedEntity;
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
import net.minecraftforge.common.util.Result;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.NoteBlockEvent;
import net.minecraftforge.eventbus.api.listener.Priority;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;

public final class ServerEventHandler {
    @SubscribeEvent
    public void onEntityJoinWorld(final EntityJoinLevelEvent event) {
        final Entity entity = event.getEntity();
        if (entity instanceof Player || entity instanceof FenceFastenerEntity) {
            entity.getCapability(CapabilityHandler.FASTENER_CAP).ifPresent(f -> f.setWorld(event.getLevel()));
        }
    }

    @SubscribeEvent
    public void onAttachEntityCapability(final AttachCapabilitiesEvent<Entity> event) {
        final Entity entity = event.getObject();
        if (entity instanceof Player pe) {
            event.addCapability(CapabilityHandler.FASTENER_ID, new PlayerFastener(pe));
        } else if (entity instanceof FenceFastenerEntity ffe) {
            event.addCapability(CapabilityHandler.FASTENER_ID, new FenceFastener(ffe));
        }
    }

    @SubscribeEvent
    public void onAttachBlockEntityCapability(final AttachCapabilitiesEvent<BlockEntity> event) {
        final BlockEntity entity = event.getObject();
        if (entity instanceof FastenerBlockEntity fbe) {
            event.addCapability(CapabilityHandler.FASTENER_ID, new BlockFastener(fbe, ServerProxy.buildBlockView()));
        }
    }

    @SubscribeEvent
    public void onTick(final TickEvent.PlayerTickEvent.Post event) {
        event.player.getCapability(CapabilityHandler.FASTENER_CAP).ifPresent(fastener -> {
            if (fastener.update() && !event.player.level().isClientSide()) {
                ServerProxy.sendToPlayerWatchingEntity(new UpdateEntityFastenerMessage(event.player, fastener.serializeNBT()), event.player);
            }
        });
    }

    @SubscribeEvent(priority = Priority.LOWEST)
    public boolean onNoteBlockPlay(final NoteBlockEvent.Play event) {
        final Level world = (Level) event.getLevel();
        final BlockPos pos = event.getPos();
        final Block noteBlock = world.getBlockState(pos).getBlock();
        final BlockState below = world.getBlockState(pos.below());
        if (below.getBlock() == FLRBlocks.FASTENER.get() && below.getValue(FastenerBlock.FACING) == Direction.DOWN) {
            final int note = event.getVanillaNoteId();
            final float pitch = (float) Math.pow(2, (note - 12) / 12D);
            world.playSound(null, pos, FLRSounds.JINGLE_BELL.get(), SoundSource.RECORDS, 3, pitch);
            world.addParticle(ParticleTypes.NOTE, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, note / 24D, 0, 0);
            if (!world.isClientSide()) {
                // TODO - is packet sending needed here?
                final Packet<?> pkt = new ClientboundBlockEventPacket(pos, noteBlock, event.getInstrument().ordinal(), note);
                final PlayerList players = world.getServer().getPlayerList();
                players.broadcast(null, pos.getX(), pos.getY(), pos.getZ(), 64, world.dimension(), pkt);
            }
            return true;
        }
        return false;
    }

    @SubscribeEvent
    public boolean onRightClickBlock(final PlayerInteractEvent.RightClickBlock event) {
        final Level world = event.getLevel();
        final BlockPos pos = event.getPos();
        if (!(world.getBlockState(pos).getBlock() instanceof FenceBlock)) return false;
        final ItemStack stack = event.getItemStack();
        boolean checkHanging = stack.getItem() == Items.LEAD;
        final Player player = event.getEntity();
        if (event.getHand() == InteractionHand.MAIN_HAND) {
            final ItemStack offhandStack = player.getOffhandItem();
            if (offhandStack.getItem() instanceof ConnectionItem) {
                if (checkHanging) return true;
                event.setUseBlock(Result.DENY);
            }
        }
        if (!checkHanging && !world.isClientSide()) {
            final double range = 7D;
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
            final BlockAttachedEntity entity = FenceFastenerEntity.findHanging(world, pos);
            if (entity != null && !(entity instanceof LeashFenceKnotEntity)) {
                return true;
            }
        }
        return false;
    }

    public static boolean tryJingle(final Level world, final HangingLightsConnection hangingLights) {
        String lib;
        if (Constants.CHRISTMAS.isOccurringNow()) {
            lib = JingleLibrary.CHRISTMAS;
        } else if (Constants.HALLOWEEN.isOccurringNow()) {
            lib = JingleLibrary.HALLOWEEN;
        } else {
            lib = JingleLibrary.RANDOM;
        }
        return tryJingle(world, hangingLights, lib);
    }

    public static boolean tryJingle(final Level world, final HangingLightsConnection hangingLights, final String lib) {
        if (world.isClientSide()) return false;
        final Light<?>[] lights = hangingLights.getFeatures();
        final Jingle jingle = JingleManager.INSTANCE.get(lib).getRandom(world.random, lights.length);
        if (jingle != null) {
            final int lightOffset = lights.length / 2 - jingle.getRange() / 2;
            hangingLights.play(jingle, lightOffset);
            ServerProxy.sendToPlayersWatchingChunk(new JingleMessage(hangingLights, lightOffset, jingle), world, hangingLights.getFastener().getPos());
            return true;
        }
        return false;
    }
}
