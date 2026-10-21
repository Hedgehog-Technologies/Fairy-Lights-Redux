package org.hedgetech.fairylightsredux.server.connection;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.server.fastener.Fastener;
import org.hedgetech.fairylightsredux.server.component.FLRDataComponents;
import org.hedgetech.fairylightsredux.server.component.PennantBunting;
import org.hedgetech.fairylightsredux.server.feature.FeatureType;
import org.hedgetech.fairylightsredux.server.feature.Pennant;
import org.hedgetech.fairylightsredux.server.item.crafting.FLRCraftingRecipes;
import org.hedgetech.fairylightsredux.server.sound.FLRSounds;
import org.hedgetech.fairylightsredux.util.styledstring.StyledString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class PennantBuntingConnection extends HangingFeatureConnection<Pennant> implements ILettered {
    private List<ItemStack> pattern = List.of();
    private StyledString text = StyledString.EMPTY;

    public PennantBuntingConnection(final ConnectionType<PennantBuntingConnection> type, final Level world, final Fastener<?> fastener, final UUID uuid) {
        super(type, world, fastener, uuid);
    }

    @Override
    public float getRadius() {
        return 0.045F;
    }

    @Override
    public ItemStack getItemStack() {
        final ItemStack stack = super.getItemStack();
        stack.set(FLRDataComponents.PENNANT_BUNTING, this.getData());
        return stack;
    }

    @Override
    public boolean matches(final ItemStack stack) {
        return super.matches(stack)
                && this.getData().equals(stack.getOrDefault(FLRDataComponents.PENNANT_BUNTING, PennantBunting.DEFAULT));
    }

    private PennantBunting getData() {
        return new PennantBunting(this.pattern, this.text);
    }

    private void setData(final PennantBunting data) {
        this.pattern = data.pattern();
        this.text = data.text();
    }

    @Override
    public boolean interact(final Player player, final Vec3 hit, final FeatureType featureType, final int feature, final ItemStack heldStack, final InteractionHand hand) {
        if (featureType == FeatureType.FEATURE && feature >= 0 && !this.pattern.isEmpty() && heldStack.is(FLRCraftingRecipes.PENNANTS)) {
            final int index = feature % this.pattern.size();
            final ItemStack pennant = this.pattern.get(index);

            if (!ItemStack.isSameItemSameComponents(pennant, heldStack)) {
                final List<ItemStack> pattern = new ArrayList<>(this.pattern);
                pattern.set(index, heldStack.split(1));
                this.pattern = List.copyOf(pattern);
                player.getInventory().placeItemBackInInventory(pennant);
                this.computeCatenary();
                this.world.playSound(null, hit.x, hit.y, hit.z, FLRSounds.FEATURE_COLOR_CHANGE, SoundSource.BLOCKS, 1, 1);
                return true;
            }
        }

        return super.interact(player, hit, featureType, feature, heldStack, hand);
    }

    @Override
    protected void onUpdate() {
        for (final Pennant pennant : this.features) {
            pennant.tick(this.world);
        }
    }

    @Override
    protected Pennant[] createFeatures(final int length) {
        return new Pennant[length];
    }

    @Override
    protected boolean canReuse(final Pennant feature, final int index) {
        return ItemStack.matches(feature.getItem(), this.getPatternStack(index));
    }

    @Override
    protected Pennant createFeature(final int index, final Vec3 point, final float yaw, final float pitch) {
        return new Pennant(index, point, yaw, pitch, this.getPatternStack(index));
    }

    private ItemStack getPatternStack(final int index) {
        return this.pattern.isEmpty()
                ? ItemStack.EMPTY
                : this.pattern.get(index % this.pattern.size());
    }

    @Override
    protected float getFeatureSpacing() {
        return 0.6875F;
    }

    @Override
    public boolean isSupportedText(final StyledString text) {
        return text.length() <= this.features.length
                && ILettered.super.isSupportedText(text);
    }

    @Override
    public void setText(final StyledString text) {
        this.text = text;
        this.computeCatenary();
    }

    @Override
    public StyledString getText() {
        return this.text;
    }

    @Override
    protected void readItem(final ItemStack stack) {
        this.setData(stack.getOrDefault(FLRDataComponents.PENNANT_BUNTING, PennantBunting.DEFAULT));
    }

    @Override
    protected void saveLogic(final ValueOutput output) {
        output.store("pennants", PennantBunting.CODEC, this.getData());
    }

    @Override
    protected void loadLogic(final ValueInput input) {
        this.setData(input.read("pennants", PennantBunting.CODEC).orElse(PennantBunting.DEFAULT));
    }
}
