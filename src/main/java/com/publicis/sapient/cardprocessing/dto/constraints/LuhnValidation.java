package com.publicis.sapient.cardprocessing.dto.constraints;

import com.publicis.sapient.cardprocessing.dto.constraints.LuhnCardNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static com.publicis.sapient.cardprocessing.config.ServerConstants.INVALID_FORMAT;

@Documented
@Constraint(validatedBy = LuhnCardNumberValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface LuhnValidation {
    String message() default INVALID_FORMAT;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

