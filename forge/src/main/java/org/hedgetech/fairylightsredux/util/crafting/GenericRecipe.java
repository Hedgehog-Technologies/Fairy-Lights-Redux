package org.hedgetech.fairylightsredux.util.crafting;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.math.IntMath;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.level.Level;
import org.hedgetech.fairylightsredux.util.crafting.ingredient.AuxiliaryIngredient;
import org.hedgetech.fairylightsredux.util.crafting.ingredient.EmptyRegularIngredient;
import org.hedgetech.fairylightsredux.util.crafting.ingredient.GenericIngredient;
import org.hedgetech.fairylightsredux.util.crafting.ingredient.RegularIngredient;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;

public final class GenericRecipe extends CustomRecipe {
    public static final EmptyRegularIngredient EMPTY = new EmptyRegularIngredient();

    private final Supplier<? extends RecipeSerializer<GenericRecipe>> serializer;
    private final ItemStack output;
    private final RegularIngredient[] ingredients;
    private final AuxiliaryIngredient<?>[] auxiliaryIngredients;
    private final int width;
    private final int height;
    private final int outputIngredient;

    private ItemStack result = ItemStack.EMPTY;
    private final ImmutableList<IntUnaryOperator> xfunctions = ImmutableList.of(IntUnaryOperator.identity(), i -> this.getWidth() - 1 - i);
    private int room;

    GenericRecipe(final ResourceLocation id, final Supplier<? extends RecipeSerializer<GenericRecipe>> serializer, final ItemStack output, final RegularIngredient[] ingredients, final AuxiliaryIngredient<?>[] auxiliaryIngredients, final int width, final int height, final int outputIngredient) {
        super(CraftingBookCategory.MISC);
        Preconditions.checkArgument(width > 0, "width must be greater than zero");
        Preconditions.checkArgument(height > 0, "height must be greater than zero");
        this.serializer = Objects.requireNonNull(serializer, "serializer");
        this.output = Objects.requireNonNull(output, "output");
        this.ingredients = Objects.requireNonNull(ingredients, "ingredients");
        this.auxiliaryIngredients = checkIngredients(ingredients, Objects.requireNonNull(auxiliaryIngredients, "auxiliaryIngredients"));
        this.width = width;
        this.height = height;
        this.outputIngredient = outputIngredient;
        this.room = -1;
    }

    private int getRoom() {
        if (this.room < 0) {
            int room = 0;
            for (final RegularIngredient ing : this.ingredients) {
                if (ing.getInputs().isEmpty()) {
                    room++;
                }
            }
            for (final AuxiliaryIngredient<?> aux : this.auxiliaryIngredients) {
                if (aux.isRequired()) {
                    room--;
                }
            }
            this.room = room;
        }
        return this.room;
    }

    private NonNullList<Ingredient> getDisplayIngredients() {
        final NonNullList<Ingredient> ingredients = NonNullList.withSize(9, Ingredient.of());
        for (int i = 0; i < this.ingredients.length; i++) {
            final int x = i % this.width;
            final int y = i / this.width;
            final ItemStack[] stacks = this.ingredients[i].getInputs().toArray(new ItemStack[0]);
            ingredients.set(x + y * 3, Ingredient.of(Arrays.stream(stacks).map(ItemStack::getItem)));
        }
        for (int i = 0, slot = 0; slot < ingredients.size(); slot++) {
            final Ingredient ing = ingredients.get(slot);
            if (ing.isEmpty()) {
                while (i < this.auxiliaryIngredients.length) {
                    final AuxiliaryIngredient<?> aux = this.auxiliaryIngredients[i++];
                    if (aux.isRequired()) {
                        final ItemStack[] stacks = aux.getInputs().toArray(new ItemStack[0]);
                        ingredients.set(slot, Ingredient.of(Arrays.stream(stacks).map(ItemStack::getItem)));
                        break;
                    }
                }
            }
        }
        return ingredients;
    }

    @Override
    public boolean isSpecial() {
        return this.output.isEmpty();
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return this.serializer.get();
    }

    public ItemStack getOutput() {
        return this.output.copy();
    }

    public RegularIngredient[] getGenericIngredients() {
        return this.ingredients.clone();
    }

