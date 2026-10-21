package org.hedgetech.fairylightsredux.server.connection;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.hedgetech.fairylightsredux.server.fastener.Fastener;
import org.hedgetech.fairylightsredux.server.item.DyeableItem;

import java.util.UUID;

public final class GarlandTinselConnection extends Connection {
    private static final int DEFAULT_COLOR = DyeableItem.getColor(DyeColor.LIGHT_GRAY);
    private int color = DEFAULT_COLOR;

    public GarlandTinselConnection(final ConnectionType<GarlandTinselConnection> type, final Level world, final Fastener<?> fastener, final UUID uuid) {
        super(type, world, fastener, uuid);
    }

    public int getColor() {
        return this.color;
    }

    @Override
    public float getRadius() {
        return 0.125F;
    }

    @Override
    public ItemStack getItemStack() {
        return DyeableItem.setColor(super.getItemStack(), this.color);
    }

    @Override
    public boolean matches(final ItemStack stack) {
        return super.matches(stack) && DyeableItem.getColor(stack) == this.color;
    }

    @Override
    protected void readItem(final ItemStack stack) {
        this.color = DyeableItem.getColor(stack);
    }

    @Override
    protected void saveLogic(final ValueOutput output) {
        output.putInt("color", this.color);
    }

    @Override
    protected void loadLogic(final ValueInput input) {
        this.color = input.getIntOr("color", DEFAULT_COLOR);
    }
}
