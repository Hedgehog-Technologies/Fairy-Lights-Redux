package org.hedgetech.fairylightsredux.util;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.locale.Language;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import java.util.Objects;

public final class Utils {
    public static <E extends Enum<E>> E getEnumValue(final Class<E> clazz, final int ordinal) {
        final E[] values = Objects.requireNonNull(clazz, "clazz").getEnumConstants();
        return values[ordinal < 0 || ordinal >= values.length ? 0 : ordinal];
    }

    public static Component formatRecipeTooltip(final String key) {
        return formatRecipeTooltipValue(Language.getInstance().getOrDefault(key));
    }

    private static Component formatRecipeTooltipValue(final String value) {
        return Component.translatable("recipe.ingredient.tooltip", value);
    }

    public static boolean impliesNbt(@Nullable Tag antecedent, @Nullable Tag consequent) {
        if (antecedent == consequent) return true;
        if ((antecedent == null) != (consequent == null)) return false;
        if (!antecedent.getClass().equals(consequent.getClass())) return false;
        if (antecedent instanceof CompoundTag act) {
            for (String key : act.keySet()) {
                if (!impliesNbt(act.get(key), ((CompoundTag) consequent).get(key))) {
                    return false;
                }
            }
            return true;
        }
        return antecedent.equals(consequent);
    }

    public static boolean impliesComponents(@Nullable DataComponentMap antecedent, @Nullable DataComponentMap consequent) {
        if (antecedent == consequent) return true;
        if ((antecedent == null) != (consequent == null)) return false;

        for (DataComponentType<?> type : antecedent.keySet()) {
            if (!consequent.has(type)) return false;

            Object a = antecedent.get(type);
            Object b = consequent.get(type);

            if (a == null || b == null) {
                if (a != b) return false;
                continue;
            }

            if (!a.equals(b)) return false;
        }

        return true;
    }

    private Utils() {}
}
