package com.publicis.sapient.cardprocessing.controller;

import com.publicis.sapient.cardprocessing.dto.CreditCardRequest;
import com.publicis.sapient.cardprocessing.dto.CreditCardResponse;
import com.publicis.sapient.cardprocessing.entity.CreditCard;
import com.publicis.sapient.cardprocessing.mapper.CreditCardMapper;
import com.publicis.sapient.cardprocessing.service.CardProcessingService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/v1/credit/")
public class CardProcessingController extends BaseController {


    private final CardProcessingService cardProcessingService;
    private final CreditCardMapper creditCardMapper;

    public CardProcessingController(final CardProcessingService cardProcessingService, final CreditCardMapper creditCardMapper) {
        this.cardProcessingService = cardProcessingService;
        this.creditCardMapper = creditCardMapper;
    }

    @ApiOperation(value = "It will add a new Credit Card")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Credit Card created"),
            @ApiResponse(code = 400, message = "Invalid request fields/ Using the same credit card which is already added")
    })
    @PostMapping(path = "cards", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createCreditCard(@Valid @RequestBody final CreditCardRequest creditCardRequest) {
        final CreditCard creditCard = cardProcessingService.createCreditCard(creditCardMapper.creditCardRequestToCreditCard(creditCardRequest));
        return ResponseEntity.created(locationByEntity(creditCard.getId())).build();
    }

    @ApiOperation(value = "It will get a single Credit Card")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Credit Card searched and displayed"),
            @ApiResponse(code = 404, message = "no credit card exists with this id")
    })
    @GetMapping("cards/{id}")
    public ResponseEntity<CreditCardResponse> getByCreditCardId(@PathVariable final long id) {
        return ResponseEntity.ok(cardProcessingService.getById(id));
    }

    @ApiOperation(value = "It will get all Credit Cards saved")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Credit Cards has been searched and displayed"),
            @ApiResponse(code = 404, message = "no credit cards to show")
    })
    @GetMapping("cards")
    public ResponseEntity<PagedModel<CreditCardResponse>> getAllCreditCards(@PageableDefault(size = 2, sort = {"id"}) final Pageable pageable) {
        return ResponseEntity.ok(cardProcessingService.getAllCreditCard(pageable));
    }
}
