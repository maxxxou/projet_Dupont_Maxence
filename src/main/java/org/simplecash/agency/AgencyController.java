package org.simplecash.agency;

import jakarta.validation.Valid;
import org.simplecash.agency.dto.AgencyRequest;
import org.simplecash.agency.dto.AgencyResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agencies")
public class AgencyController {

    private final AgencyService service;

    public AgencyController(AgencyService service) {
        this.service = service;
    }

    @GetMapping
    public List<AgencyResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public AgencyResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public ResponseEntity<AgencyResponse> create(@Valid @RequestBody AgencyRequest request) {
        AgencyResponse response = service.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public AgencyResponse update(@PathVariable Long id, @Valid @RequestBody AgencyRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
