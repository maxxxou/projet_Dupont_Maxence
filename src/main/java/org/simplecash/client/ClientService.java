package org.simplecash.client;

import org.simplecash.advisor.Advisor;
import org.simplecash.advisor.AdvisorRepository;
import org.simplecash.client.dto.ClientRequest;
import org.simplecash.client.dto.ClientResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository repository;
    private final AdvisorRepository advisorRepository;

    public ClientService(ClientRepository repository, AdvisorRepository advisorRepository) {
        this.repository = repository;
        this.advisorRepository = advisorRepository;
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

        if (request.clientType() != null) {
            client.setType(request.clientType());
        }

        if (request.advisorId() != null) {
            Advisor advisor = advisorRepository.findById(request.advisorId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Advisor not found"));
            client.setAdvisor(advisor);
        }

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

        if (request.clientType() != null) {
            client.setType(request.clientType());
        }

        if (request.advisorId() != null) {
            Advisor advisor = advisorRepository.findById(request.advisorId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Advisor not found"));
            client.setAdvisor(advisor);
        } else {
            client.setAdvisor(null);
        }

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
        Long advisorId = client.getAdvisor() != null ? client.getAdvisor().getId() : null;
        ClientType type = client.getType();
        return new ClientResponse(
                client.getId(),
                client.getLastName(),
                client.getFirstName(),
                client.getAddress(),
                client.getPostalCode(),
                client.getCity(),
                client.getPhone(),
                advisorId,
                type
        );
    }
}
