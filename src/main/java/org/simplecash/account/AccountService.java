package org.simplecash.account;

import org.simplecash.account.dto.AccountResponse;
import org.simplecash.account.dto.AmountRequest;
import org.simplecash.client.Client;
import org.simplecash.client.ClientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;

    public AccountService(AccountRepository accountRepository, ClientRepository clientRepository) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
    }

    public List<AccountResponse> getAccountsForClient(Long clientId) {
        return accountRepository.findByClientId(clientId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public AccountResponse createCurrentAccount(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
        String accountNumber = UUID.randomUUID().toString();
        Account account = new Account(accountNumber, AccountType.CURRENT, client);
        Account saved = accountRepository.save(account);
        return toResponse(saved);
    }

    @Transactional
    public AccountResponse createSavingsAccount(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
        String accountNumber = UUID.randomUUID().toString();
        Account account = new Account(accountNumber, AccountType.SAVINGS, client);
        Account saved = accountRepository.save(account);
        return toResponse(saved);
    }

    @Transactional
    public AccountResponse credit(Long accountId, AmountRequest request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        BigDecimal newBalance = account.getBalance().add(request.amount());
        account.setBalance(newBalance);
        Account saved = accountRepository.save(account);
        return toResponse(saved);
    }

    @Transactional
    public AccountResponse debit(Long accountId, AmountRequest request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        BigDecimal newBalance = account.getBalance().subtract(request.amount());

        if (account.getType() == AccountType.CURRENT) {
            BigDecimal limit = account.getOverdraftLimit() != null
                    ? account.getOverdraftLimit()
                    : Account.DEFAULT_OVERDRAFT_LIMIT;
            BigDecimal minAllowed = limit.negate();
            if (newBalance.compareTo(minAllowed) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Overdraft limit exceeded");
            }
        } else if (account.getType() == AccountType.SAVINGS) {
            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Savings account cannot be overdrawn");
            }
        }

        account.setBalance(newBalance);
        Account saved = accountRepository.save(account);
        return toResponse(saved);
    }

    @Transactional
    public void delete(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Account balance must be zero to close account"
            );
        }

        accountRepository.delete(account);
    }

    private AccountResponse toResponse(Account account) {
        Long clientId = account.getClient() != null ? account.getClient().getId() : null;
        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getOpenedAt(),
                account.getType(),
                account.getOverdraftLimit(),
                account.getInterestRate(),
                clientId
        );
    }
}
