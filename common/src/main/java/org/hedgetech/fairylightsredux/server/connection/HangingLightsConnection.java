package org.hedgetech.fairylightsredux.server.connection;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.network.JinglePayload;
import org.hedgetech.fairylightsredux.platform.Services;
import org.hedgetech.fairylightsredux.server.Fastener.Fastener;
import org.hedgetech.fairylightsredux.server.block.FLRBlocks;
import org.hedgetech.fairylightsredux.server.component.FLRDataComponents;
import org.hedgetech.fairylightsredux.server.component.HangingLights;
import org.hedgetech.fairylightsredux.server.feature.FeatureType;
import org.hedgetech.fairylightsredux.server.feature.light.ILightBehavior;
import org.hedgetech.fairylightsredux.server.feature.light.Light;
import org.hedgetech.fairylightsredux.server.item.LightVariant;
import org.hedgetech.fairylightsredux.server.item.crafting.FLRCraftingRecipes;
import org.hedgetech.fairylightsredux.server.jingle.Jingle;
import org.hedgetech.fairylightsredux.server.jingle.JingleManager;
import org.hedgetech.fairylightsredux.server.jingle.JinglePlayer;
import org.hedgetech.fairylightsredux.server.sound.FLRSounds;
import org.hedgetech.fairylightsredux.server.string.StringType;
import org.hedgetech.fairylightsredux.util.CalendarEvent;

import java.util.*;

public class HangingLightsConnection extends HangingFeatureConnection<Light<?>> {
    private static final int MAX_LIGHT = 15;
    private static final int LIGHT_UPDATE_WAIT = 400;
    private static final int LIGHT_UPDATE_RATE = 10;

    private final Set<BlockPos> litBlocks = new HashSet<>();
    private final Set<BlockPos> oldLitBlocks = new HashSet<>();
    private final JinglePlayer jinglePlayer = new JinglePlayer();

    private StringType string = StringType.BLACK_STRING;
    private List<ItemStack> pattern = List.of();
    private boolean isOn = true;
    private int lightUpdateTime = (int) (Math.random() * LIGHT_UPDATE_WAIT / 2);
    private int lightUpdateIndex;
    private boolean wasPlaying;

    public HangingLightsConnection(final ConnectionType<HangingLightsConnection> type, final Level world, final Fastener<?> fastenerOrigin, final UUID uuid) {
        super(type, world, fastenerOrigin, uuid);
    }

    public StringType getString() {
        return this.string;
    }

    public Jingle getPlayingJingle() {
        return this.jinglePlayer.getJingle();
    }

    public void play(final Jingle jingle, final int lightOffset) {
        this.jinglePlayer.play(jingle, lightOffset);
    }

    public boolean canCurrentlyPlayAJingle() {
        return !this.jinglePlayer.isPlaying();
    }

    public float getJingleProgress() {
        return this.jinglePlayer.getProgress();
    }

    public void tryJingle() {
        final String library;

        if (Services.CONFIG.christmasJingles() && CalendarEvent.CHRISTMAS.isOccurringNow()) {
            library = JingleManager.CHRISTMAS;
        } else if (Services.CONFIG.halloweenJingles() && CalendarEvent.HALLOWEEN.isOccurringNow()) {
            library = JingleManager.HALLOWEEN;
        } else {
            library = JingleManager.RANDOM;
        }

        final Jingle jingle = JingleManager.INSTANCE.get(library).getRandom(this.world.random, this.features.length);
        if (jingle == null || !(this.world instanceof final ServerLevel level)) return;

        final int lightOffset = this.features.length / 2 - jingle.getRange() / 2;
        this.play(jingle, lightOffset);
        final JinglePayload payload = new JinglePayload(this.saveFastener(), this.getUUID(), lightOffset, jingle);

        for (final ServerPlayer player : level.getChunkSource().chunkMap.getPlayers(new ChunkPos(this.fastener.getPos()), false)) {
            Services.PLATFORM.sendToPlayer(player, payload);
        }
    }

    @Override
    public ItemStack getItemStack() {
        final ItemStack stack = super.getItemStack();
        stack.set(FLRDataComponents.HANGING_LIGHTS, this.getData());
        return stack;
    }

