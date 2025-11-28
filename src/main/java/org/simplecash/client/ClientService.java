package org.simplecash.client;

import org.simplecash.client.dto.ClientRequest;
import org.simplecash.client.dto.ClientResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository repository;

    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    public List<ClientResponse> getAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public ClientResponse getById(Long id) {
        Client client = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
        return toResponse(client);
    }

    public ClientResponse create(ClientRequest request) {
        Client client = new Client(
                request.lastName(),
                request.firstName(),
                request.address(),
                request.postalCode(),
                request.city(),
                request.phone()
        );
        Client saved = repository.save(client);
        return toResponse(saved);
    }

    public ClientResponse update(Long id, ClientRequest request) {
        Client client = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        client.setLastName(request.lastName());
        client.setFirstName(request.firstName());
        client.setAddress(request.address());
        client.setPostalCode(request.postalCode());
        client.setCity(request.city());
        client.setPhone(request.phone());

        Client saved = repository.save(client);
        return toResponse(saved);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found");
        }
        repository.deleteById(id);
    }

    private ClientResponse toResponse(Client client) {
        return new ClientResponse(
                client.getId(),
                client.getLastName(),
                client.getFirstName(),
                client.getAddress(),
                client.getPostalCode(),
                client.getCity(),
                client.getPhone()
        );
    }
}
