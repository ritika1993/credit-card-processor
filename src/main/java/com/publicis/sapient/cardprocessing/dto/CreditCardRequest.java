package com.publicis.sapient.cardprocessing.dto;

import com.publicis.sapient.cardprocessing.dto.constraints.AllNumeric;
import com.publicis.sapient.cardprocessing.dto.constraints.LuhnValidation;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class CreditCardRequest {
    @NotEmpty
    private String name;

    @NotNull(message = "Card number cannot be null")
    @Size(min = 13, message = "Card Number should not be less then 16")
    @AllNumeric(message = "Credit card number has to contain all numeric value")
    @LuhnValidation(message = "Credit card should be in valid format")
    private String cardNumber;

    @NotNull(message = "Card limit cannot be null")
    private BigDecimal limit;
}
