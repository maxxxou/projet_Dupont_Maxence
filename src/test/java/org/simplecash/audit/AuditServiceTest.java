package org.simplecash.audit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.simplecash.account.Account;
import org.simplecash.account.AccountRepository;
import org.simplecash.account.AccountType;
import org.simplecash.audit.dto.AuditSummaryResponse;
import org.simplecash.audit.dto.AuditViolationResponse;
import org.simplecash.client.Client;
import org.simplecash.client.ClientType;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditServiceTest {

    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    AuditService auditService;

    @Test
    void violations_detects_personal_and_business_limits() {
        Client personalClient = new Client("Doe", "John", "addr", "75001", "Paris", "0600000000");
        personalClient.setType(ClientType.PERSONAL);
        Account personalAccount = new Account("P1", AccountType.CURRENT, personalClient);
        personalAccount.setBalance(BigDecimal.valueOf(-6000));

        Client businessClient = new Client("Corp", "Acme", "addr", "92000", "La Defense", "0100000000");
        businessClient.setType(ClientType.BUSINESS);
        Account okBusiness = new Account("B1", AccountType.CURRENT, businessClient);
        okBusiness.setBalance(BigDecimal.valueOf(-40000));

        Account badBusiness = new Account("B2", AccountType.CURRENT, businessClient);
        badBusiness.setBalance(BigDecimal.valueOf(-60000));

        when(accountRepository.findAll()).thenReturn(List.of(personalAccount, okBusiness, badBusiness));

        List<AuditViolationResponse> violations = auditService.findViolations();

        assertThat(violations).hasSize(2);
        assertThat(violations)
                .anyMatch(v -> v.clientType() == ClientType.PERSONAL && v.balance().equals(BigDecimal.valueOf(-6000)))
                .anyMatch(v -> v.clientType() == ClientType.BUSINESS && v.balance().equals(BigDecimal.valueOf(-60000)));
    }

    @Test
    void summary_computes_credit_and_debit_totals() {
        Account a1 = new Account("A1", AccountType.CURRENT, null);
        a1.setBalance(BigDecimal.valueOf(100));

        Account a2 = new Account("A2", AccountType.CURRENT, null);
        a2.setBalance(BigDecimal.valueOf(-50));

        Account a3 = new Account("A3", AccountType.CURRENT, null);
        a3.setBalance(BigDecimal.ZERO);

        when(accountRepository.findAll()).thenReturn(List.of(a1, a2, a3));

        AuditSummaryResponse summary = auditService.summary();

        assertThat(summary.totalCredit()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(summary.totalDebit()).isEqualTo(BigDecimal.valueOf(-50));
        assertThat(summary.creditAccountsCount()).isEqualTo(1);
        assertThat(summary.debitAccountsCount()).isEqualTo(1);
    }
}
