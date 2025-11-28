package org.simplecash.account;

import jakarta.validation.Valid;
import org.simplecash.account.dto.AccountResponse;
import org.simplecash.account.dto.AmountRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @GetMapping("/api/clients/{clientId}/accounts")
    public List<AccountResponse> getAccountsForClient(@PathVariable Long clientId) {
        return service.getAccountsForClient(clientId);
    }

    @PostMapping("/api/clients/{clientId}/accounts/current")
    public ResponseEntity<AccountResponse> createCurrentAccount(@PathVariable Long clientId) {
        AccountResponse created = service.createCurrentAccount(clientId);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/api/clients/{clientId}/accounts/savings")
    public ResponseEntity<AccountResponse> createSavingsAccount(@PathVariable Long clientId) {
        AccountResponse created = service.createSavingsAccount(clientId);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/api/accounts/{accountId}/credit")
    public ResponseEntity<AccountResponse> credit(@PathVariable Long accountId,
                                                  @Valid @RequestBody AmountRequest request) {
        AccountResponse updated = service.credit(accountId, request);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/api/accounts/{accountId}/debit")
    public ResponseEntity<AccountResponse> debit(@PathVariable Long accountId,
                                                 @Valid @RequestBody AmountRequest request) {
        AccountResponse updated = service.debit(accountId, request);
        return ResponseEntity.ok(updated);
    }
}
