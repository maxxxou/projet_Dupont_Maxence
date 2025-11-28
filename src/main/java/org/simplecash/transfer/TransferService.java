package org.simplecash.transfer;

import org.simplecash.account.Account;
import org.simplecash.account.AccountRepository;
import org.simplecash.account.AccountService;
import org.simplecash.account.dto.AmountRequest;
import org.simplecash.transfer.dto.TransferRequest;
import org.simplecash.transfer.dto.TransferResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TransferService {

    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final TransferRepository transferRepository;

    public TransferService(AccountRepository accountRepository,
                           AccountService accountService,
                           TransferRepository transferRepository) {
        this.accountRepository = accountRepository;
        this.accountService = accountService;
        this.transferRepository = transferRepository;
    }

    @Transactional
    public TransferResponse transfer(TransferRequest request) {
        if (request.fromAccountId().equals(request.toAccountId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Source and destination accounts must be different");
        }

        Account fromAccount = accountRepository.findById(request.fromAccountId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Source account not found"));

        Account toAccount = accountRepository.findById(request.toAccountId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Destination account not found"));

        AmountRequest amountRequest = new AmountRequest(request.amount());
        accountService.debit(fromAccount.getId(), amountRequest);
        accountService.credit(toAccount.getId(), amountRequest);

        Transfer transfer = new Transfer(fromAccount, toAccount, request.amount(), request.label());
        Transfer saved = transferRepository.save(transfer);

        return new TransferResponse(
                saved.getId(),
                saved.getFromAccount().getId(),
                saved.getToAccount().getId(),
                saved.getAmount(),
                saved.getLabel(),
                saved.getExecutedAt()
        );
    }
}
