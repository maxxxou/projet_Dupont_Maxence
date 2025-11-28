package org.simplecash.audit.dto;

import java.math.BigDecimal;

public record AuditSummaryResponse(
        BigDecimal totalCredit,
        BigDecimal totalDebit,
        long creditAccountsCount,
        long debitAccountsCount
) {
}
