package com.leoCode.TestingCourse.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

/*
    TDD: Test Driven Development: develop this test before
    writing the code for this class.
 */
public class PhoneNumberValidatorTest {

    private PhoneNumberValidator underTest;

    @BeforeEach
    void setUp() {
        underTest = new PhoneNumberValidator();
    }

    @ParameterizedTest
    @CsvSource({
            "123-456-7890, true",
            "(123)-456-7890, true",
            "(1234)-456-789, true",
            "(12345)-456-789, true",
            "1234567890, true",
            "1234 567 890, true",
            "123 456 7890, true",
            "1234-456-890, true",
            "1234.456.890, true",
            "1234-456.890, true",
            "123-456-7890 ext4444, true",
            "123-456-7890 x4444, true",
            "PhoneNumber, false",
            "123456789101112, false"
    })
    void itShouldValidatePhoneNumberUsesDashes(String phoneNumber, boolean expected) {
        // When
        boolean isValid =  underTest.test(phoneNumber);

        // Then
        assertThat(isValid).isEqualTo(expected);
    }
}
