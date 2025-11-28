package org.simplecash.advisor;

import org.simplecash.advisor.dto.AdvisorRequest;
import org.simplecash.advisor.dto.AdvisorResponse;
import org.simplecash.agency.Agency;
import org.simplecash.agency.AgencyRepository;
import org.simplecash.client.ClientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AdvisorService {

    private final AdvisorRepository repository;
    private final ClientRepository clientRepository;
    private final AgencyRepository agencyRepository;

    public AdvisorService(AdvisorRepository repository,
                          ClientRepository clientRepository,
                          AgencyRepository agencyRepository) {
        this.repository = repository;
        this.clientRepository = clientRepository;
        this.agencyRepository = agencyRepository;
    }

    public List<AdvisorResponse> getAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public AdvisorResponse getById(Long id) {
        Advisor advisor = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Advisor not found"));
        return toResponse(advisor);
    }

    public AdvisorResponse create(AdvisorRequest request) {
        Agency agency = agencyRepository.findById(request.agencyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Agency not found"));

        Advisor advisor = new Advisor(request.lastName(), request.firstName(), agency);

        Advisor saved = repository.save(advisor);
        return toResponse(saved);
    }

    public AdvisorResponse update(Long id, AdvisorRequest request) {
        Advisor advisor = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Advisor not found"));

        Agency agency = agencyRepository.findById(request.agencyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Agency not found"));

        advisor.setLastName(request.lastName());
        advisor.setFirstName(request.firstName());
        advisor.setAgency(agency);

        Advisor saved = repository.save(advisor);
        return toResponse(saved);
    }

    public void delete(Long id) {
        Advisor advisor = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Advisor not found"));

        long clientCount = clientRepository.countByAdvisorId(id);
        if (clientCount > 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Advisor cannot be deleted while having clients"
            );
        }

        repository.delete(advisor);
    }

    private AdvisorResponse toResponse(Advisor advisor) {
        Long agencyId = advisor.getAgency() != null ? advisor.getAgency().getId() : null;
        return new AdvisorResponse(advisor.getId(), advisor.getLastName(), advisor.getFirstName(), agencyId);
    }
}
