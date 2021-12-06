package com.publicis.sapient.cardprocessing.service;

import com.publicis.sapient.cardprocessing.dto.CreditCardResponse;
import com.publicis.sapient.cardprocessing.entity.CreditCard;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

public interface CardProcessingService {
    
    CreditCard createCreditCard(CreditCard creditCard);

    PagedModel<CreditCardResponse> getAllCreditCard(Pageable pageable);

    CreditCardResponse getById(final Long creditCardId);
}
