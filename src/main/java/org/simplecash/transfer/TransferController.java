package org.simplecash.transfer;

import jakarta.validation.Valid;
import org.simplecash.transfer.dto.TransferRequest;
import org.simplecash.transfer.dto.TransferResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService service;

    public TransferController(TransferService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TransferResponse> create(@Valid @RequestBody TransferRequest request) {
        TransferResponse response = service.transfer(request);
        return ResponseEntity.ok(response);
    }
}
