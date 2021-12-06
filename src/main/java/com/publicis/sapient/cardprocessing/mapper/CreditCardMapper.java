package com.publicis.sapient.cardprocessing.mapper;

import com.publicis.sapient.cardprocessing.config.ServerConstants;
import com.publicis.sapient.cardprocessing.dto.CreditCardRequest;
import com.publicis.sapient.cardprocessing.dto.CreditCardResponse;
import com.publicis.sapient.cardprocessing.entity.CreditCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface CreditCardMapper {

    CreditCard creditCardRequestToCreditCard(CreditCardRequest creditCardRequest);

    @Mapping(source = "limit", target = "limit", qualifiedByName = "addCurrency")
    @Mapping(source = "balance", target = "balance", qualifiedByName = "addCurrency")
    CreditCardResponse creditCardToCreditCardResponse(CreditCard creditCard);

    @Named("addCurrency")
    public static String addCurrency(final BigDecimal amt) {
        return amt + ServerConstants.DEFAULT_CURRENCY;
    }
}
