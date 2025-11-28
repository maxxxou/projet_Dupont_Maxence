package org.simplecash.account.dto;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AmountRequest(
        @Positive BigDecimal amount
) {
}
