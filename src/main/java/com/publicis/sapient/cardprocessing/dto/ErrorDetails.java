package com.publicis.sapient.cardprocessing.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ErrorDetails {
    private LocalDateTime timestamp;
    private String message;
    private String details;
}
