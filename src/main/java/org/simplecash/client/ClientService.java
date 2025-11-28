package org.simplecash.client;

import org.simplecash.account.Account;
import org.simplecash.account.AccountRepository;
import org.simplecash.advisor.Advisor;
import org.simplecash.advisor.AdvisorRepository;
import org.simplecash.agency.Agency;
import org.simplecash.agency.AgencyRepository;
import org.simplecash.client.dto.ClientRequest;
import org.simplecash.client.dto.ClientResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final AdvisorRepository advisorRepository;
    private final AgencyRepository agencyRepository;
    private final AccountRepository accountRepository;

    public ClientService(ClientRepository clientRepository,
                         AdvisorRepository advisorRepository,
                         AgencyRepository agencyRepository,
                         AccountRepository accountRepository) {
        this.clientRepository = clientRepository;
        this.advisorRepository = advisorRepository;
        this.agencyRepository = agencyRepository;
        this.accountRepository = accountRepository;
    }

    public List<ClientResponse> getAll() {
        return clientRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public ClientResponse getById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
        return toResponse(client);
    }

    public ClientResponse create(ClientRequest request) {
        Agency agency = agencyRepository.findById(request.agencyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Agency not found"));

        Advisor advisor = null;
        if (request.advisorId() != null) {
            advisor = advisorRepository.findById(request.advisorId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Advisor not found"));
        }

        Client client = new Client(
                request.lastName(),
                request.firstName(),
                request.address(),
                request.postalCode(),
                request.city(),
                request.phone()
        );

        client.setAgency(agency);

        if (request.clientType() != null) {
            client.setType(request.clientType());
        }

        if (advisor != null) {
            client.setAdvisor(advisor);
        }

        Client saved = clientRepository.save(client);
        return toResponse(saved);
    }

    public ClientResponse update(Long id, ClientRequest request) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        Agency agency = agencyRepository.findById(request.agencyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Agency not found"));

        Advisor advisor = null;
        if (request.advisorId() != null) {
            advisor = advisorRepository.findById(request.advisorId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Advisor not found"));
        }

        client.setLastName(request.lastName());
        client.setFirstName(request.firstName());
        client.setAddress(request.address());
        client.setPostalCode(request.postalCode());
        client.setCity(request.city());
        client.setPhone(request.phone());
        client.setAgency(agency);

        if (request.clientType() != null) {
            client.setType(request.clientType());
        }

        client.setAdvisor(advisor);

        Client saved = clientRepository.save(client);
        return toResponse(saved);
    }

    public void delete(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        List<Account> accounts = accountRepository.findByClientId(id);

        boolean hasNonZeroBalance = accounts.stream()
                .anyMatch(a -> a.getBalance().compareTo(BigDecimal.ZERO) != 0);

        if (hasNonZeroBalance) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Client has accounts with non-zero balance"
            );
        }

        if (!accounts.isEmpty()) {
            accountRepository.deleteAll(accounts);
        }

        clientRepository.delete(client);
    }

    private ClientResponse toResponse(Client client) {
        Long agencyId = client.getAgency() != null ? client.getAgency().getId() : null;
        Long advisorId = client.getAdvisor() != null ? client.getAdvisor().getId() : null;

        return new ClientResponse(
                client.getId(),
                client.getLastName(),
                client.getFirstName(),
                client.getAddress(),
                client.getPostalCode(),
                client.getCity(),
                client.getPhone(),
                agencyId,
                advisorId,
                client.getType()
        );
    }
}
