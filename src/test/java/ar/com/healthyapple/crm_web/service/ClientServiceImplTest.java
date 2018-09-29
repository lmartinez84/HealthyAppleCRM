package ar.com.healthyapple.crm_web.service;

import ar.com.healthyapple.crm_web.exceptions.AlreadyExistException;
import ar.com.healthyapple.crm_web.exceptions.NotFoundException;
import ar.com.healthyapple.crm_web.model.Client.Client;
import ar.com.healthyapple.crm_web.model.Product;
import ar.com.healthyapple.crm_web.model.Sale.Sale;
import ar.com.healthyapple.crm_web.repository.Client.ClientRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ClientServiceImplTest {
    private static final Long ID = 1L;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @TestConfiguration
    static class ServiceServiceTestContextConfiguration {

        @Bean
        public ClientService clientService(ClientRepository clientRepository) {
            return new ClientServiceImpl(clientRepository);
        }
    }

    @Autowired
    private ClientService clientService;

    @MockBean
    private ClientRepository clientRepository;

    @MockBean
    private List<Product> products;

    @MockBean
    private List<Sale> services;


    private Client clientRequest;

    private Client clientResponse;


    @Before
    public void setUp() throws Exception {
        clientRequest = new Client(1111123123213L, LocalDate.of(2018, 2, 1), "Juan", "Perez", "jp@gmail.com", "Lalal", products, services);
        clientResponse = new Client(1111123123213L, LocalDate.of(2018, 2, 1), "Juan", "Perez", "jp@gmail.com", "Lalal", products, services);
    }

    @Test
    public void create() throws AlreadyExistException {
        when(clientRepository.findById(clientRequest.getMobile()))
                .thenReturn(Optional.empty());
        when(clientRepository.save(clientRequest)).thenReturn(clientResponse);
        Client result = clientService.create(clientRequest);

        assertThat(result).isEqualTo(clientResponse);
    }

    @Test
    public void createDuplicateShouldThrowAlreadyExistsException() throws Exception {
        thrown.expect(AlreadyExistException.class);
        when(clientRepository.findById(clientRequest.getMobile()))
                .thenReturn(Optional.of(clientResponse));
        clientService.create(clientRequest);
    }

    @Test
    public void read() {
        when(clientRepository.findById(clientRequest.getMobile())).thenReturn(Optional.of(clientResponse));
        Client result = clientService.read(clientRequest.getMobile());
        assertThat(result).isEqualTo(clientResponse);
    }

    @Test
    public void readByNonExistentIdShouldThrowNotFoundException() throws Exception {
        thrown.expect(NotFoundException.class);
        when(clientRepository.findById(ID)).thenReturn(Optional.empty());
        Client result = clientService.read(ID);
    }

    @Test
    public void update() {
        when(clientRepository.findById(clientRequest.getMobile())).thenReturn(Optional.of(clientResponse));
        Client result = clientService.read(clientRequest.getMobile());
        assertThat(result).isEqualTo(clientResponse);
    }

    @Test
    public void updateNonExistentResourceThrowsNotFoundException() {
        thrown.expect(NotFoundException.class);
        when(clientRepository.findById(ID)).thenReturn(Optional.of(clientResponse));
        Client result = clientService.read(clientRequest.getMobile());
        assertThat(result).isEqualTo(clientResponse);
    }

    @Test
    public void deleteByMobile() {
        when(clientRepository.findById(clientRequest.getMobile())).thenReturn(Optional.of(clientResponse));
        doNothing().when(clientRepository).deleteById(clientRequest.getMobile());
        clientService.deleteByMobile(clientRequest.getMobile());
    }

    @Test
    public void deleteByNonExistentIdShouldThrowNotFoundException() throws NotFoundException {
        thrown.expect(NotFoundException.class);
        when(clientRepository.findById(ID)).thenReturn(Optional.empty());
        doNothing().when(clientRepository).deleteById(clientRequest.getMobile());
        clientService.deleteByMobile(ID);
    }

    @Test
    public void delete() {
        when(clientRepository.findById(clientRequest.getMobile())).thenReturn(Optional.of(clientResponse));
        doNothing().when(clientRepository).delete(clientRequest);
        clientService.deleteByMobile(clientRequest.getMobile());
    }

    @Test
    public void deleteByNonExistentResourceShouldThrowNotFoundException() throws Exception {
        thrown.expect(NotFoundException.class);
        when(clientRepository.findById(ID)).thenReturn(Optional.empty());
        doNothing().when(clientRepository).delete(clientRequest);
        clientService.delete(clientRequest);
    }

    @Test
    public void findByNameOrLastName() {
//        when(clientRepository.findByFirstNameContainingAndLastNameContaining(clientRequest.getFirstName().toLowerCase(), PageRequest.of(0, 1))).thenReturn(Arrays.asList(clientResponse));
//        List<Client>  clients = clientService.findByFirstNameOrLastName(clientRequest.getFirstName().toLowerCase(), 1, 1);
//        assertThat(clients).containsExactlyInAnyOrder(clientResponse);
    }

}