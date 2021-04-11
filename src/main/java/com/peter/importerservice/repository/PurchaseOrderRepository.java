package com.peter.importerservice.repository;

import com.peter.importerservice.domain.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    boolean existsByIdNotAndReferenceAndBrandId(Long id, String reference, Long brandId);

    boolean existsByReferenceAndBrandId(String reference, Long brandId);
}
