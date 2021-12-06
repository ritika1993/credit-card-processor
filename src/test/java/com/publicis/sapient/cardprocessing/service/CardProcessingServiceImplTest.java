package com.publicis.sapient.cardprocessing.service;

import com.publicis.sapient.cardprocessing.dao.CreditCardDao;
import com.publicis.sapient.cardprocessing.dto.CreditCardResponse;
import com.publicis.sapient.cardprocessing.entity.CreditCard;
import com.publicis.sapient.cardprocessing.exception.EntityNotFoundException;
import com.publicis.sapient.cardprocessing.mapper.CreditCardMapper;
import com.publicis.sapient.cardprocessing.mapper.CreditCardModelAssembler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CardProcessingServiceImplTest {

    @Mock
    private CreditCardDao creditCardDao;
    @Mock
    private CreditCardMapper creditCardMapper;
    @Mock
    private PagedResourcesAssembler<CreditCard> pagedResourcesAssembler;
    @Mock
    private CreditCardModelAssembler creditCardModelAssembler;

    @InjectMocks
    private CardProcessingServiceImpl cardProcessingServiceImpl;


    @Test
    public void getByIdTest() {
        final CreditCard creditCard = CreditCard.builder().id(1L).cardNumber("7658587").name("random").build();
        final CreditCardResponse creditCardResponse = CreditCardResponse.builder().id(1L).cardNumber("7658587").name("random").build();
        when(creditCardDao.findById(1L)).thenReturn(java.util.Optional.ofNullable(creditCard));
        when(creditCardMapper.creditCardToCreditCardResponse(creditCard)).thenReturn(creditCardResponse);
        final CreditCardResponse res = cardProcessingServiceImpl.getById(1L);
        assertThat(res).isEqualTo(creditCardResponse);
        verify(creditCardDao).findById(1L);
        verify(creditCardMapper).creditCardToCreditCardResponse(creditCard);
    }

    @Test
    public void getByIdTestFailure() {
        final CreditCard creditCard = CreditCard.builder().id(1L).cardNumber("7658587").name("random").build();
        when(creditCardDao.findById(1L)).thenReturn(Optional.empty());
        final Throwable throwable = catchThrowable(() -> cardProcessingServiceImpl.getById(1L));
        assertThat(throwable).isNotNull().isInstanceOf(EntityNotFoundException.class);
        verify(creditCardDao).findById(1L);
        verify(creditCardMapper, never()).creditCardToCreditCardResponse(creditCard);
    }

    @Test
    public void getAllCreditCard() {
        final CreditCard creditCard = CreditCard.builder().id(1L).cardNumber("7658587").name("random").limit(BigDecimal.valueOf(500)).balance(BigDecimal.valueOf(0.0)).build();
        final CreditCardResponse creditCardResponse = CreditCardResponse.builder().id(1L).cardNumber("7658587").name("random").limit("500").balance("0").build();
        final Pageable pageable = PageRequest.of(0, 2);

        final Page<CreditCard> creditCardPage = new PageImpl<>(Collections.singletonList(creditCard));
        final List<CreditCardResponse> creditCardResponsesList = new ArrayList<>();
        creditCardResponsesList.add(creditCardResponse);
        final PagedModel<CreditCardResponse> creditCardResponses = mock(PagedModel.class);
        when(creditCardDao.findAll(pageable)).thenReturn(creditCardPage);
        when(pagedResourcesAssembler.toModel(creditCardPage, creditCardModelAssembler)).thenReturn(creditCardResponses);
        when(creditCardResponses.getContent()).thenReturn(creditCardResponsesList);
        final PagedModel<CreditCardResponse> response = cardProcessingServiceImpl.getAllCreditCard(pageable);
        assertThat(response).isEqualTo(creditCardResponses);
        verify(creditCardDao).findAll(pageable);
        verify(pagedResourcesAssembler).toModel(creditCardPage, creditCardModelAssembler);
        verify(creditCardResponses).getContent();
    }

}