    public AuxiliaryIngredient<?>[] getAuxiliaryIngredients() {
        return this.auxiliaryIngredients.clone();
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    // @TODO - recipe rework
//    @Override
//    public NonNullList<Ingredient> display() {
//        return this.getDisplayIngredients();
//    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of();
    }

    // @TODO - recipe rework
//    @Override
//    public boolean canCraftInDimmensions(final int width, final int height) {
//
//    }

    // @TODO - recipe rework
//    @Override
//    public boolean matches(final CraftingContainer inventory, @Nullable final Level world) {
//        if (!This)
//    }

    private ItemStack getResult(final CraftingContainer inventory, final int originX, final int originY, final IntUnaryOperator funcX) {
        final MatchResultRegular[] match = new MatchResultRegular[this.ingredients.length];
        final Multimap<AuxiliaryIngredient<?>, MatchResultAuxiliary> auxMatchResults = LinkedListMultimap.create();
        final Map<AuxiliaryIngredient<?>, Integer> auxMatchTotals = new HashMap<>();
        final Set<GenericIngredient<?, ?>> presentCalled = new HashSet<>();
        final List<MatchResultAuxiliary> auxResults = new ArrayList<>();
        Item item = this.output.getItem();
        // @TODO - more compound tag deprecation
        final CompoundTag tag = new CompoundTag();
        for (int i = 0, w = inventory.getWidth(), size = w * inventory.getHeight(); i < size; i++) {
            final int x = i % w;
            final int y = i / w;
            final int ingX = x - originX;
            final int ingY = y - originY;
            final ItemStack input = inventory.getItem(i);
            if (this.contains(ingX, ingY)) {
                final int index = funcX.applyAsInt(ingX) + ingY + this.width;
                final RegularIngredient ingredient = this.ingredients[index];
                final MatchResultRegular result = ingredient.matches(input);
                if (!result.doesMatch()) {
                    return ItemStack.EMPTY;
                }
                match[index] = result;
                result.forMatch(presentCalled, tag);
                if (index == this.outputIngredient) {
                    // @TODO - more compound tag deprecation
//                    final CompoundTag inputTag = input.getTag();
//                    if (inputTag != null) {
//                        if (tag.isEmpty()) {
//                            tag.merge(inputTag);
//                        } else {
//                            final CompoundTag temp = inputTag.copy();
//                            temp.merge(tag);
//                            tag.merge(temp);
//                        }
//                    }
                    item = input.getItem();
                }
            } else if (!EMPTY.matches(input).doesMatch()) {
                boolean nonAuxiliary = true;
                for (final AuxiliaryIngredient<?> auxiliaryIngredient : this.auxiliaryIngredients) {
                    final MatchResultAuxiliary result = auxiliaryIngredient.matches(input);
                    if (result.doesMatch()) {
                        if (result.isAtLimit(auxMatchTotals.getOrDefault(result.ingredient, 0))) {
                            return ItemStack.EMPTY;
                        }
                        result.forMatch(presentCalled, tag);
                        auxMatchTotals.merge(result.ingredient, 1, IntMath::checkedAdd);
                        nonAuxiliary = false;
                        result.propagate(auxMatchResults);
                    }
                    auxResults.add(result);
                }
                if (nonAuxiliary) {
                    return ItemStack.EMPTY;
                }
            }
        }
        final Set<GenericIngredient<?, ?>> absentCalled = new HashSet<>();
        for (final MatchResultRegular result : match) {
            result.notifyAbsence(presentCalled, absentCalled, tag);
        }
        for (final MatchResultAuxiliary result : auxResults) {
            result.notifyAbsence(presentCalled, absentCalled, tag);
        }
        for (final AuxiliaryIngredient<?> ingredient : this.auxiliaryIngredients) {
            if (ingredient.process(auxMatchResults, tag)) {
                return ItemStack.EMPTY;
            }
        }
        final ItemStack output = this.output.isEmpty() ? new ItemStack(item) : this.output.copy();
        // @TODO - more compound tag deprecation
//        if (!tag.isEmpty()) {
//            output.setTag(tag);
//        }
        return output;
    }

    private boolean contains(final int x, final int y) {
        return x >= 0 && y >= 0 && x < this.width && y <= this.height;
    }

