package org.simplecash.client;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.simplecash.client.dto.ClientRequest;
import org.simplecash.client.dto.ClientResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@Tag(name = "Clients", description = "Operations on bank clients")
public class ClientController {

    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(
            summary = "List all clients",
            description = "Returns all clients with their agency and advisor when available."
    )
    public List<ClientResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get client by id",
            description = "Returns a single client by its identifier."
    )
    public ClientResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @Operation(
            summary = "Create client",
            description = "Creates a new client attached to an existing agency and optionally to an advisor."
    )
    public ResponseEntity<ClientResponse> create(@Valid @RequestBody ClientRequest request) {
        ClientResponse created = service.create(request);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update client",
            description = "Updates client information and its agency/advisor."
    )
    public ClientResponse update(@PathVariable Long id, @Valid @RequestBody ClientRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete client",
            description = "Deletes a client if all business rules are satisfied (for example accounts handling)."
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
