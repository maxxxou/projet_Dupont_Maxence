package org.simplecash.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.simplecash.advisor.Advisor;
import org.simplecash.advisor.AdvisorRepository;
import org.simplecash.client.dto.ClientRequest;
import org.simplecash.client.dto.ClientResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @InjectMocks
    ClientService clientService;

    @Test
    void create_client_with_personal_type_by_default() {
        ClientRequest request = new ClientRequest(
                "Doe",
                "John",
                "Some street",
                "75001",
                "Paris",
                "0600000000",
                null,
                null
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

        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);

        ClientResponse response = clientService.create(request);

        assertThat(response.lastName()).isEqualTo("Doe");
        assertThat(response.clientType()).isEqualTo(ClientType.PERSONAL);
    }

    @Test
    void create_client_with_business_type() {
        ClientRequest request = new ClientRequest(
                "Corp",
                "Acme",
                "HQ street",
                "92000",
                "La Defense",
                "0100000000",
                null,
                ClientType.BUSINESS
        );

        Client savedClient = new Client(
                request.lastName(),
                request.firstName(),
                request.address(),
                request.postalCode(),
                request.city(),
                request.phone()
        );
        savedClient.setType(ClientType.BUSINESS);

        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);

        ClientResponse response = clientService.create(request);

        assertThat(response.clientType()).isEqualTo(ClientType.BUSINESS);
    }

    @Test
    void getById_throws_when_client_not_found() {
        when(clientRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clientService.getById(42L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Client not found")
                .extracting(ex -> ((ResponseStatusException) ex).getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void create_client_with_advisor() throws IllegalAccessException, NoSuchFieldException {
        Advisor advisor = new Advisor("Martin", "Sophie");
        // simulate an ID
        var advisorField = advisor.getClass().getDeclaredField("id");
        advisorField.setAccessible(true);
        advisorField.set(advisor, 10L);

        ClientRequest request = new ClientRequest(
                "Doe",
                "Jane",
                "Some street",
                "75001",
                "Paris",
                "0600000000",
                10L,
                ClientType.PERSONAL
        );

        when(advisorRepository.findById(10L)).thenReturn(Optional.of(advisor));

        Client savedClient = new Client(
                request.lastName(),
                request.firstName(),
                request.address(),
                request.postalCode(),
                request.city(),
                request.phone()
        );
        savedClient.setType(ClientType.PERSONAL);
        savedClient.setAdvisor(advisor);

        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);

        ClientResponse response = clientService.create(request);

        assertThat(response.advisorId()).isEqualTo(10L);
    }
}
