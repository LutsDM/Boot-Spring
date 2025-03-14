package de.aittr.g_52_shop.repository;

import de.aittr.g_52_shop.domain.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByName(String name);


}
