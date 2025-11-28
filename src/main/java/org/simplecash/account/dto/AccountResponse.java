package org.simplecash.account.dto;

import org.simplecash.account.AccountType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AccountResponse(
        Long id,
        String accountNumber,
        BigDecimal balance,
        LocalDate openedAt,
        AccountType type,
        BigDecimal overdraftLimit,
        BigDecimal interestRate,
        Long clientId
) {
}