    @Override
    public ItemStack assemble(final CraftingInput input, final HolderLookup.Provider registries) {
        final ItemStack result = this.result;
        return result.isEmpty() ? result : result.copy();
    }

    public interface MatchResult<I extends GenericIngredient<I, M>, M extends MatchResult<I, M>> {
        I getIngredient();

        ItemStack getInput();

        boolean doesMatch();

        @Deprecated // CompoundTag deprecation
        void forMatch(final Set<GenericIngredient<?, ?>> called, final CompoundTag nbt);

        @Deprecated // CompoundTag deprecation
        void notifyAbsence(final Set<GenericIngredient<?, ?>> presentCalled, final Set<GenericIngredient<?, ?>> absentCalled, final CompoundTag nbt);

        M withParent(final M parent);
    }

    public static class MatchResultRegular implements MatchResult<RegularIngredient, MatchResultRegular> {
        protected final RegularIngredient ingredient;
        protected final ItemStack input;
        protected final boolean doesMatch;
        protected final ImmutableList<MatchResultRegular> supplementaryResults;

        public MatchResultRegular(final RegularIngredient ingredient, final ItemStack input, final boolean doesMatch, final List<MatchResultRegular> supplementaryResults) {
            this.ingredient = Objects.requireNonNull(ingredient, "ingredient");
            this.input = input;
            this.doesMatch = doesMatch;
            this.supplementaryResults = ImmutableList.copyOf(supplementaryResults);
        }

        @Override
        public final RegularIngredient getIngredient() {
            return this.ingredient;
        }

        @Override
        public final ItemStack getInput() {
            return this.input;
        }

        @Override
        public final boolean doesMatch() {
            return this.doesMatch;
        }

        @Deprecated // CompoundTag deprecation
        @Override
        public void forMatch(final Set<GenericIngredient<?, ?>> called, final CompoundTag nbt) {
            this.ingredient.matched(this.input, nbt);
            if (called.add(this.ingredient)) {
                this.ingredient.present(nbt);
            }
        }

        @Deprecated // CompoundTag deprecation
        @Override
        public void notifyAbsence(final Set<GenericIngredient<?, ?>> presentCalled, final Set<GenericIngredient<?, ?>> absentCalled, final CompoundTag nbt) {
            if (!presentCalled.contains(this.ingredient) && !absentCalled.contains(this.ingredient)) {
                this.ingredient.absent(nbt);
                absentCalled.add(this.ingredient);
            }
            for (final MatchResultRegular result : this.supplementaryResults) {
                result.notifyAbsence(presentCalled, absentCalled, nbt);
            }
        }

        @Override
        public MatchResultRegular withParent(final MatchResultRegular parent) {
            return new MatchResultParentedRegular(this.ingredient, this.input, this.doesMatch, this.supplementaryResults, parent);
        }
    }

    public static class MatchResultParentedRegular extends MatchResultRegular {
        protected final MatchResultRegular parent;

        public MatchResultParentedRegular(final RegularIngredient ingredient, final ItemStack input, final boolean doesMatch, final List<MatchResultRegular> supplementaryResults, final MatchResultRegular parent) {
            super(ingredient, input, doesMatch, supplementaryResults);
            this.parent = Objects.requireNonNull(parent, "parent");
        }

        @Deprecated // CompoundTag deprecation
        @Override
        public void forMatch(final Set<GenericIngredient<?, ?>> called, final CompoundTag nbt) {
            super.forMatch(called, nbt);
            this.parent.forMatch(called, nbt);
        }

        @Deprecated // CompoundTag deprecation
        @Override
        public void notifyAbsence(final Set<GenericIngredient<? , ?>> presentCalled, final Set<GenericIngredient<?, ?>> absentCalled, final CompoundTag nbt) {
            super.notifyAbsence(presentCalled, absentCalled, nbt);
            this.parent.notifyAbsence(presentCalled, absentCalled, nbt);
        }

        @Override
        public MatchResultRegular withParent(final MatchResultRegular parent) {
            return this.parent.withParent(new MatchResultParentedRegular(this.ingredient, this.input, this.doesMatch, this.supplementaryResults, parent));
        }
    }

