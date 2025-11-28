package org.simplecash.advisor;

import org.simplecash.advisor.dto.AdvisorRequest;
import org.simplecash.advisor.dto.AdvisorResponse;
import org.simplecash.agency.Agency;
import org.simplecash.agency.AgencyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AdvisorService {

    private final AdvisorRepository advisorRepository;
    private final AgencyRepository agencyRepository;

    public AdvisorService(AdvisorRepository advisorRepository,
                          AgencyRepository agencyRepository) {
        this.advisorRepository = advisorRepository;
        this.agencyRepository = agencyRepository;
    }

    public List<AdvisorResponse> getAll() {
        return advisorRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public AdvisorResponse getById(Long id) {
        Advisor advisor = advisorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Advisor not found"));
        return toResponse(advisor);
    }

    public AdvisorResponse create(AdvisorRequest request) {
        Agency agency = agencyRepository.findById(request.agencyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Agency not found"));

        Advisor advisor = new Advisor(request.lastName(), request.firstName(), agency);
        Advisor saved = advisorRepository.save(advisor);
        return toResponse(saved);
    }

    public AdvisorResponse update(Long id, AdvisorRequest request) {
        Advisor advisor = advisorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Advisor not found"));

        Agency agency = agencyRepository.findById(request.agencyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Agency not found"));

        advisor.setLastName(request.lastName());
        advisor.setFirstName(request.firstName());
        advisor.setAgency(agency);

        Advisor saved = advisorRepository.save(advisor);
        return toResponse(saved);
    }

    public void delete(Long id) {
        if (!advisorRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Advisor not found");
        }
        advisorRepository.deleteById(id);
    }

    private AdvisorResponse toResponse(Advisor advisor) {
        Long agencyId = advisor.getAgency() != null ? advisor.getAgency().getId() : null;

        return new AdvisorResponse(
                advisor.getId(),
                advisor.getLastName(),
                advisor.getFirstName(),
                agencyId
        );
    }
}
