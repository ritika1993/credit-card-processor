package com.publicis.sapient.cardprocessing.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "credit_cards", indexes = {
        @Index(name = "unique_card_number", columnList = "card_number")
})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "card_number", unique = true, nullable = false)
    private String cardNumber;

    @Column(name = "limit_amount", nullable = false, columnDefinition = "DECIMAL(7,2)")
    private BigDecimal limit;

    @Column(name = "balance", nullable = false, columnDefinition = "DECIMAL(7,2)")
    @Builder.Default
    private BigDecimal balance = BigDecimal.valueOf(0.0);
}
