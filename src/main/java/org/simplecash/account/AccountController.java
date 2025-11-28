package org.simplecash.account;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.simplecash.account.dto.AccountResponse;
import org.simplecash.account.dto.AmountRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Accounts", description = "Operations on bank accounts")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @GetMapping("/api/clients/{clientId}/accounts")
    @Operation(
            summary = "List accounts for a client",
            description = "Returns all accounts (current and savings) for the given client."
    )
    public List<AccountResponse> getAccountsForClient(@PathVariable Long clientId) {
        return service.getAccountsForClient(clientId);
    }

    @PostMapping("/api/clients/{clientId}/accounts/current")
    @Operation(
            summary = "Create a current account",
            description = "Creates a new current account for the given client with default overdraft limit."
    )
    public ResponseEntity<AccountResponse> createCurrentAccount(@PathVariable Long clientId) {
        AccountResponse created = service.createCurrentAccount(clientId);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/api/clients/{clientId}/accounts/savings")
    @Operation(
            summary = "Create a savings account",
            description = "Creates a new savings account for the given client with default interest rate."
    )
    public ResponseEntity<AccountResponse> createSavingsAccount(@PathVariable Long clientId) {
        AccountResponse created = service.createSavingsAccount(clientId);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/api/accounts/{accountId}/credit")
    @Operation(
            summary = "Credit an account",
            description = "Adds the given amount to the account balance."
    )
    public ResponseEntity<AccountResponse> credit(@PathVariable Long accountId,
                                                  @Valid @RequestBody AmountRequest request) {
        AccountResponse updated = service.credit(accountId, request);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/api/accounts/{accountId}/debit")
    @Operation(
            summary = "Debit an account",
            description = "Subtracts the given amount from the account balance, checking overdraft rules."
    )
    public ResponseEntity<AccountResponse> debit(@PathVariable Long accountId,
                                                 @Valid @RequestBody AmountRequest request) {
        AccountResponse updated = service.debit(accountId, request);
        return ResponseEntity.ok(updated);
    }
}
