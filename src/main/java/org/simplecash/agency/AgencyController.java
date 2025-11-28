package org.simplecash.agency;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.simplecash.agency.dto.AgencyRequest;
import org.simplecash.agency.dto.AgencyResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agencies")
@Tag(name = "Agencies", description = "Operations on bank agencies and their managers")
public class AgencyController {

    private final AgencyService service;

    public AgencyController(AgencyService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(
            summary = "List all agencies",
            description = "Returns all agencies with their managers."
    )
    public List<AgencyResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get agency by id",
            description = "Returns a single agency by its identifier."
    )
    public AgencyResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @Operation(
            summary = "Create agency",
            description = "Creates a new agency and its manager."
    )
    public ResponseEntity<AgencyResponse> create(@Valid @RequestBody AgencyRequest request) {
        AgencyResponse response = service.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update agency",
            description = "Updates agency basic information and its manager."
    )
    public AgencyResponse update(@PathVariable Long id, @Valid @RequestBody AgencyRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete agency",
            description = "Deletes an agency if there are no advisors or clients attached."
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
