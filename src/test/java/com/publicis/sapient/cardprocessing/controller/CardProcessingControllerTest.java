package com.publicis.sapient.cardprocessing.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.publicis.sapient.cardprocessing.dto.CreditCardRequest;
import com.publicis.sapient.cardprocessing.dto.CreditCardResponse;
import com.publicis.sapient.cardprocessing.dto.ErrorDetails;
import com.publicis.sapient.cardprocessing.entity.CreditCard;
import com.publicis.sapient.cardprocessing.exception.CustomResponseEntityExceptionHandler;
import com.publicis.sapient.cardprocessing.mapper.CreditCardMapper;
import com.publicis.sapient.cardprocessing.service.CardProcessingService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CardProcessingController.class)
public class CardProcessingControllerTest {
    private MockMvc mockMvc;
    @SpyBean
    private CustomResponseEntityExceptionHandler customResponseEntityExceptionHandler;
    @MockBean
    private CardProcessingService cardProcessingServiceMock;
    @MockBean
    private CreditCardMapper creditCardMapperMock;
    @Autowired
    private ObjectMapper objMapper;
    private static final String TEST_PUBLIC_PATH = "/api/v1/credit/cards";
    private CardProcessingController cardProcessingController;

    @Before
    public void before() {
        cardProcessingController = new CardProcessingController(cardProcessingServiceMock, creditCardMapperMock);
        mockMvc = MockMvcBuilders.standaloneSetup(cardProcessingController)
                .setControllerAdvice(customResponseEntityExceptionHandler).build();
    }

    @Test
    public void testCreateCreditCard() throws Exception {
        final List<String> validCardNumbers = Arrays.asList("4003600000000014", "1358954993914435", "12345678903555");
        for (final String cc : validCardNumbers) {
            final CreditCardRequest creditCardRequest = CreditCardRequest.builder().cardNumber(cc).limit(BigDecimal.valueOf(400)).name("random").build();
            final CreditCard creditCard = CreditCard.builder().cardNumber(cc).limit(BigDecimal.valueOf(400)).name("random").balance(BigDecimal.valueOf(0.0)).build();
            final CreditCard creditCardAfterSavedInDb = CreditCard.builder().id(1L).cardNumber(cc).limit(BigDecimal.valueOf(400)).name("random").balance(BigDecimal.valueOf(0.0)).build();
            when(creditCardMapperMock.creditCardRequestToCreditCard(any(CreditCardRequest.class))).thenReturn(creditCard);
            when(cardProcessingServiceMock.createCreditCard(creditCard)).thenReturn(creditCardAfterSavedInDb);
            final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(TEST_PUBLIC_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objMapper.writeValueAsString(creditCardRequest)))
                    .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();
            assertThat(mvcResult.getResponse().getHeader("location")).contains(TEST_PUBLIC_PATH + "/1");
        }
        //Todo: ArgumentCaptor can be used to verify actual value
        verify(creditCardMapperMock, times(3)).creditCardRequestToCreditCard(any(CreditCardRequest.class));
        verify(cardProcessingServiceMock, times(3)).createCreditCard(any(CreditCard.class));
    }

    @Test
    public void testCreateCreditCardFailsCardNumberContainsNonNumeric() throws Exception {
        final CreditCardRequest creditCardRequest = CreditCardRequest.builder().cardNumber("13589549wewe93914435").limit(BigDecimal.valueOf(400)).name("random").build();
        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(TEST_PUBLIC_PATH)

                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(creditCardRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
        final List<ErrorDetails> result = objMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertEquals(1, result.stream().map(ErrorDetails::getDetails)
                .filter(s -> s.equals("Credit card number has to contain all numeric value")).count());
        assertEquals(1, result.stream().map(ErrorDetails::getDetails)
                .filter(s -> s.equals("Credit card should be in valid format")).count());

        verify(creditCardMapperMock, never()).creditCardRequestToCreditCard(any(CreditCardRequest.class));
        verify(cardProcessingServiceMock, never()).createCreditCard(any(CreditCard.class));
    }

    @Test
    public void testCreateCreditCardFailsCardNumberContainsNonLuhnValidatedNumber() throws Exception {
        final CreditCardRequest creditCardRequest = CreditCardRequest.builder().cardNumber("1358954093914435").limit(BigDecimal.valueOf(400)).name("random").build();
        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(TEST_PUBLIC_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objMapper.writeValueAsString(creditCardRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
        final List<ErrorDetails> result = objMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertEquals(1, result.stream().map(ErrorDetails::getDetails)
                .filter(s -> s.equals("Credit card should be in valid format")).count());

        verify(creditCardMapperMock, never()).creditCardRequestToCreditCard(any(CreditCardRequest.class));
        verify(cardProcessingServiceMock, never()).createCreditCard(any(CreditCard.class));
    }

    @Test
    public void testGetAllCreditCard() throws Exception {
        //mockMvc is changed to fake Pageable Object creation
        mockMvc = MockMvcBuilders.standaloneSetup(cardProcessingController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
        final PagedModel<CreditCardResponse> pagedModel = PagedModel.of(Collections.emptyList(), new PagedModel.PageMetadata(10, 1, 200));
        when(cardProcessingServiceMock.getAllCreditCard(any(Pageable.class))).thenReturn(pagedModel);
        final MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(TEST_PUBLIC_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        final PagedModel<CreditCardResponse> result = objMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(result).isEqualTo(pagedModel);
        verify(cardProcessingServiceMock).getAllCreditCard(any(Pageable.class));
    }
}