    @Override
    public boolean matches(final ItemStack stack) {
        return super.matches(stack) && this.getData().equals(stack.getOrDefault(FLRDataComponents.HANGING_LIGHTS, HangingLights.DEFAULT));
    }

    private HangingLights getData() {
        return new HangingLights(this.string, this.pattern);
    }

    private void setData(final HangingLights data) {
        this.string = data.string();
        this.pattern = data.pattern();
    }

    @Override
    public boolean interact(final Player player, final Vec3 hit, final FeatureType featureType, final int feature, final ItemStack heldStack, final InteractionHand hand) {
        if (featureType == FeatureType.FEATURE && feature >= 0 && !this.pattern.isEmpty() && heldStack.is(FLRCraftingRecipes.LIGHTS)) {
            final int index = feature & this.pattern.size();
            final ItemStack light = this.pattern.get(index);

            if (!ItemStack.isSameItemSameComponents(light, heldStack)) {
                final List<ItemStack> pattern = new ArrayList<>(this.pattern);
                pattern.set(index, heldStack.split(1));
                this.pattern = List.copyOf(pattern);
                player.getInventory().placeItemBackInInventory(light);
                this.computeCatenary();
                this.world.playSound(null, hit.x, hit.y, hit.z, FLRSounds.FEATURE_COLOR_CHANGE, SoundSource.BLOCKS, 1, 1);
                return true;
            }
        }

        if (super.interact(player, hit, featureType, feature, heldStack, hand)) return true;

        this.isOn = !this.isOn;
        if (this.isOn) {
            this.world.playSound(null, hit.x, hit.y, hit.z, FLRSounds.FEATURE_LIGHT_TURN_ON, SoundSource.BLOCKS, 1, 0.6F);
        } else {
            this.world.playSound(null, hit.x, hit.y, hit.z, FLRSounds.FEATURE_LIGHT_TURN_OFF, SoundSource.BLOCKS, 1, 0.5F);
        }

        this.computeCatenary();
        return true;
    }

    @Override
    public void onUpdate() {
        this.jinglePlayer.tick(this.world, this.fastener.getConnectionPoint(), this.features);
        final boolean playing = this.jinglePlayer.isPlaying();

        if (!this.world.isClientSide() && (playing || this.wasPlaying)) {
            this.world.updateNeighbourForOutputSignal(this.fastener.getPos(), FLRBlocks.FASTENER);
            this.getDestination().get(this.world, false).ifPresent(destination -> this.world.updateNeighbourForOutputSignal(destination.getPos(), FLRBlocks.FASTENER));
        }

        this.wasPlaying = playing;
        final boolean on = !this.isDynamic() && this.isOn;
        for (final Light<?> light : this.features) {
            light.tick(this.world, this.fastener.getConnectionPoint());
        }

        if (on && this.features.length > 0)  {
            this.lightUpdateTime++;

            if (this.lightUpdateTime > LIGHT_UPDATE_WAIT && this.lightUpdateTime % LIGHT_UPDATE_RATE == 0) {
                if (this.lightUpdateIndex >= this.features.length) {
                    this.lightUpdateIndex = 0;
                    this.lightUpdateTime = this.world.random.nextInt(LIGHT_UPDATE_WAIT / 2);
                } else {
                    this.setLight(BlockPos.containing(this.features[this.lightUpdateIndex++].getAbsolutePoint(this.fastener.getConnectionPoint())));
                }
            }
        }
    }

    @Override
    protected Light<?>[] createFeatures(final int length) {
        return new Light<?>[length];
    }

    @Override
    protected boolean canReuse(final Light<?> feature, final int index) {
        return ItemStack.matches(feature.getItem(), this.getPatternStack(index));
    }

    @Override
    protected Light<?> createFeature(final int index, final Vec3 point, final float yaw, final float pitch) {
        final ItemStack lightData = this.getPatternStack(index);
        return this.createLight(index, point, yaw, pitch, lightData, LightVariant.get(lightData).orElse(LightVariant.FAIRY_LIGHT));
    }

