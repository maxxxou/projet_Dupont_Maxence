package org.simplecash.advisor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.simplecash.advisor.dto.AdvisorRequest;
import org.simplecash.advisor.dto.AdvisorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/advisors")
@Tag(name = "Advisors", description = "Operations on bank advisors")
public class AdvisorController {

    private final AdvisorService service;

    public AdvisorController(AdvisorService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(
            summary = "List all advisors",
            description = "Returns all advisors registered in the system."
    )
    public List<AdvisorResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get advisor by id",
            description = "Returns a single advisor by its identifier."
    )
    public AdvisorResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @Operation(
            summary = "Create advisor",
            description = "Creates a new advisor attached to an existing agency."
    )
    public ResponseEntity<AdvisorResponse> create(@Valid @RequestBody AdvisorRequest request) {
        AdvisorResponse created = service.create(request);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update advisor",
            description = "Updates advisor information and its agency."
    )
    public AdvisorResponse update(@PathVariable Long id, @Valid @RequestBody AdvisorRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete advisor",
            description = "Deletes an advisor if it has no clients attached."
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
