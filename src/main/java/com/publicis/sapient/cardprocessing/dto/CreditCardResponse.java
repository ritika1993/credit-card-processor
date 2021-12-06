package com.publicis.sapient.cardprocessing.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Builder
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class CreditCardResponse extends RepresentationModel<CreditCardResponse> {

    private long id;
    private String name;
    private String cardNumber;
    private String limit;
    private String balance;


}
