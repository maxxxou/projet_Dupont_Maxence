package org.simplecash.audit;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.simplecash.audit.dto.AuditSummaryResponse;
import org.simplecash.audit.dto.AuditViolationResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Audit", description = "Audit operations on bank accounts")
public class AuditController {

    private final AuditService service;

    public AuditController(AuditService service) {
        this.service = service;
    }

    @GetMapping("/api/audit/violations")
    @Operation(
            summary = "List audit violations",
            description = "Returns all accounts that exceed allowed overdraft thresholds depending on client type."
    )
    public List<AuditViolationResponse> violations() {
        return service.findViolations();
    }

    @GetMapping("/api/audit/summary")
    @Operation(
            summary = "Global audit summary",
            description = "Returns aggregated information about credit and debit balances across all accounts."
    )
    public AuditSummaryResponse summary() {
        return service.summary();
    }
}
