package com.publicis.sapient.cardprocessing.mapper;

import com.publicis.sapient.cardprocessing.controller.CardProcessingController;
import com.publicis.sapient.cardprocessing.dto.CreditCardResponse;
import com.publicis.sapient.cardprocessing.entity.CreditCard;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static com.publicis.sapient.cardprocessing.config.ServerConstants.DEFAULT_CURRENCY;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CreditCardModelAssembler extends RepresentationModelAssemblerSupport<CreditCard, CreditCardResponse> {

    CreditCardModelAssembler() {
        super(CardProcessingController.class, CreditCardResponse.class);
    }

    @Override
    public CreditCardResponse toModel(final CreditCard entity) {
        final CreditCardResponse creditCardResponse = instantiateModel(entity);

        creditCardResponse.add(linkTo(
                methodOn(CardProcessingController.class)
                        .getByCreditCardId(entity.getId()))
                .withSelfRel());
        creditCardResponse.setId(entity.getId());
        creditCardResponse.setCardNumber(entity.getCardNumber());
        creditCardResponse.setBalance(entity.getBalance().toString() + DEFAULT_CURRENCY);
        creditCardResponse.setLimit(entity.getLimit().toString() + DEFAULT_CURRENCY);
        creditCardResponse.setName(entity.getName());
        return creditCardResponse;
    }
}
