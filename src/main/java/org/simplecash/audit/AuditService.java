package org.simplecash.audit;

import org.simplecash.account.Account;
import org.simplecash.account.AccountRepository;
import org.simplecash.audit.dto.AuditSummaryResponse;
import org.simplecash.audit.dto.AuditViolationResponse;
import org.simplecash.client.Client;
import org.simplecash.client.ClientType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AuditService {

    private final AccountRepository accountRepository;

    public AuditService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<AuditViolationResponse> findViolations() {
        List<Account> accounts = accountRepository.findAll();

        return accounts.stream()
                .filter(account -> account.getClient() != null)
                .map(account -> {
                    Client client = account.getClient();
                    ClientType type = client.getType();
                    if (type == null) {
                        type = ClientType.PERSONAL;
                    }

                    BigDecimal balance = account.getBalance();
                    if (balance == null) {
                        balance = BigDecimal.ZERO;
                    }

                    BigDecimal maxAllowedDebit;
                    if (type == ClientType.PERSONAL) {
                        maxAllowedDebit = BigDecimal.valueOf(-5000);
                    } else {
                        maxAllowedDebit = BigDecimal.valueOf(-50000);
                    }

                    boolean violation = balance.compareTo(maxAllowedDebit) < 0;
                    if (!violation) {
                        return null;
                    }

                    return new AuditViolationResponse(
                            client.getId(),
                            type,
                            account.getId(),
                            balance,
                            maxAllowedDebit
                    );
                })
                .filter(v -> v != null)
                .toList();
    }

    public AuditSummaryResponse summary() {
        List<Account> accounts = accountRepository.findAll();

        BigDecimal totalCredit = BigDecimal.ZERO;
        BigDecimal totalDebit = BigDecimal.ZERO;
        long creditCount = 0;
        long debitCount = 0;

        for (Account account : accounts) {
            BigDecimal balance = account.getBalance();
            if (balance == null) {
                balance = BigDecimal.ZERO;
            }

            int cmp = balance.compareTo(BigDecimal.ZERO);
            if (cmp > 0) {
                totalCredit = totalCredit.add(balance);
                creditCount++;
            } else if (cmp < 0) {
                totalDebit = totalDebit.add(balance);
                debitCount++;
            }
        }

        return new AuditSummaryResponse(
                totalCredit,
                totalDebit,
                creditCount,
                debitCount
        );
    }
}
