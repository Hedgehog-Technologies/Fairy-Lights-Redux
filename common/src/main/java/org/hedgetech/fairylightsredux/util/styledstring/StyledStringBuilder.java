package org.hedgetech.fairylightsredux.util.styledstring;

import java.util.ArrayList;
import java.util.List;

public final class StyledStringBuilder {
    private final StringBuilder value = new StringBuilder();
    private final List<Style> styling = new ArrayList<>();

    public StyledStringBuilder append(final StyledString str) {
        this.value.append(str);

        for (int i = 0; i < str.length(); i++) {
            this.styling.add(str.styleAt(i));
        }

        return this;
    }

    public StyledStringBuilder append(final char chr, final Style style) {
        this.value.append(chr);
        this.styling.add(style);
        return this;
    }

    public StyledStringBuilder prepend(final StyledString str) {
        this.value.insert(0, str);

        for (int i = str.length() - 1; i >= 0; i--) {
            this.styling.addFirst(str.styleAt(i));
        }

        return this;
    }

    public StyledString toStyledString() {
        return new StyledString(this.value.toString(), this.styling.toArray(new Style[0]));
    }
}
