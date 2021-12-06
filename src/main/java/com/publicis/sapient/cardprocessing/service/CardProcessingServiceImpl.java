package com.publicis.sapient.cardprocessing.service;

import com.publicis.sapient.cardprocessing.dao.CreditCardDao;
import com.publicis.sapient.cardprocessing.dto.CreditCardResponse;
import com.publicis.sapient.cardprocessing.entity.CreditCard;
import com.publicis.sapient.cardprocessing.exception.EntityNotFoundException;
import com.publicis.sapient.cardprocessing.mapper.CreditCardMapper;
import com.publicis.sapient.cardprocessing.mapper.CreditCardModelAssembler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

@Service
public class CardProcessingServiceImpl implements CardProcessingService {

    private final CreditCardDao creditCardDao;
    private final CreditCardMapper creditCardMapper;
    private final PagedResourcesAssembler<CreditCard> pagedResourcesAssembler;
    private final CreditCardModelAssembler creditCardModelAssembler;

    public CardProcessingServiceImpl(final CreditCardDao creditCardDao, final CreditCardMapper creditCardMapper,
                                     final PagedResourcesAssembler<CreditCard> pagedResourcesAssembler,
                                     final CreditCardModelAssembler creditCardModelAssembler) {
        this.creditCardDao = creditCardDao;
        this.creditCardMapper = creditCardMapper;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.creditCardModelAssembler = creditCardModelAssembler;
    }

    /**
     * Creates/Saves a credit card entry to the db
     *
     * @param creditCard object to be saved in db
     **/
    @Override
    public CreditCard createCreditCard(final CreditCard creditCard) {
        return creditCardDao.save(creditCard);
    }

    /**
     * Returns a credit card based on crediutCardId
     *
     * @param creditCardId unique credit cardId in db
     **/
    @Override
    public CreditCardResponse getById(final Long creditCardId) {
        return creditCardDao.findById(creditCardId)
                .map(creditCardMapper::creditCardToCreditCardResponse).orElseThrow(() -> {
                    throw new EntityNotFoundException("Credit Card not found.");
                });
    }

    /**
     * Returns all creditCard in db
     *
     * @param pageable springs pageable object based on which paged responses will be sent back, along with Hateoas link
     **/
    @Override
    public PagedModel<CreditCardResponse> getAllCreditCard(final Pageable pageable) {
        final Page<CreditCard> creditCardPages = creditCardDao.findAll(pageable);
        final PagedModel<CreditCardResponse> creditCardsPagedModel = pagedResourcesAssembler
                .toModel(creditCardPages, creditCardModelAssembler);
        if (creditCardsPagedModel.getContent().size() == 0) {
            throw new EntityNotFoundException("No Credit Cards found.");
        }
        return creditCardsPagedModel;
    }
}
