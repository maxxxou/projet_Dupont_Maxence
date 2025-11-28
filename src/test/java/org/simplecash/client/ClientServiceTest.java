package org.simplecash.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.simplecash.advisor.AdvisorRepository;
import org.simplecash.agency.Agency;
import org.simplecash.agency.AgencyRepository;
import org.simplecash.client.dto.ClientRequest;
import org.simplecash.client.dto.ClientResponse;
import org.simplecash.client.ClientType;
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
class ClientServiceTest {

    @Mock
    ClientRepository clientRepository;

    @Mock
    AdvisorRepository advisorRepository;

    @Mock
    AgencyRepository agencyRepository;

    @InjectMocks
    ClientService clientService;

    @Test
    void create_client_with_personal_type_by_default() {
        Agency agency = new Agency("A1234", LocalDate.of(2024, 11, 28), null);

        when(agencyRepository.findById(1L)).thenReturn(Optional.of(agency));

        ClientRequest request = new ClientRequest(
                "Doe",
                "John",
                "Some street",
                "75001",
                "Paris",
                "0600000000",
                1L,      // agencyId obligatoire
                null,    // advisorId
                null     // clientType -> PERSONAL par défaut
        );

        Client savedClient = new Client(
                request.lastName(),
                request.firstName(),
                request.address(),
                request.postalCode(),
                request.city(),
                request.phone()
        );
        savedClient.setType(ClientType.PERSONAL);
        savedClient.setAgency(agency);

        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);

        ClientResponse response = clientService.create(request);

        assertThat(response.lastName()).isEqualTo("Doe");
        assertThat(response.clientType()).isEqualTo(ClientType.PERSONAL);
        assertThat(response.agencyId()).isEqualTo(agency.getId());
    }

    @Test
    void getById_throws_when_client_not_found() {
        when(clientRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clientService.getById(42L))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(ex -> ((ResponseStatusException) ex).getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
    }
}
