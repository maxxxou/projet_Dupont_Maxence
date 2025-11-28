package org.simplecash.advisor;

import jakarta.validation.Valid;
import org.simplecash.advisor.dto.AdvisorRequest;
import org.simplecash.advisor.dto.AdvisorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/advisors")
public class AdvisorController {

    private final AdvisorService service;

    public AdvisorController(AdvisorService service) {
        this.service = service;
    }

    @GetMapping
    public List<AdvisorResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public AdvisorResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public ResponseEntity<AdvisorResponse> create(@Valid @RequestBody AdvisorRequest request) {
        AdvisorResponse created = service.create(request);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public AdvisorResponse update(@PathVariable Long id, @Valid @RequestBody AdvisorRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
