package com.publicis.sapient.cardprocessing.dao;

import com.publicis.sapient.cardprocessing.entity.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardDao extends JpaRepository<CreditCard, Long> {

    CreditCard findByCardNumber(Long cardNumber);
}
