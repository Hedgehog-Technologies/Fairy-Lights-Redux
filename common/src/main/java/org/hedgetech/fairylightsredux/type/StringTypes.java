package org.hedgetech.fairylightsredux.type;

public enum StringTypes {
    BLACK_STRING(new StringType(0x323232)),
    WHITE_STRING(new StringType(0xF0F0F0));

    private final StringType stringType;

    StringTypes(StringType stringType) {
        this.stringType = stringType;
    }

    public StringType get() {
        return this.stringType;
    }
}
