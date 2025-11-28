package org.simplecash.audit;

import org.simplecash.audit.dto.AuditSummaryResponse;
import org.simplecash.audit.dto.AuditViolationResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuditController {

    private final AuditService service;

    public AuditController(AuditService service) {
        this.service = service;
    }

    @GetMapping("/api/audit/violations")
    public List<AuditViolationResponse> violations() {
        return service.findViolations();
    }

    @GetMapping("/api/audit/summary")
    public AuditSummaryResponse summary() {
        return service.summary();
    }
}
