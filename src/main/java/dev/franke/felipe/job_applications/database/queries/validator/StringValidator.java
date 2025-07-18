package dev.franke.felipe.job_applications.database.queries.validator;

import dev.franke.felipe.job_applications.database.exception.InvalidRequiredParametersException;

import java.util.regex.Pattern;

public record StringValidator(int maxLength, String theString, String stringDescription) {

    public static StringValidator getValidator(int maxLength, String fieldName, String fieldDescription) {
        return new StringValidator(maxLength, fieldName, fieldDescription);
    }

    public static void validateField(StringValidator validator) {
        validator.assertStringIsNotNull();
        validator.assertStringIsNotBlank();
        validator.assertStringDoesNotExceedMaxLength();
        validator.assertStringDoesNotHaveFileOrDirPath();
        validator.assertStringDoesNotContainInvalidInput();
        validator.assertStringDoesNotContainInvalidChars();
    }

    private void assertStringIsNotNull() {
        if (theString() == null) {
            throw new InvalidRequiredParametersException(stringDescription() + " value cannot be null!");
        }
    }

    private void assertStringIsNotBlank() {
        if (theString().isBlank()) {
            throw new InvalidRequiredParametersException(stringDescription() + " value cannot be blank!");
        }
    }

    private void assertStringDoesNotExceedMaxLength() {
        if (theString().length() >= maxLength()) {
            throw new InvalidRequiredParametersException(
                    stringDescription() + " value cannot exceed " + maxLength() + " chars!"
            );
        }
    }

    private void assertStringDoesNotHaveFileOrDirPath() {
        if (theString().contains("../") || theString().contains("..\\")) {
            throw new InvalidRequiredParametersException(
                    stringDescription() + " value cannot have file or directory path!!"
            );
        }
    }

    private void assertStringDoesNotContainInvalidInput() {
        if (containsMaliciousInput(theString)) {
            throw new InvalidRequiredParametersException(stringDescription() + " value was considered dangerous!");
        }
    }

    private void assertStringDoesNotContainInvalidChars() {
        if (containsMaliciousChar(theString)) {
            throw new InvalidRequiredParametersException(
                    stringDescription() + " value should no contain invalid chars!"
            );
        }
    }

    private String[] maliciousPatterns() {
        return new String[]{
                "DROP TABLE", "SELECT \\*", "INSERT INTO", "DELETE FROM",
                "USE", "bash -c", "sh -c", "cmd.exe", "eval", "\\$\\{", "eval\\{"
        };
    }

    private boolean containsMaliciousInput(String input) {
        for (String pattern : maliciousPatterns()) {
            if (Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(input).find()) {
                return true;
            }
        }
        return false;
    }

    private boolean containsMaliciousChar(String input) {
        String dangerousChars = ";&|<>$\\\"'`(){}[]";
        for (char theChar : dangerousChars.toCharArray()) {
            if (input.indexOf(theChar) != -1) {
                return true;
            }
        }
        return false;
    }
}
