package com.publicis.sapient.cardprocessing.dto.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.*;

import static com.publicis.sapient.cardprocessing.config.ServerConstants.NOT_NUMERIC;

@Documented
@Constraint(validatedBy = AllNumericValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AllNumeric {
    String message() default NOT_NUMERIC;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}