package org.simplecash.account;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.simplecash.account.dto.AmountRequest;
import org.simplecash.client.Client;
import org.simplecash.client.ClientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    AccountRepository accountRepository;

    @Mock
    ClientRepository clientRepository;

    @InjectMocks
    AccountService accountService;

    @Test
    void debit_current_account_within_overdraft_is_allowed() {
        Client client = new Client("Doe", "John", "addr", "75001", "Paris", "0600000000");
        Account account = new Account("ACC1", AccountType.CURRENT, client);
        account.setBalance(BigDecimal.ZERO);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

        AmountRequest request = new AmountRequest(BigDecimal.valueOf(500));

        var response = accountService.debit(1L, request);

        assertThat(response.balance()).isEqualTo(BigDecimal.valueOf(-500));
    }

    @Test
    void debit_current_account_exceeding_overdraft_is_rejected() {
        Client client = new Client("Doe", "John", "addr", "75001", "Paris", "0600000000");
        Account account = new Account("ACC1", AccountType.CURRENT, client);
        account.setBalance(BigDecimal.valueOf(-900));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        AmountRequest request = new AmountRequest(BigDecimal.valueOf(200));

        assertThatThrownBy(() -> accountService.debit(1L, request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Overdraft limit exceeded")
                .extracting(ex -> ((ResponseStatusException) ex).getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void debit_savings_account_below_zero_is_rejected() {
        Client client = new Client("Doe", "John", "addr", "75001", "Paris", "0600000000");
        Account account = new Account("ACC2", AccountType.SAVINGS, client);
        account.setBalance(BigDecimal.valueOf(100));

        when(accountRepository.findById(2L)).thenReturn(Optional.of(account));

        AmountRequest request = new AmountRequest(BigDecimal.valueOf(200));

        assertThatThrownBy(() -> accountService.debit(2L, request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Savings account cannot be overdrawn")
                .extracting(ex -> ((ResponseStatusException) ex).getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void credit_increases_balance() {
        Client client = new Client("Doe", "John", "addr", "75001", "Paris", "0600000000");
        Account account = new Account("ACC3", AccountType.CURRENT, client);
        account.setBalance(BigDecimal.valueOf(100));

        when(accountRepository.findById(3L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

        AmountRequest request = new AmountRequest(BigDecimal.valueOf(50));

        var response = accountService.credit(3L, request);

        assertThat(response.balance()).isEqualTo(BigDecimal.valueOf(150));
    }
}
