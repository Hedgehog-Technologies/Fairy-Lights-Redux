package org.hedgetech.fairylightsredux.server.connection;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.network.OpenEditLetteredConnectionScreenPayload;
import org.hedgetech.fairylightsredux.platform.Services;
import org.hedgetech.fairylightsredux.server.fastener.Fastener;
import org.hedgetech.fairylightsredux.server.component.FLRDataComponents;
import org.hedgetech.fairylightsredux.server.feature.Letter;
import org.hedgetech.fairylightsredux.util.ICurve;
import org.hedgetech.fairylightsredux.util.styledstring.StyledString;
import org.hedgetech.fairylightsredux.util.styledstring.StylingPresence;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.function.UnaryOperator;

public final class LetterBuntingConnection extends Connection implements ILettered {
    public static final SymbolSet SYMBOLS = new SymbolSet(7, "0-9, A-Z, &, !, ?")
            .add(" 0123456789ABCDEFGHJKLMNOPQRSTUVWXYZ&?", 6)
            .add("I", 4)
            .add("!", 2);

    private static final float TRACKING = 1.0F / 16.0F;
    private static final StylingPresence SUPPORTED_STYLING = new StylingPresence(true, false, false, false, false, false);

    private StyledString text = StyledString.EMPTY;
    private Letter[] letters = new Letter[0];

    public LetterBuntingConnection(final ConnectionType<LetterBuntingConnection> type, final Level world, final Fastener<?> fastener, final UUID uuid) {
        super(type, world, fastener, uuid);
    }

    @Override
    public float getRadius() {
        return 0.9F / 32;
    }

    public Letter[] getLetters() {
        return this.letters;
    }

    @Override
    public ItemStack getItemStack() {
        final ItemStack stack = super.getItemStack();
        stack.set(FLRDataComponents.LETTER_BUNTING, this.text);
        return stack;
    }

    @Override
    public boolean matches(final ItemStack stack) {
        return super.matches(stack) && this.text.equals(stack.getOrDefault(FLRDataComponents.LETTER_BUNTING, StyledString.EMPTY));
    }

    @Override
    public void onConnect(final Player user, final ItemStack heldStack) {
        if (this.text.isEmpty() && user instanceof final ServerPlayer player) {
            Services.PLATFORM.sendToPlayer(player, new OpenEditLetteredConnectionScreenPayload(this.saveFastener(), this.getUUID()));
        }
    }

    @Override
    protected void onUpdate() {
        for (final Letter letter : this.letters) {
            letter.tick(this.world);
        }
    }

    @Override
    protected void onCalculateCatenary(final boolean relocated) {
        this.updateLetters();
    }

    private void updateLetters() {
        if (this.text.isEmpty()) {
            this.letters = new Letter[0];
            return;
        }

        final ICurve catenary = this.getCatenary();
        final float catLength = catenary.getLength();
        final float[] pointOffsets = new float[this.text.length()];
        float textWidth = 0;
        int textLen = 0;

        for (int i = 0; i < this.text.length(); i++) {
            final float w = SYMBOLS.getWidth(this.text.charAt(i));
            pointOffsets[i] = textWidth + w / 2.0F;
            textWidth += w + TRACKING;
            if (textWidth > catLength) break;
            textLen++;
        }

        final float offset = catLength / 2 - textWidth / 2;
        for (int i = 0; i < textLen; i++) {
            pointOffsets[i] += offset;
        }

        final Letter[] prevLetters = this.letters;
        final List<Letter> letters = new ArrayList<>();
        final ICurve.ISegmentIterator it = catenary.iterator();
        float distance = 0;
        int pointIdx = 0;

        while (pointIdx < textLen && it.next()) {
            final float length = it.getLength();

            while (pointIdx < textLen && pointOffsets[pointIdx] < distance + length) {
                final float t = (pointOffsets[pointIdx] - distance) / length;
                final Vec3 point = new Vec3(it.getX(t), it.getY(t), it.getZ(t));
                final Letter letter;

                if (pointIdx < prevLetters.length) {
                    letter = prevLetters[pointIdx];
                    letter.set(point, it.getYaw(), it.getPitch());
                    letter.set(this.text.charAt(pointIdx), this.text.styleAt(pointIdx));
                } else {
                    letter = new Letter(pointIdx, point, it.getYaw(), it.getPitch(), SYMBOLS, this.text.charAt(pointIdx), this.text.styleAt(pointIdx));
                }

                letters.add(letter);
                pointIdx++;
            }

            distance += length;
        }

        this.letters = letters.toArray(new Letter[0]);
    }

    @Override
    public StylingPresence getSupportedStyling() {
        return SUPPORTED_STYLING;
    }

    @Override
    public boolean isSupportedCharacter(final int chr) {
        return SYMBOLS.contains(chr);
    }

    @Override
    public boolean isSupportedText(final StyledString text) {
        float len = 0;
        final float available = this.getCatenary().getLength();

        for (int i = 0; i < text.length(); i++) {
            len += SYMBOLS.getWidth(text.charAt(i)) + TRACKING;
            if (len > available || !text.styleAt(i).isPlain()) return false;
        }

        return ILettered.super.isSupportedText(text);
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
    public UnaryOperator<String> getInputTransformer() {
        return str -> Normalizer.normalize(str, Normalizer.Form.NFKD)
                .replaceAll("[\\p{Mn}\\p{Sk}]", "")
                .toUpperCase(Locale.ROOT);
    }

    @Override
    public String getAllowedDescription() {
        return SYMBOLS.getDescription();
    }

    @Override
    protected void readItem(final ItemStack stack) {
        this.text = stack.getOrDefault(FLRDataComponents.LETTER_BUNTING, StyledString.EMPTY);
    }

    @Override
    protected void saveLogic(final ValueOutput output) {
        output.store("text", StyledString.CODEC, this.text);
    }

    @Override
    protected void loadLogic(final ValueInput input) {
        this.text = input.read("text", StyledString.CODEC).orElse(StyledString.EMPTY);
    }
}
