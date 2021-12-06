package com.publicis.sapient.cardprocessing.dto.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.stream.IntStream;

public class AllNumericValidator implements ConstraintValidator<AllNumeric, String> {
    /**
     * Checks if the given string contains all digit
     *
     * @param s                          string to be validated
     * @param constraintValidatorContext constraintValidatorContext
     * @return is all numeric or not
     */
    @Override
    public boolean isValid(final String s, final ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) {
            return false;
        }
        return IntStream.range(0, s.length()).allMatch(i -> Character.isDigit(s.charAt(i)));
    }
}
