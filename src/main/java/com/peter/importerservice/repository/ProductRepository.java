package com.peter.importerservice.repository;

import com.peter.importerservice.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByBrandIdAndIdentifierValue(Long brandId, String identifierValue);

    Product findByBrandIdAndIdentifierValue(Long brandId, String identifierValue);
}
