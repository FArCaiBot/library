package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TextMasker {
    private static final int DEFAULT_PHONE_VISIBLE_SUFFIX = 4;
    private static final int DEFAULT_CARD_VISIBLE_SUFFIX = 4;

    public String mask(String value) {
        return mask(value, MaskingConfig.defaultConfig(), MaskingStrategy.GENERAL);
    }

    public String mask(String value, MaskingConfig config) {
        return mask(value, config, MaskingStrategy.GENERAL);
    }

    public String mask(String value, MaskingStrategy strategy) {
        return mask(value, MaskingConfig.defaultConfig(), strategy);
    }

    public String mask(String value, MaskingConfig config, MaskingStrategy strategy) {
        Objects.requireNonNull(config, "config must not be null");
        Objects.requireNonNull(strategy, "strategy must not be null");

        if (value == null) {
            return null;
        }
        if (value.isEmpty()) {
            return value;
        }

        switch (strategy) {
            case GENERAL:
                return applyGeneralMask(value, config);
            case EMAIL:
                return applyEmailMask(value, config);
            case PHONE:
                return applyDigitBasedMask(value, config, DEFAULT_PHONE_VISIBLE_SUFFIX);
            case CARD:
                return applyDigitBasedMask(value, config, DEFAULT_CARD_VISIBLE_SUFFIX);
            default:
                throw new IllegalArgumentException("Unsupported masking strategy: " + strategy);
        }
    }

    private String applyGeneralMask(String value, MaskingConfig config) {
        List<Integer> maskableIndexes = collectMaskableIndexes(value, config.isPreserveSeparators());
        int maskableLength = maskableIndexes.size();

        if (maskableLength <= config.getVisiblePrefix() + config.getVisibleSuffix()) {
            return value;
        }

        boolean[] visibleByIndex = new boolean[value.length()];
        for (int i = 0; i < config.getVisiblePrefix() && i < maskableLength; i++) {
            visibleByIndex[maskableIndexes.get(i)] = true;
        }
        for (int i = 0; i < config.getVisibleSuffix() && i < maskableLength; i++) {
            int fromRight = maskableLength - 1 - i;
            visibleByIndex[maskableIndexes.get(fromRight)] = true;
        }

        char[] result = value.toCharArray();
        for (int i = 0; i < result.length; i++) {
            if (config.isPreserveSeparators() && !Character.isLetterOrDigit(result[i])) {
                continue;
            }
            if (!visibleByIndex[i]) {
                result[i] = config.getMaskChar();
            }
        }
        return new String(result);
    }

    private List<Integer> collectMaskableIndexes(String value, boolean preserveSeparators) {
        List<Integer> indexes = new ArrayList<>(value.length());
        for (int i = 0; i < value.length(); i++) {
            char current = value.charAt(i);
            if (preserveSeparators && !Character.isLetterOrDigit(current)) {
                continue;
            }
            indexes.add(i);
        }
        return indexes;
    }

    private String applyEmailMask(String value, MaskingConfig config) {
        int atIndex = value.indexOf('@');
        if (atIndex <= 0 || atIndex == value.length() - 1 || atIndex != value.lastIndexOf('@')) {
            return applyGeneralMask(value, config);
        }

        String userPart = value.substring(0, atIndex);
        if (userPart.length() <= 1) {
            return value;
        }

        String domain = value.substring(atIndex);
        char[] maskedUser = userPart.toCharArray();
        for (int i = 1; i < maskedUser.length; i++) {
            maskedUser[i] = config.getMaskChar();
        }
        return new String(maskedUser) + domain;
    }

    private String applyDigitBasedMask(String value, MaskingConfig config, int defaultVisibleSuffix) {
        int visibleSuffix = config.getVisibleSuffix() > 0 ? config.getVisibleSuffix() : defaultVisibleSuffix;
        int visiblePrefix = config.getVisiblePrefix();

        List<Integer> digitIndexes = collectDigitIndexes(value);
        int digitCount = digitIndexes.size();

        if (digitCount <= visiblePrefix + visibleSuffix) {
            return value;
        }

        boolean[] visibleByIndex = new boolean[value.length()];
        for (int i = 0; i < visiblePrefix && i < digitCount; i++) {
            visibleByIndex[digitIndexes.get(i)] = true;
        }
        for (int i = 0; i < visibleSuffix && i < digitCount; i++) {
            int fromRight = digitCount - 1 - i;
            visibleByIndex[digitIndexes.get(fromRight)] = true;
        }

        char[] result = value.toCharArray();
        for (int i = 0; i < result.length; i++) {
            char current = result[i];
            if (Character.isDigit(current)) {
                if (!visibleByIndex[i]) {
                    result[i] = config.getMaskChar();
                }
                continue;
            }
            if (!config.isPreserveSeparators()) {
                result[i] = config.getMaskChar();
            }
        }

        return new String(result);
    }

    private List<Integer> collectDigitIndexes(String value) {
        List<Integer> indexes = new ArrayList<>(value.length());
        for (int i = 0; i < value.length(); i++) {
            if (Character.isDigit(value.charAt(i))) {
                indexes.add(i);
            }
        }
        return indexes;
    }
}
