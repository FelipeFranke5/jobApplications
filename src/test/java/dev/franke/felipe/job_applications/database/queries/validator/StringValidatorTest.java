package dev.franke.felipe.job_applications.database.queries.validator;

import dev.franke.felipe.job_applications.database.exception.InvalidRequiredParametersException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class StringValidatorTest {

    private static final int STRING_MAX_LENGTH = 255;
    private static final String STRING_DESCRIPTION = "The Result";


    @ParameterizedTest
    @ValueSource(strings = {"I am valid STR", "String", "Job Name", "Fe"})
    void testValidateField_WhenFieldIsValid_ShouldNotThrow(String input) {
        StringValidator validator = StringValidator.getValidator(STRING_MAX_LENGTH, input, STRING_DESCRIPTION);
        assertDoesNotThrow(() -> StringValidator.validateField(validator));
    }

    @ParameterizedTest
    @NullSource
    void testValidateField_WhenStringIsNull_ShouldThrow(String input) {
        StringValidator validator = StringValidator.getValidator(STRING_MAX_LENGTH, input, STRING_DESCRIPTION);
        assertThrows(InvalidRequiredParametersException.class, () -> StringValidator.validateField(validator));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    void testValidateField_WhenStringIsBlank_ShouldThrow(String input) {
        StringValidator validator = StringValidator.getValidator(STRING_MAX_LENGTH, input, STRING_DESCRIPTION);
        assertThrows(InvalidRequiredParametersException.class, () -> StringValidator.validateField(validator));
    }

    @ParameterizedTest
    @ValueSource(strings =
            {
                    "cd ../",
                    "cd ..\\",
                    "USE sys;",
                    "SHOW DATABASES",
                    "SHOW DATABASES;",
                    "USE mysql;",
                    "INSERT INTO mysql VALUES (1, 2, 3)",
                    "eval\\",
                    "sudo su",
                    "SELECT \\* FROM mysql;",
                    "cmd.exe",
                    "DELETE FROM",
                    "ssh -i"
            }
    )
    void testValidateField_WhenStringIsMalicious_ShouldThrow(String input) {
        StringValidator validator = StringValidator.getValidator(STRING_MAX_LENGTH, input, STRING_DESCRIPTION);
        assertThrows(InvalidRequiredParametersException.class, () -> StringValidator.validateField(validator));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a;", "b&", "c|", "d<>", "e$", "(f", "g)", "h{", "i}"})
    void testValidateField_WhenStringContainsInvalidChars_ShouldThrow(String input) {
        StringValidator validator = StringValidator.getValidator(STRING_MAX_LENGTH, input, STRING_DESCRIPTION);
        assertThrows(InvalidRequiredParametersException.class, () -> StringValidator.validateField(validator));
    }

    @Test
    void testValidateField_WhenStringExceedsMaxLength_ShouldThrow() {
        String input = "a".repeat(300);
        StringValidator validator = StringValidator.getValidator(STRING_MAX_LENGTH, input, STRING_DESCRIPTION);
        assertThrows(InvalidRequiredParametersException.class, () -> StringValidator.validateField(validator));
    }

}