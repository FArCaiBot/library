package org.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TextMaskerTest {
    private final TextMasker masker = new TextMasker();

    @Test
    public void masksWholeTextWithDefaultMaskChar() {
        assertEquals("******", masker.mask("abcdef"));
    }

    @Test
    public void supportsCustomMaskCharacter() {
        MaskingConfig config = MaskingConfig.builder()
            .maskChar('#')
            .build();

        assertEquals("######", masker.mask("123456", config));
    }

    @Test
    public void supportsVisiblePrefix() {
        MaskingConfig config = MaskingConfig.builder()
            .visiblePrefix(2)
            .build();

        assertEquals("ab****", masker.mask("abcdef", config));
    }

    @Test
    public void supportsVisibleSuffix() {
        MaskingConfig config = MaskingConfig.builder()
            .visibleSuffix(2)
            .build();

        assertEquals("****ef", masker.mask("abcdef", config));
    }

    @Test
    public void supportsVisiblePrefixAndSuffix() {
        MaskingConfig config = MaskingConfig.builder()
            .visiblePrefix(2)
            .visibleSuffix(2)
            .build();

        assertEquals("ab**ef", masker.mask("abcdef", config));
    }

    @Test
    public void returnsOriginalWhenLengthIsNotEnoughToMask() {
        MaskingConfig config = MaskingConfig.builder()
            .visiblePrefix(2)
            .visibleSuffix(2)
            .build();

        assertEquals("abc", masker.mask("abc", config));
    }

    @Test
    public void preservesSeparatorsWhenConfigured() {
        MaskingConfig config = MaskingConfig.builder()
            .visibleSuffix(4)
            .preserveSeparators(true)
            .build();

        assertEquals("****-****-9012", masker.mask("1234-5678-9012", config));
    }

    @Test
    public void masksSeparatorsWhenPreserveSeparatorsIsDisabled() {
        assertEquals("*********", masker.mask("1234-5678"));
    }

    @Test
    public void returnsNullForNullInput() {
        assertNull(masker.mask(null));
    }

    @Test
    public void returnsEmptyStringForEmptyInput() {
        assertEquals("", masker.mask(""));
    }

    @Test
    public void supportsGeneralStrategySelection() {
        MaskingConfig config = MaskingConfig.builder()
            .visibleSuffix(2)
            .build();

        assertEquals("****56", masker.mask("123456", config, MaskingStrategy.GENERAL));
    }

    @Test
    public void supportsReusableConfigurationAcrossCalls() {
        MaskingConfig config = MaskingConfig.builder()
            .visibleSuffix(4)
            .build();

        assertEquals("******4321", masker.mask("0987654321", config));
        assertEquals("************1111", masker.mask("4111111111111111", config));
    }

    @Test
    public void keepsSameLengthForGeneralMasking() {
        MaskingConfig config = MaskingConfig.builder()
            .visiblePrefix(2)
            .visibleSuffix(2)
            .preserveSeparators(true)
            .build();

        String input = "ABCD-1234";
        String output = masker.mask(input, config);

        assertEquals(input.length(), output.length());
    }

    @Test
    public void isDeterministicForSameInputAndConfig() {
        MaskingConfig config = MaskingConfig.builder()
            .visiblePrefix(1)
            .visibleSuffix(2)
            .build();

        String first = masker.mask("abcdef", config);
        String second = masker.mask("abcdef", config);

        assertEquals(first, second);
    }

    @Test
    public void supportsMultipleIndependentInvocations() {
        MaskingConfig configA = MaskingConfig.builder()
            .visibleSuffix(4)
            .build();

        MaskingConfig configB = MaskingConfig.builder()
            .visiblePrefix(2)
            .build();

        assertEquals("******4321", masker.mask("0987654321", configA));
        assertEquals("ab****", masker.mask("abcdef", configB));
    }

    @Test
    public void masksEmailPreservingDomain() {
        assertEquals("f*****@mail.com", masker.mask("flavio@mail.com", MaskingStrategy.EMAIL));
    }

    @Test
    public void masksPhoneKeepingLastFourDigitsByDefault() {
        assertEquals("******4321", masker.mask("0987654321", MaskingStrategy.PHONE));
    }

    @Test
    public void masksPhoneUsingConfiguredVisibleSuffix() {
        MaskingConfig config = MaskingConfig.builder()
            .visibleSuffix(2)
            .build();

        assertEquals("********21", masker.mask("0987654321", config, MaskingStrategy.PHONE));
    }

    @Test
    public void masksCardKeepingLastFourDigits() {
        assertEquals("************1111", masker.mask("4111111111111111", MaskingStrategy.CARD));
    }

    @Test
    public void masksCardWithSeparatorsWhenConfigured() {
        MaskingConfig config = MaskingConfig.builder()
            .preserveSeparators(true)
            .build();

        assertEquals("****-****-****-1111", masker.mask("4111-1111-1111-1111", config, MaskingStrategy.CARD));
    }

    @Test
    public void masksCardIncludingSeparatorsWhenConfigured() {
        MaskingConfig config = MaskingConfig.builder()
            .preserveSeparators(false)
            .build();

        assertEquals("***************1111", masker.mask("4111-1111-1111-1111", config, MaskingStrategy.CARD));
    }

    @Test(expected = IllegalArgumentException.class)
    public void validatesVisiblePrefix() {
        MaskingConfig.builder()
            .visiblePrefix(-1)
            .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void validatesVisibleSuffix() {
        MaskingConfig.builder()
            .visibleSuffix(-1)
            .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void validatesMaskCharacter() {
        MaskingConfig.builder()
            .maskChar(Character.MIN_VALUE)
            .build();
    }
}
