package org.hedgetech.fairylightsredux.util;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class ItemUtils {
    // Adapted from Forge API Implementation
    public static void giveItemToPlayer(Player player, @NotNull ItemStack stack) {
        giveItemToPlayer(player, stack, -1);
    }

    public static void giveItemToPlayer(Player player, @NotNull ItemStack stack, int preferredSlot) {
        if (stack.isEmpty()) return;

        Inventory inventory = player.getInventory();
        ItemStack remainder = stack;
        if (preferredSlot >= 0 && preferredSlot < inventory.getContainerSize()) {
            remainder = invInsertItem(inventory, preferredSlot, stack, false);
        }

        if (!remainder.isEmpty()) {
            remainder = insertItemStacked(inventory, remainder, false);
        }

        if (remainder.isEmpty() || remainder.getCount() != stack.getCount()) {
            player.level().playSound(null, player.getX(), player.getY() + 0.5, player.getZ(),
                    SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((player.level().random.nextFloat() - player.level().random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
        }

        if (!remainder.isEmpty() && !player.level().isClientSide()) {
            ItemEntity entityItem = new ItemEntity(player.level(), player.getX(), player.getY() + 0.5, player.getZ(), remainder);
            entityItem.setPickUpDelay(40);
            entityItem.setDeltaMovement(entityItem.getDeltaMovement().multiply(0, 1, 0));

            player.level().addFreshEntity(entityItem);
        }
    }

    public static ItemStack insertItem(@NotNull Inventory inv, @NotNull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) return stack;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            stack = invInsertItem(inv, i, stack, simulate);
            if (stack.isEmpty()) return ItemStack.EMPTY;
        }
        return stack;
    }

    public static ItemStack invInsertItem(@NotNull Inventory inv, int slot, @NotNull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) return ItemStack.EMPTY;

        ItemStack stackInSlot = inv.getItem(slot);

        int m;
        if (!stackInSlot.isEmpty()) {
            if (stackInSlot.getCount() >= Math.min(stackInSlot.getMaxStackSize(), inv.getMaxStackSize())) return stack;
            if (!ItemStack.isSameItemSameComponents(stack, stackInSlot)) return stack;
            if (!inv.canPlaceItem(slot, stack)) return stack;

            m = Math.min(stack.getMaxStackSize(), inv.getMaxStackSize()) - stackInSlot.getCount();

            if (stack.getCount() <= m) {
                if (!simulate) {
                    ItemStack copy = stack.copy();
                    copy.grow(stackInSlot.getCount());
                    inv.setItem(slot, copy);
                    inv.setChanged();
                }

                return ItemStack.EMPTY;
            } else {
                stack = stack.copy();
                if (!simulate) {
                    ItemStack copy = stack.split(m);
                    copy.grow(stackInSlot.getCount());
                    inv.setItem(slot, copy);
                    inv.setChanged();
                    return stack;
                } else {
                    stack.shrink(m);
                    return stack;
                }
            }
        } else {
            if (!inv.canPlaceItem(slot, stack)) return stack;

            m = Math.min(stack.getMaxStackSize(), inv.getMaxStackSize());
            if (m < stack.getCount()) {
                stack = stack.copy();
                if (!simulate) {
                    inv.setItem(slot, stack.split(m));
                    inv.setChanged();
                    return stack;
                } else {
                    stack.shrink(m);
                    return stack;
                }
            } else {
                if (!simulate) {
                    inv.setItem(slot, stack);
                    inv.setChanged();
                }
                return ItemStack.EMPTY;
            }
        }
    }

    public static ItemStack insertItemStacked(@NotNull Inventory inv, @NotNull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) return stack;

        if (!stack.isStackable()) return insertItem(inv, stack, simulate);

        int sizeInv = inv.getContainerSize();

        for (int i = 0; i < sizeInv; i++) {
            ItemStack slot = inv.getItem(i);
            if (ItemStack.isSameItemSameComponents(slot, stack)) {
                stack = invInsertItem(inv, i, stack, simulate);

                if (stack.isEmpty()) {
                    break;
                }
            }
        }

        if (!stack.isEmpty()) {
            for (int i = 0; i < sizeInv; i++) {
                if (inv.getItem(i).isEmpty()) {
                    stack = invInsertItem(inv, i, stack, simulate);
                    if (stack.isEmpty()) {
                        break;
                    }
                }
            }
        }

        return stack;
    }

    private ItemUtils() {}
}
