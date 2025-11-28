package org.simplecash.advisor;

import org.simplecash.advisor.dto.AdvisorRequest;
import org.simplecash.advisor.dto.AdvisorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AdvisorService {

    private final AdvisorRepository repository;

    public AdvisorService(AdvisorRepository repository) {
        this.repository = repository;
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
        Advisor advisor = new Advisor(request.lastName(), request.firstName());
        Advisor saved = repository.save(advisor);
        return toResponse(saved);
    }

    public AdvisorResponse update(Long id, AdvisorRequest request) {
        Advisor advisor = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Advisor not found"));
        advisor.setLastName(request.lastName());
        advisor.setFirstName(request.firstName());
        Advisor saved = repository.save(advisor);
        return toResponse(saved);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Advisor not found");
        }
        repository.deleteById(id);
    }

    private AdvisorResponse toResponse(Advisor advisor) {
        return new AdvisorResponse(advisor.getId(), advisor.getLastName(), advisor.getFirstName());
    }
}
