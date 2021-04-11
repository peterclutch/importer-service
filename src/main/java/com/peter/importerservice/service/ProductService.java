package com.peter.importerservice.service;

import com.peter.importerservice.domain.Product;
import com.peter.importerservice.repository.ProductRepository;
import com.peter.importerservice.util.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Optional<Product> getOne(Long productId) {
        try {
            return Optional.of(productRepository.getOne(productId));
        } catch (EntityNotFoundException e) {
            log.info("Attempting to get the product %d but it does not exists", productId);
            return Optional.empty();
        }
    }

    public Product findByName(String identifierValue) {
        return productRepository.findByBrandIdAndIdentifierValue(
                SecurityUtils.getCurrentUserBrandWhenAuthorized(), identifierValue);
    }
}
