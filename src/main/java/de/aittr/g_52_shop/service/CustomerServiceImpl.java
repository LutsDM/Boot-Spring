package de.aittr.g_52_shop.service;

import de.aittr.g_52_shop.domain.dto.CustomerDto;
import de.aittr.g_52_shop.domain.entity.Customer;
import de.aittr.g_52_shop.domain.entity.Product;
import de.aittr.g_52_shop.exception_handling.exceptions.CustomerNotFoundException;
import de.aittr.g_52_shop.exception_handling.exceptions.CustomerValidationException;
import de.aittr.g_52_shop.repository.CustomerRepository;
import de.aittr.g_52_shop.service.interfaces.CustomerService;
import de.aittr.g_52_shop.service.mapping.CustomerMappingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final CustomerMappingService mappingService;

    public CustomerServiceImpl(CustomerRepository repository, CustomerMappingService mappingService) {
        this.repository = repository;
        this.mappingService = mappingService;
    }

    @Override
    public CustomerDto save(CustomerDto dto) {
        try {
            Customer entity = mappingService.mapDtoToEntity(dto);
            entity = repository.save(entity);
            return mappingService.mapEntityToDto(entity);
        } catch (Exception e) {
            throw new CustomerValidationException(e);
        }
    }

    @Override
    public List<CustomerDto> getAllActiveCustomers() {
        return repository.findAll()
                .stream()
                .filter(Customer::isActive)
                .map(mappingService::mapEntityToDto)
                .toList();
    }

    @Override
    public CustomerDto getById(Long customerId) {
//        Customer customer = repository.findById(customerId).orElse(null);
//
//        if (customer == null || !customer.isActive()){
//            return null;
//        }
//        return mappingService.mapEntityToDto(customer);
        return mappingService.mapEntityToDto(repository.findById(customerId)
                .filter(Customer::isActive)
                .orElseThrow(() -> new CustomerNotFoundException(customerId)));
    }

    @Override
    public void update(CustomerDto customer) {

    }

    @Override
    public void deleteById(Long customerId) {

    }
    @Transactional
    @Override
    public void deleteByName(String name) {
        List<Customer> customers = repository.findByName(name);
        customers.forEach(customer -> {
            customer.setActive(false); // Мягкое удаление
            repository.save(customer);
        });
    }

    @Override
    public void restoreById(Long customerId) {

    }

    @Override
    public long getAllActiveCustomerTotalCount() {
        return repository.findAll()
                .stream()
                .filter(Customer::isActive)
                .count();
    }

    @Override
    public BigDecimal getTotalPriceInCartByProductId(Long productId) {
        return null;
    }

    @Override
    public BigDecimal getAveragePriceInCartByProductId() {
        return null;
    }

    @Override
    public Product saveProductByProductIdInCartByCustomerId(Long productId, Long customerId) {
        return null;
    }

    @Override
    public void deleteProductByProductIdFromCartByCustomerId(Long productId, Long customerId) {

    }

    @Override
    public void deleteAllFromCartByCustomerId(Long customerId) {

    }
}
