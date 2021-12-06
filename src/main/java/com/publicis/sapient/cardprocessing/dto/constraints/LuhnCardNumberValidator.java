package com.publicis.sapient.cardprocessing.dto.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class LuhnCardNumberValidator implements ConstraintValidator<LuhnValidation, String> {
    @Override
    public boolean isValid(final String cardNumber, final ConstraintValidatorContext constraintValidatorContext) {
        return isValidCreditCardNumberBAsedOnLuhnAlgorithm(cardNumber);
    }


    /**
     * This is the validator to verify is a card valid based on Luhn's algorithm
     *
     * @param cardNumber cardNumber to be validated
     * @return is valid or not
     */
    private static boolean isValidCreditCardNumberBAsedOnLuhnAlgorithm(final String cardNumber) {
        final int[] cardIntArray = new int[cardNumber.length()];

        for (int i = 0; i < cardNumber.length(); i++) {
            final char c = cardNumber.charAt(i);
            if (!Character.isDigit(c)) {
                return false;
            }
            cardIntArray[i] = Integer.parseInt("" + c);
        }

        for (int i = cardIntArray.length - 2; i >= 0; i = i - 2) {
            int num = cardIntArray[i];
            num = num * 2;
            if (num > 9) {
                num = num % 10 + num / 10;
            }
            cardIntArray[i] = num;
        }

        return Arrays.stream(cardIntArray).sum() % 10 == 0;

    }
}