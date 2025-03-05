package de.aittr.g_52_shop.controller;


import de.aittr.g_52_shop.domain.dto.CustomerDto;

import de.aittr.g_52_shop.domain.entity.Role;
import de.aittr.g_52_shop.repository.ProductRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import javax.crypto.SecretKey;


import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerControllerTestIT {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${key.access}")
    private String accessPhrase;

    @Autowired
    private ProductRepository repository;

    private final String BEARER_PREFIX = "Bearer ";
    private CustomerDto testCustomer;
    private String adminAccessToken;
    private SecretKey accessKey;
    private CustomerDto createdCustomer;

    @BeforeEach
    public void setUp() {
        accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessPhrase));
        adminAccessToken = generateAdminAccessToken();
        testCustomer = createTestCustomer();
    }


    @Test
    @Order(1)
    public void checkRequestForAllCustomer() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, adminAccessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<CustomerDto[]> response = restTemplate.exchange("/customer/all", HttpMethod.GET, request, CustomerDto[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Unexpected http status");
        assertNotNull(response.getBody(), "Response body should not be null");

        for (CustomerDto customer : response.getBody()) {
            assertNotNull(customer.getId(), "Customer id should not be null");
            assertNotNull(customer.getName(), "Customer Name should not be null");
        }

    }

    @Test
    @Order(2)
    public void checkSuccessWhileSavingCustomerWithAdminToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, adminAccessToken);
        HttpEntity<CustomerDto> request = new HttpEntity<>(testCustomer, headers);
        ResponseEntity<CustomerDto> response = restTemplate.exchange("/customer", HttpMethod.POST, request, CustomerDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Unexpected http status");
        assertNotNull(response.getBody(), "Saved Customer should not be null");

        createdCustomer = response.getBody();
        assertNotNull(createdCustomer.getId(), "Saved Customer id should not be null");
        assertEquals(testCustomer.getName(), createdCustomer.getName(), "Saved customer has incorrect name");

        // Удаление тестового пользователя после создания
        deleteTestCustomer(createdCustomer.getName());
    }

    @Test
    @Order(3)
    public void checkSuccessDeleteCustomerByNameWithAdminToken() {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, adminAccessToken);
        HttpEntity<CustomerDto> request = new HttpEntity<>(testCustomer, headers);
        ResponseEntity<CustomerDto> response = restTemplate.exchange("/customer", HttpMethod.POST, request, CustomerDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Unexpected http status");
        assertNotNull(response.getBody(), "Saved Customer should not be null");

        createdCustomer = response.getBody();
        assertNotNull(createdCustomer.getId(), "Saved Customer id should not be null");
        assertEquals(testCustomer.getName(), createdCustomer.getName(), "Saved customer has incorrect name");

        // Удаление клиента по имени
        HttpEntity<Void> deleteRequest = new HttpEntity<>(headers);
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/customer/by-name/{name}", HttpMethod.DELETE, deleteRequest, Void.class, createdCustomer.getName()
        );
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode(), "Unexpected http status while deleting customer");

        // Получение всех клиентов
        ResponseEntity<CustomerDto[]> allCustomersResponse = restTemplate.exchange(
                "/customer/all", HttpMethod.GET, new HttpEntity<>(headers), CustomerDto[].class
        );
        assertEquals(HttpStatus.OK, allCustomersResponse.getStatusCode(), "Unexpected http status while retrieving all customers");

        // Проверка, что удалённый клиент отсутствует в списке
        boolean customerFound = false;
        for (CustomerDto customer : allCustomersResponse.getBody()) {
            if (customer.getName().equals(createdCustomer.getName())) {
                customerFound = true;
                break;
            }
        }

        assertFalse(customerFound, "Deleted customer should not be found in the list");
    }

    private void deleteTestCustomer(String name) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, adminAccessToken);
        HttpEntity<Void> deleteRequest = new HttpEntity<>(headers);

        // Удаление клиента по имени
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/customer/by-name/{name}", HttpMethod.DELETE, deleteRequest, Void.class, name
        );
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode(), "Unexpected http status while deleting customer");
    }

    private CustomerDto createTestCustomer() {
        CustomerDto customer = new CustomerDto();
        customer.setName("Mick Jagger");
        return customer;
    }

    private String generateAdminAccessToken() {
        Role role = new Role();
        role.setTitle("ROLE_ADMIN");

        return BEARER_PREFIX + Jwts.builder()
                .subject("TestAdmin")
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .claim("roles", Set.of(role))
                .signWith(accessKey)
                .compact();
    }


}