    public static class MatchResultAuxiliary implements MatchResult<AuxiliaryIngredient<?>, MatchResultAuxiliary> {
        protected final AuxiliaryIngredient<?> ingredient;
        protected final ItemStack input;
        protected final boolean doesMatch;
        protected final ImmutableList<MatchResultAuxiliary> supplementaryResults;

        public MatchResultAuxiliary(final AuxiliaryIngredient<?> ingredient, final ItemStack input, final boolean doesMatch, final List<MatchResultAuxiliary> supplementaryResults) {
            this.ingredient = Objects.requireNonNull(ingredient, "ingredient");
            this.input = input;
            this.doesMatch = doesMatch;
            this.supplementaryResults = ImmutableList.copyOf(supplementaryResults);
        }

        @Override
        public final AuxiliaryIngredient<?> getIngredient() {
            return this.ingredient;
        }

        @Override
        public final ItemStack getInput() {
            return this.input;
        }

        @Override
        public final boolean doesMatch() {
            return this.doesMatch;
        }

        @Deprecated // CompoundTag deprecation
        @Override
        public void forMatch(final Set<GenericIngredient<?, ?>> called, final CompoundTag nbt) {
            if (!called.contains(this.ingredient)) {
                this.ingredient.present(nbt);
                called.add(this.ingredient);
            }
        }

        @Deprecated // CompoundTag deprecation
        @Override
        public void notifyAbsence(final Set<GenericIngredient<?, ?>> presentCalled, final Set<GenericIngredient<?, ?>> absentCalled, final CompoundTag nbt) {
            if (!presentCalled.contains(this.ingredient) && !absentCalled.contains(this.ingredient)) {
                this.ingredient.absent(nbt);
                absentCalled.add(this.ingredient);
            }
            for (final MatchResultAuxiliary result : this.supplementaryResults) {
                result.notifyAbsence(presentCalled, absentCalled, nbt);
            }
        }

        @Override
        public MatchResultAuxiliary withParent(final MatchResultAuxiliary parent) {
            return new MatchResultParentedAuxiliary(this.ingredient, this.input, this.doesMatch, this.supplementaryResults, parent);
        }

        public boolean isAtLimit(final int count) {
            return count >= this.ingredient.getLimit();
        }

        public void propagate(final Multimap<AuxiliaryIngredient<?>, MatchResultAuxiliary> map) {
            map.put(this.ingredient, this);
        }
    }

    public static class MatchResultParentedAuxiliary extends MatchResultAuxiliary {
        protected final MatchResultAuxiliary parent;

        public MatchResultParentedAuxiliary(final AuxiliaryIngredient<?> ingredient, final ItemStack input, final boolean doesMatch, final List<MatchResultAuxiliary> supplementaryResults, final MatchResultAuxiliary parent) {
            super(ingredient, input, doesMatch, supplementaryResults);
            this.parent = Objects.requireNonNull(parent, "parent");
        }

        @Deprecated // CompoundTag deprecation
        @Override
        public void forMatch(final Set<GenericIngredient<?, ?>> called, final CompoundTag nbt) {
            super.forMatch(called, nbt);
            this.parent.forMatch(called, nbt);
        }

        @Deprecated // CompoundTag deprecation
        @Override
        public void notifyAbsence(final Set<GenericIngredient<?, ?>> presentCalled, final Set<GenericIngredient<?, ?>> absentCalled, final CompoundTag nbt) {
            super.notifyAbsence(presentCalled, absentCalled, nbt);
            this.parent.notifyAbsence(presentCalled, absentCalled, nbt);
        }

        @Override
        public MatchResultAuxiliary withParent(final MatchResultAuxiliary parent) {
            return this.parent.withParent(new MatchResultParentedAuxiliary(this.ingredient, this.input, this.doesMatch, this.supplementaryResults, parent));
        }

        @Override
        public boolean isAtLimit(final int count) {
            return super.isAtLimit(count) || this.parent.isAtLimit(count);
        }

        @Override
        public void propagate(final Multimap<AuxiliaryIngredient<?>, MatchResultAuxiliary> map) {
            super.propagate(map);
            this.parent.propagate(map);
        }
    }
}
