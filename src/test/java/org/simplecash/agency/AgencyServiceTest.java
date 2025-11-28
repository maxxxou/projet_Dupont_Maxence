package org.simplecash.agency;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.simplecash.agency.dto.AgencyRequest;
import org.simplecash.agency.dto.AgencyResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgencyServiceTest {

    @Mock
    AgencyRepository agencyRepository;

    @Mock
    ManagerRepository managerRepository;

    @InjectMocks
    AgencyService agencyService;

    @Test
    void create_agency_persists_manager_and_agency() {
        LocalDate date = LocalDate.of(2024, 11, 28);
        AgencyRequest request = new AgencyRequest(
                "A1234",
                date,
                "Martin",
                "Sophie"
        );

        Manager savedManager = new Manager("Martin", "Sophie");
        Agency savedAgency = new Agency("A1234", date, savedManager);

        when(managerRepository.save(any(Manager.class))).thenReturn(savedManager);
        when(agencyRepository.save(any(Agency.class))).thenReturn(savedAgency);

        AgencyResponse response = agencyService.create(request);

        assertThat(response.code()).isEqualTo("A1234");
        assertThat(response.managerLastName()).isEqualTo("Martin");
        assertThat(response.managerFirstName()).isEqualTo("Sophie");
    }

    @Test
    void getById_throws_when_agency_not_found() {
        when(agencyRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> agencyService.getById(42L))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(ex -> ((ResponseStatusException) ex).getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
    }
}
