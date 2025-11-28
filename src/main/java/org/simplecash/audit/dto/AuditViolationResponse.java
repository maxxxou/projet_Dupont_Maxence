package org.simplecash.audit.dto;

import org.simplecash.client.ClientType;

import java.math.BigDecimal;

public record AuditViolationResponse(
        Long clientId,
        ClientType clientType,
        Long accountId,
        BigDecimal balance,
        BigDecimal maxAllowedDebit
) {
}
