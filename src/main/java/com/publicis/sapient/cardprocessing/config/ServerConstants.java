package com.publicis.sapient.cardprocessing.config;

public final class ServerConstants {

    //Todo: To use multi currency, we need to save a predefined type def enum in db and look out for currency conversion
    public static final String DEFAULT_CURRENCY = " £";

    public static final String INVALID_FORMAT = "Invalid Format";
    public static final String NOT_NUMERIC = "String should be all numeric";
    public static final String CARD_NUMBER_UNIQUE_CONSTRAINTS = "UK_RSFIXPI1CRKLJ327162YLMQ5H_INDEX_8";
    public static final String CARD_NUMBER_UNIQUE_CONSTRAINTS_ERR_MESSAGE = "Credit Card number cannot be saved it is already in the db";
}
