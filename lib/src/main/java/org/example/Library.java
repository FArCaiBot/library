package org.example;

import java.util.Objects;

public class Library {
    private final TextMasker textMasker;

    public Library() {
        this(new TextMasker());
    }

    public Library(TextMasker textMasker) {
        this.textMasker = Objects.requireNonNull(textMasker, "textMasker must not be null");
    }

    public String mask(String value) {
        return textMasker.mask(value);
    }

    public String mask(String value, MaskingConfig config) {
        return textMasker.mask(value, config);
    }

    public String mask(String value, MaskingStrategy strategy) {
        return textMasker.mask(value, strategy);
    }

    public String mask(String value, MaskingConfig config, MaskingStrategy strategy) {
        return textMasker.mask(value, config, strategy);
    }
}
