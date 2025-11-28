package org.simplecash.agency;

import org.simplecash.agency.dto.AgencyRequest;
import org.simplecash.agency.dto.AgencyResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class AgencyService {

    private final AgencyRepository agencyRepository;
    private final ManagerRepository managerRepository;

    public AgencyService(AgencyRepository agencyRepository, ManagerRepository managerRepository) {
        this.agencyRepository = agencyRepository;
        this.managerRepository = managerRepository;
    }

    public List<AgencyResponse> getAll() {
        return agencyRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public AgencyResponse getById(Long id) {
        Agency agency = agencyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agency not found"));
        return toResponse(agency);
    }

    public AgencyResponse create(AgencyRequest request) {
        LocalDate createdAt = request.createdAt() != null ? request.createdAt() : LocalDate.now();

        Manager manager = new Manager(request.managerLastName(), request.managerFirstName());
        Manager savedManager = managerRepository.save(manager);

        Agency agency = new Agency(request.code(), createdAt, savedManager);
        Agency savedAgency = agencyRepository.save(agency);

        return toResponse(savedAgency);
    }

    public AgencyResponse update(Long id, AgencyRequest request) {
        Agency agency = agencyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agency not found"));

        agency.setCode(request.code());
        agency.setCreatedAt(request.createdAt() != null ? request.createdAt() : agency.getCreatedAt());

        Manager manager = agency.getManager();
        if (manager == null) {
            manager = new Manager(request.managerLastName(), request.managerFirstName());
        } else {
            manager.setLastName(request.managerLastName());
            manager.setFirstName(request.managerFirstName());
        }
        Manager savedManager = managerRepository.save(manager);
        agency.setManager(savedManager);

        Agency savedAgency = agencyRepository.save(agency);
        return toResponse(savedAgency);
    }

    public void delete(Long id) {
        if (!agencyRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Agency not found");
        }
        agencyRepository.deleteById(id);
    }

    private AgencyResponse toResponse(Agency agency) {
        Manager manager = agency.getManager();
        Long managerId = manager != null ? manager.getId() : null;
        String lastName = manager != null ? manager.getLastName() : null;
        String firstName = manager != null ? manager.getFirstName() : null;

        return new AgencyResponse(
                agency.getId(),
                agency.getCode(),
                agency.getCreatedAt(),
                managerId,
                lastName,
                firstName
        );
    }
}