    private ItemStack getPatternStack(final int index) {
        return this.pattern.isEmpty()
                ? ItemStack.EMPTY
                : this.pattern.get(index % this.pattern.size());
    }

    @Override
    protected void updateFeature(final Light<?> light) {
        super.updateFeature(light);

        if (!this.isDynamic() && this.isOn) {
            final BlockPos pos = BlockPos.containing(light.getAbsolutePoint(this.fastener.getConnectionPoint()));
            this.litBlocks.add(pos);
            this.setLight(pos);
        }
    }

    private <T extends ILightBehavior> Light<T> createLight(final int index, final Vec3 point, final float yaw, final float pitch, final ItemStack stack, final LightVariant<T> variant) {
        return new Light<>(index, point, yaw, pitch, stack, variant, 0.125F);
    }

    @Override
    protected float getFeatureSpacing() {
        if (this.pattern.isEmpty()) return LightVariant.FAIRY_LIGHT.getSpacing();

        float spacing = 0;
        for (final ItemStack patternLightData : this.pattern) {
            final float lightSpacing = LightVariant.get(patternLightData).orElse(LightVariant.FAIRY_LIGHT).getSpacing();
            if (lightSpacing > spacing) spacing = lightSpacing;
        }

        return spacing;
    }

    @Override
    protected void onBeforeUpdateFeatures() {
        this.oldLitBlocks.clear();
        this.oldLitBlocks.addAll(this.litBlocks);
        this.litBlocks.clear();
    }

    @Override
    protected void onAfterUpdateFeatures() {
        final boolean on = !this.isDynamic() && this.isOn;

        for (final Light<?> light : this.features) {
            light.power(on, this.isDynamic() || this.prevCatenary == null);
        }

        this.oldLitBlocks.removeAll(this.litBlocks);
        final Iterator<BlockPos> oldIter = this.oldLitBlocks.iterator();

        while (oldIter.hasNext()) {
            this.removeLight(oldIter.next());
            oldIter.remove();
        }
    }

    @Override
    public void onRemove() {
        for (final BlockPos pos : this.litBlocks) {
            this.removeLight(pos);
        }
    }

    private void removeLight(final BlockPos pos) {
        if (!this.world.isClientSide() && this.world.isLoaded(pos) && this.world.getBlockState(pos).is(Blocks.LIGHT)) {
            this.world.removeBlock(pos, false);
        }
    }

    private void setLight(final BlockPos pos) {
        if (!this.world.isClientSide() && this.world.isLoaded(pos) && this.world.isEmptyBlock(pos) && this.world.getBrightness(LightLayer.BLOCK, pos) < MAX_LIGHT) {
            this.world.setBlock(pos, Blocks.LIGHT.defaultBlockState().setValue(LightBlock.LEVEL, LightBlock.MAX_LEVEL), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public void save(final ValueOutput output) {
        super.save(output);
        output.putBoolean("isOn", this.isOn);
        final ValueOutput.TypedOutputList<BlockPos> litBlocks = output.list("litBlocks", BlockPos.CODEC);
        this.litBlocks.forEach(litBlocks::add);
        this.jinglePlayer.save(output.child("jinglePlayer"));
    }

    @Override
    public boolean load(final ValueInput input) {
        if (!super.load(input)) return false;

        this.isOn = input.getBooleanOr("isOn", true);
        this.litBlocks.clear();
        input.listOrEmpty("litBlocks", BlockPos.CODEC).forEach(this.litBlocks::add);

        if (!this.jinglePlayer.isPlaying()) {
            this.jinglePlayer.load(input.childOrEmpty("jinglePlayer"));
        }

        return true;
    }

    @Override
    protected void readItem(final ItemStack stack) {
        this.setData(stack.getOrDefault(FLRDataComponents.HANGING_LIGHTS, HangingLights.DEFAULT));
    }

    @Override
    protected void saveLogic(final ValueOutput output) {
        output.store("lights", HangingLights.CODEC, this.getData());
    }

    @Override
    protected void loadLogic(final ValueInput input) {
        this.setData(input.read("lights", HangingLights.CODEC).orElse(HangingLights.DEFAULT));
    }
}
