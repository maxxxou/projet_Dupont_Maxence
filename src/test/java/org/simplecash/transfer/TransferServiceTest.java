package org.simplecash.transfer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.simplecash.account.Account;
import org.simplecash.account.AccountRepository;
import org.simplecash.account.AccountService;
import org.simplecash.account.AccountType;
import org.simplecash.transfer.dto.TransferRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    AccountRepository accountRepository;

    @Mock
    AccountService accountService;

    @Mock
    TransferRepository transferRepository;

    @InjectMocks
    TransferService transferService;

    @Test
    void transfer_rejects_same_source_and_destination_account() {
        TransferRequest request = new TransferRequest(1L, 1L, BigDecimal.valueOf(100), "test");

        assertThatThrownBy(() -> transferService.transfer(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Source and destination accounts must be different")
                .extracting(ex -> ((ResponseStatusException) ex).getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void transfer_calls_debit_and_credit_and_persists_transfer() {
        Account from = new Account("A1", AccountType.CURRENT, null);
        Account to = new Account("A2", AccountType.SAVINGS, null);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(from));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(to));
        when(transferRepository.save(any(Transfer.class))).thenAnswer(inv -> inv.getArgument(0));

        TransferRequest request = new TransferRequest(1L, 2L, BigDecimal.valueOf(50), "test");

        transferService.transfer(request);

        verify(accountService).debit(eq(1L), any());
        verify(accountService).credit(eq(2L), any());
        verify(transferRepository).save(any(Transfer.class));
    }
}
