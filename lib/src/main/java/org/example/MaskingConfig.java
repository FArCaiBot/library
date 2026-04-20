package org.example;

public final class MaskingConfig {
    private static final char DEFAULT_MASK_CHAR = '*';
    private static final int DEFAULT_VISIBLE_PREFIX = 0;
    private static final int DEFAULT_VISIBLE_SUFFIX = 0;
    private static final boolean DEFAULT_PRESERVE_SEPARATORS = false;
    private static final MaskingConfig DEFAULT_CONFIG = new Builder().build();

    private final char maskChar;
    private final int visiblePrefix;
    private final int visibleSuffix;
    private final boolean preserveSeparators;

    private MaskingConfig(Builder builder) {
        this.maskChar = builder.maskChar;
        this.visiblePrefix = builder.visiblePrefix;
        this.visibleSuffix = builder.visibleSuffix;
        this.preserveSeparators = builder.preserveSeparators;
        validate();
    }

    public static MaskingConfig defaultConfig() {
        return DEFAULT_CONFIG;
    }

    public static Builder builder() {
        return new Builder();
    }

    public char getMaskChar() {
        return maskChar;
    }

    public int getVisiblePrefix() {
        return visiblePrefix;
    }

    public int getVisibleSuffix() {
        return visibleSuffix;
    }

    public boolean isPreserveSeparators() {
        return preserveSeparators;
    }

    private void validate() {
        if (visiblePrefix < 0) {
            throw new IllegalArgumentException("visiblePrefix must be >= 0");
        }
        if (visibleSuffix < 0) {
            throw new IllegalArgumentException("visibleSuffix must be >= 0");
        }
        if (maskChar == Character.MIN_VALUE) {
            throw new IllegalArgumentException("maskChar must be a valid character");
        }
    }

    public static final class Builder {
        private char maskChar = DEFAULT_MASK_CHAR;
        private int visiblePrefix = DEFAULT_VISIBLE_PREFIX;
        private int visibleSuffix = DEFAULT_VISIBLE_SUFFIX;
        private boolean preserveSeparators = DEFAULT_PRESERVE_SEPARATORS;

        private Builder() {
        }

        public Builder maskChar(char maskChar) {
            this.maskChar = maskChar;
            return this;
        }

        public Builder visiblePrefix(int visiblePrefix) {
            this.visiblePrefix = visiblePrefix;
            return this;
        }

        public Builder visibleSuffix(int visibleSuffix) {
            this.visibleSuffix = visibleSuffix;
            return this;
        }

        public Builder preserveSeparators(boolean preserveSeparators) {
            this.preserveSeparators = preserveSeparators;
            return this;
        }

        public MaskingConfig build() {
            return new MaskingConfig(this);
        }
    }
}
