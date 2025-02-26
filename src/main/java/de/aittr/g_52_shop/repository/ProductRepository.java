package de.aittr.g_52_shop.repository;

import de.aittr.g_52_shop.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
