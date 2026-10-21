package org.hedgetech.fairylightsredux.server.connection;

import org.hedgetech.fairylightsredux.util.styledstring.StyledString;
import org.hedgetech.fairylightsredux.util.styledstring.StylingPresence;

import java.util.function.UnaryOperator;

public interface ILettered {
    default StylingPresence getSupportedStyling() {
        return StylingPresence.ALL;
    }

    default boolean isSupportedCharacter(final int chr) {
        return Character.isValidCodePoint(chr)
                && ((((1 << Character.NON_SPACING_MARK | 1 << Character.MODIFIER_SYMBOL) >> Character.getType(chr)) & 1) == 0);
    }

    default boolean isSupportedText(final StyledString text) {
        for (int i = 0; i < text.length(); i++) {
            if (!this.isSupportedCharacter(text.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    void setText(StyledString text);

    StyledString getText();

    default String getAllowedDescription() {
        return "";
    }

    default UnaryOperator<String> getInputTransformer() {
        return UnaryOperator.identity();
    }
}
