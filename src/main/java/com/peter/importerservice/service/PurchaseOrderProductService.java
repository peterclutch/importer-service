package com.peter.importerservice.service;

import com.peter.importerservice.domain.PurchaseOrderProduct;
import com.peter.importerservice.repository.PurchaseOrderProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Slf4j
@AllArgsConstructor
public class PurchaseOrderProductService {

    private final PurchaseOrderProductRepository purchaseOrderProductRepository;

    public Optional<PurchaseOrderProduct> getOne(Long purchaseOrderProductId) {
        try {
            return Optional.of(purchaseOrderProductRepository.getOne(purchaseOrderProductId));
        } catch (EntityNotFoundException e) {
            log.info(
                    "Attempting to get the purchaseOrderProduct %d but it does not exists",
                    purchaseOrderProductId);
            return Optional.empty();
        }
    }
}
